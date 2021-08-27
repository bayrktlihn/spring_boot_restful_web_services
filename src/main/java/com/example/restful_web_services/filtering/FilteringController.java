package com.example.restful_web_services.filtering;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FilteringController {

  @GetMapping("/filtering")
  public MappingJacksonValue retrieveSomeBean() {
    final SomeBean someBean = new SomeBean("value1", "value2", "value3");

    SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("field1", "field2");
    FilterProvider filters = new SimpleFilterProvider().addFilter("SomeBeanFilter", filter);
    final MappingJacksonValue mapping = new MappingJacksonValue(someBean);
    mapping.setFilters(filters);

    return mapping;
  }

  @GetMapping("/filtering-list")
  public MappingJacksonValue retrieveListOfSomeBeans() {
    final List<SomeBean> list = Arrays.asList(
        new SomeBean("value1", "value2", "value3"),
        new SomeBean("value1", "value2", "value3"),
        new SomeBean("value1", "value2", "value3"),
        new SomeBean("value1", "value2", "value3")
    );

    final SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("field2", "field3");

    final SimpleFilterProvider filters = new SimpleFilterProvider().addFilter("SomeBeanFilter", filter);

    final MappingJacksonValue mapping = new MappingJacksonValue(list);

    mapping.setFilters(filters);

    return mapping;
  }

}
