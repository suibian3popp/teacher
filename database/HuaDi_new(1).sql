-- 智能化在线教学支持服务平台数据库脚本
-- 包含完整的表删除和创建语句

-- =============================================
-- 第一部分：按依赖关系逆序删除所有表（防止外键约束冲突）
-- =============================================

-- 先删除所有表（按依赖关系逆序删除）
DROP TABLE IF EXISTS `session_attendance`;
DROP TABLE IF EXISTS `live_session`;
DROP TABLE IF EXISTS `exam_grades`;
DROP TABLE IF EXISTS `exam_submissions`;
DROP TABLE IF EXISTS `assignment_grades`;
DROP TABLE IF EXISTS `assignment_submissions`;
DROP TABLE IF EXISTS `assignment_resources`;
DROP TABLE IF EXISTS `assignments`;
DROP TABLE IF EXISTS `exams`;
DROP TABLE IF EXISTS `student_class`;
DROP TABLE IF EXISTS `classes`;
DROP TABLE IF EXISTS `chapter_resources`;
DROP TABLE IF EXISTS `operation_logs`;
DROP TABLE IF EXISTS `live_sessions`;
DROP TABLE IF EXISTS `resource_shares`;
DROP TABLE IF EXISTS `chapters`;
DROP TABLE IF EXISTS `courses`;
DROP TABLE IF EXISTS `resources`;
DROP TABLE IF EXISTS `login_auth`;
DROP TABLE IF EXISTS `students`;
DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `departments`;

-- =============================================
-- 第二部分：按依赖关系顺序创建所有表
-- =============================================

-- 1. 院系表：存储学校所有院系的基本信息
CREATE TABLE departments (
    department_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '院系ID',
    name VARCHAR(100) NOT NULL COMMENT '院系名称',
    phone VARCHAR(20) COMMENT '联系电话',
    description TEXT COMMENT '院系简介',
    admin_id INT COMMENT '院系管理员ID（关联users表）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) COMMENT '院系信息表';

-- 2. 用户表（不存储密码）
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID（登录用）',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    phone VARCHAR(20) COMMENT '用户电话',
    type ENUM('teacher', 'ta', 'department_admin') NOT NULL COMMENT '用户类型',
    department_id INT COMMENT '所属院系ID',
    real_name VARCHAR(50) COMMENT '真实姓名',
    email VARCHAR(100) COMMENT '邮箱',
    FOREIGN KEY (department_id) REFERENCES departments(department_id)
) COMMENT '用户基本信息表';

-- 3. 登录认证表（单独存储密码）
CREATE TABLE login_auth (
    auth_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '认证ID',
    user_id INT NOT NULL UNIQUE COMMENT '关联用户ID',
		password VARCHAR(255) NOT NULL COMMENT '密码',
    password_hash VARCHAR(255) NOT NULL COMMENT '密码哈希',
    last_login TIMESTAMP COMMENT '最后登录时间',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
) COMMENT '用户登录认证表';

-- 4. 学生信息表（教师端仅需基本信息）
CREATE TABLE students (
    student_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '学生ID',
    student_no VARCHAR(50) NOT NULL UNIQUE COMMENT '学号',
    real_name VARCHAR(50) NOT NULL COMMENT '学生姓名',
    department_id INT COMMENT '所属院系',
    class_name VARCHAR(50) COMMENT '班级名称',
    FOREIGN KEY (department_id) REFERENCES departments(department_id)
) COMMENT '学生信息表';

-- 5. 资源表
CREATE TABLE resources (
    resource_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '资源ID',
    owner_id INT NOT NULL COMMENT '上传者ID',
    name VARCHAR(255) NOT NULL COMMENT '资源名称',
    type ENUM('file', 'video', 'audio', 'image', 'exam') NOT NULL,
    permission ENUM('public', 'department', 'private') NOT NULL DEFAULT 'department',
    bucket VARCHAR(100) NOT NULL COMMENT 'MinIO存储桶',
    object_key VARCHAR(255) NOT NULL COMMENT 'MinIO对象键',
    file_size BIGINT COMMENT '文件大小(字节)',
    upload_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
    FOREIGN KEY (owner_id) REFERENCES users(user_id)
) COMMENT '教学资源表';

-- 6. 课程表
CREATE TABLE courses (
    course_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '课程ID',
    name VARCHAR(100) NOT NULL COMMENT '课程名称',
    teacher_id INT NOT NULL COMMENT '主讲教师ID',
    department_id INT COMMENT '所属院系ID',
    semester VARCHAR(50) COMMENT '开设学期',
    description TEXT COMMENT '课程简介',
    course_type ENUM('live', 'recorded') NOT NULL DEFAULT 'recorded' COMMENT '没有混合课，只有直播和录播',
    cover_image_resource INT COMMENT '封面图资源ID',
    FOREIGN KEY (teacher_id) REFERENCES users(user_id),
    FOREIGN KEY (department_id) REFERENCES departments(department_id),
    FOREIGN KEY (cover_image_resource) REFERENCES resources(resource_id)
) COMMENT '课程信息表';

-- 7. 班级表
CREATE TABLE classes (
    class_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '班级ID',
    course_id INT NOT NULL COMMENT '关联课程ID',
    name VARCHAR(50) NOT NULL COMMENT '班级名称',
    FOREIGN KEY (course_id) REFERENCES courses(course_id)
) COMMENT '课程班级表';

-- 8. 学生选课关联表
CREATE TABLE student_class (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '关联ID',
    student_id INT NOT NULL COMMENT '学生ID',
    class_id INT NOT NULL COMMENT '班级ID',
    UNIQUE KEY (student_id, class_id),
    FOREIGN KEY (student_id) REFERENCES students(student_id),
    FOREIGN KEY (class_id) REFERENCES classes(class_id)
) COMMENT '学生选课关系表';

-- 9. 章节表
CREATE TABLE chapters (
    chapter_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '章节ID',
    course_id INT NOT NULL COMMENT '所属课程ID',
    title VARCHAR(100) NOT NULL COMMENT '章节标题',
    parent_id INT COMMENT '父章节ID',
    order_num INT NOT NULL DEFAULT 0 COMMENT '排序序号',
    FOREIGN KEY (course_id) REFERENCES courses(course_id),
    FOREIGN KEY (parent_id) REFERENCES chapters(chapter_id)
) COMMENT '课程章节表';

-- 10. 章节-资源关联表（多对多）
CREATE TABLE chapter_resources (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '关联ID',
    chapter_id INT NOT NULL COMMENT '章节ID',
    resource_id INT NOT NULL COMMENT '资源ID',
    display_order INT NOT NULL DEFAULT 0 COMMENT '显示顺序',
    UNIQUE KEY (chapter_id, resource_id),
    FOREIGN KEY (chapter_id) REFERENCES chapters(chapter_id) ON DELETE CASCADE,
    FOREIGN KEY (resource_id) REFERENCES resources(resource_id) ON DELETE CASCADE
) COMMENT '章节资源关联表';

-- 11. 作业表
CREATE TABLE assignments (
    assignment_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '作业ID',
    chapter_id INT NOT NULL COMMENT '所属章节ID',
    title VARCHAR(100) NOT NULL COMMENT '作业标题',
    description TEXT COMMENT '作业说明',
    deadline DATETIME COMMENT '截止时间',
    total_score DECIMAL(5,2) COMMENT '满分分值',
    resource_id INT COMMENT '作业附件资源ID',
    FOREIGN KEY (chapter_id) REFERENCES chapters(chapter_id),
    FOREIGN KEY (resource_id) REFERENCES resources(resource_id)
) COMMENT '作业信息表';

-- 12. 作业提交表
CREATE TABLE assignment_submissions (
    submission_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '提交ID',
    assignment_id INT NOT NULL COMMENT '作业ID',
    student_id INT NOT NULL COMMENT '学生ID',
    submit_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
    resource_id INT NOT NULL COMMENT '作业文件资源ID',
    FOREIGN KEY (assignment_id) REFERENCES assignments(assignment_id),
    FOREIGN KEY (student_id) REFERENCES students(student_id),
    FOREIGN KEY (resource_id) REFERENCES resources(resource_id)
) COMMENT '作业提交表';

-- 13. 作业批改表
CREATE TABLE assignment_grades (
    grade_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '批改ID',
    submission_id INT NOT NULL UNIQUE COMMENT '提交记录ID',
    grader_id INT NOT NULL COMMENT '批改人ID（教师/助教）',
    score DECIMAL(5,2) COMMENT '得分',
    feedback TEXT COMMENT '评语',
    grade_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '批改时间',
    FOREIGN KEY (submission_id) REFERENCES assignment_submissions(submission_id),
    FOREIGN KEY (grader_id) REFERENCES users(user_id)
) COMMENT '作业批改表';

-- 14. 试卷表
CREATE TABLE exams (
    exam_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '试卷ID',
    resource_id INT NOT NULL UNIQUE COMMENT '关联资源ID',
    total_score DECIMAL(5,2) NOT NULL COMMENT '试卷总分',
    time_limit INT COMMENT '考试时长(分钟)',
    start_time DATETIME COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    FOREIGN KEY (resource_id) REFERENCES resources(resource_id)
) COMMENT '试卷信息表';

-- 15. 考试提交表
CREATE TABLE exam_submissions (
    submission_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '提交ID',
    exam_id INT NOT NULL COMMENT '试卷ID',
    student_id INT NOT NULL COMMENT '学生ID',
    submit_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
    resource_id INT COMMENT '答卷文件资源ID',
    FOREIGN KEY (exam_id) REFERENCES exams(exam_id),
    FOREIGN KEY (student_id) REFERENCES students(student_id),
    FOREIGN KEY (resource_id) REFERENCES resources(resource_id)
) COMMENT '考试提交表';

-- 16. 考试批改表
CREATE TABLE exam_grades (
    grade_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '批改ID',
    submission_id INT NOT NULL UNIQUE COMMENT '提交记录ID',
    grader_id INT NOT NULL COMMENT '批改人ID',
    score DECIMAL(5,2) COMMENT '得分',
    feedback TEXT COMMENT '评语',
    grade_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '批改时间',
    FOREIGN KEY (submission_id) REFERENCES exam_submissions(submission_id),
    FOREIGN KEY (grader_id) REFERENCES users(user_id)
) COMMENT '考试批改表';

-- 17. 操作日志表
CREATE TABLE operation_logs (
    log_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    user_id INT NOT NULL COMMENT '操作人ID',
    action_type VARCHAR(50) NOT NULL COMMENT '操作类型',
    target_type VARCHAR(50) NOT NULL COMMENT '目标类型',
    target_id INT COMMENT '目标ID',
    ip_address VARCHAR(50) COMMENT '操作IP',
    details JSON COMMENT '操作详情',
    operation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    FOREIGN KEY (user_id) REFERENCES users(user_id)
) COMMENT '系统操作日志表';


-- 18.在线授课模块：直播课程表
CREATE TABLE `live_session` (
  `session_id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '直播会话ID',
  `session_title` VARCHAR(200) NOT NULL COMMENT '直播课标题',
  `teacher_id` INT NOT NULL COMMENT '创建直播的教师ID',
  `trtc_room_id` VARCHAR(64) COMMENT 'TRTC房间ID',
  `max_users` INT DEFAULT 100 COMMENT '最大参与人数',
  `course_id` INT NOT NULL COMMENT '关联课程ID',
  `start_time` DATETIME NOT NULL COMMENT '计划开始时间',
  `end_time` DATETIME COMMENT '实际结束时间',
  `status` ENUM('scheduled', 'live', 'ended', 'canceled') DEFAULT 'scheduled' COMMENT '直播状态',
  `recording_path` INT COMMENT '录播文件存储资源ID',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  FOREIGN KEY (`course_id`) REFERENCES `courses`(`course_id`),
  FOREIGN KEY (`teacher_id`) REFERENCES `users`(`user_id`),
  FOREIGN KEY (`recording_path`) REFERENCES `resources`(`resource_id`)
) COMMENT '直播课程表（由教师创建）';


-- 19.在线授课模块：学生参与记录表
CREATE TABLE `session_attendance` (
  `attendance_id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '参与记录ID',
  `session_id` INT NOT NULL COMMENT '关联的直播会话ID',
  `student_id` INT NOT NULL COMMENT '参与学生ID',
  `join_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
  `leave_time` DATETIME COMMENT '离开时间',
  `duration` INT DEFAULT 0 COMMENT '实际参与时长(秒)',
  `interaction_count` INT DEFAULT 0 COMMENT '互动次数（提问/答题等）',
  FOREIGN KEY (`session_id`) REFERENCES `live_session`(`session_id`) ON DELETE CASCADE,
  FOREIGN KEY (`student_id`) REFERENCES `students`(`student_id`),
  UNIQUE KEY (`session_id`, `student_id`) COMMENT '防止重复记录'
) COMMENT '学生参与记录表（仅记录学生）';

-- 修改资源表，添加难度字段并调整权限枚举值
ALTER TABLE resources 
ADD difficulty ENUM('beginner', 'intermediate', 'advanced') NOT NULL DEFAULT 'intermediate' COMMENT '资源难度';
ALTER TABLE resources 
MODIFY COLUMN type ENUM('courseware', 'case', 'assignment', 'other') NOT NULL;