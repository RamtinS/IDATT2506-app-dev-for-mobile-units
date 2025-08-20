package edu.ntnu.idatt2506.assignment2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {

  private lateinit var number1TextView: TextView
  private lateinit var number2TextView: TextView
  private lateinit var answerEditText: EditText
  private lateinit var upperLimitEditText: EditText

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    number1TextView = findViewById(R.id.number1TextView)
    number2TextView = findViewById(R.id.number2TextView)
    answerEditText = findViewById(R.id.answerEditText)
    upperLimitEditText = findViewById(R.id.upperLimitEditText)
  }

  fun onAdderClick(view: View?) {
    val number1 = number1TextView.text.toString().toInt();
    val number2 = number2TextView.text.toString().toInt()
    val answer = answerEditText.text.toString().toInt()

    val correctAnswer = number1 + number2

    if (answer == correctAnswer) {
      Toast.makeText(this, getString(R.string.right), Toast.LENGTH_LONG).show()
    } else {
      Toast.makeText(this, getString(R.string.wrong, correctAnswer), Toast.LENGTH_LONG).show()
    }

    generateRandomNumber()
  }

  fun onMultiplierClick(view: View?) {
    val number1 = number1TextView.text.toString().toInt();
    val number2 = number2TextView.text.toString().toInt()
    val answer = answerEditText.text.toString().toInt()

    val correctAnswer = number1 * number2

    if (answer == correctAnswer) {
      Toast.makeText(this, getString(R.string.right), Toast.LENGTH_LONG).show()
    } else {
      Toast.makeText(this, getString(R.string.wrong, correctAnswer), Toast.LENGTH_LONG).show()
    }

    generateRandomNumber()
  }

  private fun generateRandomNumber() {
    val upperLimit = upperLimitEditText.text.toString().toInt()

    val intent1 = Intent(this, RandomActivity::class.java).apply {
      putExtra("UPPER_LIMIT", upperLimit)
    }
    startActivityForResult(intent1, 1)

    val intent2 = Intent(this, RandomActivity::class.java).apply {
      putExtra("UPPER_LIMIT", upperLimit)
    }
    startActivityForResult(intent2, 2)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (resultCode == RESULT_OK) {
      val randomValue = data?.getIntExtra("RANDOM_VALUE", 0)
      when (requestCode) {
        1 -> number1TextView.text = randomValue.toString()
        2 -> number2TextView.text = randomValue.toString()
      }
    }
  }

}