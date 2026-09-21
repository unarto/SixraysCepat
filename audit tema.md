# Audit Tema Terang (Light Theme) dan Tumpang Tindih (Overlap) UI

## Masalah yang Teridentifikasi (Berdasarkan Screenshot)
Terdapat masalah **tumpang tindih (overlap) teks dan UI** pada beberapa komponen ketika menggunakan Tema Terang (Light Mode), yaitu pada:
1. Dialog Pengaturan (Language dan UI mode settings) yang menembus layar Settings di belakangnya.
2. Menu navigasi samping (Drawer) yang tembus pandang ke layar utama.
3. Dropdown Menu (titik tiga) yang teksnya bertabrakan dengan layar konfigurasi.

## Analisa Penyebab
Akar permasalahan berasal dari definisi `LightColor` di file `app/src/main/java/com/sixray/cepat/ui/compose/Theme.kt`. 
```kotlin
private val LightColor = lightColorScheme(
    ...
    background = Color.Transparent, 
    surface = Color.Transparent, 
    ...
)
```
Di Jetpack Compose (Material 3), komponen melayang (elevated) seperti Dialog, DropdownMenu, dan Drawer secara default mengambil warna background dari properti `surface` atau `surfaceContainer`. Karena nilainya di-set `Color.Transparent`, latar komponen-komponen tersebut menjadi tembus pandang (transparan), sehingga teksnya bertabrakan (overlap) dengan layer yang ada di belakangnya.

## Rencana Implementasi (Pure Compose)
Berdasarkan request Anda untuk mengubah background dengan gradasi (Putih ke Biru):
1. **Tidak akan menggunakan XML Shape Drawable**. Seperti yang Anda sampaikan, karena UI dibangun menggunakan Jetpack Compose, maka penggunaan XML `.xml` adalah hal yang salah dan berisiko force close karena ketidaksesuaian rendering.
2. **Implementasi Gradasi di Compose**. Gradasi yang Anda minta (Putih ke Biru: `#FFFFFF` ke `#87CEEB` dengan angle 270 / Top to Bottom) akan diimplementasikan sepenuhnya via Compose Modifier `Brush.verticalGradient`.

**Langkah perbaikan (Belum diimplementasikan ke code, menunggu persetujuan Anda):**

**1. Perbaikan Overlap (di `Theme.kt`):**
Mengubah `surface` menjadi warna Putih solid, agar Dialog/Menu tidak lagi tembus pandang.
```kotlin
private val LightColor = lightColorScheme(
    // ...
    background = Color.Transparent, 
    onBackground = Color(0xFF1C1B1F),
    surface = Color(0xFFFFFFFF), // <-- Mengubah Transparent menjadi solid White
    onSurface = Color(0xFF1C1B1F),
    // ...
)
```
*(Catatan: Tema Gelap / DarkTheme tidak akan terdampak karena `DarkColor` menggunakan `surface = Color(0xFF1C1B1F)` yang sudah solid).*

**2. Implementasi Background Gradasi Tema Terang (di `Theme.kt`):**
Pada bagian modifier background di dalam `AngTheme` (sekitar baris 171), kita modifikasi agar merender Brush Gradient khusus untuk `!darkTheme` (tema terang).

```kotlin
val backgroundModifier = if (!darkTheme) {
    Modifier.background(
        brush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFFFFFFFF), // startColor
                Color(0xFFFFFFFF), // centerColor
                Color(0xFF87CEEB)  // endColor (Biru Langit)
            )
        )
    )
} else {
    // Mempertahankan background tema gelap yang sudah ada
    Modifier.background(colorScheme.background)
}
```

## Kesimpulan
Pendekatan ini 100% menggunakan arsitektur Jetpack Compose tanpa mencampuradukkan resource XML lama, sehingga sangat aman dari force close dan mengikuti best practice modern Android.

Menunggu persetujuan Anda untuk menerapkan perubahan ini ke dalam codebase.
