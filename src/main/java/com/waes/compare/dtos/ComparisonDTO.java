package com.waes.compare.dtos;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ComparisonDTO {
  private String comparisonResult;
  private List<ComparisonDetailDTO> comparisonDetails;
}
