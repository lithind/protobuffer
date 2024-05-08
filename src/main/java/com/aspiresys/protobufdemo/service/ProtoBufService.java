package com.aspiresys.protobufdemo.service;

import com.aspiresys.protobufdemo.entity.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProtoBufService {

    public Product.prodcutList getProducts()
    {
//        return Product.product.newBuilder()
//                .setTitle("Samsung")
//                .setDescription("Desc")
//                .setPrice("1000")
//                .build();


        List<Product.product> products = new ArrayList<>();
        Product.product product1 = Product.product.newBuilder()
                .setTitle("Samsung")
                .setDescription("Desc")
                .setPrice("1000")
                .build();
        Product.product product2 = Product.product.newBuilder()
                .setTitle("Apple")
                .setDescription("Desc")
                .setPrice("1500")
                .build();


//        products.add(product1);
        products.add(product2);

        // Build the ProductList message
        Product.prodcutList productList = Product.prodcutList.newBuilder()
                .addAllProductList(products)
                .build();
        System.out.println(productList);
        return  productList;

    }
}
