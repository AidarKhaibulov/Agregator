package com.agregator.agr.api;

import com.agregator.agr.dto.ProductDto;
import com.agregator.agr.models.Product;
import com.agregator.agr.services.ProductService;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.wall.WallpostAttachment;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import org.apache.commons.collections4.map.HashedMap;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VkApi {
    private static final int userId = 161781103;
    private static final String accessToken = "10192d4f10192d4f10192d4f72130beab81101910192d4f73ecf7ee6ad42fefd061f1f6";
    private UserActor actor;
    private VkApiClient vk;
    private ProductService productService;
    public VkApi(ProductService productService) {
        TransportClient transportClient = new HttpTransportClient();
        this.vk = new VkApiClient(transportClient);
        this.actor = new UserActor(userId,accessToken);
        this.productService=productService;
    }

    public String getProducts(int productsAmount) throws ClientException, ApiException {
        GetResponse getResponse = vk.wall().get(actor)
                .count(productsAmount)
                .domain("place_for_tourist")
                .execute();
        var wallPosts=getResponse.getItems();
        for(var post: wallPosts){

            Product product = new Product();

            String regex = "(ПРИМЕЧАНИЕ ОТ АДМИНИСТРАЦИИ)|([П|п]роверенный [П|п]родавец)";
            Pattern pattern = Pattern.compile(regex);
            String text=post.getText();
            String author="";
            if(post.getSignerId()!=null)
                author="https://vk.com/id"+post.getSignerId();
            Matcher matcher = pattern.matcher(text);

            product.setTitle("vk");
            product.setDescription(text);
            product.setPlatform(author);
            if(matcher.find()) {
                System.out.println(author);
                System.out.println(text);
                StringBuilder urls = new StringBuilder();
                for (var attachment : post.getAttachments()) {
                    if (attachment.getType().toString() == "photo") {
                        System.out.println(attachment.getPhoto().getSizes().get(0).getUrl());
                        product.setPhotoUrl(String.valueOf(attachment.getPhoto().getSizes().get(0).getUrl()));
                    }
                }
                System.out.println("_______________________________________________");
            }

            productService.saveProduct(productService.mapToProductDto(product));
        }

        return null;
    }

}
