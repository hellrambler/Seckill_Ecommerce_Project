package com.seckillproject;

import com.seckillproject.DAO.UserDAOMapper;
import com.seckillproject.dataObject.UserDAO;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world!
 *
 */
@SpringBootApplication(scanBasePackages = {"com.seckillproject"})
@RestController
@MapperScan("com.seckillproject.DAO")
public class App 
{
    @Autowired
    private UserDAOMapper userDAOMapper;

    @RequestMapping("/")
    public String home() {
        UserDAO userDAO = userDAOMapper.selectByPrimaryKey(1);
        if(userDAO == null) {
            return "user not exists";
        } else {
            return userDAO.getName();
        }
        //return "Hello World";
    }

    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        SpringApplication.run(App.class, args);
    }
}
