package com.wiley.realworldjava.preaop;

import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ProductController {

   private final Map<String, Product> productMap = new HashMap<>();

   @PostMapping("/product")
   public void addProduct(@RequestBody Product product) {
      productMap.put(product.getStyleNum(), product);
   }

   @DeleteMapping("/product")
   public Product removeProduct(String styleNumber){
      return productMap.remove(styleNumber);
   }

   @GetMapping("/product")
   public Collection<Product> listProducts(){
      return productMap.values();
   }
}
