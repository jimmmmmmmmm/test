package com.example.demo;

import com.example.repository.dao.UserRepostiory;
import com.example.repository.entity.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by wangxinghao on 2018/6/5.
 */
public class TestControllerTest {
    @Autowired
    private UserRepostiory userRepostiory;

    @Test
    public void getJsons() {
        List<User> users =userRepostiory.findAll();


        System.out.print(users.size());

    }
}