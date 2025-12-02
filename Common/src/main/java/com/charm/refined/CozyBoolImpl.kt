package com.charm.refined

import com.charm.refined.appc.CharmCenter
import com.charm.refined.tools.ToolsStr
import kotlin.reflect.KProperty

/**
 * Date：2025/12/1
 * Describe:
 */
class CozyBoolImpl(val keyStr: String = "") {
    private val editor get() = CharmCenter.mSpHelper.edit()
    private var nameStr = keyStr
    private fun fetchKeyName(string: String): String {
        if (nameStr.isEmpty()) {
            nameStr = "cozy_" + ToolsStr.strToBase("bo_$string")
        }
        return nameStr
    }

    operator fun getValue(me: Any?, p: KProperty<*>): Boolean {
        return CharmCenter.mSpHelper.getBoolean(fetchKeyName(p.name), false)
    }

    operator fun setValue(me: Any?, p: KProperty<*>, value: Boolean) {
        editor.putBoolean(fetchKeyName(p.name), value).apply()
    }
}