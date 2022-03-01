package com.esaldivia.marvelheroes.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.esaldivia.marvelheroes.databinding.ActivityMainBinding
import com.esaldivia.marvelheroes.di.NetworkModule
import com.esaldivia.marvelheroes.openMarvelScope
import toothpick.ktp.KTP

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KTP.openMarvelScope()
            .installModules(NetworkModule())

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}