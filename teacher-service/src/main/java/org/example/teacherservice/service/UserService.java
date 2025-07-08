package org.example.teacherservice.service;

import org.example.teacherservice.dto.UserDTO;

import java.util.List;
 
public interface UserService {
    List<UserDTO> getAssistants();
} 