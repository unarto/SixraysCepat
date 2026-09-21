# Audit Report: `allowInsecure` Support in Codebase

## Latar Belakang
Pengguna berencana untuk melakukan downgrade versi Xray-core ke versi v26.1.23. Mulai versi 26.2.6, pengembang Xray telah menghapus dukungan `allowInsecure=true` untuk mencegah Man-in-The-Middle (MitM) attack yang bisa membocorkan data kepada ISP atau entitas lain (sesuai peringatan di `strings.xml` -> `toast_allow_insecure_deprecated`). Downgrade ke versi v26.1.23 mengizinkan fungsionalitas `allowInsecure` digunakan secara normal kembali.

## Status Dukungan `allowInsecure` di Aplikasi
Aplikasi secara natif **masih sepenuhnya** mendukung pembacaan, penyimpanan, pengeditan, dan injeksi parameter `allowInsecure` ke *StreamSettings* pada *outbound* konfigurasi.

## File Terkait yang Menangani `allowInsecure`
Berikut adalah file yang terlibat dalam siklus hidup `allowInsecure` pada project ini:

### 1. UI & State Management
- `app/src/main/res/values*/strings.xml`: Memuat teks label "allowInsecure" dan warning deprecation untuk versi terbaru.
- `app/src/main/java/com/sixray/cepat/ui/server/BaseServerActivity.kt`: Komponen UI Compose yang me-render *Switch* `allowInsecure` untuk diedit pengguna.
- `app/src/main/java/com/sixray/cepat/ui/server/ServerActivity.kt`: *Switch* untuk Custom & Protokol standar.
- `app/src/main/java/com/sixray/cepat/ui/server/ServerHysteria2Activity.kt`: *Switch* khusus untuk pengaturan Hysteria2.
- `app/src/main/java/com/sixray/cepat/ui/server/ServerUiState.kt`: Mengelola *mutable state* UI sebelum disimpan ke database.

### 2. Data Class & Link Parser (Import / Export)
- `app/src/main/java/com/sixray/cepat/dto/V2rayConfig.kt`: Menampung objek JSON internal untuk database. Parameter `allowInsecure` disimpan sebagai `Boolean`.
- `app/src/main/java/com/sixray/cepat/dto/V2rayNShareItem.kt`: Objek skema JSON untuk pembacaan format V2rayN standar.
- `app/src/main/java/com/sixray/cepat/fmt/FmtBase.kt`: *Parser* URI dan JSON yang mengekstrak nilai query seperti `?allowInsecure=1` atau `?insecure=1` dari link (contoh: `vmess://`).

### 3. Core Config Generator (Xray Backend)
- `app/src/main/java/com/sixray/cepat/core/CoreOutboundBuilder.kt`: File paling kritikal di mana `allowInsecure` disuntikkan ke dalam `OutboundBean.StreamSettingsBean.TlsSettingsBean`. 

## Dampak Downgrade
Jika Anda melakukan downgrade Xray-core ke versi v26.1.23, Anda **TIDAK PERLU** merubah komponen UI, Data Class, ataupun Parser, karena fungsionalitas `allowInsecure` masih tertanam utuh dan terhubung secara sempurna. Ketika Xray-core diganti dengan *binary* versi lama, aplikasi otomatis akan menggunakan fitur `allowInsecure` (karena *binary* v26.1.23 mengerti field JSON `allowInsecure: true` yang dikirim dari `CoreOutboundBuilder.kt`).

## Kesimpulan
Aplikasi sangat aman dan kompatibel untuk digunakan dengan Xray-core versi lama yang mendukung `allowInsecure`.

Status: **READY FOR CORE DOWNGRADE**.
