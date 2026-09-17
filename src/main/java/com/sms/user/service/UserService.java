package com.sms.user.service;


import com.sms.user.domain.UserInfo;
import com.sms.user.dto.UserRequestDTO;
import com.sms.user.dto.UserResponseDTO;
import com.sms.user.exception.DuplicateResourceException;
import com.sms.user.exception.ResourceNotFoundException;
import com.sms.user.repository.UserRepository;
import com.sms.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    //private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO createUser(UserRequestDTO requestDTO) {
        if (userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateResourceException("Email already in use: " + requestDTO.getEmail());
        }
        if (userRepository.existsByUsername(requestDTO.getUsername())) {
            throw new DuplicateResourceException("Username already taken: " + requestDTO.getUsername());
        }

        /*Set<String> strRoles = requestDTO.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found."));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found."));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found."));
                        roles.add(userRole);
                }
            });
        }*/
        UserInfo user = UserInfo.builder()
                .username(requestDTO.getUsername())
                .email(requestDTO.getEmail())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .firstName(requestDTO.getFirstName())
                .lastName(requestDTO.getLastName())
                .role(requestDTO.getRole())
                .orgId(requestDTO.getOraganizationId())
                .customerId(requestDTO.getCustomerId())
                //.roles(roles)
                .build();

        UserInfo saved = userRepository.save(user);
        return toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(String id) {
        UserInfo user = findUserOrThrow(id);
        return toResponseDTO(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserByUsername(String username) {
        Optional<UserInfo> userInfo= userRepository.findByUsername(username);
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(userInfo.get().getId());
        userResponseDTO.setUsername(userInfo.get().getUsername());
        userResponseDTO.setPassword(userInfo.get().getPassword());
        userResponseDTO.setEmail(userInfo.get().getEmail());
        userResponseDTO.setRole(userInfo.get().getRole().toString());
        userResponseDTO.setOrganizationId(userInfo.get().getOrgId());
        userResponseDTO.setCustomerId(userInfo.get().getCustomerId());
        return userResponseDTO;
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDTO> searchUsers(String keyword, Pageable pageable) {
        return userRepository
                .findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword, pageable)
                .map(this::toResponseDTO);
    }

    public UserResponseDTO updateUser(String id, UserRequestDTO requestDTO) {
        UserInfo user = findUserOrThrow(id);

        if (!user.getEmail().equalsIgnoreCase(requestDTO.getEmail())
                && userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateResourceException("Email already in use: " + requestDTO.getEmail());
        }
        if (!user.getUsername().equalsIgnoreCase(requestDTO.getUsername())
                && userRepository.existsByUsername(requestDTO.getUsername())) {
            throw new DuplicateResourceException("Username already taken: " + requestDTO.getUsername());
        }

        user.setUsername(requestDTO.getUsername());
        user.setEmail(requestDTO.getEmail());
        user.setFirstName(requestDTO.getFirstName());
        user.setLastName(requestDTO.getLastName());
        if (StringUtils.hasText(requestDTO.getPassword())) {
            user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        }

        UserInfo updated = userRepository.save(user);
        return toResponseDTO(updated);
    }

    public UserResponseDTO partialUpdateUser(String id, UserRequestDTO requestDTO) {
        UserInfo user = findUserOrThrow(id);

        if (StringUtils.hasText(requestDTO.getUsername())) {
            user.setUsername(requestDTO.getUsername());
        }
        if (StringUtils.hasText(requestDTO.getEmail())) {
            user.setEmail(requestDTO.getEmail());
        }
        if (StringUtils.hasText(requestDTO.getFirstName())) {
            user.setFirstName(requestDTO.getFirstName());
        }
        if (StringUtils.hasText(requestDTO.getLastName())) {
            user.setLastName(requestDTO.getLastName());
        }
        if (StringUtils.hasText(requestDTO.getPassword())) {
            user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        }

        UserInfo updated = userRepository.save(user);
        return toResponseDTO(updated);
    }

    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    private UserInfo findUserOrThrow(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    private UserResponseDTO toResponseDTO(UserInfo user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().toString())
                .organizationId(user.getOrgId())
                .customerId(user.getCustomerId())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
