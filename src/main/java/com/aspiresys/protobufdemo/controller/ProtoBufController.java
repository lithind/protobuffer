package com.aspiresys.protobufdemo.controller;

import com.aspiresys.protobufdemo.entity.Product;
import com.aspiresys.protobufdemo.service.ProtoBufService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.directory.SearchResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.zip.CRC32;

@RestController
@CrossOrigin("*")
public class ProtoBufController {

    @Value("${spring.application.gtoken}")
    private String token;

    @Autowired
    private ProtoBufService protoBufService;
//    @GetMapping()
//    public String getProducts() {
//        Product.product product = protoBufService.getProducts();
//        System.out.println(product.toByteArray().length);
//        byte[] serializedData = product.toByteArray();
//        System.out.println("Serialized data: " + Arrays.toString(serializedData));
////
//        return Base64.getEncoder().encodeToString(product.toByteArray());
//    }
    @GetMapping(value = "getProductsOriginal",produces = "application/x-protobuf")
    public byte[] getProductsOriginal() throws IOException {

        System.out.println( Arrays.toString(protoBufService.getProducts().toByteArray()));
        return  protoBufService.getProducts().toByteArray();
//        System.out.println(contentType);
//        Product.product product = protoBufService.getProducts();
//        return product.toByteArray();

//        List<Product.product> products = protoBufService.getProducts();
//        // Create a ByteArrayOutputStream to collect serialized product bytes
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//
//        // Serialize each Product and write to the ByteArrayOutputStream
//        for (Product.product product : products) {
//            product.writeTo(outputStream);
//        }
//        // Get the byte array containing all serialized products
//        byte[] allProductsByteArray = outputStream.toByteArray();
//        System.out.println(Arrays.toString(allProductsByteArray));
//        System.out.println(allProductsByteArray.length);
//
//        return allProductsByteArray;


    }



    @GetMapping(value = "/getString")
    public String getString() {
        return "Hello Lithin";
    }
    @GetMapping(value = "/git")
    public  void  getGIT() throws IOException, InterruptedException {
        String baseUrl = "https://api.github.com/search/code";
//        String user = "hishanamuhammed"; // GitHub username
//        String repository = "spring-demo"; // Repository name
        String keyword = "LendKey";

        //String apiUrl = String.format("%s?q=%s+repo:%s/%s", baseUrl, keyword, user, repository);
        String apiUrl = String.format("%s?q=%s"+"&per_page=100&page=1", baseUrl, keyword);

        String accessToken = token;

        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Accept", "application/vnd.github.v3+json")
                .header("Authorization", "Bearer " + accessToken)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200)
        {
            String responseBody = response.body();
            processSearchResults(responseBody);
            int count = processSearchResults(responseBody);
            System.out.println("Total HTML URLs found: " + count);

        } else {
            // Handle error response
            System.err.println("Error: " + response.statusCode());
        }


    }
    private static int processSearchResults(String responseBody) {
        int count = 0;
        // Parse JSON response and extract file paths containing the keyword
        Gson gson = new Gson();
        SearchResult searchResult = gson.fromJson(responseBody, SearchResult.class);

        if (searchResult != null && searchResult.getItems() != null) {
            List<SearchItem> items = searchResult.getItems();
            System.out.println("Files containing the keyword:");
            for (SearchItem item : items) {
                System.out.println(item.getHtmlUrl());
                count++;

                //System.out.println(item.getPath());
            }
        } else {
            System.out.println("No search results found.");
        }
        return count;
    }
    static class SearchResult {
        private List<SearchItem> items;
        public List<SearchItem> getItems() {
            return items;
        }
    }

    static class SearchItem {
        private String html_url;
        public String getHtmlUrl() {
            return html_url;
        }
    }
}
