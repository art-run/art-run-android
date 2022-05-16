package com.example.art_run_android

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MysocialFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MysocialFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recyclerView : RecyclerView
    private lateinit var fragment:FragmentManager

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
        return inflater.inflate(R.layout.fragment_mysocial, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rv_profile)
//        fragment = targetFragment!!.childFragmentManager
        fragment = childFragmentManager
        CallRecentSocial(1)
//        val profileList = arrayListOf(
//            SocialData("","내이름", "123", "3", "2022-02-13" ),
//            SocialData("","내이름", "adsf22", "1.5", "2022-02-13"),
//            SocialData("","내이름", "qwer", "11" , "2022-12-12"),
//            SocialData("","내이름", "qwer44", "0", "2021-11-11"),
//            SocialData("","내이름", "asdf", "3", "2022-02-13" ),
//            SocialData("","내이름", "adsf22", "1.5", "2022-02-13"),
//        )
//
//        recyclerView.adapter = RecyclerAdapter_My(profileList)
//        recyclerView.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
    }

    private fun CallRecentSocial(lastRouteId:Int){
        //retrofit 만들기
        var retrofit = Retrofit.Builder()
            .baseUrl("http://artrun.kro.kr:80")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var socialService = retrofit.create(SocialService::class.java)
        val lastRouteId = DataContainer.user_id
        val memberHeader = DataContainer.memberHeader
        var testtoken =
            "Bearer {" + "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2NTMxMTkxODF9.200u47deSU9q0YujpaWWTe9C1HWtZ2iAJYyhKoXL2DBaHG5lDBxKdQzHcVSWQmQGttFSuidLVyjiPOq-lrnKuQ}"
        Log.i("데이터 확인", "lastRouteId : " + lastRouteId)
        Log.i("데이터 확인", "memberHeader : " + memberHeader)

        socialService.RecnetRoutesInfo(memberHeader, lastRouteId)
            .enqueue(object : Callback<List<SocialDClass>> {
                //통신 성공시
                override fun onResponse(
                    call: Call<List<SocialDClass>>,
                    response: Response<List<SocialDClass>>
                ) {
                    Log.d("route 1 : 데이터파싱 테스트", response.body()?.get(0)?.nickname.toString())
                    Log.i("route 1", " ROUTE 1 SIZE : " + response.body()?.size)
                    if(response.body()?.size == 0 || response.body()?.size == null){
                        val profileList = arrayListOf(
                            SocialData("", "내이름", "123", "3", "2022-02-13"),
                            SocialData("", "내이름", "adsf22", "1.5", "2022-02-13"),
                            SocialData("", "내이름", "qwer", "11", "2022-12-12"),
                            SocialData("", "내이름", "qwer44", "0", "2021-11-11"),
                            SocialData("", "내이름", "asdf", "3", "2022-02-13"),
                            SocialData("", "내이름", "adsf22", "1.5", "2022-02-13"),
                        )

                        recyclerView.adapter = RecyclerAdapter_My(profileList,context,fragment)
                        recyclerView.layoutManager =
                            LinearLayoutManager(view?.context, RecyclerView.VERTICAL, false)
                    }else{
                        val size: Int? = response.body()?.size
                        if (size != null) {
                            for(i: Int in size downTo size-5){
                                val profileList = arrayListOf(
                                    SocialData(
                                        ""
                                        , response.body()?.get(i)?.nickname.toString()
                                        , response.body()?.get(i)?.title.toString()
                                        , response.body()?.get(i)?.distance.toString()
                                        , response.body()?.get(i)?.createdAt.toString())
                                )
                                recyclerView.adapter = RecyclerAdapter_My(profileList,context,fragment)
                                recyclerView.layoutManager =
                                    LinearLayoutManager(view?.context, RecyclerView.VERTICAL, false)
                                recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                                    override fun onScrolled(recyclerView: RecyclerView, dx:Int, dy:Int){
                                        super.onScrolled(recyclerView, dx, dy)
                                        // 스크롤이 끝에 도달했는지 확인
                                        if (!recyclerView.canScrollVertically(1)) {
                                            CallRecentSocial(size-5)
                                        }
                                    }
                                })



                            }
                        }
                    }
                }
                //통신 실패시
                override fun onFailure(call: Call<List<SocialDClass>>, t: Throwable) {
                    Log.e("통신실패", t.localizedMessage.toString())
//                     Toast.makeText(this,"통신 실패!", Toast.LENGTH_LONG).show()

                }//통신 실패시
            })
    }
}