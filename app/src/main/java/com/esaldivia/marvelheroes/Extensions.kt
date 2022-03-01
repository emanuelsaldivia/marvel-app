package com.esaldivia.marvelheroes

import com.esaldivia.marvelheroes.common.Constants
import toothpick.Scope
import toothpick.ktp.KTP

/**
 * Opens a general scope for the application
 */
fun KTP.openMarvelScope(): Scope = openScope(Constants.MARVEL_APP)

fun KTP.injectSubScope(obj: Any) {
    val scope = openMarvelScope().openSubScope(obj.javaClass.name).inject(this)
    closeScope(scope)
}