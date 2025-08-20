package edu.ntnu.idatt2506.assignment2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView

class TestRandomActivity : Activity() {

  private lateinit var resultTextView : TextView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_test)

    resultTextView = findViewById(R.id.resultTextView)

    val intent = Intent(this, RandomActivity::class.java).apply {
      putExtra("UPPER_LIMIT", 100)
    }

    startActivityForResult(intent, 1)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (requestCode == 1 && resultCode == RESULT_OK) {
      val randomValue = data?.getIntExtra("RANDOM_VALUE", -1)
      resultTextView.text = getString(R.string.random_value_display, randomValue)
    }
  }

}