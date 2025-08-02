package com.spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.model.Product;
import com.spring.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {



    // ✅ Save uploaded files to this directory (absolute path)
    private final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    private final ProductService productService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Product> create(
            @RequestPart("product") String productJson,
            @RequestPart("image") MultipartFile file
    ) throws IOException {

        // ✅ Convert JSON string to Product object
        ObjectMapper objectMapper = new ObjectMapper();
        Product product = objectMapper.readValue(productJson, Product.class);

        // ✅ Ensure upload directory exists
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        // ✅ Generate unique file name
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName = UUID.randomUUID() + "_" + originalFilename;
        File destination = new File(UPLOAD_DIR + fileName);

        // ✅ Save file to destination
        file.transferTo(destination);

        // ✅ Set image path in product (relative or absolute)
        product.setImage_url("/uploads/" + fileName); // You may adjust this based on frontend usage

        // ✅ Save product to DB
        return ResponseEntity.ok(productService.create(product));
    }

//    @PostMapping(consumes = {"multipart/form-data"})
//    public ResponseEntity<Product> create(
//            @RequestPart("product") Product product,
//            @RequestPart("image") MultipartFile file) throws IOException {
//
//        // Check and create upload folder if not exists
//        File uploadDir = new File(UPLOAD_DIR);
//        if (!uploadDir.exists()) uploadDir.mkdirs();
//
//        // Unique filename
//        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
//        String fileName = UUID.randomUUID() + "_" + originalFilename;
//
//        // Save file to folder
//        File destination = new File(UPLOAD_DIR + fileName);
//        file.transferTo(destination);
//
//        // Set image path in Product
//        product.setImage_url(UPLOAD_DIR + fileName);
//
//        return ResponseEntity.ok(productService.create(product));
//    }

    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(productService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return productService.getById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product product) {
        return ResponseEntity.ok(productService.update(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
