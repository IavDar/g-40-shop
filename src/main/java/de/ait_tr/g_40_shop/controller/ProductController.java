package de.ait_tr.g_40_shop.controller;

import de.ait_tr.g_40_shop.domain.dto.ProductDto;
import de.ait_tr.g_40_shop.service.interfaces.ProductService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ProductDto save(@RequestBody ProductDto product) {
        return service.save(product);
    }
    @GetMapping
    public ProductDto getById(@RequestParam Long id) {
       return service.getById(id);
    }
    @GetMapping("/all")
    public List<ProductDto> getAll() {
        return service.getAllActiveProducts();
    }

    @PutMapping
    public ProductDto update(@RequestBody ProductDto product) {
        return service.update(product);
    }

    @DeleteMapping
    public void delete(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String title
            ) {
        if (id != null) {
            service.deleteById(id);
        } else if (title != null) {
            service.deleteByTitle(title);
        }
    }
    @PutMapping("/restore")
    public void restore(@RequestParam Long id) {
        service.restoreById(id);
    }
    @GetMapping("/quantity")
    public long getQuantity() {
        return service.getActiveProductsQuantity();
    }
    @GetMapping("/total-Price")
    public BigDecimal getTotalPrice() {
        return service.getActiveProductsTotalPrice();
    }

    @GetMapping("/average-Price")
    public BigDecimal getAveragePrice() {
        return service.getActiveProductsAveragePrice();
    }
}
