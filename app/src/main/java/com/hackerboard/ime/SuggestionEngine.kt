package com.hackerboard.ime
 
import android.content.Context
import android.view.textservice.SentenceSuggestionsInfo
import android.view.textservice.SpellCheckerSession
import android.view.textservice.TextInfo
import android.view.textservice.TextServicesManager
 
class SuggestionEngine(
    private val context: Context,
    private val onSuggestions: (List<String>) -> Unit,
) : SpellCheckerSession.SpellCheckerSessionListener {
 
    private var session: SpellCheckerSession? = null
 
    private val devKeywords = listOf(
        "function", "return", "const", "let", "var", "import", "export",
        "class", "interface", "extends", "implements", "private", "public",
        "static", "void", "null", "true", "false", "async", "await",
        "override", "suspend", "kotlin", "android", "compose", "activity",
        "service", "intent", "context", "bundle", "fragment", "viewmodel",
        "coroutine", "flow", "channel", "launch", "collect", "mutableState",
        "remember", "LaunchedEffect", "Column", "Row", "Box", "Text",
        "Button", "Modifier", "padding", "fillMaxWidth", "fillMaxHeight",
    )
 
    fun init() {
        runCatching {
            val tsm = context.getSystemService(Context.TEXT_SERVICES_MANAGER_SERVICE)
                    as TextServicesManager
            session = tsm.newSpellCheckerSession(null, null, this, true)
        }
    }
 
    fun suggest(word: String) {
        if (word.length < 2) {
            onSuggestions(emptyList())
            return
        }
        val devMatches = devKeywords
            .filter { it.startsWith(word, ignoreCase = true) && it != word }
            .take(3)
        if (devMatches.isNotEmpty()) {
            onSuggestions(devMatches)
            return
        }
        session?.getSuggestions(TextInfo(word), 3)
    }
 
    override fun onGetSuggestions(results: Array<out android.view.textservice.SuggestionsInfo>?) {
        val words = results
            ?.flatMap { info ->
                (0 until info.suggestionsCount).map { info.getSuggestionAt(it) }
            }
            ?.take(3)
            ?: emptyList()
        onSuggestions(words)
    }
 
    override fun onGetSentenceSuggestions(results: Array<out SentenceSuggestionsInfo>?) {}
 
    fun destroy() {
        session?.close()
        session = null
    }
}