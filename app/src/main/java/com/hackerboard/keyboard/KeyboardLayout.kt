package com.hackerboard.keyboard
 
enum class LayoutId { QWERTY, DVORAK, COLEMAK, PROGRAMMER_DVORAK }
 
data class Key(
    val primary: String,
    val shift: String   = "",
    val alt: String     = "",       // top-left label
    val sym: String     = "",       // top-right label
    val code: Int       = 0,        // KeyEvent keycode, 0 = use primary char
    val widthWeight: Float = 1f,
    val type: KeyType  = KeyType.CHARACTER,
)
 
enum class KeyType {
    CHARACTER, BACKSPACE, ENTER, SHIFT, SPACE, TAB,
    CTRL, ALT_KEY, SUPER, ESC, FN, SYMBOL_TOGGLE,
    ARROW_UP, ARROW_DOWN, ARROW_LEFT, ARROW_RIGHT,
    MODE_SWITCH, VOICE, AI_TOOL, MACRO_PANEL,
}
 
object KeyboardLayouts {
 
    val F_ROW: List<Key> = listOf(
        Key("F1",  type = KeyType.FN),
        Key("F2",  type = KeyType.FN),
        Key("F3",  type = KeyType.FN),
        Key("F4",  type = KeyType.FN),
        Key("F5",  type = KeyType.FN),
        Key("F6",  type = KeyType.FN),
        Key("F7",  type = KeyType.FN),
        Key("F8",  type = KeyType.FN),
        Key("F9",  type = KeyType.FN),
        Key("F10", type = KeyType.FN),
        Key("F11", type = KeyType.FN),
        Key("F12", type = KeyType.FN),
    )
 
    val SYMBOL_ROW: List<Key> = listOf(
        Key("{", "}"),
        Key("(", ")"),
        Key("[", "]"),
        Key(";", ":"),
        Key("=", "+"),
        Key("<", ">"),
        Key("/", "?"),
        Key("\\", "|"),
        Key("&", "^"),
        Key("*", "~"),
        Key("`", "\""),
    )
 
    val NUMBER_ROW: List<Key> = listOf(
        Key("ESC",  type  = KeyType.ESC,       widthWeight = 1.2f),
        Key("1",    shift = "!", alt  = "!"),
        Key("2",    shift = "@", alt  = "@"),
        Key("3",    shift = "#", alt  = "#"),
        Key("4",    shift = "$", alt  = "$"),
        Key("5",    shift = "%", alt  = "%"),
        Key("6",    shift = "^", alt  = "^"),
        Key("7",    shift = "&", alt  = "&"),
        Key("8",    shift = "*", alt  = "*"),
        Key("9",    shift = "(", alt  = "("),
        Key("0",    shift = ")", alt  = ")"),
        Key("DEL",  type  = KeyType.BACKSPACE,  widthWeight = 1.2f),
    )
 
    // ── QWERTY ────────────────────────────────────────────────────────
    val QWERTY_ROW1 = listOf(
        Key("TAB",  type  = KeyType.TAB,       widthWeight = 1.4f),
        Key("q", "Q", sym = "`"),
        Key("w", "W", sym = "~"),
        Key("e", "E"),
        Key("r", "R"),
        Key("t", "T"),
        Key("y", "Y"),
        Key("u", "U"),
        Key("i", "I"),
        Key("o", "O"),
        Key("p", "P", sym = "π"),
    )
    val QWERTY_ROW2 = listOf(
        Key("CTRL", type  = KeyType.CTRL,      widthWeight = 1.6f),
        Key("a", "A"),
        Key("s", "S"),
        Key("d", "D"),
        Key("f", "F"),
        Key("g", "G"),
        Key("h", "H"),
        Key("j", "J"),
        Key("k", "K"),
        Key("l", "L"),
        Key("↵",    type  = KeyType.ENTER,      widthWeight = 1.6f),
    )
    val QWERTY_ROW3 = listOf(
        Key("⇧",    type  = KeyType.SHIFT,      widthWeight = 1.8f),
        Key("z", "Z"),
        Key("x", "X"),
        Key("c", "C"),
        Key("v", "V"),
        Key("b", "B"),
        Key("n", "N"),
        Key("m", "M"),
        Key("↑",    type  = KeyType.ARROW_UP,   widthWeight = 0.8f),
    )
 
    // ── DVORAK ───────────────────────────────────────────────────────
    val DVORAK_ROW1 = listOf(
        Key("TAB",  type  = KeyType.TAB,       widthWeight = 1.4f),
        Key("'", "\""),
        Key(",", "<"),
        Key(".", ">"),
        Key("p", "P"),
        Key("y", "Y"),
        Key("f", "F"),
        Key("g", "G"),
        Key("c", "C"),
        Key("r", "R"),
        Key("l", "L"),
    )
    val DVORAK_ROW2 = listOf(
        Key("CTRL", type  = KeyType.CTRL,      widthWeight = 1.6f),
        Key("a", "A"),
        Key("o", "O"),
        Key("e", "E"),
        Key("u", "U"),
        Key("i", "I"),
        Key("d", "D"),
        Key("h", "H"),
        Key("t", "T"),
        Key("n", "N"),
        Key("↵",    type  = KeyType.ENTER,      widthWeight = 1.6f),
    )
    val DVORAK_ROW3 = listOf(
        Key("⇧",    type  = KeyType.SHIFT,      widthWeight = 1.8f),
        Key(";", ":"),
        Key("q", "Q"),
        Key("j", "J"),
        Key("k", "K"),
        Key("x", "X"),
        Key("b", "B"),
        Key("m", "M"),
        Key("↑",    type  = KeyType.ARROW_UP,   widthWeight = 0.8f),
    )
 
    // ── COLEMAK ──────────────────────────────────────────────────────
    val COLEMAK_ROW1 = listOf(
        Key("TAB",  type  = KeyType.TAB,       widthWeight = 1.4f),
        Key("q", "Q"),
        Key("w", "W"),
        Key("f", "F"),
        Key("p", "P"),
        Key("g", "G"),
        Key("j", "J"),
        Key("l", "L"),
        Key("u", "U"),
        Key("y", "Y"),
        Key(";", ":"),
    )
    val COLEMAK_ROW2 = listOf(
        Key("CTRL", type  = KeyType.CTRL,      widthWeight = 1.6f),
        Key("a", "A"),
        Key("r", "R"),
        Key("s", "S"),
        Key("t", "T"),
        Key("d", "D"),
        Key("h", "H"),
        Key("n", "N"),
        Key("e", "E"),
        Key("i", "I"),
        Key("↵",    type  = KeyType.ENTER,      widthWeight = 1.6f),
    )
    val COLEMAK_ROW3 = listOf(
        Key("⇧",    type  = KeyType.SHIFT,      widthWeight = 1.8f),
        Key("z", "Z"),
        Key("x", "X"),
        Key("c", "C"),
        Key("v", "V"),
        Key("b", "B"),
        Key("k", "K"),
        Key("m", "M"),
        Key("↑",    type  = KeyType.ARROW_UP,   widthWeight = 0.8f),
    )
 
    val BOTTOM_ROW = listOf(
        Key("ALT",  type  = KeyType.ALT_KEY,    widthWeight = 1f),
        Key("SYM",  type  = KeyType.SYMBOL_TOGGLE, widthWeight = 1f),
        Key("🌐",   type  = KeyType.MODE_SWITCH, widthWeight = 0.8f),
        Key(" ",    type  = KeyType.SPACE,       widthWeight = 4f),
        Key("🎙",   type  = KeyType.VOICE,        widthWeight = 0.8f),
        Key("AI",   type  = KeyType.AI_TOOL,     widthWeight = 1f),
    )
 
    fun getRows(layout: LayoutId): Triple<List<Key>, List<Key>, List<Key>> = when (layout) {
        LayoutId.QWERTY, LayoutId.PROGRAMMER_DVORAK ->
            Triple(QWERTY_ROW1, QWERTY_ROW2, QWERTY_ROW3)
        LayoutId.DVORAK ->
            Triple(DVORAK_ROW1, DVORAK_ROW2, DVORAK_ROW3)
        LayoutId.COLEMAK ->
            Triple(COLEMAK_ROW1, COLEMAK_ROW2, COLEMAK_ROW3)
    }
}