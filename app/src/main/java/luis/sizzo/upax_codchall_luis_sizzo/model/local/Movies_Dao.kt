package luis.sizzo.upax_codchall_luis_sizzo.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import luis.sizzo.upax_codchall_luis_sizzo.model.local.movies_local.MoviesLocalEntity

@Dao
interface Movies_Dao {

    //USER INFO
    @Query("SELECT * FROM movie_local")
    suspend fun getAllMoviesInfo(): List<MoviesLocalEntity>

    //USER INFO
    @Query("SELECT * FROM movie_local WHERE section LIKE :section")
    suspend fun getAllMoviesInfoBySection(section: String): List<MoviesLocalEntity>

    @Insert()
    suspend fun insertAllMoviesInfo(moviesLocal: MoviesLocalEntity)

    @Query("DELETE FROM movie_local")
    suspend fun deleteMoviesInfo()
}