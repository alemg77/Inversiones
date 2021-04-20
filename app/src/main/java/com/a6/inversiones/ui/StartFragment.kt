package com.a6.inversiones.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.a6.inversiones.R
import com.a6.inversiones.databinding.FragmentStartBinding


class StartFragment : Fragment() {

    private lateinit var binding: FragmentStartBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = FragmentStartBinding.inflate(inflater, container, false)

        binding.buttonCalcular.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.calculateFragment)
        }
        return binding.root
    }


}