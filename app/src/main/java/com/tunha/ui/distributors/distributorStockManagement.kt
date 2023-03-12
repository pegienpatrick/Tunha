package com.tunha.ui.distributors

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.tunha.DistributorAddStock
import com.tunha.R

class distributorStockManagement : Fragment() {

    companion object {
        fun newInstance() = distributorStockManagement()
    }

    private lateinit var viewModel: DistributorStockManagementViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      val view = inflater.inflate(R.layout.fragment_distributor_stock_management, container,false)
        val button = view.findViewById<Button>(R.id.addStock)

        button.setOnClickListener{
            val intent = Intent(activity, DistributorAddStock::class.java)

            activity?.startActivity(intent)
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DistributorStockManagementViewModel::class.java)
        // TODO: Use the ViewModel
    }

}