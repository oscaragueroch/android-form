package com.example.form

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import com.example.form.databinding.ActivityMainBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
private  lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val countries = arrayOf("Argentina", "Bolivia", "Chile", "Colombia", "Ecuador", "España",
            "Estados Unidos", "Mexico", "Panama", "Peru", "Uruguay")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
            countries)

        binding.actvCountries.setAdapter(adapter)
        binding.actvCountries.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        binding.actvCountries.setOnItemClickListener { adapterView, view, i, l ->
           binding.etPlaceBirth.requestFocus()

        }

        binding.etDateBirth.setOnClickListener{
            val builder = MaterialDatePicker.Builder.datePicker()
            val picker = builder.build()

            picker.addOnPositiveButtonClickListener{ timeInMilliseconds ->
                val dateStr = SimpleDateFormat("dd/MM/yyyy",
                    Locale.getDefault()).apply {
                        timeZone = TimeZone.getTimeZone("UTC")
                }.format(timeInMilliseconds)

                binding.etDateBirth.setText(dateStr)
            }

            picker.show(supportFragmentManager, picker.toString())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main , menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
if (item.itemId == R.id. action_send ){
    if(validaFields()) {
        val name: String = findViewById<TextInputEditText>(R.id.etName).text.toString().trim()
        val surname = binding.etSurname.text.toString().trim()
        val height = binding.etHeight.text.toString().trim()
        val dateBirth = binding.etDateBirth.text.toString().trim()
        val country = binding.actvCountries.text.toString().trim()
        val placeBirth = binding.etPlaceBirth.text.toString()
        val notes = binding.etNotes.text.toString().trim()
        //Toast.makeText(this,"$name $surname", Toast.LENGTH_SHORT).show()


        val buldier: AlertDialog.Builder = AlertDialog.Builder(this)
        buldier.setTitle(getString(R.string.dialog_title))
        buldier.setMessage(joinData(name, surname, height, dateBirth, country, placeBirth, notes))
        buldier.setPositiveButton(getString(R.string.dialog_ok)) { dialogInterface, i ->
            with(binding) {
                etName.text?.clear()
                etSurname.text?.clear()
                etHeight.text?.clear()
                etDateBirth.text?.clear()
                actvCountries.text?.clear()
                etPlaceBirth.text?.clear()
                etNotes.text?.clear()


            }


        }

        buldier.setNegativeButton(getString(R.string.dialog_cancel), null)

        val dialog: AlertDialog = buldier.create()
        dialog.show()
    }
}
        return super.onOptionsItemSelected(item)
    }
    private fun joinData(vararg fields : String):String{
        var result = ""

        fields.forEach {fields ->
            if (fields.isNotEmpty()){
           result += "$fields\n"
            }
        }



        return result

    }

    private fun validaFields(): Boolean {
        var isValid = true

        if (binding.etHeight.text.isNullOrEmpty()) {
            binding.tilHeight.run {
                error = getString(R.string.help_required)
                requestFocus()
            }
            isValid = false
        } else if (binding.etHeight.text.toString().toInt() < 50) {
            binding.tilHeight.run {
                error =getString (R.string.help_min_height_valid)
                requestFocus()
            }
            isValid = false

        }  else {
            binding.tilHeight.error = null
        }
        if (binding.etSurname.text.isNullOrEmpty()) {
            binding.tilSurname.run {
                error = getString(R.string.help_required)
                requestFocus()
            }
            isValid = false
        } else {
            binding.tilSurname.error = null
        }
        if (binding.etName.text.isNullOrEmpty()){
            binding.tilName.run {
                error = getString(R.string.help_required)
                requestFocus()
            }
        isValid = false
    } else{
        binding.tilName.error = null
    }
        return isValid
    }


}