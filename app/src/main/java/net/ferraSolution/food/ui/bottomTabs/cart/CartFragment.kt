package net.ferraSolution.food.ui.bottomTabs.cart

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import kotlinx.android.synthetic.main.activity_home.*
import net.ferraSolution.food.R
import net.ferraSolution.food.base.BaseSupportFragment
import net.ferraSolution.food.ui.HomeActivity
import org.koin.android.viewmodel.ext.android.viewModel

class CartFragment : BaseSupportFragment(R.layout.fragment_orders) {

    override val viewModel by viewModel<CartFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAppBarVisibilityAndTitle(View.VISIBLE, R.string.order)
        navigationVisibility = View.VISIBLE

        addCallBackToExit()
    }

    private fun addCallBackToExit() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callBack)
    }


    private val callBack: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (activity is HomeActivity) {
                val homeActivity = activity as HomeActivity
                navController.navigate(R.id.nav_homeFragment)
                homeActivity.bottomNavigationView.selectedItemId = R.id.navigation_home
            }
        }

    }

}