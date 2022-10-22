package luis.sizzo.upax_codchall_luis_sizzo.common.di

import android.content.Context
import androidx.room.Room
import dagger.*
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import luis.sizzo.upax_codchall_luis_sizzo.model.local.Movies_DB
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDBModule {

    private const val NameDatabase = "user_github_database"

    @Provides
    @Singleton
    fun provideRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            Movies_DB::class.java,
            NameDatabase
        ).build()

    @Provides
    @Singleton
    fun provideGithubDao(db: Movies_DB) = db.getGithubDao()

}