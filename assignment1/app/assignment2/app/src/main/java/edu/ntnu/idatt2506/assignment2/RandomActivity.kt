package edu.ntnu.idatt2506.assignment2

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class RandomActivity : Activity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val upperLimit = intent.getIntExtra("UPPER_LIMIT", 100)
    val value = (0..upperLimit).random()

    setResult(RESULT_OK, Intent().putExtra("RANDOM_VALUE", value))

    finish()

    //Toast.makeText(this, "Random number: $value", Toast.LENGTH_LONG).show();
  }
}