package com.example.art_run_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.art_run_android.running.record_card.CompleteRecordCard
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SocialFragment(private val isMine: Boolean) : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var fragment: FragmentManager
    lateinit var mySocialAdapter: SocialAdapter
    val myRoutes = mutableListOf<SocialDClass>()
    var lastRouteId = ""

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
        return inflater.inflate(R.layout.social_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragment = childFragmentManager
        initRecycler(view)

    }

    private fun initRecycler(view: View) {
        mySocialAdapter = SocialAdapter(requireContext())
        val rvSocial: RecyclerView = view.findViewById(R.id.rv_profile)
        rvSocial.adapter = mySocialAdapter

        mySocialAdapter.socialRoutes = myRoutes

        updateList(lastRouteId)

        rvSocial.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!rvSocial.canScrollVertically(1)) {
                    //routeId=new routeId
                    lastRouteId = myRoutes.last().routeId.toString()
                    updateList(lastRouteId)
                }
            }
        })

        mySocialAdapter.setOnItemClickListener(object : SocialAdapter.OnItemClickListener {
            override fun onItemClick(v: View, data: SocialDClass) {
                val intent = Intent(requireContext(), CompleteRecordCard::class.java)
                intent.putExtra("finishRouteID", data.routeId)
                startActivity(intent)
            }
        })

    }

    private fun updateList(lastRouteId: String) {
        val callGetMyRoutes =
            if (isMine) SocialClient.socialService.getMyRoutes(DataContainer.header, lastRouteId)
            else SocialClient.socialService.getRecentRoutes(DataContainer.header, lastRouteId)

        callGetMyRoutes.enqueue(object : Callback<List<SocialDClass>> {
            override fun onResponse(
                call: Call<List<SocialDClass>>,
                response: Response<List<SocialDClass>>
            ) {
                if (response.isSuccessful) { // <--> response.code == 200
                    Log.d("social $isMine", "통신 성공")
                    val responseBody = response.body() as List<SocialDClass>
                    if (responseBody.isEmpty()) {
                        return
                    }
                    myRoutes.addAll(responseBody)
                    mySocialAdapter.notifyItemRangeInserted(myRoutes.size,responseBody.size)

                } else { // code == 400
                    Log.d("social $isMine", "통신 실패 " + DataContainer.userNumber.toString())
                    Toast.makeText(requireContext(), "기록을 가져올 수 없습니다.", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<SocialDClass>>, t: Throwable) {
                Log.d("social $isMine", "통신 실패 : $t")
                Toast.makeText(requireContext(), "기록을 가져올 수 없습니다.", Toast.LENGTH_LONG).show()
            }
        })
    }

}