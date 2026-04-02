package com.hackerboard.ui
 
 import androidx.compose.foundation.layout.*
 import androidx.compose.runtime.Composable
 import androidx.compose.ui.Alignment
 import androidx.compose.ui.Modifier
 import androidx.compose.ui.unit.dp
 import com.hackerboard.keyboard.Key
 import com.hackerboard.keyboard.KeyType
 import com.hackerboard.themes.KeyboardTheme
  
  private val UP    = Key("↑", type = KeyType.ARROW_UP)
  private val DOWN  = Key("↓", type = KeyType.ARROW_DOWN)
  private val LEFT  = Key("←", type = KeyType.ARROW_LEFT)
  private val RIGHT = Key("→", type = KeyType.ARROW_RIGHT)
   
   @Composable
   fun ArrowClusterUI(
       theme: KeyboardTheme,
           modifier: Modifier = Modifier,
               onKey: (Key) -> Unit,
               ) {
                   Column(
                           modifier            = modifier,
                                   horizontalAlignment = Alignment.CenterHorizontally,
                                           verticalArrangement = Arrangement.spacedBy(2.dp),
                                               ) {
                                                       KeyUI(
                                                                   key         = UP,
                                                                               theme       = theme,
                                                                                           isUpperCase = false,
                                                                                                       modifier    = Modifier.fillMaxWidth(0.55f).weight(1f),
                                                                                                                   onPress     = { onKey(UP) },
                                                                                                                           )
                                                                                                                                   Row(
                                                                                                                                               modifier            = Modifier.fillMaxWidth().weight(1f),
                                                                                                                                                           horizontalArrangement = Arrangement.spacedBy(2.dp),
                                                                                                                                                                   ) {
                                                                                                                                                                               KeyUI(
                                                                                                                                                                                               key         = LEFT,
                                                                                                                                                                                                               theme       = theme,
                                                                                                                                                                                                                               isUpperCase = false,
                                                                                                                                                                                                                                               modifier    = Modifier.weight(1f).fillMaxHeight(),
                                                                                                                                                                                                                                                               onPress     = { onKey(LEFT) },
                                                                                                                                                                                                                                                                           )
                                                                                                                                                                                                                                                                                       KeyUI(
                                                                                                                                                                                                                                                                                                       key         = DOWN,
                                                                                                                                                                                                                                                                                                                       theme       = theme,
                                                                                                                                                                                                                                                                                                                                       isUpperCase = false,
                                                                                                                                                                                                                                                                                                                                                       modifier    = Modifier.weight(1f).fillMaxHeight(),
                                                                                                                                                                                                                                                                                                                                                                       onPress     = { onKey(DOWN) },
                                                                                                                                                                                                                                                                                                                                                                                   )
                                                                                                                                                                                                                                                                                                                                                                                               KeyUI(
                                                                                                                                                                                                                                                                                                                                                                                                               key         = RIGHT,
                                                                                                                                                                                                                                                                                                                                                                                                                               theme       = theme,
                                                                                                                                                                                                                                                                                                                                                                                                                                               isUpperCase = false,
                                                                                                                                                                                                                                                                                                                                                                                                                                                               modifier    = Modifier.weight(1f).fillMaxHeight(),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                               onPress     = { onKey(RIGHT) },
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           )
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       }