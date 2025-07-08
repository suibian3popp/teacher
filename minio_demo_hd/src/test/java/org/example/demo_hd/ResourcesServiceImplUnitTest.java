package org.example.demo_hd;
//ResourcesService单元测试

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.minio.MinioClient;
import org.demo_hd.dto.ResourcesQueryDTO;
import org.demo_hd.entity.Resources;
import org.demo_hd.mapper.ResourcesMapper;
import org.demo_hd.service.impl.ResourcesServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Field; // 导入反射类
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class ResourcesServiceImplUnitTest {

    //注入待测试的 Service
    @InjectMocks
    private ResourcesServiceImpl resourcesServiceImpl;

    //模拟 Mapper
    @Mock
    private ResourcesMapper resourcesMapper;

    //模拟 MinioClient
    @Mock
    private MinioClient minioClient;

    //Mock 基类的 baseMapper
    @Mock
    private BaseMapper<Resources> baseMapper;

    @BeforeEach
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        //初始化 Mockito 注解
        MockitoAnnotations.openMocks(this);

        //通过反射将 mock 的 resourcesMapper 赋值给父类的 baseMapper
        Field baseMapperField = ServiceImpl.class.getDeclaredField("baseMapper");
        baseMapperField.setAccessible(true); // 突破私有属性访问限制
        baseMapperField.set(resourcesServiceImpl, resourcesMapper); // 绑定到 mock 的 mapper

    }

    //测试delResourceById方法
    @Test
    public void testDelResourceById() throws Exception {
        //构造测试数据
        Integer resourceId = 1;
        Resources resource = new Resources();
        resource.setBucket("test-bucket");
        resource.setObjectKey("test-object");
        //模拟Mapper查询结果
        when(resourcesMapper.selectById(resourceId)).thenReturn(resource);
        //模拟Mapper删除结果（返回1表示删除成功）
        when(resourcesMapper.deleteById(resourceId)).thenReturn(1);

        //调用 Service 方法
        boolean result = resourcesServiceImpl.delResourceById(resourceId);

        //断言
        assertTrue(result, "删除资源应返回 true");
    }

    //测试saveResources 方法
    @Test
    public void testSaveResources() {
        // 1. 构造参数
        Integer ownerId = 1;
        String bucket = "test-bucket";
        String object = "test-object";
        MultipartFile file = new org.springframework.mock.web.MockMultipartFile(
                "test-file.txt", "测试内容".getBytes()
        );
        String name = "test-name";
        String type = "courseware";
        String difficulty = "beginner";
        String permission = "public";

        // 2. 调用 Service 方法
        resourcesServiceImpl.saveResources(ownerId, bucket, object, file, name, type, difficulty, permission);

        // 3. 断言（验证 Mapper 的 insert 方法被调用
        verify(resourcesMapper).insert(org.mockito.ArgumentMatchers.any(Resources.class));
    }

    // 测试 checkResourceNameExists 方法
    @Test
    public void testCheckResourceNameExists() {
        // 1. 构造参数
        Integer ownerId = 1;
        String name = "test-name";
        // 模拟 Mapper 查询结果（返回 1 表示存在）
        when(resourcesMapper.selectCount(any(QueryWrapper.class))).thenReturn(1L);

        // 2. 调用 Service 方法
        boolean exists = resourcesServiceImpl.checkResourceNameExists(ownerId, name);

        // 3. 断言
        assertTrue(exists, "资源名称应存在");
    }

    // 测试 viewList 方法
    @Test
    public void testViewList() {
        // 1. 构造参数
        Integer ownerId = 1;
        ResourcesQueryDTO queryDTO = new ResourcesQueryDTO();
        queryDTO.setPage(1);
        queryDTO.setPageSize(10);
        // 模拟 Mapper 返回分页结果
        Page<Resources> page = new Page<>(1, 10);
        List<Resources> records = new ArrayList<>();
        records.add(new Resources());
        page.setRecords(records);
        page.setTotal(1L);
        when(resourcesMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);

        // 2. 调用 Service 方法
        IPage<Resources> resultPage = resourcesServiceImpl.viewList(ownerId, queryDTO);

        // 3. 断言
        assertEquals(1, resultPage.getTotal(), "总记录数应符合预期");
    }
}
