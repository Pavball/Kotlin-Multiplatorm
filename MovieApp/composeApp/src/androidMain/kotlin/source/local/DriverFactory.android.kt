package source.local

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.example.project.MovieApp

actual class DriverFactory(private val context: Context) {
    private var sqlDriver: SqlDriver? = null

    actual fun createDriver(): SqlDriver {
        if (sqlDriver == null) {
            sqlDriver = AndroidSqliteDriver(MovieApp.Schema, context, "MovieApp.db")
        }
        return sqlDriver!!
    }
}
