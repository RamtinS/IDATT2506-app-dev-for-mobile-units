package edu.ntnu.idatt2506.assignment7.managers

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import edu.ntnu.idatt2506.assignment7.R

/**
 * The class handles saving and retrieving app preferences like background color.
 *
 * @param activity The activity context used for accessing resources and preferences.
 * @author Ramtin Samavat
 */
class MyPreferenceManager(private val activity: AppCompatActivity) {

  private val resources = activity.resources

  // Access the default SharedPreference file.
  private val preferences = PreferenceManager.getDefaultSharedPreferences(activity)

  // Create an editor for storing values in the SharedPreference file.
  private val editor: SharedPreferences.Editor = preferences.edit()

  /**
   * Save the selected background color to the SharedPreference file.
   *
   * @param colorValue The color value to save.
   */
  fun saveBackgroundColor(colorValue: String) {
    editor.putString(resources.getString(R.string.background_color), colorValue)
    editor.apply()
  }

  /**
   * Retrieve the saved background color and convert to color resource ID.
   *
   * @return color resource ID.
   */
  fun getSavedBackgroundColor(): Int {
    // Retrieve an array of all the possible background colors.
    val colorValues = resources.getStringArray(R.array.background_color_values)

    // Retrieve the background color saved in the SharedPreference file.
    val savedValue = preferences.getString(
      resources.getString(R.string.background_color), // Key value.
      resources.getString(R.string.background_color_default_value)) // Default value if the value of the key is not found.

    // Return the corresponding color id for the string value.
    return when (savedValue) {
      colorValues[0] -> R.color.white
      colorValues[1] -> R.color.purple_200
      colorValues[2] -> R.color.blue
      else -> R.color.white
    }
  }

  /**
   * Register the preference change listener.
   * This allows the application to respond to changes in shared preferences dynamically.
   *
   * @param listener The listener to register for preference changes.
   */
  fun registerListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
    preferences.registerOnSharedPreferenceChangeListener(listener)
  }

  /**
   * Unregister the preference change listener.
   * This should be called to prevent memory leaks when the listener is no longer needed.
   *
   * @param listener The listener to unregister for preference changes.
   */
  fun unregisterListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
    preferences.unregisterOnSharedPreferenceChangeListener(listener)
  }
}