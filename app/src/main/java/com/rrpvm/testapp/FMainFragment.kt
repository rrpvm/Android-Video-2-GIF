package com.rrpvm.testapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rrpvm.testapp.databinding.FMainFragmentBinding

class FMainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val f = FMainFragmentBinding.inflate(inflater)
        return f.root
    }
}