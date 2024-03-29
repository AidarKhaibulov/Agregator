package com.agregator.agr.api;

import com.agregator.agr.models.Product;
import com.agregator.agr.services.ProductService;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.wall.responses.GetResponse;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.agregator.agr.AgrApplication.VK_APP_TOKEN;

public final class VkApi {
    private static final int userId = 161781103;
    private static final String accessToken = VK_APP_TOKEN;
    private final UserActor actor;
    private final VkApiClient vk;
    private final ProductService productService;

    public VkApi(ProductService productService) {
        TransportClient transportClient = new HttpTransportClient();
        this.vk = new VkApiClient(transportClient);
        this.actor = new UserActor(userId, accessToken);
        this.productService = productService;
    }

    public void getProducts(int productsAmount) throws ClientException, ApiException {
        String addsFilterRegex = "(ПРИМЕЧАНИЕ ОТ АДМИНИСТРАЦИИ)|([П|п]роверенный [П|п]родавец)|" +
                "(списке проверенных продавцов под номером)";
       /* String productsTagsRegex = "([Б|б]отинки)|([К|к]россовки)|([К|к]уртка)|([S|s]oft[S|s]hell)|([К|к]арабины)|" +
                "([с|С]пальный мешок)|([П|п]уховик)|([Р|р]юкзак)|([П|п]алатка)|([Т|т]ермобель[ё|е])";*/
        String productsTagsRegex = "([Б|б]отинки?|[К|к]уртк[иа]?|[П|п]уховик[иа]?|[Р|р]юкзак[иа]?|[Т|т]ермобель[ея]?|softshell?|[М|м]ембран[ау]?|[К|к]россовки?|[П|п]алатк[и]?|[С|с]пальны[йе] [М|м]ешок[и]?|[К|к]арабин[ы]?|[Л|л]едоруб[ы]?|[О|о]ттяжк[иа]?|[В|в]еревк[иа]?|[К|к]ошки?|[К|к]аск[аи]?+)";
        GetResponse groupPosts = vk.wall().get(actor).count(productsAmount).domain("place_for_tourist").execute();
        var wallPosts = groupPosts.getItems();
        for (var post : wallPosts) {
            Product product = new Product();
            String text = post.getText();
            String platform = "https://vk.com/place_for_tourist?w=wall"+post.getOwnerId()+"_"+post.getId();
            StringBuilder title = new StringBuilder("Объявление VK");
            Pattern pattern = Pattern.compile(addsFilterRegex);
            Matcher allowedPosts = pattern.matcher(text);
            if (allowedPosts.find()) {
                List<String> allMatches = new ArrayList<>();
                Matcher productsTags = Pattern.compile(productsTagsRegex).matcher(text);
                while (productsTags.find()) allMatches.add(productsTags.group());
                Set<String> tags = new HashSet<>();
                for (String match : allMatches)
                    if (!tags.contains(match.toLowerCase())) {
                        title.append(" ").append(match.toLowerCase());
                        tags.add(match.toLowerCase());
                    }
                product.setTitle(String.valueOf(title));
                product.setDescription(text);
                product.setPlatform(platform);
                StringBuilder urls = new StringBuilder();
                for (var attachment : post.getAttachments())
                    if (attachment.getType().toString().equals("photo")) {
                        System.out.println(attachment.getPhoto().getSizes().get(3).getUrl());
                        urls.append("\n").append(attachment.getPhoto().getSizes().get(3).getUrl());
                    }
                urls.deleteCharAt(0);
                product.setPhotoUrl(String.valueOf(urls));
                product.setPrice(0.0);
                if(productService.findProductByPhotoUrl(String.valueOf(urls))==null) {
                    System.out.println("added!");
                    productService.saveProduct(productService.mapToProductDto(product));
                }
                else{
                    System.out.println("not added");
                }
            }
        }
    }

}
