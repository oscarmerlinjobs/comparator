package com.waes.compare.services;

import static org.junit.Assert.*;

import com.waes.compare.dtos.ComparisonDTO;
import com.waes.compare.entities.Comparison;
import com.waes.compare.enums.Side;
import com.waes.compare.repositories.ComparisonRepository;
import java.util.Optional;
import javax.swing.text.html.Option;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ComparatorServiceImplTest {

  private static final String BASE_64_JSON = "eyAiZW1haWwiOiJhc3Ryb2RpeEBnbWFpbC5jb20ifQ==";
  private static final String BASE_64_JSON_1 = "eyAiZW1haWwiOiJhc3Ryb2RpeEBhLmNvbSJ9";
  private static final String BASE_64_JSON_2 = "eyAiZW1haWwiOiJhc3Ryb211eUBnbWFpbC5jb20ifQ==";
  private static final String BASE_64_CORRUPT_JSON = "eyAiYXRvaWwiOiJhY2Z0eWRpeEBnbWFpbC5jb20=";
  private static final String EQUAL = "Equal size and content";
  private static final String NOT_EQUAL_SIZE = "Not equal size";
  private static final String NOT_EQUAL_CONTENT = "Equal size but different content";

  @MockBean
  private ComparisonRepository comparisonRepository;

  @Autowired
  private ComparatorService comparatorService;

  @Test
  public void testRegisterSideLeftRecordFound_Success() {
    String id = "testId";
    Comparison comparison = new Comparison();
    comparison.setId(id);
    Mockito.when(comparisonRepository.findById(id)).thenReturn(Optional.of(comparison));
    Mockito.when(comparisonRepository.save(Mockito.any(Comparison.class))).thenReturn(new Comparison());

    comparatorService.registerSide(id, Side.LEFT, BASE_64_JSON);

    Mockito.verify(comparisonRepository, Mockito.times(1)).findById(id);
    Mockito.verify(comparisonRepository, Mockito.times(1)).save(Mockito.any(Comparison.class));
  }

  @Test
  public void testRegisterSideLeftRecordNotFound_Success() {
    String id = "testId";
    Mockito.when(comparisonRepository.findById(id)).thenReturn(Optional.empty());
    Mockito.when(comparisonRepository.save(Mockito.any(Comparison.class))).thenReturn(new Comparison());

    comparatorService.registerSide(id, Side.LEFT, BASE_64_JSON);

    Mockito.verify(comparisonRepository, Mockito.times(1)).findById(id);
    Mockito.verify(comparisonRepository, Mockito.times(1)).save(Mockito.any(Comparison.class));
  }

  @Test
  public void testRegisterSideRightRecordFound_Success() {
    String id = "testId";
    Comparison comparison = new Comparison();
    comparison.setId(id);
    Mockito.when(comparisonRepository.findById(id)).thenReturn(Optional.of(comparison));
    Mockito.when(comparisonRepository.save(Mockito.any(Comparison.class))).thenReturn(new Comparison());

    comparatorService.registerSide(id, Side.LEFT, BASE_64_JSON);

    Mockito.verify(comparisonRepository, Mockito.times(1)).findById(id);
    Mockito.verify(comparisonRepository, Mockito.times(1)).save(Mockito.any(Comparison.class));
  }

  @Test
  public void testRegisterSideRightRecordNotFound_Success() {
    String id = "testId";
    Mockito.when(comparisonRepository.findById(id)).thenReturn(Optional.empty());
    Mockito.when(comparisonRepository.save(Mockito.any(Comparison.class))).thenReturn(new Comparison());

    comparatorService.registerSide(id, Side.LEFT, BASE_64_JSON);

    Mockito.verify(comparisonRepository, Mockito.times(1)).findById(id);
    Mockito.verify(comparisonRepository, Mockito.times(1)).save(Mockito.any(Comparison.class));
  }

  @Test(expected=IllegalArgumentException.class)
  public void testRegisterSideRightRecordNotFound_InvalidJson() {
    String id = "testId";
    Mockito.when(comparisonRepository.findById(id)).thenReturn(Optional.empty());
    Mockito.when(comparisonRepository.save(Mockito.any(Comparison.class))).thenReturn(new Comparison());

    comparatorService.registerSide(id, Side.LEFT, BASE_64_CORRUPT_JSON);

    Mockito.verify(comparisonRepository, Mockito.times(0)).findById(id);
    Mockito.verify(comparisonRepository, Mockito.times(0)).save(Mockito.any(Comparison.class));
  }

  @Test
  public void testCompareEntriesEqual_Success() {
    String id = "testId";
    Comparison comparison = new Comparison();
    comparison.setId(id);
    comparison.setLeft(BASE_64_JSON);
    comparison.setRight(BASE_64_JSON);
    Mockito.when(comparisonRepository.findById(id)).thenReturn(Optional.of(comparison));

    ComparisonDTO comparisonDTO = comparatorService.compareEntries(id);

    Mockito.verify(comparisonRepository, Mockito.times(1)).findById(id);
    Assert.assertTrue(EQUAL.equals(comparisonDTO.getComparisonResult()));
  }

  @Test
  public void testCompareEntriesNotEqualSize_Success() {
    String id = "testId";
    Comparison comparison = new Comparison();
    comparison.setId(id);
    comparison.setLeft(BASE_64_JSON);
    comparison.setRight(BASE_64_JSON_1);
    Mockito.when(comparisonRepository.findById(id)).thenReturn(Optional.of(comparison));

    ComparisonDTO comparisonDTO = comparatorService.compareEntries(id);

    Mockito.verify(comparisonRepository, Mockito.times(1)).findById(id);
    Assert.assertTrue(NOT_EQUAL_SIZE.equals(comparisonDTO.getComparisonResult()));
  }

  @Test
  public void testCompareEntriesNotEqualContent_Success() {
    String id = "testId";
    Comparison comparison = new Comparison();
    comparison.setId(id);
    comparison.setLeft(BASE_64_JSON);
    comparison.setRight(BASE_64_JSON_2);
    Mockito.when(comparisonRepository.findById(id)).thenReturn(Optional.of(comparison));

    ComparisonDTO comparisonDTO = comparatorService.compareEntries(id);

    Mockito.verify(comparisonRepository, Mockito.times(1)).findById(id);
    Assert.assertTrue(NOT_EQUAL_CONTENT.equals(comparisonDTO.getComparisonResult()));
    Assert.assertTrue(comparisonDTO.getComparisonDetails().size() == 2);
  }

  @Test(expected=IllegalArgumentException.class)
  public void testCompareEntriesNoLeft_Error() {
    String id = "testId";
    Comparison comparison = new Comparison();
    comparison.setId(id);
    comparison.setRight(BASE_64_JSON_2);
    Mockito.when(comparisonRepository.findById(id)).thenReturn(Optional.of(comparison));

    ComparisonDTO comparisonDTO = comparatorService.compareEntries(id);

    Mockito.verify(comparisonRepository, Mockito.times(1)).findById(id);
  }

  @Test(expected=IllegalArgumentException.class)
  public void testCompareEntriesNoRight_Error() {
    String id = "testId";
    Comparison comparison = new Comparison();
    comparison.setId(id);
    comparison.setLeft(BASE_64_JSON_2);
    Mockito.when(comparisonRepository.findById(id)).thenReturn(Optional.of(comparison));

    ComparisonDTO comparisonDTO = comparatorService.compareEntries(id);

    Mockito.verify(comparisonRepository, Mockito.times(1)).findById(id);
  }

}