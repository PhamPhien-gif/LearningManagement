package com.example.learning_management.enrollment;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.learning_management.config.ErrorCode;
import com.example.learning_management.enrollment.dto.OpenPeriodRequest;
import com.example.learning_management.enrollment.dto.PeriodSummary;
import com.example.learning_management.shared.AppException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
@RequiredArgsConstructor
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final PeriodRepository periodRepository;

    @Transactional
    public PeriodSummary openRegistrationPeriod(OpenPeriodRequest request) {
        // check valid time
        LocalDateTime timeBegin = request.getTimeBegin();
        LocalDateTime timeEnd = request.getTimeEnd();
        if (!timeBegin.isBefore(timeEnd)) {
            throw new AppException(ErrorCode.INVALID_PERIOD_TIME);
        }
        Period period = periodRepository.save(
                Period.builder()
                        .timeBegin(timeBegin)
                        .timeEnd(timeEnd)
                        .name(request.getName())
                        .build());
        return PeriodSummary.builder()
                .id(period.getId())
                .name(period.getName())
                .timeBegin(period.getTimeBegin())
                .timeEnd(period.getTimeEnd())
                .build();
    }


}
