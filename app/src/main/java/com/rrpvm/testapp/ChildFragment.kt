package com.rrpvm.testapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.rrpvm.testapp.databinding.ChildFragmentBinding

class ChildFragment : Fragment() {
    private val args by navArgs<ChildFragmentArgs>()
    private lateinit var binding: ChildFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         binding = ChildFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            this.tv1.text = args.userId
        }
    }
}