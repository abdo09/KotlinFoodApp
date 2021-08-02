package net.ferraSolution.food.ui.bottomSheet


import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.details_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.ferraSolution.food.R
import net.ferraSolution.food.base.BaseSupportFragment
import net.ferraSolution.food.data.models.Address
import net.ferraSolution.food.data.models.OrderModel
import net.ferraSolution.food.data.models.UserModel
import net.ferraSolution.food.ui.HomeActivity
import net.ferraSolution.food.ui.map.AddressMapViewModel
import net.ferraSolution.food.utils.*
import org.koin.android.viewmodel.ext.android.viewModel

class PlaceOrderFragment : BaseSupportFragment(R.layout.details_fragment) {
    private var isCOD = true

    override val viewModel by viewModel<AddressMapViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as HomeActivity).app_bar_layout.visibility = View.GONE

        viewModel.apply {
            launch(Dispatchers.IO) {
                getAllCart(uid = Constants().getUid(requireContext()))
            }
        }

        setOnClickListener()
        setGrayBoarderToField()
        viewModelObserver()
        addCallBackToExit()
    }

    private fun viewModelObserver() {
        viewModel.userAddressAdded.observe(viewLifecycleOwner, {
        })

        viewModel.orderPlaced.observe(viewLifecycleOwner, { orderPlaced ->
            if (orderPlaced) {
                CookieBarConfig(requireActivity()).showDefaultSuccessCookie("Order placed successfully")
                viewModel.launch(Dispatchers.IO) { viewModel.cartRoomRepository.cleanCart(Constants().getUid(requireContext())?: "") }
                navController.navigate(PlaceOrderFragmentDirections.actionNavAddressDetailsBottomSheetFragmentToNavHomeFragment())
                (activity as HomeActivity).bottomNavigationView.selectedItemId = R.id.navigation_home
            }
        })
    }

    private fun setOnClickListener() {

        btn_cod.setOnClickListener {
            if (!isCOD) {
                isCOD = true
                setChecked(
                    btn1 = btn_cod,
                    btn2 = btn_braintree,
                    tv1 = tv_cod,
                    tv2 = tv_braintree,
                    img1 = image_cod,
                    img2 = image_braintree,
                    isChecked = isCOD,
                    requireContext()
                )
            }
        }

        btn_braintree.setOnClickListener {
            CookieBarConfig(requireActivity()).showDefaultInfoCookie("For now only cash on delivery")
            /*if (isCOD) {
                isCOD = false
                setChecked(
                    btn1 = btn_cod,
                    btn2 = btn_braintree,
                    tv1 = tv_cod,
                    tv2 = tv_braintree,
                    img1 = image_cod,
                    img2 = image_braintree,
                    isChecked = isCOD,
                    requireContext()
                )
            }*/
        }

        place_order.setOnClickListener {
            if (isEntriesValidated()) {
                updateUserAddress()
                var userModel: UserModel?
                var latLng: LatLng?
                Constants().apply {
                    latLng = getLatLng(requireContext())
                    userModel = getUser(requireContext())
                }
                val order = setUpOrder(userModel = userModel, cartList = viewModel.cartList, latLng = latLng)
                viewModel.placeOrder(orderNumber = createOrderNumber(), orderModel = order)
            }
        }
    }

    private fun setUpOrder(
        userModel: UserModel?,
        cartList: List<OrderModel.CartItem>?,
        latLng: LatLng?
    ): OrderModel {
        val orderModel = OrderModel()
        val finalPrice = calculateTotalPrice(cartList?: listOf())
        if (isCOD) {
            orderModel.apply {
                cartItemList = cartList
                cod = true
                comment = ""
                finalPayment = finalPrice
                lat = latLng?.latitude
                lng = latLng?.longitude
                shippingAddress = Constants().getAddress(requireContext()).toString()
                totalPayment = finalPrice
                transactionId = "Cash On Delivery"
                userId = userModel?.uid
                userPhone = userModel?.phoneNumber
                userName = "${userModel?.firstName} ${userModel?.lastName}"
                orderTimeStamp = (System.currentTimeMillis()).toString()
            }
        }
        return orderModel
    }

    private fun updateUserAddress() {
        val address = Address()
        val latLng = Constants().getLatLng(requireContext())
        val uid = Constants().getUid(requireContext())
        address.apply {
            latLang = latLng
            city = ed_address_details_city.text.toString()
            street = ed_address_details_street.text.toString()
            district = ed_address_details_district.text.toString()
            buildingNo = ed_address_details_shipping_building_no.text.toString()
        }
        Constants().setAddress(requireContext(), address)
        viewModel.uploadUserAddress(uid ?: "", address)
    }

    //Check fields are validated
    private fun isEntriesValidated(): Boolean {
        when {
            ed_address_details_city.text.toString().isEmpty() -> {
                ip_address_details_city.setRedBoarder(R.string.city)
                return false
            }
            ed_address_details_street.text.toString().isEmpty() -> {
                ip_address_details_street.setRedBoarder(R.string.street)
                return false
            }
            ed_address_details_district.text.toString().isEmpty() -> {
                ip_address_details_district.setRedBoarder(R.string.district)
                return false
            }
            ed_address_details_shipping_building_no.text.toString().isEmpty() -> {
                ip_address_details_shipping_address_details.setRedBoarder(R.string.shipping_address_details)
                return false
            }
            else -> return true
        }
    }

    //Set default boarder
    private fun setGrayBoarderToField() {
        ed_address_details_city.setGoldColorBoarder(R.string.city, ip_address_details_city)
        ed_address_details_street.setGoldColorBoarder(R.string.street, ip_address_details_street)
        ed_address_details_district.setGoldColorBoarder(
            R.string.district,
            ip_address_details_district
        )
        ed_address_details_shipping_building_no.setGoldColorBoarder(
            R.string.shipping_address_details,
            ip_address_details_shipping_address_details
        )
    }

    private fun calculateTotalPrice(cartList: List<OrderModel.CartItem>): Double {
        var totalPrice = 0.0

        cartList.forEach { cartItem ->
            totalPrice += (cartItem.foodPrice?.toDouble()
                ?.let { cartItem.foodExtraPrice?.plus(it) }
                ?: 0.0).times(cartItem.foodQuantity.toString().toInt())
        }
        return totalPrice.formatPrice().toDouble()
    }

    private fun addCallBackToExit() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callBack)
    }

    private val callBack: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (activity is HomeActivity) {
                navController.navigate(PlaceOrderFragmentDirections.actionNavAddressDetailsBottomSheetFragmentToNavAddressMapFragment(""))
            }
        }

    }

}