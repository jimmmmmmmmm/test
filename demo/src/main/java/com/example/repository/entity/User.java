package com.example.repository.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by wangxinghao on 2018/6/5.
 */
@Data
@Entity
@Table(name = "tb_user")
public class User {

    @Id
    @Column(name = "user_id")
    private int id;
    @Column(name = "user_name")
    private String name;

}
