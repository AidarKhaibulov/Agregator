package com.agregator.agr.controllers;

import com.agregator.agr.api.AvitoApi;
import com.agregator.agr.dto.ProductDto;
import com.agregator.agr.models.Cart;
import com.agregator.agr.models.Product;
import com.agregator.agr.models.RecentlyWatchedProduct;
import com.agregator.agr.models.UserEntity;
import com.agregator.agr.security.SecurityUtil;
import com.agregator.agr.services.CartService;
import com.agregator.agr.services.ProductService;
import com.agregator.agr.services.RecentlyWatchedProductService;
import com.agregator.agr.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@Slf4j
public class ProductController {
    private static ProductService productService = null;
    private final UserService userService;
    private final CartService cartService;
    private final RecentlyWatchedProductService recentlyWatchedProductService;

    public static ProductService getProductService() {
        return productService;
    }

    public ProductController(ProductService productService, UserService userService, CartService cartService, RecentlyWatchedProductService recentlyWatchedProductService) {
        ProductController.productService = productService;
        this.userService = userService;
        this.cartService = cartService;
        this.recentlyWatchedProductService = recentlyWatchedProductService;
    }

    @GetMapping("/")
    public String mainPage(Model model) {
        UserEntity user = new UserEntity();
        String username = SecurityUtil.getSessionUser();
        List<Product> productsInCart = new ArrayList<>();
        List<Product> popularProducts=productService.getPopularProducts();

        if (username != null) {
            user = userService.findByUsername(username);
            productsInCart.addAll(user.getCart().getProducts());
            model.addAttribute("user", user);
        }

        model.addAttribute("amountOfProductsInCart", productsInCart.size());
        model.addAttribute("popularProducts", popularProducts);
        model.addAttribute("user", user);
        return "index";
    }

    @GetMapping("/products/{pageNumber}/{sortMethod}")
    public String listProducts(@PathVariable("pageNumber") int pageNumber, @PathVariable("sortMethod") String sortMethod, Model model) {
        final int countOfProductOnPage = 15;
        UserEntity user = new UserEntity();
        Pageable currentPage;
        System.out.println(sortMethod);
        currentPage = switch (sortMethod) {
            case "price" -> PageRequest.of(pageNumber - 1, countOfProductOnPage, Sort.by("price"));
            case "priceReversed" -> PageRequest.of(pageNumber - 1, countOfProductOnPage, Sort.by("price").descending());
            case "date" -> PageRequest.of(pageNumber - 1, countOfProductOnPage, Sort.by("createdOn").descending());
            case "dateReversed" -> PageRequest.of(pageNumber - 1, countOfProductOnPage, Sort.by("createdOn"));
            default -> throw new IllegalStateException("Unexpected value: " + sortMethod);
        };
        List<ProductDto> productsToReturn = productService.findAllProducts(currentPage);
        String username = SecurityUtil.getSessionUser();
        List<Long> productsInCart = new ArrayList<>();
        if (username != null) {
            user = userService.findByUsername(username);
            user.getCart().getProducts().forEach(p -> productsInCart.add(p.getId()));
            model.addAttribute("user", user);
        }
        model.addAttribute("productsInCart", productsInCart);
        model.addAttribute("user", user);
        model.addAttribute("products", productsToReturn);
        model.addAttribute("method", sortMethod);
        return "shop";
    }

    @GetMapping("/product/{productId}")
    public String productDetail(@PathVariable("productId") long productId, Model model) {
        UserEntity user = new UserEntity();
        ProductDto productDto = productService.findProductById(productId);
        String username = SecurityUtil.getSessionUser();
        List<Long> productsInCart = new ArrayList<>();
        if (username != null) {
            user = userService.findByUsername(username);
            user.getCart().getProducts().forEach(p -> productsInCart.add(p.getId()));
            model.addAttribute("user", user);

            //Adding product to Recently watched
            RecentlyWatchedProduct recentlyWatchedProductCart = recentlyWatchedProductService.findCartByUser(user);
            var currentProductList = recentlyWatchedProductCart.getProducts();
            if(currentProductList.size()>4){
                currentProductList.remove(0);
            }
            Product currentProduct = productService.mapToProduct(productDto);
            if (currentProductList.stream().noneMatch(product -> Objects.equals(product.getId(), currentProduct.getId()))) {
                currentProductList.add(productService.mapToProduct(productDto));
                recentlyWatchedProductCart.setProducts(currentProductList);
                recentlyWatchedProductService.updateCart(recentlyWatchedProductCart);
            }
        }
        String urls = productDto.getPhotoUrl();
        String[] photos = urls.split("\n");
        model.addAttribute("user", user);
        model.addAttribute("productsInCart", productsInCart);
        model.addAttribute("recentProducts", user.getRecentlyWatchedProduct().getProducts());
        model.addAttribute("product", productDto);
        model.addAttribute("photos", photos);
        return "detail";
    }

    @GetMapping("/products/search")
    public String searchProduct(@RequestParam(value = "query") String query, Model model) {
        List<ProductDto> products = productService.searchProducts(query);

        setModels(model);

        model.addAttribute("products", products);
        return "shop";
    }

    private void setModels(Model model) {
        UserEntity user = new UserEntity();
        String username = SecurityUtil.getSessionUser();
        List<Long> productsInCart = new ArrayList<>();
        if (username != null) {
            user = userService.findByUsername(username);
            user.getCart().getProducts().forEach(p -> productsInCart.add(p.getId()));
            model.addAttribute("user", user);
        }

        model.addAttribute("productsInCart", productsInCart);
        model.addAttribute("user", user);
        model.addAttribute("method", "data");
    }

    @GetMapping("/products/searchByCategory")
    public String searchProductByCategory(@RequestParam(value = "query") String query, Model model) {
        List<ProductDto> products = productService.searchProductsByCategory(query);


        setModels(model);

        model.addAttribute("products", products);
        return "shop";
    }

    @GetMapping("/cart")
    public String cart(Model model) {
        UserEntity user = new UserEntity();
        String username = SecurityUtil.getSessionUser();
        List<Product> productsInCart = new ArrayList<>();
        if (username != null) {
            user = userService.findByUsername(username);
            productsInCart.addAll(user.getCart().getProducts());
            model.addAttribute("user", user);
        }
        model.addAttribute("productsInCart", productsInCart);
        model.addAttribute("user", user);
        return "cart";
    }

    @GetMapping("/newProduct")
    public String createProduct(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "products-create";
    }

    @PostMapping("/newProduct")
    public String saveProduct(@Valid @ModelAttribute("product") ProductDto product, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("product", product);
            return "products-create";
        }
        productService.saveProduct(product);
        log.info("product {} has been saved!",product);
        return "index";
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
    public String updateProduct(@PathVariable("productId") Long productId, @Valid @ModelAttribute("product") ProductDto product, BindingResult result) {
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

    @PostMapping("/deleteFromFavorite/{productId}")
    public String deleteFromFavorite(@PathVariable("productId") Long productId, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        String username = SecurityUtil.getSessionUser();
        UserEntity currentUser = userService.findByUsername(username);
        Cart currentCart = cartService.findCartByUser(currentUser);
        var currentProductList = currentCart.getProducts();
        int i = 0;
        while (!currentProductList.get(i).getId().equals(productId)) i++;
        currentProductList.remove(i);
        currentCart.setProducts(currentProductList);
        cartService.updateCart(currentCart);
        System.out.println("deleted");
        return "redirect:" + referer;
    }
}