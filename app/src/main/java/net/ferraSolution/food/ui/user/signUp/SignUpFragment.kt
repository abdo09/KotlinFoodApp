package net.ferraSolution.food.ui.user.signUp

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
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
import net.ferraSolution.food.utils.Constants
import net.ferraSolution.food.utils.setGoldColorBoarder
import net.ferraSolution.food.utils.setRedBoarder
import org.koin.android.viewmodel.ext.android.viewModel

class SignUpFragment : BaseSupportFragment(R.layout.fragment_signup) {

    private var isMale = true

    override val viewModel by viewModel<SignUpFragmentViewModel>()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_layout.tv_title.text = getString(R.string.sign_up)
        toolbar_layout.tv_title.setTextColor(Color.parseColor("#856455"))

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
                setGenderChecked(isMale, requireContext())
            }
        }

        btn_signUp_female.setOnClickListener {
            if (isMale) {
                isMale = false
                setGenderChecked(isMale, requireContext())
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
        viewModel.isUserCreated.observe(viewLifecycleOwner) { userCreated ->

            if (userCreated) {
                viewModel.uId.observe(viewLifecycleOwner) { uId ->
                    Constants().setUid(
                        requireContext(),
                        uId ?: ""
                    )
                }

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

    // Set gender checked
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setGenderChecked(isMale: Boolean, context: Context) {
        if (isMale) {
            btn_signUp_male.background =
                ContextCompat.getDrawable(context, R.drawable.boarder_gold500_color)
            tv_signUp_male.setHintTextColor(ContextCompat.getColor(context, R.color.orange_700))
            image_male.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_checked_gender
                )
            )

            btn_signUp_female.background =
                ContextCompat.getDrawable(context, R.drawable.boarder_gray_color)
            tv_signUp_female.setHintTextColor(ContextCompat.getColor(context, R.color.grayColor500))
            image_female.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_unchecked_gender
                )
            )
        } else {
            btn_signUp_female.background =
                ContextCompat.getDrawable(context, R.drawable.boarder_gold500_color)
            tv_signUp_female.setHintTextColor(ContextCompat.getColor(context, R.color.orange_700))
            image_female.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_checked_gender
                )
            )

            btn_signUp_male.background =
                ContextCompat.getDrawable(context, R.drawable.boarder_gray_color)
            tv_signUp_male.setHintTextColor(ContextCompat.getColor(context, R.color.grayColor500))
            image_male.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_unchecked_gender
                )
            )
        }
    }

    private fun uploadUserInfo() {
        val uId = Constants().getUid(requireContext())
        val firstName = ed_signUp_firstName.text.toString()
        val lastName = ed_signUp_lastName.text.toString()
        val email = ed_signUp_email.text.toString()
        val phoneNumber = ed_signUp_number.text.toString()

        val user = UserModel()

        user.apply {
            this.uid = uId?: ""
            this.firstName = firstName
            this.lastName = lastName
            this.email = email
            this.phoneNumber = phoneNumber
        }

        viewModel.uploadUserInfo(user)

        viewModel.saveLocalUser(user)
    }

}