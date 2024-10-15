package source.local

import org.example.project.MovieApp

internal class Database(databaseDriverFactory: DriverFactory) {

    private val database by lazy { MovieApp(databaseDriverFactory.createDriver()) }

    val favoriteMovieQueries by lazy { database.favoriteMovieQueries }
}
