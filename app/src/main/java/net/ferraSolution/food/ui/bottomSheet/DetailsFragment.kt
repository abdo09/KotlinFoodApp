package net.ferraSolution.food.ui.bottomSheet


import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_home.app_bar_layout
import kotlinx.android.synthetic.main.details_fragment.*
import net.ferraSolution.food.R
import net.ferraSolution.food.base.BaseSupportFragment
import net.ferraSolution.food.data.models.Address
import net.ferraSolution.food.ui.HomeActivity
import net.ferraSolution.food.ui.map.AddressMapViewModel
import net.ferraSolution.food.utils.*
import org.koin.android.viewmodel.ext.android.viewModel

class DetailsFragment : BaseSupportFragment(R.layout.details_fragment) {
    private var isCOD = true

    override val viewModel by viewModel<AddressMapViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as HomeActivity).app_bar_layout.visibility = View.GONE

        setOnClickListener()
        setGrayBoarderToField()
        viewModelObserver()
    }

    private fun viewModelObserver() {
        viewModel.userAddressAdded.observe(viewLifecycleOwner, {
            CookieBarConfig(requireActivity()).showDefaultSuccessCookie("Successful")
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
            if (isCOD) {
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
            }
        }

        add_address_location.setOnClickListener {
            if (isEntriesValidated()) {
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
        }
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

}