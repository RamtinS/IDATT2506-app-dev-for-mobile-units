package edu.ntnu.idatt2506.assignment7.service

import android.content.Context
import android.util.Log
import edu.ntnu.idatt2506.assignment7.managers.DatabaseManager
import edu.ntnu.idatt2506.assignment7.model.Movie

/**
 * The class serves as the main entry point for managing the database operations in the application.
 *
 * @author Ramtin Samavat
 */
class Database(context: Context, movies: List<Movie>): DatabaseManager(context) {

  private val tag = "Database"

  /**
   * Initialize the database by populating it with data.
   */
  init {
    try {
      this.clear()
      movies.forEach { movie: Movie ->
        this.insert(movie.title, movie.director, movie.actors)
      }
      Log.i(tag, "Database populated with movies successfully.")
    } catch (e: Exception) {
      Log.e(tag, "Error during database initialization: ${e.message}", e)
    }
  }

  /**
   * Retrieves all movies, along with their directors and actors, and returns them as a list
   * of Movie objects.
   */
  fun getAllMovies(): List<Movie> {
    val movies = ArrayList<Movie>()

    val movieResults = performRawQuery(
      select = arrayOf("M.$MOVIE_TITLE || '|' || D.$DIRECTOR_NAME AS title_director"),
      from = arrayOf("$TABLE_MOVIE M", "$TABLE_DIRECTOR D"),
      join = arrayOf("M.$DIRECTOR_ID = D.$ID")
    )

    Log.i(tag, "Result from database:  $movieResults")
    // [Venom|Ruben Fleischer , Star Wars: The Force Awakens|J.J. Abrams , Iron Man|Jon Favreau , The Fall Guy|David Leitch , The Batman|Matt Reeves ]

    movieResults.forEach { row ->
      val columns = row.split("|")
      val movieTitle = columns[0].trim()
      val directorName = columns[1].trim()

      val actors = getActorsForMovie(movieTitle)
      movies.add(Movie(movieTitle, directorName, actors))
    }

    return movies
  }

  /**
   * Retrieves all movies directed by a specific director.
   *
   * @param name The name of the director.
   */
  fun getMoviesByDirector(name: String): List<Movie> {
    val movies = ArrayList<Movie>()

    val movieResults = performRawQuery(
      select = arrayOf("M.$MOVIE_TITLE || '|' || D.$DIRECTOR_NAME AS title_director"),
      from = arrayOf("$TABLE_MOVIE M", "$TABLE_DIRECTOR D"),
      join = arrayOf("M.$DIRECTOR_ID = D.$ID"),
      where = "LOWER(D.$DIRECTOR_NAME) = '${name.lowercase()}'"
    )

    movieResults.forEach { row ->
      val columns = row.split("|")
      val movieTitle = columns[0].trim()
      val directorName = columns[1].trim()

      val actors = getActorsForMovie(movieTitle)
      movies.add(Movie(movieTitle, directorName, actors))
    }

    return movies
  }

  /**
   * Retrieves all movies that a specific actor has acted in.
   *
   * @param name The name of the actor.
   */
  fun getMoviesByActor(name: String): List<Movie> {
    val movies = ArrayList<Movie>()

    val movieResults = performRawQuery(
      select = arrayOf("M.$MOVIE_TITLE || '|' || D.$DIRECTOR_NAME AS title_director"),
      from = arrayOf("$TABLE_MOVIE M", "$TABLE_DIRECTOR D", "$TABLE_ACTOR A", "$TABLE_MOVIE_ACTOR MA"),
      join = arrayOf("M.$ID = MA.$MOVIE_ID", "A.$ID = MA.$ACTOR_ID", "M.$DIRECTOR_ID = D.$ID"),
      where = "LOWER(A.$ACTOR_NAME) = '${name.lowercase()}'"
    )

    movieResults.forEach { row ->
      val columns = row.split("|")
      val movieTitle = columns[0].trim()
      val directorName = columns[1].trim()

      val actors = getActorsForMovie(movieTitle)
      movies.add(Movie(movieTitle, directorName, actors))
    }

    return movies
  }

  /**
   * Retrieves a movie by its title.
   *
   * @param title The tile of the movie.
   */
  fun getMoviesByTitle(title: String): List<Movie> {
    val movies = ArrayList<Movie>()

    val movieResults = performRawQuery(
      select = arrayOf("M.$MOVIE_TITLE || '|' || D.$DIRECTOR_NAME AS title_director"),
      from = arrayOf("$TABLE_MOVIE M", "$TABLE_DIRECTOR D"),
      join = arrayOf("M.$DIRECTOR_ID = D.$ID"),
      where = "LOWER(M.$MOVIE_TITLE) = '${title.lowercase()}'"
    )

    movieResults.forEach { row ->
      val columns = row.split("|")
      val movieTitle = columns[0].trim()
      val directorName = columns[1].trim()

      val actors = getActorsForMovie(movieTitle)
      movies.add(Movie(movieTitle, directorName, actors))
    }

    return movies
  }

  /**
   * Helper function to retrieve the actors for a specific movie.
   *
   * @param movieTitle The tile of the movie.
   */
  private fun getActorsForMovie(movieTitle: String): List<String> {
    val actors = ArrayList<String>()

    val actorResults = performRawQuery(
      select = arrayOf("A.$ACTOR_NAME"),
      from = arrayOf("$TABLE_ACTOR A", "$TABLE_MOVIE M", "$TABLE_MOVIE_ACTOR MA"),
      join = arrayOf("M.$ID = MA.$MOVIE_ID", "A.$ID = MA.$ACTOR_ID"),
      where = "M.$MOVIE_TITLE = '$movieTitle'"
    )

    actorResults.forEach { row ->
      actors.add(row.trim())
    }

    return actors
  }
}