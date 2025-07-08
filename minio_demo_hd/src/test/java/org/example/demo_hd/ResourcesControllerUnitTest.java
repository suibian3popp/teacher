package org.example.demo_hd;
//ResourcesController单元测试

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.demo_hd.config.MinIOInfo;
import org.demo_hd.controller.ResourcesController;
import org.demo_hd.dto.ResourcesQueryDTO;
import org.demo_hd.entity.Resources;
import org.demo_hd.result.R;
import org.demo_hd.service.ResourcesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockHttpServletResponse; // 导入模拟响应类

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class ResourcesControllerUnitTest {

    // 注入待测试的 Controller
    @InjectMocks
    private ResourcesController resourcesController;

    // 模拟 Service
    @Mock
    private ResourcesService resourcesService;

    // 模拟 MinioClient
    @Mock
    private MinioClient minioClient;

    // 模拟 MinIO 配置
    @Mock
    private MinIOInfo minIOInfo;

    @BeforeEach
    public void setup() {
        // 初始化 Mockito 注解
        MockitoAnnotations.openMocks(this);
    }

    //测试resourcesList方法
    @Test
    public void testResourcesList() {
        // 1. 构造参数
        Integer ownerId = 1001;
        ResourcesQueryDTO queryDTO = new ResourcesQueryDTO();
        queryDTO.setPage(1);
        queryDTO.setPageSize(10);
        //模拟 Service 返回
        Page<Resources> page = new Page<>();
        page.setTotal(10L);
        when(resourcesService.viewList(ownerId, queryDTO)).thenReturn(page);

        //2. 调用 Controller 方法
        R result = resourcesController.resourcesList(ownerId, queryDTO);

        //3. 断言
        assertEquals(R.OK().getCode(), result.getCode(), "返回码应正确");
    }

    //测试uploadResources方法
    @Test
    public void testUploadResources() throws Exception {
        // 1. 构造参数
        Integer ownerId = 1001;
        MultipartFile file = new MockMultipartFile("test.txt", "content".getBytes());
        String type = "courseware";
        String difficulty = "beginner";
        String permission = "public";
        //模拟MinioClient方法
        // 模拟桶不存在
        when(minioClient.bucketExists(any(BucketExistsArgs.class))).thenReturn(false);
        // 模拟创建桶（void 方法用 doNothing()）
        doNothing().when(minioClient).makeBucket(any(MakeBucketArgs.class));
        // 关键修复：返回空的 Iterable（ArrayList 实现了 Iterable）
        List<Result<Item>> emptyList = new ArrayList<>();
        when(minioClient.listObjects(any(ListObjectsArgs.class))).thenReturn(emptyList);
        // 模拟文件上传成功
        when(minioClient.putObject(any(PutObjectArgs.class))).thenReturn(new ObjectWriteResponse(null, null, null, null, null, null));

        //2. 调用 Controller 方法
        R result = resourcesController.uploadResources(ownerId, file, type, difficulty, permission);

        // 3. 断言
        assertEquals(R.OK().getCode(), result.getCode(), "上传应返回成功");
    }

    //测试 downloadResource 方法（简化，主要验证流程）
    @Test
    public void testDownloadResource() throws Exception {
        // 1. 构造参数
        Integer resourceId = 1001;
        Resources resource = new Resources();
        resource.setBucket("test-bucket");
        resource.setObjectKey("test-object.txt");
        // 模拟 Service 查询结果
        when(resourcesService.getById(resourceId)).thenReturn(resource);

        // 2. 创建模拟的 HttpServletResponse（关键！替代 null）
        MockHttpServletResponse response = new MockHttpServletResponse();

        // 3. 模拟 MinioClient 的 getObject 方法，返回一个空的输入流（避免流操作异常）
        // 注意：需要导入 io.minio.GetObjectResponse 和 java.io.ByteArrayInputStream
        when(minioClient.getObject(any(GetObjectArgs.class)))
                .thenReturn(new GetObjectResponse(
                        null, // 模拟文件内容
                        null, null, null, new ByteArrayInputStream("test content".getBytes())
                                ));

        // 4. 调用 Controller 方法，传入模拟的 response
        resourcesController.downloadResource(resourceId, response);

        // 5. 验证响应是否符合预期（可选，增强测试严谨性）
        assertEquals("attachment;filename=test-object.txt",
                response.getHeader("Content-disposition")); // 验证下载文件名
        assertEquals(200, response.getStatus()); // 验证响应状态码
    }

    //测试 previewResource 方法
    @Test
    public void testPreviewResource() throws Exception {
        //1. 构造参数
        Integer resourceId = 1;
        Resources resource = new Resources();
        resource.setBucket("test-bucket");
        resource.setObjectKey("test-object.png");
        resource.setName("test.png");
        when(resourcesService.getById(resourceId)).thenReturn(resource);

        //2. 调用 Controller 方法
        R result = resourcesController.previewResource(resourceId);

        //3. 断言
        assertEquals(R.OK().getCode(), result.getCode(), "预览应返回成功");
    }

    //测试 delResource 方法
    @Test
    public void testDelResource() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        //1. 构造参数
        Integer resourceId = 1;
        when(resourcesService.delResourceById(resourceId)).thenReturn(true);

        //2. 调用 Controller 方法
        R result = resourcesController.delResource(resourceId);

        //3. 断言
        assertEquals(R.OK().getCode(), result.getCode(), "删除应返回成功");
    }
}
