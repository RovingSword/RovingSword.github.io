package src.main.java.com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Setter
@Getter
public class Student {

    private Integer sid;
    private String name;
    private Integer age;
    private String gender;
    private String className;
    private String major;
    private String phoneNumber;
    private String emailAddress;
}
