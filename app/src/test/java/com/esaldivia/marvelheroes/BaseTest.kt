package com.esaldivia.marvelheroes

import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import toothpick.ktp.KTP

@RunWith(JUnit4::class)
abstract class BaseTest {
    @Before
    fun __setUp() {
        KTP.openApplicationScope().inject(this)
    }

    @After
    fun __tearDown() {
        KTP.closeScope("root")
    }
}