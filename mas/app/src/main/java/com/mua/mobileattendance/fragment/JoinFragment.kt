package com.mua.mobileattendance.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mua.mobileattendance.R
import com.mua.mobileattendance.databinding.FragmentJoinBinding
import com.mua.mobileattendance.viewmodel.AuthViewModel


class JoinFragment : Fragment() {

    private lateinit var viewModel: AuthViewModel
    private lateinit var mBinding: FragmentJoinBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate<ViewDataBinding>(
            inflater, R.layout.fragment_join, container, false
        ) as FragmentJoinBinding
        val view: View = mBinding.root
        viewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        mBinding.join = viewModel
        mBinding.lifecycleOwner = this
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }


    fun init() {
        viewModel.canLogin.observe(viewLifecycleOwner, Observer {

        })
    }

}