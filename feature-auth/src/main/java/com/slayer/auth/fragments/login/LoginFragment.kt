package com.slayer.auth.fragments.login

import android.app.Activity
import android.content.Intent
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
import com.slayer.auth.R
import com.slayer.auth.databinding.FragmentLoginBinding
import com.slayer.common.createSpannableString
import com.slayer.common.isValidEmailAddress
import com.slayer.common.printToLog
import com.slayer.common.safeCall
import com.slayer.common.toast
import com.slayer.login.fragments.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val TAG = this.javaClass.simpleName

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

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
                            relaunchAfterLogin()
                        }
                    }
                } catch (e: ApiException) {
                    e.stackTraceToString().printToLog()
                }
            }

            viewModel.setLoadingValue(false)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeGso()

        initializeGoogleSignInClient()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        observeLoadingState()

        setupSignUpTextColor()

        handleLoginBtnClick()
        handleGoogleBtnClick()
        handleCreateAccountClick()
        handleForgetPasswordClick()

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
                    btnLogin.isEnabled = !it
                    btnGoogle.isEnabled = !it
                }
            }
        }
    }

    private fun initializeGoogleSignInClient() {
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
    }

    private fun initializeGso() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1045840464449-bmvm00171ma15ssb4at527358jp0l6ht.apps.googleusercontent.com")
            .requestEmail()
            .build()
    }

    private fun setupSignUpTextColor() {
        binding.tvCreateAccount.text = createSpannableString(
            start = 20,
            end = 27,
            text = getString(R.string.don_t_have_account_sign_up),
            context = requireContext()
        )
    }

    private fun handleGoogleBtnClick() {
        binding.btnGoogle.setOnClickListener {
            safeCall(requireContext()) {
                viewModel.setLoadingValue(true)
                googleSignInLauncher.launch(googleSignInClient.signInIntent)
            }
        }
    }

    private fun handleCreateAccountClick() {
        binding.tvCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun handleForgetPasswordClick() {
        binding.tvForget.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
        }
    }

    private fun handleLoginBtnClick() {
        binding.apply {
            btnLogin.setOnClickListener {
                val email = etEmail.text.toString().trim().lowercase()
                val password = etPassword.text.toString().trim()

                if (email.isEmpty()) {
                    containerEmail.error =
                        getString(R.string.email_address_is_required_please_enter_a_valid_email)
                }

                if (!isValidEmailAddress(email)) {
                    containerEmail.error =
                        getString(R.string.invalid_email_address_please_enter_a_valid_email)
                    return@setOnClickListener
                }

                tryLogin(email, password)
            }
        }
    }

    private fun tryLogin(email: String, password: String) {
        safeCall(requireContext()) {
            lifecycleScope.launch {
                viewModel.apply {
                    setLoadingValue(true)
                    tryLoginWithEmailAndPassword(email, password)

                    if (loginResult.value?.user != null) {
                        relaunchAfterLogin()
                    } else {
                        toast(handleSignInWithEmailAndPasswordException())
                    }

                    setLoadingValue(false)
                }
            }
        }
    }

    private fun relaunchAfterLogin() {
        val intent = Intent(requireContext(), requireActivity().javaClass).apply {
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        startActivity(intent)
    }
}