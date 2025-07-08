package org.example.teacherservice.vo;

import lombok.Data;

@Data
public class ClassStatisticsVO {
    private Integer classId;   //班级ID
    private String className;   //班级名字
    private Integer submittedCount;  //学生提交数量
    private Integer gradedCount;  //教师批改数量
}

