-- Kích hoạt extension cho UUID nếu chưa có
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Định nghĩa vai trò người dùng
CREATE TYPE user_role AS ENUM ('STUDENT', 'INSTRUCTOR', 'REGISTRAR', 'ADMIN');

-- Định nghĩa trạng thái nộp bài
CREATE TYPE submission_status AS ENUM ('SUBMITTED', 'LATE', 'GRADED');

-- 1. Bảng Người dùng
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    email TEXT UNIQUE NOT NULL,
    name TEXT NOT NULL,
    password TEXT NOT NULL,
    role user_role NOT NULL DEFAULT 'STUDENT',
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- 2. Bảng Môn học
CREATE TABLE subjects (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    name TEXT NOT NULL,
    code TEXT UNIQUE NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 3. Bảng Môn học tiên quyết (Nhiều - Nhiều)
CREATE TABLE subject_prerequisites (
    subject_id UUID REFERENCES subjects (id) ON DELETE CASCADE,
    prerequisite_subject_id UUID REFERENCES subjects (id) ON DELETE CASCADE,
    PRIMARY KEY (
        subject_id,
        prerequisite_subject_id
    )
);

-- 4. Bảng Tài liệu
CREATE TABLE materials (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    title TEXT NOT NULL,
    file_path TEXT NOT NULL,
    file_type TEXT,
    file_size INTEGER, -- đơn vị bytes
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    deleted_at TIMESTAMPTZ
);

-- 5. Bảng Đợt đăng ký học phần
CREATE TABLE enrollment_periods (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    name TEXT, -- Ví dụ: Học kỳ 1 - 2024
    time_begin TIMESTAMPTZ NOT NULL,
    time_end TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    CONSTRAINT valid_time_range CHECK (time_end > time_begin)
);

-- 6. Bảng Khóa học (Lớp học cụ thể của một môn)
CREATE TABLE courses (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    subject_id UUID REFERENCES subjects (id) ON DELETE CASCADE,
    instructor_id UUID REFERENCES users (id),
    registrar_id UUID REFERENCES users (id),
    period_id UUID REFERENCES enrollment_periods (id),
    max_students INTEGER DEFAULT 50,
    time_begin TIMESTAMPTZ,
    time_end TIMESTAMPTZ,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- 7. Bảng liên kết Khóa học và Tài liệu
CREATE TABLE course_materials (
    course_id UUID REFERENCES courses (id) ON DELETE CASCADE,
    material_id UUID REFERENCES materials (id) ON DELETE CASCADE,
    is_preview BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (course_id, material_id)
);

-- 9. Bảng Sinh viên đăng ký học
CREATE TABLE enrollments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    student_id UUID REFERENCES users (id) ON DELETE CASCADE,
    enrollment_periods_id UUID REFERENCES enrollment_periods (id),
    course_id UUID REFERENCES courses (id) ON DELETE CASCADE,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    deleted_at TIMESTAMPTZ
);

-- 10. Bảng Bài kiểm tra
CREATE TABLE exams (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    course_id UUID REFERENCES courses (id) ON DELETE CASCADE,
    name TEXT NOT NULL,
    weight DECIMAL(5, 2) NOT NULL, -- Ví dụ: 0.30 cho 30%
    time_begin TIMESTAMPTZ,
    time_end TIMESTAMPTZ,
    CONSTRAINT valid_weight CHECK (
        weight >= 0
        AND weight <= 1
    )
);

-- 11. Bảng Nộp bài kiểm tra
CREATE TABLE exam_submissions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    exam_id UUID REFERENCES exams (id) ON DELETE CASCADE,
    student_id UUID REFERENCES users (id) ON DELETE CASCADE,
    material_submit UUID REFERENCES materials (id), -- Khóa ngoại tới material
    submitted_at TIMESTAMPTZ DEFAULT NOW(),
    status submission_status DEFAULT 'SUBMITTED',
    score DECIMAL(5, 2),
    CONSTRAINT valid_score CHECK (
        score >= 0
        AND score <= 10
    )
);

-- 12. Bảng Tổng kết điểm khóa học
CREATE TABLE grade_course (
    user_id UUID REFERENCES users (id) ON DELETE CASCADE,
    course_id UUID REFERENCES courses (id) ON DELETE CASCADE,
    total_grade DECIMAL(5, 2),
    PRIMARY KEY (user_id, course_id)
);

ALTER TABLE materials
ADD is_preview BOOLEAN DEFAULT FALSE,
ADD course_id UUID REFERENCES courses (id) on DELETE CASCADE