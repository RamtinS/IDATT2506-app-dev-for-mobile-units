package edu.ntnu.idatt2506.assignment4.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import edu.ntnu.idatt2506.assignment4.Communicator
import edu.ntnu.idatt2506.assignment4.R

class MovieListFragment : Fragment() {

  private lateinit var movies: ListView
  private lateinit var movieAdapter: ArrayAdapter<String> // Adapter to populate the ListView with data.
  private lateinit var communicator: Communicator // Communicator interface to pass data between fragments.

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.fragment_list, container, false) // Set layout for fragment.
    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    // Add movies titles to the ListView.
    movies = view.findViewById(R.id.movieList)
    val movieTitles: Array<String> = resources.getStringArray(R.array.movie_titles) // Retrieve titles from strings.xml.
    movieAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, movieTitles) // Create adapter.
    movies.adapter = movieAdapter // Add movie titles to the ListView.

    communicator = activity as Communicator // Declare communicator interface

    movies.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
      communicator.passIndex(position) // Use method from interface to send data.
    }
  }
}