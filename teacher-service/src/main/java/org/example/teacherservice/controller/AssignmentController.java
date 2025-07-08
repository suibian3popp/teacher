package org.example.teacherservice.controller;

import org.example.teachercommon.entity.JwtUserInfo;
import org.example.teacherservice.dto.assignment.AssignmentCreateDTO;
import org.example.teacherservice.dto.assignment.AssignmentUpdateDTO;
import org.example.teacherservice.response.PageParam;
import org.example.teacherservice.response.PageResult;
import org.example.teacherservice.service.AssignmentService;
import org.example.teacherservice.util.UserContext;

import org.example.teacherservice.vo.AssignmentVO;
import org.example.teacherservice.vo.assignment.AssignmentBasicVO;
import org.example.teacherservice.vo.assignment.AssignmentResourceVO;
import org.example.teacherservice.vo.assignment.AssignmentSearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("service/assignment")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;


    /**
     * 创建作业
     * POST /service/assignment
     */
    @PostMapping
    public ResponseEntity<Integer> createAssignment(@RequestBody AssignmentCreateDTO dto) {
        System.out.println("进入创建作业");

        JwtUserInfo currentUser = UserContext.get();
        System.out.println("教师名字"+currentUser.getUsername());
        System.out.println("作业标题"+dto.getTitle());
        System.out.println("截止时间"+dto.getDeadline());
        System.out.println("作业描述"+dto.getDescription());
        System.out.println("资源描述"+dto.getResourceId());
        System.out.println("关联班级"+dto.getClassIds());
        System.out.println("总分"+dto.getTotalScore());
        Integer assignmentId = assignmentService.createAssignment(dto, currentUser.getUserId());
        return ResponseEntity.ok(assignmentId);
    }

    /**
     * 更新作业
     * PUT /service/assignment
     */
    @PutMapping
    public ResponseEntity<Void> updateAssignment(@RequestBody AssignmentUpdateDTO dto) {
        System.out.println("进入更新作业");
        System.out.println("作业标题"+dto.getTitle());
        assignmentService.updateAssignment(dto);
        return ResponseEntity.ok().build();
    }

    /**
     * 删除作业
     * DELETE /service/assignment/{assignmentId}
     */
    @DeleteMapping("/{assignmentId}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Integer assignmentId) {
        System.out.println("进入删除作业");
        System.out.println("作业ID"+assignmentId);
        assignmentService.deleteAssignment(assignmentId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 获取作业基础信息
     * GET /service/assignment/{assignmentId}/basic
     */
    @GetMapping("/{assignmentId}/basic")
    public ResponseEntity<AssignmentBasicVO> getAssignmentBasicInfo(
            @PathVariable Integer assignmentId) {
        System.out.println("获取作业基础信息");
        System.out.println(assignmentId);
        AssignmentBasicVO info = assignmentService.getAssignmentBasicInfo(assignmentId);
        return ResponseEntity.ok(info);
    }

    /**
     * 检查作业是否存在
     * GET /service/assignment/{assignmentId}/exists
     */
    @GetMapping("/{assignmentId}/exists")
    public ResponseEntity<Boolean> existsById(@PathVariable Integer assignmentId) {
        System.out.println("检查作业是否存在");
        System.out.println("作业ID"+assignmentId);
        boolean exists = assignmentService.existsById(assignmentId);
        return ResponseEntity.ok(exists);
    }

    /**
     * 根据发布人ID分页查询作业
     * GET /service/assignment/creator?page=1&size=10
     */
    @GetMapping("/creator")
    public ResponseEntity<PageResult<AssignmentVO>> searchByCreator(
            PageParam pageParam) {
        System.out.println("进入根据发布者ID分页查询作业");
        try {
            System.out.println("搜索当前用户发布的作业，分页搜索");
            JwtUserInfo currentUser = UserContext.get();
            System.out.println("发布者ID"+currentUser.getUserId());
            if (currentUser == null) {
                System.out.println("未获取到当前用户信息");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            System.out.println("获取当前发布人ID: " + currentUser.getUserId());
            PageResult<AssignmentVO> result = assignmentService.searchByCreator(
                    currentUser.getUserId(), pageParam);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            System.out.println("查询用户发布的作业时发生异常: " + e.getMessage());
            e.printStackTrace(); // 打印异常堆栈信息
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 根据资源ID查询作业
     * GET /service/assignment/resource/{resourceId}
     */
    @GetMapping("/resource/{resourceId}")
    public ResponseEntity<List<AssignmentResourceVO>> searchByResource(
            @PathVariable Integer resourceId) {
        System.out.println("根据资源ID查询作业");
        System.out.println("资源ID："+resourceId);
        List<AssignmentResourceVO> result = assignmentService.searchByResource(resourceId);
        return ResponseEntity.ok(result);
    }

    /**
     * 综合搜索作业
     * GET /service/assignment/search?titleKeyword=xxx&status=1
     */
    @GetMapping("/search")
    public ResponseEntity<List<AssignmentSearchResult>> searchAssignments(
            @RequestParam(required = false) String titleKeyword,
            @RequestParam(required = false) Integer creatorId,
            @RequestParam(required = false) Integer status) {
        System.out.println("综合搜索作业");
        System.out.println("标题关键字"+titleKeyword);
        if(titleKeyword == null){}
        System.out.println("创建者ID"+creatorId);
        System.out.println("作业状态"+status);
        List<AssignmentSearchResult> result = assignmentService.searchAssignments(
                titleKeyword, creatorId, status);
        return ResponseEntity.ok(result);
    }

    /**
     * 测试接口（保留原功能）
     * GET /service/assignment/a
     */
    @GetMapping("a")
    public ResponseEntity<Boolean> isAssigned() {
        System.out.println("进入测试");
        JwtUserInfo currentUser = UserContext.get();
        System.out.println(currentUser.getUserId());
        System.out.println(currentUser.getUsername());
        System.out.println(currentUser.getUserType());
        return ResponseEntity.ok(true);
    }
}