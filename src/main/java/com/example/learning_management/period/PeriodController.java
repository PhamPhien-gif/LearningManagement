package com.example.learning_management.period;

import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.learning_management.period.dto.AllPeriodsResponse;
import com.example.learning_management.period.dto.PeriodDetail;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/period")
public class PeriodController {
    final PeriodService periodService;

    @GetMapping("/getAll")
    public ResponseEntity<AllPeriodsResponse> getAllPeriods(@RequestParam(required = false, defaultValue = "1") int page){
        Pageable pageable = PageRequest.of(page-1, 10, Sort.by("timeBegin").descending());
        return ResponseEntity.ok(periodService.getAllPeriods(pageable));
    }

    @GetMapping("/{periodId}")
    public ResponseEntity<PeriodDetail> getPeriodDetail(@PathVariable UUID periodId){
        return ResponseEntity.ok(periodService.getPeriodDetail(periodId));
    }
}
