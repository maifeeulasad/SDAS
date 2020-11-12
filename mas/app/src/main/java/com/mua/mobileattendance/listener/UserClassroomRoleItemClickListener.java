package com.mua.mobileattendance.listener;

import com.mua.mobileattendance.retrofit.dto.RoleDto;
import com.mua.mobileattendance.retrofit.dto.UserClassroomRoleDto;

public interface UserClassroomRoleItemClickListener {

    void onClick(UserClassroomRoleDto userClassroomRoleDto);

    void onLongClick(UserClassroomRoleDto userClassroomRoleDto);

    void onRoleChange(UserClassroomRoleDto userClassroomRoleDto, RoleDto role);

}
