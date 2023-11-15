package com.sliderbackend.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sliderbackend.data.model.general.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import static com.sliderbackend.data.model.general.enums.ResponseCode.Successful;

@Slf4j
@Component
public class Utils {
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private static final int BUFFER_SIZE = 8192; // Use a larger buffer size

    public static <T> Response<T> successfulResponse(List<T> list) {
        Response<T> response = new Response<>();
        response.setResponseCode(Successful.code);
        response.setResponseMessage(Successful.message);
        response.setModelList(list);
        response.setCount(CollectionUtils.isEmpty(list) ? 0 : list.size());
        return response;
    }

    public static <T> Response<T> getSuccessFulResponse(){
        Response<T> response = new Response<>();
        response.setResponseCode(Successful.code);
        response.setResponseMessage(Successful.message);
        response.setStatusCode(String.valueOf(HttpStatus.OK.value()));
        return response;
    }

    public <T> T jsonUnMarshall(String msgBody, Class<T> dataClass){
        try {
            return mapper.readValue(msgBody, dataClass);
        }catch (Exception ex){
            log.error("Error unmarshalling json", ex);
            return null;
        }
    }

    public static File createTempFileFromMultipartFile(MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        assert fileName != null;
        String prefix = fileName.substring(fileName.lastIndexOf("."));

        File tempFile = File.createTempFile(fileName, prefix);

        try (InputStream inputStream = multipartFile.getInputStream();
             FileChannel fileChannel = FileChannel.open(tempFile.toPath(), StandardOpenOption.WRITE)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileChannel.write(ByteBuffer.wrap(buffer, 0, bytesRead));
            }
        }

        tempFile.deleteOnExit();
        return tempFile;
    }

    public static byte[] compress(byte[] fileContent) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             GZIPOutputStream gzos = new GZIPOutputStream(baos)) {

            gzos.write(fileContent, 0, fileContent.length);
            gzos.finish();

            return baos.toByteArray();
        }
    }
}
