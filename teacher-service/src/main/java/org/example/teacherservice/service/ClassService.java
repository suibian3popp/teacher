package org.example.teacherservice.service;

import org.example.teacherservice.entity.StudentClass;
import org.example.teacherservice.entity.Classes;

import java.util.List;

public interface ClassService {
    /**
     * 创建班级
     * @param clazz 班级实体
     * @return 创建的班级ID
     */
    Integer createClass(Classes clazz);

    /**
     * 更新班级信息
     * @param clazz 班级实体
     */
    void updateClass(Classes clazz);

    /**
     * 删除班级
     * @param classId 班级ID
     */
    void deleteClass(Integer classId);

    /**
     * 获取班级详情
     * @param classId 班级ID
     * @return 班级实体
     */
    Classes getClassById(Integer classId);

    /**
     * 获取课程下的所有班级
     * @param courseId 课程ID
     * @return 班级列表
     */
    List<Classes> getClassesByCourse(Integer courseId);

    /**
     * 获取班级学生列表
     * @param classId 班级ID
     * @return 学生选课关系列表
     */
    List<StudentClass> getClassStudents(Integer classId);

    /**
     * 搜索班级(模糊查询)
     * @param courseId 课程ID
     * @param keyword 搜索关键词(可匹配班级名称或描述)
     * @return 匹配的班级列表
     */
    List<Classes> searchClasses(Integer courseId, String keyword);

    /**
     * 获取课程下的班级数量
     * @param courseId 课程ID
     * @return 班级数量
     */
    int countClassesByCourse(Integer courseId);

    /**
     * 检查班级是否存在
     * @param classId 班级ID
     * @return 存在返回true，否则false
     */
    boolean existsById(Integer classId);

}


