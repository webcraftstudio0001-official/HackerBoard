package com.hackerboard.ime
 
import android.inputmethodservice.InputMethodService
import android.os.Build
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.hackerboard.HackerBoardApp
import com.hackerboard.keyboard.KeyboardState
import com.hackerboard.keyboard.LayoutId
import com.hackerboard.keyboard.PanelMode
import com.hackerboard.themes.ThemeId
import com.hackerboard.themes.ThemeManager
import com.hackerboard.ui.KeyboardRootUI
import com.hackerboard.voice.VoiceInputManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
 
class HackerBoardService : InputMethodService(),
    LifecycleOwner, ViewModelStoreOwner, SavedStateRegistryOwner {
 
    // ── Lifecycle boilerplate for ComposeView inside a Service ────────
    private val lifecycleRegistry        = LifecycleRegistry(this)
    private val vmStore                  = ViewModelStore()
    private val savedStateController     = SavedStateRegistryController.create(this)
    override val lifecycle: Lifecycle    get() = lifecycleRegistry
    override val viewModelStore          get() = vmStore
    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateController.savedStateRegistry
 
    // ── App-level repos ───────────────────────────────────────────────
    private val app       get() = application as HackerBoardApp
    private val themeMan  get() = app.themeManager
    private val clipRepo  get() = app.clipboardRepo
    private val macroRepo get() = app.macroRepo
    private val snippRepo get() = app.snippetRepo
 
    // ── Service-level state ───────────────────────────────────────────
    val kbState = KeyboardState()
    private var currentThemeId  by mutableStateOf(ThemeId.HACKER)
    private var showFKeys        by mutableStateOf(true)
    private var showSymbols      by mutableStateOf(true)
    private var showNumbers      by mutableStateOf(true)
    private var vibrationEnabled by mutableStateOf(true)
 
    private val serviceScope  = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private lateinit var haptic: HapticManager
    private lateinit var voice: VoiceInputManager
    private lateinit var suggest: SuggestionEngine
 
    // ── Lifecycle ─────────────────────────────────────────────────────
    override fun onCreate() {
        super.onCreate()
        savedStateController.performAttach()
        savedStateController.performRestore(null)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
 
        haptic  = HapticManager(this)
        voice   = VoiceInputManager(this)
        suggest = SuggestionEngine(this) { words ->
            kbState.suggestions = words
        }
        suggest.init()
        observePrefs()
    }
 
    override fun onCreateInputView(): View {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
 
        return ComposeView(this).apply {
            setViewTreeLifecycleOwner(this@HackerBoardService)
            setViewTreeViewModelStoreOwner(this@HackerBoardService)
            setViewTreeSavedStateRegistryOwner(this@HackerBoardService)
            setContent {
                KeyboardRootUI(
                    service       = this@HackerBoardService,
                    themeId       = currentThemeId,
                    kbState       = kbState,
                    showFKeys     = showFKeys,
                    showSymbols   = showSymbols,
                    showNumbers   = showNumbers,
                    clipEntries   = clipRepo.entries,
                    macros        = macroRepo.macros,
                    snippets      = snippRepo.snippets,
                    onKeyPressed  = ::handleKey,
                    onSuggestion  = ::commitSuggestion,
                    onClipPaste   = ::pasteClip,
                    onMacroApply  = ::applyMacro,
                    onSnippetApply = ::applySnippet,
                    onVoiceStart  = ::startVoice,
                    onHackerTool  = ::applyHackerTool,
                )
            }
        }
    }
 
    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        kbState.shiftOn  = false
        kbState.capsLock = false
        kbState.ctrlHeld = false
        kbState.altHeld  = false
        kbState.panelMode = PanelMode.NONE
    }
 
    override fun onFinishInput() {
        super.onFinishInput()
        voice.stop()
    }
 
    override fun onDestroy() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        serviceScope.cancel()
        suggest.destroy()
        voice.destroy()
        super.onDestroy()
    }
 
    // ── Prefs observer ────────────────────────────────────────────────
    private fun observePrefs() {
        serviceScope.launch {
            themeMan.themeFlow.collect        { currentThemeId    = it.id }
        }
        serviceScope.launch {
            themeMan.showFKeysFlow.collect    { showFKeys         = it }
        }
        serviceScope.launch {
            themeMan.showSymbolsFlow.collect  { showSymbols       = it }
        }
        serviceScope.launch {
            themeMan.showNumbersFlow.collect  { showNumbers       = it }
        }
        serviceScope.launch {
            themeMan.vibrationFlow.collect    { vibrationEnabled  = it }
        }
        serviceScope.launch {
            themeMan.layoutFlow.collect       { kbState.layout    = it }
        }
    }
 
    // ── Key handling ─────────────────────────────────────────────────
    fun handleKey(keyLabel: String, keyType: com.hackerboard.keyboard.KeyType) {
        if (vibrationEnabled) haptic.keyClick()
        val ic = currentInputConnection ?: return
 
        when (keyType) {
            com.hackerboard.keyboard.KeyType.CHARACTER -> {
                val ch = if (kbState.isUpperCase()) keyLabel.uppercase() else keyLabel.lowercase()
                // Check macro trigger
                val before = ic.getTextBeforeCursor(32, 0)?.toString() ?: ""
                val candidate = before + ch
                val macro = macroRepo.findByTrigger(candidate)
                if (macro != null) {
                    val trigger = macro.trigger
                    repeat(trigger.length) { ic.deleteSurroundingText(1, 0) }
                    ic.commitText(macro.expansion, 1)
                } else {
                    ic.commitText(ch, 1)
                }
                kbState.onCharCommitted()
                updateSuggestions(ic)
            }
            com.hackerboard.keyboard.KeyType.BACKSPACE -> {
                ic.deleteSurroundingText(1, 0)
                updateSuggestions(ic)
            }
            com.hackerboard.keyboard.KeyType.ENTER -> {
                val action = currentInputEditorInfo?.imeOptions
                    ?.and(EditorInfo.IME_MASK_ACTION) ?: EditorInfo.IME_ACTION_NONE
                if (action != EditorInfo.IME_ACTION_NONE && action != EditorInfo.IME_ACTION_UNSPECIFIED) {
                    ic.performEditorAction(action)
                } else {
                    ic.commitText("\n", 1)
                }
            }
            com.hackerboard.keyboard.KeyType.SHIFT -> kbState.toggleShift()
            com.hackerboard.keyboard.KeyType.SPACE -> {
                ic.commitText(" ", 1)
                kbState.onCharCommitted()
            }
            com.hackerboard.keyboard.KeyType.TAB ->
                ic.commitText("\t", 1)
            com.hackerboard.keyboard.KeyType.ESC ->
                sendDownUpKeyEvents(KeyEvent.KEYCODE_ESCAPE)
            com.hackerboard.keyboard.KeyType.CTRL ->
                kbState.ctrlHeld = !kbState.ctrlHeld
            com.hackerboard.keyboard.KeyType.ALT_KEY ->
                kbState.altHeld = !kbState.altHeld
            com.hackerboard.keyboard.KeyType.ARROW_UP ->
                sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_UP)
            com.hackerboard.keyboard.KeyType.ARROW_DOWN ->
                sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_DOWN)
            com.hackerboard.keyboard.KeyType.ARROW_LEFT ->
                sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_LEFT)
            com.hackerboard.keyboard.KeyType.ARROW_RIGHT ->
                sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_RIGHT)
            com.hackerboard.keyboard.KeyType.FN -> {
                val fNum = keyLabel.removePrefix("F").toIntOrNull() ?: return
                val code = KeyEvent.KEYCODE_F1 + (fNum - 1)
                sendDownUpKeyEvents(code)
            }
            com.hackerboard.keyboard.KeyType.SYMBOL_TOGGLE ->
                kbState.panelMode = if (kbState.panelMode == PanelMode.SYMBOL)
                    PanelMode.NONE else PanelMode.SYMBOL
            com.hackerboard.keyboard.KeyType.MODE_SWITCH ->
                switchToNextInputMethod(false)
            com.hackerboard.keyboard.KeyType.VOICE -> startVoice()
            com.hackerboard.keyboard.KeyType.AI_TOOL ->
                kbState.panelMode = if (kbState.panelMode == PanelMode.AI)
                    PanelMode.NONE else PanelMode.AI
            com.hackerboard.keyboard.KeyType.MACRO_PANEL ->
                kbState.panelMode = if (kbState.panelMode == PanelMode.MACRO)
                    PanelMode.NONE else PanelMode.MACRO
            com.hackerboard.keyboard.KeyType.SUPER ->
                sendDownUpKeyEvents(KeyEvent.KEYCODE_META_LEFT)
        }
    }
 
    private fun updateSuggestions(ic: InputConnection) {
        val word = ic.getTextBeforeCursor(32, 0)
            ?.toString()
            ?.trimEnd()
            ?.split("\\s".toRegex())
            ?.lastOrNull() ?: ""
        suggest.suggest(word)
    }
 
    // ── Commit helpers ────────────────────────────────────────────────
    fun commitSuggestion(word: String) {
        val ic = currentInputConnection ?: return
        val before = ic.getTextBeforeCursor(32, 0)?.toString() ?: ""
        val partial = before.split("\\s".toRegex()).lastOrNull() ?: ""
        ic.deleteSurroundingText(partial.length, 0)
        ic.commitText("$word ", 1)
        kbState.suggestions = emptyList()
    }
 
    fun pasteClip(text: String) {
        currentInputConnection?.commitText(text, 1)
    }
 
    fun applyMacro(expansion: String) {
        currentInputConnection?.commitText(expansion, 1)
        kbState.panelMode = PanelMode.NONE
    }
 
    fun applySnippet(body: String) {
        currentInputConnection?.commitText(body, 1)
        kbState.panelMode = PanelMode.NONE
    }
 
    fun applyHackerTool(result: String) {
        currentInputConnection?.commitText(result, 1)
    }
 
    // ── Voice input ───────────────────────────────────────────────────
    private fun startVoice() {
        if (!voice.isAvailable) return
        voice.start(
            onResult      = { text -> currentInputConnection?.commitText(text, 1) },
            onError       = { /* surface error in UI if needed */ },
            onStateChange = { /* future: animate mic icon */ },
        )
    }
}