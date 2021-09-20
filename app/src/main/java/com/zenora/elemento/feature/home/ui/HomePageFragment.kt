package com.zenora.elemento.feature.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.zenora.elemento.common.baseclass.BaseFragment
import com.zenora.elemento.databinding.FragmentHomeBinding
import com.zenora.elemento.feature.home.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomePageFragment : BaseFragment() {
    private lateinit var lBinder: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lBinder = FragmentHomeBinding.inflate(inflater)
        return lBinder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}
