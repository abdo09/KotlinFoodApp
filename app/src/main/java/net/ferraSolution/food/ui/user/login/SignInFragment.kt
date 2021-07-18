package net.ferraSolution.food.ui.user.login

import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.toolbar.view.*
import net.ferraSolution.food.R
import net.ferraSolution.food.base.BaseSupportFragment
import net.ferraSolution.food.utils.*
import org.koin.android.viewmodel.ext.android.viewModel

class SignInFragment : BaseSupportFragment(R.layout.fragment_sign_in) {

    override val viewModel by viewModel<LoginFragmentViewModel>()

    var isEmail: Boolean = true

    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationVisibility = View.GONE

        onClick()

        viewModelObserver()

        callbacks()

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

                clearAllFields()
            }

        }

        viewModel.verificationsID.observe(viewLifecycleOwner, {

            viewModel.verificationID = it

        })

        viewModel.loginByPhoneIsSuccessful.observe(viewLifecycleOwner, { isSuccessful ->

            if (isSuccessful) {
                navController.navigate(R.id.action_loginFragment_to_homeFragment)
            }

        })
    }

    //Clear all fields
    private fun clearAllFields() {
        ed_login_email.text = null
        ed_login_password.text = null
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
            ) {}
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
                        ed_login_email.text.toString(),
                        ed_login_password.text.toString()
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

        /*btn_verification.setOnClickListener {

            if (isPhoneEntryValidated(ed_login_verificationNumber, ip_login_verificationNumber)) {

                viewModel.verifyCode(
                    ed_login_verificationNumber.text.toString().trim(),
                    requireActivity()
                )

            }

        }

        tv_login_option.setOnClickListener {

            if (isEmail) {

                isEmail = false
                tv_login_option.text = getString(R.string.email)

                loginWithEmailLayout.fadeOut(300)

                view.postDelayed({
                    loginLayoutPhoneNumber.fadeIn(300)
                }, 400)

            } else {

                isEmail = true
                tv_login_option.text = getString(R.string.phone_number)

                view.postDelayed({
                    loginWithEmailLayout.fadeIn(300)
                }, 400)

                loginLayoutPhoneNumber.fadeOut(300)


            }

        }*/
    }

}
