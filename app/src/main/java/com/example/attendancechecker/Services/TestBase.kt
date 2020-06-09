package com.example.attendancechecker.Services

import android.annotation.SuppressLint
import android.util.Log
import java.sql.*
import java.util.*

class TestBase {

    val db = DB()

    @Throws(Exception::class)
    fun ex_main() {
        val scanner = Scanner(System.`in`)
        val st: Statement = connection.createStatement()
        var rs: ResultSet
        st.use { st ->
            while (true) {
                val command: String = scanner.nextLine()
                if (command.equals("stop", ignoreCase = true)) break
                if (command.split(" ").toTypedArray()[0].equals("select", ignoreCase = true)) {
                    rs = st.executeQuery(command)
                    while (rs.next()) {
                        Log.d("SQL_TEST", rs.getString(db.NAME) + "\n" + rs.getString(db.SURNAME) + "\n" + rs.getString(db.THIRDNAME))
                    }
                } else Log.d("SQL_TEST", "Выполнение запроса: " + st.execute(command))
            }
        }
    }

    private val connection: Connection = db.getConnection()!!

}