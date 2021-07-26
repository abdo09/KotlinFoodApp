package net.ferraSolution.food.ui.bottomTabs.tools

import com.google.firebase.auth.FirebaseAuth
import net.ferraSolution.food.base.BaseViewModel
import net.ferraSolution.food.data.dao.CartItemDAO

class ToolsFragmentViewModel(private val auth: FirebaseAuth, val cartItemDAO: CartItemDAO): BaseViewModel() {
    fun signOutUser() {
        auth.signOut()
    }
}