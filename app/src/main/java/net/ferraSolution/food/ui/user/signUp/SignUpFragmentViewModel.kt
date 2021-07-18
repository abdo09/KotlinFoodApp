package net.ferraSolution.food.ui.user.signUp

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.ferraSolution.food.base.BaseViewModel
import net.ferraSolution.food.data.dao.UsersDAO
import net.ferraSolution.food.data.models.CategoryModel
import net.ferraSolution.food.data.models.UserModel
import net.ferraSolution.food.data.repository.MenuRepository
import net.ferraSolution.food.data.repository.roomRepository.UserRoomRepository

class SignUpFragmentViewModel(
    private val auth: FirebaseAuth,
    private val menuRepository: MenuRepository,
    private val userRoomRepository: UserRoomRepository
) : BaseViewModel() {
    var categories: MutableLiveData<ArrayList<CategoryModel?>> = MutableLiveData()
    var isUserCreated = MutableLiveData<Boolean>()
    var userUploaded = MutableLiveData<Boolean>()
    var uId = MutableLiveData<String>()

    fun createUser(email: String?, password: String?) {
        showLoading.value = true
        try {
            email?.let { e_mail ->
                password.let { password ->
                    auth.createUserWithEmailAndPassword(e_mail, password ?: "")
                        .addOnCompleteListener { task ->
                            showLoading.value = false
                            isUserCreated.postValue(task.isSuccessful)
                            if (task.isSuccessful) {
                                uId.postValue(auth.currentUser?.uid)
                            } else{
                                showError.postValue(task.exception?.message)
                            }
                        }

                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    fun getCategories(loading: Boolean) {
        showLoading.postValue(loading)

        val categoriesListTemp: ArrayList<CategoryModel?> = arrayListOf()

        menuRepository.getAllCategories()
            ?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    showLoading.postValue(false)
                    snapshot.children.forEachIndexed { index, itemSnapshot ->
                        val model = itemSnapshot.getValue(CategoryModel::class.java)
                        model?.menuId = itemSnapshot.key ?: ""
                        categoriesListTemp.add(index, model)
                    }
                    categories.postValue(categoriesListTemp)
                }

                override fun onCancelled(error: DatabaseError) {
                    showLoading.postValue(false)
                    showError.postValue(error.message)
                }

            })
    }

    fun saveLocalUser(userModel: UserModel) {
        launch(Dispatchers.IO) {
            userRoomRepository.upsertUser(userModel)
        }
    }

    fun uploadUserInfo(userModel: UserModel){
        menuRepository.setUserInfo()?.child(userModel.uid)?.setValue(userModel)?.addOnCompleteListener { task ->
            if (task.isSuccessful)
                userUploaded.postValue(true)
        }
    }

}