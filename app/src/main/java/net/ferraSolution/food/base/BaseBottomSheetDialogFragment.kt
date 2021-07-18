
package net.ferraSolution.food.base

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import timber.log.Timber


open class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var mBehavior: BottomSheetBehavior<View>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }


    fun getScreenHeight(): Int { // Calculate window height for fullscreen use
        return resources.displayMetrics.heightPixels
    }


    fun toggleKeyboard(activity: Activity, show: Boolean? = null) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        Timber.d("imm -> ${imm.isActive}        show $show")
        if (show != null) {
            if (!show) {
                Timber.d("hideKeyboard?")
                imm.hideSoftInputFromWindow(view?.windowToken, 0) // hide
            } else {
                imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY) // show
            }
        } else {
            if (imm.isActive) {
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0) // hide
            } else {
                imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY) // show
            }
        }
    }
}