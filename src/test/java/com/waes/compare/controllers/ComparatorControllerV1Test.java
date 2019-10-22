package com.waes.compare.controllers;

import com.waes.compare.Application;
import com.waes.compare.controllers.dtos.ComparisonResponse;
import com.waes.compare.entities.Comparison;
import com.waes.compare.repositories.ComparisonRepository;
import java.util.Objects;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ComparatorControllerV1Test {

  private static final String BASE_64_JSON = "eyAiZW1haWwiOiJhc3Ryb2RpeEBnbWFpbC5jb20ifQ==";
  private static final String BASE_64_JSON_1 = "eyAiZW1haWwiOiJhc3Ryb2RpeEBhLmNvbSJ9";
  private static final String BASE_64_JSON_2 = "eyAiZW1haWwiOiJhc3Ryb211eUBnbWFpbC5jb20ifQ==";
  private static final String BASE_64_CORRUPT_JSON = "eyAiYXRvaWwiOiJhY2Z0eWRpeEBnbWFpbC5jb20=";
  private static final String INVALID_BASE_64 = "eyAiYXRvaWwiOiJhY2Z0eWRpeEBnbW";
  private static final String PATH_REGISTER_LEFT = "/v1/diff/{id}/left";
  private static final String PATH_REGISTER_RIGHT = "/v1/diff/{id}/right";
  private static final String PATH_COMPARE = "/v1/diff/{id}";
  private static final String EQUAL = "Equal size and content";
  private static final String NOT_EQUAL_SIZE = "Not equal size";
  private static final String NOT_EQUAL_CONTENT = "Equal size but different content";

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Autowired
  private ComparisonRepository comparisonRepository;


  @Test
  public void testRegisterLeft_Success(){
    String id = "testId";
    String url = PATH_REGISTER_LEFT.replace("{id}", id);
    String body = BASE_64_JSON;

    ResponseEntity<Void> response = testRestTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body), Void.class);

    Assert.assertTrue(response.getStatusCode().equals(HttpStatus.CREATED));

    Comparison comparison = comparisonRepository.findById(id).get();
    Assert.assertTrue(Objects.nonNull(comparison));

  }

  @Test
  public void testRegisterLeftNoBody_Error(){
    String id = "testId";
    String url = PATH_REGISTER_LEFT.replace("{id}", id);
    String body = null;

    ResponseEntity<Void> response = testRestTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body), Void.class);

    Assert.assertTrue(response.getStatusCode().equals(HttpStatus.BAD_REQUEST));
  }

  @Test
  public void testRegisterLeftInvalidBase64_Error(){
    String id = "testId";
    String url = PATH_REGISTER_LEFT.replace("{id}", id);
    String body = INVALID_BASE_64;

    ResponseEntity<Void> response = testRestTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body), Void.class);

    Assert.assertTrue(response.getStatusCode().equals(HttpStatus.BAD_REQUEST));
  }

  @Test
  public void testRegisterLeftCorruptJson_Error(){
    String id = "testId";
    String url = PATH_REGISTER_LEFT.replace("{id}", id);
    String body = BASE_64_CORRUPT_JSON;

    ResponseEntity<Void> response = testRestTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body), Void.class);

    Assert.assertTrue(response.getStatusCode().equals(HttpStatus.BAD_REQUEST));
  }

  @Test
  public void testRegisterRight_Success(){
    String id = "testId";
    String url = PATH_REGISTER_RIGHT.replace("{id}", id);
    String body = BASE_64_JSON;

    ResponseEntity<Void> response = testRestTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body), Void.class);

    Assert.assertTrue(response.getStatusCode().equals(HttpStatus.CREATED));

    Comparison comparison = comparisonRepository.findById(id).get();
    Assert.assertTrue(Objects.nonNull(comparison));

  }

  @Test
  public void testRegisterRightNoBody_Error(){
    String id = "testId";
    String url = PATH_REGISTER_RIGHT.replace("{id}", id);
    String body = null;

    ResponseEntity<Void> response = testRestTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body), Void.class);

    Assert.assertTrue(response.getStatusCode().equals(HttpStatus.BAD_REQUEST));
  }

  @Test
  public void testRegisterRightInvalidBase64_Error(){
    String id = "testId";
    String url = PATH_REGISTER_RIGHT.replace("{id}", id);
    String body = INVALID_BASE_64;

    ResponseEntity<Void> response = testRestTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body), Void.class);

    Assert.assertTrue(response.getStatusCode().equals(HttpStatus.BAD_REQUEST));
  }

  @Test
  public void testRegisterRightCorruptJson_Error(){
    String id = "testId";
    String url = PATH_REGISTER_RIGHT.replace("{id}", id);
    String body = BASE_64_CORRUPT_JSON;

    ResponseEntity<Void> response = testRestTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body), Void.class);

    Assert.assertTrue(response.getStatusCode().equals(HttpStatus.BAD_REQUEST));
  }

  @Test
  public void testCompareEntriesSameJson_Success(){
    String id = "testId";
    String urlRight = PATH_REGISTER_RIGHT.replace("{id}", id);
    String urlLeft = PATH_REGISTER_LEFT.replace("{id}", id);
    String urlCompare = PATH_COMPARE.replace("{id}", id);

    String base64 = BASE_64_JSON;

    testRestTemplate.exchange(urlRight, HttpMethod.POST, new HttpEntity<>(base64), Void.class);
    testRestTemplate.exchange(urlLeft, HttpMethod.POST, new HttpEntity<>(base64), Void.class);

    ResponseEntity<ComparisonResponse> response = testRestTemplate.exchange(urlCompare,
        HttpMethod.GET, new HttpEntity<>(null), ComparisonResponse.class);


    Assert.assertTrue(response.getStatusCode().equals(HttpStatus.OK));
    ComparisonResponse comparisonResult = response.getBody();
    Assert.assertTrue(Objects.nonNull(comparisonResult));
    Assert.assertTrue(comparisonResult.getComparisonResult().equals(EQUAL));

  }

  @Test
  public void testCompareEntriesDifferentLength_Success(){
    String id = "testId";
    String urlRight = PATH_REGISTER_RIGHT.replace("{id}", id);
    String urlLeft = PATH_REGISTER_LEFT.replace("{id}", id);
    String urlCompare = PATH_COMPARE.replace("{id}", id);

    String base64 = BASE_64_JSON;
    String base64_1 = BASE_64_JSON_1;

    testRestTemplate.exchange(urlRight, HttpMethod.POST, new HttpEntity<>(base64), Void.class);
    testRestTemplate.exchange(urlLeft, HttpMethod.POST, new HttpEntity<>(base64_1), Void.class);

    ResponseEntity<ComparisonResponse> response = testRestTemplate.exchange(urlCompare,
        HttpMethod.GET, new HttpEntity<>(null), ComparisonResponse.class);


    Assert.assertTrue(response.getStatusCode().equals(HttpStatus.OK));
    ComparisonResponse comparisonResult = response.getBody();
    Assert.assertTrue(Objects.nonNull(comparisonResult));
    Assert.assertTrue(comparisonResult.getComparisonResult().equals(NOT_EQUAL_SIZE));

  }

  @Test
  public void testCompareEntriesDifferentContent_Success(){
    String id = "testId";
    String urlRight = PATH_REGISTER_RIGHT.replace("{id}", id);
    String urlLeft = PATH_REGISTER_LEFT.replace("{id}", id);
    String urlCompare = PATH_COMPARE.replace("{id}", id);

    String base64 = BASE_64_JSON;
    String base64_1 = BASE_64_JSON_2;

    testRestTemplate.exchange(urlRight, HttpMethod.POST, new HttpEntity<>(base64), Void.class);
    testRestTemplate.exchange(urlLeft, HttpMethod.POST, new HttpEntity<>(base64_1), Void.class);

    ResponseEntity<ComparisonResponse> response = testRestTemplate.exchange(urlCompare,
        HttpMethod.GET, new HttpEntity<>(null), ComparisonResponse.class);

    Assert.assertTrue(response.getStatusCode().equals(HttpStatus.OK));
    ComparisonResponse comparisonResult = response.getBody();
    Assert.assertTrue(Objects.nonNull(comparisonResult));
    Assert.assertTrue(comparisonResult.getComparisonResult().equals(NOT_EQUAL_CONTENT));
    Assert.assertTrue(comparisonResult.getDetails().size() == 1);

  }

  @Test
  public void testCompareEntriesNoRecordFound_Error(){
    String id = "testId3";
    String urlCompare = PATH_COMPARE.replace("{id}", id);

    ResponseEntity<ComparisonResponse> response = testRestTemplate.exchange(urlCompare,
        HttpMethod.GET, new HttpEntity<>(null), ComparisonResponse.class);

    Assert.assertTrue(response.getStatusCode().equals(HttpStatus.NOT_FOUND));

  }

  @Test
  public void testCompareEntriesNoLeft_Error(){
    String id = "testId1";
    String urlRight = PATH_REGISTER_RIGHT.replace("{id}", id);
    String urlCompare = PATH_COMPARE.replace("{id}", id);

    String base64 = BASE_64_JSON;

    testRestTemplate.exchange(urlRight, HttpMethod.POST, new HttpEntity<>(base64), Void.class);

    ResponseEntity<ComparisonResponse> response = testRestTemplate.exchange(urlCompare,
        HttpMethod.GET, new HttpEntity<>(null), ComparisonResponse.class);

    Assert.assertTrue(response.getStatusCode().equals(HttpStatus.BAD_REQUEST));

  }

  @Test
  public void testCompareEntriesNoRight_Error(){
    String id = "testId2";
    String urlLeft = PATH_REGISTER_LEFT.replace("{id}", id);
    String urlCompare = PATH_COMPARE.replace("{id}", id);

    String base64_1 = BASE_64_JSON_1;

    testRestTemplate.exchange(urlLeft, HttpMethod.POST, new HttpEntity<>(base64_1), Void.class);

    ResponseEntity<ComparisonResponse> response = testRestTemplate.exchange(urlCompare,
        HttpMethod.GET, new HttpEntity<>(null), ComparisonResponse.class);


    Assert.assertTrue(response.getStatusCode().equals(HttpStatus.BAD_REQUEST));

  }

}