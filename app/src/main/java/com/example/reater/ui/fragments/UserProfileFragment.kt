package com.example.reater.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.reater.R



class UserProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)
        
        view.findViewById<android.widget.TextView>(R.id.User_Name).text = "Rithik Bansal"
        view.findViewById<android.widget.TextView>(R.id.EnrollmentValue).text = "23148"
        view.findViewById<android.widget.TextView>(R.id.email).text = "rithikbansal123@gmail.com"
        view.findViewById<android.widget.TextView>(R.id.contact).text = "9889926671"
        view.findViewById<android.widget.TextView>(R.id.address_).text = "1/1/26" // Reusing Address field for DOB if DOB isn't visible
        view.findViewById<android.widget.TextView>(R.id.Ey_).text = "2023"
        view.findViewById<android.widget.TextView>(R.id.dateofbirth).text = "1/1/26"

        return view
    }


}