package course.service;

import course.dto.*;
import course.entity.Course;

import java.util.List;

public interface CourseService {

    List<CourseDTO> getCoursesByTeacherId(Integer teacherId);

    CourseDetailDTO getCourseDetailWithChapters(Integer courseId);

    Course createCourse(CourseCreateDTO courseCreateDTO);

    void deleteCourse(Integer courseId);

    ChapterDTO addChapter(ChapterCreateDTO chapterCreateDTO);

    ChapterDTO updateChapter(Integer chapterId, ChapterUpdateDTO chapterUpdateDTO);

    void deleteChapter(Integer chapterId);

//    void deleteResourceFromChapter(Integer resourceId, Integer chapterId);

    List<ResourceDTO> getResourcesByChapterId(Integer chapterId);

//    Integer createCourse(CourseCreateDTO courseCreateDTO, Integer teacherId);
}
