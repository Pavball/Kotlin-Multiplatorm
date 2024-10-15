package source.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.example.project.FavoriteMovie

internal interface LocalDataSource {

    fun getAllFavoriteMovies(): Flow<List<FavoriteMovie>>

    suspend fun deleteFavoriteMovieById(movieId: Long)

    suspend fun insertFavoriteMovieById(movieId: Long, posterPath: String, movieType: String)

}

internal class LocalDataSourceImpl(
    private val database: Database
) : LocalDataSource {

    override fun getAllFavoriteMovies(): Flow<List<FavoriteMovie>> {
        return database.favoriteMovieQueries.selectAllFavoriteMovies().asFlow().mapToList(Dispatchers.Default)
    }

    override suspend fun deleteFavoriteMovieById(movieId: Long) {
        withContext(Dispatchers.IO) {
            database.favoriteMovieQueries.removeMovieById(movieId)
        }
    }

    override suspend fun insertFavoriteMovieById(movieId: Long, posterPath: String, movieType: String) {
        withContext(Dispatchers.IO) {
            database.favoriteMovieQueries.insertFavoriteMoviesById(movieId, posterPath, movieType)
        }
    }

}
