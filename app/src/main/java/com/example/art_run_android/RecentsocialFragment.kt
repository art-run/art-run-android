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
import com.example.art_run_android.databinding.FragmentRecentsocialBinding
import kotlinx.android.synthetic.main.fragment_recentsocial.*
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
class RecentsocialFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var fragment: FragmentManager

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
        Log.i("RecentsocialFragment", "DataContainer : " + "")
        fragment = childFragmentManager
        CallRecentSocial(1)

    }

    private fun CallRecentSocial(lastRouteId:Int){
        //retrofit 만들기
        var retrofit = Retrofit.Builder()
            .baseUrl("http://artrun.kro.kr:80")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var socialService = retrofit.create(SocialService::class.java)
        val lastRouteId = 6



        Log.d("헤더정보 확인",DataContainer.header.toString())
        socialService.RecnetRoutesInfo(DataContainer.header,lastRouteId.toString())
            .enqueue(object: Callback<List<SocialDClass>>{
                //통신 실패시 실행되는 코드
                override fun onFailure(call: Call<List<SocialDClass>>, t: Throwable) {
                    Log.e("서버에서 소셜정보 받아오기 : 서버접속 실패", "${t.localizedMessage}")
                }

                //통신 성공시 실행되는 코드
                override fun onResponse(call: Call<List<SocialDClass>>, response: Response<List<SocialDClass>>) {
                    var serverCheck=response
                    Log.d("서버에서 소셜정보 받아오기 : 성공1",serverCheck.toString())
                    Log.d("서버에서 소셜정보 받아오기 : 성공2",serverCheck.body().toString())
                    Log.d("사이즈",response.body()?.size.toString())
                    if(response.body()?.size == 0 || response.body()?.size == null){
                        Log.d("이상하다 데이터가없는데",response.body()?.size.toString())
                    }else{
                        val size: Int? = response.body()?.size
                        if (size != null) {
                            Log.d("사이즈:4",response.body()?.size.toString())
                            var data : MutableList<SocialData> = mutableListOf()

                            for(i: Int in size-1 downTo size-lastRouteId+1){
                                Log.d("여기부터",i.toString())
                                var socialData=SocialData(
                                    response.body()?.get(i)?.profileImg.toString(),
                                    response.body()?.get(i)?.nickname.toString(),
                                    response.body()?.get(i)?.title.toString(),
                                    response.body()?.get(i)?.distance.toString(),
                                    response.body()?.get(i)?.createdAt.toString())

                                data.add(socialData)

                                Log.d("socialData 확인",socialData.toString())
                                Log.d("data 확인",data.toString())
                                //data라는 이름으로 데이터리스트 생성 완료!(for문을 다 마치면!)
                                recyclerView.adapter = RecyclerAdapter_Social(data,view!!.context)
                                recyclerView.layoutManager =
                                    LinearLayoutManager(view?.context, RecyclerView.VERTICAL, false)

                            }
                        }
                    }
                }
            }
        )
    }
}