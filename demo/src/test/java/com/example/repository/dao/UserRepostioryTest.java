package com.example.repository.dao;

import com.example.repository.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by wangxinghao on 2018/6/5.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepostioryTest {

    @Autowired
    private UserRepostiory userRepostiory;
    @Test
    public void findAll() {
       List<User> users =   userRepostiory.findAll();
       System.out.print(users.size());
    }
}