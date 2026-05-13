package ru.filimonov.datageneratorservice.aspect.pointcut;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
public class GeneratorPointcuts {
  @Pointcut("@annotation(ru.filimonov.datageneratorservice.annotation.GenerateData)")
  public void allGenerateMethods(){}
}
