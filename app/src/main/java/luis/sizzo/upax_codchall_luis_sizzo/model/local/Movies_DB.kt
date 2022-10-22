package luis.sizzo.upax_codchall_luis_sizzo.model.local

import androidx.room.Database
import androidx.room.RoomDatabase
import luis.sizzo.upax_codchall_luis_sizzo.model.local.movies_local.MoviesLocalEntity

@Database(
    entities = [
        MoviesLocalEntity::class
    ],
    version = 1
)
abstract class Movies_DB : RoomDatabase() {
    abstract fun getGithubDao(): Movies_Dao
}