package com.example.salwano1

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import kotlinx.coroutines.*

class PostView(context: Context, attrs: AttributeSet?) : View(context, attrs) {


    private val xBounds = android.graphics.Rect(40, 30, 80, 90)

    private val paint = Paint().apply {
        color = Color.BLACK
        textSize = 60f
        isAntiAlias = true
    }

//    private val postButtonPaint = Paint().apply {
//        color = Color.parseColor("#1DA1F2") // Twitter Blue
//        textSize = 48f
//        isFakeBoldText = true
//        isAntiAlias = true
//    }

    fun simpanPostKeDatabase(idPengguna: Int, teks: String, onSuccess: () -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val conn = koneksi.connection()
                val sql = "INSERT INTO post (id_pengguna, content) VALUES (?, ?)"
                val stmt = conn?.prepareStatement(sql)
                stmt?.setInt(1, idPengguna)     // ini id user
                stmt?.setString(2, teks)        // ini isi tweet/post
                stmt?.executeUpdate()
                conn?.close()

                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Tombol X (kiri atas)
        canvas.drawText("X", 40f, 80f, paint)

        // Foto profil (lingkaran)
        canvas.drawCircle(90f, 220f, 60f, paint)


        // Garis bawah input teks
        // canvas.drawLine(150f, 240f, width - 40f, 240f, paint)

        // Tombol Post (kanan atas)
//        canvas.drawRoundRect(width - 250f, 40f, width - 40f, 120f, 30f, 30f, postButtonPaint)
//        canvas.drawText("Post", width - 185f, 95f, Paint().apply {
//            color = Color.WHITE
//            textSize = 40f
//            isFakeBoldText = true
//        })
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val x = event.x.toInt()
            val y = event.y.toInt()

            if (xBounds.contains(x, y)) {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
                return true
            }
        }
        return super.onTouchEvent(event)
    }

}
