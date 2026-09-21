package com.sixray.cepat.fmt

import com.sixray.cepat.AppConfig
import com.sixray.cepat.dto.V2rayNShareItem
import com.sixray.cepat.dto.entities.ProfileItem
import com.sixray.cepat.util.JsonUtil
import com.sixray.cepat.util.LogUtil
import com.sixray.cepat.util.Utils

object V2rayNFmt : FmtBase() {
    fun parse(str: String): ProfileItem? {
        try {
            val jsonBase64Payload = str.substringAfterLast('/')
            val jsonPayload = Utils.decode(jsonBase64Payload)
            val v2rayNShareItem = JsonUtil.fromJson(jsonPayload, V2rayNShareItem::class.java)
            return v2rayNShareItem?.toProfileItem()
        } catch (e: Exception) {
            LogUtil.e(AppConfig.TAG, "Failed to parse V2rayN format", e)
        }
        return null
    }
}