package edu.ntnu.idatt2506.server

import android.app.Activity
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView

/**
 * MainActivity is the main entry point of the application.
 * It initializes the user interface and manages server interactions.
 * This activity allows the user to connect to the server and view connected clients.
 *
 * @author Ramtin Samavat
 */
class MainActivity: Activity() {

  /**
   * Called when the activity is first created.
   * Initializes the UI components and sets up the server to listen for incoming client connections.
   *
   * @param savedInstanceState Bundle containing saved data if the activity was previously shut down, otherwise null.
   */
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val serverInfoView = findViewById<TextView>(R.id.serverInfo)
    val clientListView = findViewById<ListView>(R.id.connectedClients)
    Server(serverInfoView, clientListView, 8080).start()
  }
}