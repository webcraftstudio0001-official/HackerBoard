package com.hackerboard
 
import android.app.Application
import com.hackerboard.clipboard.ClipboardRepository
import com.hackerboard.macros.MacroRepository
import com.hackerboard.snippets.SnippetRepository
import com.hackerboard.themes.ThemeManager
 
class HackerBoardApp : Application() {
 
    lateinit var clipboardRepo: ClipboardRepository
        private set
    lateinit var macroRepo: MacroRepository
        private set
    lateinit var snippetRepo: SnippetRepository
        private set
    lateinit var themeManager: ThemeManager
        private set
 
    override fun onCreate() {
        super.onCreate()
        instance        = this
        clipboardRepo   = ClipboardRepository(this)
        macroRepo       = MacroRepository(this)
        snippetRepo     = SnippetRepository(this)
        themeManager    = ThemeManager(this)
    }
 
    companion object {
        lateinit var instance: HackerBoardApp
            private set
    }
}