package com.example.StickToMyWish

import android.app.AlertDialog
import android.app.Dialog
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.StickToMyWish.MyApplication.Companion.password
import com.example.StickToMyWish.MyApplication.Companion.passwordAble


open class BaseActivity : ComponentActivity() {

    private val TAG = "BaseActivity"

    override fun onRestart() {
        super.onRestart()

        if (passwordAble) {
            val view: View = layoutInflater.inflate(R.layout.half_dialog_view, null)
            val editText = view.findViewById<View>(R.id.dialog_edit) as EditText
            val dialog: AlertDialog =
                AlertDialog.Builder(this).setTitle("please input your password").setView(view)
                    .setPositiveButton("confirm") { dialog, which ->
                        val content = editText.text.toString()
                        if (content != password) {
                            Toast.makeText(this, "wrong password!", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            dialog.dismiss()
                        }
                    }.create()
            setDialogTouchOutsideCloseable(dialog, Gravity.CENTER);
            dialog.show()
        }

    }

    fun setDialogTouchOutsideCloseable(dialog: Dialog, gravity: Int) {
        val window = dialog.window
        val params = window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        window.attributes = params
        window.setGravity(gravity)
    }

}