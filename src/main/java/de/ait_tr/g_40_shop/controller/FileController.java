package de.ait_tr.g_40_shop.controller;

import de.ait_tr.g_40_shop.exception_handling.Response;
import de.ait_tr.g_40_shop.service.interfaces.FileService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService service;

    public FileController(FileService service) {
        this.service = service;
    }

    @PostMapping
    public Response upload(
            @RequestParam MultipartFile file, // файл отправляется в виде параметра
            @RequestParam String productTitle // чтобы знать, к какому продукту относ. изображение
    ) {
        String url = service.upload(file, productTitle);
        return new Response("Product saved with url - " + url);
    }
}
