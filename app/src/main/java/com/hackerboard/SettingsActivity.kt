package com.hackerboard
 
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hackerboard.themes.LayoutId
import com.hackerboard.themes.ThemeId
import com.hackerboard.themes.ThemeManager
import com.hackerboard.themes.Themes
import kotlinx.coroutines.launch
 
class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val themeMan = (application as HackerBoardApp).themeManager
        setContent { SettingsScreen(themeMan, ::openImeSettings) }
    }
 
    private fun openImeSettings() {
        startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))
    }
}
 
@Composable
private fun SettingsScreen(
    themeMan: ThemeManager,
    onOpenIme: () -> Unit,
) {
    val scope         = rememberCoroutineScope()
    val theme         by themeMan.themeFlow.collectAsState(Themes.get(ThemeId.HACKER))
    val layout        by themeMan.layoutFlow.collectAsState(LayoutId.QWERTY)
    val showFKeys     by themeMan.showFKeysFlow.collectAsState(true)
    val showSymbols   by themeMan.showSymbolsFlow.collectAsState(true)
    val showNumbers   by themeMan.showNumbersFlow.collectAsState(true)
    val vibration     by themeMan.vibrationFlow.collectAsState(true)
    val sound         by themeMan.soundFlow.collectAsState(false)
 
    val bg = Color(0xFF0A0A0F)
    val accent = theme.accentColor
 
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 24.dp),
    ) {
        item {
            Text(
                "⌨ HackerBoard",
                fontSize   = 24.sp,
                fontWeight = FontWeight.Black,
                color      = accent,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 2.sp,
            )
            Text(
                "Settings",
                fontSize = 12.sp,
                color    = accent.copy(alpha = 0.5f),
                fontFamily = FontFamily.Monospace,
            )
        }
 
        // Enable keyboard
        item {
            SettingsButton(
                label   = "Enable HackerBoard",
                sub     = "Open Android IME settings",
                accent  = accent,
                onClick = onOpenIme,
            )
        }
 
        // Theme
        item {
            SectionHeader("Theme", accent)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                ThemeId.entries.forEach { id ->
                    val t = Themes.get(id)
                    val selected = theme.id == id
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(t.background)
                            .border(
                                if (selected) 2.dp else 0.5.dp,
                                if (selected) accent else Color(0xFF222222),
                                RoundedCornerShape(8.dp),
                            )
                            .clickable { scope.launch { themeMan.setTheme(id) } },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            t.name,
                            fontSize = 7.sp,
                            color    = t.accentColor,
                            fontFamily = FontFamily.Monospace,
                        )
                    }
                }
            }
        }
 
        // Layout
        item {
            SectionHeader("Layout", accent)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                LayoutId.entries.forEach { id ->
                    val selected = layout == id
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (selected) accent.copy(alpha = 0.15f) else Color(0xFF111118))
                            .border(
                                if (selected) 1.5.dp else 0.5.dp,
                                if (selected) accent else Color(0xFF222233),
                                RoundedCornerShape(8.dp),
                            )
                            .clickable { scope.launch { themeMan.setLayout(id) } }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            id.name,
                            fontSize = 8.sp,
                            color    = if (selected) accent else Color(0xFF888888),
                            fontFamily = FontFamily.Monospace,
                        )
                    }
                }
            }
        }
 
        // Row toggles
        item {
            SectionHeader("Rows", accent)
            ToggleRow("F1–F12 Row",    showFKeys,   accent) { scope.launch { themeMan.setShowFKeys(it) } }
            Spacer(Modifier.height(6.dp))
            ToggleRow("Symbol Row",    showSymbols, accent) { scope.launch { themeMan.setShowSymbols(it) } }
            Spacer(Modifier.height(6.dp))
            ToggleRow("Number Row",    showNumbers, accent) { scope.launch { themeMan.setShowNumbers(it) } }
        }
 
        // Feedback
        item {
            SectionHeader("Feedback", accent)
            ToggleRow("Haptic Feedback", vibration, accent) { scope.launch { themeMan.setVibration(it) } }
            Spacer(Modifier.height(6.dp))
            ToggleRow("Key Sounds",      sound,     accent) { scope.launch { themeMan.setSound(it) } }
        }
 
        // Version
        item {
            Spacer(Modifier.height(8.dp))
            Text(
                "v1.0.0 · HackerBoard · Open Source",
                fontSize = 10.sp,
                color    = Color(0xFF333344),
                fontFamily = FontFamily.Monospace,
            )
        }
    }
}
 
@Composable
private fun SectionHeader(title: String, accent: Color) {
    Text(
        title,
        fontSize   = 11.sp,
        color      = accent.copy(alpha = 0.6f),
        fontFamily = FontFamily.Monospace,
        letterSpacing = 2.sp,
        modifier   = Modifier.padding(bottom = 6.dp),
    )
}
 
@Composable
private fun ToggleRow(
    label: String,
    checked: Boolean,
    accent: Color,
    onToggle: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF111118))
            .border(0.5.dp, Color(0xFF1E1E2E), RoundedCornerShape(8.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically,
    ) {
        Text(label, fontSize = 13.sp, color = Color(0xFFCCCCDD), fontFamily = FontFamily.Monospace)
        Switch(
            checked         = checked,
            onCheckedChange = onToggle,
            colors          = SwitchDefaults.colors(
                checkedThumbColor  = accent,
                checkedTrackColor  = accent.copy(alpha = 0.3f),
                uncheckedThumbColor = Color(0xFF444444),
                uncheckedTrackColor = Color(0xFF222222),
            ),
        )
    }
}
 
@Composable
private fun SettingsButton(
    label: String,
    sub: String,
    accent: Color,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(accent.copy(alpha = 0.1f))
            .border(1.dp, accent.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
    ) {
        Column {
            Text(label, fontSize = 14.sp, color = accent, fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold)
            Text(sub, fontSize = 11.sp, color = accent.copy(alpha = 0.5f), fontFamily = FontFamily.Monospace)
        }
    }
}