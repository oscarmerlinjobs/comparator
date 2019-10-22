package com.waes.compare.adapters;

import static org.junit.Assert.*;

import com.waes.compare.controllers.dtos.ComparisonResponse;
import com.waes.compare.dtos.ComparisonDTO;
import com.waes.compare.dtos.ComparisonDetailDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.junit.Assert;
import org.junit.Test;

public class ComparisonAdapterTest {

  @Test
  public void buildComparisonResponse_NullDetails() {

    ComparisonDTO comparisonDTO = ComparisonDTO.builder().comparisonResult("Result").build();

    ComparisonResponse response = ComparisonAdapter.buildComparisonResponse(comparisonDTO);

    Assert.assertTrue("Result".equals(response.getComparisonResult()));
    Assert.assertTrue(Objects.isNull(response.getDetails()));

  }

  @Test
  public void buildComparisonResponse_WithDetails() {
    List<ComparisonDetailDTO> details =  new ArrayList<>(2);
    details.add(ComparisonDetailDTO.builder().initialPosition(0).finalPosition(2)
        .differenceLength(2).build());
    details.add(ComparisonDetailDTO.builder().initialPosition(5).finalPosition(6)
        .differenceLength(1).build());
    ComparisonDTO comparisonDTO = ComparisonDTO.builder().comparisonResult("Result")
        .comparisonDetails(details).build();

    ComparisonResponse response = ComparisonAdapter.buildComparisonResponse(comparisonDTO);

    Assert.assertTrue("Result".equals(response.getComparisonResult()));
    Assert.assertTrue(Objects.nonNull(response.getDetails()));
    Assert.assertTrue(response.getDetails().size() == 2);

  }
}