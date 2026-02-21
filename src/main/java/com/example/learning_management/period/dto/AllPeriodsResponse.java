package com.example.learning_management.period.dto;

import java.util.List;
import com.example.learning_management.shared.PageResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class AllPeriodsResponse{
    PageResponse pageDetail;
    List<PeriodSummary> periods;
}
