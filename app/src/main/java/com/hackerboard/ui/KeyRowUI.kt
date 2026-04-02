package com.hackerboard.ui
 
 import androidx.compose.foundation.layout.*
 import androidx.compose.runtime.Composable
 import androidx.compose.ui.Modifier
 import androidx.compose.ui.unit.dp
 import com.hackerboard.keyboard.Key
 import com.hackerboard.keyboard.KeyType
 import com.hackerboard.themes.KeyboardTheme
  
  @Composable
  fun KeyRowUI(
      keys: List<Key>,
          theme: KeyboardTheme,
              isUpperCase: Boolean,
                  activeModifiers: Set<KeyType> = emptySet(),
                      rowHeight: Int = 40,
                          onKey: (Key) -> Unit,
                              onLongKey: ((Key) -> Unit)? = null,
                              ) {
                                  Row(
                                          modifier = Modifier
                                                      .fillMaxWidth()
                                                                  .height(rowHeight.dp)
                                                                              .padding(horizontal = 4.dp),
                                                                                      horizontalArrangement = Arrangement.spacedBy(3.dp),
                                                                                          ) {
                                                                                                  keys.forEach { key ->
                                                                                                              KeyUI(
                                                                                                                              key        = key,
                                                                                                                                              theme      = theme,
                                                                                                                                                              isUpperCase = isUpperCase,
                                                                                                                                                                              isActive   = key.type in activeModifiers,
                                                                                                                                                                                              modifier   = Modifier
                                                                                                                                                                                                                  .weight(key.widthWeight)
                                                                                                                                                                                                                                      .fillMaxHeight(),
                                                                                                                                                                                                                                                      onPress    = { onKey(key) },
                                                                                                                                                                                                                                                                      onLongPress = onLongKey?.let { cb -> { cb(key) } },
                                                                                                                                                                                                                                                                                  )
                                                                                                                                                                                                                                                                                          }
                                                                                                                                                                                                                                                                                              }
                                                                                                                                                                                                                                                                                              }