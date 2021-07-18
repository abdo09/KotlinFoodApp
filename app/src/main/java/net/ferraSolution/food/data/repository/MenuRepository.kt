package net.ferraSolution.food.data.repository

import com.google.firebase.database.*
import net.ferraSolution.food.data.paths.FireStoreConfig.Companion.CATEGORY
import net.ferraSolution.food.data.paths.FireStoreConfig.Companion.COMMENT
import net.ferraSolution.food.data.paths.FireStoreConfig.Companion.USERS
import net.ferraSolution.food.utils.replaceAllId

class MenuRepository(
    private val database: FirebaseDatabase,
    private val authRepository: AuthRepository
) {

    fun getAllCategories() : DatabaseReference? {
        return if (authRepository.isUserLogged())
            database.getReference(CATEGORY)
        else null
    }

    fun getBestDealOrPopularCategory(menuId: String) : DatabaseReference? {
        return if (authRepository.isUserLogged())
            database.getReference(CATEGORY).child(menuId)
        else null
    }

    fun setComment() : DatabaseReference? {
        return if (authRepository.isUserLogged())
            database.getReference(COMMENT)
        else null
    }

    fun getUserMenu() : DatabaseReference? {
        return if (authRepository.isUserLogged())
            database.getReference(USERS)
        else null
    }

    fun updateUserMenu(uid: String, menuId: String) : DatabaseReference? {
        return if (authRepository.isUserLogged())
            database.getReference(USERS).child(uid).child("userMenu").child(menuId.replaceAllId()).child("foods")
        else null
    }

    fun setUserInfo() : DatabaseReference? {
        return if (authRepository.isUserLogged())
            database.getReference(USERS)
        else null
    }

}