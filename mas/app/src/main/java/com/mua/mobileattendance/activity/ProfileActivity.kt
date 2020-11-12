package com.mua.mobileattendance.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.mua.mobileattendance.BaseActivity
import com.mua.mobileattendance.R
import com.mua.mobileattendance.databinding.ActivityProfileBinding
import com.mua.mobileattendance.viewmodel.ProfileViewModel

class ProfileActivity : BaseActivity() {

    private lateinit var mBinding: ActivityProfileBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
        initDpUpload()

        setMinimizable(true)
    }

    fun initDpUpload() {
        mBinding.ivDp.setOnClickListener {
            if (viewModel.editable.value!!) {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, 2222)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 2222) {
            mBinding.ivDp.setImageURI(data?.data)
        }
    }


    fun init() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        mBinding.profile = viewModel
        mBinding.lifecycleOwner = this


        initFromChild(viewModel)

        try {
            viewModel.setUserIdAndLoad(intent.extras!!.getLong("userid"))
        } catch (e: Exception) {
            viewModel.setUserIdAndLoad()
        }
        //setFullScreen()
    }


}