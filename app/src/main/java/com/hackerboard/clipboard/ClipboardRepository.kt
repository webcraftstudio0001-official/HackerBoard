package com.hackerboard.clipboard
 
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
 
data class ClipEntry(
    val id: String = System.currentTimeMillis().toString(),
    val text: String,
    val pinned: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
)
 
class ClipboardRepository(context: Context) {
 
    private val prefs   = context.getSharedPreferences("clipboard", Context.MODE_PRIVATE)
    private val gson    = Gson()
    private val type    = object : TypeToken<MutableList<ClipEntry>>() {}.type
    private val maxSize = 50
 
    private var _entries: MutableList<ClipEntry> = load()
 
    val entries: List<ClipEntry> get() = _entries
        .sortedWith(compareByDescending<ClipEntry> { it.pinned }.thenByDescending { it.timestamp })
 
    fun push(text: String) {
        if (text.isBlank()) return
        _entries.removeAll { it.text == text && !it.pinned }
        _entries.add(ClipEntry(text = text))
        if (_entries.size > maxSize) {
            val unpinned = _entries.filter { !it.pinned }.sortedBy { it.timestamp }
            if (unpinned.isNotEmpty()) _entries.remove(unpinned.first())
        }
        save()
    }
 
    fun togglePin(id: String) {
        val idx = _entries.indexOfFirst { it.id == id }
        if (idx >= 0) {
            _entries[idx] = _entries[idx].copy(pinned = !_entries[idx].pinned)
            save()
        }
    }
 
    fun remove(id: String) {
        _entries.removeAll { it.id == id }
        save()
    }
 
    fun clear() {
        _entries.removeAll { !it.pinned }
        save()
    }
 
    private fun save() {
        prefs.edit().putString("data", gson.toJson(_entries)).apply()
    }
 
    private fun load(): MutableList<ClipEntry> {
        val json = prefs.getString("data", null) ?: return mutableListOf()
        return runCatching { gson.fromJson<MutableList<ClipEntry>>(json, type) }
            .getOrElse { mutableListOf() }
    }
}