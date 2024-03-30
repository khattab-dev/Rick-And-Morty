package com.slayer.rickandmorty.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.slayer.rickandmorty.R
import com.slayer.rickandmorty.core.Constants
import com.slayer.rickandmorty.databinding.DialogCustomersFilterBinding
import com.slayer.rickandmorty.ui.fragments.characters.CharactersViewModel

class CustomerFilterDialog : DialogFragment() {
    private var _binding: DialogCustomersFilterBinding? = null
    private val binding get() = _binding!!

    private val vm: CharactersViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogCustomersFilterBinding.inflate(inflater, container, false)

        setInitialValues()

        handleStatusSelection()
        handleGenderSelection()

        handleApplyBtnClick()
        handleResetStatusBtnClick()
        handleResetGenderBtnClick()
        handleResetAllBtnClick()

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val width = (requireContext().resources.displayMetrics.widthPixels * 0.85).toInt()

        requireDialog().window?.apply {
            setLayout(
                width, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawable(null)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

    private fun handleResetAllBtnClick() {
        binding.btnResetAll.setOnClickListener {
            binding.rgStatus.clearCheck()
            binding.rgGender.clearCheck()

            vm.setCurrentStatus(null)
            vm.setCurrentGender(null)
        }
    }

    private fun handleResetGenderBtnClick() {
        binding.btnResetGender.setOnClickListener {
            binding.rgGender.clearCheck()

            vm.setCurrentGender(null)
        }
    }

    private fun handleResetStatusBtnClick() {
        binding.btnClearStatus.setOnClickListener {
            binding.rgStatus.clearCheck()

            vm.setCurrentStatus(null)
        }
    }

    private fun handleApplyBtnClick() {
        binding.btnApply.setOnClickListener {
            dismiss()
            vm.submitQuery(vm.getCurrentSearchValue(), vm.getCurrentStatus(), vm.getCurrentGender())
        }
    }

    private fun handleGenderSelection() {
        binding.rgGender.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_male -> vm.setCurrentGender(Constants.MALE_GENDER)
                R.id.rb_female -> vm.setCurrentGender(Constants.FEMALE_GENDER)
                R.id.rb_genderless -> vm.setCurrentGender(Constants.GENDERLESS_GENDER)
                R.id.rb_unknown_gender -> vm.setCurrentGender(Constants.UNKNOWN_GENDER)
            }
        }
    }

    private fun handleStatusSelection() {
        binding.rgStatus.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_alive -> vm.setCurrentStatus(Constants.ALIVE_STATUS)
                R.id.rb_dead -> vm.setCurrentStatus(Constants.DEAD_STATUS)
                R.id.rb_unknown -> vm.setCurrentStatus(Constants.UNKNOWN_STATUS)
            }
        }
    }

    private fun setInitialValues() {
        when (vm.getCurrentStatus()) {
            Constants.ALIVE_STATUS -> binding.rgStatus.check(R.id.rb_alive)
            Constants.DEAD_STATUS -> binding.rgStatus.check(R.id.rb_dead)
            Constants.UNKNOWN_STATUS -> binding.rgStatus.check(R.id.rb_unknown)
        }

        when (vm.getCurrentGender()) {
            Constants.MALE_GENDER -> binding.rgGender.check(R.id.rb_male)
            Constants.FEMALE_GENDER -> binding.rgGender.check(R.id.rb_female)
            Constants.GENDERLESS_GENDER -> binding.rgGender.check(R.id.rb_genderless)
            Constants.UNKNOWN_GENDER -> binding.rgGender.check(R.id.rb_unknown_gender)
        }
    }
}