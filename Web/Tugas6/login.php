<?php
session_start();
include_once "koneksi.php";

function login()
{
    if (empty($_POST['username']) || empty($_POST['password'])) {
        return;
    }

    $conn = koneksidb();
    $username = $_POST['username'];
    $password = $_POST['password'];

    $sql = "SELECT * FROM pengguna WHERE username = $1";
    pg_prepare($conn, "my_query", $sql);
    $result = pg_execute($conn, "my_query", array($username));

    if (!$result || pg_num_rows($result) <= 0) {
        echo "<script>alert('Gagal login: username atau password salah!'); window.location.href='login.php';</script>";
        pg_close($conn);
        return;
    }

    $row = pg_fetch_array($result);
    $password_db = $row["password"];

    if ($password_db == $password) {
        // Set session dan redirect ke main.php
        $_SESSION['username'] = $username;

        setcookie("ingat_aku", $username, time() + (60 * 60 * 24 * 30), "/");

        pg_close($conn);
        echo "<script>alert('Login berhasil!'); window.location.href='main.php';</script>";
        exit();
    } else {
        echo "<script>alert('Gagal login: username atau password salah!'); window.location.href='login.php';</script>";
        pg_close($conn);
    }
}

login();
?>


<!DOCTYPE html>
<html lang="id">
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
<head>
    <meta charset="UTF-8">
    <title>Login X</title>
    <link rel="stylesheet" href="login.css">
</head>
<body>
<div class="wrapper">
    <!-- Kolom Kiri -->
    <div class="left-side">
    <div class="headline">
        <h1>Sedang tren saat ini</h1>
        <p>Bergabunglah sekarang.</p>
    </div>
    </div>
    <!-- Kolom Kanan -->
    <div class="right-side">
        <div class="login-box">
            <!-- Logo X -->
            <img src="image/twitter.png" alt="Logo X" class="logo-login">
            
            <form method="POST" action="">
                <input type="text" name="username" placeholder="Nomor telepon, nama pengguna, atau email" required>
                <input type="password" name="password" placeholder="Kata Sandi" required>
                <button class="btn" type="submit">Masuk</button>
            </form>

            <!-- Garis pemisah dan "ATAU" -->
            <div class="separator">
                <hr><span>ATAU</span><hr>
            </div>

            <!-- Tombol masuk dengan email -->
            <div class="alt-login">
                <img src="image/gmail.png" alt="Email Icon">
                <a href="#">Masuk dengan Email</a>
            </div>

            <!-- Lupa kata sandi -->
            <div class="forgot-password">
                <a href="#">Lupa kata sandi?</a>
            </div>

        </div>

        <!-- Signup Box -->
        <div class="signup-box">
            <p>Belum punya akun? <a href="regis.php">Buat akun</a></p>
        </div>
    </div>
</div>
</body>
</html>