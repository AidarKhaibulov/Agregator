package com.agregator.agr.controllers;

import com.agregator.agr.dto.ProductDto;
import com.agregator.agr.models.Product;
import com.agregator.agr.services.ProductService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String listProducts(Model model) {
        List<ProductDto> productList = productService.findAllProducts();
        model.addAttribute("products", productList);
        return "products-list";
    }

    @GetMapping("/products/new")
    public String createProduct(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "products-create";
    }

    @PostMapping("/products/new")
    public String saveProduct(@Valid @ModelAttribute("product") ProductDto product,
                              BindingResult result,
                              Model model) {
        if(result.hasErrors()){
            model.addAttribute("product",product);
            return "products-create";
        }
        productService.saveProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/products/{productId}/edit")
    public String editProduct(@PathVariable("productId") Long productId, Model model) {
        ProductDto product = productService.findProductById(productId);
        model.addAttribute("product", product);
        return "products-edit";
    }

    @PostMapping("/products/{productId}/edit")
    public String updateProduct(@PathVariable("productId") Long productId,
                                @Valid @ModelAttribute("product") ProductDto product,
                                BindingResult result) {
        if(result.hasErrors()){
            return "products-edit";
        }
        product.setId(productId);
        productService.updateProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/products/{productId}")
    public String productDetail(@PathVariable("productId") long productId, Model model){
        ProductDto productDto=productService.findProductById(productId);
        model.addAttribute("product",productDto);
        return "products-detail";
    }

}
