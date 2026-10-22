package com.mygordienko.grabbic

import androidx.room.Room
import androidx.room.RoomDatabase
import com.mygordienko.grabbic.clip.adapter.db.ClipSourceDatabase
import java.io.File

fun getDatabaseBuilder(): RoomDatabase.Builder<ClipSourceDatabase> {
    val userHome = System.getProperty("user.home")
    val appDataDir = File(userHome, ".grabbic")
    if (!appDataDir.exists()) {
        appDataDir.mkdirs()
    }

    val dbFile = File(appDataDir, "my_room.db")
    return Room.databaseBuilder<ClipSourceDatabase>(
        name = dbFile.absolutePath,
    )
}