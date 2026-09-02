package com.sms.user.service.impl;

import com.sms.user.dto.OrgRequestDTO;
import com.sms.user.dto.OrgResponseDTO;
import com.sms.user.domain.OrgInfo;
import com.sms.user.exception.DuplicateResourceException;
import com.sms.user.exception.ResourceNotFoundException;
import com.sms.user.repository.OrgRepository;
import com.sms.user.service.OrgService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrgServiceImpl implements OrgService {

    private final OrgRepository orgRepository;

    @Override
    public OrgResponseDTO createOrg(OrgRequestDTO requestDTO) {

        if (orgRepository.existsByOrgname(requestDTO.getOrgname())) {
            throw new DuplicateResourceException("Organization already registered: " + requestDTO.getEmail());
        }
        if (orgRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateResourceException("Email already in use: " + requestDTO.getEmail());
        }
        OrgInfo org = OrgInfo.builder()
                .orgname(requestDTO.getOrgname())
                .email(requestDTO.getEmail())
                .legalName(requestDTO.getLegalName())
                .phone(requestDTO.getPhone())
                .website(requestDTO.getWebsite())
                .country(requestDTO.getCountry())
                .state(requestDTO.getState())
                .city(requestDTO.getCity())
                .address1(requestDTO.getAddress1())
                .address2(requestDTO.getAddress2())
                .description(requestDTO.getDescription())
                .build();

        OrgInfo saved = orgRepository.save(org);
        return toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrgResponseDTO getOrgById(String id) {
        OrgInfo org = findOrgOrThrow(id);
        return toResponseDTO(org);
    }

    @Override
    public OrgResponseDTO getOrgByOrgname(String orgname) {
        Optional<OrgInfo> orgInfo = orgRepository.findByOrgname(orgname);
        OrgResponseDTO orgResponseDTO = new OrgResponseDTO();

        return toResponseDTO(orgInfo.get());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrgResponseDTO> getAllOrg() {
        return orgRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrgResponseDTO> getAllOrg(Pageable pageable) {
        return orgRepository.findAll(pageable).map(this::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrgResponseDTO> searchOrg(String keyword, Pageable pageable) {
        return orgRepository
                .findByOrgnameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword, pageable)
                .map(this::toResponseDTO);
    }

    @Override
    public OrgResponseDTO updateOrg(String id, OrgRequestDTO requestDTO) {
        OrgInfo org = findOrgOrThrow(id);

        if (!org.getEmail().equalsIgnoreCase(requestDTO.getEmail())
                && orgRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateResourceException("Email already in use: " + requestDTO.getEmail());
        }
        if (!org.getOrgname().equalsIgnoreCase(requestDTO.getOrgname())
                && orgRepository.existsByOrgname(requestDTO.getOrgname())) {
            throw new DuplicateResourceException("Organization name already taken: " + requestDTO.getOrgname());
        }
        org.setOrgname(requestDTO.getOrgname());
        org.setEmail(requestDTO.getEmail());
        org.setLegalName(requestDTO.getLegalName());
        org.setPhone(requestDTO.getPhone());
        org.setWebsite(requestDTO.getWebsite());
        org.setCountry(requestDTO.getCountry());
        org.setState(requestDTO.getState());
        org.setCity(requestDTO.getCity());
        org.setAddress1(requestDTO.getAddress1());
        org.setAddress2(requestDTO.getAddress2());
        org.setDescription(requestDTO.getDescription());
        OrgInfo updated = orgRepository.save(org);
        return toResponseDTO(updated);
    }

    @Override
    public OrgResponseDTO partialUpdateOrg(String id, OrgRequestDTO requestDTO) {
        OrgInfo org = findOrgOrThrow(id);
        if (StringUtils.hasText(requestDTO.getOrgname())) {
            org.setOrgname(requestDTO.getOrgname());
        }
        if (StringUtils.hasText(requestDTO.getEmail())) {
            org.setEmail(requestDTO.getEmail());
         }
        if (StringUtils.hasText(requestDTO.getLegalName())) {
            org.setLegalName(requestDTO.getLegalName());
        }
        if (StringUtils.hasText(requestDTO.getPhone())) {
            org.setPhone(requestDTO.getPhone());
        }
        if (StringUtils.hasText(requestDTO.getWebsite())) {
            org.setWebsite(requestDTO.getWebsite());
        }
        if (StringUtils.hasText(requestDTO.getCountry())) {
            org.setCountry(requestDTO.getCountry());
        }
        if (StringUtils.hasText(requestDTO.getState())) {
            org.setState(requestDTO.getState());
        }
        if (StringUtils.hasText(requestDTO.getCity())) {
            org.setCity(requestDTO.getCity());
        }
        if (StringUtils.hasText(requestDTO.getAddress1())) {
            org.setAddress1(requestDTO.getAddress1());
        }
        if (StringUtils.hasText(requestDTO.getAddress2())) {
            org.setAddress2(requestDTO.getAddress2());
        }
        if (StringUtils.hasText(requestDTO.getDescription())) {
            org.setDescription(requestDTO.getDescription());
        }
        OrgInfo updated = orgRepository.save(org);

        return null;
    }

    @Override
    public void deleteOrg(String id) {
        if (!orgRepository.existsById(id)) {
            throw new ResourceNotFoundException("Organization not found with id: " + id);
        }
        orgRepository.deleteById(id);
    }

    private OrgResponseDTO toResponseDTO(OrgInfo org) {
        return OrgResponseDTO.builder()
                .id(org.getId())
                .orgname(org.getOrgname())
                .legalName(org.getLegalName())
                .description(org.getDescription())
                .orgEmail(org.getEmail())
                .phone(org.getPhone())
                .address1(org.getAddress1())
                .address1(org.getAddress1())
                .country(org.getCountry())
                .state(org.getState())
                .city(org.getCity())
                .createdAt(org.getCreatedAt())
                .updatedAt(org.getUpdatedAt())
                .build();
    }
    private OrgInfo findOrgOrThrow(String id) {
        return orgRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + id));
    }
}
