package com.takehomeassessmentpesto

import com.takehomeassessmentpesto.utils.Constants
import com.takehomeassessmentpesto.view.AddEditTaskActivity
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun checkFormValidation(){
        val result = Constants.checkValidation("")
        assertTrue(result)
    }
}