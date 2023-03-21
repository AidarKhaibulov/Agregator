package com.agregator.agr;
import java.io.*;

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
				InputStream is2 = AgrApplication.class.getResourceAsStream("/vk.txt");
		BufferedReader reader2 = new BufferedReader(new InputStreamReader(is2))
				)
		{
			VK_APP_TOKEN= reader2.readLine();
		}

		SpringApplication.run(AgrApplication.class, args);

		ProductService productService= ProductController.getProductService();
		VkApi vk = new VkApi(productService);
		vk.getProducts(10);
	}

}
