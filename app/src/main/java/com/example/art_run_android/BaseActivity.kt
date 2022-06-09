package com.example.art_run_android

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.net.toUri
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.example.art_run_android.DataContainer.Companion.userProfileImg
import com.example.art_run_android.databinding.BaseHeaderBinding
import com.example.art_run_android.running.MainActivity
import com.example.art_run_android.member_management.SelectSettingsActivity
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_my.*
import java.net.URL
import java.security.AccessController.getContext

open class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_activity_base)
    }

    override fun setContentView(layoutResID: Int) {
        val fullView = layoutInflater.inflate(R.layout.base_activity_base, null)
        val activityContainer: FrameLayout =
            fullView.findViewById(R.id.activity_content) as FrameLayout
        layoutInflater.inflate(layoutResID, activityContainer, true)
        super.setContentView(fullView)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_navigation_menu)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        val navHeaderView = navigationView.getHeaderView(0)


        val nickName = navHeaderView.findViewById<TextView>(R.id.nickName)
        nickName.text = DataContainer.userNickname + " 님"

        //프로필 이미지 불러오기
        /*
        //이건 glide를 사용해본 것
        val url = userProfileImg
        val imageView = findViewById<ImageView>(R.id.img_sideProfile)
        Glide.with(this)        //context어떻게 넣지...
            .load(url) // 불러올 이미지 url
            .placeholder(R.mipmap.ic_artrun) // 이미지 로딩 시작하기 전 표시할 이미지
            .error(R.drawable.example_picture) // 로딩 에러 발생 시 표시할 이미지
            .fallback(R.drawable.example_picture) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
            .into(imageView) // 이미지를 넣을 뷰



        //이건 그냥 뷰바인딩 사용해본 것
        val url = userProfileImg
        val binding=BaseHeaderBinding.inflate(layoutInflater)
        binding.imgSideProfile.setImageURI(url?.toUri())
        val url = userProfileImg
        //피카소 사용해봤는데 안됨.
        val binding=BaseHeaderBinding.inflate(layoutInflater)
        Picasso.get().load(url).into(binding.imgSideProfile);

        */

        var image_task:URLtoBitmapTask= URLtoBitmapTask()
        image_task=URLtoBitmapTask().apply{
            url=URL(userProfileImg)
        }
        var bitmap:Bitmap=image_task.execute().get()

        val binding=BaseHeaderBinding.inflate(layoutInflater)
        binding.imgSideProfile.setImageBitmap(bitmap)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawers()
        when (item.itemId) {
            R.id.menu_run ->
                startActivity(Intent(this, MainActivity::class.java))
            R.id.menu_social ->
                startActivity(Intent(this, SocialActivity::class.java))
            R.id.menu_info ->
                startActivity(Intent(this, SelectSettingsActivity::class.java))
        }
        return false
    }
}
class URLtoBitmapTask() : AsyncTask<Void, Void, Bitmap>() {
    //액티비티에서 설정해줌
    lateinit var url: URL
    override fun doInBackground(vararg params: Void?): Bitmap {
        val bitmap = BitmapFactory.decodeStream(url.openStream())
        return bitmap
    }
    override fun onPreExecute() {
        super.onPreExecute()

    }
    override fun onPostExecute(result: Bitmap) {
        super.onPostExecute(result)
    }
}