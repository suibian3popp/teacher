package org.example.teacherservice.service;

import org.example.teacherservice.entity.Student;
import org.example.teacherservice.entity.StudentClass;

import java.util.List;

public interface StudentService {
    /**
     * 创建学生信息
     * @param student 学生实体
     * @return 创建的学生ID
     */
    Integer createStudent(Student student);

    /**
     * 更新学生信息
     * @param student 学生实体
     */
    void updateStudent(Student student);

    /**
     * 删除学生
     * @param studentId 学生ID
     */
    void deleteStudent(Integer studentId);

    /**
     * 获取学生详情
     * @param studentId 学生ID
     * @return 学生实体
     */
    Student getStudentById(Integer studentId);

    /**
     * 根据学号查询学生
     * @param studentNo 学号
     * @return 学生实体
     */
    Student getStudentByNo(String studentNo);

    /**
     * 学生选课
     * @param studentClass 学生选课关系
     */
    void selectCourse(StudentClass studentClass);

    /**
     * 获取学生所选课程列表
     * @param studentId 学生ID
     * @return 学生选课列表
     */
    List<StudentClass> getStudentCourses(Integer studentId);

    /**
     * 搜索学生(模糊查询)
     * @param keyword 搜索关键词(可匹配学号或姓名)
     * @return 匹配的学生列表
     */
    List<Student> searchStudents(String keyword);

    /**
     * 根据院系搜索学生
     * @param departmentId 院系ID
     * @param keyword 可选搜索关键词
     * @return 匹配的学生列表
     */
    List<Student> searchStudentsByDepartment(Integer departmentId, String keyword);

    /**
     * 检查学生是否存在
     * @param studentId 学生ID
     * @return 存在返回true，否则false
     */
    boolean existsById(Integer studentId);
}
