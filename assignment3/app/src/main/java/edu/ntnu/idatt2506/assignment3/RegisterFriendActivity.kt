package edu.ntnu.idatt2506.assignment3

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText

class RegisterFriendActivity : Activity() {

  private lateinit var nameEditText : EditText
  private lateinit var birthDatePicker : DatePicker
  private lateinit var saveButton: Button
  private var position: Int = -1

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_register_friend)

    nameEditText = findViewById(R.id.nameEditText)
    birthDatePicker = findViewById(R.id.birthDatePicker)
    saveButton = findViewById(R.id.saveButton)

    val name = intent.getStringExtra("NAME")
    val birthDate = intent.getStringExtra("BIRTHDATE")
    position = intent.getIntExtra("POSITION", -1)

    if (name != null && birthDate != null) {
      nameEditText.setText(name)
      val dateParts = birthDate.split(".")
      birthDatePicker.updateDate(dateParts[2].toInt(), dateParts[1].toInt() - 1, dateParts[0].toInt())
    }

    saveButton.setOnClickListener {
      val newName = nameEditText.text.toString()
      // Note: .month starts from zero. Add one to get correct month.
      val newBirthDate = "${birthDatePicker.dayOfMonth}.${birthDatePicker.month + 1}.${birthDatePicker.year}"

      val intent = Intent().apply {
        putExtra("NAME", newName)
        putExtra("BIRTHDATE", newBirthDate)
        putExtra("POSITION", position)
      }

      setResult(RESULT_OK, intent)
      finish()
    }

  }
}