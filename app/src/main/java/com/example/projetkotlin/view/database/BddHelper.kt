package com.example.projetkotlin.view.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BddHelper(context : Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(Contracts.Notes.CREATE_TABLE)
    }



    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(Contracts.Notes.DROP_TABLE)

        onCreate(db)
    }

    companion object {
        private const val DATABASE_VERSION = 3
        private const val DATABASE_NAME = "projet.db"
    }
}