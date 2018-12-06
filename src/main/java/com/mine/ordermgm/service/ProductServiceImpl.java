package com.mine.ordermgm.service;

import com.mine.ordermgm.api.UpdateProductRequest;
import com.mine.ordermgm.model.Product;
import com.mine.ordermgm.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @stefanl
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductRepository productRepository;

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public long createProduct(Product product) {
        Optional<Product> existingProduct = productRepository.findByName(product.getName());
        if (!existingProduct.isPresent()) {
            return productRepository.save(product).getId();
        }
        return -1;
    }

    @Override
    public Optional<Product> updateProduct(long id,
                                           UpdateProductRequest request) {
        Optional<Product> existingProductOpt = productRepository.findById(id);
        if (existingProductOpt.isPresent()) {
            Product existingProduct = existingProductOpt.get();
            applyUpdates(request, existingProduct);
            productRepository.save(existingProduct);
        }
        return existingProductOpt;
    }

    private void applyUpdates(UpdateProductRequest request, Product existingProduct) {
        if (Objects.nonNull(request.getName())) {
            existingProduct.setName(request.getName());
        }
        if (Objects.nonNull(request.getPrice())) {
            existingProduct.setPrice(request.getPrice());
        }
    }
}
