package com.example.githubuserapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_setting.*


class SettingActivity : AppCompatActivity() {

    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val setting = "Pengaturan"
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.title = setting
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        alarmReceiver = AlarmReceiver()
        if(alarmReceiver.isAlarmSet(this)){
            switch_reminder.isChecked = true
        }
        switch_reminder.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                alarmReceiver.setRepeatingAlarm(this, "09:00")
            }
            else{
                alarmReceiver.cancelAlarm(this)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
