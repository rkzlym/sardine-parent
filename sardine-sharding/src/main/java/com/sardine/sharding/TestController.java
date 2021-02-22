package com.sardine.sharding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private OrderMapper orderMapper;

    @PostMapping("add")
    public int add(@RequestBody TOrder order){
        return orderMapper.insert(order);
    }
}
