package net.ferraSolution.food.ui.bottomTabs.menu.foodsList.foodDetails

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_food_details.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.ferraSolution.food.R
import net.ferraSolution.food.base.BaseSupportFragment
import net.ferraSolution.food.data.models.*
import net.ferraSolution.food.ui.HomeActivity
import net.ferraSolution.food.ui.bottomTabs.menu.foodsList.foodDetails.bottomSheet.adapters.SizeAdapter
import net.ferraSolution.food.ui.bottomTabs.menu.foodsList.FoodsListFragmentViewModel
import net.ferraSolution.food.utils.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class FoodDetailsFragment : BaseSupportFragment(R.layout.fragment_food_details) {

    override val viewModel by sharedViewModel<FoodsListFragmentViewModel>()

    private val args by navArgs<FoodDetailsFragmentArgs>()

    private lateinit var sizeAdapter: SizeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAppBarVisibilityAndTitle(View.VISIBLE, R.string.details)

        initViews()
        setupRecyclerView()
        setOnClickListener(view)
        addCallBackToExit()
        viewModelObserver()

    }

    private fun initViews() {

        requireContext().loadWithGlide(details_img_food, args.itemFood?.image)

        tv_details_food_name.text = args.itemFood?.name
        tv_price.text = args.itemFood?.price?.toString()?: ""
        food_description.text = args.itemFood?.description


    }

    private fun viewModelObserver() {
        viewModel.addons.observe(viewLifecycleOwner, {
            calculateTotalPrice(
                tv_current_value.text.toString().toInt(),
                sizeAdapter.differ.currentList,
                it as List<AddonModel>
            )
        })


    }

    private fun addCallBackToExit() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callBack)
    }

    private fun setupRecyclerView() {
        sizeAdapter = SizeAdapter()
        carousel_size.apply {
            this.adapter = sizeAdapter
        }
        sizeAdapter.differ.submitList(args.itemFood?.size)
    }

    private fun addOne() {
        var quantity = tv_current_value.text.toString().toInt()
        quantity++
        tv_current_value.text = quantity.toString()
        val addon = viewModel.addons.value
        calculateTotalPrice(quantity, sizeAdapter.differ.currentList, addon ?: listOf())
    }

    private fun subOne() {
        var quantity = tv_current_value.text.toString().toInt()
        if (quantity >= 2)
            quantity--
        tv_current_value.text = quantity.toString()
        val addon = viewModel.addons.value
        calculateTotalPrice(quantity, sizeAdapter.differ.currentList, addon ?: listOf())
    }

    private fun calculateTotalPrice(
        quantity: Int,
        sizes: List<SizeModel>,
        addons: List<AddonModel>
    ) {
        var foodPrice = args.itemFood?.price
        addons.forEach { addonModel ->
            if (addonModel.taken == true) {
                addonModel.price?.let { foodPrice = foodPrice?.plus(it) }
            }
        }
        sizes.forEach { sizeModel ->
            if (sizeModel.taken == true) {
                sizeModel.price.let { foodPrice = foodPrice?.plus(it) }
            }
        }

        val totalPrice = foodPrice?.times(quantity)
        tv_price.text = totalPrice.toString()
    }

    private val callBack: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (activity is HomeActivity) {
                viewModel.addons.postValue(listOf())
               when(navController.previousBackStackEntry?.destination?.label){
                   "fragment_home" -> {
                       navController.navigate(
                           FoodDetailsFragmentDirections.actionNavFoodDetailsFragmentToNavHomeFragment()
                       )
                   }
                   else -> {
                       navController.navigate(
                           FoodDetailsFragmentDirections.actionFoodDetailsFragmentToFoodListFragment(
                               args.category
                           )
                       )
                   }
               }
            }
        }

    }

    private fun setOnClickListener(view: View) {
        btn_plus.setOnClickListener {
            addOne()
        }

        btn_minus.setOnClickListener {
            subOne()
        }

        btn_rate.setOnClickListener {
            btn_rate.apply {
                this.speed = 1f
                this.playAnimation()
            }
            view.postDelayed({
                navController.navigate(
                    FoodDetailsFragmentDirections.actionNavRatingDialogBottomSheetFragment(
                        category = args.category,
                        itemFood = args.itemFood
                    )
                )
            }, 1000)
        }

        btn_select_addon.apply {
            setOnClickListener {
                this.speed = 1f
                this.playAnimation()
                navController.navigate(
                    FoodDetailsFragmentDirections.actionNavAddonDialogBottomSheetFragment(
                        category = args.category,
                        itemFood = args.itemFood
                    )
                )
                this.speed = -1f
                this.playAnimation()
            }
        }

        btn_add_to_cart.apply {
            setOnClickListener {
                speed = 2f
                playAnimation()
                speed = -2f
                playAnimation()
                handleAddFoodToCart(foods = args.itemFood)
            }
        }

        sizeAdapter.setOnItemClickListener {
            val addon = viewModel.addons.value
            calculateTotalPrice(
                tv_current_value.text.toString().toInt(),
                sizeAdapter.differ.currentList,
                addon ?: listOf()
            )
        }

    }

    private fun handleAddFoodToCart(foods: Foods?) {
        val userModel = Constants().getUser(requireContext())

        val selectedSize = sizeAdapter.differ.currentList.filter { it.taken == true }
        val selectedAddon = viewModel.addons.value?.filter { it.taken == true }

        val cartItemSize = returnExtras(selectedSize, selectedAddon, true)
        val cartItemAddon = returnExtras(selectedSize, selectedAddon, false)
        val extraPrice = returnExtrasPrice(selectedSize, selectedAddon).formatPrice().toDouble()

        val cartItem = OrderModel.CartItem()
        cartItem.apply {
            this.foodAddon = cartItemAddon
            this.foodSize = cartItemSize
            this.foodId = foods?.id ?: ""
            this.foodExtraPrice = extraPrice
            this.foodQuantity = tv_current_value.text.toString().toInt()
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
            foods = foods,
            selectedAddon = cartItemAddon,
            selectedSize = cartItemSize,
            extraPrice = extraPrice
        )

        itemsInCartCounter()

    }

    private fun addItemToCart(
        userModel: UserModel?,
        foodId: String?,
        cartItem: OrderModel.CartItem,
        foods: Foods?,
        selectedSize: String,
        selectedAddon: String,
        extraPrice: Double
    ) {
        Timber.d("$userModel $foodId $cartItem $foods $selectedSize $selectedAddon $userModel $extraPrice")
        viewModel.launch(Dispatchers.IO) {
            val cartItemFromDB = viewModel.cartItemDAO.getItemWithAllOptionsInCart(
                userModel?.uid ?: "",
                foodId ?: "",
                selectedSize,
                selectedAddon
            )
            if (cartItem == cartItemFromDB) {
                cartItemFromDB.apply {
                    this.foodAddon = selectedAddon
                    this.foodSize = selectedSize
                    this.foodId = foodId ?: ""
                    this.foodExtraPrice = extraPrice
                    this.foodQuantity = this.foodQuantity + cartItem.foodQuantity
                    this.foodName = foods?.name
                    this.foodPrice = foods?.price
                    this.foodImage = foods?.image
                    this.userPhone = userModel?.phoneNumber
                    this.uid = userModel?.uid ?: ""
                }
                viewModel.launch(Dispatchers.IO) {
                    viewModel.cartItemDAO.upsertItemInCart(cartItemFromDB)
                }
            } else {
                viewModel.launch(Dispatchers.IO) {
                    viewModel.cartItemDAO.upsertItemInCart(cartItem)
                }
            }
            viewModel.launch(Dispatchers.IO) {
                viewModel.cartItemDAO.getAllCart(userModel?.uid ?: "")
            }
        }
    }

    private fun itemsInCartCounter() {
        val uid = Constants().getUid(requireContext()) ?: ""
        viewModel.launch {
            viewModel.countItemInCart(uid = uid)
                .observe(viewLifecycleOwner) {
                    if ((it ?: 0) > 0)
                        setCartCount(it ?: 0, View.VISIBLE)
                    else
                        setCartCount(0, View.GONE)
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Constants().setAddons(requireContext(), listOf())
    }

}