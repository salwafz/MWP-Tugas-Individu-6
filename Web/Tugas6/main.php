<?php
session_start();
if (!isset($_SESSION['username'])) {
    header("Location: login.php");
    exit();
}

include_once "koneksi.php"; // koneksi ke database
$conn = koneksidb(); // pastikan fungsi ini terdefinisi

$username = $_SESSION['username'];

// Ambil data pengguna dari tabel 'pengguna'
$sql = "SELECT * FROM pengguna WHERE username = $1";
pg_prepare($conn, "get_user", $sql);
$result = pg_execute($conn, "get_user", array($username));

if ($row = pg_fetch_assoc($result)) {
    $namaLengkap = $row['name'];       // pastikan kolom ini ada
    $usernameFix = $row['username'];   // pastikan kolom ini ada
    $bio = $row['bio'];                // opsional
    $fotoProfil = $row['foto_profil'] ?? 'image/ppcat.jpg'; // fallback default
    $cover = $row['cover'] ?? 'image/header.jpg';
    // Tambahkan field lain sesuai kebutuhan
} else {
    // Jika user tidak ditemukan
    echo "Pengguna tidak ditemukan.";
    exit();
}
?>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">  <!-- Menentukan encoding karakter -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0">  <!-- Responsif untuk perangkat mobile -->
    <meta name="author" content="SALWA">  <!-- Menentukan penulis halaman -->
    <title>Twitter Profile UI</title>
    <link rel="stylesheet" href="main.css">
</head>
<body>
    <div class="wrapper">
        <div class="container">
        <img src="image/twitter.png" alt="X">
        <img src="image/home.png" alt="Home">
        <img src="image/search.png" alt="Search">
        <img src="image/notif.png" alt="Notifications">
        <img src="image/grok.png" alt="Grok">
        <img src="image/group.png" alt="Community">
        <img src="image/user.png" alt="User">
        <img src="image/more.png" alt="More">
        <button class="postBTN" id="postingButton">Post</button>
            <p class="name"><?php echo htmlspecialchars($namaLengkap); ?></p>
            <p class="username">@<?php echo htmlspecialchars($usernameFix); ?></p>
                <div class="roundedcircle">
                    <img id="pfp" src="image/ppcat.jpg">
                </div>
        </div>
    
        <div class="main-content">
            <div class="header">
                <div class="profile-header">
                    <span class="back-btn">← </span>
                    <div class="profile-name">
                    <?php echo htmlspecialchars($namaLengkap ?? 'name'); ?>
                    </div>
                </div>
                <img src="image/header.jpg" class="cover" alt="Cover Image">
                <img src="image/ppcat.jpg" class="profile-pic" alt="Profile Picture">
            </div>            
            <div class="profile-info">
                <div class="profile-details">
                    <h2 class="name"><?php echo htmlspecialchars($namaLengkap); ?></h2>
                    <p class="username">@<?php echo htmlspecialchars($usernameFix); ?></p>
                    <p class="bio"><?php echo htmlspecialchars($bio ?? ''); ?></p>
                    <p class="details">⚧ she/her 20 • 📅 Joined September 2017</p>
                    <p class="stats"><strong>46</strong> Following • <strong>120</strong> Followers</p>
                </div>
                <button class="edit-profile">Edit Profile</button>
            </div>

            <div class="tabs">
                <span class="active">Posts</span>
                <span>Replies</span>
                <span>Media</span>
                <span>Likes</span>
            </div>

            <div class="posts post-container">
                <div class="post pinned">
                    <img src="image/ppcat.jpg" class="post-pic">
                    <div class="post-content">
                    <p><strong><?php echo htmlspecialchars($namaLengkap); ?></strong> @<?php echo htmlspecialchars($usernameFix); ?> • Sept 23, 2020</p>
                    <p>Hello WOrld!</p>
                    </div>
                </div>

                <div class="post pinned">
                    <img src="image/ppcat.jpg" class="post-pic">
                    <div class="post-content">
                    <p><strong><?php echo htmlspecialchars($namaLengkap); ?></strong> @<?php echo htmlspecialchars($usernameFix); ?> • Sept 23, 2020</p>
                    <p>Hello WOrld!</p>
                    </div>
                </div>

                <div class="post pinned">
                    <img src="image/ppcat.jpg" class="post-pic">
                    <div class="post-content"> 
                    <p><strong><?php echo htmlspecialchars($namaLengkap); ?></strong> @<?php echo htmlspecialchars($usernameFix); ?> • Sept 23, 2020</p>
                    <p>Hello WOrld!</p>
                    </div>
                </div>
            </div>
        </div>
</div>

        <!-- Right Sidebar -->
    <div class="right-sidebar">
            <div class="search-bar">
                <input type="text" placeholder="Search" />
            </div>
            <div class="suggestions">
                <h3>You might like</h3>
                <div class="suggestion">
                    <img src="image/pp1.jpg" class="suggestion-pic" alt="Elon Musk">
                    <div class="suggestion-info">
                        <strong>pinguin panas</strong>
                        <p>@pinkUIN</p>
                    </div>
                    <button class="follow-btn">Follow</button>
                </div>
                <div class="suggestion">
                    <img src="image/pp2.jpg" class="suggestion-pic" alt="Donald Trump">
                    <div class="suggestion-info">
                        <strong>Nana Study</strong>
                        <p>@cumakamu</p>
                    </div>
                    <button class="follow-btn">Follow</button>
                </div>
                <div class="suggestion">
                    <img src="image/pp3.jpg" class="suggestion-pic" alt="NFL">
                    <div class="suggestion-info">
                        <strong>Open PO</strong>
                        <p>@moneymoney</p>
                    </div>
                    <button class="follow-btn">Follow</button>
                </div>
                <a href="#" class="show-more">Show more</a>
            </div>
                <div class="trending">
                    <h3>What’s happening</h3>
                    
                    <div class="trending-item">
                        <small>Trending in Indonesia</small>
                        <p>Efisiensi Anggaran</p>
                        <span>1M posts</span>
                    </div>
                
                    <div class="trending-item">
                        <small>Music • Trending</small>
                        <p>#Jennie</p>
                        <span>2M posts</span>
                    </div>
                
                    <div class="trending-item">
                        <small>Trending</small>
                        <p>samsung</p>
                        <span>10K posts</span>
                    </div>
                
                    <div class="trending-item">
                        <small>Trending in Indonesia</small>
                        <p>jualan</p>
                        <span>34.7K posts</span>
                    </div>
                </div>                
    </div>
 
<!-- Toast Notification -->
<!-- <div id="toast" class="toast">Post berhasil di-upload!</div> -->
<div class="posting-modal" id="postingModal">
        <div class="modal-content">
            <div class="modal-header">
                <button class="close-btn" id="closeModal">&times;</button>
            </div>
            <div class="modal-body">
                <!-- <div class="audience-selector" id="audienceSelector">Semua orang</div> -->
                <textarea class="post-input" placeholder="Apa yang sedang terjadi?" id="postContent"></textarea>
                <!-- <div class="audience-selector">Semua orang dapat membalas</div> -->
            </div>
            <div class="modal-footer">
                <button class="post-submit" id="submitPost" disabled>Posting</button>
            </div>
        </div>
    </div>
    <script>
    document.addEventListener('DOMContentLoaded', function() {
    const postingButton = document.getElementById('postingButton');
    const postingModal = document.getElementById('postingModal');
    const closeModal = document.getElementById('closeModal');
    const postContent = document.getElementById('postContent');
    const submitPost = document.getElementById('submitPost');

    // Buka modal
    postingButton.addEventListener('click', function() {
        postingModal.style.display = 'flex';
        postContent.focus();
    });

    // Tutup modal
    closeModal.addEventListener('click', function() {
        postingModal.style.display = 'none';
    });

    // Tutup modal saat klik di luar
    postingModal.addEventListener('click', function(e) {
        if (e.target === postingModal) {
            postingModal.style.display = 'none';
        }
    });

    // Aktifkan tombol Posting jika ada konten
    postContent.addEventListener('input', function() {
        submitPost.disabled = postContent.value.trim() === '';
    });

    // Submit postingan
    submitPost.addEventListener('click', function() {
        const content = postContent.value;

        // Mengirim data ke server menggunakan fetch
        const data = new FormData();
        data.append('content', content);

        fetch('/create_post', {
            method: 'POST',
            body: data,
        })
        .then(response => {
            if (response.ok) {
                alert('Postingan berhasil dibuat: ' + content);
                postContent.value = ''; // Kosongkan textarea
                postingModal.style.display = 'none'; // Tutup modal
                submitPost.disabled = true; // Nonaktifkan tombol
                window.location.reload(); // Refresh untuk melihat posting terbaru
            } else {
                alert('Gagal membuat postingan. Coba lagi.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Gagal membuat postingan. Coba lagi.');
        });
    });
});
</script>
</body>
</html>
