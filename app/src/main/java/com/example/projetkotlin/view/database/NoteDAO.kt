package com.example.projetkotlin.view.database

import android.content.ContentValues
import android.content.Context
import com.example.projetkotlin.model.Note

class NoteDAO(context: Context) {
    private val helper = BddHelper(context)

    fun insert(note: Note): Long ?
    {
        val tagString = note.tag.joinToString(separator = ",")
        val db = helper.writableDatabase
        val values = ContentValues().apply {
            put(Contracts.Notes.COLUMN_TITLE, note.title)
            put(Contracts.Notes.COLUMN_CONTENT, note.content)
            put(Contracts.Notes.COLUMN_TAG, tagString)
            put(Contracts.Notes.COLUMN_DATE, note.date)
        }

        return db?.insert(Contracts.Notes.TABLE_NAME, null, values)
    }

    fun getAllNotes(): ArrayList<Note> {
        val db = helper.readableDatabase
        val notes = ArrayList<Note>()

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        val projection = arrayOf(
            Contracts.Notes.COLUMN_ID,
            Contracts.Notes.COLUMN_TITLE,
            Contracts.Notes.COLUMN_CONTENT,
            Contracts.Notes.COLUMN_TAG,
            Contracts.Notes.COLUMN_DATE,
        )

        // Perform a query to check if any rows match the given email and password
        val cursor = db.query(
            Contracts.Notes.TABLE_NAME,   // The table to query
            projection,
            null,                // The values for the WHERE clause
            null,                // The columns for the WHERE clause
            null,                // Don't group the rows
            null,                 // Don't filter by row groups
            null                 // The sort order
        )

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(Contracts.Notes.COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(Contracts.Notes.COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(Contracts.Notes.COLUMN_CONTENT))
            val tag = cursor.getString(cursor.getColumnIndexOrThrow(Contracts.Notes.COLUMN_TAG))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(Contracts.Notes.COLUMN_DATE))

            val tags = tag.split(",")

            notes.add(Note(id, title, content, tags, date))
        }
        cursor.close()

        return notes
    }

    fun deleteNoteById(id: Int): Int {
        val db = helper.writableDatabase
        val selection = "${Contracts.Notes.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        return db.delete(Contracts.Notes.TABLE_NAME, selection, selectionArgs)
    }

    fun getNoteById(id: Int): Note? {
        val db = helper.readableDatabase
        val projection = arrayOf(
            Contracts.Notes.COLUMN_ID,
            Contracts.Notes.COLUMN_TITLE,
            Contracts.Notes.COLUMN_CONTENT,
            Contracts.Notes.COLUMN_TAG,
            Contracts.Notes.COLUMN_DATE,
        )
        val selection = "${Contracts.Notes.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(id.toString())

        val cursor = db.query(
            Contracts.Notes.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var note: Note? = null
        if (cursor.moveToNext()) {
            val title = cursor.getString(cursor.getColumnIndexOrThrow(Contracts.Notes.COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(Contracts.Notes.COLUMN_CONTENT))
            val tag = cursor.getString(cursor.getColumnIndexOrThrow(Contracts.Notes.COLUMN_TAG))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(Contracts.Notes.COLUMN_DATE))

            val tags = tag.split(",")

            note = Note(id, title, content, tags, date)
        }
        cursor.close()

        return note
    }

    fun updateNoteById(note: Note): Int {

        val tagString = note.tag.joinToString(separator = ",")
        val db = helper.writableDatabase
        val values = ContentValues().apply {
            put(Contracts.Notes.COLUMN_TITLE, note.title)
            put(Contracts.Notes.COLUMN_CONTENT, note.content)
            put(Contracts.Notes.COLUMN_TAG, tagString)
            put(Contracts.Notes.COLUMN_DATE, note.date)
        }
        val selection = "${Contracts.Notes.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(note.id.toString())

        return db.update(Contracts.Notes.TABLE_NAME, values, selection, selectionArgs)
    }
}