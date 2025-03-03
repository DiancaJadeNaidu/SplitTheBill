package com.dianca.splitthebill

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val totalBillInput = findViewById<EditText>(R.id.totalBillInput)
        val tipPercentageInput = findViewById<EditText>(R.id.tipPercentageInput)
        val numPeopleInput = findViewById<EditText>(R.id.numPeopleInput)
        val paymentMethodGroup = findViewById<RadioGroup>(R.id.paymentMethodGroup)
        val individualPaymentsLayout = findViewById<LinearLayout>(R.id.individualPaymentsLayout)
        val addPersonButton = findViewById<Button>(R.id.addPersonButton)
        val calculateButton = findViewById<Button>(R.id.calculateButton)
        val resultText = findViewById<TextView>(R.id.resultText)

        val peopleList = mutableListOf<Pair<EditText, EditText>>()

        paymentMethodGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.payIndividually) {
                individualPaymentsLayout.visibility = View.VISIBLE
                addPersonButton.visibility = View.VISIBLE
                numPeopleInput.visibility = View.GONE
            } else {
                individualPaymentsLayout.visibility = View.GONE
                addPersonButton.visibility = View.GONE
                numPeopleInput.visibility = View.VISIBLE
            }
        }

        addPersonButton.setOnClickListener {
            val nameInput = EditText(this)
            nameInput.hint = "Enter Name"
            val amountInput = EditText(this)
            amountInput.hint = "Enter Amount"
            amountInput.inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL

            individualPaymentsLayout.addView(nameInput)
            individualPaymentsLayout.addView(amountInput)
            peopleList.add(Pair(nameInput, amountInput))
        }

        calculateButton.setOnClickListener {
            val tip = tipPercentageInput.text.toString().toDoubleOrNull() ?: 0.0

            if (paymentMethodGroup.checkedRadioButtonId == R.id.splitEqually) {
                val bill = totalBillInput.text.toString().toDoubleOrNull()
                val people = numPeopleInput.text.toString().toIntOrNull()

                if (bill == null || bill <= 0 || people == null || people <= 0) {
                    resultText.text = "Please enter valid inputs."
                    return@setOnClickListener
                }

                val totalWithTip = bill + (bill * tip / 100)
                val perPerson = totalWithTip / people
                resultText.text = "Each person pays: R%.2f".format(perPerson)
            } else {
                val breakdown = StringBuilder()
                for ((name, amount) in peopleList) {
                    val personName = name.text.toString()
                    val personAmount = amount.text.toString().toDoubleOrNull() ?: 0.0
                    val amountWithTip = personAmount + (personAmount * tip / 100)
                    breakdown.append("$personName owes: R%.2f\n".format(amountWithTip))
                }
                resultText.text = breakdown.toString()
            }
        }
    }
}



