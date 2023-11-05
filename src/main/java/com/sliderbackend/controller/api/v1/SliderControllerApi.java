package com.sliderbackend.controller.api.v1;


import com.sliderbackend.data.model.Presentation;
import com.sliderbackend.data.model.general.Response;
import com.sliderbackend.data.model.general.request.PresentationRequest;
import com.sliderbackend.services.DocumentEditor;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

@RestController
@RequestMapping("api/v1/file")
@RequiredArgsConstructor
public class SliderControllerApi {
    private final DocumentEditor documentEditor;
    @PostMapping("/upload")
    public Callable<ResponseEntity<Response<Presentation>>> create(@RequestBody @Valid PresentationRequest request) {
        return () -> {
            Response<Presentation> viewResponse = documentEditor.createPresentation(request);
            return new ResponseEntity<>(viewResponse, HttpStatus.OK);
        };
    }
}
