package luis.sizzo.upax_codchall_luis_sizzo.model.local.movies_local

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "movie_local", primaryKeys = ["id"])
data class MoviesLocalEntity (
    @ColumnInfo(name = "id")val id: Int,
    @ColumnInfo(name = "title")val title: String?,
    @ColumnInfo(name = "section")val section: String?,
    @ColumnInfo(name = "popularity")val popularity: Float?,
    @ColumnInfo(name = "poster_path")val poster_path: String?,
    @ColumnInfo(name = "release_date")val release_date: String?
)