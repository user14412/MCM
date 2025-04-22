package com.mcm.backend.controller.admin;

import com.mcm.backend.service.admin.ExhibitManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/exhibits/")
public class ExhibitManagementController {
    @Autowired
    private ExhibitManagementService exhibitManagementService;

    @GetMapping("")
    public Map<String, Object> getExhibitList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String category) {
        return exhibitManagementService.getExhibitList(page, size, query, category);
    }

    @PostMapping("")
    public Map<String, String> addExhibit(
            @RequestParam String name,
            @RequestParam String comment,
            @RequestParam String category,
            @RequestParam String primaryPhoto) {
        return exhibitManagementService.addExhibit(name, comment, category, primaryPhoto);
    }

    @PutMapping("{id}/")
    public Map<String, String> updateExhibit(
            @PathVariable Integer id,
            @RequestParam String name,
            @RequestParam String comment,
            @RequestParam String category,
            @RequestParam(required = false) String primaryPhoto) {
        return exhibitManagementService.updateExhibit(id, name, comment, category, primaryPhoto);
    }

    @DeleteMapping("{id}/")
    public Map<String, String> deleteExhibit(@PathVariable Integer id) {
        return exhibitManagementService.deleteExhibit(id);
    }

    @GetMapping("{id}/images/")
    public Map<String, Object> getExhibitImages(@PathVariable Integer id) {
        return exhibitManagementService.getExhibitImages(id);
    }

    @PostMapping("{id}/images/")
    public Map<String, String> addExhibitImage(
            @PathVariable Integer id,
            @RequestParam String imageUrl) {
        return exhibitManagementService.addExhibitImage(id, imageUrl);
    }

    @DeleteMapping("images/{imageId}/")
    public Map<String, String> deleteExhibitImage(@PathVariable Integer imageId) {
        return exhibitManagementService.deleteExhibitImage(imageId);
    }
} 