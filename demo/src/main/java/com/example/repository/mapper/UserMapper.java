package com.example.repository.mapper;

import com.example.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wangxinghao on 2018/6/5.
 */

public interface UserMapper {
    User findById(Integer Id);
}
