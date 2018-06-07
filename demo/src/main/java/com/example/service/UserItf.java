package com.example.service;

import com.example.repository.entity.User;

import java.util.List;

/**
 * Created by wangxinghao on 2018/6/5.
 */
public interface UserItf {
    List<User> findAll();
}
