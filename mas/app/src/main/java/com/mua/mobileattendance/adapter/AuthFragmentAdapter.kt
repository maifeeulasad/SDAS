package com.mua.mobileattendance.adapter

import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class AuthFragmentAdapter(
    fm: FragmentManager,
    private val fragments: List<Fragment>
) :
    BaseFragmentAdapter(fm, fragments) {

    @NonNull
    override fun getItem(position: Int): Fragment {
        return fragments.get(position)
    }

    override fun getCount(): Int {
        return fragments.size
    }

}