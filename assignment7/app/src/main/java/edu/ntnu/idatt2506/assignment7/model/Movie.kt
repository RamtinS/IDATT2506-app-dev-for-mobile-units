package edu.ntnu.idatt2506.assignment7.model

/**
 * Data class representing a movie.
 *
 * @property title The title of the movie.
 * @property director The director of the movie.
 * @property actors A list of actors featured in the movie.
 *
 * @author Ramtin Samavat
 */
data class Movie(
  val title: String,
  val director: String,
  val actors: List<String>
)
