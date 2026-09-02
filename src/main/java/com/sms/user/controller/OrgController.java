package com.sms.user.controller;

import com.sms.user.dto.OrgRequestDTO;
import com.sms.user.dto.OrgResponseDTO;
import com.sms.user.service.OrgService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/org")
@RequiredArgsConstructor
public class OrgController {

    private final OrgService orgService;

    // CREATE
    @PostMapping("/register")
    public ResponseEntity<OrgResponseDTO> register(@Valid @RequestBody OrgRequestDTO requestDTO) {
        OrgResponseDTO created = orgService.createOrg(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // READ - single
    @GetMapping("/{id}")
    public ResponseEntity<OrgResponseDTO> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(orgService.getOrgById(id));
    }

    // READ - all (simple list, no paging)
    @GetMapping("/all")
    public ResponseEntity<List<OrgResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(orgService.getAllOrg());
    }

    // READ - paged/sorted, e.g. GET /api/v1/users?page=0&size=10&sort=username,asc
    @GetMapping
    public ResponseEntity<Page<OrgResponseDTO>> getUsersPaged(Pageable pageable) {
        return ResponseEntity.ok(orgService.getAllOrg(pageable));
    }

    @GetMapping("/info")
    public ResponseEntity<OrgResponseDTO> userByUsername(
            @RequestParam String username) {
        return ResponseEntity.ok(orgService.getOrgByOrgname(username));
    }

    // READ - search by username/email keyword
    @GetMapping("/search")
    public ResponseEntity<Page<OrgResponseDTO>> searchUsers(
            @RequestParam String keyword, Pageable pageable) {
        return ResponseEntity.ok(orgService.searchOrg(keyword, pageable));
    }

    // UPDATE - full replace
    @PutMapping("/{id}")
    public ResponseEntity<OrgResponseDTO> updateUser(
            @PathVariable String id, @Valid @RequestBody OrgRequestDTO requestDTO) {
        return ResponseEntity.ok(orgService.updateOrg(id, requestDTO));
    }

    // UPDATE - partial
    @PatchMapping("/{id}")
    public ResponseEntity<OrgResponseDTO> partialUpdateUser(
            @PathVariable String id, @RequestBody OrgRequestDTO requestDTO) {
        return ResponseEntity.ok(orgService.partialUpdateOrg(id, requestDTO));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        orgService.deleteOrg(id);
        return ResponseEntity.noContent().build();
    }

    //Org Based Apis

}
