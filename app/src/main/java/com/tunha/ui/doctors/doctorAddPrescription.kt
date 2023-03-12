package com.tunha.ui.doctors

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import com.tunha.R

class doctorAddPrescription : Fragment() {

    companion object {
        fun newInstance() = doctorAddPrescription()
    }

    private lateinit var viewModel: DoctorAddPrescriptionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        return inflater.inflate(R.layout.fragment_doctor_add_prescription, container, false)
        val view = inflater.inflate(R.layout.fragment_doctor_add_prescription, container, false)
        val dropdownMenu = view.findViewById<Spinner>(R.id.dropdown_menu)

        dropdownMenu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                // do something with the selected item
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // do nothing
            }
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DoctorAddPrescriptionViewModel::class.java)
        // TODO: Use the ViewModel

    }






}