package com.example.pdfviewer

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.pdfviewer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.floatingActionButton.setOnClickListener{
            launcher.launch("application/pdf")
        }

        if (intent.action == Intent.ACTION_VIEW) {
            val pdfUri: Uri? = intent.data
            binding.pdfview.fromUri(pdfUri).load()
        }
    }

    private val launcher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ){uri ->
        uri?.let {
            binding.pdfview.fromUri(it).load()
        }
    }
}