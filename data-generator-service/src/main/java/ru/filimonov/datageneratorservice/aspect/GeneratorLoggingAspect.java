package ru.filimonov.datageneratorservice.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class GeneratorLoggingAspect {
  private final ObjectMapper objectMapper;

  @AfterReturning(pointcut = "ru.filimonov.datageneratorservice.aspect.pointcut.GeneratorPointcuts.allGenerateMethods()",
                  returning = "generatedData")
  public void loggingForGeneratedData(JoinPoint joinPoint, Object generatedData) {
    var methodName = joinPoint.getSignature().toShortString();
    var dataType = generatedData.getClass().getSimpleName();

    log.info("Generated data, method - [{}]; type of data - [{}]", methodName, dataType);

    if(log.isDebugEnabled()){
      try {
        var jsonForGeneratedData = objectMapper.writeValueAsString(generatedData);
        log.debug("Generated data, method - [{}], data: [{}]", methodName, jsonForGeneratedData);
      }catch (Exception exception){
        log.warn("Cannot serialize data for logging, method - [{}]",methodName, exception);
      }
    }
  }
}
