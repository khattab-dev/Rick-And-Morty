package com.slayer.rickandmorty.ui.fragments.locations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.slayer.rickandmorty.adapters.LocationsAdapter
import com.slayer.rickandmorty.databinding.FragmentLocationsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationsFragment : Fragment() {
    private var _binding: FragmentLocationsBinding? = null
    private val binding get() = _binding!!

    private val vm : LocationsViewModel by viewModels()
    private val adapter = LocationsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationsBinding.inflate(inflater, container, false)

        binding.rvLocations.adapter = adapter

        observeLocationsPagingData()

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
}