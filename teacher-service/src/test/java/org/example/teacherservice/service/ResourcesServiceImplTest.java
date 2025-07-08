//package org.example.teacherservice.service;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import io.minio.*;
//import org.example.teacherservice.entity.Classes;
//import org.example.teacherservice.entity.Courses;
//import org.example.teacherservice.entity.Resources;
//import org.example.teacherservice.mapper.ClassesMapper;
//import org.example.teacherservice.mapper.CoursesMapper;
//import org.example.teacherservice.mapper.ResourcesMapper;
//import org.example.teacherservice.service.impl.ResourcesServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@Transactional
//public class ResourcesServiceImplTest {
//    @Autowired
//    private ResourcesServiceImpl resourcesService;
//
//    @Autowired
//    private CoursesMapper coursesMapper;
//
//    @Autowired
//    private ResourcesMapper resourcesMapper;
//
//    @Autowired
//    private ClassesMapper classesMapper;
//
//    @MockBean
//    private MinioClient minioClient;
//
//    private Integer testOwnerId=1;//测试用的用户id
//    private MultipartFile testFile;;//测试用文件
//
//    @BeforeEach
//    void setUp() throws Exception {
//        //先删除 classes 表中关联 courses 的记录
//        LambdaQueryWrapper<Classes> classesWrapper = new LambdaQueryWrapper<>();
//        classesWrapper.isNotNull(Classes::getCourseId); // 删除所有关联了 courses 的记录
//        classesMapper.delete(classesWrapper);
//
//        //再删除 courses 表中关联 resources 的记录（如果之前的逻辑还在）
//        LambdaQueryWrapper<Courses> coursesWrapper = new LambdaQueryWrapper<>();
//        coursesWrapper.isNotNull(Courses::getCoverImageResource);
//        coursesMapper.delete(coursesWrapper);
//
//        //再清空 resources 表数据
//        resourcesMapper.delete(null);
//
//        //准备测试文件
//        testFile=new MockMultipartFile(
//                "test.txt",
//                "test.txt",
//                "text/plain",
//                "Hello World".getBytes()
//        );
//
//        //模拟MinIO操作
//        when(minioClient.bucketExists(any(BucketExistsArgs.class))).thenReturn(true);
//        doNothing().when(minioClient).makeBucket(any(MakeBucketArgs.class));
//        doNothing().when(minioClient).removeObject(any(RemoveObjectArgs.class));
//        doReturn("https://localhost:9000/test-bucket/test.txt")
//                .when(minioClient).getPresignedObjectUrl(any(GetPresignedObjectUrlArgs.class));
//    }
//
//
//    /**
//     * 测试资源上传功能
//     */
//    @Test
//    void saveResources_Success() {
//        //执行上传
//        // 创建资源对象
//        Resource resource = new Resource();
//        resource.setOwnerId(testOwnerId);
//        resource.setBucket("test-bucket");
//        resource.setObjectKey("test.txt");
//        resource.setName("Test Resource");
//        resource.setType("文档");
//        resource.setDifficulty("简单");
//        resource.setPermission("私有");
//        resource.setFileSize(testFile.getSize()); // 设置文件大小
//
//        // 执行保存操作
//        Resources savedResource = resourcesService.saveResources(resource);
//
//        // 验证返回的对象不为空
//        assertNotNull(savedResource);
//        assertNotNull(savedResource.getResourceId()); // 确保ID已生成
//
//        // 从数据库查询验证
//        Resources dbResource = resourcesMapper.selectById(savedResource.getResourceId());
//        assertNotNull(dbResource);
//        assertEquals("Test Resource", dbResource.getName());
//        //验证
//        Resources saved = resourcesMapper.selectById(1);
//        assertNotNull(saved);
//        assertEquals("Test Resource", saved.getName());
//        assertEquals("case", saved.getType());
//        assertEquals("beginner", saved.getDifficulty());
//        assertEquals("private", saved.getPermission());
//        assertEquals(testFile.getSize(), saved.getFileSize());
//    }
//}
