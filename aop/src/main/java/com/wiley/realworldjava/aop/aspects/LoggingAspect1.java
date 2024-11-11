package com.wiley.realworldjava.aop.aspects;

import com.wiley.realworldjava.aop.product.Product;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Aspect
@Component
public class LoggingAspect1 {
   private final Logger logger = LoggerFactory.getLogger(LoggingAspect1.class);

   @Before("execution(com.wiley.realworldjava.aop.product.Pro*uct com.wiley.realworldjava.aop.product.ProductController.addProduct(..))")
   public void logAddOperations(JoinPoint joinPoint) {
      logger.info("=====> called {}.", joinPoint.getSignature().getName());
   }

   @AfterReturning(pointcut = "execution(public * com.wiley.realworldjava.aop.product." +
      "ProductController.addProduct(com.wiley.realworldjava.aop.product.Product))")
   public void logAfterReturning(JoinPoint joinPoint) {
      logger.info("=====> called after returning " + joinPoint.getSignature().getName());
   }

   @AfterReturning (value = "execution(public * com.wiley.realworldjava.aop.product."
      + "ProductController.addProduct(com.wiley.realworldjava.aop.product.Product))",
      returning = "product")
   public void descriptionToUpperCase(JoinPoint joinPoint, Product product){
      logger.info("===> called after returning {}. Returned {}.",
         joinPoint.getSignature().getName(), product) ;
      product.setDescription(product.getDescription().toUpperCase());
   }


   @AfterReturning(pointcut = "execution(public * com.wiley.realworldjava.aop.product.ProductController.addProduct(com.wiley.realworldjava.aop.product.Product))", returning = "product")
   public void logAfterReturning(JoinPoint joinPoint, Product product) {
      logger.info("=====> called after returning {}  returned:{}", joinPoint.getSignature().getName(), product);
   }

   @AfterThrowing(pointcut = "execution(public * com.wiley.realworldjava.aop.product.ProductController.listProductsWithException())",
      throwing = "cause")
   public void logAfterReturning(JoinPoint joinPoint, Throwable cause) {
      logger.info("=====> called after throwing " + joinPoint.getSignature().toShortString() + ". Throwing: " + cause);
   }

   private final Map<String, Connection> connectionCache = new HashMap<>();

   @Around("execution(public java.sql.Connection com.wiley.realworldjava.aop." +
           "product.ProductController.getDbConnection(java.util.Properties))")
   public Connection cacheConnection(ProceedingJoinPoint joinPoint)
           throws Throwable {
      Object[] args = joinPoint.getArgs();
      Properties properties = (Properties) args[0];
      Connection connection = connectionCache.get(
              properties.getProperty("database"));
      if(connection==null) {
         logger.info("==> Nothing cached. Aspect proceeds with the original call!");
         try {
            connection = (Connection) joinPoint.proceed();
            logger.info("==> Aspect caches the connection for future calls");
            connectionCache.put(properties.getProperty("database"), connection);
         } catch(Throwable throwable) {
            throw throwable;
         }
      } else {
         logger.info("Aspect got connection from cache!");
      }
      return connection;
   }

}
