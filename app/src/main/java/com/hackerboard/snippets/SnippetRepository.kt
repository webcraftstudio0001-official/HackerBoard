package com.hackerboard.snippets
 
 import android.content.Context
 import com.google.gson.Gson
 import com.google.gson.reflect.TypeToken
  
  data class Snippet(
      val id: String,
          val label: String,
              val body: String,
                  val language: String = "any",
                  )
                   
                   class SnippetRepository(context: Context) {
                    
                        private val prefs = context.getSharedPreferences("snippets", Context.MODE_PRIVATE)
                            private val gson  = Gson()
                                private val type  = object : TypeToken<MutableList<Snippet>>() {}.type
                                 
                                     private var _snippets: MutableList<Snippet> = load()
                                      
                                          val snippets: List<Snippet> get() = _snippets.toList()
                                           
                                               fun add(snippet: Snippet) {
                                                       _snippets.removeAll { it.id == snippet.id }
                                                               _snippets.add(snippet)
                                                                       save()
                                                                           }
                                                                            
                                                                                fun remove(id: String) {
                                                                                        _snippets.removeAll { it.id == id }
                                                                                                save()
                                                                                                    }
                                                                                                     
                                                                                                         private fun save() {
                                                                                                                 prefs.edit().putString("data", gson.toJson(_snippets)).apply()
                                                                                                                     }
                                                                                                                      
                                                                                                                          private fun load(): MutableList<Snippet> {
                                                                                                                                  val json = prefs.getString("data", null) ?: return defaultSnippets()
                                                                                                                                          return runCatching { gson.fromJson<MutableList<Snippet>>(json, type) }
                                                                                                                                                      .getOrElse { defaultSnippets() }
                                                                                                                                                          }
                                                                                                                                                           
                                                                                                                                                               private fun defaultSnippets(): MutableList<Snippet> = mutableListOf(
                                                                                                                                                                       Snippet("1", "if/else",       "if () {\n  \n} else {\n  \n}",     "any"),
                                                                                                                                                                               Snippet("2", "for loop",      "for (int i = 0; i < n; i++) {\n  \n}", "java"),
                                                                                                                                                                                       Snippet("3", "Kotlin data",   "data class  (\n    val : \n)",       "kotlin"),
                                                                                                                                                                                               Snippet("4", "Python def",    "def ():\n    pass",                  "python"),
                                                                                                                                                                                                       Snippet("5", "HTML boiler",   "<!DOCTYPE html>\n<html>\n<head>\n  <meta charset=\"UTF-8\">\n 
                                                                                                                                                                                                           <title></title>\n</head>\n<body>\n  \n</body>\n</html>", "html"),
                                                                                                                                                                                                                   Snippet("6", "SQL Select",    "SELECT *\nFROM \nWHERE \nLIMIT 100;", "sql"),
                                                                                                                                                                                                                           Snippet("7", "curl GET",      "curl -X GET \\\n  -H 'Content-Type: application/json' \\\n 
                                                                                                                                                                                                                               https://", "shell"),
                                                                                                                                                                                                                                       Snippet("8", "Docker run",    "docker run -it --rm \\\n  -p 8080:8080 \\\n  IMAGE_NAME", "shell"),
                                                                                                                                                                                                                                           )
                                                                                                                                                                                                                                           }