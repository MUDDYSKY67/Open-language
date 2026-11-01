package com.example

import com.example.data.audio.PronunciationEvaluator
import com.example.data.model.SupportedLanguages
import com.example.data.repository.LanguageCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testSupportedLanguagesCatalog() {
        val languages = SupportedLanguages.list
        assertTrue(languages.size >= 15)
        assertNotNull(SupportedLanguages.getById("es"))
        assertNotNull(SupportedLanguages.getById("ja"))
        assertNotNull(SupportedLanguages.getById("fr"))
        assertNotNull(SupportedLanguages.getById("de"))
        assertNotNull(SupportedLanguages.getById("hi"))
    }

    @Test
    fun testCourseCurriculumGeneration() {
        val spanishCourse = LanguageCatalog.getCourseForLanguage("es")
        assertEquals("Spanish", spanishCourse.targetLanguageName)
        assertTrue(spanishCourse.units.isNotEmpty())

        val firstUnit = spanishCourse.units.first()
        assertTrue(firstUnit.lessons.isNotEmpty())

        val firstLesson = firstUnit.lessons.first()
        assertTrue(firstLesson.exercises.isNotEmpty())
    }

    @Test
    fun testPronunciationEvaluation() {
        val matchResult = PronunciationEvaluator.evaluate("¡Hola!", "Hola")
        assertTrue(matchResult.accuracyScore >= 90)
        assertTrue(matchResult.isPassed)

        val poorResult = PronunciationEvaluator.evaluate("Goodbye", "Hola")
        assertTrue(poorResult.accuracyScore < 50)
    }
}
