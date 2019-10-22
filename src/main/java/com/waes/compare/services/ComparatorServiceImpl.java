package com.waes.compare.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waes.compare.dtos.ComparisonDTO;
import com.waes.compare.dtos.ComparisonDetailDTO;
import com.waes.compare.entities.Comparison;
import com.waes.compare.enums.Side;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.waes.compare.repositories.ComparisonRepository;

@Service
public class ComparatorServiceImpl implements ComparatorService {

  private static final String EQUAL = "Equal size and content";
  private static final String NOT_EQUAL_SIZE = "Not equal size";
  private static final String NOT_EQUAL_CONTENT = "Equal size but different content";

  @Autowired
  private ComparisonRepository comparisonRepository;

  private static final Logger LOGGER = LoggerFactory.getLogger(ComparatorServiceImpl.class);

  @Override
  public void registerSide(String id, Side side, String encodedContent) {
    LOGGER.debug("Registering comparison part for side: {}", side.name());

    String json = new String(Base64.getDecoder().decode(encodedContent));
    validateJsonStructure(json);
    Comparison comparison = comparisonRepository.findById(id).orElse(new Comparison(id));

    if (side.equals(Side.RIGHT)) {
      comparison.setRight(json);
    } else if (side.equals(Side.LEFT)) {
      comparison.setLeft(json);
    }
    comparisonRepository.save(comparison);
  }

  private void validateJsonStructure(String json) {
    try {
      new ObjectMapper().readTree(json);
    } catch (IOException e) {
      LOGGER.error("invalid Json", e);
      throw new IllegalArgumentException("Invalid Json", e);
    }
  }

  @Override
  public ComparisonDTO compareEntries(String entryId) {
    LOGGER.debug("Comparing json entries with id: {}", entryId);
    Comparison comparison = comparisonRepository.findById(entryId)
        .orElseThrow(() -> new EntityNotFoundException("Comparison entity not found"));

    if(Objects.isNull(comparison.getLeft()) || Objects.isNull(comparison.getRight())){
      throw new IllegalArgumentException("Object to compare does not have required information");
    }

    ComparisonDTO comparisonDTO;
    if (comparison.getLeft().equals(comparison.getRight())) {
      comparisonDTO = ComparisonDTO.builder().comparisonResult(EQUAL).build();
    } else if (comparison.getLeft().length() != comparison.getRight().length()) {
      comparisonDTO = ComparisonDTO.builder().comparisonResult(NOT_EQUAL_SIZE)
          .build();
    } else {
      comparisonDTO = ComparisonDTO.builder().comparisonResult(NOT_EQUAL_CONTENT)
          .comparisonDetails(getComparisonDetails(comparison.getRight(), comparison.getLeft()))
          .build();
    }

    return comparisonDTO;
  }

  private List<ComparisonDetailDTO> getComparisonDetails(String right, String left) {
    LOGGER.debug("Getting comparison details");
    List<ComparisonDetailDTO> comparisonDetailDTOS = new ArrayList<>();
    boolean difference = false;
    int differenceLength = 0;
    for (int i = 0; i < right.length(); i++) {
      if (right.charAt(i) != left.charAt(i)) {
        difference = true;
        differenceLength++;
      } else if (difference) {
        comparisonDetailDTOS.add(ComparisonDetailDTO.builder()
            .initialPosition(i - differenceLength)
            .finalPosition(i)
            .differenceLength(differenceLength)
            .build());
        difference = false;
        differenceLength = 0;
      }
    }
    return comparisonDetailDTOS;
  }
}
