package edu.ntnu.idatt2506.assignment5

import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import java.net.HttpURLConnection
import java.net.URL

const val ENCODING = "UTF-8"

/**
 * The class is responsible for making HTTP requests, with cookie handling.
 *
 * @author Ramtin Samavat
 * @version 1.0
 */
class HttpWrapper(private val URL: String) {

  /**
   * Initializes a global cookie manager: cookies are accepted by default.
   * It is executed immediately after the primary constructor of the class is called.
   */
  init {
    CookieHandler.setDefault(CookieManager(null, CookiePolicy.ACCEPT_ALL))
  }

  /**
   * The function opens a HTTP connection to the provided URL.
   *
   * @param url The URL to which the connection will be made.
   * @return HttpURLConnection The opened HTTP connection.
   * @throws java.net.MalformedURLException If the provided URL is not valid.
   * @throws java.io.IOException If an I/O exception occurs while opening the connection.
   */
  private fun openConnection(url: String): HttpURLConnection {
    val connection = URL(url).openConnection() as HttpURLConnection // Creates a connection.
    connection.setRequestProperty("Accept-Charset", ENCODING) // Set the request encoding.
    return connection
  }

  /**
   * The function sends a GET request with provided parameters.
   *
   * @param params The query parameters to be appended to the base URL.
   * @return The response from the server as a string.
   * @throws java.net.MalformedURLException If the provided URL is not valid.
   * @throws java.io.IOException If an I/O error occurs during the connection or while reading the response.
   */
  fun getRequest(params: String): String {
    Log.i("HttpWrapper", "Sending GET request with params: $params")

    val fullUrl = "$URL?$params"

    val connection = openConnection(fullUrl) // Open the connection to the URL.
    connection.requestMethod = "GET" // Set the request method to GET
    connection.doInput = true // Enable input stream (read response)

    val reader = BufferedReader(InputStreamReader(connection.inputStream, ENCODING))

    // Read the response line by line and concatenate into a single string.
    val response = reader.use { it.readText() }

    return response;
  }

}