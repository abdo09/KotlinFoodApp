package net.ferraSolution.food.ui.bottomTabs.menu.foodsList.foodDetails

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import net.ferraSolution.food.data.dao.MenuDAO
import net.ferraSolution.food.data.models.CommentModel
import net.ferraSolution.food.data.models.Foods
import net.ferraSolution.food.data.repository.HomeRepository
import net.ferraSolution.food.data.repository.MenuRepository
import net.ferraSolution.food.data.repository.roomRepository.MenuRoomRepository
import net.ferraSolution.food.ui.bottomTabs.menu.MenuFragmentViewModel

class RatingDialogViewModel(
    menuRepository: MenuRepository,
    menuDAO: MenuDAO,
    menuRoomRepository: MenuRoomRepository,
    homeRepository: HomeRepository
) : MenuFragmentViewModel(menuRepository, menuDAO, menuRoomRepository, homeRepository) {

    fun updateRate(
        menuId: String,
        foodId: String,
        rate: Float
    ) {
        menuRepository.getAllCategories()?.child(menuId)?.child("foods")
            ?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEachIndexed { _, itemSnapshot ->
                        val model = itemSnapshot.getValue(Foods::class.java)
                        model?.foodKey = itemSnapshot.key ?: ""
                        if(foodId == model?.id){
                            val sumRate = model.ratingValue?.plus(rate)
                            val ratingCount = model.ratingCount?.plus(1)
                            val result = ratingCount?.let { sumRate?.div(it) }
                            foodRate.postValue(result)
                            menuRepository.getAllCategories()?.child(menuId)?.child("foods")?.child(model.foodKey.toString())?.child("ratingValue")?.setValue(sumRate)
                            menuRepository.getAllCategories()?.child(menuId)?.child("foods")?.child(model.foodKey.toString())?.child("ratingCount")?.setValue(ratingCount)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showLoading.postValue(false)
                    showError.postValue(error.message)
                }

            })
    }

    fun addComment(
        commentModel: CommentModel,
        foodId: String
    ) {
        menuRepository.setComment()?.child(foodId)?.key
    }

}