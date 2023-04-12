package ghoudan.anfaSolution.com

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import chari.groupewib.com.ui.command.sales.CommandFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import ghoudan.anfaSolution.com.common_ui.appbar.AnfaAppBar
import ghoudan.anfaSolution.com.databinding.ActivityMainBinding
import ghoudan.anfaSolution.com.networking.di.CurrentUserProvider
import ghoudan.anfaSolution.com.utils.AnfaAppNavigator
import ghoudan.anfaSolution.com.utils.LocaleHelper
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    @Inject
    lateinit var localeHelper: LocaleHelper

    @Inject
    lateinit var currentUserProvider: CurrentUserProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)
        setSupportActionBar(binding?.mainAppToolbar?.getToolbar())
        setupViewToolbar(
            AnfaAppBar.AppBarViewConstraints(
                showUnderLine = false
            )
        )

        setupMainNavigation()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }

    override fun onBackPressed() {
        if (navController.currentDestination?.id == R.id.AccountFragment || navController.currentDestination?.id == R.id.loginFragment) {
            finishAffinity()
        } else
            super.onBackPressed()
    }

    fun handleNoContent(isNoContent: Boolean) {
        if (isNoContent) {
            binding?.noDataView?.visibility = View.VISIBLE
        } else {
            binding?.noDataView?.visibility = View.GONE
        }
    }

    fun enableView(isEnabled: Boolean) {
    }


    fun setupViewToolbar(
        constraints: AnfaAppBar.AppBarViewConstraints,
        searchNavDirection: NavDirections? = null,
        profileNavDirection: NavDirections? = null,
    ): AnfaAppBar? {
        return binding?.mainAppToolbar?.setupAppBar(constraints) { appBarCallbackAction ->
            when (appBarCallbackAction) {
                AnfaAppBar.AppBarAction.SHOW_SEARCH -> {
                    searchNavDirection?.let {
                        AnfaAppNavigator.navigate(
                            navController = navController,
                            action = searchNavDirection
                        )
                    }

                }
                AnfaAppBar.AppBarAction.SHOW_PROFILE -> {
                    profileNavDirection?.let {
                        AnfaAppNavigator.navigate(
                            navController = navController,
                            action = profileNavDirection
                        )
                    }
                }
                AnfaAppBar.AppBarAction.SHOW_SUPPORT_POPUP -> {
                }
                AnfaAppBar.AppBarAction.SHOW_DONE -> {
                    AnfaAppNavigator.navigate(
                        navController = navController,
                        action = CommandFragmentDirections.actionCommandFragmentToCommandDoneFragment()
                    )
                }
            }
        }
    }

    private fun setupMainNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val navGraph = navController.navInflater.inflate(R.navigation.navigation)

        //go to AccountFragment if the user is previously logged in, otherwise show loginFragment
//        if (currentUserProvider.currentUser() != null)
//            navGraph.setStartDestination(R.id.AccountFragment)
//        else
            navGraph.setStartDestination(R.id.loginFragment)


        navController.graph = navGraph

        binding?.mainNavigation?.apply {
            setupWithNavController(navController)

            //navigate BottomNavigationView to the correct destination
            setOnItemSelectedListener { item ->
                NavigationUI.onNavDestinationSelected(item, navController)
                navController.popBackStack(destinationId = item.itemId, inclusive = false)
                true
            }
            setOnItemReselectedListener {
                navController.popBackStack(destinationId = it.itemId, inclusive = false)
            }
        }

        //fragment where we don't need the navigation back btn
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.loginFragment,
                R.id.ordersFragment,
                R.id.cartFragment,
                R.id.familiesBrandsFragment,
                R.id.AccountFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding?.mainNavigation?.visibility = View.GONE
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    binding?.mainNavigation?.visibility = View.VISIBLE
                    binding?.mainAppToolbar?.visibility = View.VISIBLE
                }
                R.id.commandFragment, R.id.loginFragment -> {
                    binding?.mainNavigation?.visibility = View.GONE
                    binding?.mainAppToolbar?.visibility = View.VISIBLE
                }
                else -> {
                    binding?.mainAppToolbar?.visibility = View.VISIBLE
                }
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        hideSoftKeyboard()
        return navController.navigateUp(appBarConfiguration)
    }

    fun showLoader() {
        enableView(false)
        handleNoContent(false)
        binding?.loaderOverlay?.visibility = View.VISIBLE
    }

    fun hideLoader() {
        enableView(true)
        if (binding?.loaderOverlay?.isVisible == true) {
            binding?.loaderOverlay?.visibility = View.GONE
        }
    }

    fun hideSoftKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (currentFocus != null) inputMethodManager.hideSoftInputFromWindow(
            currentFocus?.windowToken, 0
        )
    }


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.setupLanguage(newBase))
    }
}
