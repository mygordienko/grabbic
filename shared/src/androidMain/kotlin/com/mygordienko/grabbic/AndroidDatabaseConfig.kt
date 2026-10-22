package com.mygordienko.grabbic

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mygordienko.grabbic.clip.adapter.db.ClipSourceDatabase
import kotlin.jvm.java

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<ClipSourceDatabase> {
    val appContext = context.applicationContext
    val dpPath = appContext.getDatabasePath("grabbic.db")

    return Room.databaseBuilder(appContext, ClipSourceDatabase::class.java, dpPath.path)
}