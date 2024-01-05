package com.app.to_do.di

import android.content.Context
import androidx.room.Room
import com.app.to_do.data.ToDoDatabase
import com.app.to_do.utils.Constant.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideToDoDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, ToDoDatabase::class.java, DATABASE_NAME)
            .build()

    @Singleton
    @Provides
    fun provideToDoDao(
        toDoDatabase: ToDoDatabase
    ) = toDoDatabase.toDoDao()

}