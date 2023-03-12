package com.tunha.ui.distributors

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tunha.R

class fragmentChemistWholesale : Fragment() {

    companion object {
        fun newInstance() = fragmentChemistWholesale()
    }

    private lateinit var viewModel: FragmentChemistWholesaleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fragment_chemist_wholesale, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FragmentChemistWholesaleViewModel::class.java)
        // TODO: Use the ViewModel
    }

}