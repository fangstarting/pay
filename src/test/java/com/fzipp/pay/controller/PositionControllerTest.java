package com.fzipp.pay.controller;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @ClassName PositionControllerTest
 * @Description
 * @Author 24k
 * @Date 2021/12/27 22:43
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
//告诉SpringBoot去寻找主配置类(例如使用@SpringBootApplication注解标注的类)，并使用它来启动一个Spring application context;
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)  //SpringBootTest.WebEnvironment.RANDOM_PORT设置随机端口启动服务器（有助于避免测试环境中的冲突）
@SpringBootTest
public class PositionControllerTest {

//    @Autowired
//    private PositionController positionController;

    //测试@SpringBootTest是否会将@Component加载到Spring application context
    @Test
    public void contextLoads(){
//        System.out.println("测试");
    }

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before // 在测试开始前初始化工作
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

//    @Test
//    public void testDownload() throws Exception {
//
//        //Get请求
//        MvcResult result = mockMvc.perform(get("http://localhost:8081/pay/position/getinbydeptid").content("deptId=1"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }



}
