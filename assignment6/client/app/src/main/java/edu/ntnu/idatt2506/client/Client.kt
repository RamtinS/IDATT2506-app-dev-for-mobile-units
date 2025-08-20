package edu.ntnu.idatt2506.client

import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import android.util.Log
import kotlinx.coroutines.withContext

/**
 * The class represents a client that communicat with a server.
 * It manages sending and receiving messages and updates the chat UI accordingly.
 *
 * @property chatListView The ListView to display the chat log.
 * @property messageInputView The EditText for user input of messages.
 * @property sendButton The Button to send messages.
 * @property serverIp The IP address of the server to connect to (default is "10.0.2.2").
 * @property port The port number of the server to connect to (default is 8080).
 *
 * @author Ramtin Samavat
 */
class Client(
  private val chatListView: ListView,
  private val messageInputView: EditText,
  private val sendButton: Button,
  private val serverIp: String = "10.0.2.2",
  private val port: Int = 8080
) {

  private val chatLog =  mutableListOf<String>()
  private lateinit var adapter: ArrayAdapter<String>

  private lateinit var writer: PrintWriter
  private lateinit var reader: BufferedReader

  /**
   * Starts the client by initializing the chat log and setting up the button listener for sending messages.
   * Establishes a socket connection to the server and listens for incoming messages.
   */
  fun start() {
    adapter = ArrayAdapter(chatListView.context, android.R.layout.simple_list_item_1, chatLog)
    chatListView.adapter = adapter

    // Send message when button is clicked.
    sendButton.setOnClickListener {
      val message = messageInputView.text.toString()
      if (message.isNotBlank()) {
        sendMessage(message)
        messageInputView.text.clear()
        updateChatLog("Me: $message")
      }
    }

    CoroutineScope(Dispatchers.IO).launch {
      try {
        Log.d("Client", "Connecting to server at $serverIp:$port")
        Socket(serverIp, port).use { socket: Socket ->
          writer = PrintWriter(socket.getOutputStream(), true)
          reader = BufferedReader(InputStreamReader(socket.getInputStream()))

          while (true) {
            val incomingMessage = reader.readLine() ?: break
            updateChatLog(incomingMessage)
          }
        }
      } catch (e: Exception) {
        Log.e("Client", "Error during socket connection", e)
      }
    }
  }

  /**
   * Sends a message to the server to be forwarded to connected clients.
   *
   * @param message The message to be sent.
   */
  private fun sendMessage(message: String) {
    CoroutineScope(Dispatchers.IO).launch {
      if (::writer.isInitialized) {
        writer.println(message)
      }
    }
  }

  /**
   * Updates the chat log with a new message.
   *
   * @param message The message to be added to the chat log.
   */
  private fun updateChatLog(message: String ) {
    CoroutineScope(Dispatchers.IO).launch {
      withContext(Dispatchers.Main) {
        chatLog.add(message)
        adapter.notifyDataSetChanged() // Notify the adapter to update the ListView.
      }
    }
  }
}