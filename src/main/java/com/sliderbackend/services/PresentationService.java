package com.sliderbackend.services;

import com.sliderbackend.data.model.Media;
import com.sliderbackend.data.model.dto.PresentationUploadDTO;
import com.sliderbackend.data.model.general.Response;
import com.sliderbackend.services.storage.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PresentationService {
    private final StorageService storageService;

    public Response<Media> createPresentation(PresentationUploadDTO presentationUploadDTO) {
        return storageService.createMediaInPresentation(presentationUploadDTO);
    }
}
