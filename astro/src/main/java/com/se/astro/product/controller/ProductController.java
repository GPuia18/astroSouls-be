package com.se.astro.product.controller;

import com.se.astro.product.dto.AddProductRequest;
import com.se.astro.product.dto.SearchProductRequest;
import com.se.astro.product.model.Product;
import com.se.astro.product.service.ProductService;
import com.se.astro.user.dto.SuccessResponse;
import com.se.astro.user.model.AstroUser;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/product")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<Product> fetchAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping("/search")
    public ResponseEntity<Product> searchProduct(@RequestBody SearchProductRequest searchProductRequest) {
        Optional<Product> product = productService.getProduct(searchProductRequest);
        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> addProduct(@RequestBody AddProductRequest addProductRequest) {

        if (addProductRequest.getName().equals("Admin")) {
            return ResponseEntity.badRequest().build();
        }

        productService.addProduct(addProductRequest);
        return ResponseEntity.ok(SuccessResponse.builder().message("Success").build());
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteProduct(@RequestBody SearchProductRequest searchProductRequest) {
        productService.deleteProduct(searchProductRequest);
        return ResponseEntity.ok(SuccessResponse.builder().message("Success").build());
    }
}
