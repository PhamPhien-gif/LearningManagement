package com.example.learning_management.period;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.learning_management.config.ErrorCode;
import com.example.learning_management.course.dto.CourseSummary;
import com.example.learning_management.period.dto.AllPeriodsResponse;
import com.example.learning_management.period.dto.PeriodDetail;
import com.example.learning_management.period.dto.PeriodSummary;
import com.example.learning_management.shared.AppException;
import com.example.learning_management.shared.PageResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class PeriodService {
    final PeriodRepository periodRepository;

    public AllPeriodsResponse getAllPeriods(Pageable pageable) {
        Page<Period> periodsPage = periodRepository.findAll(pageable);

        var pageDetail = new PageResponse(periodsPage);
        List<PeriodSummary> periods = new ArrayList<>();
        periodsPage.forEach(period -> {
            periods.add(PeriodSummary.from(period));
        });

        return AllPeriodsResponse.builder()
                .pageDetail(pageDetail)
                .periods(periods)
                .build();
    }

    public PeriodDetail getPeriodDetail(UUID periodId){
        Period period = periodRepository.findWithCoursesById(periodId)
                            .orElseThrow(() -> new AppException(ErrorCode.PERIOD_NOT_FOUND));

        List<CourseSummary> courseSummaries = new ArrayList<>();
        period.getCourses().forEach( course ->
            courseSummaries.add(CourseSummary.from(course))
        );
        var periodSummary = PeriodSummary.from(period);
        
        return PeriodDetail.builder()
                        .courses(courseSummaries)
                        .periodSummary(periodSummary)
                        .build();
    }
}
