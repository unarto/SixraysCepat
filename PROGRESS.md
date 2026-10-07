## Date
2026-10-06

## Task
Pemisahan APK per ABI (arm64-v8a, armeabi-v7a, x86, x86_64, dan universal) untuk build GitHub Actions

## Status
SELESAI

## Files Changed
- app/build.gradle.kts

## Summary
Mengaktifkan konfigurasi `splits.abi` pada `app/build.gradle.kts` agar menghasilkan build APK terpisah per arsitektur CPU (`arm64-v8a`, `armeabi-v7a`, `x86`, `x86_64`) serta universal APK untuk keperluan rilis build di GitHub Actions.

## Technical Details
- Menetapkan `isEnable = true` pada blok `android.splits.abi`.
- Memasukkan ABI target: `"armeabi-v7a"`, `"arm64-v8a"`, `"x86"`, `"x86_64"`.
- Mempertahankan `isUniversalApk = true` untuk menyediakan paket universal.
- Terverifikasi menghasilkan output APK terpisah:
  - `app-arm64-v8a-*.apk` (armv8)
  - `app-armeabi-v7a-*.apk` (armv7)
  - `app-x86-*.apk` (x86)
  - `app-x86_64-*.apk` (x64)
  - `app-universal-*.apk` (universal)
- Menyertakan komentar kepatuhan `[Jalur Class/Modul]` dan `[Penjelasan]`.

## Verification
- Kompilasi `compile_applet` -> PASS.

---

## Date
2026-10-06

## Task
Penerapan Kebijakan Privasi (Opsi 2) dengan dokumen PRIVACY.md di GitHub Sixray

## Status
SELESAI

## Files Changed
- PRIVACY.md
- app/src/main/java/com/sixray/cepat/AppConfig.kt

## Summary
Mengubah tautan menu Privacy Policy ke `https://github.com/unarto/Sixray/blob/main/PRIVACY.md` dan membuat dokumen kebijakan privasi `PRIVACY.md` yang ringkas, transparan, dan mudah dipahami oleh pengguna.

## Technical Details
- Membuat file `PRIVACY.md` dengan ringkasan dwibahasa (Bahasa Indonesia & English) yang mencakup No-Logs policy, fungsi Android VpnService, penyimpanan konfigurasi secara lokal, serta kanal kontak bantuan.
- Memperbarui `AppConfig.APP_PRIVACY_POLICY` menjadi `"$APP_URL/blob/main/PRIVACY.md"` (`https://github.com/unarto/Sixray/blob/main/PRIVACY.md`).
- Menu pada `AboutActivity.kt` tetap memanggil `R.string.title_privacy_policy` tanpa hardcoding string di UI.
- Menyertakan komentar kepatuhan `[Jalur Class/Modul]` dan `[Penjelasan]`.

## Verification
- Kompilasi `compile_applet` -> PASS.

---

## Date
2026-10-06

## Task
Pembaruan dan verifikasi menu Feedback pada halaman About ke https://github.com/unarto/Sixray/issues

## Status
SELESAI

## Files Changed
- app/src/main/java/com/sixray/cepat/AppConfig.kt
- app/src/main/java/com/sixray/cepat/ui/AboutActivity.kt

## Summary
Memastikan menu Feedback pada halaman About mengarahkan pengguna ke halaman issues resmi repositori `https://github.com/unarto/Sixray/issues` via `AppConfig.APP_ISSUES_URL`.

## Technical Details
- Memverifikasi `AppConfig.APP_ISSUES_URL` yang didefinisikan dari `"$APP_URL/issues"` sehingga menghasilkan `"https://github.com/unarto/Sixray/issues"`.
- Menu `SettingsMenuItem` untuk `R.string.title_pref_feedback` pada `AboutActivity.kt` membuka `AppConfig.APP_ISSUES_URL` melalui `Utils.openUri(...)`.
- Menyertakan komentar kepatuhan `[Jalur Class/Modul]` dan `[Penjelasan]`.

## Verification
- Kompilasi `compile_applet` -> PASS.

---

## Date
2026-10-06

## Task
Pembaruan aksi menu Open Source pada halaman About ke https://github.com/unarto/Sixray

## Status
SELESAI

## Files Changed
- app/src/main/java/com/sixray/cepat/ui/AboutActivity.kt

## Summary
Mengarahkan aksi klik menu "Open Source licenses" pada halaman About ke alamat repositori resmi `https://github.com/unarto/Sixray` (`AppConfig.APP_URL`).

## Technical Details
- Memperbarui event `onClick` pada item menu `title_oss_license` di `AboutActivity.kt` agar memanggil `Utils.openUri(context, AppConfig.APP_URL)`.
- Nilai URL berasal dari `AppConfig.APP_URL` (`"$GITHUB_URL/unarto/Sixray"`), sehingga tetap terpusat dan tanpa hardcoded URL di UI composable.
- Menyertakan komentar kepatuhan `[Jalur Class/Modul]` dan `[Penjelasan]`.

## Verification
- Kompilasi `compile_applet` -> PASS.

---

## Date
2026-10-06

## Task
Pembaruan tautan menu Telegram channel di halaman About ke https://t.me/sivpncepat

## Status
SELESAI

## Files Changed
- app/src/main/java/com/sixray/cepat/AppConfig.kt

## Summary
Memperbarui konstanta `TG_CHANNEL_URL` agar menu "Telegram channel" pada halaman About mengarahkan pengguna ke channel resmi `https://t.me/sivpncepat`.

## Technical Details
- Memperbarui `AppConfig.TG_CHANNEL_URL` menjadi `"https://t.me/sivpncepat"`.
- Menu pada `AboutActivity.kt` tetap memanggil `R.string.title_tg_channel` dan membuka `AppConfig.TG_CHANNEL_URL` via `Utils.openUri(...)`.
- Menyertakan komentar kepatuhan `[Jalur Class/Modul]` dan `[Penjelasan]`.

## Verification
- Kompilasi `compile_applet` -> PASS.

---

## Date
2026-10-06

## Task
Perbaikan sistem check for update dan penanganan rilis GitHub

## Status
SELESAI

## Files Changed
- app/src/main/java/com/sixray/cepat/handler/UpdateCheckerManager.kt
- app/src/main/java/com/sixray/cepat/dto/GitHubRelease.kt

## Summary
Memperbaiki seluruh bug pada alur pengecekan pembaruan aplikasi agar kompatibel dengan skema rilis GitHub, menangani rilis pre-release dan stabil dengan aman tanpa crash NumberFormatException, serta menambahkan fallback penentuan file APK download.

## Technical Details
- Mengarahkan request check update langsung ke `AppConfig.APP_API_URL` untuk menghindari HTTP 404 dari endpoint `/latest` ketika repositori hanya memiliki pre-release.
- Memperbaiki parsing tag versi pada `UpdateCheckerManager` dengan memotong prefix `v` dan `V` secara case-insensitive serta menggunakan `takeWhile { it.isDigit() }.toIntOrNull()` untuk mencegah `NumberFormatException`.
- Memperbaiki penentuan unduhan APK (`getDownloadUrl`) dengan multi-tier fallback: pencocokan arsitektur ABI -> Universal APK -> File APK pertama yang tersedia -> Fallback URL halaman rilis (`htmlUrl`).
- Menambahkan field `html_url` pada DTO `GitHubRelease.kt`.
- Menyertakan komentar kepatuhan `[Jalur Class/Modul]` dan `[Penjelasan]`.

## Verification
- Kompilasi `compile_applet` -> PASS.

---

## Date
2026-10-05

## Task
Penyesuaian nama menu promotion menjadi Buy VPN Premium

## Status
SELESAI

## Files Changed
- app/src/main/res/values/strings.xml

## Summary
Menyesuaikan string resource `title_pref_promotion` menjadi "Buy VPN Premium" agar konsisten dengan pengaturan bahasa default aplikasi (English).

## Technical Details
- Mengubah nilai string `title_pref_promotion` di `app/src/main/res/values/strings.xml` dari `"Beli VPN Premium"` menjadi `"Buy VPN Premium"`.
- Menu tetap terpanggil melalui referensi `R.string.title_pref_promotion` tanpa hardcoded string.
- Menyertakan komentar kepatuhan `[Jalur Class/Modul]` dan `[Penjelasan]`.

## Verification
- Kompilasi `compile_applet` -> PASS.

---

## Date
2026-10-05

## Task
Integrasi tautan bot Telegram @Sivpncepat_bot pada menu Beli VPN Premium

## Status
SELESAI

## Files Changed
- app/src/main/java/com/sixray/cepat/AppConfig.kt
- app/src/main/java/com/sixray/cepat/ui/main/MainActivity.kt
- app/src/main/java/com/sixray/cepat/ui/main/MainDrawer.kt

## Summary
Menghubungkan menu "Beli VPN Premium" agar membuka bot Telegram `@Sivpncepat_bot` (`https://t.me/Sivpncepat_bot`) dan memperbarui ikon menu menggunakan ikon Telegram.

## Technical Details
- Menambahkan konstanta `TG_BOT_URL = "https://t.me/Sivpncepat_bot"` pada `AppConfig.kt`.
- Menghubungkan handling navigasi menu `"promotion"` pada `MainActivity.kt` untuk membuka `AppConfig.TG_BOT_URL` melalui `Utils.openUri(...)`.
- Memperbarui icon item menu pada `MainDrawer.kt` menjadi `R.drawable.ic_telegram_24dp`.
- Menyertakan komentar kepatuhan `[Jalur Class/Modul]` dan `[Penjelasan]` pada setiap perubahan.

## Verification
- Kompilasi `compile_applet` -> PASS.

---

## Date
2026-10-05

## Task
Penggantian nama menu promotion menjadi Beli VPN Premium

## Status
SELESAI

## Files Changed
- app/src/main/res/values/strings.xml

## Summary
Mengubah nama/label menu "Promotion" pada side navigation drawer menjadi "Beli VPN Premium" melalui string resource `title_pref_promotion`.

## Technical Details
- Memperbarui string resource `title_pref_promotion` di `app/src/main/res/values/strings.xml` dari `"Promotion"` menjadi `"Beli VPN Premium"`.
- UI Drawer (`MainDrawer.kt`) memanggil resource `R.string.title_pref_promotion` sehingga tetap mematuhi prinsip anti-hardcoding.
- Tidak mengubah logika/arsitektur sebelum instruksi lanjutan dari pengguna.

## Verification
- Kompilasi `compile_applet` -> PASS.

---

## Date
2026-10-05

## Task
Penggantian server check for update ke repositori https://github.com/unarto/Sixray/releases

## Status
SELESAI

## Files Changed
- app/src/main/java/com/sixray/cepat/AppConfig.kt

## Summary
Mengubah endpoint GitHub release and update check dari repositori upstream lama ke `https://github.com/unarto/Sixray/releases` (`https://api.github.com/repos/unarto/Sixray/releases`).

## Technical Details
- Memperbarui `AppConfig.APP_URL` menjadi `"$GITHUB_URL/unarto/Sixray"`.
- Memperbarui `AppConfig.APP_API_URL` menjadi `"https://api.github.com/repos/unarto/Sixray/releases"`.
- Menyertakan komentar wajib `[Jalur Class/Modul]` dan `[Penjelasan]` pada blok kode yang diubah.
- Call chain `CheckUpdateViewModel` -> `UpdateCheckerManager` secara otomatis mengarahkan permintaan check release dan download rilis ke repositori `unarto/Sixray`.

## Verification
- Kompilasi `compile_applet` -> PASS.

---

## Date
2026-09-21

## Task
Pembaruan applicationId dari com.sixray.ibuwwi ke com.sixray.cepat

## Status
SELESAI

## Files Changed
- app/build.gradle.kts

## Summary
Memperbarui konfigurasi `applicationId` di `app/build.gradle.kts` dari `com.sixray.ibuwwi` menjadi `com.sixray.cepat` sesuai instruksi eksplisit pengguna.

## Technical Details
- Menyesuaikan `defaultConfig.applicationId` menjadi `"com.sixray.cepat"`.
- Menyertakan komentar penjelasan jalur modul dan alasan perubahan sesuai aturan kepatuhan.

## Verification
- Kompilasi proyek menggunakan `compile_applet` / Gradle build -> PASS.

---

## Date
2026-08-03

## Task
Analisis efek implementasi DNS default Adguard dan mapping DNS Hosts

## Files Changed
- None

## Summary
Melakukan analisis terhadap nilai default DNS Adguard (`DNS_PROXY`, `DNS_DIRECT`, `DNS_VPN`) dan mapping `PREF_DNS_HOSTS` (`dns.adguard-dns.com:94.140.14.14`) di `AppConfig.kt` beserta efek integrasinya ke konfigurasi Xray-core melalui `CoreConfigManager.kt`.

## Technical Details
- `AppConfig.DNS_PROXY` menggunakan DoH `https://dns.adguard-dns.com/dns-query`.
- `AppConfig.DNS_HOSTS_DEFAULT` diset ke `dns.adguard-dns.com:94.140.14.14`.
- Core akan memetakan domain DoH Adguard langsung ke IP `94.140.14.14` di dalam modul DNS internal Xray, menyelesaikan masalah bootstrapping DNS.

## Impact
- Ad-blocking bawaan karena traffic direvolve oleh Adguard.
- Mencegah DNS leak/poisoning dari ISP karena server DoH sudah di-bootstrap secara statis.
- Resolusi DNS awal (handshake) lebih cepat.

## Verification
Dianalisis melalui pipeline `CoreConfigManager.kt` -> `v2rayConfig.dns.hosts`.

## Next Step
Menunggu tanggapan pengguna terkait laporan implementasi DNS Adguard.
