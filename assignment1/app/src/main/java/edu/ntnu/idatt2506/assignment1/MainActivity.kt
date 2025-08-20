package edu.ntnu.idatt2506.assignment1

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    // Note: Add elements form the xml file in the menu
    menuInflater.inflate(R.menu.menu_main, menu)
    return true // Return true to show the menu
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.item_first_name -> {
        Log.w("Menu", "First name chosen.")
        return true;
      }
      R.id.item_last_name -> {
        Log.e("Menu", "Last name chosen.")
        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }
}