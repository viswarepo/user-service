package com.sms.user.service;


import com.sms.user.dto.OrgRequestDTO;
import com.sms.user.dto.OrgResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrgService {

    OrgResponseDTO createOrg(OrgRequestDTO requestDTO);

    OrgResponseDTO getOrgById(String id);

    OrgResponseDTO getOrgByOrgname(String username);

    List<OrgResponseDTO> getAllOrg();

    Page<OrgResponseDTO> getAllOrg(Pageable pageable);

    Page<OrgResponseDTO> searchOrg(String keyword, Pageable pageable);

    OrgResponseDTO updateOrg(String id, OrgRequestDTO requestDTO);

    OrgResponseDTO partialUpdateOrg(String id, OrgRequestDTO requestDTO);

    void deleteOrg(String id);
}
