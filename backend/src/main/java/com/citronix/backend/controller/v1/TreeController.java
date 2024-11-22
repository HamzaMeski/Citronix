package com.citronix.backend.controller.v1;

import com.citronix.backend.dto.tree.request.CreateTreeRequest;
import com.citronix.backend.dto.tree.response.TreeDetailResponse;
import com.citronix.backend.dto.tree.response.TreeResponse;
import com.citronix.backend.service.TreeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trees")
@RequiredArgsConstructor
public class TreeController {
    private final TreeService treeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TreeResponse create(@Valid @RequestBody CreateTreeRequest request) {
        return treeService.create(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        treeService.delete(id);
    }

    @GetMapping("/{id}")
    public TreeDetailResponse findById(@PathVariable Long id) {
        return treeService.findById(id);
    }

    @GetMapping("/field/{fieldId}")
    public List<TreeResponse> findAllByFieldId(@PathVariable Long fieldId) {
        return treeService.findAllByFieldId(fieldId);
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@PathVariable Long id) {
        treeService.updateTreeStatus(id);
    }
} 