package com.waes.compare.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ComparisonDetailDTO {
  private int initialPosition;
  private int finalPosition;
  private int differenceLength;
}
