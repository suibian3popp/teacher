package org.example.teacherservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.teacherservice.entity.StudentClass;
import org.example.teacherservice.mapper.ClassMapper;
import org.example.teacherservice.mapper.StudentClassMapper;
import org.example.teacherservice.service.ClassService;
import org.example.teacherservice.entity.Class;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements ClassService {
    @Autowired
    private StudentClassMapper studentClassMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer createClass(Class clazz) {
        // 参数校验
        if (clazz == null) {
            throw new IllegalArgumentException("班级信息不能为空");
        }
        if (clazz.getCourseId() == null) {
            throw new IllegalArgumentException("课程ID不能为空");
        }
        if (!StringUtils.hasText(clazz.getName())) {
            throw new IllegalArgumentException("班级名称不能为空");
        }

        // 设置初始值
        clazz.setStudentCount(0); // 新班级初始人数为0
        if (!StringUtils.hasText(clazz.getDescription())) {
            clazz.setDescription(""); // 确保描述不为null
        }

        // 保存班级信息
        if (!this.save(clazz)) {
            throw new RuntimeException("创建班级失败");
        }
        return clazz.getClassId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateClass(Class clazz) {
        // 参数校验
        if (clazz == null || clazz.getClassId() == null) {
            throw new IllegalArgumentException("班级ID不能为空");
        }

        // 获取原始数据
        Class existing = this.getById(clazz.getClassId());
        if (existing == null) {
            throw new RuntimeException("班级不存在");
        }

        // 合并更新数据（保留原有非空字段）
        Class merged = mergeClassFields(existing, clazz);

        // 执行更新
        if (!this.updateById(merged)) {
            throw new RuntimeException("更新班级失败");
        }
    }

    private Class mergeClassFields(Class existing, Class update) {
        Class merged = new Class();
        merged.setClassId(existing.getClassId());

        // 必填字段
        merged.setCourseId(update.getCourseId() != null ? update.getCourseId() : existing.getCourseId());
        merged.setName(StringUtils.hasText(update.getName()) ? update.getName() : existing.getName());

        // 可选字段
        merged.setDescription(StringUtils.hasText(update.getDescription()) ?
                update.getDescription() : existing.getDescription());
        merged.setStudentCount(update.getStudentCount() != null ?
                update.getStudentCount() : existing.getStudentCount());

        return merged;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteClass(Integer classId) {
        // 参数校验
        if (classId == null) {
            throw new IllegalArgumentException("班级ID不能为空");
        }

        // 检查班级是否存在
        if (!this.existsById(classId)) {
            throw new RuntimeException("班级不存在");
        }

        // 删除关联的选课记录
        LambdaQueryWrapper<StudentClass> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StudentClass::getClassId, classId);
        studentClassMapper.delete(wrapper);

        // 删除班级
        if (!this.removeById(classId)) {
            throw new RuntimeException("删除班级失败");
        }
    }

    @Override
    public Class getClassById(Integer classId) {
        // 参数校验
        if (classId == null) {
            throw new IllegalArgumentException("班级ID不能为空");
        }

        Class clazz = this.getById(classId);
        if (clazz == null) {
            throw new RuntimeException("班级不存在");
        }
        return clazz;
    }

    @Override
    public List<Class> getClassesByCourse(Integer courseId) {
        // 参数校验
        if (courseId == null) {
            throw new IllegalArgumentException("课程ID不能为空");
        }

        LambdaQueryWrapper<Class> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Class::getCourseId, courseId);
        return this.list(wrapper);
    }

    @Override
    public List<StudentClass> getClassStudents(Integer classId) {
        // 参数校验
        if (classId == null) {
            throw new IllegalArgumentException("班级ID不能为空");
        }

        // 检查班级是否存在
        if (!this.existsById(classId)) {
            throw new RuntimeException("班级不存在");
        }

        LambdaQueryWrapper<StudentClass> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StudentClass::getClassId, classId);
        return studentClassMapper.selectList(wrapper);
    }

    @Override
    public List<Class> searchClasses(Integer courseId, String keyword) {
        // 参数校验
        if (courseId == null) {
            throw new IllegalArgumentException("课程ID不能为空");
        }

        LambdaQueryWrapper<Class> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Class::getCourseId, courseId);

        if (StringUtils.hasText(keyword)) {
            wrapper.and(qw -> qw.like(Class::getName, keyword)
                    .or()
                    .like(Class::getDescription, keyword));
        }

        return this.list(wrapper);
    }

    @Override
    public int countClassesByCourse(Integer courseId) {
        // 参数校验
        if (courseId == null) {
            throw new IllegalArgumentException("课程ID不能为空");
        }

        LambdaQueryWrapper<Class> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Class::getCourseId, courseId);
        long count = count(wrapper);
        return (int) count; // 显式转换为int
    }

    /**
     * 检查班级是否存在
     * @param classId 班级ID
     * @return 存在返回true，否则false
     */
    public boolean existsById(Integer classId) {
        if (classId == null) {
            return false;
        }
        return this.getById(classId) != null;
    }
}
