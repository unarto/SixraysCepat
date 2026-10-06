package com.sixray.cepat.dto

import com.google.gson.annotations.SerializedName

data class GitHubRelease(
    @SerializedName("tag_name")
    val tagName: String,
    @SerializedName("body")
    val body: String,
    @SerializedName("assets")
    val assets: List<Asset>,
    @SerializedName("prerelease")
    val prerelease: Boolean = false,
    @SerializedName("published_at")
    val publishedAt: String = "",
    // [Jalur Class/Modul]: app/src/main/java/com/sixray/cepat/dto/GitHubRelease.kt
    // [Penjelasan]: Menambahkan URL halaman rilis GitHub untuk fallback ketika rilis tidak memiliki APK langsung
    @SerializedName("html_url")
    val htmlUrl: String? = null
) {
    data class Asset(
        @SerializedName("name")
        val name: String,
        @SerializedName("browser_download_url")
        val browserDownloadUrl: String
    )
}