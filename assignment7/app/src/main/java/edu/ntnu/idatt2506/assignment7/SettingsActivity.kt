package edu.ntnu.idatt2506.assignment7

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import edu.ntnu.idatt2506.assignment7.databinding.ActivitySettingsBinding
import edu.ntnu.idatt2506.assignment7.managers.MyPreferenceManager

/**
 * The SettingsActivity allows users to adjust app settings such as background color.
 *
 * @author Ramtin Samavat
 */
class SettingsActivity: AppCompatActivity(),
  SharedPreferences.OnSharedPreferenceChangeListener,
  Preference.SummaryProvider<ListPreference> {

  private lateinit var binding: ActivitySettingsBinding
  private lateinit var myPreferenceManager: MyPreferenceManager

  /**
   * Called when the activity is first created.
   * Initializes the MyPreferenceManager and registers the preference listener.
   * Sets up the layout and handles the return button click.
   *
   * @param savedInstanceState If the activity is being re-initialized after previously being shut down.
   */
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // Initialize the preference manager and register the listener.
    myPreferenceManager = MyPreferenceManager(this)
    myPreferenceManager.registerListener(this)

    // Inflate the layout using ViewBinding.
    binding = ActivitySettingsBinding.inflate(layoutInflater)
    setContentView(binding.root)

    // Set up the preference fragment.
    supportFragmentManager
      .beginTransaction()
      .replace(R.id.background_settings, SettingsFragment())
      .commit()

    // When the return button is clicked, pass the selected color back to MainActivity
    binding.returnButton.setOnClickListener {
      // Retrieve the selected color from the SharedPreference file.
      val selectedColor = myPreferenceManager.getSavedBackgroundColor()

      // Send the selected color to the MainActivity.
      val returnIntent = Intent().apply {
        putExtra("selected_color", selectedColor)
      }
      setResult(RESULT_OK, returnIntent)

      finish() // Close the activity and return to the MainActivity.
    }
  }

  /**
   * The method is triggered when a preference changes.
   *
   * @param sharedPreferences The SharedPreferences where the change happened.
   * @param key The key of the changed preference.
   */
  override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
    // Currently unused, as immediate action when preference is changed is not necessary.
  }

  /**
   * Provides the summary of the selected ListPreference item.
   *
   * @param preference The ListPreference for which to provide the summary.
   * @return The selected preference entry, or "Unknown" if not available.
   */
  override fun provideSummary(preference: ListPreference): CharSequence? {
    return preference.entry ?: "Unknown"
  }

  /**
   * Called when the activity is being destroyed.
   * Unregisters the preference listener.
   */
  override fun onDestroy() {
    super.onDestroy()
    myPreferenceManager.unregisterListener(this)
  }

  /**
   * A fragment to display the preference screen defined in XML.
   *
   * When the user interacts with the preferences UI elements, such as selecting a ListPreference,
   * the Android framework automatically saves the new preference value to the SharedPreferences file
   * based on the key defined in the XML.
   */
  class SettingsFragment : PreferenceFragmentCompat() {

    /**
     * Sets up the preferences from the resource file.
     *
     * @param savedInstanceState If the fragment is being re-initialized after previously being shut down.
     * @param rootKey Optional root key to which this fragment's preferences should be attached.
     */
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
      setPreferencesFromResource(R.xml.preference_screen, rootKey)
    }
  }
}