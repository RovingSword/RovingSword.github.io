package src.main.java.com.example.mappers;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import src.main.java.com.example.entity.Student;

import java.util.List;
import java.util.Map;

public interface StudentMapper {

    /**
     * 两个一致：
     * 1. 映射文件的namespace要和mapper接口全类名保持一致
     * 2. 映射文件中的SQL语句的id要和mapper接口中的方法名一致
     */

    /**
     * 添加学生信息
     * @return ok line
     */
    int insertStudent(Student student);

    /**
     * 更新学生信息
     */
    void updateUser();

    /**
     * 根据id查询学生信息
     *
     * @return Student
     */
    List<Student> getStudentByName(@Param("name") String name);

    Student getStudentById(@Param("sid") Integer sid);

    /**
     * get by map
     * @param name k-v
     * @return student
     */
    @MapKey("sid")
    Map<String, Object> getStudentByMap(String name);
    /**
     *
     * @return all students
     */
    List<Student> getAllStudent();

    Integer getCount();
}
