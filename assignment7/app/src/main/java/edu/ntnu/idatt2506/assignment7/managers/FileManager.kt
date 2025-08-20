package edu.ntnu.idatt2506.assignment7.managers

import android.content.Context
import android.util.Log
import edu.ntnu.idatt2506.assignment7.model.Movie
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

/**
 * FileManager is a class responsible for managing file operations related to movies.
 * It can read movie data from a raw resource file and write movie data to a local file in internal storage.
 *
 * @property context The context from which the FileManager is initialized, used to access resources and files.
 * @author Ramtin Samavat
 */
class FileManager(private val context: Context) {

  private val tag = "FileManager"
  private var dir: File = context.filesDir // Refers to the app's internal file directory.

  /**
   * Parses a list of movies from a raw resource file.
   *
   * @param rawResourceId The resource ID of the raw file containing movie data.
   * @return A list of Movie objects parsed from the file.
   */
  fun parseMoviesFromFile(rawResourceId: Int): List<Movie> {
    val movies = mutableListOf<Movie>()

    try {
      val inputStream: InputStream = context.resources.openRawResource(rawResourceId) // Open the raw resource file.
      BufferedReader(InputStreamReader(inputStream)).use { reader ->
        var line: String? = reader.readLine()
        var title: String? = null
        var director: String? = null
        var actors: List<String>? = null

        while (line != null) {
          line.trim()

          if (line.startsWith("Title:")) {
            title = line.removePrefix("Title:").trim()
          } else if (line.startsWith("Director:")) {
            director = line.removePrefix("Director:").trim()
          } else if (line.startsWith("Actors:")) {
            actors = line.removePrefix("Actors:").trim().split(",").map { it.trim() }
          }

          if (title != null && director != null && actors != null) {
            val movie = Movie(title, director, actors)
            movies.add(movie)

            title = null
            director = null
            actors = null
          }

          line = reader.readLine()
        }
      }
    } catch (e: IOException) {
      Log.e(tag, "Error while reading the file: ${e.message}", e)
    }

    return movies;
  }

  /**
   * Writes a list of movies to a specified file in internal storage.
   *
   * @param newFilename The name of the file to write the movie data to.
   * @param movieList The list of Movie objects to be written to the file.
   */
  fun writeMoviesToFile(newFilename: String, movieList: List<Movie>) {
    val file = File(dir, newFilename)

    try {
      // Open or create the file and write each movie's details.
      file.bufferedWriter().use { writer ->
        movieList.forEach { movie ->
          writer.write("Title: ${movie.title}\n")
          writer.write("Director: ${movie.director}\n")
          writer.write("Actors: ${movie.actors.joinToString(", ")}\n")
          writer.write("\n")
        }
      }
      Log.i(tag, "Successfully wrote movies to file: $newFilename")
    } catch (e: Exception) {
      Log.e(tag, "Error while writing to file: ${e.message}", e)
    }
  }
}