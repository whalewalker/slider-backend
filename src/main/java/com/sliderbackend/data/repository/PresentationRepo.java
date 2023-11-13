package com.sliderbackend.data.repository;

import com.sliderbackend.data.model.Presentation;
import com.sliderbackend.data.repository.contract.IPresentationRepo;
import com.sliderbackend.data.repository.contract.RelationalBaseRepo;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service

public class PresentationRepo extends RelationalBaseRepo<Presentation, IPresentationRepo> {
    private final IPresentationRepo iPresentationRepo;

    public PresentationRepo(@Lazy IPresentationRepo iPresentationRepo) {
        super(iPresentationRepo, "Presentation");
        this.iPresentationRepo = iPresentationRepo;
    }


    public Presentation getByPath(String path) {
        return iPresentationRepo.findByPath(path).orElse(null);
    }

}
