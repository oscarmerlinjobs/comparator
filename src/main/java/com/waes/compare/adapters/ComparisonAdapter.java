package com.waes.compare.adapters;

import com.waes.compare.controllers.dtos.ComparisonDetailResponse;
import com.waes.compare.controllers.dtos.ComparisonResponse;
import com.waes.compare.dtos.ComparisonDTO;
import com.waes.compare.dtos.ComparisonDetailDTO;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ComparisonAdapter {

  public static ComparisonResponse buildComparisonResponse(ComparisonDTO comparisonDTO){
    return ComparisonResponse.builder()
        .comparisonResult(comparisonDTO.getComparisonResult())
        .details(buildDetails(comparisonDTO.getComparisonDetails())).build();

  }

  private static List<ComparisonDetailResponse> buildDetails(List<ComparisonDetailDTO> comparisonDetails) {
    if (Objects.nonNull(comparisonDetails)) {
      return comparisonDetails.stream()
          .map(detail -> new ComparisonDetailResponse(detail.getInitialPosition(),
              detail.getFinalPosition(), detail.getDifferenceLength())).collect(Collectors.toList());
    } else {
      return null;
    }
  }

}
