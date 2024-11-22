package com.citronix.backend.service;

import com.citronix.backend.dto.tree.request.CreateTreeRequest;
import com.citronix.backend.dto.tree.response.TreeDetailResponse;
import com.citronix.backend.dto.tree.response.TreeResponse;
import java.util.List;

public interface TreeService {
    TreeResponse create(CreateTreeRequest request);
    void delete(Long id);
    TreeDetailResponse findById(Long id);
    List<TreeResponse> findAllByFieldId(Long fieldId);
    void updateTreeStatus(Long id);
} 