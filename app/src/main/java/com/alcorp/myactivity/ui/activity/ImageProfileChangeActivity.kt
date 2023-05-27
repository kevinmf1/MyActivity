package com.alcorp.myactivity.ui.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.alcorp.myactivity.R
import com.alcorp.myactivity.database.repository.ProfileRepository
import com.alcorp.myactivity.databinding.ActivityImageProfileChangeBinding
import com.alcorp.myactivity.tools.intToBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImageProfileChangeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageProfileChangeBinding
    private lateinit var profileRepository: ProfileRepository
    private lateinit var profileId: String
    private var currentImageResId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageProfileChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeViews()
        setupRepositories()
        setupObservers()
        setupListeners()
    }

    private fun initializeViews() {
        profileRepository = ProfileRepository(application)
    }

    private fun setupRepositories() {
        profileRepository = ProfileRepository(application)
    }

    private fun setupObservers() {
        profileRepository.getProfile().observe(this) { profile ->
            profile?.let {
                binding.profileImagePreview.setImageBitmap(intToBitmap(this, it.photo ?: 0))
            }
        }
    }

    private fun setupListeners() {
        profileId = intent.getStringExtra(EXTRA_PROFILE_ID).toString()

        binding.backProfilePictureEdit.setOnClickListener {
            finish()
        }

        binding.saveProfilePictureEdit.setOnClickListener {
            showSaveConfirmationDialog()
        }

        val imageViews = arrayOf(
            binding.manImage1, binding.manImage2, binding.manImage3, binding.manImage4,
            binding.womanImage1, binding.womanImage2, binding.womanImage3, binding.womanImage4
        )
        for (imageView in imageViews) {
            imageView.setOnClickListener {
                setOnClickAction(imageView, R.drawable.background_imageview_choose)
            }
        }
    }

    private fun showSaveConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.confirmation_activity_title))
            .setMessage(getString(R.string.profile_picture_save_message))
            .setPositiveButton(getString(R.string.profile_btnSave)) { dialog, _ ->
                dialog.dismiss()
                savePicture()
            }
            .setNegativeButton(getString(R.string.profile_btnCancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun savePicture() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                profileRepository.saveImageById(profileId.toInt(), currentImageResId)
            }
        }
        Toast.makeText(
            this,
            getString(R.string.profile_picture_success_edit_message),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setOnClickAction(imageView: ImageView, backgroundResId: Int) {
        resetBackgrounds()
        currentImageResId = getImageResId(imageView)
        binding.profileImagePreview.setImageResource(getImageResId(imageView))
        imageView.setBackgroundResource(backgroundResId)
    }

    private fun resetBackgrounds() {
        val imageViews = arrayOf(
            binding.manImage1, binding.manImage2, binding.manImage3, binding.manImage4,
            binding.womanImage1, binding.womanImage2, binding.womanImage3, binding.womanImage4
        )
        for (imageView in imageViews) {
            imageView.setBackgroundResource(R.drawable.background_imageview)
        }
    }

    private fun getImageResId(imageView: ImageView): Int {
        return when (imageView) {
            binding.manImage1 -> R.drawable.man_pic1
            binding.manImage2 -> R.drawable.man_pic2
            binding.manImage3 -> R.drawable.man_pic3
            binding.manImage4 -> R.drawable.man_pic4
            binding.womanImage1 -> R.drawable.woman_pic1
            binding.womanImage2 -> R.drawable.woman_pic2
            binding.womanImage3 -> R.drawable.woman_pic3
            binding.womanImage4 -> R.drawable.woman_pic4
            else -> throw IllegalArgumentException("ImageView not recognized")
        }
    }

    companion object {
        const val EXTRA_PROFILE_ID = "extra_profile_id"
    }
}