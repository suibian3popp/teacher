package org.example.teacherservice.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 学生参与记录表（仅记录学生）
 * @TableName session_attendance
 */
@TableName(value ="session_attendance")
@Data
public class SessionAttendance {
    /**
     * 参与记录ID
     */
    @TableId(value = "attendance_id", type = IdType.AUTO)
    private Integer attendanceId;

    /**
     * 关联的直播会话ID
     */
    @TableField(value = "session_id")
    private Integer sessionId;

    /**
     * 参与学生ID
     */
    @TableField(value = "student_id")
    private Integer studentId;

    /**
     * 加入时间
     */
    @TableField(value = "join_time")
    private Date joinTime;

    /**
     * 离开时间
     */
    @TableField(value = "leave_time")
    private Date leaveTime;

    /**
     * 实际参与时长(秒)
     */
    @TableField(value = "duration")
    private Integer duration;

    /**
     * 互动次数（提问/答题等）
     */
    @TableField(value = "interaction_count")
    private Integer interactionCount;
}