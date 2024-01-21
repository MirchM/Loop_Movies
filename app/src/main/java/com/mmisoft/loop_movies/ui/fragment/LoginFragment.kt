package com.mmisoft.loop_movies.ui.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.mmisoft.loop_movies.databinding.FragmentLoginBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater)

        updateImageIfStored()
        binding.profileImagePicker.setOnClickListener {
            pickImage.launch("image/*")
            updateImageIfStored()
        }
        deleteImage()

        return binding.root
    }

    private fun updateImageIfStored(){
        val imageFile = File(
            activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "profilePicture.jpg"
        )
        if (imageFile.exists()) {
            // Image exists, proceed to set it to the ImageView
            Log.d("PROFILE_PICTURE", "EXISTS")
            binding.profileImagePicker.setImageBitmap(BitmapFactory.decodeFile(imageFile.path))
        } else {
            // Image doesn't exist yet, don't set it to the ImageView
            Log.d("PROFILE_PICTURE", "DOESN'T EXIST")
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private val pickImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            var outputStream: FileOutputStream? = null
            try {
                outputStream = FileOutputStream(
                    File(
                        activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
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
                    activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "profilePicture.jpg"
                )
                if (imageFile.exists()) {
                    // Image exists, proceed to set it to the ImageView
                    Log.d("PROFILE_PICTURE", "EXISTS")
                } else {
                    // Image doesn't exist yet, don't set it to the ImageView
                    Log.d("PROFILE_PICTURE", "DOESN'T EXIST")
                }
                Toast.makeText(this.context, "Image saved successfully", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this.context, "Failed to save image", Toast.LENGTH_SHORT).show()
            } finally {
                outputStream?.close()
            }
        }
    }

    private fun getCapturedImage(selectedPhotoUri: Uri): Bitmap? {
        val contentResolver = activity?.contentResolver
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

    private fun deleteImage(){
        val imageFile = File(
            activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "profilePicture.jpg"
        )
        if (imageFile.exists()) {
            imageFile.delete()
        }
    }
}