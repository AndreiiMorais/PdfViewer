package com.example.pdfviewer

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.example.pdfviewer.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.shareButton.setOnClickListener {
            sharePdfFromIntentData()
        }

        if (intent.action == Intent.ACTION_VIEW) {
            val pdfUri: Uri? = intent.data
            binding.pdfview.fromUri(pdfUri).load()
        }
    }

    private fun sharePdfFromIntentData(){
        val uri = intent?.data
        if(uri == null){
            Toast.makeText(this, "Nenhum arquivo pra compartilhar", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val tempFile = File(cacheDir, "PdfViewerTemp.pdf")

            contentResolver.openInputStream(uri).use { input ->
                FileOutputStream(tempFile).use { output ->
                    input?.copyTo(output)
                }
            }

            val contentUri = FileProvider.getUriForFile(
                this, "${packageName}.provider",
                tempFile
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, contentUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            startActivity(Intent.createChooser(shareIntent, "Compartilhar PDF"))
        }catch (e: Exception){
            Toast.makeText(this, "Erro ao compartilhar PDF", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }
}

