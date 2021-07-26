package net.ferraSolution.food.ui.bottomSheet


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_confirmation_dialog_bottom_sheet.*
import net.ferraSolution.food.R
import net.ferraSolution.food.ui.bottomTabs.cart.CartFragmentViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class ConfirmationBottomSheetFragment : BottomSheetDialogFragment() {

    private val viewModel by viewModel<CartFragmentViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.MyBottomSheetDialogTheme)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirmation_dialog_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
    }

    private fun setOnClickListener() {

        btn_cancel_dialog.setOnClickListener {
            dismiss()
        }

        btn_confirm.setOnClickListener {
            viewModel.deleteItem.postValue(true)
            dismiss()
        }

        btn_no.setOnClickListener {
            viewModel.deleteItem.postValue(false)
            dismiss()
        }

    }

}