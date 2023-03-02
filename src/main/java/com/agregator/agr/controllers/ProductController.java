package com.agregator.agr.controllers;

import com.agregator.agr.api.VkApi;
import com.agregator.agr.dto.ProductDto;
import com.agregator.agr.models.Cart;
import com.agregator.agr.models.Product;
import com.agregator.agr.models.UserEntity;
import com.agregator.agr.security.SecurityUtil;
import com.agregator.agr.services.CartService;
import com.agregator.agr.services.ProductService;
import com.agregator.agr.services.UserService;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class ProductController {
    private ProductService productService;
    private UserService userService;
    private CartService cartService;

    public ProductController(ProductService productService, UserService userService, CartService cartService) {
        this.productService = productService;
        this.userService = userService;
        this.cartService = cartService;
    }

    @GetMapping("/home")
    public String home() throws ClientException, ApiException {
        VkApi vk = new VkApi(productService);
        vk.getProducts(10);
        return "index";
    }

    @GetMapping("/products")
    public String listProducts(Model model) {
        UserEntity user = new UserEntity();
        List<ProductDto> productList = productService.findAllProducts();
        String username = SecurityUtil.getSessionUser();
        List<Long> productsInCart = new ArrayList<>();
        if (username != null) {
            user = userService.findByUsername(username);
            user.getCart().getProducts().forEach(p -> productsInCart.add(p.getId()));
            model.addAttribute("user", user);
        }
        model.addAttribute("productsInCart", productsInCart);
        model.addAttribute("user", user);
        model.addAttribute("products", productList);

        return "shop";
    }

    @GetMapping("/products/{productId}")
    public String productDetail(@PathVariable("productId") long productId, Model model) {
        UserEntity user = new UserEntity();
        ProductDto productDto = productService.findProductById(productId);
        String username = SecurityUtil.getSessionUser();
        List<Long> productsInCart = new ArrayList<>();
        if (username != null) {
            user = userService.findByUsername(username);
            user.getCart().getProducts().forEach(p -> productsInCart.add(p.getId()));
            model.addAttribute("user", user);
        }

        String urls = productDto.getPhotoUrl();
        String[] photos = urls.split("\n");
        model.addAttribute("user", user);
        model.addAttribute("productsInCart", productsInCart);
        model.addAttribute("product", productDto);
        model.addAttribute("photos", photos);
        return "detail";
    }

    @GetMapping("/products/search")
    public String searchProduct(@RequestParam(value = "query") String query, Model model) {
        List<ProductDto> products = productService.searchProducts(query);
        model.addAttribute("products", products);
        return "shop";
    }

    @GetMapping("/products/searchByCategory")
    public String searchProductByCategory(@RequestParam(value = "query") String query, Model model) {
        List<ProductDto> products = productService.searchProductsByCategory(query);
        model.addAttribute("products", products);
        return "shop";
    }

    @GetMapping("/newProduct")
    public String createProduct(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "products-create";
    }

    @PostMapping("/newProduct")
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

    @GetMapping("/products/{productId}/delete")
    public String deleteProduct(@PathVariable("productId") Long productId) {

        productService.delete(productId);
        return "redirect:/products";
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

    @PostMapping("/addToFavorite/{productId}")
    public String addToFavorite(@PathVariable("productId") Long productId, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        String username = SecurityUtil.getSessionUser();
        UserEntity currentUser = userService.findByUsername(username);
        Cart currentCart = cartService.findCartByUser(currentUser);
        var currentProductList = currentCart.getProducts();
        Product product = productService.mapToProduct(productService.findProductById(productId));
        currentProductList.add(product);
        currentCart.setProducts(currentProductList);
        cartService.updateCart(currentCart);
        return "redirect:" + referer;
    }
}