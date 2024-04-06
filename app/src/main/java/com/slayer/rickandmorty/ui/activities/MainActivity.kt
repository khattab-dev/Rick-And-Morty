package com.slayer.rickandmorty.ui.activities

import android.os.Bundle
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.google.android.material.switchmaterial.SwitchMaterial
import com.slayer.rickandmorty.R
import com.slayer.rickandmorty.core.toast
import com.slayer.rickandmorty.core.visibleIf
import com.slayer.rickandmorty.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnNavigationItemSelectedListener {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val vm: MainActivityViewModel by viewModels()

    private val TAG = MainActivity::class.simpleName

    private lateinit var navController: NavController

    private lateinit var mOnBackPressedCallback: OnBackPressedCallback

    private val mainDestinations = setOf(
        R.id.charactersFragment,
        R.id.locationsFragment
    )

    private var hide = true

    private lateinit var mDarkModeSwitch: SwitchMaterial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setInitialTheme()

        _binding = ActivityMainBinding.inflate(layoutInflater)

        //enableEdgeToEdge()

        setContentView(binding.root)

        mDarkModeSwitch =
            binding.navView.menu.findItem(R.id.drawer_dark_mode).actionView as SwitchMaterial

        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        setupNavController()

        setupBackPressedCallback()
        setupDrawerListener(mOnBackPressedCallback)
        setupAppBar()
        setupNavViews()
        setupDarkModeSwitch()

        binding.navView.setNavigationItemSelectedListener(this)

        observeDestinationChanges()

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }

    private fun setInitialTheme() {
        if (vm.isDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
        }
    }

    private fun setupDarkModeSwitch() {
        mDarkModeSwitch.isChecked = vm.isDarkMode()
        mDarkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            vm.setDarkModeValue(isChecked)

            val value = if (isChecked) MODE_NIGHT_YES else MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(value)
        }
    }

    private fun observeDestinationChanges() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.apply {
                hide = destination.id !in mainDestinations

                toggleAppBar()

                bottomNavigationView visibleIf (destination.id in mainDestinations)
            }
        }
    }

    private fun setupBackPressedCallback() {
        mOnBackPressedCallback = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, mOnBackPressedCallback)
    }

    private fun setupDrawerListener(onBackPressedCallback: OnBackPressedCallback) {
        val drawerListener = object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                // Called when the drawer's position changes due to animation
            }

            override fun onDrawerOpened(drawerView: View) {
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                onBackPressedCallback.isEnabled = true
            }

            override fun onDrawerClosed(drawerView: View) {
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                onBackPressedCallback.isEnabled = false
            }

            override fun onDrawerStateChanged(newState: Int) {
                // Called when the drawer's state changes (e.g., idle, dragging, settling)
            }
        }

        binding.drawerLayout.addDrawerListener(drawerListener)
    }

    private fun setupNavViews() {
        binding.navView.setupWithNavController(navController)
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    private fun setupAppBar() {
        val appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)

        binding.toolbar.setupWithNavController(
            navController,
            appBarConfiguration
        )
    }

    private fun setupNavController() {
        val navHostFragment = binding.fragmentContainerView.getFragment() as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun toggleAppBar() {
        val tv = TypedValue()
        var actionBarHeight = 0

        if (theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight =
                TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
        }

        val lp = binding.appBarLayout.layoutParams as CoordinatorLayout.LayoutParams
        lp.height = if (hide) 0 else actionBarHeight
        binding.appBarLayout.layoutParams = lp
        binding.appBarLayout.setExpanded(!hide, true)
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.drawer_signout -> {
                lifecycleScope.launch {
                    if (vm.tryLogout()) {
                        navController.navigate(R.id.action_charactersFragment_to_loginFragment)
                    } else {
                        toast("Something went wrong, Please try again")
                    }
                }
            }
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}