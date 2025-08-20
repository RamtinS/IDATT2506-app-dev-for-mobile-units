package edu.ntnu.idatt2506.assignment4

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import edu.ntnu.idatt2506.assignment4.fragments.MovieDetailsFragment

class MainActivity : AppCompatActivity(), Communicator {

  private var currentIndex: Int = -1

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  // The method is executed each time the orientation or the size of the screen is changed.
  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    setOrientation(newConfig)
  }

  private fun setOrientation(config: Configuration) {
    val isPortrait: Boolean = config.orientation == Configuration.ORIENTATION_PORTRAIT

    val layout = findViewById<LinearLayout>(R.id.linearLayout)
    layout.orientation = if (isPortrait) LinearLayout.VERTICAL else LinearLayout.HORIZONTAL

    val listFragment = findViewById<androidx.fragment.app.FragmentContainerView>(R.id.listFragment)
    val detailFragment = findViewById<androidx.fragment.app.FragmentContainerView>(R.id.detailFragment)
    val descriptionView = findViewById<TextView>(R.id.description)

    // To adjust the layout parameters based on orientation, I need an instance of LayoutParams.
    val listParams = listFragment.layoutParams as LinearLayout.LayoutParams
    val detailParams = detailFragment.layoutParams as LinearLayout.LayoutParams
    val descriptionParams = descriptionView.layoutParams as LinearLayout.LayoutParams

    if (isPortrait) {
      listParams.width = LinearLayout.LayoutParams.MATCH_PARENT
      listParams.height = 0

      detailParams.width = LinearLayout.LayoutParams.MATCH_PARENT
      detailParams.height = 0

      descriptionParams.weight = 0.4F
    } else {
      listParams.width = 0
      listParams.height = LinearLayout.LayoutParams.MATCH_PARENT

      detailParams.width = 0
      detailParams.height = LinearLayout.LayoutParams.MATCH_PARENT

      descriptionParams.weight = 0.1F
    }

    // Apply the updated layout parameters
    listFragment.layoutParams = listParams
    detailFragment.layoutParams = detailParams
    descriptionView.layoutParams = descriptionParams
  }
  
  override fun passIndex(index: Int) {
    currentIndex = index
    val movieDetailsFragment = supportFragmentManager.findFragmentById(R.id.detailFragment) as? MovieDetailsFragment
    movieDetailsFragment?.updateMovieDetails(index)
  }

  // Code for menu below
  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.action_next -> {
        val movieTilesArray = resources.obtainTypedArray(R.array.movie_titles)
        if (currentIndex < movieTilesArray.length() - 1) {
          currentIndex++
        }
        passIndex(currentIndex)
        movieTilesArray.recycle()
        true
      }
      R.id.action_previous -> {
        if (currentIndex > 0) {
          currentIndex--
        }
        if (currentIndex >= 0) {
          passIndex(currentIndex)
        }
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }
}