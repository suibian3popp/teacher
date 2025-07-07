package org.example.teacherservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.teacherservice.entity.Student;
import org.example.teacherservice.entity.StudentClass;
import org.example.teacherservice.mapper.StudentClassMapper;
import org.example.teacherservice.mapper.StudentMapper;
import org.example.teacherservice.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Autowired
    private StudentClassMapper studentClassMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer createStudent(Student student) {
        // 参数校验
        if (student == null) {
            throw new IllegalArgumentException("学生信息不能为空");
        }
        if (!StringUtils.hasText(student.getStudentNo())) {
            throw new IllegalArgumentException("学号不能为空");
        }
        if (!StringUtils.hasText(student.getRealName())) {
            throw new IllegalArgumentException("学生姓名不能为空");
        }

        // 检查学号是否已存在
        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Student::getStudentNo, student.getStudentNo());
        if (this.count(queryWrapper) > 0) {
            throw new RuntimeException("学号已存在");
        }

        // 保存学生信息
        if (!this.save(student)) {
            throw new RuntimeException("创建学生失败");
        }
        return student.getStudentId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStudent(Student student) {
        // 参数校验
        if (student == null || student.getStudentId() == null) {
            throw new IllegalArgumentException("学生ID不能为空");
        }

        // 获取原始数据
        Student existingStudent = this.getById(student.getStudentId());
        if (existingStudent == null) {
            throw new RuntimeException("学生不存在");
        }

        // 合并更新数据
        Student mergedStudent = mergeStudentFields(existingStudent, student);

        // 执行更新
        if (!this.updateById(mergedStudent)) {
            throw new RuntimeException("更新学生失败");
        }
    }

    private Student mergeStudentFields(Student existing, Student update) {
        Student merged = new Student();
        merged.setStudentId(existing.getStudentId());

        // 学号不能修改
        merged.setStudentNo(existing.getStudentNo());

        // 其他字段处理
        merged.setRealName(StringUtils.hasText(update.getRealName()) ?
                update.getRealName() : existing.getRealName());
        merged.setDepartmentId(update.getDepartmentId() != null ?
                update.getDepartmentId() : existing.getDepartmentId());

        return merged;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStudent(Integer studentId) {
        // 参数校验
        if (studentId == null) {
            throw new IllegalArgumentException("学生ID不能为空");
        }

        // 检查学生是否存在
        if (!this.existsById(studentId)) {
            throw new RuntimeException("学生不存在");
        }

        // 删除关联的选课记录
        LambdaQueryWrapper<StudentClass> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StudentClass::getStudentId, studentId);
        studentClassMapper.delete(wrapper);

        // 删除学生
        if (!this.removeById(studentId)) {
            throw new RuntimeException("删除学生失败");
        }
    }

    @Override
    public Student getStudentById(Integer studentId) {
        // 参数校验
        if (studentId == null) {
            throw new IllegalArgumentException("学生ID不能为空");
        }

        Student student = this.getById(studentId);
        if (student == null) {
            throw new RuntimeException("学生不存在");
        }
        return student;
    }

    @Override
    public Student getStudentByNo(String studentNo) {
        // 参数校验
        if (!StringUtils.hasText(studentNo)) {
            throw new IllegalArgumentException("学号不能为空");
        }

        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Student::getStudentNo, studentNo);
        Student student = this.getOne(wrapper);

        if (student == null) {
            throw new RuntimeException("学生不存在");
        }
        return student;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void selectCourse(StudentClass studentClass) {
        // 参数校验
        if (studentClass == null || studentClass.getStudentId() == null
                || studentClass.getClassId() == null || studentClass.getCourseId() == null) {
            throw new IllegalArgumentException("选课信息不完整");
        }

        // 检查学生是否存在
        if (!this.existsById(studentClass.getStudentId())) {
            throw new RuntimeException("学生不存在");
        }

        // 检查是否已选同课程的其他班级
        LambdaQueryWrapper<StudentClass> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StudentClass::getStudentId, studentClass.getStudentId())
                .eq(StudentClass::getCourseId, studentClass.getCourseId());
        if (studentClassMapper.exists(wrapper)) {
            throw new RuntimeException("该学生已选本课程的其它班级");
        }

        // 插入选课记录
        if (studentClassMapper.insert(studentClass) <= 0) {
            throw new RuntimeException("选课失败");
        }
    }

    @Override
    public List<StudentClass> getStudentCourses(Integer studentId) {
        // 参数校验
        if (studentId == null) {
            throw new IllegalArgumentException("学生ID不能为空");
        }

        // 检查学生是否存在
        if (!this.existsById(studentId)) {
            throw new RuntimeException("学生不存在");
        }

        LambdaQueryWrapper<StudentClass> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StudentClass::getStudentId, studentId);
        return studentClassMapper.selectList(wrapper);
    }

    @Override
    public List<Student> searchStudents(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            throw new IllegalArgumentException("搜索关键词不能为空");
        }

        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Student::getStudentNo, keyword)
                .or()
                .like(Student::getRealName, keyword);
        return this.list(wrapper);
    }

    @Override
    public List<Student> searchStudentsByDepartment(Integer departmentId, String keyword) {
        // 参数校验
        if (departmentId == null) {
            throw new IllegalArgumentException("院系ID不能为空");
        }
        if (!StringUtils.hasText(keyword)) {
            return this.list(new LambdaQueryWrapper<Student>()
                    .eq(Student::getDepartmentId, departmentId));
        }

        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Student::getDepartmentId, departmentId)
                .and(qw -> qw.like(Student::getStudentNo, keyword)
                        .or()
                        .like(Student::getRealName, keyword));
        return this.list(wrapper);
    }

    /**
     * 检查学生是否存在
     * @param studentId 学生ID
     * @return 存在返回true，否则false
     */
    public boolean existsById(Integer studentId) {
        if (studentId == null) {
            return false;
        }
        return this.getById(studentId) != null;
    }
}
