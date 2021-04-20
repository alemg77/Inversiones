package com.a6.inversiones.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.a6.inversiones.EstimatorViewModel
import com.a6.inversiones.MainActivity.Companion.ALL_SYMBOLS
import com.a6.inversiones.databinding.FragmentCalculateBinding


class CalculateFragment : Fragment() {

    private lateinit var binding: FragmentCalculateBinding

    private val viewModel = EstimatorViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalculateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonEvaluarCoeficientes.setOnClickListener {
            viewModel.evalueteCoeficiente(ALL_SYMBOLS)
        }

        binding.buttonEvaluarComprar.setOnClickListener {
            viewModel.evaluateBuy(ALL_SYMBOLS, 0.15)
        }

    }

}