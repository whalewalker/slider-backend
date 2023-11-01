package com.sliderbackend.data.repository.contract;

import com.sliderbackend.data.model.Presentation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IPresentationRepo extends MongoRepository<Presentation, String> {
}
