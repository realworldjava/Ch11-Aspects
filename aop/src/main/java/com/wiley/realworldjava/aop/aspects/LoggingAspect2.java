package com.wiley.realworldjava.aop.aspects;

import com.wiley.realworldjava.aop.product.Product;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

//@Aspect
@Component
public class LoggingAspect2 {
   private final Logger logger = LoggerFactory.getLogger(LoggingAspect2.class);

   @Before("execution(public * com.wiley.realworldjava.aop.product.ProductController.addProduct(..))")
   public void logAddOperations(JoinPoint joinPoint) {
      logger.info("=====> called {}", joinPoint.getSignature().getName());
   }
   // Define the pointcut method "addProductPointcut"
   @Pointcut(value = "execution(public * com.wiley.realworldjava.aop.product.ProductController.addProduct(com.wiley.realworldjava.aop.product.Product))")
   public void addProductPointcut() {}

   // refer to the pointcut method, instead of inlining the execution
   @AfterReturning(value = "addProductPointcut()")
   public void logAfterReturning(JoinPoint joinPoint) {
      logger.info("=====> called after returning " + joinPoint.getSignature().getName());
   }

   @AfterReturning(value = "addProductPointcut()", returning = "product")
   public void logAfterReturningWithReturningValue(JoinPoint joinPoint, Product product) {
      logger.info("=====> called after returning {}  returned:{}", joinPoint.getSignature().getName(), product);
   }

   @AfterThrowing(value = "addProductPointcut()", throwing = "cause")
   public void logAfterReturningWithException(JoinPoint joinPoint, Throwable cause) {
      logger.info("=====> called after throwing " + joinPoint.getSignature().toShortString() + ". Throwing: " + cause);
   }

   @Pointcut("@annotation(com.wiley.realworldjava.aop.aspects.Cache)")
   public void cacheAnnotationMethod(){
   }

   /**
    * Map to capture cached results
    */
   private final Map<MultiKey, Object> methodCache = new HashMap<>();

   @Around("cacheAnnotationMethod()")
   public Object cacheMethodResults(ProceedingJoinPoint joinPoint) throws Throwable {
      // Each unique combination of args will be cached separately. Create a multikey representing the args:
      Object[] args = joinPoint.getArgs();
      Object[] argsLong = new Object[args.length + 1];
      System.arraycopy(args, 0, argsLong, 1, args.length);
      // we want to cache each different method separately, so add the method and signature to the MultiKey:
      argsLong[0] = joinPoint.toLongString();
      MultiKey key = new MultiKey(argsLong);

      Object result = methodCache.get(key);
      if(result==null) {
         logger.info("Nothing cached. Aspect proceeds with the original call!");
         try {
            result = joinPoint.proceed();
            logger.info("Aspect caches the connection for future calls");
            methodCache.put(key, result);
         } catch(Throwable throwable) {
            throw throwable;
         }
      }
      else {
         logger.info("Aspect got connection from cache!");
      }
      return result;
   }
   @Pointcut("@annotation(com.wiley.realworldjava.aop.aspects.Timing)")
   public void timingAnnotationMethod(){
   }

   @Around("timingAnnotationMethod()")
   public Object cacheTimingResults(ProceedingJoinPoint joinPoint) throws Throwable {
// grab the start time
      long start = System.currentTimeMillis();
// execute the target method…
      Object result = joinPoint.proceed();
// grab the end time
      long end = System.currentTimeMillis();
// log the timing
      logger.info(">>>> Call to {} took {} ms.", joinPoint.toString(), end-start);
// return the result
      return result;
   }


}
