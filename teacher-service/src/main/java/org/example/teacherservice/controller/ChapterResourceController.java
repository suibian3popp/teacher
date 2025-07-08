//package org.example.teacherservice.controller;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import jakarta.annotation.Resource;
//import org.example.teacherservice.dto.ResourcesQueryDTO;
//import org.example.teacherservice.entity.ChapterResources;
//import org.example.teacherservice.entity.Chapters;
//import org.example.teacherservice.entity.Courses;
//import org.example.teacherservice.entity.Resources;
//import org.example.teacherservice.response.R;
//import org.example.teacherservice.service.ChapterResourcesService;
//import org.example.teacherservice.service.ChaptersService;
//import org.example.teacherservice.service.CoursesService;
//import org.example.teacherservice.service.ResourcesService;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/chapter-resource")
//public class ChapterResourceController {
//    @Resource
//    private ChapterResourcesService chapterResourcesService;
//
//    @Resource
//    private ResourcesService resourcesService;
//
//    @Resource
//    private ChaptersService chaptersService;
//
//    @Resource
//    private CoursesService coursesService;
//
//    //查询章节可关联列表
//    @GetMapping("/list-resource/{chapterId}")
//    public R listAvaialableResourcesForChhapter(
//            @PathVariable Integer chapterId,
//            @RequestParam(defaultValue = "1") Integer page,
//            @RequestParam(defaultValue = "10") Integer pageSize){
//        Chapters chapter = chaptersService.getById(chapterId);
//        if (chapter == null) {
//            return R.FAIL();
//        }
//
//        ResourcesQueryDTO resourcesQueryDTO = new ResourcesQueryDTO();
//        resourcesQueryDTO.setPage(page);
//        resourcesQueryDTO.setPageSize(pageSize);
//
//        Courses course = coursesService.getById(chapter.getCourseId());
//        if (course == null) {
//            return R.FAIL();
//        }
//        Integer ownerId=course.getTeacherId();
//
//        Page<Resources> resourcePage = resourcesService.viewList(ownerId,resourcesQueryDTO);
//        return R.OK(resourcePage);
//    }
//
//    //关联资源到章节
//    @PostMapping("/associate")
//    public R associateResourceToChapter(@RequestBody ChapterResources chapterResources){
//        if (chapterResources.getChapterId() == null || chapterResources.getResourceId() == null) {
//            return R.FAIL();
//        }
//        boolean success = chapterResourcesService.save(chapterResources);
//        return success ? R.OK("关联成功") : R.FAIL();
//    }
//
//    //查询章节已关联资源
//    @GetMapping("/list-associated")
//    public R listAssociatedResourcesForChapter(@RequestParam Integer chapterId) {
//        List<ChapterResources> associatedList = chapterResourcesService.list(
//                new QueryWrapper<ChapterResources>().eq("chapter_id", chapterId)
//                        .orderByAsc("display_order") //按显示顺序排序
//        );
//        return R.OK(associatedList);
//    }
//
//    //解除章节与资源关联
//    @DeleteMapping("/disassociate")
//    public R disassociateResourceFromChapter(@RequestParam Integer id) {
//        boolean success = chapterResourcesService.removeById(id);
//        return success ? R.OK("解除关联成功") : R.FAIL();
//    }
//
//
//}