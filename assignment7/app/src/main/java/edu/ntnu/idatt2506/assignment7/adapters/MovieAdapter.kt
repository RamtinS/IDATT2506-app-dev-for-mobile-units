package edu.ntnu.idatt2506.assignment7.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.ntnu.idatt2506.assignment7.R
import edu.ntnu.idatt2506.assignment7.model.Movie

class MovieAdapter(private var movieList: List<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

  /**
   * Provide a reference to the type of views that you are using.
   */
  class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title: TextView = itemView.findViewById(R.id.movieTitle)
    val director: TextView = itemView.findViewById(R.id.movieDirector)
    val actors: TextView = itemView.findViewById(R.id.movieActors)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
    // Create a new view, which defines the UI of the list item.
    val itemView = LayoutInflater.from(parent.context)
      .inflate(R.layout.movie_item, parent, false)

    return MovieViewHolder(itemView)
  }

  /**
   * Return the size of the dataset.
   */
  override fun getItemCount(): Int {
    return movieList.size
  }

  override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
    val movie = movieList[position]  // Get the current movie.

    // Set the data to the views
    holder.title.text = movie.title
    val director = "Director: ${movie.director}"
    holder.director.text = director
    val actors = "Actors: ${movie.actors.joinToString(", ")}"
    holder.actors.text = actors
  }

  fun updateMovies(newMovies: List<Movie>) {
    movieList = newMovies
    notifyDataSetChanged()
  }
}