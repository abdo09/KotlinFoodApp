package net.ferraSolution.food.ui.user.verification

import android.graphics.drawable.TransitionDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.verification_otp_fragment.*
import net.ferraSolution.food.R
import net.ferraSolution.food.base.BaseSupportFragment
import net.ferraSolution.food.ui.user.login.LoginFragmentViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class VerificationOtpFragment : BaseSupportFragment(R.layout.verification_otp_fragment) {

    override val viewModel by viewModel<VerificationViewModel>()

    //private val args: VerificationOtpFragmentDirections by navArgs()

    private val backgroundActive1 by lazy { digit1.background as TransitionDrawable }
    private val backgroundActive2 by lazy { digit2.background as TransitionDrawable }
    private val backgroundActive3 by lazy { digit3.background as TransitionDrawable }
    private val backgroundActive4 by lazy { digit4.background as TransitionDrawable }
    private val backgroundActive5 by lazy { digit5.background as TransitionDrawable }
    private val backgroundActive6 by lazy { digit6.background as TransitionDrawable }

    var countryCode = ""
    var phoneNumber = ""


}