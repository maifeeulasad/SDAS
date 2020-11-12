package com.mua.mobileattendance.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.mua.mobileattendance.retrofit.dto.*
import com.mua.mobileattendance.retrofit.service.impl.*


class ClassroomViewModel(application: Application) :
    BaseViewModel(application), RetrofitResponseListener {

    val classroomId = MutableLiveData<Long>(0L)
    val fabExpanded = MutableLiveData<Boolean>(false)

    val rolesRefreshing = MutableLiveData<Boolean>(false)
    val sessionsRefreshing = MutableLiveData<Boolean>(false)

    val refreshing = MediatorLiveData<Boolean>()

    val assigneId = MutableLiveData<Long>(0L)
    val assigneUsername = MutableLiveData<String>("")
    val assigneRole = MutableLiveData<RoleDto>(RoleDto.Student)

    val superAuthorizedMember = MediatorLiveData<Boolean>()
    val isMember = MutableLiveData<Boolean>(false)
    val justMember = MediatorLiveData<Boolean>()

    private var createClassroomServiceImpl: ClassroomCreateServiceImpl =
        ClassroomCreateServiceImpl(application.baseContext, this)
    private var assignClassroomServiceImpl: ClassroomAssignServiceImpl =
        ClassroomAssignServiceImpl(application.baseContext, this)
    private var sessionsClassroomServiceImpl: ClassroomSessionsServiceImpl =
        ClassroomSessionsServiceImpl(application.baseContext, this)
    private var rolesClassroomServiceImpl: ClassroomRolesServiceImpl =
        ClassroomRolesServiceImpl(application.baseContext, this)
    private var roleClassroomServiceImpl: ClassroomRoleServiceImpl =
        ClassroomRoleServiceImpl(application.baseContext, this)
    private var joinClassroomServiceImpl: ClassroomJoinServiceImpl =
        ClassroomJoinServiceImpl(application.baseContext, this)
    private var editClassroomServiceImpl: ClassroomEditServiceImpl =
        ClassroomEditServiceImpl(application.baseContext, this)
    private var currentUserServiceImpl: UserGetCurrentServiceImpl =
        UserGetCurrentServiceImpl(application.baseContext, this)

    val classroomDto = MutableLiveData<ClassroomDto>()
    val name = MutableLiveData<String>("")
    val details = MutableLiveData<String>("")
    val role = MutableLiveData<String>("")
    val actingAs = MediatorLiveData<String>()
    val canCreate = MediatorLiveData<Boolean>()
    val createClassroomSuccess = MutableLiveData<Boolean>(false)
    val joinClassroomSuccess = MutableLiveData<Boolean>(false)
    val classroomEditing = MutableLiveData<Boolean>(false)
    val updateList = MediatorLiveData<Boolean>()

    val sessions = MutableLiveData<List<SessionDto>>(ArrayList())
    val userClassroomRoles = MutableLiveData<List<UserClassroomRoleDto>>(ArrayList())

    init {
        refreshing.addSource(rolesRefreshing){
            refreshing.postValue(rolesRefreshing.value!! || sessionsRefreshing.value!!)
        }
        refreshing.addSource(sessionsRefreshing){
            refreshing.postValue(rolesRefreshing.value!! || sessionsRefreshing.value!!)
        }
        //status.postValue(Status.Loading)
        currentUserServiceImpl.getCurrent()
        justMember.value = false
        canCreate.value = false
        canCreate.addSource(name) {
            //todo : read student value from string.xml
            canCreate
                .postValue(
                    !name.value.isNullOrBlank()
                            && !name.value.isNullOrEmpty()
                            && !role.value.equals("Student")
                )
        }
        updateList.value = false
        updateList.addSource(classroomDto) {
            updateList.value = true
        }
        justMember.addSource(isMember) {
            justMember.postValue(isMember.value!! && !superAuthorizedMember.value!!)
        }
        justMember.addSource(superAuthorizedMember) {
            justMember.postValue(isMember.value!! && !superAuthorizedMember.value!!)
        }
        actingAs.addSource(role) {
            if (role.value == null)
                actingAs.postValue("Acting as -- None")
            else
                actingAs.postValue("Acting as : " + role.value)
        }
    }

    fun toggleFab() {
        fabExpanded.postValue(!fabExpanded.value!!)
    }

    fun updateRoles(classroomId: Long) {
        rolesRefreshing.postValue(true)
        rolesClassroomServiceImpl.roles(classroomId)
    }

    fun updateSessions(classroomId: Long) {
        sessionsRefreshing.postValue(true)
        sessionsClassroomServiceImpl.sessions(classroomId)
    }

    fun updateRoles() {
        val classroomId = classroomDto.value!!.classroomId
        rolesRefreshing.postValue(true)
        rolesClassroomServiceImpl.roles(classroomId)
    }

    fun updateSessions() {
        val classroomId = classroomDto.value!!.classroomId

        sessionsRefreshing.postValue(true)
        sessionsClassroomServiceImpl.sessions(classroomId)
    }

    fun updateList() {
        updateList.value = false
        val classroomId = classroomDto.value!!.classroomId
        //status.postValue(Status.Loading)
        updateSessions(classroomId)
        updateRoles(classroomId)
        roleClassroomServiceImpl.role(classroomId)
    }

    fun create(): View.OnClickListener {
        return View.OnClickListener {
            //status.postValue(Status.Loading)
            createClassroomServiceImpl.create(name.value!!, details.value!!, role.value!!)
        }
    }

    fun joinClassroom() {
        //status.postValue(Status.Loading)
        joinClassroomServiceImpl.join(classroomId.value!!)
    }

    fun join(): View.OnClickListener {
        return View.OnClickListener {
            joinClassroom()
        }
    }

    fun addToClassroom(classroomId: Long, userId: Long, roleDto: RoleDto) {
        //status.postValue(Status.Loading)
        assignClassroomServiceImpl
            .assignUserId(classroomId, userId, roleDto)
    }

    fun addToClassroom(classroomId: Long, username: String, roleDto: RoleDto) {
        assignClassroomServiceImpl
            .assignUsername(classroomId, username, roleDto)
    }

    fun addToClassroom(userId: Long, roleDto: RoleDto) {
        //status.postValue(Status.Loading)
        assignClassroomServiceImpl
            .assignUserId(classroomId.value!!, userId, roleDto)
    }

    fun addToClassroom(username: String, roleDto: RoleDto) {
        //status.postValue(Status.Loading)
        assignClassroomServiceImpl
            .assignUsername(classroomId.value!!, username, roleDto)
    }

    fun addToClassroom() {
        //status.postValue(Status.Loading)
        if (assigneId.value != 0L) {
            assignClassroomServiceImpl
                .assignUserId(classroomId.value!!, assigneId.value!!, assigneRole.value!!)
        } else {
            assignClassroomServiceImpl
                .assignUsername(classroomId.value!!, assigneUsername.value!!, assigneRole.value!!)
        }
    }

    fun edit() {
        //status.postValue(Status.Loading)
        editClassroomServiceImpl.edit(classroomId.value!!, classroomDto.value!!)
    }

    override fun onRetrofitResponse(retrofitResponseObject: RetrofitResponseObject?) {
        //status.postValue(Status.Loaded)
        if (retrofitResponseObject!!.requestCode == ClassroomCreateServiceImpl.TASK_ID) {
            if (retrofitResponseObject.`object` as Long != -1L) {
                addMessage("Classroom creation successful")
                createClassroomSuccess.postValue(true)
            }
        } else if (retrofitResponseObject.requestCode == ClassroomSessionsServiceImpl.TASK_ID) {
            sessionsRefreshing.postValue(false)
            addMessage("Sessions are being updated")
            sessions.postValue(retrofitResponseObject.`object` as ArrayList<SessionDto>)
        } else if (retrofitResponseObject.requestCode == ClassroomJoinServiceImpl.TASK_ID) {
            addMessage("Join classroom success")
            joinClassroomSuccess.postValue(true)
        } else if (retrofitResponseObject.requestCode == ClassroomRolesServiceImpl.TASK_ID) {
            rolesRefreshing.postValue(false)
            addMessage("Users and Roles are being updated")
            userClassroomRoles.postValue(retrofitResponseObject.`object` as ArrayList<UserClassroomRoleDto>)
        } else if (retrofitResponseObject.requestCode == ClassroomEditServiceImpl.TASK_ID) {
            if (retrofitResponseObject.`object` as Boolean) {
                addMessage("Classroom editing successful")
            } else {
                addErrorMessage("Classroom editing un-successful")
            }
        } else if (retrofitResponseObject.requestCode == ClassroomRoleServiceImpl.TASK_ID) {
            val role = retrofitResponseObject.`object` as RoleDto
            this.role.postValue(role.toString())
            superAuthorizedMember.postValue(role == RoleDto.CR || role == RoleDto.Teacher)
            isMember.postValue(role != RoleDto.Pending)
            justMember.postValue(role == RoleDto.Student)
            actingAs.postValue("Acting as : $role")
            /*
            if (role != RoleDto.Pending) {
                addMessage("Welcome, you are currently not a member of this classroom\nPlease contact with COURSE TEACHER or CR to add you")
            } else {
                addMessage("You are acting as $role in this classroom")
            }

             */
        } else if (retrofitResponseObject.requestCode == UserGetCurrentServiceImpl.TASK_ID) {
            val user = retrofitResponseObject.`object` as UserDto
            assigneUsername.postValue(user.name)
            addMessage("Welcome "+user.name.toString())
        } else if (retrofitResponseObject.requestCode == ClassroomAssignServiceImpl.TASK_ID) {
            if (retrofitResponseObject.`object` as Boolean) {
                addMessage("Successful\nUser and Roles will be loaded in a while")
            } else {
                addErrorMessage("Adding/Joining to classroom failed")
            }
            updateList()
        }
    }
}