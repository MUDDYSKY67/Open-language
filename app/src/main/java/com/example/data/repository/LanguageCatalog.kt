package com.example.data.repository

import com.example.data.model.CefrLevel
import com.example.data.model.Course
import com.example.data.model.CourseUnit
import com.example.data.model.Exercise
import com.example.data.model.ExerciseType
import com.example.data.model.Lesson
import com.example.data.model.MatchPair

object LanguageCatalog {

    fun getCourseForLanguage(languageId: String): Course {
        return when (languageId) {
            "es" -> createSpanishCourse()
            "fr" -> createFrenchCourse()
            "ja" -> createJapaneseCourse()
            "de" -> createGermanCourse()
            "hi" -> createHindiCourse()
            "ko" -> createKoreanCourse()
            "zh" -> createChineseCourse()
            "it" -> createItalianCourse()
            "pt" -> createPortugueseCourse()
            "ru" -> createRussianCourse()
            "ar" -> createArabicCourse()
            else -> createGenericCourse(languageId)
        }
    }

    private fun createSpanishCourse(): Course {
        val unit1 = CourseUnit(
            id = "es_u1",
            number = 1,
            title = "Essential Greetings & Introductions",
            subtitle = "Master basic greetings, polite phrases, and naming yourself",
            cefrLevel = CefrLevel.A1,
            colorHex = 0xFF10B981,
            iconEmoji = "👋",
            lessons = listOf(
                Lesson(
                    id = "es_u1_l1",
                    title = "First Words",
                    description = "Learn how to say Hello, Goodbye, and Thank you",
                    order = 1,
                    exercises = listOf(
                        Exercise(
                            id = "es_e1",
                            type = ExerciseType.MULTIPLE_CHOICE,
                            prompt = "Select the correct Spanish translation for 'Hello':",
                            targetText = "Hello",
                            phonetic = "o-la",
                            options = listOf("¡Hola!", "Adiós", "Por favor", "Gracias"),
                            correctAnswer = "¡Hola!",
                            explanation = "'¡Hola!' is the universal greeting in Spanish, suitable for both formal and informal situations.",
                            audioText = "¡Hola!"
                        ),
                        Exercise(
                            id = "es_e2",
                            type = ExerciseType.TRANSLATE,
                            prompt = "Translate this polite expression:",
                            targetText = "Muchas gracias",
                            phonetic = "moo-chas gra-see-as",
                            options = listOf("Thank you very much", "Good morning", "See you later", "Please"),
                            correctAnswer = "Thank you very much",
                            explanation = "'Muchas gracias' combines 'muchas' (many) and 'gracias' (thanks) to mean 'Thank you very much'.",
                            audioText = "Muchas gracias"
                        ),
                        Exercise(
                            id = "es_e3",
                            type = ExerciseType.MATCH_WORDS,
                            prompt = "Match each Spanish word with its English meaning:",
                            targetText = "Match pairs",
                            correctAnswer = "matched",
                            matchPairs = listOf(
                                MatchPair("Hola", "Hello"),
                                MatchPair("Adiós", "Goodbye"),
                                MatchPair("Por favor", "Please"),
                                MatchPair("Gracias", "Thank you")
                            ),
                            explanation = "Building basic vocabulary associations helps rapid recognition."
                        ),
                        Exercise(
                            id = "es_e4",
                            type = ExerciseType.SENTENCE_BUILDER,
                            prompt = "Arrange the words to say 'Good morning, how are you?':",
                            targetText = "Good morning, how are you?",
                            phonetic = "bwe-nos dee-as, ko-mo es-tas",
                            sentenceTiles = listOf("Buenos", "días,", "¿cómo", "estás?"),
                            correctAnswer = "Buenos días, ¿cómo estás?",
                            explanation = "'Buenos días' is used until midday. '¿cómo estás?' is the friendly way to ask how someone is.",
                            audioText = "Buenos días, ¿cómo estás?"
                        ),
                        Exercise(
                            id = "es_e5",
                            type = ExerciseType.PRONUNCIATION,
                            prompt = "Speak this phrase clearly into the microphone:",
                            targetText = "Mucho gusto",
                            phonetic = "moo-cho goos-to",
                            correctAnswer = "Mucho gusto",
                            explanation = "'Mucho gusto' means 'Nice to meet you'. Listen carefully to the gentle rolled 'r' and soft 'o'.",
                            audioText = "Mucho gusto"
                        )
                    )
                ),
                Lesson(
                    id = "es_u1_l2",
                    title = "Introducing Yourself",
                    description = "State your name, origin, and friendly responses",
                    order = 2,
                    exercises = listOf(
                        Exercise(
                            id = "es_e6",
                            type = ExerciseType.FILL_BLANKS,
                            prompt = "Fill in the blank: 'Yo ___ Pedro' (I am called Pedro)",
                            targetText = "Yo ___ Pedro.",
                            options = listOf("me llamo", "tengo", "soy de", "estoy"),
                            correctAnswer = "me llamo",
                            explanation = "'Llamarse' is a reflexive verb: 'Yo me llamo...' literally means 'I call myself...'.",
                            audioText = "Yo me llamo Pedro."
                        ),
                        Exercise(
                            id = "es_e7",
                            type = ExerciseType.LISTEN_SELECT,
                            prompt = "Listen to the audio and select what you heard:",
                            targetText = "¿De dónde eres?",
                            phonetic = "de don-de e-res",
                            options = listOf("¿De dónde eres?", "¿Cómo te llamas?", "Hasta mañana", "Buenas noches"),
                            correctAnswer = "¿De dónde eres?",
                            explanation = "'¿De dónde eres?' asks 'Where are you from?' using the informal 'tú' form of ser.",
                            audioText = "¿De dónde eres?"
                        ),
                        Exercise(
                            id = "es_e8",
                            type = ExerciseType.PICTURE_VOCAB,
                            prompt = "Which item represents 'el libro' (the book)?",
                            targetText = "el libro",
                            phonetic = "el lee-bro",
                            options = listOf("📖 Book", "☕ Coffee", "🍎 Apple", "✈️ Airplane"),
                            correctAnswer = "📖 Book",
                            explanation = "'Libro' is a masculine noun in Spanish, requiring the article 'el'.",
                            audioText = "el libro",
                            iconEmoji = "📖"
                        )
                    )
                ),
                Lesson(
                    id = "es_u1_l3",
                    title = "Poly's Coffee Chat Checkpoint",
                    description = "Comprehensive check for Unit 1 greetings and politeness",
                    order = 3,
                    isCheckpoint = true,
                    xpReward = 40,
                    gemReward = 15,
                    exercises = listOf(
                        Exercise(
                            id = "es_e9",
                            type = ExerciseType.MULTIPLE_CHOICE,
                            prompt = "Poly asks: '¿Cómo estás?' What is the most natural reply?",
                            targetText = "¿Cómo estás?",
                            options = listOf("Muy bien, gracias", "Me llamo Poly", "Por favor", "Adiós"),
                            correctAnswer = "Muy bien, gracias",
                            explanation = "'Muy bien, gracias' (Very well, thank you) directly answers inquiries about your state.",
                            audioText = "¿Cómo estás? Muy bien, gracias."
                        ),
                        Exercise(
                            id = "es_e10",
                            type = ExerciseType.SENTENCE_BUILDER,
                            prompt = "Arrange to say: 'I speak a little Spanish':",
                            targetText = "I speak a little Spanish",
                            sentenceTiles = listOf("Yo", "hablo", "un", "poco", "de", "español"),
                            correctAnswer = "Yo hablo un poco de español",
                            explanation = "'Hablo' is the first person singular form of 'hablar' (to speak).",
                            audioText = "Yo hablo un poco de español."
                        )
                    )
                )
            )
        )

        val unit2 = CourseUnit(
            id = "es_u2",
            number = 2,
            title = "Tapas, Cafe & Dining Out",
            subtitle = "Order food and drinks like a local in Madrid or Barcelona",
            cefrLevel = CefrLevel.A1,
            colorHex = 0xFFF59E0B,
            iconEmoji = "☕",
            lessons = listOf(
                Lesson(
                    id = "es_u2_l1",
                    title = "At the Cafe",
                    description = "Ordering coffee, tea, and water politely",
                    order = 1,
                    exercises = listOf(
                        Exercise(
                            id = "es_e11",
                            type = ExerciseType.MULTIPLE_CHOICE,
                            prompt = "How do you ask for 'a coffee, please'?",
                            targetText = "a coffee, please",
                            options = listOf("Un café, por favor", "Una cuenta, gracias", "El agua fría", "Tengo hambre"),
                            correctAnswer = "Un café, por favor",
                            explanation = "'Un café, por favor' is the standard polite order at any Spanish cafeteria.",
                            audioText = "Un café, por favor"
                        ),
                        Exercise(
                            id = "es_e12",
                            type = ExerciseType.MATCH_WORDS,
                            prompt = "Match the restaurant words:",
                            targetText = "Match foods",
                            correctAnswer = "matched",
                            matchPairs = listOf(
                                MatchPair("El agua", "Water"),
                                MatchPair("El té", "Tea"),
                                MatchPair("La cuenta", "The bill"),
                                MatchPair("El pan", "Bread")
                            ),
                            explanation = "Notice gender articles: 'el' (masculine) vs 'la' (feminine)."
                        )
                    )
                )
            )
        )

        val unit3 = CourseUnit(
            id = "es_u3",
            number = 3,
            title = "City Directions & Exploring",
            subtitle = "Navigate metro stations, plazas, and city landmarks",
            cefrLevel = CefrLevel.A2,
            colorHex = 0xFF0284C7,
            iconEmoji = "🗺️",
            lessons = listOf(
                Lesson(
                    id = "es_u3_l1",
                    title = "Where is the Station?",
                    description = "Asking directions and understanding left, right, straight",
                    order = 1,
                    exercises = listOf(
                        Exercise(
                            id = "es_e13",
                            type = ExerciseType.MULTIPLE_CHOICE,
                            prompt = "Translate: '¿Dónde está la estación de metro?'",
                            targetText = "¿Dónde está la estación de metro?",
                            options = listOf("Where is the metro station?", "How much is the ticket?", "When does the train arrive?", "Is the museum open?"),
                            correctAnswer = "Where is the metro station?",
                            explanation = "'¿Dónde está...?' is the universal question for asking location.",
                            audioText = "¿Dónde está la estación de metro?"
                        )
                    )
                )
            )
        )

        return Course("es", "Spanish", listOf(unit1, unit2, unit3))
    }

    private fun createFrenchCourse(): Course {
        val unit1 = CourseUnit(
            id = "fr_u1",
            number = 1,
            title = "Premiers Pas & Salutations",
            subtitle = "Essential French greetings, etiquette, and polite basics",
            cefrLevel = CefrLevel.A1,
            colorHex = 0xFF0284C7,
            iconEmoji = "🥐",
            lessons = listOf(
                Lesson(
                    id = "fr_u1_l1",
                    title = "Bonjour Paris!",
                    description = "Master Bonjour, Merci, and S'il vous plaît",
                    order = 1,
                    exercises = listOf(
                        Exercise(
                            id = "fr_e1",
                            type = ExerciseType.MULTIPLE_CHOICE,
                            prompt = "Select the French translation for 'Good morning / Hello':",
                            targetText = "Hello",
                            options = listOf("Bonjour", "Bonsoir", "Merci", "Au revoir"),
                            correctAnswer = "Bonjour",
                            explanation = "'Bonjour' is the essential greeting used everywhere in France from sunrise until evening.",
                            audioText = "Bonjour"
                        ),
                        Exercise(
                            id = "fr_e2",
                            type = ExerciseType.SENTENCE_BUILDER,
                            prompt = "Arrange to say: 'Thank you very much, goodbye!':",
                            targetText = "Thank you very much, goodbye!",
                            sentenceTiles = listOf("Merci", "beaucoup,", "au", "revoir!"),
                            correctAnswer = "Merci beaucoup, au revoir!",
                            explanation = "'Merci beaucoup' adds emphasis to 'merci', and 'au revoir' literally means 'until seeing again'.",
                            audioText = "Merci beaucoup, au revoir!"
                        ),
                        Exercise(
                            id = "fr_e3",
                            type = ExerciseType.MATCH_WORDS,
                            prompt = "Match the French greetings with their meanings:",
                            targetText = "Match pairs",
                            correctAnswer = "matched",
                            matchPairs = listOf(
                                MatchPair("Bonjour", "Good day / Hello"),
                                MatchPair("Bonsoir", "Good evening"),
                                MatchPair("Merci", "Thank you"),
                                MatchPair("S'il vous plaît", "Please (formal)")
                            ),
                            explanation = "French etiquette places high value on polite opening greetings before asking questions."
                        )
                    )
                )
            )
        )
        return Course("fr", "French", listOf(unit1))
    }

    private fun createJapaneseCourse(): Course {
        val unit1 = CourseUnit(
            id = "ja_u1",
            number = 1,
            title = "Hajimemashite & Daily Greetings",
            subtitle = "Learn essential Japanese polite greetings and self-introductions",
            cefrLevel = CefrLevel.A1,
            colorHex = 0xFFEF4444,
            iconEmoji = "🌸",
            lessons = listOf(
                Lesson(
                    id = "ja_u1_l1",
                    title = "Konnichiwa Tokyo",
                    description = "Polite greetings: morning, afternoon, and night",
                    order = 1,
                    exercises = listOf(
                        Exercise(
                            id = "ja_e1",
                            type = ExerciseType.MULTIPLE_CHOICE,
                            prompt = "Which greeting is used for 'Hello' during daytime?",
                            targetText = "Hello (afternoon)",
                            phonetic = "kon-ni-chi-wa",
                            options = listOf("こんにちは (Konnichiwa)", "おはよう (Ohayou)", "こんばんは (Konbanwa)", "さようなら (Sayounara)"),
                            correctAnswer = "こんにちは (Konnichiwa)",
                            explanation = "'Konnichiwa' (こんにちは) is the standard daytime greeting across Japan.",
                            audioText = "こんにちは"
                        ),
                        Exercise(
                            id = "ja_e2",
                            type = ExerciseType.SENTENCE_BUILDER,
                            prompt = "Arrange to say: 'Nice to meet you, I am Ken':",
                            targetText = "Nice to meet you, I am Ken",
                            sentenceTiles = listOf("はじめまして、", "ケン", "です。"),
                            correctAnswer = "はじめまして、 ケン です。",
                            explanation = "'Hajimemashite' begins a first-time introduction, while 'desu' is the polite copula.",
                            audioText = "はじめまして、ケンです。"
                        ),
                        Exercise(
                            id = "ja_e3",
                            type = ExerciseType.MATCH_WORDS,
                            prompt = "Match the Japanese expressions:",
                            targetText = "Match pairs",
                            correctAnswer = "matched",
                            matchPairs = listOf(
                                MatchPair("ありがとう (Arigatou)", "Thank you"),
                                MatchPair("すみません (Sumimasen)", "Excuse me / Sorry"),
                                MatchPair("はい (Hai)", "Yes"),
                                MatchPair("いいえ (Iie)", "No")
                            ),
                            explanation = "'Sumimasen' is exceptionally versatile: use it to apologize, thank someone, or call a waiter."
                        )
                    )
                )
            )
        )
        return Course("ja", "Japanese", listOf(unit1))
    }

    private fun createGermanCourse(): Course {
        val unit1 = CourseUnit(
            id = "de_u1",
            number = 1,
            title = "Hallo & Kennenlernen",
            subtitle = "Get started with polite German greetings, names, and numbers",
            cefrLevel = CefrLevel.A1,
            colorHex = 0xFFF59E0B,
            iconEmoji = "🥨",
            lessons = listOf(
                Lesson(
                    id = "de_u1_l1",
                    title = "First Steps in Berlin",
                    description = "Guten Tag, Danke, and Bitte",
                    order = 1,
                    exercises = listOf(
                        Exercise(
                            id = "de_e1",
                            type = ExerciseType.MULTIPLE_CHOICE,
                            prompt = "What is the German word for 'Thank you'?",
                            targetText = "Thank you",
                            options = listOf("Danke", "Bitte", "Hallo", "Tschüss"),
                            correctAnswer = "Danke",
                            explanation = "'Danke' means thank you, while 'Bitte' serves as both 'please' and 'you are welcome'.",
                            audioText = "Danke"
                        ),
                        Exercise(
                            id = "de_e2",
                            type = ExerciseType.FILL_BLANKS,
                            prompt = "Complete: 'Guten ___, wie geht es Ihnen?' (Good day, how are you?)",
                            targetText = "Guten ___, wie geht es Ihnen?",
                            options = listOf("Tag", "Morgen", "Abend", "Nacht"),
                            correctAnswer = "Tag",
                            explanation = "'Guten Tag' is the polite daytime greeting throughout Germany, Austria, and Switzerland.",
                            audioText = "Guten Tag, wie geht es Ihnen?"
                        )
                    )
                )
            )
        )
        return Course("de", "German", listOf(unit1))
    }

    private fun createHindiCourse(): Course {
        val unit1 = CourseUnit(
            id = "hi_u1",
            number = 1,
            title = "Namaste & Foundations",
            subtitle = "Learn traditional greetings and simple respectful responses",
            cefrLevel = CefrLevel.A1,
            colorHex = 0xFFF97316,
            iconEmoji = "🪷",
            lessons = listOf(
                Lesson(
                    id = "hi_u1_l1",
                    title = "Respectful Greetings",
                    description = "Namaste, Dhanyavaad, and Aap kaise hain?",
                    order = 1,
                    exercises = listOf(
                        Exercise(
                            id = "hi_e1",
                            type = ExerciseType.MULTIPLE_CHOICE,
                            prompt = "Select the translation for 'Hello' in Hindi:",
                            targetText = "Hello",
                            phonetic = "na-mas-te",
                            options = listOf("नमस्ते (Namaste)", "धन्यवाद (Dhanyavaad)", "अलविदा (Alvida)", "हाँ (Haan)"),
                            correctAnswer = "नमस्ते (Namaste)",
                            explanation = "'Namaste' (नमस्ते) is the traditional greeting, acknowledging respect and welcoming another.",
                            audioText = "नमस्ते"
                        ),
                        Exercise(
                            id = "hi_e2",
                            type = ExerciseType.TRANSLATE,
                            prompt = "Translate 'धन्यवाद' (Dhanyavaad):",
                            targetText = "धन्यवाद",
                            phonetic = "dhan-ya-vaad",
                            options = listOf("Thank you", "Please", "Goodbye", "Excuse me"),
                            correctAnswer = "Thank you",
                            explanation = "'Dhanyavaad' is the formal and respectful word for thank you in Hindi.",
                            audioText = "धन्यवाद"
                        )
                    )
                )
            )
        )
        return Course("hi", "Hindi", listOf(unit1))
    }

    private fun createKoreanCourse(): Course {
        val unit1 = CourseUnit(
            id = "ko_u1",
            number = 1,
            title = "Annyeonghaseyo & Politeness",
            subtitle = "Learn essential polite Hangul phrases and introductions",
            cefrLevel = CefrLevel.A1,
            colorHex = 0xFF8B5CF6,
            iconEmoji = "🥢",
            lessons = listOf(
                Lesson(
                    id = "ko_u1_l1",
                    title = "First Words in Seoul",
                    description = "Hello, Thank you, and Goodbye in polite speech",
                    order = 1,
                    exercises = listOf(
                        Exercise(
                            id = "ko_e1",
                            type = ExerciseType.MULTIPLE_CHOICE,
                            prompt = "What is the polite Korean expression for 'Hello'?",
                            targetText = "Hello (polite)",
                            phonetic = "an-nyeong-ha-se-yo",
                            options = listOf("안녕하세요 (Annyeonghaseyo)", "감사합니다 (Gamsahamnida)", "안녕히 가세요 (Annyeonghi gaseyo)", "네 (Ne)"),
                            correctAnswer = "안녕하세요 (Annyeonghaseyo)",
                            explanation = "'안녕하세요' is the cornerstone polite greeting used when meeting people in Korea.",
                            audioText = "안녕하세요"
                        )
                    )
                )
            )
        )
        return Course("ko", "Korean", listOf(unit1))
    }

    private fun createChineseCourse(): Course {
        val unit1 = CourseUnit(
            id = "zh_u1",
            number = 1,
            title = "Ni Hao & Everyday Mandarin",
            subtitle = "Master tones, basic greetings, and polite manners in Hanzi",
            cefrLevel = CefrLevel.A1,
            colorHex = 0xFFEF4444,
            iconEmoji = "🏮",
            lessons = listOf(
                Lesson(
                    id = "zh_u1_l1",
                    title = "Welcome to Beijing",
                    description = "Ni Hao, Xiexie, and Zaijian",
                    order = 1,
                    exercises = listOf(
                        Exercise(
                            id = "zh_e1",
                            type = ExerciseType.MULTIPLE_CHOICE,
                            prompt = "Select the translation for 'Hello':",
                            targetText = "Hello",
                            phonetic = "nǐ hǎo",
                            options = listOf("你好 (Nǐ hǎo)", "谢谢 (Xièxie)", "再见 (Zàijiàn)", "对不起 (Duìbuqǐ)"),
                            correctAnswer = "你好 (Nǐ hǎo)",
                            explanation = "'你好' (Nǐ hǎo) combines 'you' and 'good' to form the universal greeting.",
                            audioText = "你好"
                        )
                    )
                )
            )
        )
        return Course("zh", "Mandarin Chinese", listOf(unit1))
    }

    private fun createItalianCourse(): Course {
        val unit1 = CourseUnit(
            id = "it_u1",
            number = 1,
            title = "Ciao Roma! First Words",
            subtitle = "Learn musical Italian greetings and cafe culture",
            cefrLevel = CefrLevel.A1,
            colorHex = 0xFF10B981,
            iconEmoji = "🍕",
            lessons = listOf(
                Lesson(
                    id = "it_u1_l1",
                    title = "Greetings & Coffee",
                    description = "Ciao, Buongiorno, Grazie mille",
                    order = 1,
                    exercises = listOf(
                        Exercise(
                            id = "it_e1",
                            type = ExerciseType.MULTIPLE_CHOICE,
                            prompt = "What is the versatile Italian word for both 'Hello' and 'Goodbye'?",
                            targetText = "Hello / Goodbye",
                            options = listOf("Ciao", "Grazie", "Prego", "Buonasera"),
                            correctAnswer = "Ciao",
                            explanation = "'Ciao' is the beloved informal greeting used between friends throughout Italy.",
                            audioText = "Ciao"
                        )
                    )
                )
            )
        )
        return Course("it", "Italian", listOf(unit1))
    }

    private fun createPortugueseCourse(): Course {
        val unit1 = CourseUnit(
            id = "pt_u1",
            number = 1,
            title = "Olá & Boas-Vindas",
            subtitle = "Warm greetings from Rio to Lisbon",
            cefrLevel = CefrLevel.A1,
            colorHex = 0xFF10B981,
            iconEmoji = "⚽",
            lessons = listOf(
                Lesson(
                    id = "pt_u1_l1",
                    title = "First Expressions",
                    description = "Olá, Obrigado, and Tudo bem?",
                    order = 1,
                    exercises = listOf(
                        Exercise(
                            id = "pt_e1",
                            type = ExerciseType.MULTIPLE_CHOICE,
                            prompt = "How do you say 'Hello' in Portuguese?",
                            targetText = "Hello",
                            options = listOf("Olá", "Obrigado", "Por favor", "Adeus"),
                            correctAnswer = "Olá",
                            explanation = "'Olá' is the friendly standard hello in both Brazilian and European Portuguese.",
                            audioText = "Olá"
                        )
                    )
                )
            )
        )
        return Course("pt", "Portuguese", listOf(unit1))
    }

    private fun createRussianCourse(): Course {
        val unit1 = CourseUnit(
            id = "ru_u1",
            number = 1,
            title = "Privet & First Cyrillic",
            subtitle = "Basic Russian greetings and polite vocabulary",
            cefrLevel = CefrLevel.A1,
            colorHex = 0xFF0284C7,
            iconEmoji = "🪆",
            lessons = listOf(
                Lesson(
                    id = "ru_u1_l1",
                    title = "Friendly Introductions",
                    description = "Привет, Спасибо, and Пожалуйста",
                    order = 1,
                    exercises = listOf(
                        Exercise(
                            id = "ru_e1",
                            type = ExerciseType.MULTIPLE_CHOICE,
                            prompt = "Select the informal Russian greeting for 'Hi / Hello':",
                            targetText = "Hi",
                            phonetic = "pree-vyet",
                            options = listOf("Привет (Privet)", "Спасибо (Spasibo)", "Пока (Poka)", "Да (Da)"),
                            correctAnswer = "Привет (Privet)",
                            explanation = "'Привет' (Privet) is the friendly everyday greeting used among peers.",
                            audioText = "Привет"
                        )
                    )
                )
            )
        )
        return Course("ru", "Russian", listOf(unit1))
    }

    private fun createArabicCourse(): Course {
        val unit1 = CourseUnit(
            id = "ar_u1",
            number = 1,
            title = "Marhaban & Cultural Courtesy",
            subtitle = "Beautiful classical Arabic greetings and expressions of hospitality",
            cefrLevel = CefrLevel.A1,
            colorHex = 0xFF059669,
            iconEmoji = "🕌",
            lessons = listOf(
                Lesson(
                    id = "ar_u1_l1",
                    title = "Greetings of Peace",
                    description = "Marhaban and Shukran",
                    order = 1,
                    exercises = listOf(
                        Exercise(
                            id = "ar_e1",
                            type = ExerciseType.MULTIPLE_CHOICE,
                            prompt = "Select the Arabic word for 'Thank you':",
                            targetText = "Thank you",
                            phonetic = "shuk-ran",
                            options = listOf("شكراً (Shukran)", "مرحباً (Marhaban)", "نعم (Na'am)", "لا (La)"),
                            correctAnswer = "شكراً (Shukran)",
                            explanation = "'شكراً' (Shukran) is recognized and used across all Arabic-speaking countries.",
                            audioText = "شكراً"
                        )
                    )
                )
            )
        )
        return Course("ar", "Arabic", listOf(unit1))
    }

    private fun createGenericCourse(langId: String): Course {
        val unit1 = CourseUnit(
            id = "${langId}_u1",
            number = 1,
            title = "Core Foundations",
            subtitle = "Build conversational vocabulary and daily phrases",
            cefrLevel = CefrLevel.A1,
            colorHex = 0xFF10B981,
            iconEmoji = "🌍",
            lessons = listOf(
                Lesson(
                    id = "${langId}_u1_l1",
                    title = "Essential Phrases",
                    description = "Key greetings, etiquette, and polite words",
                    order = 1,
                    exercises = listOf(
                        Exercise(
                            id = "${langId}_e1",
                            type = ExerciseType.MULTIPLE_CHOICE,
                            prompt = "Choose the universal greeting to begin speaking:",
                            targetText = "Hello",
                            options = listOf("Hello / Salutation", "Goodbye", "Please", "Thank you"),
                            correctAnswer = "Hello / Salutation",
                            explanation = "Always start a conversation with a polite greeting.",
                            audioText = "Hello"
                        )
                    )
                )
            )
        )
        return Course(langId, langId.uppercase(), listOf(unit1))
    }
}
