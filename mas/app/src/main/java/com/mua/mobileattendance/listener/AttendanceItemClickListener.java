package com.mua.mobileattendance.listener;

import com.mua.mobileattendance.retrofit.dto.AttendanceDto;
import com.mua.mobileattendance.retrofit.dto.StateDto;

public interface AttendanceItemClickListener {

    void onClick(AttendanceDto attendanceDto);

    void onStateChange(AttendanceDto attendanceDto, StateDto stateDto);

}
