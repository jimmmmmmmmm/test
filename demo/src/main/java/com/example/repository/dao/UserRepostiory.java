package com.example.repository.dao;

import com.example.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wangxinghao on 2018/6/5.
 */
@Repository
@Component
public interface UserRepostiory extends JpaRepository<User,String>{
    @Override
    List<User> findAll();
}
