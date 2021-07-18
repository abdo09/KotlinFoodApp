package net.ferraSolution.food.ui.bottomTabs.menu.foodsList.foodDetails.bottomSheet


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_rating_dialog_bottom_sheet.*
import net.ferraSolution.food.R
import net.ferraSolution.food.ui.bottomTabs.menu.foodsList.foodDetails.RatingDialogViewModel
import net.ferraSolution.food.utils.approachDoubleValue
import net.ferraSolution.food.utils.format
import org.koin.android.viewmodel.ext.android.viewModel

class RatingDialogBottomSheetFragment : BottomSheetDialogFragment() {

    private val viewModel by viewModel<RatingDialogViewModel>()

    private val args by navArgs<RatingDialogBottomSheetFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.MyBottomSheetDialogTheme)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rating_dialog_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
    }

    private fun setOnClickListener() {

        btn_cancel_dialog.setOnClickListener {
            dismiss()
        }

        btn_submit_rating.setOnClickListener {
            viewModel.updateRate(args.category?.menuId.toString(), args.itemFood?.id.toString(), get_rate_from_rating_bar.rating)

        }

        viewModel.foodRate.observe(viewLifecycleOwner, {
            val approachValue = it.approachDoubleValue()
            var a = it.format(1).dropLast(2).toDouble()
            when (approachValue) {
                1.0 -> {
                    a = a.plus(1.0)
                }
                0.5 -> {
                    a = a.plus(0.5)
                }
                0.0 -> {
                    a = a.plus(0.0)
                }
            }
            Toast.makeText(requireContext(), a.toString(), Toast.LENGTH_SHORT).show()
        })

    }

}