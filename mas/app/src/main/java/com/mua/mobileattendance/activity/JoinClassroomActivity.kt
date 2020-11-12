package com.mua.mobileattendance.activity

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mua.mobileattendance.BaseActivity
import com.mua.mobileattendance.R
import com.mua.mobileattendance.databinding.ActivityJoinClassroomBinding
import com.mua.mobileattendance.viewmodel.ClassroomViewModel

class JoinClassroomActivity : BaseActivity() {

    private lateinit var mBinding: ActivityJoinClassroomBinding
    private lateinit var viewModel: ClassroomViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
        setMinimizable(true)
    }


    fun init() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_join_classroom)
        viewModel = ViewModelProvider(this).get(ClassroomViewModel::class.java)
        mBinding.classroom = viewModel
        mBinding.lifecycleOwner = this



        initFromChild(viewModel)
        setTitle("Join classroom")


        viewModel.joinClassroomSuccess.observe(this, Observer {
            if (it == true) {
                Toast
                    .makeText(
                        this,
                        "You have joined that classroom\nNow wait for approval",
                        Toast.LENGTH_LONG
                    )
                    .show()
                finish()
            }
        })

    }

}