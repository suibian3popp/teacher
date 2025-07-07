package org.example.teacherservice.service;

import java.util.List;

public interface ClassStudentService {
    /**
     * 批量添加学生到班级
     * @param classId 班级ID
     * @param studentIds 学生ID列表
     * @param courseId 课程ID(用于验证学生是否已选同课程的其他班级)
     * @return 成功添加的学生数量
     */
    int batchAddStudentsToClass(Integer classId, List<Integer> studentIds, Integer courseId);

    /**
     * 从班级中移除学生
     * @param classId 班级ID
     * @param studentId 学生ID
     */
    void removeStudentFromClass(Integer classId, Integer studentId);

    /**
     * 批量从班级中移除学生
     * @param classId 班级ID
     * @param studentIds 学生ID列表
     * @return 成功移除的学生数量
     */
    int batchRemoveStudentsFromClass(Integer classId, List<Integer> studentIds);

    /**
     * 检查学生是否已选同课程的其他班级
     * @param studentId 学生ID
     * @param courseId 课程ID
     * @return 如果已选返回true，否则false
     */
    boolean hasStudentSelectedSameCourse(Integer studentId, Integer courseId);
}
