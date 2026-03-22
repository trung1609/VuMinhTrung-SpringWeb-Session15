package com.example.session15.service.impl;

import com.example.session15.dto.request.PageRequestDto;
import com.example.session15.dto.request.ProductRequest;
import com.example.session15.dto.response.ApiResponse;
import com.example.session15.dto.response.PageResponseDto;
import com.example.session15.dto.response.ProductResponse;
import com.example.session15.entity.Product;
import com.example.session15.exception.ResourceNotFoundException;
import com.example.session15.repository.ProductRepository;
import com.example.session15.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public ApiResponse<ProductResponse> createProduct(ProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .size(request.getSize())
                .toppings(request.getToppings())
                .build();
        productRepository.save(product);
        ProductResponse productResponse = new ProductResponse(
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getSize(),
                product.getToppings()
        );
        return new ApiResponse<>(
                "Product created successfully",
                HttpStatus.CREATED.value(),
                productResponse
        );
    }

    @Override
    public ApiResponse<ProductResponse> updateProduct(Long id, ProductRequest request) throws ResourceNotFoundException {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setSize(request.getSize());
        product.setToppings(request.getToppings());
        productRepository.save(product);
        ProductResponse productResponse = new ProductResponse(
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getSize(),
                product.getToppings()
        );
        return new ApiResponse<>(
                "Product updated successfully",
                HttpStatus.OK.value(),
                productResponse
        );
    }

    @Override
    public String deleteProduct(Long id) throws ResourceNotFoundException {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        productRepository.deleteById(id);
        return "Product deleted successfully";
    }

    @Override
    public PageResponseDto<ProductResponse> getAllProducts(PageRequestDto pageRequestDto) {
        Sort sort = null;
        if (pageRequestDto.getPage() == null){
            pageRequestDto.setPage(0);
        }else {
            pageRequestDto.setPage(pageRequestDto.getPage());
        }

        if (pageRequestDto.getSize() == null){
            pageRequestDto.setSize(5);
        }else {
            pageRequestDto.setSize(pageRequestDto.getSize());
        }

        if (pageRequestDto.getSort() == null){
            sort = Sort.by("id");
        }else {
            sort = Sort.by(pageRequestDto.getSort());
        }

        if (pageRequestDto.getDirection() == null){
            sort = sort.ascending();
        }else {
            sort = sort.descending();
        }

        Pageable pageable = PageRequest.of(pageRequestDto.getPage(), pageRequestDto.getSize(), sort);

        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductResponse> productResponses = productPage.getContent().stream()
                .map(product -> new ProductResponse(
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getSize(),
                        product.getToppings()
                ))
                .toList();

        PageResponseDto<ProductResponse> pageResponseDto = new PageResponseDto<>();
        pageResponseDto.setPage(productPage.getNumber());
        pageResponseDto.setSize(productPage.getSize());
        pageResponseDto.setTotalElements(productPage.getTotalElements());
        pageResponseDto.setTotalPages(productPage.getTotalPages());
        pageResponseDto.setData(productResponses);

        return pageResponseDto;

    }
}
