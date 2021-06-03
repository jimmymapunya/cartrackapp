package co.cartrack.za.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import co.cartrack.za.model.Search

@Database(
    entities = [Search::class],
    version = 1
)

abstract class OmdbDatabase  : RoomDatabase(){

    abstract fun getOmdbDao(): OmdbDao

    companion object{
        @Volatile
        private var instance: OmdbDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance?: createDatabase(context).also{ instance = it}
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                OmdbDatabase::class.java,
                "search_results.db"
            ).build()
    }
}