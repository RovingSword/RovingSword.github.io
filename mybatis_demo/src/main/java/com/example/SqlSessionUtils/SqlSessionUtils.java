package src.main.java.com.example.SqlSessionUtils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class SqlSessionUtils {

    public static SqlSession getSqlSession() {
        InputStream is = null;
        try {
            is = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSessionFactoryBuilder sqlSessionFactoryBuilder
                    = new SqlSessionFactoryBuilder();
            SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(is);
            return sqlSessionFactory.openSession(true);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
