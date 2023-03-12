package com.tunha.ui.doctors

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tunha.R

class doctorListPrescriptions : Fragment() {

    companion object {
        fun newInstance() = doctorListPrescriptions()
    }

    private lateinit var viewModel: DoctorListPrescriptionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_doctor_list_prescriptions, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DoctorListPrescriptionsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}