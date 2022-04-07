package com.example.tocasencillo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tocasencillo.databinding.ActivityAssemblyBinding

class AssemblyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAssemblyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssemblyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mySong: String = intent.getStringExtra("songName").toString()

        binding.myAssembledSong.text = mySong


    //TODO:
        //Search song (for name + id)
        //Search all cancion_fragmento with id_song
        // _id_fragmento, tipo TEXT -> Create Fragment (if (tipo = "tipo"))
        //onCreate Fragment -> select * from "tipoQueSea" and .text in all fields


    }
}