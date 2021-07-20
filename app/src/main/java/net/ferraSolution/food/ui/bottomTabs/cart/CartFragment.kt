package net.ferraSolution.food.ui.bottomTabs.cart

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_foods.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.ferraSolution.food.R
import net.ferraSolution.food.base.BaseSupportFragment
import net.ferraSolution.food.data.models.OrderModel
import net.ferraSolution.food.ui.HomeActivity
import net.ferraSolution.food.ui.bottomTabs.menu.foodsList.adapter.FoodsAdapter
import net.ferraSolution.food.ui.common.SwipeToDeleteCallback
import net.ferraSolution.food.utils.Constants
import net.ferraSolution.food.utils.fadeIn
import net.ferraSolution.food.utils.fadeOut
import net.ferraSolution.food.utils.formatPrice
import org.koin.android.viewmodel.ext.android.viewModel

class CartFragment : BaseSupportFragment(R.layout.fragment_cart) {

    override val viewModel by viewModel<CartFragmentViewModel>()

    private lateinit var cartAdapter: CartAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAppBarVisibilityAndTitle(View.VISIBLE, R.string.cart)
        navigationVisibility = View.VISIBLE

        initViews()
        setupRecyclerView()
        viewModelObserver()
        addCallBackToExit()
        onClickListener()
    }

    private fun onClickListener() {
        cartAdapter.setOnItemClickListener {cartList ->
            calculateTotalPrice(cartAdapter.differ.currentList)
        }
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter()

        rv_cart.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }

        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToDelete = cartAdapter.differ.currentList[viewHolder.adapterPosition]
                viewModel.launch(Dispatchers.IO) {
                    viewModel.cartRoomRepository.deleteItemInCart(itemToDelete)
                    viewModel.getAllCart(Constants().getUid(requireContext()))
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(rv_cart)
    }

    private fun initViews() {
        val uid = Constants().getUid(requireContext())
        viewModel.launch(Dispatchers.IO) {
            viewModel.getAllCart(uid)
        }
    }

    private fun viewModelObserver() {
        viewModel.allCart.observe(viewLifecycleOwner, {
            if (it.isNullOrEmpty()) {
                card_recycler_container.fadeOut(300)
                card_price_container.fadeOut(300)
                empty_layout.fadeIn(300)
            } else {
                empty_layout.fadeOut(300)
                card_recycler_container.fadeIn(300)
                card_price_container.fadeIn(300)
                cartAdapter.differ.submitList(it)
                calculateTotalPrice(it)
            }
        })
    }

    private fun addCallBackToExit() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callBack)
    }

    private val callBack: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (activity is HomeActivity) {
                val homeActivity = activity as HomeActivity
                navController.navigate(R.id.nav_homeFragment)
                homeActivity.bottomNavigationView.selectedItemId = R.id.navigation_home
            }
        }

    }

    private fun calculateTotalPrice(cartList: List<OrderModel.CartItem>){
        var totalPrice = 0.0

        cartList.forEach { cartItem ->
            totalPrice += (cartItem.foodPrice?.toDouble()
                ?.let { cartItem.foodExtraPrice?.plus(it) }
                ?: 0.0).times(cartItem.foodQuantity.toString().toInt())
            tv_cart_price.text = totalPrice.formatPrice()
        }
    }

}