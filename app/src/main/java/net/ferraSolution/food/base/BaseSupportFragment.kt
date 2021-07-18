package net.ferraSolution.food.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.toolbar_with_logo.view.*
import net.ferraSolution.food.ui.HomeActivity
import net.ferraSolution.food.R
import net.ferraSolution.food.ui.progress.ProgressDialog
import net.ferraSolution.food.utils.CookieBarConfig
import net.ferraSolution.food.utils.getCustomColor


abstract class BaseSupportFragment(val fragment: Int): Fragment(fragment){

    protected open var navigationVisibility = View.GONE

    //full screen loading dialog
    val progressDialog by lazy { ProgressDialog(activity) }

    //base viewModel for configuring info ,success, failure ,loading ,user login events
    abstract val viewModel: BaseViewModel

    //alert module , controlled through the base viewModel
    private val cookieBarConfig: CookieBarConfig by lazy { CookieBarConfig(requireActivity()) }

    //basic navigation controller for fragments within the app
    val navController by lazy { findNavController() }

    //Fragments who require the user to be logged in should override this with value true
    open var requireLogin = false

    open val isCartButtonShown = true

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //viewModel.userLogged.value = !Constants().getAccessToken(requireContext()).isNullOrBlank()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as HomeActivity).apply {
            activeLocalization()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkNetworkConnectivity(requireContext())

        viewModel.showLoading.observe(viewLifecycleOwner, { showLoading ->
            if (activity != null && !requireActivity().isFinishing) {
                if (showLoading)
                    view.postDelayed({ progressDialog.show() }, 100)
                else
                    view.postDelayed({ progressDialog.dismiss() }, 100)
            }
        })

        viewModel.showInfo.observe(viewLifecycleOwner, { infoMessage ->
            if (infoMessage is String)
                cookieBarConfig.showDefaultInfoCookie(infoMessage)
            else
                context?.resources?.getString(infoMessage as Int)?.let {
                    cookieBarConfig.showDefaultInfoCookie(it)
                }
        })


        viewModel.showSuccess.observe(viewLifecycleOwner, { infoMessage ->
            if (infoMessage is String)
                cookieBarConfig.showDefaultSuccessCookie(infoMessage)
            else
                context?.resources?.getString(infoMessage as Int)?.let {
                    cookieBarConfig.showDefaultSuccessCookie(it)
                }
        })

        viewModel.showError.observe(viewLifecycleOwner, { showError ->
            cookieBarConfig.runCatching {
                viewModel.showLoading.postValue(false)
                if (showError is String) {
                    view.post { this.showDefaultErrorCookie(error = showError) }
                    if (showError.contains("No address associated with hostname")) {
                        val alert =
                            Snackbar.make(view, R.string.network_error, Snackbar.LENGTH_INDEFINITE)
                                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE)
                                .setBackgroundTint(context?.getCustomColor(requireContext(), R.color.orange_700)!!)
                                .setTextColor(context?.getCustomColor(requireContext(), R.color.white)!!)
                        alert.setAction(R.string.retry) {
                            alert.dismiss()
                            refreshUI()
                        }.setActionTextColor(context?.getCustomColor(requireContext(),R.color.white)!!)
                        alert.show()
                    } else {
                    }
                } else
                    context?.resources?.getString(showError as Int)?.let {
                        cookieBarConfig.showDefaultErrorCookie(it)
                    }
            }

        })

        onLazyInitView(savedInstanceState)
    }

    open fun onLazyInitView(savedInstanceState: Bundle?) {
    }

    open fun refreshUI() {

    }

    @SuppressLint("NewApi")
    private fun checkNetworkConnectivity(context: Context) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        val isConnected: Boolean = networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

        if (!isConnected) viewModel.showError.postValue(R.string.internet_not_available)
    }

    fun toggleKeyboard(activity: Context, show: Boolean? = null) {

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

            if (show != null) {
                if (!show) {
                    imm.hideSoftInputFromWindow(requireView().windowToken, 0) // hide
                } else {
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY) // show
                }
            } else {
                if (imm.isActive) {
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0) // hide
                } else {
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY) // show
                }
            }
        }

    }

    protected fun launchWebTab(url: String) {
        (activity as BaseSupportActivity).launchWebUrl(url)
    }

    fun showKeyboard(etText: EditText) {
        etText.requestFocus()
        toggleKeyboard(requireActivity(), true)
    }

    fun setAppBarVisibilityAndTitle(visibility: Int, title: Int) {
        if (activity is HomeActivity) {
            val mainActivity = activity as HomeActivity
            if (visibility == View.VISIBLE){
                mainActivity.toolbar_layout.fragment_title.text = resources.getString(title)
            }else if (visibility == View.GONE){
                mainActivity.toolbar_layout.fragment_title.text = ""
            }
            mainActivity.app_bar_layout.visibility = visibility
        }
    }

    fun setCartCount(counter: Int, visibility: Int) {
        if (activity is HomeActivity) {
            val  homeActivity = activity as HomeActivity
            homeActivity.setCartCount(counter, visibility)
        }
    }

    fun returnTureId(menuId: String?): String? {
        return menuId?.takeLast(2)
    }

    override fun onDestroy() {
        super.onDestroy()
        progressDialog.dismiss()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity is HomeActivity){
            val  homeActivity = activity as HomeActivity
            homeActivity.setBottomNavigationVisibility(navigationVisibility)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity).apply {
            activeLocalization()
        }
    }

}