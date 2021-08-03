package net.ferraSolution.food.ui.user.login

import android.annotation.SuppressLint
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_sign_in.*
import net.ferraSolution.food.R
import net.ferraSolution.food.base.BaseSupportFragment
import net.ferraSolution.food.ui.HomeActivity
import net.ferraSolution.food.utils.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.text.DecimalFormat
import java.text.NumberFormat

class SignInFragment : BaseSupportFragment(R.layout.fragment_sign_in), View.OnClickListener {

    override val viewModel by viewModel<LoginFragmentViewModel>()

    var isEmail: Boolean = true

    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    private val backgroundActive1 by lazy { digit1.background as TransitionDrawable }
    private val backgroundActive2 by lazy { digit2.background as TransitionDrawable }
    private val backgroundActive3 by lazy { digit3.background as TransitionDrawable }
    private val backgroundActive4 by lazy { digit4.background as TransitionDrawable }
    private val backgroundActive5 by lazy { digit5.background as TransitionDrawable }
    private val backgroundActive6 by lazy { digit6.background as TransitionDrawable }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAppBarVisibilityAndTitle(View.GONE, R.string.login)

        navigationVisibility = View.GONE

        onClick()

        viewModelObserver()

        callbacks()
        addCallBackToExit()
        init()
        codeTextWatcher()

    }

    private fun init() {

        digit1.setOnClickListener(this)
        digit2.setOnClickListener(this)
        digit3.setOnClickListener(this)
        digit4.setOnClickListener(this)
        digit5.setOnClickListener(this)
        digit6.setOnClickListener(this)

        digit1.setBackgroundResource(R.drawable.otp_background)
        digit2.setBackgroundResource(R.drawable.otp_background)
        digit3.setBackgroundResource(R.drawable.otp_background)
        digit4.setBackgroundResource(R.drawable.otp_background)
        digit5.setBackgroundResource(R.drawable.otp_background)
        digit6.setBackgroundResource(R.drawable.otp_background)

    }

    override fun onClick(view: View?) {
        if (view?.id == digit1.id ||
            view?.id == digit2.id ||
            view?.id == digit3.id ||
            view?.id == digit4.id ||
            view?.id == digit5.id ||
            view?.id == digit6.id
        ) {
            context?.let {
                showKeyboard(digit7)
            }
        }
    }

    //Check fields are validated
    private fun isEmailEntryValidated(
        email: EditText,
        password: EditText,
        inputLayout: TextInputLayout
    ): Boolean {
        return when {
            email.text.toString().isEmpty() -> {
                inputLayout.setRedBoarder(R.string.email)
                false
            }
            password.text.toString().isEmpty() -> {
                inputLayout.setRedBoarder(R.string.password)
                false
            }
            else -> true
        }
    }

    //Check fields are validated
    private fun isPhoneEntryValidated(phone: EditText, inputLayout: TextInputLayout): Boolean {
        return when {
            phone.text.toString().isEmpty() -> {
                inputLayout.setRedBoarder(R.string.phone_number)
                false
            }
            else -> true
        }
    }

    //ViewModel observer
    private fun viewModelObserver() {
        viewModel.uId.observe(viewLifecycleOwner) { uId ->
            Constants().setUid(
                requireContext(),
                uId ?: ""
            )
        }

        viewModel.isUserSignedIn.observe(viewLifecycleOwner) { userSignedIn ->

            if (userSignedIn) {

                toggleKeyboard(requireActivity(), false)

                navController.navigate(SignInFragmentDirections.actionLoginFragmentToHomeFragment())
                (activity as HomeActivity).bottomNavigationView.selectedItemId = R.id.navigation_home

                clearAllFields()
            }

        }

        viewModel.verificationsID.observe(viewLifecycleOwner, {

            viewModel.verificationID = it

        })

        viewModel.loginByPhoneIsSuccessful.observe(viewLifecycleOwner, { isSuccessful ->

            if (isSuccessful) {
                viewModel.getUserInfo(Constants().getUid(requireContext()))
                navController.navigate(R.id.action_loginFragment_to_homeFragment)
            }

        })

        viewModel.userModel.observe(viewLifecycleOwner, { isSuccessful ->

            Timber.d(isSuccessful.uid)

        })
    }

    //Clear all fields
    private fun clearAllFields() {
        ed_login_email.text = null
        ed_login_password.text = null
    }

    private fun addCallBackToExit() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callBack)
    }


    private val callBack: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (activity is HomeActivity) {
                val homeActivity = activity as HomeActivity
                homeActivity.finish()
            }
        }

    }

    //Callbacks
    private fun callbacks() {

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                val code = credential.smsCode

                viewModel.verifyCode(code, requireActivity())

            }

            override fun onVerificationFailed(e: FirebaseException) {

                viewModel.showLoading.value = false

                CookieBarConfig(requireActivity()).showDefaultErrorCookie(e.toString())

            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                viewModel.verificationsID.postValue(verificationId)
                viewModel.showLoading.postValue(false)
                ip_login_phoneNumber.fadeOut(600)
                otp_layout.fadeIn(600)
                timer.fadeIn(600)
                startCounter()
            }
        }

    }

    //Handle fragment clicks
    private fun onClick() {
        tv_goTo_signUpFragment.setOnClickListener {

            navController.navigate(SignInFragmentDirections.actionLoginFragmentToSignUpFragment())

        }

        btn_doLogin.setOnClickListener {

            if (isEmail) {

                if (isEmailEntryValidated(ed_login_email, ed_login_password, ip_login_password)) {

                    viewModel.signInUserWithEmail(
                        ed_login_email.text.toString().trim(),
                        ed_login_password.text.toString().trim()
                    )

                }

            } else {

                if (isPhoneEntryValidated(ed_login_phoneNumber, ip_login_phoneNumber_numberLayout)) {

                    var phoneNumber = "+"
                    phoneNumber += ed_login_phoneNumber.text.toString()

                    viewModel.signInUserWithPhoneNumber(phoneNumber, requireActivity(), callbacks)

                }

            }

        }

        tv_loginWithEmail.setOnClickListener {
            login_otp_layout.fadeOut(600)
            login_emailPassword_layout.fadeIn(600)
        }

        tv_loginWithPhone.setOnClickListener {
            login_otp_layout.fadeIn(600)
            login_emailPassword_layout.fadeOut(600)
        }

        btn_send_otp.setOnClickListener {
            if (isPhoneEntryValidated(ed_login_phoneNumber, ip_login_phoneNumber_numberLayout)) {

                var phoneNumber = ed_login_phoneNumber.text.toString()
                if (phoneNumber.take(1).toInt() == 0 && phoneNumber.length == 10)
                    phoneNumber = "+${code_picker.selectedCountryCode}${phoneNumber.drop(1)}"
                else if(phoneNumber.take(1).toInt() != 0 && phoneNumber.length == 9)
                    phoneNumber = "+${code_picker.selectedCountryCode}$phoneNumber"
                else
                    CookieBarConfig(
                        requireActivity()
                    ).showDefaultErrorCookie("The number you entered is not correct")

                viewModel.signInUserWithPhoneNumber(phoneNumber, requireActivity(), callbacks)

            }
        }
    }

    private fun setErrorTransition() {
        digit1.setBackgroundResource(R.drawable.otp_background_active)
        digit2.setBackgroundResource(R.drawable.otp_background_active)
        digit3.setBackgroundResource(R.drawable.otp_background_active)
        digit4.setBackgroundResource(R.drawable.otp_background_active)
        digit5.setBackgroundResource(R.drawable.otp_background_active)
        digit6.setBackgroundResource(R.drawable.otp_background_active)
        (digit1.background as TransitionDrawable).startTransition(200)
        (digit2.background as TransitionDrawable).startTransition(200)
        (digit3.background as TransitionDrawable).startTransition(200)
        (digit4.background as TransitionDrawable).startTransition(200)
        (digit5.background as TransitionDrawable).startTransition(200)
        (digit6.background as TransitionDrawable).startTransition(200)
    }

    private fun resetBackground() {
        digit1.setBackgroundResource(R.drawable.otp_background)
        digit2.setBackgroundResource(R.drawable.otp_background)
        digit3.setBackgroundResource(R.drawable.otp_background)
        digit4.setBackgroundResource(R.drawable.otp_background)
        digit5.setBackgroundResource(R.drawable.otp_background)
        digit6.setBackgroundResource(R.drawable.otp_background)
        backgroundActive1.resetTransition()
        backgroundActive2.resetTransition()
        backgroundActive3.resetTransition()
        backgroundActive4.resetTransition()
        backgroundActive5.resetTransition()
        backgroundActive6.resetTransition()
    }

    private fun startCounter() {
        timer.visibility = View.VISIBLE
        val timer: CountDownTimer = object : CountDownTimer(90000.toLong(), 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val seconds = (millisUntilFinished / 1000).toInt() % 60
                val minutes = (millisUntilFinished / (1000 * 60) % 60).toInt()
                val numberFormat: NumberFormat = DecimalFormat("00")
                if (isAdded) {
                    timer.text =
                        numberFormat.format(minutes.toLong()) + ":" + numberFormat.format(seconds.toLong())
                }
            }

            override fun onFinish() {
                if (isAdded) {
                    timer.text = getString(R.string.set_timer)
                }
            }
        }
        timer.start()
    }

    private fun codeTextWatcher(){
        digit7.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, p1: Int, p2: Int, p3: Int) {
                    if (s.isEmpty()) {
                        resetBackground()
                    }
                }

                @SuppressLint("HardwareIds")
                override fun onTextChanged(s: CharSequence, p1: Int, p2: Int, p3: Int) {
                    when (s.length) {
                        0 -> {
                            digit1.setText("")
                            digit2.setText("")
                            digit3.setText("")
                            digit4.setText("")
                            digit5.setText("")
                            digit6.setText("")
                        }
                        1 -> {
                            digit1.setText(s.subSequence(0, 1).toString())
                            digit2.setText("")
                            digit3.setText("")
                            digit4.setText("")
                            digit5.setText("")
                            digit6.setText("")
                        }
                        2 -> {
                            digit2.setText(s.subSequence(1, 2).toString())
                            digit3.setText("")
                            digit4.setText("")
                            digit5.setText("")
                            digit6.setText("")
                        }
                        3 -> {
                            digit3.setText(s.subSequence(2, 3).toString())
                            digit4.setText("")
                            digit5.setText("")
                            digit6.setText("")
                        }
                        4 -> {
                            digit4.setText(s.subSequence(3, 4).toString())
                            digit5.setText("")
                            digit6.setText("")
                        }
                        5 -> {
                            digit5.setText(s.subSequence(4, 5).toString())
                            digit6.setText("")
                        }
                        6 -> {
                            digit6.setText(s.subSequence(5, 6).toString())
                            context?.let { context ->
                                toggleKeyboard(context, false)
                            }

                            context?.let { context ->
                                Constants().apply {
                                    setDeviceId(
                                        context,
                                        Settings.Secure.getString(
                                            requireContext().contentResolver,
                                            Settings.Secure.ANDROID_ID
                                        )
                                    )
                                    val otp = digit7.text.toString()
                                    viewModel.verifyCode(otp, requireActivity())
                                }
                            }
                        }
                    }
                }

                override fun afterTextChanged(s: Editable) {
                    when (s.length) {
                        1 -> {
                            backgroundActive1.startTransition(500)
                            backgroundActive2.resetTransition()
                            backgroundActive3.resetTransition()
                            backgroundActive4.resetTransition()
                            backgroundActive5.resetTransition()
                            backgroundActive6.resetTransition()
                        }
                        2 -> {
                            backgroundActive2.startTransition(500)
                            backgroundActive3.resetTransition()
                            backgroundActive4.resetTransition()
                            backgroundActive5.resetTransition()
                            backgroundActive6.resetTransition()
                        }
                        3 -> {
                            backgroundActive3.startTransition(500)
                            backgroundActive4.resetTransition()
                            backgroundActive5.resetTransition()
                            backgroundActive6.resetTransition()
                        }
                        4 -> {
                            backgroundActive4.startTransition(500)
                            backgroundActive5.resetTransition()
                            backgroundActive6.resetTransition()
                        }
                        5 -> {
                            backgroundActive5.startTransition(500)
                            backgroundActive6.resetTransition()
                        }
                        6 -> {
                            backgroundActive6.startTransition(500)
                        }
                    }
                }

            }
        )
    }

}
