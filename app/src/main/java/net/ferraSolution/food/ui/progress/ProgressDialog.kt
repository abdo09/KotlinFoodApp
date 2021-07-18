package net.ferraSolution.food.ui.progress

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import net.ferraSolution.food.R

class ProgressDialog(context: Context?) : AlertDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //setCancelable(true)
        setContentView(R.layout.prgress_dialog_layout)
    }


    init {
        setCanceledOnTouchOutside(false)
    }
}