package org.example.teacherservice.service.impl;


import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.teacherservice.entity.Classes;
import org.example.teacherservice.entity.StudentClass;
import org.example.teacherservice.mapper.ClassesMapper;
import org.example.teacherservice.mapper.StudentClassMapper;
import org.example.teacherservice.service.ClassService;
import org.example.teacherservice.service.ClassStudentService;
import org.example.teacherservice.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClassStudentServiceImpl implements ClassStudentService {
    @Autowired
    private StudentClassMapper studentClassMapper;
    @Autowired
    private StudentService studentService;
    @Autowired
    private ClassService classService;
    @Autowired
    private ClassesMapper classesMapper;




    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchAddStudentsToClass(Integer classId, List<Integer> studentIds, Integer courseId) {
        // 参数校验
        if (classId == null) {
            throw new IllegalArgumentException("班级ID不能为空");
        }
        if (courseId == null) {
            throw new IllegalArgumentException("课程ID不能为空");
        }
        if (CollectionUtils.isEmpty(studentIds)) {
            throw new IllegalArgumentException("学生ID列表不能为空");
        }

        // 检查班级是否存在
        if (!classService.existsById(classId)) {
            throw new RuntimeException("班级不存在");
        }

        int successCount = 0;

        for (Integer studentId : studentIds) {
            try {
                // 检查学生是否存在
                if (!studentService.existsById(studentId)) {
                    continue;
                }

                // 检查是否已选同课程的其他班级
                if (hasStudentSelectedSameCourse(studentId, courseId)) {
                    continue;
                }

                // 创建选课记录
                StudentClass studentClass = new StudentClass();
                studentClass.setStudentId(studentId);
                studentClass.setClassId(classId);
                studentClass.setCourseId(courseId);

                // 插入记录并计数
                if (studentClassMapper.insert(studentClass) > 0) {
                    successCount++;
                }
            } catch (Exception e) {
                // 单个学生添加失败不影响其他学生
                continue;
            }
        }
        classService.updateCountById(classId,successCount);
        return successCount;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeStudentFromClass(Integer classId, Integer studentId) {
        // 参数校验
        if (classId == null) {
            throw new IllegalArgumentException("班级ID不能为空");
        }
        if (studentId == null) {
            throw new IllegalArgumentException("学生ID不能为空");
        }

        // 检查关联是否存在
        LambdaQueryWrapper<StudentClass> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StudentClass::getClassId, classId)
                .eq(StudentClass::getStudentId, studentId);
        //更新班级人数
        classService.updateCountById(classId,-1);
        if (studentClassMapper.delete(wrapper) <= 0) {
            throw new RuntimeException("删除学生班级关联失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchRemoveStudentsFromClass(Integer classId, List<Integer> studentIds) {
        // 参数校验
        if (classId == null) {
            throw new IllegalArgumentException("班级ID不能为空");
        }
        if (CollectionUtils.isEmpty(studentIds)) {
            throw new IllegalArgumentException("学生ID列表不能为空");
        }

        // 检查班级是否存在
        if (!classService.existsById(classId)) {
            throw new RuntimeException("班级不存在");
        }

        LambdaQueryWrapper<StudentClass> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StudentClass::getClassId, classId)
                .in(StudentClass::getStudentId, studentIds);

        int deletedCount = studentClassMapper.delete(wrapper);
        //更新班级人数
        classService.updateCountById(classId,deletedCount);

        if (deletedCount <= 0) {
            throw new RuntimeException("批量删除学生班级关联失败");
        }
        return deletedCount;
    }

    @Override
    public boolean hasStudentSelectedSameCourse(Integer studentId, Integer courseId) {
        // 参数校验
        if (studentId == null) {
            throw new IllegalArgumentException("学生ID不能为空");
        }
        if (courseId == null) {
            throw new IllegalArgumentException("课程ID不能为空");
        }

        LambdaQueryWrapper<StudentClass> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StudentClass::getStudentId, studentId)
                .eq(StudentClass::getCourseId, courseId);
        return studentClassMapper.exists(wrapper);
    }
}
