package com.example.service.impl;

import com.example.repository.dao.UserRepostiory;
import com.example.repository.entity.User;
import com.example.service.UserItf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Created by wangxinghao on 2018/6/5.
 */
@Service
public class UseImpl implements UserItf {

    @Autowired
    private UserRepostiory userRepostiory;


    @Override
    public List<User> findAll() {
        return userRepostiory.findAll();
    }


}
