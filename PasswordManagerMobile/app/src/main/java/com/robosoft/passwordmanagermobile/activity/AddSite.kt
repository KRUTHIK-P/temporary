package com.robosoft.passwordmanagermobile.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*
import com.robosoft.passwordmanagermobile.R
import com.robosoft.passwordmanagermobile.databinding.ActivityAddSiteBinding

class AddSite : AppCompatActivity() {

    private lateinit var binding: ActivityAddSiteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSiteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val backBtn = binding.toolbar.getChildAt(1)
        backBtn.setOnClickListener {
            onBackPressed()
        }
        SpinnerData()

        binding.passwordToggle.setOnClickListener {
            if (binding.sitePassEt.inputType == InputType.TYPE_CLASS_TEXT){
                binding.passwordToggle.setImageResource(R.drawable.ic_baseline_visibility_off_24)
                binding.sitePassEt.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            else{
                binding.passwordToggle.setImageResource(R.drawable.ic_baseline_visibility_24)
                binding.sitePassEt.inputType = InputType.TYPE_CLASS_TEXT
            }
        }

        binding.resetBtn.setOnClickListener {
            binding.urlEdt.text.clear()
            binding.siteNameEt.text.clear()
            binding.userNameEt.text.clear()
            binding.sitePassEt.text.clear()
            binding.notesEt.text.clear()
            binding.folderSpiner.isSelected = false
        }

        binding.saveBtn.setOnClickListener {

            Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
    }

    private fun SpinnerData(){
        val folderData = arrayOf("","Social Media", "Wikipedia", "Educational Sector", "Economic Sector", "Stock Market Sector")
        val spinner = findViewById<Spinner>(R.id.folderSpiner)
        val arraydapter = ArrayAdapter<String>(this, R.layout.spinner_text, folderData)
        arraydapter.setDropDownViewResource(R.layout.spinner_text)
        spinner.adapter = arraydapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {}

            override fun onNothingSelected(p0: AdapterView<*>?) {}

        }
    }
}