package xyz.heart.sms.util

import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Test
import xyz.heart.sms.MessengerRobolectricSuite
import xyz.heart.sms.shared.util.BlacklistUtils

class BlacklistUtilsTest : MessengerRobolectricSuite() {

    @Test
    fun blacklistsMatch_withSpecialCharacters() {
        assertTrue(BlacklistUtils.numbersMatch("5159911493", "5159911493"))
        assertTrue(BlacklistUtils.numbersMatch("+15159911493", "5159911493"))
        assertTrue(BlacklistUtils.numbersMatch("+15159911493", "515 991 1493"))
        assertTrue(BlacklistUtils.numbersMatch("+15159911493", "+1 (515) 991 1493"))
        assertTrue(BlacklistUtils.numbersMatch("+15159911493", "+1 (515) 991-1493"))
        assertTrue(BlacklistUtils.numbersMatch("+15159911493", "515-991-1493"))
        assertTrue(BlacklistUtils.numbersMatch("+1 (515) 991-1493", "515-991-1493"))
        assertTrue(BlacklistUtils.numbersMatch("515991-1493", "991-1493"))
        assertTrue(BlacklistUtils.numbersMatch("991-1493", "515-991-1493"))
    }

    @Test
    fun blacklistsMatch_shortNumbersMatchingLength() {
        assertTrue(BlacklistUtils.numbersMatch("55544", "55544"))
        assertFalse(BlacklistUtils.numbersMatch("655544", "55544"))
    }

    @Test
    fun blacklistsDoNotMatch() {
        assertFalse(BlacklistUtils.numbersMatch("5159911493", "5154224558"))
        assertFalse(BlacklistUtils.numbersMatch("(515) 9911493", "5154224558"))
        assertFalse(BlacklistUtils.numbersMatch("24558", "5154224558"))
    }

    @Test
    fun blacklistsForNonNumericNumbers() {
        assertTrue(BlacklistUtils.numbersMatch("ABCDEF", "ABCD-EF"))
    }

    @Test
    fun isNullOrBlank() {
        val value1: String? = null
        assertTrue(value1.isNullOrBlank())

        val value2: String? = ""
        assertTrue(value2.isNullOrBlank())

        val value3: String? = " "
        assertTrue(value3.isNullOrBlank())

        val value4: String? = " a"
        assertFalse(value4.isNullOrBlank())
    }
}