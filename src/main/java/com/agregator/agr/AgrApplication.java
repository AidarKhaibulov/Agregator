package com.agregator.agr;
import java.io.*;

import com.agregator.agr.api.AvitoApi;
import com.agregator.agr.api.VkApi;
import com.agregator.agr.controllers.ProductController;
import com.agregator.agr.services.ProductService;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AgrApplication {
	public static  String VK_APP_TOKEN="";
	public static void main(String[] args) throws IOException, ClientException, ApiException {
		try (
				// file should be in resources folder
				InputStream is2 = AgrApplication.class.getResourceAsStream("/vk.txt");
				BufferedReader reader2 = new BufferedReader(new InputStreamReader(is2))
		)
		{
			VK_APP_TOKEN= reader2.readLine();
		}
		SpringApplication.run(AgrApplication.class, args);


		// Vk async parsing
		Thread vkParsingProducts = new Thread(() -> {
			while(true) {
				ProductService productService = ProductController.getProductService();
				VkApi vk = new VkApi(productService);
				try {
					vk.getProducts(10);
				} catch (ClientException e) {
					throw new RuntimeException(e);
				} catch (ApiException e) {
					throw new RuntimeException(e);
				}
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		});

		// Avito async parsing
		Thread avitoParsingProducts = new Thread(() -> {
			AvitoApi avitoApi= new AvitoApi();
			try {
				avitoApi.getProducts("kazan","лыжи");
			} catch (IOException e) {
				throw new RuntimeException(e);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		});

		vkParsingProducts.start();
		avitoParsingProducts.start();

	}

}
