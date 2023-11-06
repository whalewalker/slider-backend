package com.sliderbackend.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sliderbackend.data.model.Media;
import com.sliderbackend.data.model.Presentation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.DeflaterOutputStream;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final Cloudinary cloudinary;

    private byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) > 0) {
            baos.write(buffer, 0, len);
        }
        is.close();
        return baos.toByteArray();
    }

    protected byte[] compressFile(byte[] media) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DeflaterOutputStream deflaterOut = new DeflaterOutputStream(baos);
        deflaterOut.write(media);
        deflaterOut.close();
        return baos.toByteArray();
    }

    public MediaType getMediaTypeForFileType(String fileType) {
        return switch (fileType) {
            case "pdf" -> MediaType.APPLICATION_PDF;
            case "png" -> MediaType.IMAGE_PNG;
            case "jpeg" -> MediaType.IMAGE_JPEG;
            case "gif" -> MediaType.IMAGE_GIF;
            case "csv", "txt" -> MediaType.TEXT_PLAIN;
            case "xml" -> MediaType.APPLICATION_XML;
            case "json" -> MediaType.APPLICATION_JSON;
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };
    }

    protected Presentation createPresentation(String title) {
        return Presentation
                .builder()
                .title(title)
                .uuid(UUID.randomUUID().toString())
                .privacySettings("")
                .build();
    }

    protected Media uploadToCloudinary(MultipartFile file, String fileName, String presentationId) {
        try {
            byte[] compressedFile = compressFile(file.getBytes());
            Map<?, ?> uploadResult = cloudinary.uploader().upload(compressedFile, ObjectUtils.asMap("resource_type", "auto", "public_id", presentationId + "/" + fileName));
            String url = uploadResult.get("secure_url").toString();
//            String format = uploadResult.get("format").toString();
            String resourceType = uploadResult.get("resource_type").toString();
            String publicId = uploadResult.get("public_id").toString();
            AtomicInteger atomicCounter = new AtomicInteger(1);
            int position = atomicCounter.getAndIncrement();
            return Media.builder()
                    .resourceType(resourceType)
                    .publicId(publicId)
                    .format(getMediaTypeForFileType(file.getContentType()).getType())
                    .url(url)
                    .name(format("%s_%s", file.getOriginalFilename(), position))
                    .uuid(UUID.randomUUID().toString())
                    .position(position)
                    .build();
        } catch (IOException e) {
            // Handle the exception appropriately (e.g., logging or rethrowing)
            throw new RuntimeException("Error uploading to Cloudinary", e);
        }
    }


}
