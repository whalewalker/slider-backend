package com.sliderbackend.services;

import com.sliderbackend.data.model.Media;
import com.sliderbackend.data.repository.MediaRepo;
import com.sliderbackend.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final MediaRepo mediaRepo;
    private final ModelMapper mapper;


    public String uploadFile(MultipartFile file, String url) throws BadRequestException, IOException {
        if (file.isEmpty()) throw new BadRequestException("File cannot be empty");
        String baseUrl = url.substring(0, url.lastIndexOf("thullo"));
        Media media = uploadFileData(file, baseUrl);
        return media.getPath();
    }


    private Media uploadFileData(MultipartFile file, String url) throws IOException {
        String originalFileName = file.getOriginalFilename();
        assert originalFileName != null;
        String fileType = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        byte[] compressedFile = compressFile(file.getBytes());
        InputStream is = new ByteArrayInputStream(compressedFile);
        Media media = Media.builder().name(originalFileName).mediaType(fileType).path(url).fileByte(is.readAllBytes()).build();
        String path = generateMediaUrl(url, media);
        media.setPath(path);
        return mediaRepo.create(media);
    }


    private byte[] decompressFile(byte[] compressedFile) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(compressedFile);
        GZIPInputStream gzipIn = new GZIPInputStream(bais);
        return getBytes(gzipIn);
    }

    public Media getFile(String fileId) throws IOException {
        Media dbFile = mediaRepo.getByFileId(fileId);
        if (dbFile != null) {
            byte[] compressedFile = dbFile.getFileByte();
            byte[] decompressedFile = decompressFile(compressedFile);
            dbFile.setFileByte(decompressedFile);
        }
        return dbFile;
    }

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

    private byte[] compressFile(byte[] Media) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gzipOut = new GZIPOutputStream(baos);
        gzipOut.write(Media);
        gzipOut.close();
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

    public void deleteFile(String mediaId) {
        Media media = mediaRepo.getByFileId(mediaId);
        mediaRepo.delete(mediaId);
    }

    public String generateMediaUrl(String url, Media media) {
        String baseUrl = url.substring(0, url.lastIndexOf("thullo"));
        return String.format("%s%s%s", baseUrl, "thullo/files/", media.getUuid()) + "." + media.getMediaType();
    }

}
