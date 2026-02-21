package com.example.learning_management.course;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.learning_management.config.ErrorCode;
import com.example.learning_management.course.dto.AllCourseStudentReponse;
import com.example.learning_management.course.dto.AllCoursesResponse;
import com.example.learning_management.course.dto.CourseDetailResponse;
import com.example.learning_management.course.dto.CourseSummary;
import com.example.learning_management.course.dto.CreateCourseRequest;
import com.example.learning_management.material.dto.MaterialSummary;
import com.example.learning_management.period.Period;
import com.example.learning_management.period.PeriodRepository;
import com.example.learning_management.shared.AppException;
import com.example.learning_management.shared.PageResponse;
import com.example.learning_management.user.Role;
import com.example.learning_management.user.User;
import com.example.learning_management.user.UserRepository;
import com.example.learning_management.user.dto.StudentSummary;
import lombok.RequiredArgsConstructor;

import static com.example.learning_management.course.CourseSpecification.hasInstructor;
import static com.example.learning_management.course.CourseSpecification.hasPeriod;
import static com.example.learning_management.course.CourseSpecification.hasStudent;
import static com.example.learning_management.course.CourseSpecification.hasSubject;
import static org.springframework.data.jpa.domain.Specification.where;
import static com.example.learning_management.user.specification.StudentSpecification.hasEnrolledCourse;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class) // rollback when catch exception, default readOnly
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final PeriodRepository periodRepository;

    @Transactional
    public CourseSummary createCourse(CreateCourseRequest request, User registrar) {
        final UUID instructorId = request.getInstructorId();
        final UUID subjectId = request.getSubjectId();
        final UUID periodId = request.getPeriodId();
        final LocalDateTime timeBegin = request.getTimeBegin();
        final LocalDateTime timeEnd = request.getTimeEnd();
        final Integer maxStudents = request.getMaxStudents();

        // check instructor
        boolean existsInstructor = userRepository.existsByIdAndRole(instructorId, Role.INSTRUCTOR);
        if (!existsInstructor) {
            throw new AppException(ErrorCode.INSTRUCTOR_NOT_FOUND);
        }

        // check subject
        boolean existsSubject = subjectRepository.existsById(subjectId);
        if (!existsSubject) {
            throw new AppException(ErrorCode.SUBJECT_NOT_FOUND);
        }

        // check period
        boolean existsPeriod = periodRepository.existsById(periodId);
        if (!existsPeriod) {
            throw new AppException(ErrorCode.PERIOD_NOT_FOUND);
        }

        // check valid time
        if (!timeBegin.isBefore(timeEnd)) {
            throw new AppException(ErrorCode.INVALID_COURSE_TIME);
        }

        User instructor = userRepository.getReferenceById(instructorId);
        Subject subject = subjectRepository.getReferenceById(subjectId);
        Period period = periodRepository.getReferenceById(periodId);

        var newCourse = Course.builder()
                .registrar(registrar)
                .subject(subject)
                .instructor(instructor)
                .period(period)
                .maxStudents(maxStudents)
                .timeBegin(timeBegin)
                .timeEnd(timeEnd)
                .build();

        // Insert into database
        Course successCourse = courseRepository.save(newCourse);

        return CourseSummary.from(successCourse);
    }

    public CourseDetailResponse getCourseDetail(UUID courseId) {
        // find course
        Course course = courseRepository.findWithInstructorAndSubjectAndMaterialById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        // prepare list material summaries
        List<MaterialSummary> materialSummaries = new ArrayList<>();
        course.getMaterials().forEach(material -> {
            var materialSummary = MaterialSummary.from(material);
            materialSummaries.add(materialSummary);
        });

        CourseSummary courseSummary = CourseSummary.from(course);
        return CourseDetailResponse.builder()
                .courseSummary(courseSummary)
                .materials(materialSummaries)
                .build();

    }

    public AllCoursesResponse getAllCourses(UUID periodId, UUID studentId, UUID instructorId, UUID subjectId,
            Pageable pageable) {
        // get all courses, using jpa specification
        Page<Course> courses = courseRepository.findAllWithSubjectAndInstructorBy(where(hasPeriod(periodId)
                .and(hasStudent(studentId))
                .and(hasInstructor(instructorId))
                .and(hasSubject(subjectId))), pageable);

        // build list course summaries
        List<CourseSummary> courseSummaries = new ArrayList<>();

        courses.forEach(course -> courseSummaries.add(CourseSummary.from(course)));

        var pageDetail = new PageResponse(courses);
        return AllCoursesResponse.builder()
                .courses(courseSummaries)
                .pageDetail(pageDetail)
                .build();
    }

    public AllCourseStudentReponse getAllCourseStudent(UUID courseId, Pageable pageable, User viewer) {
        // the viewer must be the instructor of this course
        boolean isInstructor = courseRepository.existsByIdAndInstructorId(courseId, viewer.getId());
        if (!isInstructor) {
            // if has no permission, assume the course not found
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);
        }

        Page<User> usersPage = userRepository.findAll(hasEnrolledCourse(courseId), pageable);

        // prepare students
        List<StudentSummary> students = new ArrayList<>();
        usersPage.get().forEach(student -> {
            var studentSummary = StudentSummary.builder()
                    .id(student.getId())
                    .name(student.getName())
                    .email(student.getEmail())
                    .role(student.getRole().name())
                    .build();
            students.add(studentSummary);
        });
        var pageDetail = new PageResponse(usersPage);
        return AllCourseStudentReponse.builder()
                .students(students)
                .pageDetail(pageDetail)
                .build();

    }

}
