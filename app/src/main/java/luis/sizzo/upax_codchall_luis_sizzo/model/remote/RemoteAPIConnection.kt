package luis.sizzo.upax_codchall_luis_sizzo.model.remote

import luis.sizzo.upax_codchall_luis_sizzo.common.*
import luis.sizzo.upax_codchall_luis_sizzo.model.local.movies_local.MoviesLocalEntity
import retrofit2.*
import retrofit2.http.*

interface RemoteAPIConnection{

    @GET(END_POINT_MOVIES)
    suspend fun getMovies(
        @Path("section") section: String,
        @Query("api_key") api_key: String = API_KEY,
        ): Response<MoviesObject>

//    @GET("$PARAM_USER/{username}")
//    suspend fun getUsers(
//        @Path("username") username: String,
//        @Query(PARAM_AUTH) auth: String = AUTORIZATION,
//        @Query(PARAM_ACCEPT) accept: String = ACCEPT,
//    ): Response<UserInfo>
//
//    @GET("$PARAM_USER/{username}/repos")
//    suspend fun getUsersRepo(
//        @Path("username") username: String,
//        @Query(PARAM_AUTH) auth: String = AUTORIZATION,
//        @Query(PARAM_ACCEPT) accept: String = ACCEPT,
//    ): Response<List<UserRepositoriesEntity>>
//
//    @GET("$PARAM_REPOS/{username}/{reponame}")
//    suspend fun getUsersSearchRepo(
//        @Path("username") username: String,
//        @Path("reponame") reponame: String,
//        @Query(PARAM_AUTH) auth: String = AUTORIZATION,
//        @Query(PARAM_ACCEPT) accept: String = ACCEPT,
//    ): Response<List<UserRepositoriesEntity>>
}