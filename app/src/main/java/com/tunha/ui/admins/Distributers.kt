package com.tunha.ui.admins

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tunha.R

class Distributers : Fragment() {

    companion object {
        fun newInstance() = Distributers()
    }

    private lateinit var viewModel: DistributersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_distributers, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DistributersViewModel::class.java)
        // TODO: Use the ViewModel
    }

}