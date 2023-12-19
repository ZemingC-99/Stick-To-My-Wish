package com.example.StickToMyWish

import org.junit.Test
import java.time.LocalDateTime


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val threeDayAgo = LocalDateTime.now().plusDays(0).getMonth().name.substring(0,3)
        println(threeDayAgo)
    }
}