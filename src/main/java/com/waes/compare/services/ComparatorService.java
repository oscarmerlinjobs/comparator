package com.waes.compare.services;

import com.waes.compare.dtos.ComparisonDTO;
import com.waes.compare.enums.Side;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface ComparatorService {

  void registerSide(@NotBlank String id, @NotNull Side side, @NotBlank String encodedContent);

  ComparisonDTO compareEntries(String id);
}
