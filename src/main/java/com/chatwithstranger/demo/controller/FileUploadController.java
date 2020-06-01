package com.chatwithstranger.demo.controller;

import com.chatwithstranger.demo.message.Message;
import com.chatwithstranger.demo.message.ResourceMessage;
import com.chatwithstranger.demo.websocket.OpenChatWebSocketChatServer;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class FileUploadController {
    private static Logger logger = LoggerFactory.getLogger(FileUploadController.class);
    private final SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm");

    @PostMapping("/upload")
    public void fileUploadHandler(
            @RequestParam("file") MultipartFile file, @RequestParam("sender") String username)
            throws IOException {

        if (file.isEmpty()) throw new IllegalArgumentException("File is empty!");

        logger.info("Original File Name : " + file.getOriginalFilename());
        logger.info("Original File Size : " + file.getSize());
        logger.info("Original File Content Type : " + file.getContentType());
        logger.info("File Temp Path : " + System.getProperty("java.io.tmpdir"));
        byte[] bytes = file.getBytes();

        // Creating the directory to store file
        String rootPath = ".";
        File dir = new File(rootPath + File.separator + "upload");
        if (!dir.exists()) dir.mkdirs();

        // Create the file on server
        File serverFile = new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename());
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
        stream.write(bytes);
        stream.close();

        logger.info("Server File Path : " + serverFile.getAbsolutePath());

        Message message =
                new ResourceMessage(
                        username,
                        sdf.format(new Date()),
                        0,
                        "",
                        "http://localhost:8080/upload/" + file.getOriginalFilename());
        OpenChatWebSocketChatServer.sendMessageToAll(message);
    }

    @GetMapping(
            value = "/upload/{filename}",
            produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public byte[] getUploadedFile(@PathVariable String filename) throws IOException {
        File file = new File("./upload/" + filename);
        InputStream inputStream = Files.newInputStream(file.toPath(), StandardOpenOption.READ);
        return IOUtils.toByteArray(inputStream);
    }
}
