package net.ferraSolution.food.ui.user.signUp

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.android.synthetic.main.toolbar.view.*
import net.ferraSolution.food.R
import net.ferraSolution.food.base.BaseSupportFragment
import net.ferraSolution.food.data.models.CategoryModel
import net.ferraSolution.food.data.models.UserModel
import net.ferraSolution.food.utils.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class SignUpFragment : BaseSupportFragment(R.layout.fragment_signup) {

    private var isMale = true

    override val viewModel by viewModel<SignUpFragmentViewModel>()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAppBarVisibilityAndTitle(View.GONE, R.string.sign_up)

        toolbar_layout.tv_title.text = getString(R.string.sign_up)
        navigationVisibility = View.GONE

        onClick()

        setGrayBoarderToField()

        viewModelObserver()

    }

    //Check fields are validated
    private fun isEntryValidated(): Boolean {
        when {
            ed_signUp_firstName.text.toString().isEmpty() -> {
                ip_signUp_firstName.setRedBoarder(R.string.first_name)
                return false
            }
            ed_signUp_lastName.text.toString().isEmpty() -> {
                ip_signUp_lastName.setRedBoarder(R.string.last_name)
                return false
            }
            ed_signUp_email.text.toString().isEmpty() -> {
                ip_signUp_email.setRedBoarder(R.string.email)
                return false
            }
            ed_signUp_number.text.toString().isEmpty() -> {
                ip_signUp_number.setRedBoarder(R.string.number)
                return false
            }
            ed_signUp_password.text.toString().isEmpty() -> {
                ip_signUp_password.setRedBoarder(R.string.password)
                return false
            }
            else -> return true
        }
    }

    //Handle fragment clicks
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun onClick() {
        btn_signUp.setOnClickListener {
            if (isEntryValidated()) {
                viewModel.createUser(
                    ed_signUp_email.text.toString(),
                    ed_signUp_password.text.toString()
                )
            }
        }

        tv_login.setOnClickListener {
            navController.navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
        }






        btn_signUp_male.setOnClickListener {
            if (!isMale) {
                isMale = true
                setChecked(
                    btn1 = btn_signUp_male,
                    btn2 = btn_signUp_female,
                    tv1 = tv_signUp_male,
                    tv2 = tv_signUp_female,
                    img1 = image_male,
                    img2 = image_female,
                    isChecked = isMale,
                    requireContext()
                )
            }
        }

        btn_signUp_female.setOnClickListener {
            if (isMale) {
                isMale = false
                setChecked(
                    btn1 = btn_signUp_male,
                    btn2 = btn_signUp_female,
                    tv1 = tv_signUp_male,
                    tv2 = tv_signUp_female,
                    img1 = image_male,
                    img2 = image_female,
                    isChecked = isMale,
                    requireContext()
                )
            }
        }
    }

    //Set default boarder
    private fun setGrayBoarderToField() {
        ed_signUp_firstName.setGoldColorBoarder(R.string.first_name, ip_signUp_firstName)
        ed_signUp_lastName.setGoldColorBoarder(R.string.last_name, ip_signUp_lastName)
        ed_signUp_email.setGoldColorBoarder(R.string.email, ip_signUp_email)
        ed_signUp_number.setGoldColorBoarder(R.string.number, ip_signUp_number)
        ed_signUp_password.setGoldColorBoarder(R.string.password, ip_signUp_password)
    }

    //ViewModel observer
    private fun viewModelObserver() {
        ed_signUp_number.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    if (s.take(1).toString().toInt() == 0 && s.length == 10)
                        toggleKeyboard(requireContext(), false)
                    else if (s.take(1).toString().toInt() != 0 && s.length == 9) {
                        toggleKeyboard(requireContext(), false)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        viewModel.isUserCreated.observe(viewLifecycleOwner) { userCreated ->

            if (userCreated) {
                viewModel.uId.observe(viewLifecycleOwner) { uId ->
                    Constants().setUid(
                        requireContext(),
                        uId ?: ""
                    )
                }
                uploadUserInfo()
            }
        }

        viewModel.userUploaded.observe(viewLifecycleOwner, {
            navController.navigate(SignUpFragmentDirections.actionSignUpFragmentToHomeFragment())
            clearAllFields()
        })

    }

    //Clear all fields
    private fun clearAllFields() {
        ed_signUp_firstName.setText("")
        ed_signUp_lastName.setText("")
        ed_signUp_email.setText("")
        ed_signUp_number.setText("")
        ed_signUp_password.setText("")
    }

    private fun uploadUserInfo() {
        val uId = Constants().getUid(requireContext())
        val firstName = ed_signUp_firstName.text.toString()
        val lastName = ed_signUp_lastName.text.toString()
        val email = ed_signUp_email.text.toString()
        var phoneNumber = ed_signUp_number.text.toString()
        if (phoneNumber.take(1).toInt() == 0 && phoneNumber.length == 10)
            phoneNumber = "+${code_picker_signUp.selectedCountryCode}${phoneNumber.drop(1)}"
        else if (phoneNumber.take(1).toInt() != 0 && phoneNumber.length == 9)
            phoneNumber = "+${code_picker_signUp.selectedCountryCode}$phoneNumber"
        else
            CookieBarConfig(
                requireActivity()
            ).showDefaultErrorCookie("The number you entered is not correct")

        val user = UserModel()

        user.apply {
            this.uid = uId ?: ""
            this.firstName = firstName
            this.lastName = lastName
            this.email = email
            this.phoneNumber = phoneNumber
        }

        Timber.d("${user.phoneNumber}")

        viewModel.uploadUserInfo(user)

        viewModel.saveLocalUser(user)
    }

}