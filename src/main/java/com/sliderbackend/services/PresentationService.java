package com.sliderbackend.services;

import com.sliderbackend.data.model.Presentation;
import com.sliderbackend.data.model.general.request.PresentationRequest;
import com.sliderbackend.data.repository.PresentationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PresentationService {

    private final PresentationRepo presentationRepo;
    public Presentation createPresentation(PresentationRequest request) {
        // Implement the logic to create a presentation
        return null;
    }

    public Presentation editContent(String presentationId, String newContent) {
        // Implement the logic to edit the content of a presentation
        // Find the presentation by ID, update the content, and save it
        return null;
    }
    
}