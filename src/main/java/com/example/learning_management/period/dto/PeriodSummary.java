package com.example.learning_management.period.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.learning_management.period.Period;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PeriodSummary {
    private UUID id;
    private String name;
    private LocalDateTime timeBegin;
    private LocalDateTime timeEnd;

    public PeriodSummary(Period period){
        this.id = period.getId();
        this.name = period.getName();
        this.timeBegin = period.getTimeBegin();
        this.timeEnd = period.getTimeEnd();
    }
    public static PeriodSummary from(Period period){
        return PeriodSummary.builder()
                        .id(period.getId())
                        .name(period.getName())
                        .timeBegin(period.getTimeBegin())
                        .timeEnd(period.getTimeEnd())
                        .build();
    }
}
