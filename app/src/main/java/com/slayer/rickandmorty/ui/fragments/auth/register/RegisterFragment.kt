package com.slayer.rickandmorty.ui.fragments.auth.register

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.slayer.common.printToLog
import com.slayer.rickandmorty.R
import com.slayer.rickandmorty.core.arePasswordsEqual
import com.slayer.rickandmorty.core.createSpannableString
import com.slayer.rickandmorty.core.isValidEmailAddress
import com.slayer.rickandmorty.core.isValidPasswordLength
import com.slayer.rickandmorty.core.safeCall
import com.slayer.rickandmorty.core.toast
import com.slayer.rickandmorty.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private val TAG = this.javaClass.simpleName

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var gso: GoogleSignInOptions
    private lateinit var googleSignInClient: GoogleSignInClient

    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    lifecycleScope.launch {
                        val account = task.getResult(ApiException::class.java)!!
                        viewModel.tryLoginWithGoogle(account.idToken!!)

                        if (firebaseAuth.currentUser != null) {
                            findNavController().navigate(R.id.action_registerFragment_to_charactersFragment)
                        }
                    }
                } catch (e: ApiException) {
                    "Google sign in failed".printToLog(TAG)
                }
            }

            viewModel.setLoadingValue(false)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()

        initializeGso()

        initializeGoogleSignInClient()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        setupSignUpTextColor()

        handleAlreadyHaveAccountClick()
        handleGoogleBtnClick()
        handleRegisterBtnClick()

        observeLoadingState()

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observeLoadingState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                binding.apply {
                    btnRegister.isEnabled = !it
                    btnGoogle.isEnabled = !it
                }
            }
        }
    }

    private fun handleGoogleBtnClick() {
        binding.btnGoogle.setOnClickListener {
            safeCall(requireContext()) {
                viewModel.setLoadingValue(true)
                googleSignInLauncher.launch(googleSignInClient.signInIntent)
            }
        }
    }

    private fun handleAlreadyHaveAccountClick() {
        binding.tvAlreadyHaveAccount.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun handleRegisterBtnClick() {
        binding.apply {
            btnRegister.setOnClickListener {
                val email = etEmail.text.toString().trim().lowercase()
                val password = etPassword.text.toString().trim()
                val confirmPassword = etConfirmPassword.text.toString().trim()

                if (areValidFields(email, password, confirmPassword)) return@setOnClickListener

                safeCall(requireContext()) {
                    lifecycleScope.launch {
                        viewModel.setLoadingValue(true)
                        viewModel.tryRegister(email, password)

                        if (viewModel.registerResult.value?.user != null) {
                            findNavController().navigate(R.id.action_registerFragment_to_charactersFragment)
                        }
                        else {
                            toast(viewModel.handleSignUpWithEmailAndPasswordException())
                        }

                        viewModel.setLoadingValue(false)
                    }
                }
            }
        }
    }

    private fun areValidFields(
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        binding.apply {
            if (!isValidEmailAddress(email)) {
                containerEmail.error =
                    getString(R.string.invalid_email_address_please_enter_a_valid_email)
                return true
            }

            if (!isValidPasswordLength(password)) {
                containerPassword.error = getString(R.string.invalid_password_length)
                return true
            }

            if (!arePasswordsEqual(password, confirmPassword)) {
                containerConfirmPassword.error = getString(R.string.password_mismatch)
                return true
            }
        }
        return false
    }

    private fun setupSignUpTextColor() {
        binding.tvAlreadyHaveAccount.text = createSpannableString(
            20,
            28,
            getString(R.string.do_you_have_account_sign_in),
            requireContext()
        )
    }

    private fun initializeGoogleSignInClient() {
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
    }

    private fun initializeGso() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(com.firebase.ui.auth.R.string.default_web_client_id))
            .requestEmail()
            .build()
    }
}