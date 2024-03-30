package com.slayer.rickandmorty.ui.activities

import android.os.Bundle
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.slayer.rickandmorty.R
import com.slayer.rickandmorty.core.printToLog
import com.slayer.rickandmorty.core.toast
import com.slayer.rickandmorty.core.visibleIf
import com.slayer.rickandmorty.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


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

    private fun toggleAppBar() {
        // Calculate ActionBar height
        val tv = TypedValue()
        var actionBarHeight = 0

        if (theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
        }

        val lp = binding.appBarLayout.layoutParams as CoordinatorLayout.LayoutParams
        lp.height = if (hide) 0 else actionBarHeight
        binding.appBarLayout.layoutParams = lp
        binding.appBarLayout.setExpanded(!hide, true)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()

        setContentView(binding.root)
        setupNavController()


        setupBackPressedCallback()
        setupDrawerListener(mOnBackPressedCallback)
        setupAppBar()
        setupNavViews()
        binding.bottomNavigationView.setupWithNavController(navController)

        binding.navView.setNavigationItemSelectedListener(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onStart() {
        super.onStart()

        navController.addOnDestinationChangedListener { _, destination, _ ->
            destination.label.printToLog()
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
                // Called when the drawer is fully opened
                onBackPressedCallback.isEnabled = true
            }

            override fun onDrawerClosed(drawerView: View) {
                // Called when the drawer is fully closed
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
    }

    private fun setupAppBar() {
        val appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)

        binding.collapsingToolBar.setupWithNavController(
            binding.toolbar,
            navController,
            appBarConfiguration
        )
    }

    private fun setupNavController() {
        val navHostFragment = binding.fragmentContainerView.getFragment() as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {

        when (p0.itemId) {
            R.id.nav_signout -> {
                if (vm.tryLogout()) {
                    navController.navigate(R.id.action_charactersFragment_to_loginFragment)
                } else {
                    toast("Something went wrong, Please try again")
                }
            }
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}