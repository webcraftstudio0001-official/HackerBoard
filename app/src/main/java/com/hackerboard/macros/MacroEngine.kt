package com.hackerboard.macros
 
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
 
data class Macro(
    val id: String,
    val name: String,
    val trigger: String,        // e.g. ":fn"
    val expansion: String,      // text to type out
    val createdAt: Long = System.currentTimeMillis(),
)
 
class MacroRepository(context: Context) {
 
    private val prefs = context.getSharedPreferences("macros", Context.MODE_PRIVATE)
    private val gson  = Gson()
    private val type  = object : TypeToken<MutableList<Macro>>() {}.type
 
    private var _macros: MutableList<Macro> = load()
 
    val macros: List<Macro> get() = _macros.toList()
 
    fun add(macro: Macro) {
        _macros.removeAll { it.trigger == macro.trigger }
        _macros.add(macro)
        save()
    }
 
    fun remove(id: String) {
        _macros.removeAll { it.id == id }
        save()
    }
 
    fun findByTrigger(text: String): Macro? =
        _macros.firstOrNull { text.endsWith(it.trigger) }
 
    private fun save() {
        prefs.edit().putString("data", gson.toJson(_macros)).apply()
    }
 
    private fun load(): MutableList<Macro> {
        val json = prefs.getString("data", null) ?: return defaultMacros()
        return runCatching { gson.fromJson<MutableList<Macro>>(json, type) }
            .getOrElse { defaultMacros() }
    }
 
    private fun defaultMacros(): MutableList<Macro> = mutableListOf(
        Macro("1", "Function",     ":fn",   "function() {\n  \n}"),
        Macro("2", "Arrow Fn",     ":af",   "() => {\n  \n}"),
        Macro("3", "Console Log",  ":cl",   "console.log()"),
        Macro("4", "Try/Catch",    ":tc",   "try {\n  \n} catch (e) {\n  \n}"),
        Macro("5", "Shebang",      ":sh",   "#!/usr/bin/env bash\n"),
        Macro("6", "Main fn",      ":mn",   "fun main() {\n    \n}"),
        Macro("7", "IP Localhost", ":ip",   "127.0.0.1"),
        Macro("8", "SSH Template", ":ssh",  "ssh user@host -p 22"),
    )
}