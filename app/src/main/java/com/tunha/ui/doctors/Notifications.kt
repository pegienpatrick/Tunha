package com.tunha.ui.doctors

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tunha.R

class Notifications : Fragment() {

    companion object {
        fun newInstance() = Notifications()
    }

    private lateinit var viewModel: NotificationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notifications_doctors, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}