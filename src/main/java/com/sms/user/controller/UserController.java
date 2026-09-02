package com.sms.user.controller;

import com.sms.user.dto.UserRequestDTO;
import com.sms.user.dto.UserResponseDTO;
import com.sms.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // CREATE
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO requestDTO) {
        UserResponseDTO created = userService.createUser(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // READ - single
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // READ - all (simple list, no paging)
    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // READ - paged/sorted, e.g. GET /api/v1/users?page=0&size=10&sort=username,asc
    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> getUsersPaged(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @GetMapping("/info")
    public ResponseEntity<UserResponseDTO> userByUsername(
            @RequestParam String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    // READ - search by username/email keyword
    @GetMapping("/search")
    public ResponseEntity<Page<UserResponseDTO>> searchUsers(
            @RequestParam String keyword, Pageable pageable) {
        return ResponseEntity.ok(userService.searchUsers(keyword, pageable));
    }

    // UPDATE - full replace
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id, @Valid @RequestBody UserRequestDTO requestDTO) {
        return ResponseEntity.ok(userService.updateUser(id, requestDTO));
    }

    // UPDATE - partial
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> partialUpdateUser(
            @PathVariable Long id, @RequestBody UserRequestDTO requestDTO) {
        return ResponseEntity.ok(userService.partialUpdateUser(id, requestDTO));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


    //Org based users

}
