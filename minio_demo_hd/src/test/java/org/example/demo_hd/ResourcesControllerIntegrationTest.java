package org.example.demo_hd;
//Resource集成测试

import org.demo_hd.MinioHdApplication;
import org.demo_hd.dto.ResourcesQueryDTO;
import org.demo_hd.entity.Resources;
import org.demo_hd.result.R;
import org.demo_hd.service.ResourcesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(classes = MinioHdApplication.class)
@AutoConfigureMockMvc
public class ResourcesControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired(required=false)
    private ResourcesService resourcesService;

    @BeforeEach
    void setup() {
        //插入一条测试数据到数据库
        Resources testResource = new Resources();
        testResource.setOwnerId(1001);
        testResource.setBucket("test-bucket");
        testResource.setObjectKey("test-object");
        testResource.setName("test-file.txt"); // 关键：设置 name 字段的值，符合数据库要求
        testResource.setType("other");
        testResource.setDifficulty("beginner");
        testResource.setPermission("public");
        resourcesService.save(testResource);
    }

    //测试 resourcesList 接口
    @Test
    public void testResourcesList() throws Exception {
        // 1. 构造入参
        Integer ownerId = 1001;
        ResourcesQueryDTO queryDTO = new ResourcesQueryDTO();
        queryDTO.setPage(1);
        queryDTO.setPageSize(10);

        // 2. 发送HTTP请求
        mockMvc.perform(MockMvcRequestBuilders.get("/api/resource/list/{ownerId}", ownerId)
                        .param("page", queryDTO.getPage().toString())
                        .param("pageSize", queryDTO.getPageSize().toString()))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(R.OK().getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.records.length()").value(1)); // 断言测试数据存在
    }
}
