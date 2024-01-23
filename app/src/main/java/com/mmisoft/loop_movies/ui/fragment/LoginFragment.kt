package com.mmisoft.loop_movies.ui.fragment

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mmisoft.loop_movies.R
import com.mmisoft.loop_movies.data.model.remote.AuthenticationState
import com.mmisoft.loop_movies.databinding.FragmentLoginBinding
import com.mmisoft.loop_movies.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(layoutInflater)

        userViewModel.authenticationState.observe(viewLifecycleOwner) { state ->
            if (state == AuthenticationState.Authenticated) {
                findNavController().navigateUp()
            }
        }

        binding.profileImagePicker.setOnClickListener {
            pickImage.launch("image/*")
        }
        binding.signupButton.setOnClickListener {
            if (binding.signupNameEditText.text.toString() != "") {
                if (binding.signupConfirmPasswordEditText.text.toString() != binding.signupPasswordEditText.text.toString()) {
                    showConfirmPasswordErrorMessage()
                } else {
                    hideConfirmPasswordErrorMessage()
                    userViewModel.createNewUser(
                        binding.signupEmailEditText.text.toString(),
                        binding.signupPasswordEditText.text.toString()
                    )
                    userViewModel.saveUserName(binding.signupNameEditText.text.toString())
                }
            } else {
                Toast.makeText(
                    context,
                    getText(R.string.signup_no_name_error_toast),
                    Toast.LENGTH_LONG
                ).show()
            }

        }

        userViewModel.newProfilePictureEvent.observe(viewLifecycleOwner) {
            updateImageIfStored()
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LoginFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val pickImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            userViewModel.saveImage(uri)
        }
    }

    private fun updateImageIfStored() {
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

    private fun showConfirmPasswordErrorMessage() {
        val constraintSet = ConstraintSet()
        val constraintLayout = binding.signupConfirmPasswordConstraint

        constraintSet.clone(constraintLayout)
        constraintSet.connect(
            binding.signupConfirmErrorTextview.id,
            ConstraintSet.TOP,
            binding.signupConfirmPasswordEditText.id,
            ConstraintSet.BOTTOM
        )
        constraintSet.connect(
            binding.signupConfirmErrorTextview.id,
            ConstraintSet.BOTTOM,
            constraintLayout.id,
            ConstraintSet.BOTTOM
        )

        constraintSet.applyTo(constraintLayout)
    }

    private fun hideConfirmPasswordErrorMessage() {
        val constraintSet = ConstraintSet()
        val constraintLayout = binding.signupConfirmPasswordConstraint

        constraintSet.clone(constraintLayout)
        constraintSet.clear(binding.signupConfirmErrorTextview.id)
        constraintSet.applyTo(constraintLayout)
        constraintSet.connect(
            binding.signupConfirmErrorTextview.id,
            ConstraintSet.TOP,
            constraintLayout.id,
            ConstraintSet.BOTTOM
        )
        constraintSet.connect(
            binding.signupConfirmPasswordEditText.id,
            ConstraintSet.TOP,
            constraintLayout.id,
            ConstraintSet.TOP
        )
        constraintSet.connect(
            binding.signupConfirmPasswordEditText.id,
            ConstraintSet.BOTTOM,
            constraintLayout.id,
            ConstraintSet.BOTTOM
        )
        constraintSet.applyTo(constraintLayout)
    }
}
