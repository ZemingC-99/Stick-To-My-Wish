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

// BaseActivity is an extension of ComponentActivity to provide common functionality across activities,
// including a password check feature.
open class BaseActivity : ComponentActivity() {

    private val TAG = "BaseActivity"

    override fun onRestart() {
        super.onRestart()
        // Handles activity restarts. If password protection is enabled, prompts the user for a password.
        if (passwordAble) {
            // Sets up a dialog to input the password. If the entered password is incorrect, displays a toast message and closes the activity.
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

    // Sets dialog properties like dimensions and gravity to control its appearance on screen.
    // 'gravity' parameter determines the position of the dialog.
    fun setDialogTouchOutsideCloseable(dialog: Dialog, gravity: Int) {
        val window = dialog.window
        val params = window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        window.attributes = params
        window.setGravity(gravity)
    }

}