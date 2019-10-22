package com.waes.compare.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ComparisonDetailResponse {

  @JsonProperty("started_at")
  private int initialPosition;

  @JsonProperty("ended_at")
  private int finalPosition;

  @JsonProperty("length")
  private int differenceLength;
}
