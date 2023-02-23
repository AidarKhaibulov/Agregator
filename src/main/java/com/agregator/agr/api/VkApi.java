package com.agregator.agr.api;

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

public class VkApi {
    private static final int userId = 161781103;
    private static final String accessToken = "10192d4f10192d4f10192d4f72130beab81101910192d4f73ecf7ee6ad42fefd061f1f6";
    private UserActor actor;
    private VkApiClient vk;
    public VkApi() {
        TransportClient transportClient = new HttpTransportClient();
        this.vk = new VkApiClient(transportClient);
        this.actor = new UserActor(userId,accessToken);
    }

    public String getProducts(int productsAmount) throws ClientException, ApiException {
        GetResponse getResponse = vk.wall().get(actor)
                .count(productsAmount)
                .domain("place_for_tourist")
                .execute();
        var wallPosts=getResponse.getItems();
        LinkedList<String> photosUrls=null;
        LinkedList<String> titles=null;
        for(var post: wallPosts){
            titles.add(post.getText());
            StringBuilder urls=new StringBuilder();
            for(var photo: post.getAttachments())
                urls.append(photo.getPhoto().getSizes());

        }
        var attachments= wallPosts.get(9).getAttachments();
        for(var el: attachments){
            Integer user_id=el.getPhoto().getOwnerId();
            Integer photo_id=el.getPhoto().getId();
            var r= vk.photos().getByIdLegacy(actor, String.valueOf(user_id+"_"+photo_id));
            for(var e:r.execute()){
                System.out.print(e.getSizes().get(1).getUrl());
            }
        }
        return null;
    }

}
