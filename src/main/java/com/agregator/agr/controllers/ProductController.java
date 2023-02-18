package com.agregator.agr.controllers;

import com.agregator.agr.dto.ProductDto;
import com.agregator.agr.models.Product;
import com.agregator.agr.models.UserEntity;
import com.agregator.agr.security.SecurityUtil;
import com.agregator.agr.services.ProductService;
import com.agregator.agr.services.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductController {
    private ProductService productService;
    private UserService userService;

    public ProductController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/products")
    public String listProducts(Model model) {
        UserEntity user=new UserEntity();
        List<ProductDto> productList = productService.findAllProducts();
        String username= SecurityUtil.getSessionUser();
        if(username!=null){
            user=userService.findByUsername(username);
            model.addAttribute("user",user);
        }
        model.addAttribute("user",user);
        model.addAttribute("products", productList);
        //return "products-list";
        return "shop";
    }

    @GetMapping("/products/search")
    public String searchProduct(@RequestParam(value = "query") String query, Model model) {
        List<ProductDto> products = productService.searchProducts(query);
        model.addAttribute("products", products);
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
        if (result.hasErrors()) {
            model.addAttribute("product", product);
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
        if (result.hasErrors()) {
            return "products-edit";
        }
        product.setId(productId);
        productService.updateProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/products/{productId}")
    public String productDetail(@PathVariable("productId") long productId, Model model) {
        UserEntity user=new UserEntity();
        ProductDto productDto = productService.findProductById(productId);
        String username= SecurityUtil.getSessionUser();
        if(username!=null){
            user=userService.findByUsername(username);
            model.addAttribute("user",user);
        }
        model.addAttribute("user",user);
        model.addAttribute("product", productDto);
        return "products-detail";
    }

    @GetMapping("/products/{productId}/delete")
    public String deleteProduct(@PathVariable("productId") Long productId) {

        productService.delete(productId);
        return "redirect:/products";
    }


}
