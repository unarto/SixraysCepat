# Laporan Folder Res yang "Hilang"

## Update Terbaru
Pengguna telah mengunggah file backup `res` secara utuh. File-file tersebut telah disalin dan direstorasi kembali ke dalam `app/src/main/res/`.

Dengan demikian:
1. Folder `drawable` dan `mipmap` (termasuk berbagai resolusi jadul hdpi, mdpi, xhdpi, xxhdpi, xxxhdpi) yang sebelumnya kosong kini **sudah berisi file aslinya**.
2. File bantuan `.gitkeep` yang sempat dibuat sebelumnya **telah dihapus**, karena folder-folder tersebut sudah terisi file `.png` (seperti `ic_launcher.png`, dll) sehingga Git otomatis akan mendeteksinya tanpa manipulasi `.gitkeep`.
3. Resource berhasil di-*merge* tanpa merusak (overwrite) desain XML modern (Vector Drawable) yang telah dibuat sebelumnya, sehingga "pertahanan UI" tetap terjaga sesuai instruksi.

## Masalah Duplikasi (Terselesaikan)
Saat melakukan sinkronisasi file PNG dari pengguna, terjadi bentrok dengan file XML (misalnya `nav_header_bg.png` dan `nav_header_bg.xml` yang memiliki nama sama di dalam direktori `drawable`). Sistem build AAPT2 memunculkan error duplikat. File PNG yang berkonflik telah dihapus dan memprioritaskan Vector Drawable XML terbaru.
