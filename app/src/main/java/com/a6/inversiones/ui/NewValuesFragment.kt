package com.a6.inversiones.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.a6.inversiones.MainActivity.Companion.ALL_SYMBOLS
import com.a6.inversiones.MarketStockViewModel
import com.a6.inversiones.databinding.FragmentNewValuesBinding


class NewValuesFragment : Fragment() {

    private lateinit var binding: FragmentNewValuesBinding

    private val viewModel = MarketStockViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewValuesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getNewStockValue(ALL_SYMBOLS, 20)

        viewModel.getNewDividends(ALL_SYMBOLS)

        //viewModel.getStockValue("AAPL")
    }

}