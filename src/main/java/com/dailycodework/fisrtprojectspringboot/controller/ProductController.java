package com.dailycodework.fisrtprojectspringboot.controller;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.fisrtprojectspringboot.dto.ProductDto;
import com.dailycodework.fisrtprojectspringboot.exceptions.ResourceNotFoundException;
import com.dailycodework.fisrtprojectspringboot.model.Product;
import com.dailycodework.fisrtprojectspringboot.request.AddProductRequest;
import com.dailycodework.fisrtprojectspringboot.request.ProductUpdateRequest;
import com.dailycodework.fisrtprojectspringboot.response.ApiResponse;
import com.dailycodework.fisrtprojectspringboot.service.product.IProductService;

import lombok.RequiredArgsConstructor;

    @RequiredArgsConstructor
    @RestController
    @RequestMapping("${api.prefix}/products")
    public class ProductController {
        private final IProductService productService;

        @GetMapping("/all")
        public ResponseEntity<ApiResponse> getAllProducts() {
                List<Product> products = productService.getAllProducts();
                // Appelle le service pour convertir une liste d'entités Product en liste de ProductDto
                // using ModelMapper pour transformer les données 
                List<ProductDto> convertProduct = productService.getConvertedProducts(products);
                return ResponseEntity.ok(new ApiResponse("Found", convertProduct));  
        }

        @GetMapping("/product/{productId}/product")
        public ResponseEntity<ApiResponse> getProductById(@PathVariable("productId") Long id) {
            try {
                Product product = productService.getProductById(id);
                // Convertit une entité Product en ProductDto using ModelMapper pour séparer les données de la base de données de l'API REST
                ProductDto productDto = productService.convertToDto(product);
                return ResponseEntity.ok(new ApiResponse("Found", productDto));
            } catch (ResourceNotFoundException e) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
            }
        }

        @PostMapping("/add")
        public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product) {
            try {
                Product theproduct = productService.addProduct(product);
                return ResponseEntity.ok(new ApiResponse("Add product success", theproduct));
            } catch (Exception e) {
                return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
            }
        }

        @PutMapping("/product/{productId}/update")
        public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest product,
                @PathVariable("productId") Long id) {
            try {
                Product theproduct = productService.updateProduct(product, id);
                return ResponseEntity.ok(new ApiResponse("Update product success", theproduct));
            } catch (ResourceNotFoundException e) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
            }
        }

        @DeleteMapping("/product/{productId}/delete")
        public ResponseEntity<ApiResponse> deleteProductById(@PathVariable("productId") Long id) {
            try {
                productService.deleteProductById(id);
                return ResponseEntity.ok(new ApiResponse("Deleted", null));
            } catch (ResourceNotFoundException e) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
            }
        }

        @GetMapping("/by/brand-and-name")
        public ResponseEntity<ApiResponse> getProductsByBrandAndName(@RequestParam String brandName,
                @RequestParam String productName) {
            try {
                List<Product> products = productService.getProductsByBrandAndName(brandName, productName);
                List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
                if (products.isEmpty()) {
                    return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Not products found", null));
                }
                return ResponseEntity.ok(new ApiResponse("success", convertedProducts));
            } catch (Exception e) {
                return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
            }
        }

        @GetMapping("/by/category-and-brand")
        public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(@RequestParam String category,
                @RequestParam String brand) {
            try {
                List<Product> products = productService.getProductsByCategoryAndBrand(category, brand);
                List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
                if (products.isEmpty()) {
                    return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Not products found", null));
                }
                return ResponseEntity.ok(new ApiResponse("success", convertedProducts));
            } catch (Exception e) {
                return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("error", e.getMessage()));

            }
        }

        @GetMapping("/{name}/products")
        public ResponseEntity<ApiResponse> getProductsByName(@PathVariable String name) {
            try {
                List<Product> products = productService.getProductsByName(name);
                List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
                if (products.isEmpty()) {
                    return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Not products found", null));
                }
                return ResponseEntity.ok(new ApiResponse("success", convertedProducts));
            } catch (Exception e) {
                return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
            }
        }

        @GetMapping("/product/by-brand")
        public ResponseEntity<ApiResponse> getProductsByBrand(@RequestParam String brand) {
            try {
                List<Product> products = productService.getProductsByBrand(brand);
                List<ProductDto> convertedProducts = productService.getConvertedProducts(products);

                if (products.isEmpty()) {
                    return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Not products found", null));
                }
                return ResponseEntity.ok(new ApiResponse("success", convertedProducts));
            } catch (Exception e) {
                return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
            }
        }

        @GetMapping("/product/{category}/all/products")
        public ResponseEntity<ApiResponse> getProductsByCategory(@PathVariable String category){
            try {
                List<Product> products = productService.getProductsByCategory(category);
                List<ProductDto> convertedProducts = productService.getConvertedProducts(products);

                if (products.isEmpty()) {
                    return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Not products found", null));
                }
                return ResponseEntity.ok(new ApiResponse("success", convertedProducts));
            } catch (Exception e) {
                return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
            }
        }
        @GetMapping("/product/count/by-brand/and-name")
        public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brand,@RequestParam String name) {
            try {
                var productCount = productService.countProductsByBrandAndName(brand, name);
                // It's often better to return 200 OK with a count of 0
                // than a 404 Not Found, as the request was processed successfully.
                return ResponseEntity.ok(new ApiResponse("Product count!" ,productCount));
            } catch (Exception e) {
                // It's good practice to log the exception here for debugging purposes
                // log.error("Error counting products by brand and name", e);
                return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
            }
        }
    }
