package lk.ijse.dep10.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor

public class Employee implements Serializable {
    private int id;
    private String name;
    private LeaveType leaveType;
    private LocalDate applyDate;
    private LocalDate leaveDate;
    private LeaveDuration leaveDuration;
    private Status status;

    public enum LeaveType{
        SICK,OTHER;
    }
    public enum Status{
        APPROVED,PENDING,REJECTED
    }
    public enum LeaveDuration{
        FULL_DAY,HALF_DAY
    }
}
