package com.sliderbackend.data.repository.contract;

import com.sliderbackend.data.model.Media;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IMediaRepo extends MongoRepository<Media, String> {
    Optional<Media> findByMediaId(String mediaId);
}