package com.example.ownerapp.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val listFrag = ArrayList<Fragment>()
    private val listFragTitle = ArrayList<String>()

    override fun getCount() = listFrag.size

    override fun getItem(position: Int): Fragment = listFrag[position]

    override fun getPageTitle(position: Int): CharSequence = listFragTitle[position]

    fun addFrag(fragment: Fragment,title:String){
        listFrag.add(fragment)
        listFragTitle.add(title)
    }
}