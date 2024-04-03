package com.slayer.rickandmorty.ui.fragments.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.slayer.domain.models.Character
import com.slayer.rickandmorty.R
import com.slayer.rickandmorty.core.goneIf
import com.slayer.rickandmorty.core.hideKeyboard
import com.slayer.rickandmorty.core.startShimmerIf
import com.slayer.rickandmorty.core.visibleIf
import com.slayer.rickandmorty.databinding.FragmentCharactersBinding
import com.slayer.rickandmorty.epoxy.controllers.CharactersController
import com.slayer.rickandmorty.ui.dialogs.CustomerFilterDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharactersFragment : Fragment() {
    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!

    private val vm: CharactersViewModel by viewModels()

    private val TAG = this::class.simpleName

    private val chars = mutableListOf<Character>()

    private val charactersController by lazy {
        CharactersController {
            if (it.isFavorite) {
                vm.insertCharacterToFav(it)
            } else {
                vm.deleteCharacterToFav(it)
            }
        }
    }

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigateToLoginIfNotAuthenticated()
    }

    private fun navigateToLoginIfNotAuthenticated() {
        auth = FirebaseAuth.getInstance()

        if (auth.currentUser == null) {
            findNavController().navigate(R.id.action_charactersFragment_to_loginFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharactersBinding.inflate(inflater, container, false)

        viewLifecycleOwner.lifecycleScope.launch {
            vm.getRandomCharacters()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            vm.randomCharactersResult.collectLatest {
                it?.let {
                    charactersController.randomCharss.addAll(it)
                    charactersController.requestModelBuild()
                }
            }
        }

        setupRecyclerView()
        observeCustomersPagingData()

        handleSearchInputEndIconClick()
        handleKeyboardSearchBtnClick()
        handleFilterBtnClick()

        observeAdapterLoadingState()

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

    private fun handleFilterBtnClick() {
        binding.btnFilter.setOnClickListener {
            val dialog = CustomerFilterDialog()

            dialog.show(childFragmentManager, dialog.javaClass.simpleName)
        }
    }

    private fun setupRecyclerView() {
        binding.apply {
            val layoutManager = GridLayoutManager(requireContext(), 2)

            rvCharacters.adapter = charactersController.adapter
            rvCharacters.layoutManager = layoutManager

            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position in 0..2) 2 else 1
                }
            }
        }
    }

    private fun observeCustomersPagingData() {
        viewLifecycleOwner.lifecycleScope.launch {
            vm.characterFlow.collectLatest {
                charactersController.submitData(it)
            }
        }
    }

    private fun handleSearchInputEndIconClick() {
        binding.containerSearch.apply {
            setEndIconOnClickListener {
                if (editText?.text.isNullOrEmpty()) {
                    hideKeyboard()
                    editText?.clearFocus()
                } else {
                    editText?.text = null
                    vm.submitQuery(null, vm.getCurrentStatus(), vm.getCurrentGender())
                }
            }
        }
    }

    private fun handleKeyboardSearchBtnClick() {
        binding.containerSearch.editText?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                vm.submitQuery(v.text.toString(), vm.getCurrentStatus(), vm.getCurrentGender())

                hideKeyboard()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun observeAdapterLoadingState() {
        charactersController.addLoadStateListener { loadState ->
            val isRefreshing = loadState.refresh is LoadState.Loading

            binding.apply {
                shimmerLayout.apply {
                    this visibleIf isRefreshing
                    this startShimmerIf isRefreshing
                }

                layoutGroup goneIf isRefreshing
            }
        }
    }
}