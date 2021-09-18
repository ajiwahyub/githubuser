package com.submisi2.githubuser

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.submisi2.githubuser.notification.AlarmReceiver
import kotlinx.android.synthetic.main.activity_notification.*


class NotificationActivity : AppCompatActivity(){

    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var mPreferences: SharedPreferences

    companion object {
        const val PREF_SET = "SettingPref"
        private const val REPEATING_TYPE = "repeating"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        setActionBar(title = "Notification Setting")
        alarmReceiver = AlarmReceiver()
        mPreferences = getSharedPreferences(PREF_SET,Context.MODE_PRIVATE)
        init()
        switchReminder.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                alarmReceiver.setRepeatingAlarm(this,"09:00")
        }else {
                alarmReceiver.cancelAlarm(this)
            }
            saveChange(isChecked)
        }
    }
    private fun init() {
        switchReminder.isChecked = mPreferences.getBoolean(REPEATING_TYPE, false)
    }
    private fun setActionBar(title: String) {
        val icon = ResourcesCompat.getDrawable(resources, R.drawable.logo, null)?.toBitmap()
        val fixedIcon = BitmapDrawable(resources,
            icon.let { Bitmap.createScaledBitmap(it!!, 60, 60, true) })
        supportActionBar?.setHomeAsUpIndicator(fixedIcon)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (supportActionBar != null) {
            (supportActionBar as ActionBar).title = title
        }
    }

    private fun saveChange(value: Boolean) {
        val temp = mPreferences.edit()
        temp.putBoolean(REPEATING_TYPE, value)
        temp.apply()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

}
