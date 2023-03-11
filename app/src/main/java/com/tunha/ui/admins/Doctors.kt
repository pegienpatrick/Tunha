package com.tunha.ui.admins

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tunha.R
import com.tunha.chemistDetails
import com.tunha.doctorDetails

class Doctors : Fragment() {

    companion object {
        fun newInstance() = Doctors()
    }

    private lateinit var viewModel: DoctorsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_doctors, container, false)
        val viewDetail = view.findViewById<LinearLayout>(R.id.viewDetails)

        viewDetail.setOnClickListener {
            val intent = Intent(activity, doctorDetails::class.java)
            activity?.startActivity(intent)
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DoctorsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}