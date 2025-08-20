package edu.ntnu.idatt2506.assignment5

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val URL = "https://bigdata.idi.ntnu.no/mobil/tallspill.jsp"

/**
 * MainActivity is the main entry point of the application.
 * It allows the user to submit their name and card number, and guess a number.
 * It handles network requests and updates the UI based on the server's response.
 *
 * @author Ramtin Samavat
 * @version 1.0
 */
class MainActivity : AppCompatActivity() {

  private val network: HttpWrapper = HttpWrapper(URL)

  private lateinit var nameInputView: EditText
  private lateinit var cardNumberInputView: EditText
  private lateinit var numberInputView: EditText
  private lateinit var submitButton: Button
  private lateinit var guessButton: Button
  private lateinit var resultView: TextView

  /**
   * The function is called when the activity is first created.
   * It initializes the UI components and sets up the click listeners for the buttons.
   *
   * @param savedInstanceState Bundle containing saved data if the activity was previously shut down, otherwise null.
   */
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main) // Set the GUI of the main activity.

    // Initialize the views and the buttons.
    nameInputView = findViewById(R.id.name_input)
    cardNumberInputView = findViewById(R.id.card_number_input)
    numberInputView = findViewById(R.id.number_input)
    submitButton = findViewById(R.id.submit_button)
    guessButton = findViewById(R.id.guess_button)
    resultView = findViewById(R.id.result_text)

    // Attach an on click listener to the button.
    submitButton.setOnClickListener {
      // Execute the code below when the button is clicked.
      val name = nameInputView.text.toString()
      val cardNumber = cardNumberInputView.text.toString()

      // Start a coroutine in the IO thread to perform a network request.
      CoroutineScope(Dispatchers.IO).launch {
        // Perform the request int he background.
        val response: String = try {
          network.getRequest("navn=$name&kortnummer=$cardNumber")
        } catch (e: Exception) {
          Log.e("Submit button", "Exception during GET request", e)
          e.toString()
        }

        // Switch back to the main thread to update the UI.
        // UI updates must be done on the main thread.
        withContext(Dispatchers.Main) {
          setResult(response)
        }
      }
    }

    guessButton.setOnClickListener {
      val guess = numberInputView.text.toString()

      CoroutineScope(Dispatchers.IO).launch {
        val response = try {
          network.getRequest("tall=$guess").toString()
        } catch (e: Exception) {
          Log.e("Guess button", "Exception during GET request", e)
          e.toString()
        }

        withContext(Dispatchers.Main) {
          setResult(response)
        }
      }
    }
  }

  /**
   * Show result from server in UI
   *
   * @param response The response from the server.
   */
  private fun setResult(response: String) {
    resultView.text = response
  }
}