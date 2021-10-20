package com.example.ownerapp.activities

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.ownerapp.Adapters.ViewPagerAdapter
import com.example.ownerapp.Fragments.userTabFrags.MembersTabFragment
import com.example.ownerapp.Fragments.userTabFrags.PlanExpiredFragment
import com.example.ownerapp.R
import com.example.ownerapp.databinding.ActivityViewUsersBinding

class ViewUsers : AppCompatActivity() {
    lateinit var binding: ActivityViewUsersBinding
    private val TAG = "ViewUsers"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)


        init()


    }

    private fun init() {
        //Toolbar stuff
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorprimarymain)

        //ViewPager and TabLay
        binding.tabLayUsers.setupWithViewPager(binding.viewPagerUsers)

        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFrag(MembersTabFragment(), "Members")
        viewPagerAdapter.addFrag(PlanExpiredFragment(), "Non-Paid")
        binding.viewPagerUsers.adapter = viewPagerAdapter
    }
}