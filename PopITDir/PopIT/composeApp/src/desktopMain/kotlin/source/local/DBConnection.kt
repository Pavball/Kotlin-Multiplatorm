package source.local

import java.sql.Connection
import java.sql.DriverManager

fun getConnection(): Connection {
    val url = "jdbc:mysql://localhost:3307/popitdb"
    val user = "root"
    val password = ""

    return DriverManager.getConnection(url, user, password)
}
