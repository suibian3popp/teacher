package org.example.teacherservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.example.teacherservice.dto.*;
import org.example.teacherservice.entity.Chapter;
import org.example.teacherservice.entity.ChapterResource;
import org.example.teacherservice.entity.Course;
import org.example.teacherservice.entity.Resource;
import org.example.teacherservice.exception.BusinessException;
import org.example.teacherservice.mapper.*;
import org.example.teacherservice.response.ResponseCode;
import org.example.teacherservice.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseMapper courseMapper;
    private final ChapterMapper chapterMapper;
    private final ResourceMapper resourceMapper;
    private final ChapterResourceMapper chapterResourceMapper;
    private final UsersMapper userMapper;

    @Override
    public List<CourseDTO> getCoursesByTeacherId(Integer teacherId) {
        List<Course> courses = courseMapper.selectList(new QueryWrapper<Course>().eq("teacher_id", teacherId));
        if (courses.isEmpty()) {
            return Collections.emptyList();
        }

        List<Integer> courseIds = courses.stream().map(Course::getCourseId).collect(Collectors.toList());
        List<Integer> assistantIds = courses.stream()
                .map(Course::getAssistantId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        Map<Integer, Long> chapterCounts = getChapterCounts(courseIds);
        Map<Integer, String> assistantNames = getAssistantNames(assistantIds);

        return courses.stream()
                .map(course -> convertToCourseDTO(course, chapterCounts.getOrDefault(course.getCourseId(), 0L).intValue(),
                        assistantNames.get(course.getAssistantId())))
                .collect(Collectors.toList());
    }

    private Map<Integer, Long> getChapterCounts(List<Integer> courseIds) {
        if (courseIds.isEmpty()) {
            return Collections.emptyMap();
        }
        return chapterMapper.countChaptersByCourseIds(courseIds).stream()
                .collect(Collectors.toMap(
                        map -> (Integer) map.get("course_id"),
                        map -> (Long) map.get("chapter_count")));
    }

    private Map<Integer, String> getAssistantNames(List<Integer> assistantIds) {
        if (assistantIds.isEmpty()) {
            return Collections.emptyMap();
        }
        return userMapper.selectBatchIds(assistantIds).stream()
                .collect(Collectors.toMap(org.example.teacherservice.entity.Users::getUserId, org.example.teacherservice.entity.Users::getRealName));
    }


    @Override
    public CourseDetailDTO getCourseDetailWithChapters(Integer courseId) {
        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            throw new BusinessException(ResponseCode.NOT_FOUND, "未找到指定课程");
        }

        // 1. Fetch all chapters for the course
        List<Chapter> allChapters = chapterMapper.selectList(new QueryWrapper<Chapter>().eq("course_id", courseId));

        // 2. Fetch all resources for these chapters in one go, if chapters exist
        Map<Integer, List<ResourceDTO>> chapterResourcesMap = new HashMap<>();
        if (!allChapters.isEmpty()) {
            List<Integer> chapterIds = allChapters.stream().map(Chapter::getChapterId).collect(Collectors.toList());
            List<Resource> resources = resourceMapper.findResourcesByChapterIds(chapterIds);
            Map<Integer, Resource> resourceMap = resources.stream().collect(Collectors.toMap(Resource::getResourceId, r -> r));

            // 3. Get chapter-resource relations
            List<ChapterResource> relations = chapterResourceMapper.selectList(new QueryWrapper<ChapterResource>().in("chapter_id", chapterIds));
            chapterResourcesMap = relations.stream()
                    .collect(Collectors.groupingBy(ChapterResource::getChapterId,
                            Collectors.mapping(rel -> convertToResourceDTO(resourceMap.get(rel.getResourceId())), Collectors.toList())));
        }

        // 4. Build chapter DTO hierarchy
        final Map<Integer, List<ResourceDTO>> finalChapterResourcesMap = chapterResourcesMap;
        List<ChapterDTO> chapterDTOs = allChapters.stream()
                .map(chapter -> convertToChapterDTO(chapter, finalChapterResourcesMap.getOrDefault(chapter.getChapterId(), new ArrayList<>())))
                .collect(Collectors.toList());

        List<ChapterDTO> hierarchicalChapters = buildChapterHierarchy(chapterDTOs);

        // 5. Build final CourseDetailDTO
        CourseDetailDTO courseDetailDTO = new CourseDetailDTO();
        courseDetailDTO.setCourseId(course.getCourseId());
        courseDetailDTO.setCourseName(course.getName());
        courseDetailDTO.setTeacherId(course.getTeacherId());
        courseDetailDTO.setCourseType(course.getCourseType());
        courseDetailDTO.setChapters(hierarchicalChapters);

        // Handle cover image
        if (course.getCoverImageResource() != null) {
            Resource coverImage = resourceMapper.selectById(course.getCoverImageResource());
            if (coverImage != null) {
                courseDetailDTO.setCoverImage("/storage/resources/" + coverImage.getObjectKey());
            }
        }

        return courseDetailDTO;
    }

    @Override
    public ChapterDTO addChapter(ChapterCreateDTO chapterCreateDTO) {
        Chapter chapter = new Chapter();
        chapter.setCourseId(chapterCreateDTO.getCourseId());
        chapter.setParentId(chapterCreateDTO.getParentId());
        chapter.setTitle(chapterCreateDTO.getTitle());
        Integer maxOrderNum = chapterMapper.findMaxOrderNumByParent(chapterCreateDTO.getCourseId(), chapterCreateDTO.getParentId());
        chapter.setOrderNum(maxOrderNum == null ? 1 : maxOrderNum + 1);

        chapterMapper.insert(chapter);

        // Convert back to DTO to return the created object with its new ID
        return convertToChapterDTO(chapter, new ArrayList<>());
    }

    @Override
    public ChapterDTO updateChapter(Integer chapterId, ChapterUpdateDTO chapterUpdateDTO) {
        Chapter chapter = chapterMapper.selectById(chapterId);
        if (chapter == null) {
            // Or throw a more specific "NotFoundException"
            return null;
        }
        chapter.setTitle(chapterUpdateDTO.getChapterName());
        chapterMapper.updateById(chapter);

        // Fetching resources for the chapter to return a complete DTO
        List<ResourceDTO> resources = getResourcesByChapterId(chapterId);
        ChapterDTO chapterDTO = convertToChapterDTO(chapter, resources);

        // We need to find the level for the DTO
        // This is complex without fetching the whole tree. For now, we leave it null.
        chapterDTO.setLevel(null);

        return chapterDTO;
    }

    @Override
    public void deleteChapter(Integer chapterId) {
        Chapter chapterToDelete = chapterMapper.selectById(chapterId);
        if (chapterToDelete == null) {
            throw new BusinessException(ResponseCode.NOT_FOUND, "未找到指定章节");
        }
        // Check if the chapter has any children
        long childrenCount = chapterMapper.selectCount(new QueryWrapper<Chapter>().eq("parent_id", chapterId));
        if (childrenCount > 0) {
            throw new BusinessException(ResponseCode.BUSINESS_ERROR, "章节下存在子章节，无法删除");
        }

        chapterMapper.deleteById(chapterId);

        chapterMapper.reorderChaptersAfterDeletion(chapterToDelete.getParentId(), chapterToDelete.getOrderNum());
    }

    @Override
    public List<ResourceDTO> getResourcesByChapterId(Integer chapterId) {
        QueryWrapper<ChapterResource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chapter_id", chapterId);
        List<ChapterResource> relations = chapterResourceMapper.selectList(queryWrapper);

        if (relations.isEmpty()) {
            return new ArrayList<>();
        }

        List<Integer> resourceIds = relations.stream()
                .map(ChapterResource::getResourceId)
                .collect(Collectors.toList());

        List<Resource> resources = resourceMapper.selectBatchIds(resourceIds);
        return resources.stream()
                .map(this::convertToResourceDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Course createCourse(CourseCreateDTO courseCreateDTO) {
        Course course = Course.builder()
                .name(courseCreateDTO.getName())
                .teacherId(courseCreateDTO.getTeacherId())
                .assistantId(courseCreateDTO.getAssistantId())
                .departmentId(courseCreateDTO.getDepartmentId())
                .semester(courseCreateDTO.getSemester())
                .description(courseCreateDTO.getDescription())
                .courseType(courseCreateDTO.getCourseType())
                .coverImageResource(courseCreateDTO.getCoverImageResource())
                .build();
        courseMapper.insert(course);
        return course;
    }

    @Override
    public void deleteCourse(Integer courseId) {
        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            throw new BusinessException(ResponseCode.NOT_FOUND);
        }
        courseMapper.deleteById(courseId);
    }

    private List<ChapterDTO> buildChapterHierarchy(List<ChapterDTO> flatList) {
        Map<Integer, ChapterDTO> map = flatList.stream().collect(Collectors.toMap(ChapterDTO::getChapterId, dto -> dto));
        List<ChapterDTO> topLevel = new ArrayList<>();

        for (ChapterDTO dto : flatList) {
            dto.setChildren(new ArrayList<>()); // Initialize children list
            if (dto.getParentId() == null || dto.getParentId() == 0) {
                topLevel.add(dto);
                dto.setLevel(0);
            } else {
                ChapterDTO parent = map.get(dto.getParentId());
                if (parent != null) {
                    parent.getChildren().add(dto);
                    dto.setLevel(parent.getLevel() + 1);
                } else {
                    // Orphan chapter, treat as top-level for robustness
                    topLevel.add(dto);
                    dto.setLevel(0);
                }
            }
        }

        // Sort chapters and their children by order number
        sortChapters(topLevel);

        return topLevel;
    }

    private void sortChapters(List<ChapterDTO> chapters) {
        if (chapters == null || chapters.isEmpty()) {
            return;
        }
        chapters.sort(java.util.Comparator.comparing(ChapterDTO::getOrderNum));
        for (ChapterDTO chapter : chapters) {
            sortChapters(chapter.getChildren());
        }
    }

    private ChapterDTO convertToChapterDTO(Chapter chapter, List<ResourceDTO> resources) {
        ChapterDTO dto = new ChapterDTO();
        dto.setChapterId(chapter.getChapterId());
        dto.setParentId(chapter.getParentId());
        dto.setTitle(chapter.getTitle());
        dto.setOrderNum(chapter.getOrderNum());
        dto.setResources(resources != null ? resources : new ArrayList<>());
        return dto;
    }

    private ResourceDTO convertToResourceDTO(Resource resource) {
        if (resource == null) return null;
        ResourceDTO dto = new ResourceDTO();
        dto.setResourceId(resource.getResourceId());
        dto.setName(resource.getName());
        dto.setType(resource.getType());
        dto.setUrl("/storage/resources/" + resource.getObjectKey()); // Example path
        return dto;
    }

    private CourseDTO convertToCourseDTO(Course course, Integer chapterCount, String assistantName) {
        CourseDTO dto = new CourseDTO();
        dto.setCourseId(course.getCourseId());
        dto.setCourseName(course.getName());
        dto.setCourseType(course.getCourseType());
        dto.setTeacherId(course.getTeacherId());
        dto.setChapterCount(chapterCount);
        dto.setAssistantRealName(assistantName);

        if (course.getCoverImageResource() != null) {
            Resource coverImage = resourceMapper.selectById(course.getCoverImageResource());
            if (coverImage != null) {
                dto.setCoverImage("/storage/resources/" + coverImage.getObjectKey());
            }
        }

        return dto;
    }
}
