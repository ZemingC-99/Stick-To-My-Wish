//Reference: https://stackoverflow.com/questions/1332269/how-to-set-mobile-system-time-and-date-in-android
package com.example.StickToMyWish

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.StickToMyWish.MyApplication.Companion.darkMode
import com.example.StickToMyWish.MyApplication.Companion.getSharedPreferencesEditor
import com.example.StickToMyWish.MyApplication.Companion.notification
import com.example.StickToMyWish.MyApplication.Companion.notificationAble
import com.example.StickToMyWish.MyApplication.Companion.password
import com.example.StickToMyWish.MyApplication.Companion.passwordAble
import com.example.StickToMyWish.MyApplication.Companion.shareDefault
import com.example.StickToMyWish.databinding.ActivityMain3Binding
import com.example.StickToMyWish.databinding.LayoutTitleBinding
import com.example.StickToMyWish.util.ImageUtil
import java.util.Calendar


class MainActivity3 : BaseActivity() {

    private val TAG = "MainActivity3"

    private lateinit var binding: ActivityMain3Binding
    private lateinit var titleBinding: LayoutTitleBinding

    // Initialize all the layout and onClick listener for the app
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain3Binding.inflate(layoutInflater)
        titleBinding = LayoutTitleBinding.bind(binding.titleLayout.root)
        setContentView(binding.root)

        titleBinding.title.text = "Setting"
        titleBinding.imageArrowLeft.visibility = View.VISIBLE
        titleBinding.imageArrowLeft.setOnClickListener {
            finish()
        }

        binding.notificationLayout.visibility = if (notificationAble) View.VISIBLE else View.GONE
        binding.passwordLayout.visibility = if (passwordAble) View.VISIBLE else View.GONE
        binding.editPassword.setText(if (TextUtils.isEmpty(password)) "" else password)
        binding.darkMode.isChecked = darkMode
        binding.notification.isChecked = notificationAble
        binding.password.isChecked = passwordAble

        binding.notification.setOnCheckedChangeListener { _, isChecked ->
            getSharedPreferencesEditor().putBoolean("notificationAble", isChecked).commit()
            binding.notificationLayout.visibility = if (isChecked) View.VISIBLE else View.GONE
            notificationAble = isChecked
        }

        selectNotificationFrequency(notification)
        selectShareDefault(shareDefault)

        binding.passwordLayout.visibility = if (passwordAble) View.VISIBLE else View.GONE
        binding.password.setOnCheckedChangeListener { _, isChecked ->
            getSharedPreferencesEditor().putBoolean("passwordAble", isChecked).commit()
            binding.passwordLayout.visibility = if (isChecked) View.VISIBLE else View.GONE
            passwordAble = isChecked
        }

        binding.editPassword.addTextChangedListener {
            getSharedPreferencesEditor().putString("password", binding.editPassword.text.toString())
                .commit()
            password = binding.editPassword.text.toString()
        }

        binding.day.setOnClickListener {
            selectNotificationFrequency("day")
            setClock(it)
        }
        binding.hour.setOnClickListener {
            selectNotificationFrequency("hour")
            setClock(it)
        }
        binding.never.setOnClickListener {
            selectNotificationFrequency("never")
        }
        binding.ins.setOnClickListener {
            selectShareDefault("ins")
        }
        binding.facebook.setOnClickListener {
            selectShareDefault("facebook")
        }
        binding.twitter.setOnClickListener {
            selectShareDefault("twitter")
        }

        changeDarkMode(darkMode)
        binding.darkMode.setOnCheckedChangeListener { _, isChecked ->
            getSharedPreferencesEditor().putBoolean("darkMode", isChecked).commit()
            darkMode = isChecked
            changeDarkMode(isChecked)
        }

    }

    //Select the frequency of notification
    private fun selectNotificationFrequency(frequency: String) {
        binding.day.setBackgroundColor(Color.WHITE)
        binding.hour.setBackgroundColor(Color.WHITE)
        binding.never.setBackgroundColor(Color.WHITE)

        getSharedPreferencesEditor().putString("notification", frequency).commit()
        notification = frequency

        when (frequency) {
            "day" -> binding.day.setBackgroundColor(Color.BLUE)

            "hour" -> binding.hour.setBackgroundColor(Color.BLUE)

            "never" -> binding.never.setBackgroundColor(Color.BLUE)
        }

    }

    //choose in setting page for sharing default
    //not in used
    private fun selectShareDefault(shareRoad: String?) {
        var value: String? = shareRoad
        binding.ins.setBackgroundColor(Color.WHITE)
        binding.facebook.setBackgroundColor(Color.WHITE)
        binding.twitter.setBackgroundColor(Color.WHITE)

        if (value.equals(shareDefault)) {
            value = null
        }

        getSharedPreferencesEditor().putString("shareDefault", value).commit()
        shareDefault = value

        when (value) {
            "ins" -> binding.ins.setBackgroundColor(Color.BLUE)

            "facebook" -> binding.facebook.setBackgroundColor(Color.BLUE)

            "twitter" -> binding.twitter.setBackgroundColor(Color.BLUE)
        }

    }

    //Set clock and calendar view when needed
    fun setClock(view: View?) {
        val alarmManager: AlarmManager;
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager;

        val calendar: Calendar = Calendar.getInstance()
        val hour: Int = calendar.get(Calendar.HOUR_OF_DAY)
        val minute: Int = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this, { view, hourOfDay, minute ->
                val c: Calendar = Calendar.getInstance()
                c.set(Calendar.HOUR_OF_DAY, hourOfDay)
                c.set(Calendar.MINUTE, minute)
                val intent = Intent(this, AlarmReceiver::class.java)
                val pendingIntent: PendingIntent
                pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.getActivity(this, 0X102, intent, PendingIntent.FLAG_IMMUTABLE)
                } else {
                    PendingIntent.getActivity(this, 0X102, intent,
                        PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
                }
                alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent)
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
            }, hour, minute, true
        )
        timePickerDialog.show()
    }

    // Design some color for dark mode
    // Assign them to each components
    private fun changeDarkMode(open: Boolean) {

        val backgroundColor: Int =
            ContextCompat.getColor(this, if (open) R.color.black else R.color.white)
        val displayColor: Int =
            ContextCompat.getColor(this, if (open) R.color.white else R.color.black)

        val leftArrow = ContextCompat.getDrawable(this, R.drawable.arrow_left)
        val rightArrow = ContextCompat.getDrawable(this, R.drawable.arrow_right)

        binding.titleLayout.layout1.setBackgroundColor(backgroundColor)
        binding.titleLayout.title.setTextColor(displayColor)
        binding.titleLayout.view1.setBackgroundColor(displayColor)
        val drawable1: Drawable? = ImageUtil().tintDrawable(leftArrow, displayColor)
        binding.titleLayout.imageArrowLeft.setImageDrawable(drawable1)
        val drawable2: Drawable? = ImageUtil().tintDrawable(rightArrow, displayColor)
        binding.titleLayout.imageArrowRight.setImageDrawable(drawable2)
        binding.view2.setTextColor(displayColor)
        binding.view3.setTextColor(displayColor)
        //binding.day.setTextColor(displayColor)
        //binding.hour.setTextColor(displayColor)
        //binding.never.setTextColor(displayColor)
        binding.view4.setTextColor(displayColor)
        binding.view5.setTextColor(displayColor)
        binding.view6.setBackgroundColor(backgroundColor)

        if (open){
            binding.notification.trackTintMode = PorterDuff.Mode.SCREEN
            binding.password.trackTintMode = PorterDuff.Mode.SCREEN
            binding.darkMode.trackTintMode = PorterDuff.Mode.SCREEN
        }else{
            binding.notification.trackTintMode = PorterDuff.Mode.SCREEN
            binding.password.trackTintMode = PorterDuff.Mode.SCREEN
            binding.darkMode.trackTintMode = PorterDuff.Mode.SCREEN
        }

        //binding.facebook.setTextColor(displayColor)
        //binding.ins.setTextColor(displayColor)
        //binding.twitter.setTextColor(displayColor)

    }

}