package com.rrpvm.testapp

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.rrpvm.testapp.databinding.ExampleLayoutBinding


class DialogFragmentTest : DialogFragment() {
    private lateinit var binding: ExampleLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ExampleLayoutBinding.inflate(inflater, container, false).run {
            binding = this
            this.root
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            var w = 0
            val vto: ViewTreeObserver = horizontalView.getViewTreeObserver()
            vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    horizontalView.getViewTreeObserver().removeOnGlobalLayoutListener(this)
                    val width = horizontalView.getChildAt(0).measuredWidth
                    val w2 = horizontalView.measuredWidth
                    w = width - w2
                }
            })
            this.horizontalView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                Log.e("F", "${w}\\$scrollX")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}