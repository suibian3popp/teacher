package org.example.teacherservice.vo.assignment;

import lombok.Data;

@Data
public class GradingProgressVO {
    private long graded;
    private long ungraded;

    public GradingProgressVO(long graded, long ungraded) {
        this.graded = graded;
        this.ungraded = ungraded;
    }
}