package lk.ijse.dep10.app.util;

import java.io.Serializable;
import java.sql.Time;
import java.time.Duration;
import java.util.Date;

public class EmployeeAttendance implements Serializable {

    private String name;
    private int id;
    private Date date;
    private Time signInTime;

    private Time signOutTime;
    private String workingTime;

    public EmployeeAttendance(String name, int id, Date date, Time signInTime, Time signOutTime, String workingTime) {
        this.name = name;
        this.id = id;
        this.date = date;
        this.signInTime = signInTime;
        this.signOutTime = signOutTime;
        this.workingTime = workingTime;
    }

    public EmployeeAttendance() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getSignInTime() {
        return signInTime;
    }

    public void setSignInTime(Time signInTime) {
        this.signInTime = signInTime;
    }

    public Time getSignOutTime() {
        return signOutTime;
    }

    public void setSignOutTime(Time signOutTime) {
        this.signOutTime = signOutTime;
    }

    public String getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(String workingTime) {
        this.workingTime = workingTime;
    }
}
