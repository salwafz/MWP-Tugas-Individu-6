package com.example.salwano1

import android.util.Log
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object koneksi {
    private const val URL = "jdbc:postgresql://10.0.2.2:5432/dbTwt"
    private const val USER = "postgres"
    private const val PASSWORD = "kuanlinee"

    fun connection (): Connection?{
        return try{
            Class.forName("org.postgresql.Driver")
            DriverManager.getConnection(URL,USER, PASSWORD).also {
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("DB_ERROR", "Koneksi gagal: ${e.message}", e)
            null
        }
    }
}