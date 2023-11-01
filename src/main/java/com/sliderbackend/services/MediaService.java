package com.sliderbackend.services;

import com.sliderbackend.data.model.Media;
import com.sliderbackend.data.repository.MediaRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final MediaRepo mediaRepo;
    private final ModelMapper mapper;

    public Media editMedia(String mediaId, Media updatedMedia) {
        Media existingMedia = mediaRepo.getById(mediaId);
        mapper.map(existingMedia, updatedMedia);
        return mediaRepo.create(existingMedia);
    }


    public void deleteMedia(String mediaId) {
        Media existingMedia = mediaRepo.getById(mediaId);
        mediaRepo.delete(mediaId);
    }
}
