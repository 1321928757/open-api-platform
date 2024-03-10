package com.apiinterface;

import javax.annotation.Resource;

import com.apiinterface.provider.OpenService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 主类测试
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@SpringBootTest
class MainApplicationTests {

    @Resource
    private OpenService openService;

    @Test
    void contextLoads() {
        Boolean aBoolean = openService.invokeCount(1L, 13L);
        System.out.println(aBoolean);
    }

}
