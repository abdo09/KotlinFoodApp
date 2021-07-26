package net.ferraSolution.food.ui.bottomSheet


import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_addon_dialog_bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_rating_dialog_bottom_sheet.btn_cancel_dialog
import net.ferraSolution.food.R
import net.ferraSolution.food.ui.bottomTabs.menu.foodsList.foodDetails.bottomSheet.adapters.AddonAdapter
import net.ferraSolution.food.ui.bottomTabs.menu.foodsList.FoodsListFragmentViewModel
import net.ferraSolution.food.utils.Constants
import org.koin.android.viewmodel.ext.android.sharedViewModel

class AddonBottomSheetFragment : BottomSheetDialogFragment() {

    private val viewModel by sharedViewModel<FoodsListFragmentViewModel>()

    private val args by navArgs<AddonBottomSheetFragmentArgs>()

    private lateinit var addonAdapter: AddonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.MyBottomSheetDialogTheme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object: BottomSheetDialog(requireContext(), theme) {
            override fun onBackPressed() {
                dismiss()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_addon_dialog_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setOnClickListener()
    }

    private fun setOnClickListener() {

        btn_cancel_dialog.setOnClickListener {
            dismiss()
        }

        addonAdapter.setOnItemClickListener {
            Constants().setAddons(requireContext(), addonAdapter.differ.currentList)
            viewModel.addons.postValue(addonAdapter.differ.currentList)
        }

    }

    private fun setupRecyclerView() {
        addonAdapter = AddonAdapter()
        carousel_addon.apply {
            this.adapter = addonAdapter
        }
        addonAdapter.differ.submitList(args.itemFood?.addon)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        Constants().setAddons(requireContext(), addonAdapter.differ.currentList)
    }

}