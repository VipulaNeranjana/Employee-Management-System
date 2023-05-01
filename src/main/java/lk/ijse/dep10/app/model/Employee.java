package lk.ijse.dep10.app.model;

import lk.ijse.dep10.app.enumaration.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee implements Serializable {
    private String nic;
//    private int id;
    private String name;
    private Date date;
    private String contact;
    private Enum<Gender> gender;
    private Blob profilePic;
    private Enum<MaritalStatus> maritalStatus;
    private String address;
    private Enum<UserType> userType;
    private String nationality;
    private String userName;
    private String password;
    private Enum<Designation> designation;
    private Date joinedDate;
    private boolean unionMember;
    private Enum<Status> status;
    private int basic_salary;
    private String bankName;
    private int accountNo;
    private String branchName;
    private Blob cv;
    private Blob birthCertificate;
    private Blob offerLatter;
    private Blob agreementLatter;
}
