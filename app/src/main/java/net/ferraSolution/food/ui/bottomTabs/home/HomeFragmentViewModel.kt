package net.ferraSolution.food.ui.bottomTabs.home


import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.ferraSolution.food.base.BaseViewModel
import net.ferraSolution.food.data.models.*
import net.ferraSolution.food.data.repository.AuthRepository
import net.ferraSolution.food.data.repository.HomeRepository
import net.ferraSolution.food.data.repository.MenuRepository
import net.ferraSolution.food.utils.Constants

open class HomeFragmentViewModel(
    private val homeRepository: HomeRepository,
    private val menuRepository: MenuRepository,
    private val authRepository: AuthRepository
) : BaseViewModel() {

    var popularCategories: MutableLiveData<List<PopularCategoriesResponse>> = MutableLiveData()
    var bestDeals: MutableLiveData<List<BestDealModel>> = MutableLiveData()

    var categories: MutableLiveData<CategoryModel> = MutableLiveData()
    var userModel: MutableLiveData<UserModel> = MutableLiveData()
    var food: MutableLiveData<Foods> = MutableLiveData()
    var foodLoaded: MutableLiveData<Boolean> = MutableLiveData()

    var allCategories: MutableLiveData<ArrayList<CategoryModel?>> = MutableLiveData()
    fun getAllCategories() {

        val categoriesListTemp: ArrayList<CategoryModel?> = arrayListOf()

        menuRepository.getAllCategories()
            ?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEachIndexed { index, itemSnapshot ->
                        val model = itemSnapshot.getValue(CategoryModel::class.java)
                        model?.menuId = itemSnapshot.key ?: ""
                        categoriesListTemp.add(index, model)
                    }
                    allCategories.postValue(categoriesListTemp)
                }

                override fun onCancelled(error: DatabaseError) {
                    showError.postValue(error.message)
                }

            })
    }

    fun getBestDealOrPopularCategory(menuId: String, foodId: String) {
        showLoading.postValue(true)

        menuRepository.getBestDealOrPopularCategory(menuId)
            ?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val model = snapshot.getValue(CategoryModel::class.java)
                    model?.menuId = snapshot.key ?: ""
                    categories.postValue(model)
                    getBestDealOrPopularFood(menuId, foodId)
                }

                override fun onCancelled(error: DatabaseError) {
                    showLoading.postValue(false)
                    showError.postValue(error.message)
                }

            })
    }

    fun getBestDealOrPopularFood(menuId: String, foodId: String) {

        menuRepository.getBestDealOrPopularCategory(menuId)?.child("foods")?.child(foodId)
            ?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val model = snapshot.getValue(Foods::class.java)
                    food.postValue(model)
                    foodLoaded.postValue(true)
                    showLoading.postValue(false)
                }

                override fun onCancelled(error: DatabaseError) {
                    showLoading.postValue(false)
                    showError.postValue(error.message)
                }

            })
    }

    fun getPopularCategories() {
        var popularCategoriesListTemp: List<PopularCategoriesResponse?>

        homeRepository.getMostPopular()?.get()?.addOnSuccessListener { snapshot ->
            try {
                popularCategoriesListTemp = snapshot?.documents?.map { sn ->
                    sn.toObject(PopularCategoriesResponse::class.java).also { popularCategory ->
                        popularCategory?.categoryId = sn.reference.id
                        popularCategory?.name = sn.get("name")?.toString()
                        popularCategory?.food_id = sn.get("food_id")?.toString()
                        popularCategory?.menu_id = sn.get("menu_id")?.toString()
                        popularCategory?.image = sn.get("image")?.toString()
                    }
                } ?: listOf()

                popularCategories.postValue(popularCategoriesListTemp.filterNotNull())
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

    }

    fun getBestDeals(showLoad: Boolean) {
        showLoading.postValue(showLoad)
        var bestDealsListTemp: List<BestDealModel?>

        homeRepository.getBestDeals()?.get()?.addOnSuccessListener { snapshot ->
            try {
                showLoading.postValue(false)
                bestDealsListTemp = (snapshot?.documents?.map { sn ->
                    sn.toObject(BestDealModel::class.java).also { bestDeal ->
                        bestDeal?.categoryId = sn.reference.id
                        bestDeal?.name = sn.get("name")?.toString()
                        bestDeal?.food_id = sn.get("food_id")?.toString()
                        bestDeal?.menu_id = sn.get("menu_id")?.toString()
                        bestDeal?.image = sn.get("image")?.toString()
                    }
                } ?: listOf())!!
                bestDeals.postValue(bestDealsListTemp.filterNotNull())
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
            ?.addOnFailureListener {
                showLoading.postValue(false)
            }

    }

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

}