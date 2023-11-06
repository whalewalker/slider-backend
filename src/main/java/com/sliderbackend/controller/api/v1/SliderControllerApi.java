package com.sliderbackend.controller.api.v1;


import com.sliderbackend.data.model.Presentation;
import com.sliderbackend.data.model.general.Response;
import com.sliderbackend.data.model.general.request.PresentationRequest;
import com.sliderbackend.services.DocumentEditor;
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
    private final DocumentEditor documentEditor;

    @PostMapping("/upload")
    public ResponseEntity<Response<Presentation>> create(PresentationRequest request) {
            Response<Presentation> viewResponse = documentEditor.createPresentation(request);
            return new ResponseEntity<>(viewResponse, HttpStatus.OK);
    }
}
