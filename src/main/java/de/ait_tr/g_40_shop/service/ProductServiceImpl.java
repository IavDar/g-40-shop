package de.ait_tr.g_40_shop.service;

import de.ait_tr.g_40_shop.domain.dto.ProductDto;
import de.ait_tr.g_40_shop.domain.dto.ProductSupplyDto;
import de.ait_tr.g_40_shop.domain.entity.Product;
import de.ait_tr.g_40_shop.exception_handling.exceptions.*;
import de.ait_tr.g_40_shop.repository.ProductRepository;
import de.ait_tr.g_40_shop.service.interfaces.ProductService;
import de.ait_tr.g_40_shop.service.mapping.ProductMappingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


@Service
public class ProductServiceImpl implements ProductService {

    private Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository repository;
    private final ProductMappingService mappingService;

    public ProductServiceImpl(ProductRepository repository, ProductMappingService mappingService) {
        this.repository = repository;
        this.mappingService = mappingService;
    }

    @Override
    public ProductDto save(ProductDto dto) {
       Product entity = mappingService.mapDtoToEntity(dto);

       try {
           repository.save(entity);
       } catch (Exception e) {
           throw new FourthTestException(e.getMessage());
       }

       return mappingService.mapEntityToDto(entity);
    }

    @Override
    public List<ProductDto> getAllActiveProducts() {
        return repository.findAll()
                .stream()
                .filter(Product::isActive)
//              .map(x -> mappingService.mapEntityToDto(x))
                .map(mappingService::mapEntityToDto)
                .toList();
    }

//    демонстрация логирования на разные уровни:
//    @Override
//    public ProductDto getById(Long id) {
//
//        logger.info("Method getById called with parameter {}", id);
//        logger.warn("Method getById called with parameter {}", id);
//        logger.error("Method getById called with parameter {}", id);
//
//        Product product = repository.findById(id).orElse(null);
//        if (product == null || !product.isActive()) {
//            return null;
//        }
//        return mappingService.mapEntityToDto(product);
//    }

    @Override
    public ProductDto getById(Long id) {

        Product product = repository.findById(id).orElse(null);
        if (product == null || !product.isActive()) {
            throw new ThirdTestException(String.format("Product with id %d not found", id));
        }
        return mappingService.mapEntityToDto(product);
    }

    @Override
    public ProductDto update(ProductDto product) {
        return null;
    }

    @Override
    public void deleteById(Long id) {


    }

    @Override
    public void deleteByTitle(String title) {

    }

    @Override
    public void restoreById(Long id) {

    }

    @Override
    public Long getActiveProductsQuantity() {
        return null;
    }

    @Override
    public BigDecimal getActiveProductsTotalPrice() {
        return null;
    }

    @Override
    public BigDecimal getActiveProductsAveragePrice() {
        return null;
    }

    @Override
    @Transactional //чтобы транзакция оставалась открыта до завершения всех операций
    public void attachImage(String imageUrl, String productTitle) {
        Product product = repository.findByTitle(productTitle).orElseThrow( //после этого транзакция
                // закрылась бы, если бы мы не указали @Transactional
                () -> new ProductNotFoundException(productTitle)
        );

        product.setImage(imageUrl); // эта аннотация @Transactional нужна,
        // чтобы транзакция не закрылась, и изменения в БД отразились
    }

    @Override
    public List<ProductSupplyDto> getProductsForSupply() {
        return repository.findAll()
                .stream()
                .filter(Product::isActive)
                .map(mappingService::mapEntityToSupplyDto)
                .toList();
    }
}
