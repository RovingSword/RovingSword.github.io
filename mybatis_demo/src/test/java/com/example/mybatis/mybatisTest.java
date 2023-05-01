package src.test.java.com.example.mybatis;


import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;
import src.main.java.com.example.SqlSessionUtils.SqlSessionUtils;
import src.main.java.com.example.entity.Student;
import src.main.java.com.example.mappers.StudentMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class mybatisTest {

    @Test
    void test1() throws IOException {
        // 加载配置文件
        InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
        // 获取SqlSessionFactoryBuilder
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        // 获取sqlSessionFactory
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(is);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        // 获取mapper接口对象(接口实现类的对象)
        StudentMapper studentMapper = sqlSession.getMapper(StudentMapper.class);
        Student stu = new Student(null, "jack", 13, "男", "3-2", "math", "123456", "jack@ustc.com");
        int result = studentMapper.insertStudent(stu);
        System.out.println("result: " + stu.getSid());

        // 提交事务
//        sqlSession.commit();
    }

    @Test
    void testUpdate() throws IOException {
        InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
        mapper.updateUser();
    }

    @Test
    void selectAll() throws IOException {
        InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
        List<Student> allStudent = mapper.getAllStudent();
        for(Student stu: allStudent){
            System.out.println(stu.toString());
        }
    }

    @Test
    void testParam(){
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
        List<Student> s = mapper.getStudentByName("Ragnaro");
        System.out.println(s);

        Student id = mapper.getStudentById(1);
        System.out.println(id);
    }

    @Test
    void testCount(){
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
        Map<String, Object> ragnaro = mapper.getStudentByMap("Ragnaro");
        System.out.println(ragnaro);
    }

}
