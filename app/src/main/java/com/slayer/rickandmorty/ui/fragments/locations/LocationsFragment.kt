package com.slayer.rickandmorty.ui.fragments.locations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.slayer.common.goneIf
import com.slayer.common.hideKeyboard
import com.slayer.common.visibleIf
import com.slayer.rickandmorty.adapters.LocationsAdapter
import com.slayer.rickandmorty.databinding.FragmentLocationsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationsFragment : Fragment() {
    private var _binding: FragmentLocationsBinding? = null
    private val binding get() = _binding!!

    private val vm: LocationsViewModel by viewModels()
    private val adapter = LocationsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationsBinding.inflate(inflater, container, false)

        init()

        observeLocationsPagingData()
        observeAdapterLoadingState()

        hideKeyboardOnSearchClick()
        resetSearchOnEndIconClick()

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun observeLocationsPagingData() {
        viewLifecycleOwner.lifecycleScope.launch {
            vm.locationsFlow.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun init() {
        binding.rvLocations.adapter = adapter
    }

    private fun hideKeyboardOnSearchClick() {
        binding.containerSearch.editText?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                vm.submitQuery(v.text.toString())

                hideKeyboard()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun resetSearchOnEndIconClick() {
        binding.containerSearch.apply {
            setEndIconOnClickListener {
                if (editText?.text.isNullOrEmpty()) {
                    hideKeyboard()
                    editText?.clearFocus()
                } else {
                    editText?.text = null
                    vm.submitQuery(null)
                }
            }
        }
    }

    private fun observeAdapterLoadingState() {
        adapter.addLoadStateListener { loadState ->
            val isRefreshing = loadState.refresh is LoadState.Loading

            binding.apply {
                if (isRefreshing) {
                    shimmerLayout.startShimmer()
                } else {
                    shimmerLayout.stopShimmer()
                }

                shimmerLayout visibleIf isRefreshing
                layoutGroup goneIf isRefreshing
            }
        }
    }
}