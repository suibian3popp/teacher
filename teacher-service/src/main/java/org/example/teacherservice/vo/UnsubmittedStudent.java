package org.example.teacherservice.vo;

import lombok.Data;

/**
 * 未提交学生信息
 */
@Data
public class UnsubmittedStudent {
    private Integer studentId;
    private String studentName;
    private String studentNo;
}
