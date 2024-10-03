package com.kel11.inote

//import kelas yang diperlukan
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //memanggil metode oncreate dari kelas utama
        super.onCreate(savedInstanceState)

        //penetapal layout activity dari xml
        setContentView(R.layout.activity_main)

        //pergantian fragment utama dgn new fragment
        replaceFragment(HomeFragment.newInstance(),false)

    }

    // Metode untuk mengganti fragment yang ditampilkan di dalam FrameLayout
    fun replaceFragment(fragment:Fragment, istransition:Boolean){
        val fragmentTransition = supportFragmentManager.beginTransaction()

        if (istransition){
            fragmentTransition.setCustomAnimations(android.R.anim.slide_out_right,android.R.anim.slide_in_left)
        }
        fragmentTransition.add(R.id.frame_layout,fragment).addToBackStack(fragment.javaClass.simpleName).commit()
    }

    // Metode yang dipanggil ketika tombol back ditekan
    override fun onBackPressed() {
        // Memanggil metode onBackPressed dari kelas induk
        super.onBackPressed()

        // Mendapatkan daftar fragment yang saat ini ditampilkan
        val fragments = supportFragmentManager.fragments

        // Jika tidak ada fragment lagi, keluar dari aplikasi
        if (fragments.size == 0){
            finish()
        }
    }
}