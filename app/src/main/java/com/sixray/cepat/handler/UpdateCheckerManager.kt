package com.sixray.cepat.handler

import android.os.Build
import com.sixray.cepat.AppConfig
import com.sixray.cepat.BuildConfig
import com.sixray.cepat.dto.CheckUpdateResult
import com.sixray.cepat.dto.GitHubRelease
import com.sixray.cepat.dto.UrlContentRequest
import com.sixray.cepat.extension.concatUrl
import com.sixray.cepat.util.HttpUtil
import com.sixray.cepat.util.JsonUtil
import com.sixray.cepat.util.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// [Jalur Class/Modul]: app/src/main/java/com/sixray/cepat/handler/UpdateCheckerManager.kt
// [Penjelasan]: Mengelola pengecekan pembaruan aplikasi dari rilis GitHub dengan dukungan rilis stabil, pre-release, perbandingan versi aman tanpa NumberFormatException, dan pemilihan download APK fleksibel
object UpdateCheckerManager {
    suspend fun checkForUpdate(includePreRelease: Boolean = false): CheckUpdateResult = withContext(Dispatchers.IO) {
        val url = AppConfig.APP_API_URL

        val proxyUsername = SettingsManager.getSocksUsername()
        val proxyPassword = SettingsManager.getSocksPassword()

        var response = HttpUtil.getUrlContent(
            UrlContentRequest(
                url = url,
                timeout = 10000
            )
        )
        if (response.isNullOrEmpty()) {
            val httpPort = SettingsManager.getHttpPort()
            if (httpPort != 0) {
                response = HttpUtil.getUrlContent(
                    UrlContentRequest(
                        url = url,
                        timeout = 10000,
                        httpPort = httpPort,
                        proxyUsername = proxyUsername,
                        proxyPassword = proxyPassword
                    )
                )
            }
        }

        if (response.isNullOrEmpty()) {
            throw IllegalStateException("Failed to get response")
        }

        val releases = JsonUtil.fromJsonSafe(response, Array<GitHubRelease>::class.java)
        if (releases.isNullOrEmpty()) {
            return@withContext CheckUpdateResult(hasUpdate = false)
        }

        val latestRelease = if (includePreRelease) {
            releases.firstOrNull()
        } else {
            releases.firstOrNull { !it.prerelease }
        }

        if (latestRelease == null) {
            return@withContext CheckUpdateResult(hasUpdate = false)
        }

        val latestVersion = latestRelease.tagName.trim().removePrefix("v").removePrefix("V")
        LogUtil.i(
            AppConfig.TAG,
            "Found release version: $latestVersion (current: ${BuildConfig.VERSION_NAME})"
        )

        return@withContext if (compareVersions(latestVersion, BuildConfig.VERSION_NAME) > 0) {
            val downloadUrl = getDownloadUrl(latestRelease, Build.SUPPORTED_ABIS.firstOrNull() ?: "arm64-v8a")
            CheckUpdateResult(
                hasUpdate = true,
                latestVersion = latestVersion,
                releaseNotes = latestRelease.body,
                downloadUrl = downloadUrl,
                isPreRelease = latestRelease.prerelease
            )
        } else {
            CheckUpdateResult(hasUpdate = false)
        }
    }

    private fun compareVersions(version1: String, version2: String): Int {
        val v1 = version1.trim().removePrefix("v").removePrefix("V").split(".")
        val v2 = version2.trim().removePrefix("v").removePrefix("V").split(".")

        for (i in 0 until maxOf(v1.size, v2.size)) {
            val part1 = if (i < v1.size) v1[i] else "0"
            val part2 = if (i < v2.size) v2[i] else "0"

            val num1 = part1.takeWhile { it.isDigit() }.toIntOrNull() ?: 0
            val num2 = part2.takeWhile { it.isDigit() }.toIntOrNull() ?: 0
            if (num1 != num2) return num1 - num2
        }
        return 0
    }

    private fun getDownloadUrl(release: GitHubRelease, abi: String): String {
        val fDroid = "fdroid"
        val isFdroid = BuildConfig.APPLICATION_ID.contains(fDroid, ignoreCase = true)

        val apkAssets = release.assets.filter { it.name.endsWith(".apk", ignoreCase = true) }

        // 1. Cocokkan berdasarkan ABI arsitektur perangkat
        val abiAssets = apkAssets.filter { it.name.contains(abi, ignoreCase = true) }
        val specificAsset = if (isFdroid) {
            abiAssets.firstOrNull { it.name.contains(fDroid, ignoreCase = true) }
        } else {
            abiAssets.firstOrNull { !it.name.contains(fDroid, ignoreCase = true) }
        } ?: abiAssets.firstOrNull()

        if (specificAsset != null) {
            return specificAsset.browserDownloadUrl
        }

        // 2. Cocokkan universal APK jika tidak ada ABI spesifik
        val universalAsset = apkAssets.firstOrNull {
            it.name.contains("universal", ignoreCase = true) || it.name.contains("all", ignoreCase = true)
        }
        if (universalAsset != null) {
            return universalAsset.browserDownloadUrl
        }

        // 3. Fallback ke APK apapun yang tersedia dalam rilis
        val fallbackApk = apkAssets.firstOrNull()
        if (fallbackApk != null) {
            return fallbackApk.browserDownloadUrl
        }

        // 4. Fallback ke URL halaman rilis GitHub
        return release.htmlUrl ?: AppConfig.APP_URL
    }
}
