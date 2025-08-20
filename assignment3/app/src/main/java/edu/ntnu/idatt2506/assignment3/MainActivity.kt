package edu.ntnu.idatt2506.assignment3

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView

class MainActivity : Activity() {

  private lateinit var addFriendButton : Button
  private lateinit var friendListView : ListView
  private lateinit var friendAdapter : ArrayAdapter<String>
  private var friendList = mutableListOf<String>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    addFriendButton = findViewById(R.id.addFriendButton)
    friendListView = findViewById(R.id.friendListView)

    friendAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, friendList)
    friendListView.adapter = friendAdapter

    addFriendButton.setOnClickListener {
      val intent = Intent(this, RegisterFriendActivity::class.java)
      startActivityForResult(intent, 1)
    }

    friendListView.setOnItemClickListener { _, _, position, _ ->
      val friend = friendList[position]
      val intent = Intent(this, RegisterFriendActivity::class.java).apply {
        putExtra("NAME", friend.split(" - ")[0])
        putExtra("BIRTHDATE", friend.split(" - ")[1])
        putExtra("POSITION", position)
      }
      startActivityForResult(intent, 2)
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (resultCode == RESULT_OK) {
      val name = data?.getStringExtra("NAME")
      val birthDate = data?.getStringExtra("BIRTHDATE")
      val position = data?.getIntExtra("POSITION", -1)

      if (name != null && birthDate != null) {
        if (requestCode == 1) {
          friendList.add("$name - $birthDate")
        } else if (requestCode == 2 && position != null && position >= 0) {
          friendList[position] = "$name - $birthDate"
        }
        friendAdapter.notifyDataSetChanged()
      }
    }
  }
}