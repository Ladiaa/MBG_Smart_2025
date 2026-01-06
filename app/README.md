# MBGSmart_2025

## Deskripsi Aplikasi
MBGSmart adalah aplikasi berbasis Android yang dirancang untuk mendukung
monitoring dan evaluasi Program Menu Bergizi (MBG) di sekolah.
Aplikasi ini memungkinkan sekolah mengunggah menu harian, murid
melihat serta melaporkan menu, dan admin memantau keseluruhan proses.

Aplikasi dikembangkan menggunakan arsitektur **MVVM (Model-View-ViewModel)** **Gamifikasi**
dan menerapkan konsep **CRUD (Create, Read, Update, Delete)**.

---

## Role Pengguna
Aplikasi MBGSmart memiliki 3 peran utama:

### 1. Admin
- Monitoring data sekolah
- Monitoring menu yang diunggah sekolah
- Monitoring dan validasi laporan murid
- Manajemen data sekolah
- Manajemen data catering

### 2. Sekolah
- Login dan pengisian identitas sekolah
- Upload menu harian
- Melihat riwayat menu
- Melihat leaderboard sekolah
- Mengelola profil

### 3. Murid
- Login dan register
- Melihat menu dari sekolah asal
- Melaporkan menu
- Melihat riwayat laporan
- Melihat detail laporan
- Melihat leaderboard
- Mengelola profil

---

## Teknologi yang Digunakan
- Kotlin
- Android Jetpack Compose
- Firebase Authentication
- Firebase Firestore
- MVVM Architecture
- Material Design 3

---

## Struktur Database (Firebase Firestore)
- users_sekolah
- menus
- reports
- caterings

---

## Cara Menjalankan Aplikasi
1. Clone atau download project
2. Buka project di Android Studio
3. Pastikan file `google-services.json` berada di folder `app/`
4. Sync Gradle
5. Jalankan aplikasi menggunakan emulator atau perangkat fisik

---
## Desain Relasi Antarmuka (Penjelasan Alur Lengkap)

Desain relasi antarmuka pada aplikasi MBGSmart menggambarkan alur perpindahan antar screen atau 
aktivitas secara berurutan sesuai dengan peran pengguna. Alur dimulai dari halaman awal 
(Select Role Screen), di mana pengguna memilih peran yang akan digunakan, 
yaitu Admin, Sekolah, atau Murid. Pemilihan peran ini menentukan jalur antarmuka dan fitur yang 
dapat diakses oleh pengguna.

Setelah memilih peran, pengguna diarahkan ke halaman Login sesuai dengan peran yang dipilih. 
Sistem melakukan proses autentikasi untuk memastikan bahwa pengguna memiliki hak akses yang sesuai. 
Jika proses login berhasil, aplikasi akan mengarahkan pengguna ke halaman berikutnya berdasarkan 
status dan perannya.

- Pada peran Sekolah, setelah login berhasil, sistem terlebih dahulu melakukan pengecekan apakah data 
identitas sekolah sudah lengkap. Jika identitas belum diisi, pengguna diarahkan ke halaman 
Pengisian Identitas Sekolah. Setelah identitas tersimpan, sekolah dapat mengakses halaman Upload 
Menu Harian untuk memasukkan data menu makanan. Dari halaman ini, sekolah dapat berpindah ke 
halaman analisis selanjutnya halaman mengarah ke halaman Riwayat Menu untuk melihat daftar 
menu yang telah diunggah sebelumnya, kemudian ke halaman Leaderboard untuk melihat peringkat 
sekolah, serta ke halaman Profil untuk mengelola akun atau 
keluar dari aplikasi.

- Pada peran Murid, setelah login atau registrasi berhasil, pengguna diarahkan ke halaman Home yang 
menampilkan daftar menu dari sekolah asal murid tersebut. Dari halaman Home, murid dapat melihat daftar 
menu tertentu dan dihalaman ini murid bisa melaporkan menu jika tidak sesuai 
dan berpindah ke halaman Pelaporan Menu untuk mengisi laporan apabila menu 
tidak sesuai. Setelah laporan dikirim, murid dapat melihat hasilnya melalui halaman Riwayat 
Laporan, serta mengakses halaman Detail Laporan untuk melihat status laporan secara rinci. 
Selain itu, murid juga dapat berpindah ke halaman Leaderboard dan halaman Profil melalui 
navigasi yang tersedia.

- Pada peran Admin, setelah login berhasil, pengguna diarahkan ke halaman Dashboard Admin yang 
menampilkan ringkasan data sistem. Dari dashboard ini, admin dapat berpindah ke halaman Monitoring 
Menu untuk melihat seluruh menu yang diunggah oleh sekolah, kemudian ke halaman Monitoring Laporan 
untuk melakukan validasi laporan yang dikirim oleh murid. Admin juga dapat mengakses halaman 
Manajemen Sekolah dan Manajemen Catering untuk menambah atau mengelola data pendukung sistem.

Dengan desain relasi antarmuka tersebut, setiap screen atau aktivitas saling terhubung secara 
logis dan berurutan. Alur navigasi dirancang agar pengguna dapat berpindah antar screen dengan 
jelas sesuai perannya, sehingga penggunaan aplikasi menjadi terstruktur, mudah dipahami, dan 
mendukung proses monitoring Program Menu Bergizi secara efektif.

