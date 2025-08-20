package edu.ntnu.idatt2506.assignment7.managers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * DatabaseManager is a helper class for managing the SQLite database for the application.
 * It handles the creation, upgrading, and basic CRUD operations for the movie database.
 *
 * @param context The context of the application, used to access resources and databases.
 *
 * @author Ramtin Samavat
 */
open class DatabaseManager(context: Context):
  SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

  // Declare variables for creating the tables for the database.
  companion object {
    const val DATABASE_NAME = "MovieDb"
    const val DATABASE_VERSION = 2

    const val ID = "_id"

    const val TABLE_DIRECTOR = "DIRECTOR"
    const val DIRECTOR_NAME = "name"

    const val TABLE_MOVIE = "MOVIE"
    const val MOVIE_TITLE = "title"
    const val DIRECTOR_ID = "director_id"

    const val TABLE_ACTOR = "ACTOR"
    const val ACTOR_NAME = "name"

    // Since the relationship between movies and actors is many-to-many, we need a junction table.
    const val TABLE_MOVIE_ACTOR = "MOVIE_ACTOR"
    const val MOVIE_ID = "movie_id"
    const val ACTOR_ID = "actor_id"

    val JOIN_MOVIE_ACTOR = arrayOf(
      "$TABLE_ACTOR.$ID=$TABLE_MOVIE_ACTOR.$ACTOR_ID",
      "$TABLE_MOVIE.$ID=$TABLE_MOVIE_ACTOR.$MOVIE_ID"
    )
  }

  /**
   * Creates the necessary tables in the database when it is first created.
   *
   * @param db The SQLiteDatabase instance where the tables will be created.
   */
  override fun onCreate(db: SQLiteDatabase) {
    // Create table for directors.
    db.execSQL(
      """create table $TABLE_DIRECTOR (
                $ID integer primary key autoincrement, 
                $DIRECTOR_NAME text unique not null
            );"""
    )

    // Create table for movies.
    db.execSQL(
      """create table $TABLE_MOVIE (
                $ID integer primary key autoincrement, 
                $MOVIE_TITLE text unique not null,
                $DIRECTOR_ID integer not null,
                FOREIGN KEY($DIRECTOR_ID) REFERENCES $TABLE_DIRECTOR($ID)
            );"""
    )

    // Create table for actors.
    db.execSQL(
      """create table $TABLE_ACTOR (
                $ID integer primary key autoincrement, 
                $ACTOR_NAME text unique not null
            );"""
    )

    // Create junction table for movies and actors.
    db.execSQL(
      """create table $TABLE_MOVIE_ACTOR (
                $ID integer primary key autoincrement, 
                $MOVIE_ID integer, 
                $ACTOR_ID integer,
                FOREIGN KEY($MOVIE_ID) REFERENCES $TABLE_MOVIE($ID), 
                FOREIGN KEY($ACTOR_ID) REFERENCES $TABLE_ACTOR($ID)
            );"""
    )
  }

  /**
   * Upgrades the database when the version number changes.
   * Drops the existing tables and recreates them.
   *
   * @param db The SQLiteDatabase instance to upgrade.
   * @param oldVersion The old version number of the database.
   * @param newVersion The new version number of the database.
   */
  override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    db.execSQL("DROP TABLE IF EXISTS $TABLE_DIRECTOR")
    db.execSQL("DROP TABLE IF EXISTS $TABLE_MOVIE")
    db.execSQL("DROP TABLE IF EXISTS $TABLE_ACTOR")
    db.execSQL("DROP TABLE IF EXISTS $TABLE_MOVIE_ACTOR")
    onCreate(db)
  }

  /**
   * Clears all data in the database by dropping and recreating tables.
   */
  fun clear() {
    writableDatabase.use { onUpgrade(it, 0, 0) }
  }

  /**
   * Inserts a new movie along with its director and actors into the database.
   *
   * @param movieTitle The title of the movie.
   * @param director The director of the movie.
   * @param actors A list of actors who acted in the movie.
   */
  fun insert(movieTitle: String, director: String, actors: List<String>) {
    writableDatabase.use { database ->
      val directorId = insertValueIfNotExists(database, TABLE_DIRECTOR, DIRECTOR_NAME, director)
      val movieId = insertValueIfNotExists(database, TABLE_MOVIE, MOVIE_TITLE, movieTitle, DIRECTOR_ID, directorId)
      actors.forEach { actor ->
        val actorId = insertValueIfNotExists(database, TABLE_ACTOR, ACTOR_NAME, actor)
        linkMovieAndActor(database, movieId, actorId)
      }
    }
  }

  /**
   * Inserts a value into the specified table if it does not already exist.
   *
   * @param database The SQLiteDatabase instance to perform the insert.
   * @param table The name of the table to insert the value into.
   * @param field The field name to check for existence.
   * @param value The value to insert if it does not exist.
   * @param foreignKeyField The foreign key field name (optional).
   * @param foreignKeyValue The foreign key value (optional).
   * @return The ID of the existing or newly inserted value.
   */
  private fun insertValueIfNotExists(database: SQLiteDatabase, table: String, field: String, value: String, foreignKeyField: String? = null, foreignKeyValue: Long? = null): Long {
    query(database, table, arrayOf(ID, field), "$field='$value'").use { cursor ->
      // Insert if value doesn't exist.
      return if (cursor.count != 0) {
        cursor.moveToFirst()
        cursor.getLong(0) //id of found value
      } else {
        insertValue(database, table, field, value, foreignKeyField, foreignKeyValue)
      }
    }
  }

  /**
   * Inserts a new value into the specified table.
   *
   * @param database The SQLiteDatabase instance to perform the insert.
   * @param table The name of the table to insert the value into.
   * @param field The field name to insert the value.
   * @param value The value to insert.
   * @param foreignKeyField The foreign key field name (optional).
   * @param foreignKeyValue The foreign key value (optional).
   * @return The ID of the newly inserted value.
   */
  private fun insertValue(database: SQLiteDatabase, table: String, field: String, value: String,
                          foreignKeyField: String? = null, foreignKeyValue: Long? = null): Long {

    val values = ContentValues()
    values.put(field, value.trim())
    if (foreignKeyField != null && foreignKeyValue != null) {
      values.put(foreignKeyField, foreignKeyValue)
    }

    return database.insert(table, null, values)
  }

  /**
   * Links a movie with an actor in the junction table.
   *
   * @param database The SQLiteDatabase instance to perform the insert.
   * @param movieId The ID of the movie.
   * @param actorId The ID of the actor.
   */
  private fun linkMovieAndActor(database: SQLiteDatabase, movieId: Long, actorId: Long) {
    val values = ContentValues()
    values.put(MOVIE_ID, movieId)
    values.put(ACTOR_ID, actorId)
    database.insert(TABLE_MOVIE_ACTOR, null, values)
  }

  /**
   * Performs a query on the specified table and returns the results.
   *
   * @param table The name of the table to query.
   * @param columns The columns to select.
   * @param selection An optional filter for the rows to return.
   * @return A list of strings representing the query results.
   */
  fun performQuery(table: String, columns: Array<String>, selection: String? = null): ArrayList<String> {
    assert(columns.isNotEmpty())
    readableDatabase.use { database ->
      query(database, table, columns, selection).use { cursor ->
        return readFromCursor(cursor, columns.size)
      }
    }
  }

  /**
   * Runs a raw SQL query and returns the results.
   *
   * @param select The columns to select.
   * @param from The tables to query from.
   * @param join The join conditions.
   * @param where Optional conditions to filter the results.
   * @return A list of strings representing the query results.
   */
  fun performRawQuery(select: Array<String>, from: Array<String>, join: Array<String>,
                      where: String? = null): ArrayList<String> {

    val query = StringBuilder("SELECT ")
    for ((i, field) in select.withIndex()) {
      query.append(field)
      if (i != select.lastIndex) query.append(", ")
    }

    query.append(" FROM ")
    for ((i, table) in from.withIndex()) {
      query.append(table)
      if (i != from.lastIndex) query.append(", ")
    }

    query.append(" WHERE ")
    for ((i, link) in join.withIndex()) {
      query.append(link)
      if (i != join.lastIndex) query.append(" and ")
    }

    if (where != null) query.append(" and $where")

    readableDatabase.use { db ->
      db.rawQuery("$query;", null).use { cursor ->
        return readFromCursor(cursor, select.size)
      }
    }
  }

  /**
   * Reads data from the cursor and returns it as a list of strings.
   *
   * @param cursor The cursor pointing to the result set.
   * @param numberOfColumns The number of columns in the result set.
   * @return A list of strings representing the results.
   */
  private fun readFromCursor(cursor: Cursor, numberOfColumns: Int): ArrayList<String> {
    val result = ArrayList<String>()
    cursor.moveToFirst()
    while (!cursor.isAfterLast) {
      val item = StringBuilder("")
      for (i in 0 until numberOfColumns) {
        item.append("${cursor.getString(i)} ")
      }
      result.add("$item")
      cursor.moveToNext()
    }
    return result
  }

  /**
   * Executes a query and returns a cursor pointing to the results.
   *
   * @param database The SQLiteDatabase instance to perform the query.
   * @param table The name of the table to query.
   * @param columns The columns to select.
   * @param selection An optional filter for the rows to return.
   * @return A cursor pointing to the results of the query.
   */
  private fun query(
    database: SQLiteDatabase, table: String, columns: Array<String>, selection: String?): Cursor {
    return database.query(table, columns, selection, null, null, null, null, null)
  }
}