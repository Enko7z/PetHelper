package com.example.pethelper

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pethelper.databinding.ActivityMainScreenBinding


class ActivityMainScreen : AppCompatActivity() {
    private lateinit var binding: ActivityMainScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaseFragment(MainPage_frag())

//        Нижний бар навигации
        binding.bottomNavigationView2.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.calendar -> replaseFragment(Calendar_frag())
                R.id.menu -> replaseFragment(MainPage_frag())
                R.id.profile -> replaseFragment(Profile_frag())

                else -> {


                }

            }
            true
        }
        binding.bottomNavigationView2.setSelectedItemId(R.id.menu);

    }

    private fun replaseFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}
