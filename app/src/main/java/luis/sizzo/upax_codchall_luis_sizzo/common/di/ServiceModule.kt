package luis.sizzo.upax_codchall_luis_sizzo.common.di

import dagger.*
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.*
import luis.sizzo.upax_codchall_luis_sizzo.common.BASE_URL
import luis.sizzo.upax_codchall_luis_sizzo.model.local.Movies_Dao
import luis.sizzo.upax_codchall_luis_sizzo.model.remote.RemoteAPIConnection
import luis.sizzo.upax_codchall_luis_sizzo.model.res.Repository
import luis.sizzo.upax_codchall_luis_sizzo.model.res.RepositoryImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    fun provideApiService(): RemoteAPIConnection =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RemoteAPIConnection::class.java)

    @Provides
    fun provideRepositoryLayer(service: RemoteAPIConnection, dao: Movies_Dao): Repository =
        RepositoryImpl(service, dao)

    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun provideCoroutineScope(dispatcher: CoroutineDispatcher): CoroutineScope =
        CoroutineScope(dispatcher)


}