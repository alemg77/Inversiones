package com.a6.inversiones.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.a6.inversiones.EstimatorViewModel
import com.a6.inversiones.MainActivity
import com.a6.inversiones.MainActivity.Companion.TAG
import com.a6.inversiones.databinding.FragmentChooseBuyBinding


class ChooseBuyFragment : Fragment() {

    private lateinit var binding: FragmentChooseBuyBinding

    private val viewModel = EstimatorViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChooseBuyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.analisis.observe(viewLifecycleOwner, {
            Log.d(TAG, it.toString())
        })

        viewModel.evaluateBuy(MainActivity.SYMBOLS_BIGGEST, 0.12)
        viewModel.evaluateBuy(MainActivity.SYMBOLS_BIG, 0.15)
        viewModel.evaluateBuy(MainActivity.SYMBOLS, 0.20)
        viewModel.evaluateBuy(MainActivity.SYMBOLS_CHINA, 0.22)
        viewModel.evaluateBuy(MainActivity.SYMBOLS_TRAMPA, 0.23)

    }


}