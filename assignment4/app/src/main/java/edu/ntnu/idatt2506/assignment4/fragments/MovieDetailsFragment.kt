package edu.ntnu.idatt2506.assignment4.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import edu.ntnu.idatt2506.assignment4.R

class MovieDetailsFragment : Fragment() {

  private lateinit var movieTitle: TextView
  private lateinit var movieImage: ImageView
  private lateinit var movieDescription: TextView

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.fragment_details, container, false)
    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    movieTitle = view.findViewById(R.id.headline)
    movieImage = view.findViewById(R.id.movieImage)
    movieDescription = view.findViewById(R.id.description)
  }

  fun updateMovieDetails(index: Int) {
    setMovieTitle(index)
    setMovieImage(index)
    setDescription(index)
  }

  private fun setMovieTitle(index: Int?) {
    if (index != null && index >= 0) {
      val movieTilesArray = resources.obtainTypedArray(R.array.movie_titles)
      if (index < movieTilesArray.length()) {
        val title = movieTilesArray.getString(index)
        movieTitle.text = title
      }
      movieTilesArray.recycle()
    }
  }

  private fun setMovieImage(index: Int?) {
    if (index != null && index >= 0) {
      val movieImagesArray = resources.obtainTypedArray(R.array.movie_images)
      if (index < movieImagesArray.length()) {
        val imageDrawable = movieImagesArray.getDrawable(index)
        movieImage.setImageDrawable(imageDrawable)
      }
      movieImagesArray.recycle()
    }
  }

  private fun setDescription(index: Int?) {
    if (index != null && index >= 0) {
      val movieDescriptionArray = resources.obtainTypedArray(R.array.movie_descriptions)
      if (index < movieDescriptionArray.length()) {
        val description = movieDescriptionArray.getString(index)
        movieDescription.text = description
      }
      movieDescriptionArray.recycle()
    }
  }

}