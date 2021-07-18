package net.ferraSolution.food.ui.bottomTabs.tools

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import kotlinx.android.synthetic.main.activity_home.*
import net.ferraSolution.food.R
import net.ferraSolution.food.base.BaseSupportFragment
import net.ferraSolution.food.ui.HomeActivity
import net.ferraSolution.food.ui.user.login.LoginFragmentViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class ToolsFragment : BaseSupportFragment(R.layout.fragment_tools) {

    override val viewModel by viewModel<ToolsFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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