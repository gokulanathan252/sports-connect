package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.SportsConnectRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Sports Connect", appName)
    }

    @Test
    fun `repository initial sports and match score update`() {
        val repo = SportsConnectRepository()
        assertTrue(repo.sports.value.size >= 20)

        val firstMatch = repo.matches.value.first()
        val initialScore = firstMatch.teamA.score

        repo.updateScore(firstMatch.id, isTeamA = true, delta = 1)
        val updatedMatch = repo.matches.value.find { it.id == firstMatch.id }
        assertNotNull(updatedMatch)
        assertEquals(initialScore + 1, updatedMatch!!.teamA.score)
    }
}
