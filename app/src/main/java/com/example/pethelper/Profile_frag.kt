package com.example.pethelper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Profile_frag : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    // FRAG
    lateinit var et_BirthDate: EditText
    lateinit var et_petName: EditText
    lateinit var bt_ChangePet: Button
    lateinit var et_petType: EditText
    lateinit var et_petBreed: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val frag = inflater.inflate(R.layout.fragment_profile, container, false)
        // findViewById для элементов
        et_petName = frag.findViewById(R.id.PetName)
        et_petType = frag.findViewById(R.id.PetType)
        et_petBreed = frag.findViewById(R.id.PetBreed)
        et_BirthDate = frag.findViewById(R.id.PetDate)
        bt_ChangePet = frag.findViewById(R.id.ChangePetButton)

        bt_ChangePet.setOnClickListener() {
            ShowChangeInfoPetDialog(selectedPet, inflater, requireContext(),::UpdateSelectedPetData)
        }
        UpdateSelectedPetData()
        return frag
    }

    fun UpdateSelectedPetData(){
        et_petName.setText(selectedPet.name)
        et_petType.setText(selectedPet.type)
        et_petBreed.setText(selectedPet.breed)
        et_BirthDate.setText(DateToString(selectedPet.birthDate))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Profile_frag().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}