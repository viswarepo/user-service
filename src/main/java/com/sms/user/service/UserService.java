package com.sms.user.service;


import com.sms.user.dto.UserRequestDTO;
import com.sms.user.dto.UserResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO requestDTO);

    UserResponseDTO getUserById(Long id);

    UserResponseDTO getUserByUsername(String username);

    List<UserResponseDTO> getAllUsers();

    Page<UserResponseDTO> getAllUsers(Pageable pageable);

    Page<UserResponseDTO> searchUsers(String keyword, Pageable pageable);

    UserResponseDTO updateUser(Long id, UserRequestDTO requestDTO);

    UserResponseDTO partialUpdateUser(Long id, UserRequestDTO requestDTO);

    void deleteUser(Long id);
}
