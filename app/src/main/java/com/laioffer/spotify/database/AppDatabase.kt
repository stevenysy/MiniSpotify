package com.laioffer.spotify.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.laioffer.spotify.model.Album

@Database(entities = [Album::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun databaseDao(): DatabaseDao
}