package com.example.comtroller;

import com.example.repository.entity.User;
import com.example.service.UserItf;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wxh
 * @Package com.example.demo
 * @Description: TODO
 * @date 2018/6/4 0004 上午 10:34
 */
@RestController
@Api(value = "测试接口", tags = {"测试 Json 接口"})
public class TestController {
    /**
     *
     */
    @Autowired
    private UserItf userItf;

    @ApiOperation(value = "测试", notes = "这是个多参数测试")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "a", value = "参数a", required = true,
                    dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "b", value = "参数b", required = true,
                    dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "c", value = "参数c", required = true,
                    dataType = "int", paramType = "query"),
    })
    @GetMapping("/query_merchant_product_list")
    public Map getJson(String a, String b, Integer c){
        Map map = new HashMap(3);
        map.put("a",a);
        map.put("b",a);
        map.put("c",a);
        return map;
    }
    @ApiOperation(value = "测试", notes = "这返回json字符串")
    @GetMapping("/query")
    public List<User> getJsons(){
        List<User> users =userItf.findAll();

        return users;
    }
}
