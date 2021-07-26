package net.ferraSolution.food.ui.bottomTabs.tools

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_tools.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.ferraSolution.food.R
import net.ferraSolution.food.base.BaseSupportFragment
import net.ferraSolution.food.data.models.AddonModel
import net.ferraSolution.food.data.models.UserModel
import net.ferraSolution.food.ui.HomeActivity
import net.ferraSolution.food.ui.user.login.LoginFragmentViewModel
import net.ferraSolution.food.utils.Constants
import org.koin.android.viewmodel.ext.android.viewModel


class ToolsFragment : BaseSupportFragment(R.layout.fragment_tools) {

    override val viewModel by viewModel<ToolsFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAppBarVisibilityAndTitle(View.VISIBLE, R.string.tools)
        navigationVisibility = View.VISIBLE
        initViews()

        addCallBackToExit()

    }

    private fun initViews(){
        val user = Constants().getUser(requireContext())
        your_name_tv.text = user?.firstName?.plus(user.lastName)
        phone_number_tv.text = user?.phoneNumber
        email_tv.text = user?.email

        sign_out_tv.setOnClickListener {
            val uid = Constants().getUid(requireContext())
            viewModel.launch(Dispatchers.IO) { viewModel.cartItemDAO.cleanCart(uid = uid?:"") }
            Constants().apply {
                setUid(requireContext(), "")
                setUser(requireContext(), UserModel())
                firstTime(requireContext(), true)
                setAddons(requireContext(), listOf())
                setSize(requireContext(), listOf())
            }
            viewModel.signOutUser()
            navController.navigate(ToolsFragmentDirections.actionNavToolsFragmentToNavSignInFragment())
        }
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