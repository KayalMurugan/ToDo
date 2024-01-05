package com.app.to_do.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.app.to_do.data.models.ToDoTask
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {

    @Query("SELECT * FROM ToDo_table ORDER BY id ASC")
    fun getAllTask(): Flow<List<ToDoTask>>

    @Query("SELECT * FROM ToDo_table WHERE id=:taskId")
    fun getSelectedTask(taskId: Int): Flow<ToDoTask>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(toDoTask: ToDoTask)

    @Update
    suspend fun updateTask(toDoTask: ToDoTask)

    @Delete
    suspend fun deleteTask(toDoTask: ToDoTask)

    @Query("DELETE FROM ToDo_table")
    suspend fun deleteAllTask()

    @Query("SELECT * FROM ToDo_table WHERE title LIKE :searchQuery OR description LIKE :searchQuery")
    fun searchDataBase(searchQuery: String): Flow<List<ToDoTask>>

    @Query(
        """
            SELECT * FROM ToDo_table ORDER BY
        CASE
            WHEN priority LIKE 'L%' THEN 1
            WHEN priority LIKE 'M%' THEN 2 
            WHEN priority LIKE 'H%' THEN 3 
        END
        """)
    fun sortByLowPriority(): Flow<List<ToDoTask>>

    @Query(
        """
            SELECT * FROM ToDo_table ORDER BY 
        CASE 
            WHEN priority LIKE 'H%' THEN 1 
            WHEN priority LIKE 'M%' THEN 2 
            WHEN priority LIKE 'L%' THEN 3
        END
        """)
    fun sortByHighPriority(): Flow<List<ToDoTask>>

}