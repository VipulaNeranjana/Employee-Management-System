package lk.ijse.dep10.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeTable implements Serializable {
    int id;
    String name;
    String contact;
}
