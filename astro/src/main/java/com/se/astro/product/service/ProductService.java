package com.se.astro.product.service;

import com.se.astro.product.dto.AddProductRequest;
import com.se.astro.product.dto.SearchProductRequest;
import com.se.astro.product.model.Product;
import com.se.astro.product.repository.ProductRepository;
import com.se.astro.user.model.AstroUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProduct(SearchProductRequest searchProductRequest) {
        return productRepository.findByName(searchProductRequest.getName());
    }

    public void addProduct(AddProductRequest addProductRequest) {
        Product product = Product.builder()
                .name(addProductRequest.getName())
                .description(addProductRequest.getDescription())
                .price(addProductRequest.getPrice())
                .length(addProductRequest.getLength())
                .build();

        productRepository.save(product);
    }

    public void deleteProduct(SearchProductRequest searchProducRequest) {
        Optional<Product> product = productRepository.findByName(searchProducRequest.getName());

        if (product.isPresent()) {
            productRepository.delete(product.get());
        }
    }


}
