package com.mua.mobileattendance.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mua.mobileattendance.R
import com.mua.mobileattendance.databinding.FragmentLoginBinding
import com.mua.mobileattendance.viewmodel.AuthViewModel


class LoginFragment : Fragment() {

    private lateinit var viewModel: AuthViewModel
    private lateinit var mBinding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate<ViewDataBinding>(
            inflater, R.layout.fragment_login, container, false
        ) as FragmentLoginBinding
        val view: View = mBinding.root
        viewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        mBinding.login = viewModel
        mBinding.lifecycleOwner = this
        return view
    }

}