package com.sliderbackend.services;

import com.sliderbackend.data.model.Presentation;
import com.sliderbackend.data.model.general.Response;
import com.sliderbackend.data.model.general.request.PresentationRequest;
import com.sliderbackend.services.contract.ICRUDService;

public class PresentationService implements ICRUDService<Presentation> {
    public Response<Presentation> createPresentation(PresentationRequest request) {
        return null;
    }
}
