package com.slayer.rickandmorty.ui.fragments.auth.forget_password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.slayer.rickandmorty.R
import com.slayer.rickandmorty.core.isValidEmailAddress
import com.slayer.rickandmorty.core.safeCall
import com.slayer.rickandmorty.databinding.DialogResetMailSentBinding
import com.slayer.rickandmorty.databinding.FragmentForgetPasswordBinding
import com.slayer.rickandmorty.ui.dialogs.DefaultDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgetPasswordFragment : Fragment() {
    private val TAG = this.javaClass.simpleName

    private var _binding: FragmentForgetPasswordBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ForgetPasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)

        observeLoadingState()

        handleForgetButtonClick()

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
                    btnForget.isEnabled = !it
                }
            }
        }
    }

    private fun handleForgetButtonClick() {
        binding.apply {
            btnForget.setOnClickListener {
                val email = containerEmail.editText?.text.toString().trim().lowercase()

                if (email.isEmpty()) {
                    containerEmail.error =
                        getString(R.string.email_address_is_required_please_enter_a_valid_email)
                    return@setOnClickListener
                }

                if (!isValidEmailAddress(email)) {
                    containerEmail.error =
                        getString(R.string.invalid_email_address_please_enter_a_valid_email)
                    return@setOnClickListener
                }

                tryForgetPassword(email)
            }
        }
    }

    private fun tryForgetPassword(email: String) {
        safeCall(requireContext()) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.setLoadingValue(true)
                viewModel.forgetPassword(email)
                viewModel.setLoadingValue(false)

                showConfirmDialog()
            }
        }
    }

    private fun showConfirmDialog() {
        val dialogResetMailSentBinding = DialogResetMailSentBinding.inflate(
            LayoutInflater.from(requireContext())
        )

        val dialog = DefaultDialog(requireContext(), dialogResetMailSentBinding.root)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()

        dialogResetMailSentBinding.btnConfirm.setOnClickListener {
            dialog.dismiss()
            findNavController().navigateUp()
        }
    }
}