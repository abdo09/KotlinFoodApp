package net.ferraSolution.food.ui.user.login

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import net.ferraSolution.food.base.BaseViewModel
import net.ferraSolution.food.data.models.UserModel
import net.ferraSolution.food.data.repository.AuthRepository
import net.ferraSolution.food.utils.CookieBarConfig
import timber.log.Timber
import java.util.concurrent.TimeUnit

class LoginFragmentViewModel(private val auth: FirebaseAuth, private val authRepository: AuthRepository) : BaseViewModel() {
    var isUserSignedIn = MutableLiveData<Boolean>()
    var loginByPhoneIsSuccessful = MutableLiveData<Boolean>()
    var uId = MutableLiveData<String>()
    var verificationsID = MutableLiveData<String>()
    var verificationID: String = ""
    var resendToken: String = ""
    var userModel: MutableLiveData<UserModel> = MutableLiveData()

    fun getUserInfo(uid: String?) {
        showLoading.value = false
        authRepository.getUserInfo()?.child(uid?: "")
            ?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val model = snapshot.getValue(UserModel::class.java)
                    userModel.postValue(model)
                }

                override fun onCancelled(error: DatabaseError) {
                    showLoading.postValue(false)
                    showError.postValue(error.message)
                }

            })
    }

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

                if (task.isSuccessful){

                    uId.postValue(auth.currentUser?.uid)

                    loginByPhoneIsSuccessful.postValue(task.isSuccessful)

                } else showError.postValue(task.exception?.message)

            }
    }
}