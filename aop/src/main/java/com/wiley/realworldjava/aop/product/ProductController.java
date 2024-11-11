package com.wiley.realworldjava.aop.product;

import com.wiley.realworldjava.aop.aspects.Cache;
import com.wiley.realworldjava.aop.aspects.Timing;
import com.wiley.realworldjava.aop.product.connection.DummyConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@RestController
public class ProductController {

   private final Map<String, Product> productMap = new HashMap<>();
   Logger logger = LoggerFactory.getLogger(ProductController.class);

   @PostMapping("/product")
   public Product addProduct(@RequestBody Product product) {
      logger.info("Adding product " + product);
      productMap.put(product.getStyleNum(), product);
      return product;
   }

   @DeleteMapping("/product")
   public Product removeProduct(String styleNumber){
      return productMap.remove(styleNumber);
   }

   @GetMapping("/products")
   public Collection<Product> listProducts(){
      return productMap.values();
   }

   @GetMapping("/products-exception")
   public Collection<Product> listProductsWithException() throws IOException {
      throw new IOException("An IOException to handle");
   }

   @GetMapping("/product")
   public Product productEndpoint(@RequestParam(required = false) String style, @RequestParam(required = false) String description){
      Product product = new Product(style, description);
      productMap.put(product.getStyleNum(), product);
      return product;
   }

   @GetMapping("/ping")
   @Timing
   public void ping(){
      logger.info("Ping hit");
   }


   @Override
   public String toString() {
      return super.toString() + " The instrumented instance";
   }


   @Cache
   @Timing
   @PostMapping("/connection")
   public Connection getDbConnection(@RequestBody Properties config) throws SQLException, ClassNotFoundException {
      Connection connection = createDBConnection(config.getProperty("driver"), config.getProperty("url"), config.getProperty("username"), config.getProperty("password"));
      return connection;
   }

   public Connection createDBConnection(String driver, String jdbcUrl, String username, String password) throws SQLException, ClassNotFoundException {
      // real implementation would do Class.forName(driver), and create a real connection
//      Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
      return new DummyConnection();
   }

}
