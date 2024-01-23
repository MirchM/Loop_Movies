package com.mmisoft.loop_movies.ui.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mmisoft.loop_movies.data.model.local.User
import com.mmisoft.loop_movies.data.model.remote.AuthenticationState
import com.mmisoft.loop_movies.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    @ApplicationContext val application: Context,
    private val repository: UserRepository
) :
    ViewModel() {


    private val auth: FirebaseAuth = Firebase.auth

    private val _authenticationState =
        MutableLiveData<AuthenticationState>(AuthenticationState.Unauthenticated)
    val authenticationState: LiveData<AuthenticationState> get() = _authenticationState

    private val _user = MutableLiveData<User>(User("", listOf()))
    val user: LiveData<User> = _user

    private val _newProfilePictureEvent = MutableLiveData<Boolean>(false)
    val newProfilePictureEvent: LiveData<Boolean> = _newProfilePictureEvent

    // User Data Store
    fun saveUserName(newUserName: String) {
        viewModelScope.launch {
            repository.saveUserName(newUserName)
        }
        getUserName()
    }

    fun getUserName() =
        viewModelScope.launch() {
            repository.getUserName()?.let { userName ->
                _user.value?.let { user ->
                    _user.postValue(User(userName, user.favouriteMovies))
                }
            }
        }

    fun saveUserFavouriteMovies(newBookmarkedMovie: Int) {
        viewModelScope.launch {
            if (_user.value?.favouriteMovies?.indexOf(newBookmarkedMovie) == -1) {
                _user.value?.let {
                    repository.saveFavouriteMovies(
                        serializeListOfIntegers(
                            listOf(
                                newBookmarkedMovie
                            ) + it.favouriteMovies
                        )
                    )
                }
            } else {
                _user.value?.let {
                    repository.saveFavouriteMovies(
                        serializeListOfIntegers(
                            it.favouriteMovies.filter { movie -> movie != newBookmarkedMovie }
                        )
                    )
                }
            }
            getUserFavouriteMovies()
        }
    }


    fun getUserFavouriteMovies() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUserFavouriteMovies()?.let { favouriteMovies ->
                _user.value?.let { user ->
                    _user.postValue(User(user.name, deserializeListOfIntegers(favouriteMovies)))
                }
            }
        }

    fun fetchUser() =
        viewModelScope.launch(Dispatchers.IO) {
            val fetchedUser = repository.fetchUser()
            if (fetchedUser.second != null) {
                fetchedUser.second?.let { favouriteMoves ->
                    fetchedUser.first?.let { userName ->
                        _user.postValue(User(userName, deserializeListOfIntegers(favouriteMoves)))
                    }
                }
            } else {
                fetchedUser.first?.let { userName ->
                    _user.postValue(_user.value?.favouriteMovies?.let { User(userName, it) })
                }
            }
        }

    // Helpers for storing the list of favourite Movies
    private fun deserializeListOfIntegers(serializedList: String): List<Int> {
        val gson = Gson()
        return gson.fromJson<List<Int>>(serializedList, object : TypeToken<List<Int>>() {}.type)
    }

    private fun serializeListOfIntegers(list: List<Int>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    // Authentication
    fun checkIfUserLoggedIn() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentUser = auth.currentUser
            val authenticationState = if (currentUser != null) {
                AuthenticationState.Authenticated
            } else {
                AuthenticationState.Unauthenticated
            }
            _authenticationState.postValue(authenticationState)
        }
    }

    fun createNewUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        _authenticationState.postValue(AuthenticationState.Authenticated)
                    }
                    .addOnFailureListener {
                        _authenticationState.postValue(AuthenticationState.RegistrationFailed)
                    }
            } catch (e: Exception) {
                Log.e("AuthenticationViewModel", "Error registering: ${e.message}")
                _authenticationState.postValue(AuthenticationState.RegistrationFailed)
            }
        }
    }

    // Not needed at the moment
    private fun signInUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        _authenticationState.postValue(AuthenticationState.Authenticated)
                    }
                    .addOnFailureListener {
                        _authenticationState.postValue(AuthenticationState.AuthenticationFailed)
                    }
            } catch (e: Exception) {
                Log.e("AuthenticationViewModel", "Error signing in: ${e.message}")
                _authenticationState.postValue(AuthenticationState.AuthenticationFailed)
            }
        }
    }

    // Storing/Modifying User Profile Picture
    fun saveImage(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            var outputStream: FileOutputStream? = null
            try {
                outputStream = FileOutputStream(
                    File(
                        application.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                        "profilePicture.jpg"
                    )
                )

                val imageBitmap = getCapturedImage(uri)
                val byteArrayOutputStream = ByteArrayOutputStream()
                imageBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                val imageBytes = byteArrayOutputStream.toByteArray()

                outputStream.write(imageBytes)
                outputStream.flush()
                outputStream.close()

                val imageFile = File(
                    application.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "profilePicture.jpg"
                )
                if (imageFile.exists()) {
                    // Image exists, proceed to set it to the ImageView
                    Log.d("PROFILE_PICTURE", "EXISTS")
                } else {
                    // Image doesn't exist yet, don't set it to the ImageView
                    Log.d("PROFILE_PICTURE", "DOESN'T EXIST")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                outputStream?.close()
            }
            _newProfilePictureEvent.value?.let { _newProfilePictureEvent.postValue(!it) }
        }
    }

    private fun getCapturedImage(selectedPhotoUri: Uri): Bitmap? {
        val contentResolver = application.contentResolver
        contentResolver?.let { it ->
            val bitmap = when {
                Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                    it,
                    selectedPhotoUri
                )

                else -> {
                    val source = ImageDecoder.createSource(it, selectedPhotoUri)
                    ImageDecoder.decodeBitmap(source)
                }
            }
            return bitmap
        }
        return null
    }

    private fun deleteImage() {
        val imageFile = File(
            application.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "profilePicture.jpg"
        )
        if (imageFile.exists()) {
            imageFile.delete()
        }
    }
}