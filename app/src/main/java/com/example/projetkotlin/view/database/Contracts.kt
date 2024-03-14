package com.example.projetkotlin.view.database

object Contracts {

    object Notes {
        const val TABLE_NAME = "notes"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_TAG = "tag"
        const val COLUMN_DATE = "date"
        const val COLUMN_USER_ID = "user_id"

        const val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_TITLE TEXT," +
                "$COLUMN_CONTENT TEXT," +
                "$COLUMN_TAG TEXT," +
                "$COLUMN_DATE TEXT" +
                ")"

        const val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}