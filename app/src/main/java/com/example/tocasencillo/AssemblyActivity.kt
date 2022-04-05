package com.example.tocasencillo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tocasencillo.databinding.ActivityAssemblyBinding

class AssemblyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAssemblyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssemblyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mySong: String = intent.getStringExtra("songName").toString()

        binding.myAssembledSong.text = mySong

    }
}