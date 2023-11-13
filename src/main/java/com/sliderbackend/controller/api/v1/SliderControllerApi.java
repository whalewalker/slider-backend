package com.sliderbackend.controller.api.v1;


import com.sliderbackend.data.model.Media;
import com.sliderbackend.data.model.dto.PresentationUploadDTO;
import com.sliderbackend.data.model.general.Response;
import com.sliderbackend.services.PresentationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class SliderControllerApi {
    private final PresentationService presentationService;
    @PostMapping("/upload")
    public ResponseEntity<Response<Media>> create(@Valid PresentationUploadDTO request) {
            Response<Media> viewResponse = presentationService.createPresentation(request);
            return new ResponseEntity<>(viewResponse, HttpStatus.OK);
    }
}
