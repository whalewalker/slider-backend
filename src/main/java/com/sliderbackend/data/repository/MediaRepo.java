package com.sliderbackend.data.repository;

import com.sliderbackend.data.model.Media;
import com.sliderbackend.data.repository.contract.IMediaRepo;
import com.sliderbackend.data.repository.contract.RelationalBaseRepo;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;


@Service
public class MediaRepo extends RelationalBaseRepo<Media, IMediaRepo> {
    private final IMediaRepo iMediaRepo;

    public MediaRepo(@Lazy IMediaRepo iMediaRepo) {
        super(iMediaRepo, "Media");
        this.iMediaRepo = iMediaRepo;
    }
}
