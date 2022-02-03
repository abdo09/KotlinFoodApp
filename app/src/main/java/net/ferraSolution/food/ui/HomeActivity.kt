package net.ferraSolution.food.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.toolbar_with_logo.view.*
import kotlinx.coroutines.launch
import net.ferraSolution.food.R
import net.ferraSolution.food.base.BaseSupportActivity
import net.ferraSolution.food.ui.bottomTabs.menu.foodsList.FoodsListFragmentViewModel
import net.ferraSolution.food.utils.Constants
import net.ferraSolution.food.utils.fadeIn
import net.ferraSolution.food.utils.fadeOut
import org.koin.android.ext.android.inject

class HomeActivity : BaseSupportActivity() {
    private val foodsListFragmentViewModel: FoodsListFragmentViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Constants().firstTime(this, true)

        setContentView(R.layout.activity_home)

        setUpNavigation()

        setBottomNavigationVisibility()

        toolbar_layout.go_to_cart_button.setOnClickListener {
            navController.navigate(R.id.nav_cart_fragment)
            bottomNavigationView.selectedItemId = R.id.navigation_orders
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                return@OnCompleteListener
            }
        })

        foodsListFragmentViewModel.launch {
            foodsListFragmentViewModel.countItemInCart(Constants().getUid(this@HomeActivity) ?: "")
                .observe(this@HomeActivity) {
                    if (it ?: 0 > 0) {
                        setCartCount(it ?: 0, View.VISIBLE)
                    } else setCartCount(0, View.GONE)
                }
        }

    }


    @SuppressLint("RestrictedApi")
    fun setBottomNavigationVisibility() {
        // get the reference of the bottomNavigationView and set the visibility.
        when (NavHostFragment.findNavController(foodNavHostFragment).currentDestination?.id) {
            R.id.nav_homeFragment -> {
                bottom_app_bar.fadeIn(250, View.VISIBLE)
            }
            R.id.nav_menuFragment -> {
                bottom_app_bar.fadeIn(250, View.VISIBLE)
            }
            R.id.nav_cart_fragment -> {
                bottom_app_bar.fadeIn(250, View.VISIBLE)
            }
            R.id.nav_toolsFragment -> {
                bottom_app_bar.fadeIn(250, View.VISIBLE)
            }
            R.id.splash_screen -> {
                bottom_app_bar.fadeOut(250, View.GONE)
                app_bar_layout.visibility = View.GONE
            }
            R.id.nav_signInFragment -> {
                bottom_app_bar.fadeOut(250, View.GONE)
                app_bar_layout.visibility = View.GONE
            }
            R.id.nav_signUpFragment -> {
                bottom_app_bar.fadeOut(250, View.GONE)
                app_bar_layout.visibility = View.GONE
            }
            R.id.nav_foodListFragment -> {
                bottom_app_bar.fadeOut(250, View.GONE)
            }
        }
    }

    @SuppressLint("RestrictedApi")
    fun setBottomNavigationVisibility(visibility: Int) {

        // get the reference of the bottomNavigationView and set the visibility.
        if (visibility == View.VISIBLE) {
            bottom_app_bar.fadeIn(250, View.VISIBLE)
        } else if (visibility == View.GONE) {
            bottom_app_bar.fadeOut(250, View.GONE)
        }

    }

    private fun setUpNavigation() {
        bottomNavigationView.background = null

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigation.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {

                R.id.navigation_home -> {
                    findNavController(R.id.foodNavHostFragment).navigate(R.id.nav_homeFragment)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.navigation_menu -> {
                    findNavController(R.id.foodNavHostFragment).navigate(R.id.nav_menuFragment)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.navigation_orders -> {
                    findNavController(R.id.foodNavHostFragment).navigate(R.id.nav_cart_fragment)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.navigation_tools -> {
                    findNavController(R.id.foodNavHostFragment).navigate(R.id.nav_toolsFragment)
                    return@OnNavigationItemSelectedListener true
                }

            }
            return@OnNavigationItemSelectedListener false

        })
    }

    fun setCartCount(counter: Int, visibility: Int) {
        toolbar_layout.items_in_cart_count.visibility = visibility
        toolbar_layout.items_in_cart_count.text = counter.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        Constants().setAddons(this, listOf())
        Constants().firstTime(this, false)
    }
}