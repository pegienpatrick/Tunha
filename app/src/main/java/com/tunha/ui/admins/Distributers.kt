package com.tunha.ui.admins

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.tunha.MainActivity
import com.tunha.R
import com.tunha.chemistDetails

class Distributers : Fragment() {

    companion object {
        fun newInstance() = Distributers()
    }

    private lateinit var viewModel: DistributersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        return inflater.inflate(R.layout.fragment_distributers, container, false)

        val view = inflater.inflate(R.layout.fragment_distributers, container, false)
        val viewDetail = view.findViewById<LinearLayout>(R.id.viewDetails)

        viewDetail.setOnClickListener {
            val intent = Intent(activity, chemistDetails::class.java)
            activity?.startActivity(intent)
        }

        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DistributersViewModel::class.java)
        // TODO: Use the ViewModel



    }

}