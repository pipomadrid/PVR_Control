package com.pedrosaez.pvr_control.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pedrosaez.pvr_control.R
import com.pedrosaez.pvr_control.databinding.ActivityCheckMachineBinding
import com.pedrosaez.pvr_control.databinding.ActivityHomeBinding




class CheckMachineActivity : AppCompatActivity() {


    private lateinit var binding: ActivityCheckMachineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_machine)

        binding = ActivityCheckMachineBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val btNew = binding.btMachineNew
        val btUsed = binding.btMachineUsed
        val prefs = this.getSharedPreferences((this.getString(R.string.prefs_file)), Context.MODE_PRIVATE)
        val prefFlag = this.getSharedPreferences((this.getString(R.string.prefs_file_flag)), Context.MODE_PRIVATE)
        val pvrName = prefs.getString("pvrName","")

        btNew.setOnClickListener {

            with(prefFlag.edit()) {
                putBoolean(pvrName, true)
                apply()
            }

            val intent: Intent = Intent(this,PvrInfoActivity::class.java)
            intent.putExtra("newMachine",true)
            startActivity(intent)

        }

        btUsed.setOnClickListener {
            with(prefFlag.edit()) {
                putBoolean(pvrName, true)
                apply()
            }

            val intent: Intent = Intent (this,PvrInfoActivity::class.java)
            intent.putExtra("newMachine",false)
            startActivity(intent)
        }

    }


}