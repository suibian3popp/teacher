package org.example.teacherservice.controller;

import org.example.teacherservice.dto.TrtcParams;
import org.example.teacherservice.entity.LiveSession;
import org.example.teacherservice.entity.SessionAttendance;
import org.example.teacherservice.service.LiveSessionService;
import org.example.teacherservice.service.SessionAttendanceService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/service/live-sessions")
public class LiveController {


    private final LiveSessionService liveSessionService;
    private final SessionAttendanceService attendanceService;

    public LiveController(LiveSessionService liveSessionService,
                          SessionAttendanceService attendanceService) {
        this.liveSessionService = liveSessionService;
        this.attendanceService = attendanceService;
    }


    // 创建直播会话

    @PostMapping("/create")
    @ResponseBody
    public LiveSession createLiveSession(@RequestBody LiveSession liveSession) {
        System.out.println("进入直播创建");
        LiveSession createdLiveSession = liveSessionService.createLiveSession(liveSession);
        System.out.println("createdLiveSession = " + createdLiveSession.getSessionTitle());
        return createdLiveSession;
    }

    // 开始直播 - 修改sessionId类型为int
    @PutMapping("/{sessionId}/start")
    @ResponseBody
    public LiveSession startLiveSession(@PathVariable int sessionId) {
        return liveSessionService.startLiveSession(sessionId);
    }

    // 结束直播 - 修改sessionId类型为int
    @PutMapping("/{sessionId}/end")
    @ResponseBody
    public LiveSession endLiveSession(@PathVariable int sessionId) {
        return liveSessionService.endLiveSession(sessionId);
    }

    // 获取TRTC参数 - 修改参数类型为int, 明确role参数
    @GetMapping("/{sessionId}/trtc-params")
    @ResponseBody
    public TrtcParams getTrtcParams(
            @PathVariable int sessionId,
            @RequestParam int userId,
            @RequestParam String role) {
        return liveSessionService.getTrtcParamsForSession(sessionId, userId, role);
    }


    // 学生加入直播 - 使用@RequestParam获取studentId
    @PostMapping("/{sessionId}/join")
    @ResponseBody
    public SessionAttendance joinSession(
            @PathVariable int sessionId,
            @RequestParam int studentId) {
        return attendanceService.joinSession(sessionId, studentId);
    }

    // 学生离开直播 - 使用@RequestParam获取studentId
    @PostMapping("/{sessionId}/leave")
    @ResponseBody
    public SessionAttendance leaveSession(
            @PathVariable int sessionId,
            @RequestParam int studentId) {
        return attendanceService.leaveSession(sessionId, studentId);
    }

    // 记录互动 - 使用@RequestParam获取studentId
    @PostMapping("/{sessionId}/interaction")
    @ResponseBody
    public void recordInteraction(
            @PathVariable int sessionId,
            @RequestParam int studentId) {
        attendanceService.incrementInteractionCount(sessionId, studentId);
    }
}
