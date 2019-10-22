package com.waes.compare.controllers;

import com.waes.compare.adapters.ComparisonAdapter;
import com.waes.compare.controllers.dtos.ComparisonResponse;
import com.waes.compare.enums.Side;
import io.swagger.annotations.SwaggerDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.waes.compare.services.ComparatorService;

@RestController
@RequestMapping(value = "/v1/diff", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@SwaggerDefinition
public class ComparatorControllerV1 {

  @Autowired
  private ComparatorService comparatorService;

  @PostMapping("{id}/left")
  @ResponseStatus(HttpStatus.CREATED)
  public void registerLeft(@PathVariable("id") String id, @RequestBody String encodedContent){
      comparatorService.registerSide(id, Side.LEFT, encodedContent);
  }

  @PostMapping("{id}/right")
  @ResponseStatus(HttpStatus.CREATED)
  public void registerRight(@PathVariable("id") String id, @RequestBody String encodedContent){
      comparatorService.registerSide(id, Side.RIGHT, encodedContent);
  }

  @GetMapping("{id}")
  public ResponseEntity<ComparisonResponse> compareEntries(@PathVariable("id") String id){
    return ResponseEntity.ok(ComparisonAdapter.buildComparisonResponse(comparatorService.compareEntries(id)));
  }
}
