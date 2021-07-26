package net.ferraSolution.food.ui.user.login

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.*
import com.google.firebase.database.FirebaseDatabase
import net.ferraSolution.food.base.BaseViewModel
import net.ferraSolution.food.data.repository.AuthRepository
import net.ferraSolution.food.utils.CookieBarConfig
import timber.log.Timber
import java.util.concurrent.TimeUnit

class LoginFragmentViewModel(private val auth: FirebaseAuth) : BaseViewModel() {
    var isUserSignedIn = MutableLiveData<Boolean>()
    var loginByPhoneIsSuccessful = MutableLiveData<Boolean>()
    var uId = MutableLiveData<String>()
    var verificationsID = MutableLiveData<String>()
    var verificationID: String = ""
    var resendToken: String = ""

    //Login with email and password
    fun signInUserWithEmail(email: String?, password: String?) {
        showLoading.value = true

        try {
            email?.let { e_mail ->
                password.let { password ->

                    auth.signInWithEmailAndPassword(e_mail, password ?: "")
                        .addOnCompleteListener { task ->

                            showLoading.value = false

                            isUserSignedIn.postValue(task.isSuccessful)

                            if (task.isSuccessful) {
                                uId.postValue(auth.currentUser?.uid)
                            } else {
                                Timber.d(task.exception?.message.toString())
                            }

                        }

                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    //Login with email and password
    fun signInUserWithPhoneNumber(phone: String, activity: Activity, callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks) {

        showLoading.value = true

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)
            .setTimeout(30L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    fun verifyCode(code: String?, activity: Activity) {

        val credential = code?.let { PhoneAuthProvider.getCredential(verificationID, it) }

        credential?.let { signInWithPhoneAuthCredential(it, activity) }

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential, activity: Activity) {

        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                showLoading.value = false

                loginByPhoneIsSuccessful.postValue(task.isSuccessful)

                if (task.isSuccessful){

                    uId.postValue(auth.currentUser?.uid)

                } else showError.postValue(task.exception?.message)

            }
    }
}