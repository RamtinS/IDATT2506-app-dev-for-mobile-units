package edu.ntnu.idatt2506.client

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView

/**
 * MainActivity is the main entry point of the application.
 * It initializes the user interface components and starts the client for communication with the server.
 *
 * @author Ramtin Samavat
 */
class MainActivity: Activity() {

  /**
   * Called when the activity is first created.
   * Initializes the UI components and starts the chat client.
   *
   * @param savedInstanceState Bundle containing saved data if the activity was previously shut down, otherwise null.
   */
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val chatListView = findViewById<ListView>(R.id.chatLog)
    val messageInputView = findViewById<EditText>(R.id.messageInput)
    val sendButton = findViewById<Button>(R.id.sendButton)

    Client(chatListView, messageInputView, sendButton).start()
  }
}