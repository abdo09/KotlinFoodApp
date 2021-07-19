package net.ferraSolution.food.ui.bottomTabs.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.ferraSolution.food.base.BaseViewModel
import net.ferraSolution.food.data.dao.MenuDAO
import net.ferraSolution.food.data.models.AddonModel
import net.ferraSolution.food.data.models.CategoryModel
import net.ferraSolution.food.data.repository.AuthRepository
import net.ferraSolution.food.data.repository.HomeRepository
import net.ferraSolution.food.data.repository.MenuRepository
import net.ferraSolution.food.data.repository.roomRepository.MenuRoomRepository
import net.ferraSolution.food.ui.bottomTabs.home.HomeFragmentViewModel


open class MenuFragmentViewModel(
    val menuRepository: MenuRepository,
    val menuDAO: MenuDAO,
    private val menuRoomRepository: MenuRoomRepository,
    homeRepository: HomeRepository,
    authRepository: AuthRepository
) : HomeFragmentViewModel(homeRepository, menuRepository, authRepository) {

    var category: MutableLiveData<ArrayList<CategoryModel?>> = MutableLiveData()
    var foodRate: MutableLiveData<Double> = MutableLiveData()

    var addons: MutableLiveData<List<AddonModel>> = MutableLiveData<List<AddonModel>>()

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
                    launch(Dispatchers.IO) {
                        menuDAO.insertAll(categoriesListTemp.toList())
                    }
                    category.postValue(categoriesListTemp)
                }

                override fun onCancelled(error: DatabaseError) {
                    showLoading.postValue(false)
                    showError.postValue(error.message)
                }

            })
    }

    fun getCategoryFromDB(): LiveData<List<CategoryModel>> {
        return menuRoomRepository.getAllCategoriesFromDB()
    }

}