package lk.ijse.dep10.app.model;

import lk.ijse.dep10.app.util.LeaveType;
import lk.ijse.dep10.app.util.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
@AllArgsConstructor

public class Leave implements Serializable {
    private Date leaveDate;
    private Date applyDate;
    private String leaveType;
    private String leaveDuration;
    private String status;
}
