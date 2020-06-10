package com.example.attendancechecker.Services

import android.annotation.SuppressLint
import android.os.StrictMode
import android.util.Log
import java.net.URI
import java.net.URISyntaxException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class DB {

    val ID = "id"
    val NAME = "name"
    val SURNAME = "surname"
    val THIRDNAME = "thirdname"
    val GROUP = "groupname"
    val HASHCODE = "hashcode"
    val COMEDATE = "comedate"
    val LEAVEDATE = "leavedate"
    val AVATAR = "avatar"

    init {
        Class.forName("org.postgresql.Driver")
    }

    @SuppressLint("AuthLeak")
    @Throws(URISyntaxException::class, SQLException::class)
    fun getConnection(): Connection? {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        Log.d("SQL", "Starting to create connection!")
        val dbUri = URI("postgres://msdnsbjhzhioiv:2d07eb87323bdf124993d05e1d48ec6bc7ab09a4f43b165f9103743b7ce1279a@ec2-54-247-79-178.eu-west-1.compute.amazonaws.com:5432/dieipc1jhv4m9")
        val username: String = dbUri.userInfo.split(":")[0]
        val password: String = dbUri.userInfo.split(":")[1]
        val dbUrl = "jdbc:postgresql://" + dbUri.host + dbUri.path.toString() + "?sslmode=require"
        Log.d("SQL", "Connection ready!")
        return DriverManager.getConnection(dbUrl, username, password)
    }
}