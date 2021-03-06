package com.example.attendancechecker.Services

import android.util.Log
import java.sql.*

class TestBase {

    val db = DB()


    @Throws(Exception::class)
    fun ex_main() {
        val st: Statement = connection.createStatement()
        var rs: ResultSet
        st.use { st ->
                val command = "SELECT * FROM Pupils"
                if (command.split(" ").toTypedArray()[0].equals("select", ignoreCase = true)) {
                    rs = st.executeQuery(command)
                    while (rs.next()) {
                        Log.d("SQL_TEST", rs.getString(db.NAME) + "\n" + rs.getString(db.SURNAME) + "\n" + rs.getString(db.THIRDNAME))
                    }
                } else Log.d("SQL_TEST", "Выполнение запроса: " + st.execute(command))
        }
    }

    private val connection: Connection = db.getConnection()!!

}