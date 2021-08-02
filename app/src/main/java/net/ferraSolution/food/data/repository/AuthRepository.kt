package net.ferraSolution.food.data.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import net.ferraSolution.food.data.paths.FireStoreConfig
import net.ferraSolution.food.utils.Constants
import timber.log.Timber

class AuthRepository(private val database: FirebaseDatabase, private var auth: FirebaseAuth) {

    fun createUser(email: String?, password: String?, context: Context) {
        try {
            email?.let { e_mail ->
                password.let { password ->
                    auth.createUserWithEmailAndPassword(e_mail, password ?: "")
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                    val uid = auth.currentUser?.uid
                                Constants().setUid(context, uid?: "")
                            } else {
                                // If create user fails, display a message to the user.
                                Timber.e(task.exception, "createUserWithEmailAndPassword:failure")
                            }
                        }

                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    fun signInUser(email: String?, password: String?) {
        try {
            email?.let { e_mail ->
                password.let { password ->

                    auth.signInWithEmailAndPassword(e_mail, password ?: "")
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                val user = auth.currentUser
                            } else {
                                // If sign in fails, display a message to the user.
                                Timber.e(task.exception, "signInWithEmailAndPassword:failure")
                            }
                        }

                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun getUserInfo() : DatabaseReference? {
        return if (isUserLogged())
            database.getReference(FireStoreConfig.USERS)
        else null
    }

    fun orders() : DatabaseReference? {
        return if (isUserLogged())
            database.getReference(FireStoreConfig.ORDERS)
        else null
    }

    fun isUserLogged(): Boolean {
        Timber.d("auth.currentUser ${auth.currentUser?.uid}  ${auth.currentUser} ")
        return auth.currentUser != null
    }

    fun userId(): String {
        return auth.currentUser?.uid.toString()
    }
}