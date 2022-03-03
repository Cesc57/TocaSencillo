package com.example.tocasencillo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tocasencillo.databinding.ActivityEditorBinding

class EditorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}