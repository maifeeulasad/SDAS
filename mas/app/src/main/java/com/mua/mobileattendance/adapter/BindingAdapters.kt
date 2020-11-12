package com.mua.mobileattendance.adapter

import android.view.View
import android.view.animation.LinearInterpolator
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("errorMessage")
    fun setErrorMessage(view: TextInputLayout, errorMessage: String?) {
        view.isErrorEnabled = errorMessage != null && errorMessage != ""
        view.error = errorMessage
    }

    @JvmStatic
    @BindingAdapter("visibility")
    fun setVisibility(view: View, visibile: Boolean) {
        if (visibile)
            view.visibility = View.VISIBLE
        else
            view.visibility = View.GONE
    }

    @JvmStatic
    @BindingAdapter("animationDim")
    fun setAnimationDim(view: View, execute: Boolean) {
        if (execute) {
            view.alpha = 0.1f
            view.animate().apply {
                interpolator = LinearInterpolator()
                duration = 1000
                alpha(1f)
                start()
            }
        } else {
            view.alpha = 1f
        }
    }
}