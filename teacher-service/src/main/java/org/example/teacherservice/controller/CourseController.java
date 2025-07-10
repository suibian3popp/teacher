package org.example.teacherservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.teacherservice.dto.*;
import org.example.teacherservice.entity.Course;
import org.example.teacherservice.response.CommonResponse;
import org.example.teacherservice.service.CourseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public CommonResponse<Course> createCourse(@RequestBody CourseCreateDTO courseCreateDTO) {
        Course newCourse = courseService.createCourse(courseCreateDTO);
        return CommonResponse.success(newCourse, "课程创建成功");
    }

    @DeleteMapping("/{courseId}")
    public CommonResponse<Void> deleteCourse(@PathVariable Integer courseId) {
        courseService.deleteCourse(courseId);
        return CommonResponse.success(null, "课程删除成功");
    }

    // 1. 获取教师课程列表
    @GetMapping("/teacher/{teacherId}")
    public CommonResponse<List<CourseDTO>> getTeacherCourses(@PathVariable Integer teacherId) {
        System.out.println("进入获取教师课程列表"+"teacherId: " + teacherId);
        List<CourseDTO> courses = courseService.getCoursesByTeacherId(teacherId);
        return CommonResponse.success(courses);
    }

    // 4. 获取课程详情（含章节结构）
    @GetMapping("/{courseId}/chapters")
    public CommonResponse<List<ChapterDTO>> getCourseChapters(@PathVariable Integer courseId) {
        CourseDetailDTO courseDetail = courseService.getCourseDetailWithChapters(courseId);
        return CommonResponse.success(courseDetail.getChapters(), "成功获取章节列表");
    }

    // 5. 添加章节
//    @PostMapping("/courses/{courseId}/chapters")
//    public CommonResponse<Void> addChapter(@PathVariable Integer courseId, @RequestBody ChapterCreateDTO chapterCreateDTO) {
//        courseService.addChapter(courseId, chapterCreateDTO);
//        return CommonResponse.success(null, "章节添加成功");
//    }

    // 9. 获取章节资源列表
    @GetMapping("/chapters/{chapterId}/resources")
    public CommonResponse<List<ResourceDTO>> getChapterResources(@PathVariable Integer chapterId) {
        System.out.println("进入获取章节资源列表接口，chapterId：" + chapterId);
        List<ResourceDTO> resources = courseService.getResourcesByChapterId(chapterId);
        return CommonResponse.success(resources);
    }
}
