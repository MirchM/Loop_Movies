package com.mmisoft.loop_movies.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mmisoft.loop_movies.R

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Initialize Firebase Auth
        auth = Firebase.auth

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        // navController.navigate(R.id.action_homeFragment_to_allMoviesFragment)


        // RecyclerView
        /*val recyclerView = findViewById<RecyclerView>(R.id.horizontal_movie_image_recyclerview)
        val horizontalLayoutManager =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = horizontalLayoutManager
        val adapter = RecyclerViewHorizontalAdapter()
        adapter.setData(
            listOf<RecyclerViewDataItem>(
                RecyclerViewDataItem.ImageViewItem("https://image.tmdb.org/t/p/w500/x21s3p5wPww534nYj1cWakTcqz4.jpg"),
                RecyclerViewDataItem.ImageViewItem("https://image.tmdb.org/t/p/w500/vQtBqpF2HDdzbfXHDzR4u37i1Ac.jpg"),
                RecyclerViewDataItem.ButtonItem {
                    Log.d("TAG123", "CLICKED")
                    val fullScreenMovieDetail =
                        FullscreenModalMovieBottomSheetDialog(Movie(
                            rating = 3.9870000000000005,
                            id = 530915,
                            revenue = 374733942,
                            releaseDate = "2019-12-25",
                            director = null,
                            posterUrl = "https://image.tmdb.org/t/p/w500/iZf0KyrE25z1sage4SYFLCCrMi9.jpg",
                            cast = null,
                            runtime = 119,
                            title = "1917",
                            overview = "At the height of the First World War, two young British soldiers must cross enemy territory and deliver a message that will stop a deadly attack on hundreds of soldiers.",
                            reviews = 9932,
                            budget = 100000000,
                            language = "en",
                            genres = arrayListOf("War", "Drama", "Action", "Thriller", "History")
                        ))
                    supportFragmentManager.let {
                        fullScreenMovieDetail.show(
                            it,
                            "fullScreenMovieDetailDialog"
                        )
                    }
                }
            )
        )
        recyclerView.adapter = adapter*/

    }

    override fun onStart() {
        super.onStart()
        //checkIfUserLoggedIn()
        //createNewUser("mmisoft.developer+Loop_Movies@gmail.com", "asd123312")
        checkIfUserLoggedIn()
        getCurrentUserUID()
    }

    private fun checkIfUserLoggedIn() {
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Log.d("FIREBASE_CUSTOM", "LOGGED IN")
        }else{
            Log.d("FIREBASE_CUSTOM", "NOT LOGGED IN")
        }
    }

    private fun createNewUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("FIREBASE_CUSTOM", "createUserWithEmail:success")
                    val user = auth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("FIREBASE_CUSTOM", "createUserWithEmail:failure", task.exception)
                }
            }
    }

    private fun signInUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("FIREBASE_CUSTOM", "signInWithEmail:success")
                    val user = auth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("FIREBASE_CUSTOM", "signInWithEmail:failure", task.exception)
                }
            }
    }

    private fun getCurrentUserUID(): String? {
        val user = Firebase.auth.currentUser
        user?.let {
            // Name, email address, and profile photo Url
            val name = it.displayName
            val email = it.email
            Log.d("FIREBASE_CUSTOM", "Current User Email: $email")
            val photoUrl = it.photoUrl

            // Check if user's email is verified
            val emailVerified = it.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            val uid = it.uid
            Log.d("FIREBASE_CUSTOM", "Current User UID: $uid")
            return uid
        }
        return null
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_container)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}