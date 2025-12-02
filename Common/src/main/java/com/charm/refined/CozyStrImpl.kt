package com.charm.refined

import com.charm.refined.appc.CharmCenter
import com.charm.refined.tools.ToolsStr
import kotlin.reflect.KProperty

/**
 * Date：2025/12/1
 * Describe:
 */
class CozyStrImpl(val keyStr: String = "") {
    private val editor get() = CharmCenter.mSpHelper.edit()
    private var nameStr = keyStr
    private fun fetchKeyName(string: String): String {
        if (nameStr.isEmpty()) {
            nameStr = "cozy_" + ToolsStr.strToBase("c_$string")
        }
        return nameStr
    }

    operator fun getValue(me: Any?, p: KProperty<*>): String {
        return CharmCenter.mSpHelper.getString(fetchKeyName(p.name), "") ?: ""
    }

    operator fun setValue(me: Any?, p: KProperty<*>, value: String) {
        editor.putString(fetchKeyName(p.name), value).apply()
    }
}