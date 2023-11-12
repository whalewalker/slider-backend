package com.sliderbackend.config;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.sliderbackend.data.model.dto.GoogleDriveResponse;
import com.sliderbackend.exception.NotFoundException;
import com.sliderbackend.exception.SystemException;
import com.sliderbackend.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@Slf4j
public class AppConfig {
    @Value("${google.service.account.key}")
    private Resource serviceAccountKey;

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public ModelMapper mapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setSkipNullEnabled(true);
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper;
    }

    @Bean
    public Drive drive() {
        try {
            final GoogleCredentials googleCredentials = ServiceAccountCredentials
                    .fromStream(serviceAccountKey.getInputStream())
                    .createScoped(DriveScopes.DRIVE, "https://www.googleapis.com/auth/drive.install");
            HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(googleCredentials);

            return new Drive.Builder(new NetHttpTransport(),
                    GsonFactory.getDefaultInstance(), requestInitializer)
                    .setApplicationName(applicationName)
                    .build();
        } catch (Exception e) {
            log.error(e.toString());
            if (e.getClass().equals(GoogleJsonResponseException.class))
                handleGoogleJsonResponseException((GoogleJsonResponseException) e);
            throw new SystemException("Error occurred while authenticating to google drive");
        }
    }

    private void handleGoogleJsonResponseException(GoogleJsonResponseException e) {
        GoogleDriveResponse googleDriveResponse = new Utils().jsonUnMarshall(e.getContent(), GoogleDriveResponse.class);
        log.error(e.toString());
        if (googleDriveResponse.getCode() == 404)
            throw new NotFoundException(googleDriveResponse.getMessage());
        throw new SystemException(googleDriveResponse.getMessage());
    }
}
