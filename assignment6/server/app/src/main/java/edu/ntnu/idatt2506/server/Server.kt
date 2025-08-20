package edu.ntnu.idatt2506.server

import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.atomic.AtomicInteger

/**
 * The class represents a server that handles multiple client connections.
 *
 * @property serverInfoView The TextView to display server information.
 * @property clientListView The ListView to display the list of connected clients.
 * @property port The port number on which the server listens for incoming connections.
 *
 * @author Ramtin Samavat
 */
class Server(
  private val serverInfoView: TextView,
  private val clientListView: ListView,
  private val port: Int
) {

  private val clientWriters = mutableListOf<ClientInfo>()
  private val clientList = mutableListOf<String>()
  private lateinit var adapter: ArrayAdapter<String>
  private val clientIdGenerator = AtomicInteger(0)

  /**
   * Starts the server, initializing the server socket and waiting for client connections.
   */
  fun start() {
    adapter = ArrayAdapter(clientListView.context, android.R.layout.simple_list_item_1, clientList)
    clientListView.adapter = adapter

    CoroutineScope(Dispatchers.IO).launch {
      val serverSocket = ServerSocket(port)

      val serverInfo = "Server runs on Port: $port."
      setServerInfo(serverInfo)

      while (true) {
        val clientSocket = serverSocket.accept()
        val clientId = clientIdGenerator.incrementAndGet()
        handleClient(clientSocket, clientId)
      }
    }
  }

  /**
   * Handles a connected client, reading messages and broadcasting them to other clients.
   *
   * @param clientSocket The socket for the connected client.
   * @param clientId The unique ID assigned to the client.
   */
  private fun handleClient(clientSocket: Socket, clientId: Int) {
    CoroutineScope(Dispatchers.IO).launch {
      val writer = PrintWriter(clientSocket.getOutputStream(), true)
      val reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))

      val clientInfo = ClientInfo(clientId, writer)
      clientWriters.add(clientInfo)

      updateClientList("Client $clientId connected IP: ${clientSocket.inetAddress.hostAddress}")

      // .use will automatically close the socket when it is no longer in use or if an exception occurs.
      clientSocket.use {
        while (true) {
          val message = reader.readLine() ?: break
          broadcastMessage("Client $clientId: $message", clientId)
        }
      }

      clientWriters.remove(clientInfo)

      val disconnectInfo = "Client $clientId disconnected IP: ${clientSocket.inetAddress.hostAddress}"
      updateClientList(disconnectInfo)
    }
  }

  /**
   * Broadcasts a message to all connected clients except the sender.
   *
   * @param message The message to broadcast.
   * @param clientId The ID of the client that sent the message.
   */
  private fun broadcastMessage(message: String, clientId: Int) {
    for (client in clientWriters) {
      if (client.clientId != clientId) {
        client.writer.println(message)
      }
    }
  }

  /**
   * Updates the server information displayed on the serverInfoView.
   *
   * @param info The information to display.
   */
  private fun setServerInfo(info: String) {
    CoroutineScope(Dispatchers.IO).launch {
      withContext(Dispatchers.Main) {
        serverInfoView.text = info
      }
    }
  }

  /**
   * Updates the client list displayed in the clientListView.
   *
   * @param info The information about the client connection or disconnection.
   */
  private fun updateClientList(info: String) {
    CoroutineScope(Dispatchers.IO).launch {
      withContext(Dispatchers.Main) {
        clientList.add(info)
        adapter.notifyDataSetChanged()
      }
    }
  }
}