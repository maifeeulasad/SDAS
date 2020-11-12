package com.mua.mobileattendance.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mua.mobileattendance.BaseActivity
import com.mua.mobileattendance.R
import com.mua.mobileattendance.adapter.SessionRecyclerViewAdapter
import com.mua.mobileattendance.adapter.UserClassroomRoleRecyclerViewAdapter
import com.mua.mobileattendance.custom.picker.DateTimePicker
import com.mua.mobileattendance.databinding.ActivityClassroomBinding
import com.mua.mobileattendance.listener.DateTimeChangeListener
import com.mua.mobileattendance.listener.SessionItemClickListener
import com.mua.mobileattendance.listener.UserClassroomRoleItemClickListener
import com.mua.mobileattendance.manager.ActivityManager
import com.mua.mobileattendance.retrofit.dto.ClassroomDto
import com.mua.mobileattendance.retrofit.dto.RoleDto
import com.mua.mobileattendance.retrofit.dto.SessionDto
import com.mua.mobileattendance.retrofit.dto.UserClassroomRoleDto
import com.mua.mobileattendance.viewmodel.ClassroomViewModel
import java.util.*
import kotlin.collections.ArrayList


class ClassroomActivity :
    BaseActivity(),
    DateTimeChangeListener,
    SessionItemClickListener,
    UserClassroomRoleItemClickListener {


    private lateinit var mBinding: ActivityClassroomBinding
    private lateinit var viewModel: ClassroomViewModel


    private lateinit var mSessionAdapter: SessionRecyclerViewAdapter
    private lateinit var mUserClassroomRoleAdapter: UserClassroomRoleRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
        setMinimizable(true)
        initSwipeRefresh()
    }


    /*
    override fun onResume() {
        super.onResume()
        viewModel.updateList()
    }
     */

    fun initSwipeRefresh() {
        //initSwipeRefreshRoles()
        //initSwipeRefreshSessions()
        mBinding.srlParent.setOnRefreshListener {
            viewModel.updateList()
        }
        viewModel.refreshing.observe(this, Observer {
            mBinding.srlParent.isRefreshing = it
        })
    }


    /*
    fun initSwipeRefreshRoles() {
        mBinding.srlRoles.setOnRefreshListener {
            viewModel.updateRoles()
        }
        viewModel.rolesRefresing.observe(this, Observer {
            mBinding.srlRoles.isRefreshing = it
        })
    }

    fun initSwipeRefreshSessions() {
        mBinding.srlSessions.setOnRefreshListener {
            viewModel.updateSessions()
        }
        viewModel.sessionsRefresing.observe(this, Observer {
            mBinding.srlSessions.isRefreshing = it
        })
    }
     */


    fun initJoinOrAdd() {
        mBinding.fabJoinOrAdd.setOnClickListener {
            viewModel.toggleFab()
            if (!viewModel.isMember.value!!) {
                viewModel.joinClassroom()
            } else {
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle("Add User-Id/Username And Role")

                val layout = LinearLayout(this)
                layout.orientation = LinearLayout.VERTICAL

                val userId = EditText(this)
                userId.hint = "User ID"
                layout.addView(userId)
                val username = EditText(this)
                username.hint = "Username"
                //username.setText(viewModel.assigneUsername.value)
                layout.addView(username)
                val role = Spinner(this)
                val roles = resources.getStringArray(R.array.array_role)
                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item, roles
                )
                role.adapter = adapter
                layout.addView(role)
                var selectedRole = RoleDto.Student


                role.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View, position: Int, id: Long
                    ) {
                        selectedRole = RoleDto.fromValue(roles.get(position))
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {

                    }
                }


                builder.setView(layout)


                builder.setPositiveButton("Add", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        if (!userId.text!!.toString().equals("")) {
                            viewModel
                                .addToClassroom(userId.text.toString().toLong(), selectedRole)
                        } else {
                            viewModel
                                .addToClassroom(username.text.toString(), selectedRole)
                        }
                    }
                })
                builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
                builder.show()
            }
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ActivityManager.WILL_LOAD_REQUEST_CODE) {
            if (resultCode == ActivityManager.WILL_LOAD_RESULT_CODE) {
                viewModel.updateList()
            }
        }
    }


    fun init() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_classroom)
        viewModel = ViewModelProvider(this).get(ClassroomViewModel::class.java)
        mBinding.classroom = viewModel
        mBinding.lifecycleOwner = this

        initClassroom()
        initTitle()
        initTimePicker()

        initJoinOrAdd()

        setTitle("Loading classroom")

        initSessionList()
        initUserRoleList()
        initLoadList()
        initFab()

        initFromChild(viewModel)

        mBinding.fabManageClassroom.setOnClickListener {
            viewModel.toggleFab()

            val intent = Intent(this, ManageMembersActivity::class.java)

            val bundle = Bundle()
            bundle.putSerializable("classroomid", viewModel.classroomId.value)
            intent.putExtras(bundle)

            startActivityForResult(intent, ActivityManager.WILL_LOAD_REQUEST_CODE)
        }

        mBinding.fabEditClassroom.setOnClickListener {
            viewModel.toggleFab()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (!viewModel.classroomEditing.value!!) {
                    mBinding.fabEditClassroom.setImageDrawable(getDrawable(R.drawable.done))
                } else {
                    mBinding.fabEditClassroom.setImageDrawable(getDrawable(R.drawable.edit))
                }
            }
            if (viewModel.classroomEditing.value!!) {
                viewModel.edit()
            }
            viewModel.classroomEditing.postValue(!viewModel.classroomEditing.value!!)
        }


        mBinding.fabAddClassroom.setOnClickListener {
            viewModel.toggleFab()

            val intent = Intent(this, SessionActivity::class.java)

            val bundle = Bundle()
            bundle.putSerializable("classroom", viewModel.classroomDto.value)
            bundle.putBoolean("editable", true)
            bundle.putString("role", viewModel.role.value)
            intent.putExtras(bundle)

            startActivityForResult(intent, ActivityManager.WILL_LOAD_REQUEST_CODE)
        }

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
                mBinding.fabExpand.animate().rotation(180f)
            } else {
                mBinding.fabExpand.animate().rotation(0f)
            }
        })
    }


    private fun expendFab() {
        viewModel.fabExpanded.postValue(false)
    }

    private fun collapseFab() {
        viewModel.fabExpanded.postValue(true)
    }


    fun initLoadList() {
        viewModel.updateList.observe(this, Observer {
            if (it) {
                viewModel.updateList()
            }
        })
    }

    fun initClassroom() {
        val classroom = intent.extras!!.getSerializable("classroom") as ClassroomDto
        viewModel.classroomDto.postValue(classroom)
        viewModel.classroomId.postValue(classroom.classroomId)

        mSessionAdapter = SessionRecyclerViewAdapter(this, ArrayList(), this)
        mUserClassroomRoleAdapter = UserClassroomRoleRecyclerViewAdapter(this, ArrayList(), this)
    }


    fun initTitle() {
        viewModel.classroomDto.observe(this, Observer {
            setTitle("Classroom : " + it!!.name)
        })

    }


    fun initTimePicker() {
        viewModel.classroomDto.observe(this, Observer {
            if (it.nextSessionTime != null)
                DateTimePicker(
                    mBinding.btnDatePick,
                    viewModel.classroomDto.value!!.nextSessionTime,
                    this,
                    this
                )
            else
                DateTimePicker(
                    mBinding.btnDatePick,
                    null,
                    this,
                    this
                )
        })
    }

    fun initSessionList() {
        mBinding.rvSessions.setLayoutManager(LinearLayoutManager(this))
        mBinding.rvSessions.adapter = mSessionAdapter

        viewModel.sessions.observe(this, Observer {
            if (it != null) {
                val tem = it as ArrayList<SessionDto>
                tem.reverse()
                mSessionAdapter = SessionRecyclerViewAdapter(this, tem, this)
                mBinding.rvSessions.adapter = mSessionAdapter
            }
        })
    }

    fun initUserRoleList() {
        mBinding.rvUserRoles.setLayoutManager(LinearLayoutManager(this))
        mBinding.rvUserRoles.adapter = mUserClassroomRoleAdapter

        viewModel.userClassroomRoles.observe(this, Observer {
            if (it != null) {
                mUserClassroomRoleAdapter =
                    UserClassroomRoleRecyclerViewAdapter(this, it as ArrayList, this)
                mBinding.rvUserRoles.adapter = mUserClassroomRoleAdapter
            }
        })

    }

    override fun onDateTimeChange(date: Date?) {
        val classroom = viewModel.classroomDto.value!!
        classroom.nextSessionTime = date
        viewModel.classroomDto.postValue(classroom)
    }

    override fun onClick(sessionDto: SessionDto?) {
        val intent = Intent(this, SessionActivity::class.java)

        val bundle = Bundle()
        bundle.putLong("classroomid", viewModel.classroomDto.value!!.classroomId)
        bundle.putSerializable("session", sessionDto)
        bundle.putBoolean("editable", false)
        bundle.putString("role", viewModel.role.value)
        intent.putExtras(bundle)

        startActivityForResult(intent, ActivityManager.WILL_LOAD_REQUEST_CODE)
    }

    override fun onClick(userClassroomRoleDto: UserClassroomRoleDto?) {
        val intent = Intent(this, ProfileActivity::class.java)

        val bundle = Bundle()
        bundle.putLong("userid", userClassroomRoleDto!!.userId)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onLongClick(userClassroomRoleDto: UserClassroomRoleDto?) {
        //todo : open manage activity and navigate to bla blah bum bumm
    }

    override fun onRoleChange(userClassroomRoleDto: UserClassroomRoleDto?, role: RoleDto?) {
        TODO("Not yet implemented")
    }

}