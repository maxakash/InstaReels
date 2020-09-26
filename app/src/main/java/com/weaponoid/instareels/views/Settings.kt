package com.weaponoid.instareels.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.weaponoid.instareels.R
import com.weaponoid.instareels.viewmodels.SettingsViewModel

class Settings : AppCompatActivity() {
    private lateinit var viewModel: SettingsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
    }

    fun onClick(view: View) {

        when (view.id) {
            R.id.feedback -> {
                val addressees = arrayOf("woocommtech@gmail.com")
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:")
                intent.putExtra(Intent.EXTRA_EMAIL, addressees)
                intent.putExtra(Intent.EXTRA_SUBJECT, "Regarding Insta Reels App")
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
            }

            R.id.rate -> {
                viewModel.rateApp(this)
            }

            R.id.privacy -> {
                viewModel.privacy(this)
            }

            R.id.disclaimer -> {
                viewModel.disclaimer(this)
            }

        }
    }
}