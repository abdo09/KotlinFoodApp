package net.ferraSolution.food.ui.bottomTabs.menu.foodsList

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_foods.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.ferraSolution.food.R
import net.ferraSolution.food.base.BaseSupportFragment
import net.ferraSolution.food.data.models.CategoryModel
import net.ferraSolution.food.data.models.Foods
import net.ferraSolution.food.data.models.OrderModel
import net.ferraSolution.food.data.models.UserModel
import net.ferraSolution.food.ui.HomeActivity
import net.ferraSolution.food.ui.bottomTabs.menu.foodsList.adapter.FoodsAdapter
import net.ferraSolution.food.utils.Constants
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class FoodsListFragment :
    BaseSupportFragment(R.layout.fragment_foods) {
    override val viewModel by viewModel<FoodsListFragmentViewModel>()

    private lateinit var foodsAdapter: FoodsAdapter

    private val args by navArgs<FoodsListFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uid = Constants().getUid(requireContext())
        viewModel.getUserMenu(true, uid ?: "")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAppBarVisibilityAndTitle(View.VISIBLE, R.string.foods)

        setupRecyclerView()

        adapterClickListeners()

        addCallBackToExit()

        observer()

    }

    private fun addCallBackToExit() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callBack)
    }


    private val callBack: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (activity is HomeActivity) {
                navController.navigate(FoodsListFragmentDirections.actionFoodsFragmentToMenuFragment())
            }
        }

    }

    private fun setupRecyclerView() {
        foodsAdapter = FoodsAdapter()
        rv_foods.apply {
            adapter = foodsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun observer() {
        viewModel.menuDAO.getAllCategories().observe(viewLifecycleOwner, { category ->
            category.forEach {
                if (args.category?.name == it.name){
                    foodsAdapter.differ.submitList(it.foods)
                }
            }
        })

    }

    @SuppressLint("LogNotTimber")
    private fun adapterClickListeners() {

        foodsAdapter.setOnItemClickListener { categoryFoods ->
            navController.navigate(
                FoodsListFragmentDirections.actionFoodsFragmentToFoodDetailsFragment(
                    category = args.category,
                    itemFood = categoryFoods
                )
            )
        }

        foodsAdapter.setOnLikedClickListener { categoryFood ->

            val uid = Constants().getUid(requireContext())
            val menuId = returnTureId(args.category?.menuId)
            viewModel.updateUserMen(
                uid ?: "",
                menuId ?: "",
                categoryFood.id ?: "",
                "liked",
                categoryFood.isLiked ?: false
            )

        }

        foodsAdapter.setOnAddToCartClickListener { categoryFood ->
            handleAddFoodToCart(foods = categoryFood)
        }

    }

    private fun handleAddFoodToCart(foods: Foods?) {
        val userModel = Constants().getUser(requireContext())
        val cartItem = OrderModel.CartItem()

        cartItem.apply {
            this.foodAddon = "Default"
            this.foodSize = "Default"
            this.foodId = foods?.id ?: ""
            this.foodExtraPrice = 0.0
            this.foodQuantity = 1
            this.foodName = foods?.name
            this.foodPrice = foods?.price
            this.foodImage = foods?.image
            this.userPhone = userModel?.phoneNumber
            this.uid = userModel?.uid ?: ""
        }

        addItemToCart(
            userModel = userModel,
            foodId = foods?.id ?: "",
            cartItem = cartItem,
            foods = foods
        )

        itemsInCartCounter()

    }

    private fun addItemToCart(
        userModel: UserModel?,
        foodId: String?,
        cartItem: OrderModel.CartItem,
        foods: Foods?
    ) {
            viewModel.launch(Dispatchers.IO) {
                val cartItemFromDB = viewModel.cartItemDAO.getItemWithAllOptionsInCart(
                    userModel?.uid ?: "",
                    foodId ?: "",
                    "Default",
                    "Default"
                )
                if (cartItem == cartItemFromDB){
                    cartItemFromDB.apply {
                        this.foodAddon = "Default"
                        this.foodSize = "Default"
                        this.foodId = foodId ?: ""
                        this.foodExtraPrice = 0.0
                        this.foodQuantity = this.foodQuantity.plus(cartItem.foodQuantity)
                        this.foodName = foods?.name
                        this.foodPrice = foods?.price
                        this.foodImage = foods?.image
                        this.userPhone = userModel?.phoneNumber
                        this.uid = userModel?.uid ?: ""
                    }
                    viewModel.launch(Dispatchers.IO) {
                        viewModel.cartItemDAO.upsertItemInCart(cartItemFromDB)
                    }
                } else{
                    viewModel.launch(Dispatchers.IO) {
                        viewModel.cartItemDAO.upsertItemInCart(cartItem)
                    }
                }
            }
    }

    private fun itemsInCartCounter() {
        viewModel.launch {
            viewModel.countItemInCart(Constants().getUid(requireContext()) ?: "")
                .observe(viewLifecycleOwner, {
                    if (it ?: 0 > 0)
                        setCartCount(it ?: 0, View.VISIBLE)
                    else
                        setCartCount(0, View.GONE)
                })
        }
    }

}