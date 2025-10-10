package com.dailycodework.fisrtprojectspringboot.service.product;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.dailycodework.fisrtprojectspringboot.dto.ImageDto;
import com.dailycodework.fisrtprojectspringboot.dto.ProductDto;
import com.dailycodework.fisrtprojectspringboot.exceptions.ResourceNotFoundException;
import com.dailycodework.fisrtprojectspringboot.model.Category;
import com.dailycodework.fisrtprojectspringboot.model.Image;
import com.dailycodework.fisrtprojectspringboot.model.Product;
import com.dailycodework.fisrtprojectspringboot.repository.CategoryRepository;
import com.dailycodework.fisrtprojectspringboot.repository.ImageRepository;
import com.dailycodework.fisrtprojectspringboot.repository.ProductRepository;
import com.dailycodework.fisrtprojectspringboot.request.AddProductRequest;
import com.dailycodework.fisrtprojectspringboot.request.ProductUpdateRequest;

import lombok.RequiredArgsConstructor;

@Service //  Déclare cette classe comme un service Spring (géré par le framework)
@RequiredArgsConstructor //  Génère un constructeur avec les champs 'final'
public class ProductService implements IProductService {//  Implémente le contrat
    //  Dépendances injectées automatiquement par Spring via le constructeur généré
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    @Override
    public Product addProduct(AddProductRequest request) {
        // Check if the category is found in the DB
        // if Yes,set it as the new product category
        // if No,the save it as a new category
        // The set as the new product category
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
    }

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category);
    }

    @Override
    public Product getProductById(long id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product Not found"));
    }

    @Override
    public void deleteProductById(long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete, () -> {
            throw new ResourceNotFoundException("Product not found");
        });
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, long productId) {
        return productRepository.findById(productId)
        .map(existingProduct->updateExistingProduct(existingProduct, request))
        .map(productRepository :: save)
        .orElseThrow(()-> new ResourceNotFoundException("Product not found"));
    }

    private Product updateExistingProduct(Product existingProduct,ProductUpdateRequest request){
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    // Implémentation de l'interface qui convertit une liste d'entités Product en ProductDto
    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products){
        return products.stream()       // Transforme la liste en stream pour le traitement
        .map(this::convertToDto)       // Convertit chaque Product en ProductDto
        .toList();                     // Retourne la liste convertie
    }

    // Convertit une entité Product en ProductDto en incluant les images associées
    @Override
    public ProductDto convertToDto(Product product) {
        // Convertit le Product de base en ProductDto using ModelMapper
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        // Récupère toutes les images associées à ce produit depuis la base de données
        List<Image> images = imageRepository.findByProductId(product.getId()); 
        // Convertit la liste d'entités Image en liste de ImageDto
        List<ImageDto> imageDtos = images.stream()
        .map(image -> modelMapper.map(image, ImageDto.class))
        .toList();
        // Assigne les images converties au DTO du produit
        productDto.setImages(imageDtos);
        // Retourne le ProductDto complet avec ses images
        return  productDto;
    }

}
