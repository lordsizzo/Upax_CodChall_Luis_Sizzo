package luis.sizzo.upax_codchall_luis_sizzo.model.remote

data class MoviesObject(val results: List<MoviesResults>)

data class MoviesResults(
    val id: Int,
    val title: String,
    val popularity: Float,
    val poster_path: String,
    val release_date: String
)
