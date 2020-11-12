package com.mua.mobileattendance.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mua.mobileattendance.BaseActivity
import com.mua.mobileattendance.R
import com.mua.mobileattendance.databinding.ActivityCreateClassroomBinding
import com.mua.mobileattendance.viewmodel.ClassroomViewModel


class CreateClassroomActivity : BaseActivity() {


    private lateinit var mBinding: ActivityCreateClassroomBinding
    private lateinit var viewModel: ClassroomViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
        setMinimizable(true)
    }


    fun init() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_classroom)
        viewModel = ViewModelProvider(this).get(ClassroomViewModel::class.java)
        mBinding.classroom = viewModel
        mBinding.lifecycleOwner = this

        setTitle("Create classroom")
        //setFullScreen()

        initFromChild(viewModel)


        mBinding.spRole.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                val role = mBinding.spRole.selectedItem.toString()
                viewModel.role.postValue(role)
                viewModel.canCreate.postValue(role != "Student")
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        })

        viewModel.createClassroomSuccess.observe(this, Observer {
            if (it) {
                Toast
                    .makeText(
                        this,
                        "Classroom creation successful\nNow ask people to join or add",
                        Toast.LENGTH_LONG
                    )
                    .show()
                finish()
            }
        })

    }


}