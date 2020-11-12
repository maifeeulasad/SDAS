package com.mua.mobileattendance.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.mua.mobileattendance.BaseActivity
import com.mua.mobileattendance.R
import com.mua.mobileattendance.adapter.AuthFragmentAdapter
import com.mua.mobileattendance.databinding.ActivityAuthBinding
import com.mua.mobileattendance.fragment.JoinFragment
import com.mua.mobileattendance.fragment.LoginFragment
import com.mua.mobileattendance.viewmodel.AuthViewModel
import java.util.*

class AuthActivity : BaseActivity() {

    private lateinit var mBinding: ActivityAuthBinding
    private lateinit var viewModel: AuthViewModel
    private lateinit var adapter: AuthFragmentAdapter

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
        setMinimizable(true)
        setSecureScreen()
    }

    fun init() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_auth)
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        mBinding.auth = viewModel
        mBinding.lifecycleOwner = this

        setTitle("Login or Join")

        initTabs()
        onJoin()
        onLogin()
        initFromChild(viewModel)
    }

    fun initTabs() {
        tabLayout = mBinding.tlLayout
        viewPager = mBinding.vpPager

        val fragments: List<androidx.fragment.app.Fragment> =
            Arrays.asList(LoginFragment(), JoinFragment())

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.login)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.join)))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        adapter =
            AuthFragmentAdapter(supportFragmentManager, fragments)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }
        })
    }

    fun onJoin() {
        viewModel.tabIndex.observe(this, androidx.lifecycle.Observer {
            tabLayout.getTabAt(it)?.select()
            viewPager.setAdapter(adapter)
        })

    }

    fun onLogin() {
        viewModel.startHome.observe(this, androidx.lifecycle.Observer {
            if (it == true)
                startActivityAndClear(applicationContext, HomeActivity::class.java)
        })
    }

}