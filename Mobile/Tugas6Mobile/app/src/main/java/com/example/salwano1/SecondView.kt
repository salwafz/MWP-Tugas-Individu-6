package com.example.salwano1

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat

class SecondView (context: Context, attributeSet: AttributeSet): View(context, attributeSet) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    val textJudul = Paint(Paint.ANTI_ALIAS_FLAG)
    val textPrem = Paint(Paint.ANTI_ALIAS_FLAG)
    val buttonRect = RectF()

    lateinit var vectorDrawable: Drawable

    init {
        textPaint.apply {
            color = Color.BLACK
            textSize = 55f
            textAlign = Paint.Align.LEFT
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        textJudul.apply {
            color = Color.BLACK
            textSize = 80f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        textPrem.apply {
            color = Color.WHITE
            textSize = 50f
            typeface = Typeface.DEFAULT_BOLD
            textAlign = Paint.Align.CENTER
        }

        vectorDrawable = ContextCompat.getDrawable(context, R.drawable.black)!!
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.color = Color.WHITE
        canvas.drawRect(0f, 0f, width.toFloat(), 150f, paint)

        vectorDrawable.setBounds(45, 40, 115, 110)
        vectorDrawable.draw(canvas)

        canvas.drawText("Subscribe", 190f, 95f, textPaint)

        paint.color = Color.BLACK
        paint.strokeWidth = 2f
        canvas.drawLine(0f, 150f, width.toFloat(), 150f, paint)

        val textY = 280f
        val textX = 550f
        canvas.drawText("Get Verified With Premium", textX, textY, textJudul)

        val text =
            "Enjoy an enhanced experience, exclusive creator tools, top-tier verification and security."
        val textPaint = TextPaint().apply {
            color = Color.BLACK
            textSize = 40f
        }

        val width = width - 40  // Sesuaikan dengan lebar view
        val staticLayout = StaticLayout(
            text, textPaint, width,
            Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false
        )

        canvas.save()
        canvas.translate(20f, 300f) // Sesuaikan posisi teks
        staticLayout.draw(canvas)
        canvas.restore()

        val btnEditX = 45f
        val btnEditY = 450f
        val btnEditWidth = 1000f
        val btnEditHeight = 150f

        buttonRect.set(btnEditX, btnEditY, btnEditX + btnEditWidth, btnEditY + btnEditHeight)

        // Menggambar background hitam untuk tombol
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
        canvas.drawRoundRect(buttonRect, 50f, 50f, paint)

        // Mengatur warna teks menjadi putih
        paint.color = Color.WHITE
        paint.textSize = 70f

        val buttonText = "Premium"
        val textWidth = paint.measureText(buttonText)
        canvas.drawText(buttonText, buttonRect.centerX() - textWidth / 2, buttonRect.centerY()+20f,paint)

        // Posisi awal untuk kotak pertama
        var boxTop = 650f
        val boxHeight = 350f
        val boxPadding = 20f
        val boxCornerRadius = 20f

        // Warna latar belakang kotak
        paint.color = Color.parseColor("#F0F0F0") // Warna abu-abu muda
        paint.style = Paint.Style.FILL

        // Kotak 1: Enhanced Experience
        buttonRect.set(btnEditX, boxTop, btnEditX + btnEditWidth, boxTop + boxHeight)
        canvas.drawRoundRect(buttonRect, boxCornerRadius, boxCornerRadius, paint)

        // Judul Kotak 1
        paint.color = Color.BLACK
        paint.textSize = 50f
        paint.typeface = Typeface.DEFAULT_BOLD // Teks bold
        canvas.drawText("Enhanced Experience", btnEditX + boxPadding, boxTop + 60f, paint)

        // Kembalikan typeface ke default untuk teks fitur
        paint.textSize = 40f
        paint.typeface = Typeface.DEFAULT // Kembalikan ke normal
        canvas.drawText("- Monthly Price Rp. 20,000", btnEditX + boxPadding, boxTop + 120f, paint)
        canvas.drawText("- Fully ad-free", btnEditX + boxPadding, boxTop + 170f, paint)
        canvas.drawText("- Larger reply boost", btnEditX + boxPadding, boxTop + 220f, paint)
        canvas.drawText("- Edit posts", btnEditX + boxPadding, boxTop + 270f, paint)
        canvas.drawText("- Longer posts", btnEditX + boxPadding, boxTop + 320f, paint)

        // Kotak 2: Grok AI
        boxTop += boxHeight + boxPadding
        buttonRect.set(btnEditX, boxTop, btnEditX + btnEditWidth, boxTop + boxHeight)
        paint.color = Color.parseColor("#F0F0F0")
        canvas.drawRoundRect(buttonRect, boxCornerRadius, boxCornerRadius, paint)

        // Judul Kotak 2
        paint.color = Color.BLACK
        paint.textSize = 50f
        paint.typeface = Typeface.DEFAULT_BOLD
        canvas.drawText("Grok AI", btnEditX + boxPadding, boxTop + 60f, paint)

        // Fitur-fitur dalam Kotak 2
        paint.typeface = Typeface.DEFAULT // Kembalikan ke normal
        paint.textSize = 40f
        canvas.drawText("- Monthly Price Rp. 25,000", btnEditX + boxPadding, boxTop + 120f, paint)
        canvas.drawText("- Higher usage limits", btnEditX + boxPadding, boxTop + 170f, paint)
        canvas.drawText("- Unlock DeepSearch & Think", btnEditX + boxPadding, boxTop + 220f, paint)
        canvas.drawText("- Early access to new features", btnEditX + boxPadding, boxTop + 270f, paint)

        // Kotak 3: Creator Hub
        boxTop += boxHeight + boxPadding
        buttonRect.set(btnEditX, boxTop, btnEditX + btnEditWidth, boxTop + boxHeight)
        paint.color = Color.parseColor("#F0F0F0")
        canvas.drawRoundRect(buttonRect, boxCornerRadius, boxCornerRadius, paint)

        // Judul Kotak 3
        paint.color = Color.BLACK
        paint.textSize = 50f
        paint.typeface = Typeface.DEFAULT_BOLD
        canvas.drawText("Creator Hub", btnEditX + boxPadding, boxTop + 60f, paint)

        // Fitur-fitur dalam Kotak 3
        paint.typeface = Typeface.DEFAULT // Kembalikan ke normal
        paint.textSize = 40f
        canvas.drawText("- Monthly Price Rp. 40,000", btnEditX + boxPadding, boxTop + 120f, paint)
        canvas.drawText("- Create and publish articles", btnEditX + boxPadding, boxTop + 170f, paint)
        canvas.drawText("- Monetize your content", btnEditX + boxPadding, boxTop + 220f, paint)
        canvas.drawText("- Offer subscriptions to followers", btnEditX + boxPadding, boxTop + 270f, paint)
        canvas.drawText("- Insights into performance", btnEditX + boxPadding, boxTop + 320f, paint)


        // Kotak 4: Verifications & Security
        boxTop += boxHeight + boxPadding
        buttonRect.set(btnEditX, boxTop, btnEditX + btnEditWidth, boxTop + boxHeight)
        paint.color = Color.parseColor("#F0F0F0")
        canvas.drawRoundRect(buttonRect, boxCornerRadius, boxCornerRadius, paint)

        // Judul Kotak 4
        paint.color = Color.BLACK
        paint.textSize = 50f
        paint.typeface = Typeface.DEFAULT_BOLD
        canvas.drawText("Verifications & Security", btnEditX + boxPadding, boxTop + 60f, paint)

        // Fitur-fitur dalam Kotak 4
        paint.typeface = Typeface.DEFAULT // Kembalikan ke normal
        paint.textSize = 40f
        canvas.drawText("- Monthly Price Rp. 45,000", btnEditX + boxPadding, boxTop + 120f, paint)
        canvas.drawText("- Verified account status", btnEditX + boxPadding, boxTop + 170f, paint)
        canvas.drawText("- Secure your identity", btnEditX + boxPadding, boxTop + 220f, paint)
        canvas.drawText("- Encrypted direct messages", btnEditX + boxPadding, boxTop + 270f, paint)

        // Posisi untuk tombol baru
        val btnSubscribeY = btnEditY + btnEditHeight + 1550f // Tambahkan jarak dari tombol premium
        val btnSubscribeWidth = btnEditWidth
        val btnSubscribeHeight = 140f

        // Menggambar background hitam untuk tombol baru
        buttonRect.set(btnEditX, btnSubscribeY, btnEditX + btnSubscribeWidth, btnSubscribeY + btnSubscribeHeight)
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
        canvas.drawRoundRect(buttonRect, 100f, 100f, paint)

        // Mengatur warna teks menjadi putih
        paint.color = Color.WHITE
        paint.textSize = 50f

        val subscribeText = "Starting at Rp.20,000"
        val subscribeTextWidth = paint.measureText(subscribeText)
        canvas.drawText(subscribeText, buttonRect.centerX() - subscribeTextWidth / 2, buttonRect.centerY() + 20f, paint)


    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Deteksi klik pada tombol panah (kembali)
                if (event.x in 0f..150f && event.y in 0f..150f) {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                    return true
                }

                // Deteksi klik pada tombol "Starting at Rp.20,000"
                val btnSubscribeY = 450f + 150f + 1550f // Posisi Y tombol subscribe
                val btnSubscribeHeight = 140f
                val btnSubscribeWidth = 1000f

                if (event.x in 45f..(45f + btnSubscribeWidth) &&
                    event.y in btnSubscribeY..(btnSubscribeY + btnSubscribeHeight)) {
                    showPopup()
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun showPopup() {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Premium Subscription")
            .setMessage("Anda akan berlangganan paket Premium mulai dari Rp.20,000/bulan. Lanjutkan?")
            .setPositiveButton("YA") { dialog, _ ->
                // Tampilkan Toast jika "YA" diklik
                Toast.makeText(context, "Berhasil berlangganan!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("TIDAK") { dialog, _ ->
                // Tidak ada aksi jika "TIDAK" diklik
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }

}