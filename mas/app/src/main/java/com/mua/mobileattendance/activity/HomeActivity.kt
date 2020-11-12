package com.mua.mobileattendance.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mua.mobileattendance.BaseActivity
import com.mua.mobileattendance.MainActivity
import com.mua.mobileattendance.R
import com.mua.mobileattendance.adapter.ClassroomRecyclerViewAdapter
import com.mua.mobileattendance.databinding.ActivityHomeBinding
import com.mua.mobileattendance.listener.ClassroomItemClickListener
import com.mua.mobileattendance.manager.AuthenticationManager
import com.mua.mobileattendance.retrofit.dto.ClassroomDto
import com.mua.mobileattendance.viewmodel.HomeViewModel
import java.util.*


class HomeActivity : BaseActivity(), ClassroomItemClickListener {

    private lateinit var mBinding: ActivityHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var mAdapter: ClassroomRecyclerViewAdapter
    private lateinit var mAdapterSearch: ClassroomRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
        setMinimizable(true)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadAllClassroom()
    }

    fun init() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        mBinding.home = viewModel
        mBinding.lifecycleOwner = this

        setTitle("Home")
        //setFullScreen()


        initFromChild(viewModel)


        viewModel.loadAllClassroom()

        mBinding.fabCreateClassroom.setOnClickListener {
            viewModel.toggleFab()
            startActivity(Intent(this, CreateClassroomActivity::class.java))
        }

        mBinding.fabJoinClassroom.setOnClickListener {
            viewModel.toggleFab()
            startActivity(Intent(this, JoinClassroomActivity::class.java))
        }

        mBinding.rvClassrooms.setLayoutManager(LinearLayoutManager(this))
        viewModel.resultantClassroomDtos.observe(this, Observer {
            mAdapter = ClassroomRecyclerViewAdapter(this, it as ArrayList<ClassroomDto>, this)
            mBinding.rvClassrooms.adapter = mAdapter
        })


        mBinding.rvClassroomsSearch.setLayoutManager(LinearLayoutManager(this))
        viewModel.searchedClassroomDtos.observe(this, Observer {
            mAdapterSearch = ClassroomRecyclerViewAdapter(this, it as ArrayList<ClassroomDto>, this)
            mBinding.rvClassroomsSearch.adapter = mAdapterSearch
        })



        mBinding.srlParent.setOnRefreshListener {
            viewModel.loadAllClassroom()
        }

        viewModel.loading.observe(this, Observer {
            mBinding.srlParent.isRefreshing = it
        })


        initFab()
    }


    fun initFab() {
        mBinding.fabExpand.setOnClickListener {
            if (viewModel.fabExpanded.value == true) {
                expendFab()
            } else {
                collapseFab()
            }
        }
        viewModel.fabExpanded.observe(this, Observer {
            if (it) {
                mBinding.fabExpand.animate().rotation(90f)
            } else {
                mBinding.fabExpand.animate().rotation(270f)
            }
        })
    }


    private fun expendFab() {
        viewModel.fabExpanded.postValue(false)
    }

    private fun collapseFab() {
        viewModel.fabExpanded.postValue(true)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_home, menu)

        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val search: SearchView = menu!!.findItem(R.id.action_search).actionView as SearchView

        search.setSearchableInfo(manager.getSearchableInfo(componentName))

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.allSearch(query!!)
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                viewModel.findByName(query!!)
                return true
            }
        })

        search.setOnCloseListener {
            viewModel.searching.postValue(false)
            true
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.menu_logout) {
            return logout()
        } else if (id == R.id.menu_profile) {
            return openMyProfile()
        } else if (id == R.id.menu_info) {
            return showInfo()
        }
        return false
    }

    fun showInfo(): Boolean {
        val msg = WebView(this)
        msg.loadData(
            "<head>\n" +
                    "<style>\n" +
                    "p {text-align: center;}\n" +
                    "</style>\n" +
                    "</head>" +
                    "<p>Mobile Application Development Lab</p>" +
                    "<p>CSE - 618</p>" +
                    "<p>Course Instructor : <b><i>Md. Hanif Seddiqui</i></b></p>" +
                    "<p>Developed by : </p>" +
                    "<p>Maifee Ul Asad, <b>17701086</b></p>" +
                    "<p>Md. Khalilul Mustafa Jihan, <b>16701027</b></p>",
            "text/html; charset=utf-8", "UTF-8"
        )

        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Smartphone based Digital Attendance System")
        dialog.setView(msg)
        dialog.setCancelable(true)
        dialog.create().show()
        return true
    }

    fun logout(): Boolean {
        val res = AuthenticationManager.removeAuthKey(this)
        if (res) {
            startActivityAndClear(this, MainActivity::class.java)
        }
        return res
    }

    fun openMyProfile(): Boolean {
        startActivity(this, ProfileActivity::class.java)
        return true
    }

    override fun onClick(classroomDto: ClassroomDto?) {
        val intent = Intent(this, ClassroomActivity::class.java)

        val bundle = Bundle()
        bundle.putSerializable("classroom", classroomDto)
        intent.putExtras(bundle)

        startActivity(intent)
    }

}