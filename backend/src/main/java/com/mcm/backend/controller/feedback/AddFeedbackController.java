package com.mcm.backend.controller.feedback;

import com.mcm.backend.service.feedback.AddFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AddFeedbackController {
    @Autowired
    AddFeedbackService addFeedbackService;
    @PostMapping("/feedback/add/")
    String addFeedback(@RequestParam Map<String, String> data){
        return addFeedbackService.addFeedback(data);
    }
}
