<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

$host = '172.22.160.1';
$port = '5432';
$dbname = 'twitter';
$user = 'postgres';
$password = 'kuanlinee';

function koneksidb() {
    global $host, $port, $dbname, $user, $password;
    $conn = pg_connect("host=$host port=$port dbname=$dbname user=$user password=$password");
    if (!$conn) {
        echo "Koneksi gagal.";
    } else {
        echo " ";
    }
    return $conn;
}
?>