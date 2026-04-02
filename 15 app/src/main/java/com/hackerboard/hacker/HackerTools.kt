package com.hackerboard.hacker
 
import android.util.Base64
import java.security.MessageDigest
import java.net.URLEncoder
import java.net.URLDecoder
 
object HackerTools {
 
    fun toBase64(input: String): String =
        Base64.encodeToString(input.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
 
    fun fromBase64(input: String): String = runCatching {
        String(Base64.decode(input, Base64.DEFAULT), Charsets.UTF_8)
    }.getOrElse { "Invalid Base64" }
 
    fun toHex(input: String): String =
        input.toByteArray(Charsets.UTF_8).joinToString("") { "%02x".format(it) }
 
    fun fromHex(input: String): String = runCatching {
        val clean = input.replace(" ", "").replace("0x", "")
        val bytes = ByteArray(clean.length / 2) { i ->
            clean.substring(i * 2, i * 2 + 2).toInt(16).toByte()
        }
        String(bytes, Charsets.UTF_8)
    }.getOrElse { "Invalid Hex" }
 
    fun md5(input: String): String    = hash(input, "MD5")
    fun sha1(input: String): String   = hash(input, "SHA-1")
    fun sha256(input: String): String = hash(input, "SHA-256")
 
    fun urlEncode(input: String): String = URLEncoder.encode(input, "UTF-8")
    fun urlDecode(input: String): String = URLDecoder.decode(input, "UTF-8")
 
    fun toBinary(input: String): String =
        input.toByteArray(Charsets.UTF_8)
             .joinToString(" ") { it.toInt().and(0xFF).toString(2).padStart(8, '0') }
 
    fun toAsciiCodes(input: String): String =
        input.map { it.code }.joinToString(" ")
 
    fun reverseString(input: String): String = input.reversed()
 
    fun countChars(input: String): String =
        "Chars: ${input.length} | Words: ${input.split("\\s+".toRegex()).size} | Lines: ${input.lines().size}"
 
    private fun hash(input: String, algo: String): String {
        val digest = MessageDigest.getInstance(algo)
        return digest.digest(input.toByteArray()).joinToString("") { "%02x".format(it) }
    }
}
 
data class HackerToolDef(
    val label: String,
    val description: String,
    val action: (String) -> String,
)
 
val ALL_HACKER_TOOLS = listOf(
    HackerToolDef("→B64",   "Encode Base64")   { HackerTools.toBase64(it) },
    HackerToolDef("B64→",   "Decode Base64")   { HackerTools.fromBase64(it) },
    HackerToolDef("→HEX",   "Encode Hex")      { HackerTools.toHex(it) },
    HackerToolDef("HEX→",   "Decode Hex")      { HackerTools.fromHex(it) },
    HackerToolDef("MD5",    "MD5 Hash")         { HackerTools.md5(it) },
    HackerToolDef("SHA1",   "SHA-1 Hash")       { HackerTools.sha1(it) },
    HackerToolDef("SHA256", "SHA-256 Hash")     { HackerTools.sha256(it) },
    HackerToolDef("URL+",   "URL Encode")       { HackerTools.urlEncode(it) },
    HackerToolDef("URL-",   "URL Decode")       { HackerTools.urlDecode(it) },
    HackerToolDef("→BIN",   "Text to Binary")  { HackerTools.toBinary(it) },
    HackerToolDef("→ASC",   "Text to ASCII")   { HackerTools.toAsciiCodes(it) },
    HackerToolDef("REV",    "Reverse String")  { HackerTools.reverseString(it) },
    HackerToolDef("COUNT",  "Count Stats")     { HackerTools.countChars(it) },
)