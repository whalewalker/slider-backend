package com.sliderbackend.data.repository;

import com.sliderbackend.data.model.Media;
import com.sliderbackend.data.repository.contract.IMediaRepo;
import com.sliderbackend.data.repository.contract.RelationalBaseRepo;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import static java.lang.String.format;


@Service
public class MediaRepo extends RelationalBaseRepo<Media, IMediaRepo> {
    private final IMediaRepo iMediaRepo;

    public MediaRepo(@Lazy IMediaRepo iMediaRepo) {
        super(iMediaRepo, "Media");
        this.iMediaRepo = iMediaRepo;
    }

    public Media getByFileId(String mediaId) {
        return iMediaRepo.findById(mediaId).orElseThrow(() -> new ResourceNotFoundException(format("Media with %s not found", mediaId)));
    }
}
