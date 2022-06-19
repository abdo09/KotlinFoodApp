package net.ferraSolution.food.ui.onBoarding

import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.core.view.postDelayed
import kotlinx.android.synthetic.main.activity_home.*
import net.ferraSolution.food.R
import net.ferraSolution.food.base.BaseSupportFragment
import net.ferraSolution.food.ui.HomeActivity
import net.ferraSolution.food.ui.user.login.LoginFragmentViewModel

import net.ferraSolution.food.utils.Constants
import net.ferraSolution.food.utils.navigationBarAndStatusBarColor
import org.koin.android.viewmodel.ext.android.viewModel

class SplashFragment : BaseSupportFragment(R.layout.fragment_splash) {


    override val viewModel by viewModel<LoginFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationVisibility = View.GONE
        setAppBarVisibilityAndTitle(View.GONE, R.string.splash)

        requireActivity().navigationBarAndStatusBarColor(R.color.appColor, R.color.appColor)

        val point = Point()
        requireActivity().windowManager.defaultDisplay.getSize(point)
        val translation: Float = (point.y / 5).toFloat()
        requireView().postDelayed(100) {
            view.findViewById<ImageView>(R.id.splash_logo).animate()
                .translationYBy(translation * -1)
                .setDuration(1150)
                .setInterpolator(DecelerateInterpolator())
                .withStartAction {

                    view.postDelayed({
                        navigateToNextDestination()
                    }, 3000)
                }
                .start()
        }

    }

    private fun navigateToNextDestination() {
        navController.navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment())
        /*if (Constants().getUid(requireContext()).isNullOrEmpty()) {
            navController.navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
        } else {
            navController.navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment())
        }*/
    }

}