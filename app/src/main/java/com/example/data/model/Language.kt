package com.example.data.model

data class Language(
    val id: String,
    val name: String,
    val nativeName: String,
    val flagEmoji: String,
    val code: String,          // ISO 639-1 e.g. "es", "ja", "fr"
    val ttsLocaleTag: String,  // e.g. "es-ES", "ja-JP", "fr-FR"
    val script: String,        // Latin, Kanji/Kana, Hangul, Devanagari, Arabic, Cyrillic
    val helloGreeting: String,
    val description: String,
    val popular: Boolean = false
)

object SupportedLanguages {
    val list: List<Language> = listOf(
        Language("es", "Spanish", "Español", "🇪🇸", "es", "es-ES", "Latin", "¡Hola!", "Over 500 million speakers worldwide", true),
        Language("fr", "French", "Français", "🇫🇷", "fr", "fr-FR", "Latin", "Bonjour !", "The language of culture and cuisine", true),
        Language("ja", "Japanese", "日本語", "🇯🇵", "ja", "ja-JP", "Kanji/Kana", "こんにちは！", "Master Hiragana, Katakana, and Kanji", true),
        Language("de", "German", "Deutsch", "🇩🇪", "de", "de-DE", "Latin", "Hallo!", "Europe's economic engine and literature", true),
        Language("en", "English", "English", "🇬🇧", "en", "en-US", "Latin", "Hello!", "The global language for business & travel", true),
        Language("hi", "Hindi", "हिन्दी", "🇮🇳", "hi", "hi-IN", "Devanagari", "नमस्ते!", "One of India's most spoken languages", true),
        Language("ko", "Korean", "한국어", "🇰🇷", "ko", "ko-KR", "Hangul", "안녕하세요!", "K-Pop, drama, and rich culture", true),
        Language("zh", "Mandarin Chinese", "中文", "🇨🇳", "zh", "zh-CN", "Hanzi", "你好！", "Tones and thousands of characters", true),
        Language("ar", "Arabic", "العربية", "🇸🇦", "ar", "ar-SA", "Arabic", "مرحباً!", "Rich phonetic roots and calligraphy", false),
        Language("pt", "Portuguese", "Português", "🇧🇷", "pt", "pt-BR", "Latin", "Olá!", "Vibrant rhythms of Brazil and Portugal", false),
        Language("ru", "Russian", "Русский", "🇷🇺", "ru", "ru-RU", "Cyrillic", "Привет!", "Cyrillic alphabet and Slavic idioms", false),
        Language("it", "Italian", "Italiano", "🇮🇹", "it", "it-IT", "Latin", "Ciao!", "The musical language of art and pasta", false),
        Language("tr", "Turkish", "Türkçe", "🇹🇷", "tr", "tr-TR", "Latin", "Merhaba!", "Harmonic vowel harmony and rich history", false),
        Language("bn", "Bengali", "বাংলা", "🇧🇩", "bn", "bn-BD", "Bengali", "নমস্কার!", "Poetry, melody, and South Asian heritage", false),
        Language("ur", "Urdu", "اردو", "🇵🇰", "ur", "ur-PK", "Nastaliq", "سلام!", "Poetic grace and Persian-Arabic heritage", false)
    )

    fun getById(id: String): Language = list.firstOrNull { it.id == id } ?: list.first()
}
