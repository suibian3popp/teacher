package course.controller;

import course.dto.ChapterCreateDTO;
import course.dto.ChapterDTO;
import course.dto.ChapterUpdateDTO;
import course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.example.teacherservice.response.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("service")
@RequiredArgsConstructor
public class ChapterController {

    private final CourseService courseService;

    @PostMapping("/chapters")
    public ResponseEntity<CommonResponse<ChapterDTO>> addChapter(@RequestBody ChapterCreateDTO chapterCreateDTO) {
        ChapterDTO newChapter = courseService.addChapter(chapterCreateDTO);
        return new ResponseEntity<>(CommonResponse.created(newChapter, "章节创建成功"), HttpStatus.CREATED);
    }

    @PutMapping("/chapters/update/{chapterId}")
    public CommonResponse<ChapterDTO> updateChapter(@PathVariable Integer chapterId, @RequestBody ChapterUpdateDTO chapterUpdateDTO) {
        System.out.println("进入编辑章节接口，chapterId：" + chapterId + "，参数：" + chapterUpdateDTO);
        ChapterDTO updatedChapter = courseService.updateChapter(chapterId, chapterUpdateDTO);
        return CommonResponse.success(updatedChapter);
    }

    @DeleteMapping("/chapters/{chapterId}")
    public CommonResponse<Void> deleteChapter(@PathVariable Integer chapterId) {
        courseService.deleteChapter(chapterId);
        return CommonResponse.success(null, "章节删除成功");
    }
} 