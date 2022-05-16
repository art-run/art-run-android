package com.example.art_run_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MysocialFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecentsocialFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recyclerView : RecyclerView

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recentsocial, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rv_profile)
        val profileList = arrayListOf(
            SocialData("김ㅇㅇ", "asdf", "3", "2022-02-13" ),
            SocialData("이ㅇㅇ", "adsf22", "1.5", "2022-02-13"),
            SocialData("박ㅇㅇ", "qwer", "11" , "2022-12-12"),
            SocialData("한ㅇㅇ", "qwer44", "0", "2021-11-11"),
            SocialData("김ㅇㅇ", "asdf", "3", "2022-02-13" ),
            SocialData("이ㅇㅇ", "adsf22", "1.5", "2022-02-13"),
        )

        recyclerView.adapter = RecyclerAdapter_Social(profileList)
        recyclerView.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false) }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyFragment1.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RecentsocialFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}