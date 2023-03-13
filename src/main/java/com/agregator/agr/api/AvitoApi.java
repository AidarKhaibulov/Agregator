package com.agregator.agr.api;


import com.agregator.agr.controllers.ProductController;
import com.agregator.agr.models.Product;
import com.agregator.agr.services.ProductService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public final class AvitoApi {
    public AvitoApi() {
    }

    private final ProductService productService= ProductController.getProductService();
    public void getProducts(String city, String desiredProduct) throws IOException, InterruptedException {
        Product product = new Product();

        Document doc = Jsoup.connect("https://www.avito.ru/" + city + "?p=1&q=" + desiredProduct).get();
        Elements pag = doc.getElementsByClass("link-link-MbQDP link-design-default-_nSbv title-root-zZCwT iva-item-title-py3i_ title-listRedesign-_rejR title-root_maxHeight-X6PsH");
        int i = 0;

        for (var el : pag) {
            i++;
            if (i == 20)
                break;

            //link
            String platform="https://www.avito.ru" + el.attributes().asList().get(0).getValue();
            product.setPlatform(platform);
            System.out.println(platform);
            doc = Jsoup.connect(platform).get();

            // title
            Element pag2 = doc.getElementsByClass("title-info-title-text").first();
            product.setTitle(pag2.text());
            System.out.println(pag2.text());

            //description
            pag2 = doc.getElementsByClass("style-item-description-text-mc3G6").first();
            if(pag2!=null) {
                product.setDescription(pag2.text());
                System.out.println(pag2.text());
            }

            //price
            pag2 = doc.getElementsByClass("js-item-price style-item-price-text-_w822 text-text-LurtD text-" +
                    "size-xxl-UPhmI").first();
            product.setPrice(Double.valueOf(pag2.attributes().asList().get(0).getValue()));
            System.out.println(pag2.attributes().asList().get(0).getValue());

            //adress
            pag2 = doc.getElementsByClass("style-item-address__string-wt61A").first();
            System.out.println(pag2.text());

            //photoUrl
            StringBuilder urls = new StringBuilder("");
            pag2 = doc.getElementsByClass("desktop-1ky5g7j").first();
            urls.append(pag2.attributes().asList().get(0).getValue()).append("\n");
            Elements pag3 = doc.getElementsByClass("desktop-1i6k59z");
            for (var e : pag3)
                urls.append(e.attributes().asList().get(0).getValue()).append("\n");
            product.setPhotoUrl(String.valueOf(urls));
            System.out.println(urls);
            System.out.println("__________________________________");

            if(productService.findProductByPlatform(platform)==null) {
                System.out.println("added!");
                productService.saveProduct(productService.mapToProductDto(product));
            }
            else{
                System.out.println("not added");
            }
            Thread.sleep(2000);
        }
    }
}
