package com.sliderbackend.services;

import com.sliderbackend.data.model.Media;
import com.sliderbackend.data.model.Presentation;
import com.sliderbackend.data.model.general.Response;
import com.sliderbackend.data.model.general.request.PresentationRequest;
import com.sliderbackend.data.repository.MediaRepo;
import com.sliderbackend.data.repository.PresentationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.sliderbackend.util.Utils.successfulResponse;

@Service
@RequiredArgsConstructor
public class DocumentEditor {

    private final MediaService mediaService;
    private final MediaRepo mediaRepo;
    private final PresentationRepo presentationRepo;

    public Response<Presentation> createPresentation(PresentationRequest request) {
        List<Media> mediaList = new ArrayList<>();
        Presentation presentation = mediaService.createPresentation(request.getTitle());
        for (MultipartFile file: request.getFiles()) {
            Media media = mediaService.uploadToCloudinary(file, file.getOriginalFilename(), presentation.getUuid());
            Media savedMedia = mediaRepo.create(media);
            mediaList.add(savedMedia);
        }
        presentation.setContent(mediaList);
        presentationRepo.create(presentation);
        return successfulResponse(List.of(presentation));
    }


    public Response<Presentation> addMediaToPresentation(String presentationId, String mediaId) {
        Presentation presentation = presentationRepo.getById(presentationId);
        Media media = mediaRepo.getById(mediaId);

        // Implement the logic to add the media to the presentation
        // For example, you can add the media to a list of media within the presentation object

        // Save the updated presentation
        Presentation updatedPresentation = presentationRepo.update(presentation);
        return successfulResponse(List.of(updatedPresentation));
    }
}
