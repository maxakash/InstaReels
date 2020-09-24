package com.weaponoid.instareels.views

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.weaponoid.instareels.R
import com.weaponoid.instareels.adapters.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val tabNames = arrayListOf<String>("Home", "Downloads")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.elevation = 0F

        checkPermission()

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.openInsta -> {
                //sa
            }
            R.id.settings -> {

                startActivity(Intent(this, Settings::class.java))
            }
        }


        return true
    }

    private fun setPager() {
        view_pager.adapter =
            ViewPagerAdapter(this)

        TabLayoutMediator(
            tabs, view_pager
        ) { tab, position ->
            tab.text = tabNames[position]

        }.attach()

    }

    private fun checkPermission() {

        if (Build.VERSION.SDK_INT >= 23) {
            val checkSelfPermission = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
            } else {
                setPager()
            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        println(requestCode)
        println(grantResults)
        when (requestCode) {

            1 ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setPager()

                } else {
                    checkPermission()
                    Toast.makeText(this, "Grant permission to use this app.", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }


}