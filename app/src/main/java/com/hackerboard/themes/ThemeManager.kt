package com.hackerboard.themes
 
 import android.content.Context
 import androidx.datastore.preferences.core.edit
 import androidx.datastore.preferences.core.stringPreferencesKey
 import androidx.datastore.preferences.preferencesDataStore
 import kotlinx.coroutines.flow.Flow
 import kotlinx.coroutines.flow.map
import com.hackerboard.keyboard.LayoutId
import com.hackerboard.keyboard.KeyType
  
  private val Context.ds by preferencesDataStore("hb_prefs")
   
   class ThemeManager(private val context: Context) {
    
        companion object {
                private val KEY_THEME          = stringPreferencesKey("theme")
                        private val KEY_LAYOUT         = stringPreferencesKey("layout")
                                private val KEY_SHOW_FKEYS     = stringPreferencesKey("show_fkeys")
                                        private val KEY_SHOW_SYMBOLS   = stringPreferencesKey("show_symbols")
                                                private val KEY_SHOW_NUMBERS   = stringPreferencesKey("show_numbers")
                                                        private val KEY_VIBRATION      = stringPreferencesKey("vibration")
                                                                private val KEY_SOUND          = stringPreferencesKey("sound")
                                                                    }
                                                                     
                                                                         val themeFlow: Flow<KeyboardTheme> = context.ds.data.map { prefs ->
                                                                                 val id = prefs[KEY_THEME]?.let { ThemeId.valueOf(it) } ?: ThemeId.HACKER
                                                                                         Themes.get(id)
                                                                                             }
                                                                                              
                                                                                                  val layoutFlow: Flow<LayoutId> = context.ds.data.map { prefs ->
                                                                                                          prefs[KEY_LAYOUT]?.let { LayoutId.valueOf(it) } ?: LayoutId.QWERTY
                                                                                                              }
                                                                                                               
                                                                                                                   val showFKeysFlow: Flow<Boolean> = context.ds.data.map { prefs ->
                                                                                                                           prefs[KEY_SHOW_FKEYS]?.toBooleanStrictOrNull() ?: true
                                                                                                                               }
                                                                                                                                
                                                                                                                                    val showSymbolsFlow: Flow<Boolean> = context.ds.data.map { prefs ->
                                                                                                                                            prefs[KEY_SHOW_SYMBOLS]?.toBooleanStrictOrNull() ?: true
                                                                                                                                                }
                                                                                                                                                 
                                                                                                                                                     val showNumbersFlow: Flow<Boolean> = context.ds.data.map { prefs ->
                                                                                                                                                             prefs[KEY_SHOW_NUMBERS]?.toBooleanStrictOrNull() ?: true
                                                                                                                                                                 }
                                                                                                                                                                  
                                                                                                                                                                      val vibrationFlow: Flow<Boolean> = context.ds.data.map { prefs ->
                                                                                                                                                                              prefs[KEY_VIBRATION]?.toBooleanStrictOrNull() ?: true
                                                                                                                                                                                  }
                                                                                                                                                                                   
                                                                                                                                                                                       val soundFlow: Flow<Boolean> = context.ds.data.map { prefs ->
                                                                                                                                                                                               prefs[KEY_SOUND]?.toBooleanStrictOrNull() ?: false
                                                                                                                                                                                                   }
                                                                                                                                                                                                    
                                                                                                                                                                                                        suspend fun setTheme(id: ThemeId)          { context.ds.edit { it[KEY_THEME]        = id.name } }
                                                                                                                                                                                                            suspend fun setLayout(id: LayoutId)        { context.ds.edit { it[KEY_LAYOUT]       = id.name } }
                                                                                                                                                                                                                suspend fun setShowFKeys(v: Boolean)       { context.ds.edit { it[KEY_SHOW_FKEYS]   = v.toString() } }
                                                                                                                                                                                                                    suspend fun setShowSymbols(v: Boolean)     { context.ds.edit { it[KEY_SHOW_SYMBOLS] = v.toString() } }
                                                                                                                                                                                                                        suspend fun setShowNumbers(v: Boolean)     { context.ds.edit { it[KEY_SHOW_NUMBERS] = v.toString() } }
                                                                                                                                                                                                                            suspend fun setVibration(v: Boolean)       { context.ds.edit { it[KEY_VIBRATION]    = v.toString() } }
                                                                                                                                                                                                                                suspend fun setSound(v: Boolean)           { context.ds.edit { it[KEY_SOUND]        = v.toString() } }
                                                                                                                                                                                                                                }