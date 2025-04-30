package com.example.salwano1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import android.util.Log
import java.sql.SQLException

class MainView (context: Context, attributeSet: AttributeSet): View(context, attributeSet) {
    companion object {
        private const val MAX_POST_LENGTH=500
        }

    val radius = 120f
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val paintCircle = Paint(Paint.ANTI_ALIAS_FLAG)
    val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    val normalTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    val bioTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    val linkTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    val verifTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    lateinit var vectorDrawable: Drawable
    lateinit var vectorDrawable2: Drawable
    lateinit var vectorDrawable3: Drawable
    lateinit var vectorDrawable4: Drawable
    lateinit var vectorDrawable5: Drawable
    lateinit var vectorDrawable6: Drawable
    lateinit var vectorDrawable7: Drawable
    val buttonRect = RectF()
    private var verifBound = Rect()
    private var fabButtonBounds = Rect()


    private val fabPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#1DA1F2")
        style = Paint.Style.FILL
    }

    private val fabTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE  // Warna ikon "+"
        textSize = 100f
        textAlign = Paint.Align.CENTER
    }
    // Posisi FAB
    private var fabX = 0f
    private var fabY = 0f
    private val fabRadius = 90f

    private val tabTitles = listOf("Posts", "Replies", "Highlights", "Articles", "Media")
    private val tabRects = mutableListOf<RectF>()
    private var selectedTabIndex = 0

    private val postinganList = mutableListOf<String>()
    private var showUnfollowPopup = false
    private var unfollowConfirmRects: List<RectF> = emptyList()
    private var name: String = ""
    private var username: String = ""
    private var bio: String = ""
    private val idPengguna=1

    init {
        textPaint.apply {
            color = Color.BLACK
            textSize = 50f // Ukuran teks
            textAlign = Paint.Align.LEFT // Mengatur teks agar berada di tengah
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        normalTextPaint.apply {
            color = Color.GRAY
            textSize = 40f // Ukuran teks
            textAlign = Paint.Align.LEFT // Mengatur teks agar berada di tengah
        }
        bioTextPaint.apply {
            color = Color.BLACK
            textSize = 40f // Ukuran teks
            textAlign = Paint.Align.LEFT // Mengatur teks agar berada di tengah
//            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        linkTextPaint.apply {
            color = Color.parseColor("#87CEFA")
            textSize = 40f // Ukuran teks
            textAlign = Paint.Align.LEFT // Mengatur teks agar berada di tengah
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        verifTextPaint.apply {
            color = Color.parseColor("#87CEFA")
            textSize = 40f // Ukuran teks
            textAlign = Paint.Align.LEFT // Mengatur teks agar berada di tengah
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        vectorDrawable = ContextCompat.getDrawable(context, R.drawable.bussiness)!!
        vectorDrawable2 = ContextCompat.getDrawable(context, R.drawable.location)!!
        vectorDrawable3 = ContextCompat.getDrawable(context, R.drawable.calendar)!!
        vectorDrawable4 = ContextCompat.getDrawable(context, R.drawable.link)!!
        vectorDrawable5 = ContextCompat.getDrawable(context, R.drawable.back)!!
        vectorDrawable6 = ContextCompat.getDrawable(context, R.drawable.verified)!!
        vectorDrawable6 = ContextCompat.getDrawable(context, R.drawable.ppcat)!!

        ambilDataDariDatabase()
        ambilPostinganDariDatabase()
    }

    fun insertPostingan(idPengguna: Int, content: String) {
        if (content.length > MAX_POST_LENGTH) {
            Toast.makeText(context, "Maksimal $MAX_POST_LENGTH karakter", Toast.LENGTH_SHORT).show()
            return
        }

        Thread {
            val conn = koneksi.connection()
            if (conn == null) {
                showToastOnUiThread("Gagal koneksi ke database")
                return@Thread
            }

            try {
                val query = "INSERT INTO post (id_pengguna, content) VALUES (?, ?)"
                val statement = conn.prepareStatement(query)
                statement.setInt(1, idPengguna)
                statement.setString(2, content)

                val rowsAffected = statement.executeUpdate()

                if (rowsAffected > 0) {
                    showToastOnUiThread("Postingan berhasil dikirim")
                    ambilPostinganDariDatabase() // Refresh data setelah insert
                } else {
                    showToastOnUiThread("Gagal mengirim postingan")
                }
            } catch (e: SQLException) {
                logAndShowError("Error SQL saat menyisipkan postingan", e)
            } catch (e: Exception) {
                logAndShowError("Error saat menyisipkan postingan", e)
            } finally {
                closeConnection(conn)
            }
        }.start()
    }

    private fun ambilPostinganDariDatabase() {
        Thread {
            val conn = koneksi.connection()
            if (conn == null) {
                Log.e("DB_ERROR", "Koneksi gagal")
                return@Thread
            }

            try {
                val query = "SELECT content FROM post WHERE id_pengguna = ? ORDER BY id DESC"
                val statement = conn.prepareStatement(query)
                statement.setInt(1, idPengguna)

                val result = statement.executeQuery()
                val tempList = mutableListOf<String>()

                while (result.next()) {
                    result.getString("content")?.let { content ->
                        tempList.add(content)
                    }
                }

                updatePostinganList(tempList)
            } catch (e: SQLException) {
                logAndShowError("Error SQL saat mengambil postingan", e)
            } catch (e: Exception) {
                logAndShowError("Error saat mengambil postingan", e)
            } finally {
                closeConnection(conn)
            }
        }.start()
    }

    private fun ambilDataDariDatabase() {
        Thread {
            val conn = koneksi.connection()
            if (conn == null) {
                Log.e("DB_ERROR", "Koneksi gagal")
                return@Thread
            }

            try {
                val query = "SELECT name, username, bio FROM pengguna WHERE id = 1"
                val statement = conn.createStatement()
                val result = statement.executeQuery(query)

                if (result.next()) {
                    name = result.getString("name") ?: ""
                    username = result.getString("username") ?: ""
                    bio = result.getString("bio") ?: ""

                    Log.d("DB_RESULT", "nama: $name, username: $username, bio: $bio")
                    refreshUi()
                }
            } catch (e: SQLException) {
                logError("Error SQL saat mengambil data pengguna", e)
            } catch (e: Exception) {
                logError("Error saat mengambil data pengguna", e)
            } finally {
                closeConnection(conn)
            }
        }.start()
    }

    private fun showToastOnUiThread(message: String) {
        (context as Activity).runOnUiThread {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun logAndShowError(message: String, e: Exception) {
        Log.e("DB_ERROR", "$message: ${e.message}")
        showToastOnUiThread("Error: ${e.message ?: "Terjadi kesalahan"}")
    }

    private fun logError(message: String, e: Exception) {
        Log.e("DB_ERROR", "$message: ${e.message}")
    }

    private fun closeConnection(conn: java.sql.Connection?) {
        try {
            conn?.close()
        } catch (e: SQLException) {
            Log.e("DB_ERROR", "Gagal menutup koneksi: ${e.message}")
        }
    }

    private fun updatePostinganList(newList: List<String>) {
        (context as Activity).runOnUiThread {
            postinganList.clear()
            postinganList.addAll(newList.filterNotNull()) // Pastikan tidak ada elemen null
            invalidate()
        }
    }

    private fun refreshUi() {
        (context as Activity).runOnUiThread {
            invalidate()
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.color = Color.parseColor("#1DA1F2")
        paintCircle.color = Color.LTGRAY

        canvas?.drawRect(0f, 0f, width - 0f, height - 1800f, paint)

        val backX = 45f // Menyesuaikan posisi X gambar vektor
        val backY = 40f
        val backWidth = 70
        val backHeight = 70
        vectorDrawable5.setBounds(
            backX.toInt(),
            backY.toInt(),
            backX.toInt() + backWidth,
            backY.toInt() + backHeight
        )
        vectorDrawable5.draw(canvas)

        // Variabel untuk posisi tiga titik
        val dotX = width - 85f // Posisi X (tepi kanan layar)
        val dotY = 50f // Posisi Y (di bagian atas)
        val dotRadius = 8f // Ukuran titik
        val dotSpacing = 25f // Jarak antar titik
        paint.color = Color.WHITE // Warna titik
        canvas.drawCircle(dotX, dotY, dotRadius, paint)
        canvas.drawCircle(dotX, dotY + dotSpacing, dotRadius, paint)
        canvas.drawCircle(dotX, dotY + 2 * dotSpacing, dotRadius, paint)

        val circleX = radius + 30f // Jarak dari tepi kiri
        val circleY = height - radius - 1700f // Jarak dari tepi bawah
        canvas?.drawCircle(circleX, circleY, radius, paintCircle)

        val textY = circleY + radius + 60f // Tambahkan 50 piksel di bawah lingkaran
        val textX = circleX - radius
        canvas.drawText("iniSalwaa", textX, textY, textPaint)

        val verifX = textX + 250f
        val verifY = 589f
        val verifWidth = 50
        val verifHeight = 50
        vectorDrawable6.setBounds(verifX.toInt(), verifY.toInt(), verifX.toInt() + verifWidth, verifY.toInt() + verifHeight)
        vectorDrawable6.draw(canvas)

        val verifYZ = textY + -285f // Tambahkan 50 piksel di bawah teks pertama
        val verifXY = verifY + 40f
        canvas.drawText("Get Verified", verifYZ, verifXY, verifTextPaint)
        normalTextPaint.getTextBounds("Get Verified", 0, "Get Verified".length, verifBound)
        verifBound.offset(verifYZ.toInt(), verifXY.toInt())

        val textY2 = textY + 55f // Tambahkan 50 piksel di bawah teks pertama
        canvas.drawText("@salwafz", textX, textY2, normalTextPaint)

        val textY3 = textY2 + 70f // Tambahkan 50 piksel di bawah teks pertama
        canvas.drawText("Empty here!!", textX, textY3, bioTextPaint)

        val badgeX = textX  // Menyesuaikan posisi X gambar vektor
        val badgeY = textY3 + 40f // Menempatkan gambar di bawah teks bio
        val badgeWidth = 50
        val badgeHeight = 50
        vectorDrawable.setBounds(
            badgeX.toInt(),
            badgeY.toInt(),
            badgeX.toInt() + badgeWidth,
            badgeY.toInt() + badgeHeight
        )
        vectorDrawable.draw(canvas)

        val verY4 = badgeX + 70f // Jarak dari tepi kiri
        val horiY4 = textY3 + 80f // Jarak dari tepi bawah
        canvas.drawText("Programmer", verY4, horiY4, normalTextPaint)

        val locX = textX  // Menyesuaikan posisi X gambar vektor kedua
        val locY =
            badgeY + badgeHeight + 20f // Menempatkan gambar kedua di bawah gambar pertama dengan jarak 20 piksel
        val locWidth = 50
        val locHeight = 50
        vectorDrawable2.setBounds(
            locX.toInt(),
            locY.toInt(),
            locX.toInt() + locWidth,
            locY.toInt() + locHeight
        )
        vectorDrawable2.draw(canvas)

        val loctextX = locX + 70f // Jarak dari tepi kiri
        val loctextY = horiY4 + 70f // Jarak dari tepi bawah
        canvas.drawText("Surabaya, Indonesia", loctextX, loctextY, normalTextPaint)

        val linkX = loctextX + 390f // Menyesuaikan posisi X gambar vektor
        val linkY = locY
        val linkWidth = 50
        val linkHeight = 50
        vectorDrawable4.setBounds(
            linkX.toInt(),
            linkY.toInt(),
            linkX.toInt() + linkWidth,
            linkY.toInt() + linkHeight
        )
        vectorDrawable4.draw(canvas)

        val linktextX = linkX + 70f // Jarak dari tepi kiri
        val linktextY = linkY + 40f // Jarak dari tepi bawah
        canvas.drawText("https://salwafaizahh.....", linktextX, linktextY, linkTextPaint)

        val calX = textX  // Menyesuaikan posisi X gambar vektor kedua
        val calY =
            locY + locHeight + 20f // Menempatkan gambar kedua di bawah gambar pertama dengan jarak 20 piksel
        val calWidth = 50
        val calHeight = 50
        vectorDrawable3.setBounds(
            calX.toInt(),
            calY.toInt(),
            calX.toInt() + calWidth,
            calY.toInt() + calHeight
        )
        vectorDrawable3.draw(canvas)

        val caltextX = calX + 70f // Jarak dari tepi kiri
        val caltextY = loctextY + 70f // Jarak dari tepi bawah
        canvas.drawText("Joined Sept, 2019", caltextX, caltextY, normalTextPaint)

        val jmlFollowingX = circleX - radius // Tambahkan 50 piksel di bawah lingkaran
        val jmlFollowingY = caltextY + 70f
        canvas.drawText("125", jmlFollowingX, jmlFollowingY, bioTextPaint)

        val FollowingX = jmlFollowingX + 90f // Tambahkan 50 piksel di bawah lingkaran
        val FollowingY = caltextY + 70f
        canvas.drawText("Following", FollowingX, FollowingY, normalTextPaint)

        val jmlFollowerX = FollowingX + 250f // Tambahkan 50 piksel di bawah lingkaran
        val jmlFollowerY = caltextY + 70f
        canvas.drawText("123", jmlFollowerX, jmlFollowerY, bioTextPaint)

        val FollowerX = jmlFollowerX + 90f // Tambahkan 50 piksel di bawah lingkaran
        val FollowerY = caltextY + 70f
        canvas.drawText("Followers", FollowerX, FollowerY, normalTextPaint)

        val btnEditX = width / 4f + 400f
        val btnEditY = height - radius - 1650f
        val btnEditWidth = 300f
        val btnEditHeight = 80f

// Mengatur batas tombol
        buttonRect.set(btnEditX, btnEditY, btnEditX + btnEditWidth, btnEditY + btnEditHeight)

// Menggambar border tombol
        paint.color = Color.LTGRAY // Warna border
        paint.style = Paint.Style.STROKE // Hanya menggambar garis luar
        paint.strokeWidth = 3f // Ketebalan border

        canvas.drawRoundRect(buttonRect, 50f, 50f, paint)

// Reset kembali ke default agar teks bisa digambar dengan benar
        paint.style = Paint.Style.FILL
        paint.color = Color.BLACK
        paint.textSize = 45f

        // Menggambar teks tombol
        paint.color = Color.BLACK
        paint.textSize = 40f
        val buttonText = "Edit Profile"
        val textWidth = paint.measureText(buttonText)
        canvas.drawText(
            buttonText,
            buttonRect.centerX() - textWidth / 2,
            buttonRect.centerY() + 15f,
            paint
        )

        // Hitung posisi FAB (pojok kanan bawah)
        fabX = width - fabRadius - 50
        fabY = height - fabRadius - 50


        fabButtonBounds.set(
            (fabX - fabRadius).toInt(),
            (fabY - fabRadius).toInt(),
            (fabX + fabRadius).toInt(),
            (fabY + fabRadius).toInt()
        )
// Gambar lingkaran FAB
        canvas.drawCircle(fabX, fabY, fabRadius, fabPaint)

// Gambar tanda "+"
        canvas.drawText("+", fabX, fabY + 30f, fabTextPaint)

        drawPostingan(canvas)  // Pastikan ini dipanggil di sini
        drawTabMenu(canvas)
    }

    private fun drawTabMenu(canvas: Canvas) {
        val tabHeight = 60f
        val tabStartY = height - 1800f + paint.measureText("iniSalwaa") + 460f

        paint.textSize = 40f
        val totalWidth = width.toFloat()
        val totalTabWidth = paint.measureText(tabTitles.joinToString("")) + (tabTitles.size * 80f)
        val startX = (totalWidth - totalTabWidth) / 2

        tabRects.clear()

        var currentX = startX
        tabTitles.forEachIndexed { index, title ->
            val textWidth = paint.measureText(title)
            val tabWidth = textWidth + 80f

            // Set typeface based on active state
            paint.typeface = if (index == selectedTabIndex) {
                Typeface.DEFAULT_BOLD // Bold for active tab
            } else {
                Typeface.DEFAULT // Normal for inactive tabs
            }

            paint.color = Color.BLACK
            canvas.drawText(
                title,
                currentX + 40f,
                tabStartY + tabHeight / 2 + 15f,
                paint
            )

            if (index == selectedTabIndex) {
                paint.color = Color.parseColor("#1DA1F2")
                paint.strokeWidth = 3f
                canvas.drawLine(
                    currentX + 20f,
                    tabStartY + tabHeight - 0f,
                    currentX + tabWidth - 20f,
                    tabStartY + tabHeight - 0f,
                    paint
                )
                paint.strokeWidth = 1f
            }

            tabRects.add(RectF(
                currentX,
                tabStartY,
                currentX + tabWidth,
                tabStartY + tabHeight
            ))

            currentX += tabWidth
        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val x = event.x.toInt()
            val y = event.y.toInt()

            when {
                verifBound.contains(x, y) -> {
                    context.startActivity(Intent(context, VerifiedActivity::class.java))
                    return true
                }
                fabButtonBounds.contains(x, y) -> {
                    showPopupDialog() // Ganti dari langsung startActivity jadi tampilkan popup
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }


    private fun showPopupDialog() {
        val editText = android.widget.EditText(context).apply {
            hint = "Tulis sesuatu di sini..."
            setPadding(20, 20, 20, 20)
        }

        val builder = android.app.AlertDialog.Builder(context)
        builder.setTitle("Apa yang sedang terjadi?")
        builder.setView(editText)

        builder.setPositiveButton("Post") { dialog, _ ->
            val inputText = editText.text.toString()
            if (inputText.isNotBlank()) {
                insertPostingan(
                    idPengguna,
                    inputText
                )  // Pakai fungsi lokal, bukan dari database object
                postinganList.add(0, inputText)
                (context as Activity).runOnUiThread {
                    invalidate()
                }
            } else {
                Toast.makeText(context, "Isi tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Batal") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

private fun drawPostingan(canvas: Canvas) {
    val startY = height - 1000f // mulai dari bawah header menu
    var currentY = startY

    val profileSize = (width * 0.1).toInt()
    val profileX = 70f

    for (post in postinganList) {
        // Gambar profil kecil
       vectorDrawable7.setBounds(
            profileX.toInt(),
            currentY.toInt(),
            (profileX + profileSize).toInt(),
            (currentY + profileSize.toFloat()).toInt()
        )
        vectorDrawable7.draw(canvas)

        // Nama dan username
        val nmaX = profileX + profileSize + 30f
        canvas.drawText("$name", nmaX, currentY + 40f, textPaint)
        canvas.drawText("@$username", nmaX + 120f, currentY + 40f, textPaint)

        // Postingan
        canvas.drawText(post, nmaX, currentY + 90f, normalTextPaint)

        currentY += 170f // Jarak antar postingan
    }
}
}
