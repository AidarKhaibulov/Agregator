package com.agregator.agr;
import java.io.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AgrApplication {
	static String DB_USERNAME="";
	static  String DB_PASSWORD="";
	public static  String VK_APP_TOKEN="";
	public static void main(String[] args) throws IOException {
		try (
				InputStream is1 = AgrApplication.class.getResourceAsStream("/db.txt");
				BufferedReader reader1 = new BufferedReader(new InputStreamReader(is1));
				InputStream is2 = AgrApplication.class.getResourceAsStream("/vk.txt");
		BufferedReader reader2 = new BufferedReader(new InputStreamReader(is2))
				)
		{
			DB_USERNAME=reader1.readLine();
			DB_PASSWORD=reader1.readLine();
			VK_APP_TOKEN= reader2.readLine();
		}

		SpringApplication.run(AgrApplication.class, args);
	}

}
