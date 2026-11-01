# OPEN LANGUAGE — Multilingual Learning Architecture & Implementation Plan

OPEN LANGUAGE is a multi-language learning Android application that unites the gamified progression of Duolingo, the clean curriculum scaffolding of Airlearn, and the friendly mascot charm of LingoMouse into a unified experience featuring **Poly the colorful Parrot**.

---

## User Review & Critical Decisions

> [!IMPORTANT]
> **Summary of Confirmed Choices from Phase 1:**
> - **Mascot Identity**: Poly the colorful Parrot — a cheerful, multilingual feathered guide who celebrates milestones, reacts to quiz streaks, and guides conversations.
> - **Language Catalogue**: All catalog languages (English, Spanish, French, German, Japanese, Korean, Mandarin Chinese, Hindi, Arabic, Italian, Russian, Portuguese, Turkish, Bengali, Urdu) unlocked and fully accessible from day one.
> - **Voice & Audio Pipeline**: Dual architecture combining instant on-device Text-to-Speech (TTS) with phonetic breakdown alongside Gemini AI conversational voice tutor and pronunciation evaluator.

---

## 1. Overview & Core Concept

### What It Does
OPEN LANGUAGE delivers a language acquisition platform supporting 15+ world languages with real, culturally authentic curricula spanning CEFR levels (Beginner A1 through Advanced C2). Learners master reading, writing, listening, speaking, grammar, and vocabulary through bite-sized interactive lessons, 10 game modes, an AI tutor, and an SRS (Spaced Repetition System) vocabulary notebook.

### Target Audience & Persona
- **Casual & Hobby Learners**: Retaining motivation through daily streaks, bite-sized 5-minute sessions, and games.
- **Academic & Professional Learners**: Targeting conversational fluency, travel phrases, business idioms, and grammar depth.
- **Auditory & Speech-Focused Learners**: Practicing real dialogue simulations, phonetic playback speed tuning, and pronunciation assessment.

### Key Value Proposition
A free-flowing, fully unlocked language learning journey with zero paywalls, offline-capable local curriculum persistence, real-time AI conversation coaching in both native and target languages, and tactile game mechanics that make daily repetition rewarding.

---

## 2. User Experience & Visual Design

```
┌──────────────────────────────────────────────────────────────────┐
│                   TOP APP BAR / STATS PILL                      │
│  [Poly 🦜]  🔥 7 Day Streak    💎 450 Gems    ❤️ 5/5 Hearts       │
├──────────────────────────────────────────────────────────────────┤
│                                                                  │
│  CURRENT COURSE: 🇪🇸 Spanish (Beginner A1)     [Switch Track ▾]  │
│                                                                  │
│  ┌────────────────────────────────────────────────────────────┐  │
│  │ UNIT 1: Essential Greetings & Introductions                │  │
│  │ "¡Hola! ¿Cómo estás?" • 4 of 6 Lessons Completed           │  │
│  │ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓░░░░░░░░ 66%                                 │  │
│  └────────────────────────────────────────────────────────────┘  │
│                                                                  │
│                     ( Lesson 1: Basics ✓ )                       │
│                           \                                      │
│                            ( Lesson 2: Greetings ✓ )             │
│                                   /                              │
│                    ( Lesson 3: Poly's Cafe 🦜 )                  │
│                           \                                      │
│                            ( Lesson 4: In Progress ⚡ )          │
│                                   /                              │
│                     [ ⭐ Unit 1 Checkpoint ]                     │
│                                                                  │
├──────────────────────────────────────────────────────────────────┤
│  [ 🏠 Home ]  [ 📚 Learn ]  [ 🎮 Games ]  [ 🗣️ Practice ]  [ 👤 Profile ]
└──────────────────────────────────────────────────────────────────┘
```

### Visual Identity & Theme
- **Theme Archetype**: Vibrant Playful Material 3 — saturated candy accents against clean elevated surfaces, friendly pill-shaped chips, bouncy spring animations, and tactile 3D-styled action buttons.
- **Color Tokens**:
  - `Primary Emerald`: `#10B981` (Success, active lesson nodes, progress fills)
  - `Poly Azure`: `#0284C7` (Brand accents, audio playback, grammar highlights)
  - `Poly Amber / Gold`: `#F59E0B` (XP points, coin balances, streak flame)
  - `Ruby Heart`: `#EF4444` (Lives counter, error states)
  - `Parrot Plum`: `#8B5CF6` (AI Language Tutor dialogues, mystery chests)
  - `Surface Light`: `#F8FAFC` / `Surface Dark`: `#0F172A`
- **Mascot Styling**: Poly the Parrot rendered with expressive visual vector badges (Happy, Thinking, Cheering, Surprised, Confident) integrated into dialogue speech bubbles, feedback bottom sheets, and empty states.
- **Typography**: Clean modern display typography with generous letter spacing, bold numerals for streaks/counters, and high-legibility sans-serif for multi-script language texts (Latin, Devanagari, Kanji/Kana, Hangul, Arabic).

### Main Navigation & Views
1. **Home**: Daily streak flame, personalized daily goals (XP/lessons/minutes), continue current unit path, Word & Phrase of the Day card, quick practice launcher.
2. **Learn**: Interactive serpentine curriculum path with Units, Chapters, interactive nodes, checkpoints, CEFR level selector (A1–C2), and language switcher modal.
3. **Games**: 10 game modes:
   - Word Matching
   - Picture Guessing
   - Sentence Builder
   - Listening Challenge
   - Pronunciation Challenge
   - Speed Vocabulary
   - Translation Battle
   - Daily Quiz
   - Memory Cards
   - Conversation Challenge
4. **Practice**: Voice salon with speech-to-text pronunciation scoring, AI Tutor dialogue roleplay, grammar reference library, and Spaced Repetition (SRS) vocabulary review.
5. **Profile**: User stats (Total XP, Streak, Accuracy %, Time Spent), badges/achievements showcase, daily tasks tracker, language preferences, and audio/theme settings.

---

## 3. Key Product Decisions & Trade-Offs

| Decision | Chosen Approach | Rationale & Trade-Off |
| :--- | :--- | :--- |
| **Course Data & Offline Engine** | Bundled comprehensive multi-language JSON repositories cached in Room database | Enables instant, reliable lesson loads with zero latency and full offline operation, without relying on unstable external scrapers. |
| **Voice & Speech Engine** | Dual engine: native Android `TextToSpeech` with rate/pitch tuning + Gemini AI logic for speech evaluation and dialogue roleplay | Guarantees instant voice playback on any device while unlocking conversational AI tutoring. |
| **Lesson Question Mechanics** | 8 distinct interactive question types (Multiple Choice, Cloze Fill-in, Match Pairs, Sentence Tile Builder, Audio Listen & Pick, Speech Echo, Picture Prompt, Reverse Translate) | Prevents monotony and ensures balanced testing of reading, listening, writing, and speaking skills. |
| **Mascot Integration** | Declarative vector & Canvas-drawn mascot system with dynamic mood states (Idle, Celebrating, Encouraging, Puzzled) | Guarantees lightweight asset footprints, crisp resolution on all DPIs, and zero external image loading failures. |
| **Gamification Mechanics** | Heart/life protection system, streak freeze saves, XP multiplier leagues, and unlockable badges | Drives retention without punishing genuine mistakes (users can practice in Games/Review to refill hearts). |

---

## 4. Technical Architecture & Data Strategy

```
┌────────────────────────────────────────────────────────────────────────┐
│                          PRESENTATION LAYER                            │
│  MainActivity ─── OpenLanguageNavHost                                 │
│  ├── HomeScreen          (Daily path, streak, goals, stats pill)       │
│  ├── LearnScreen         (Serpentine curriculum map, unit chapters)   │
│  ├── LessonScreen        (8 interactive exercise types, instant review)│
│  ├── GamesScreen         (10 playable gamified training challenges)    │
│  ├── PracticeScreen      (AI Tutor dialogue, SRS review, pronunciation)│
│  ├── VocabularyScreen    (Notebook, search, flashcard SRS intervals)   │
│  └── ProfileScreen       (Badges, settings, theme toggle, stats)       │
└────────────────────────────────────┬───────────────────────────────────┘
                                     │ StateFlow / Actions
┌────────────────────────────────────▼───────────────────────────────────┐
│                           VIEWMODEL LAYER                              │
│  ├── AppViewModel (Global profile, active course, streak, hearts, gems)│
│  ├── LessonViewModel (Exercise progression, answer validation, scoring)│
│  ├── GamesViewModel (Game timers, score streaks, leaderboard calculation│
│  ├── AiTutorViewModel (Gemini conversation session, grammar breakdown) │
│  └── VocabularyViewModel (SRS scheduler, word notebook, filters)       │
└────────────────────────────────────┬───────────────────────────────────┘
                                     │
┌────────────────────────────────────▼───────────────────────────────────┐
│                        DATA & PERSISTENCE LAYER                        │
│  ├── OpenLanguageDatabase (Room SQLite persistence)                    │
│  │   ├── UserProfileDao (XP, coins, hearts, streak, level, settings)  │
│  │   ├── CourseProgressDao (Unit/Lesson completion, high scores)      │
│  │   └── VocabularyDao (Saved words, SRS box, next review timestamp)  │
│  ├── LanguageCatalogRepository (Structured curricula for 15+ languages│
│  ├── AudioVoiceManager (Android TextToSpeech + pitch/rate/locale)      │
│  └── FirebaseAiTutorService (Gemini 3.8 Flash for AI tutor chats)      │
└────────────────────────────────────────────────────────────────────────┘
```

### Core Entities & State Structures
- **Language**: ID, Name, Native Name, Flag Emoji, Directionality (LTR/RTL), Voice Locale.
- **LessonNode**: Unit ID, Title, Description, CEFR Level, Order, XP Reward, Question Set.
- **Exercise**: Type (`MULTIPLE_CHOICE`, `FILL_BLANKS`, `SENTENCE_BUILDER`, `MATCH_PAIRS`, `LISTEN_SELECT`, `PRONUNCIATION`, `PICTURE_GUESS`, `TRANSLATE`), Prompt, Target Sentence, Options, Correct Answer, Explanation, Audio Text.
- **SavedWord**: Word, Translation, Language, Phonetic, Example Sentence, SRS Interval Level (1–5), Last Reviewed Date, Next Review Date.
- **DailyTask**: ID, Title, Icon, Target Count, Current Count, Reward XP/Coins, Completed.
- **AchievementBadge**: ID, Name, Description, Tier, Icon, Unlocked, Progress.

---

## 5. Next Steps Post-Approval
Upon receiving user confirmation, execution will proceed with:
1. Setting application metadata name and Android strings resource to "OPEN LANGUAGE".
2. Establishing the UI theme tokens, Poly mascot vector components, and shared interactive widgets.
3. Building the complete language curriculum data models and multi-language catalog across 15+ languages.
4. Implementing the Room database layer and repositories for user progress, vocabulary notebook, and game stats.
5. Implementing the 5 main navigation destinations, the 8 lesson question exercise types, and the 10 game modes.
6. Integrating the dual audio engine (TextToSpeech + Gemini AI Tutor) and conducting end-to-end compilation checks.
