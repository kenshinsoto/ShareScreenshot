package com.kristianconk.appscreenshot

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kristianconk.appscreenshot.databinding.ActivityMainBinding
import com.kristianconk.screenshare.Share

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnScreenshare.setOnClickListener {
            Share.shareScreenshot(
                this,
                "te comparto mi captura",
                binding.root
            )
        }
    }
}
