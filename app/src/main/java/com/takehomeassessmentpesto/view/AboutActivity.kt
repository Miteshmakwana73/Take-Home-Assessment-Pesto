package com.takehomeassessmentpesto.view

import android.os.Bundle
import com.takehomeassessmentpesto.core.BaseActivity
import com.takehomeassessmentpesto.databinding.ActivityAboutBinding
import com.takehomeassessmentpesto.utils.Constants

class AboutActivity : BaseActivity() {
    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgClose.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.tvAbout.text = Constants.ABOUT
    }
}