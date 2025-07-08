package org.example.teacherservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.teacherservice.dto.UserDTO;
import org.example.teacherservice.response.CommonResponse;
import org.example.teacherservice.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/assistants")
    public CommonResponse<List<UserDTO>> getAssistants() {
        List<UserDTO> assistants = userService.getAssistants();
        return CommonResponse.success(assistants);
    }
} 