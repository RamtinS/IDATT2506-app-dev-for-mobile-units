package edu.ntnu.idatt2506.assignment7

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import edu.ntnu.idatt2506.assignment7.adapters.MovieAdapter
import edu.ntnu.idatt2506.assignment7.databinding.ActivityMainBinding
import edu.ntnu.idatt2506.assignment7.managers.FileManager
import edu.ntnu.idatt2506.assignment7.managers.MyPreferenceManager
import edu.ntnu.idatt2506.assignment7.model.Movie
import edu.ntnu.idatt2506.assignment7.service.Database

/**
 * MainActivity is the entry point of the application.
 * It handles the main user interface, displaying a list of movies,
 * and allows users to search for movies based on various criteria.
 *
 * @author Ramtin Samavat
 */
class MainActivity: AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding // Binding object for direct access to views in activity_main.xml.
  private lateinit var database: Database
  private lateinit var movieAdapter: MovieAdapter

  // Launcher for starting SettingsActivity and receiving results.
  private lateinit var launcher: ActivityResultLauncher<Intent>

  /**
   * Called when the activity is first created.
   *
   * @param savedInstanceState If the activity is being re-initialized after previously being shut down.
   */
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityMainBinding.inflate(layoutInflater) // Initialize the view binding.
    setContentView(binding.root) // Set the gui for the activity.

    val fileManager = FileManager(this)
    val movies: List<Movie> = fileManager.parseMoviesFromFile(R.raw.movies)
    fileManager.writeMoviesToFile("new_movies.txt", movies)

    database = Database(this, movies)

    // Set up RecyclerView to display the list of movies.
    binding.recyclerView.layoutManager = LinearLayoutManager(this)
    movieAdapter = MovieAdapter(emptyList()) // Initialize the adapter with an empty list.
    binding.recyclerView.adapter = movieAdapter

    // Set up the spinner for selecting the type of search.
    val searchOptions = resources.getStringArray(R.array.search_type_options)
    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, searchOptions)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    binding.searchTypeSpinner.adapter = adapter

    // Set up the search button's click listener.
    binding.searchButton.setOnClickListener {
      val query = binding.searchInput.text.toString().trim() // Retrieve the search query from user.
      val searchType = binding.searchTypeSpinner.selectedItemPosition // Retrieve the selected search option.

      when (searchType) {
        0 -> {
          val movieResults = database.getAllMovies()
          showMovies(movieResults)
        }

        1 -> { // Find movies on director.
          if (query.isNotEmpty()) {
            val movieResults = database.getMoviesByDirector(query)
            showMovies(movieResults)
          } else {
            Toast.makeText(this, "Please enter a director.", Toast.LENGTH_SHORT).show()
          }
        }

        2 -> { // Find movies on title.
          if (query.isNotEmpty()) {
            val movieResult = database.getMoviesByTitle(query)
            showMovies(movieResult)
          } else {
            Toast.makeText(this, "Please enter a movie title.", Toast.LENGTH_SHORT).show()
          }
        }

        3 -> { // Find movies on actor.
          if (query.isNotEmpty()) {
            val movieResults = database.getMoviesByActor(query)
            showMovies(movieResults)
          } else {
            Toast.makeText(this, "Please enter a movie title.", Toast.LENGTH_SHORT).show()
          }
        }
      }
    }

    // Retrieve and apply the saved background color from preferences.
    val savedColorId = MyPreferenceManager(this).getSavedBackgroundColor()
    binding.recyclerView.setBackgroundColor(ContextCompat.getColor(this, savedColorId))

    // Register the launcher for receiving results from SettingsActivity.
    // Use ActivityForResultLauncher as StartActivityForResult() is Deprecated.
    launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
      if (result.resultCode == RESULT_OK && result.data != null) {

        // Get the selected color from the intent.
        // R.color.white acts default color if the key "selected_color" is not found.
        val selectedColor = result.data?.getIntExtra("selected_color", R.color.white)

        // Apply the selected color to the RecyclerView background.
        if (selectedColor != null) {
          binding.recyclerView.setBackgroundColor(ContextCompat.getColor(this, selectedColor))
        }
      }
    }
  }

  /**
   * Initializes the contents of the Activity's standard options menu.
   *
   * @param menu The options menu in which you place your items.
   * @return true if the menu is created and should be displayed.
   */
  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    return true
  }

  /**
   * Handles item selections from the options menu.
   *
   * @param item The menu item that was selected.
   * @return true if the selection was handled; false otherwise.
   */
  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.settings -> {
        val intent = Intent(this, SettingsActivity::class.java)
        launcher.launch(intent)
        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }

  /**
   * Updates the movie list displayed in the RecyclerView.
   *
   * @param movies A list of Movie objects to be displayed.
   */
  private fun showMovies(movies: List<Movie>) {
    movieAdapter.updateMovies(movies)
  }
}