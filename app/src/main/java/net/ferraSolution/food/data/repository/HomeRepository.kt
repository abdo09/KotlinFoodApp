package net.ferraSolution.food.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import net.ferraSolution.food.data.paths.FireStoreConfig

class HomeRepository (private val fireStore: FirebaseFirestore,
                      private val authRepository: AuthRepository) {

    fun getMostPopular(): Query? {
        return if (authRepository.isUserLogged())
            fireStore
                .collection(FireStoreConfig.MOSTPOPULAR)
        else null

    }

    fun getBestDeals(): Query? {
        return if (authRepository.isUserLogged())
            fireStore
                .collection(FireStoreConfig.BESTDEALS)
        else null

    }

}