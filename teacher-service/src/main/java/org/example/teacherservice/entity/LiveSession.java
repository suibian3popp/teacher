package org.example.teacherservice.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 直播课程表（由教师创建）
 * @TableName live_session
 */
@TableName(value ="live_session")
@Data
public class LiveSession {
    /**
     * 直播会话ID
     */
    @TableId(value = "session_id", type = IdType.AUTO)
    private Integer sessionId;

    /**
     * 直播课标题
     */
    @TableField(value = "session_title")
    private String sessionTitle;

    /**
     * 创建直播的教师ID
     */
    @TableField(value = "teacher_id")
    private Integer teacherId;

    /**
     * TRTC房间ID
     */
    @TableField(value = "trtc_room_id")
    private String trtcRoomId;

    /**
     * 最大参与人数
     */
    @TableField(value = "max_users")
    private Integer maxUsers;

    /**
     * 关联课程ID
     */
    @TableField(value = "course_id")
    private Integer courseId;

    /**
     * 计划开始时间
     */
    @TableField(value = "start_time")
    private Date startTime ;

    /**
     * 实际结束时间
     */
    @TableField(value = "end_time")
    private Date endTime;

    /**
     * 直播状态
     */
    @TableField(value = "status")
    private Object status;

    /**
     * 录播文件存储资源ID
     */
    @TableField(value = "recording_path")
    private Integer recordingPath;

    /**
     * 创建时间
     */
    @TableField(value = "created_at")
    private Date createdAt;
}