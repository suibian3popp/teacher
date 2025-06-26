-- 用户与权限模块
CREATE TABLE `user` (
  `user_id` INT PRIMARY KEY AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL UNIQUE,
  `real_name` VARCHAR(50) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `phone` VARCHAR(20),
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `last_login` DATETIME
) COMMENT '用户基本信息表';

CREATE TABLE 'login'(
	`user_id` INT PRIMARY KEY AUTO_INCREMENT,
	`password` VARCHAR(100) NOT NULL,
	`password_hash` VARCHAR(255) COMMENT 'BCrypt加密存储、可以为空，可以不做，做了就改一下',
	FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`) ON DELETE CASCADE
)COMMENT '用户登录表';

CREATE TABLE `role` (
  `role_id` INT PRIMARY KEY AUTO_INCREMENT,
  `role_name` VARCHAR(20) NOT NULL UNIQUE COMMENT '系统管理员/院系管理员/教师/助教'
) COMMENT '角色表';

CREATE TABLE `permission` (
  `perm_id` INT PRIMARY KEY AUTO_INCREMENT,
  `perm_module` VARCHAR(30) NOT NULL COMMENT '课程管理/资源管理/...',
  `perm_action` VARCHAR(20) NOT NULL COMMENT 'create/view/edit/delete/approve'
) COMMENT '权限明细表';

CREATE TABLE `role_permission` (
  `role_id` INT,
  `perm_id` INT,
  PRIMARY KEY (`role_id`, `perm_id`),
  FOREIGN KEY (`role_id`) REFERENCES `role`(`role_id`),
  FOREIGN KEY (`perm_id`) REFERENCES `permission`(`perm_id`)
) COMMENT '角色权限关联表';

CREATE TABLE `user_role` (
  `user_id` INT,
  `role_id` INT,
  `valid_from` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `valid_to` DATETIME COMMENT '临时权限有效期',
  PRIMARY KEY (`user_id`, `role_id`),
  FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`),
  FOREIGN KEY (`role_id`) REFERENCES `role`(`role_id`)
) COMMENT '用户角色分配表';

-- 课程管理模块
CREATE TABLE `course` (
  `course_id` INT PRIMARY KEY AUTO_INCREMENT,
  `course_name` VARCHAR(100) NOT NULL,
  `course_type` ENUM('live', 'recorded', 'mixed') NOT NULL COMMENT '直播/录播/混合',
  `teacher_id` INT NOT NULL,
  `cover_image` VARCHAR(255),
  `credit` DECIMAL(3,1) NOT NULL,
  `start_time` DATETIME,
  `end_time` DATETIME,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`teacher_id`) REFERENCES `user`(`user_id`)
) COMMENT '课程主表';

CREATE TABLE `chapter` (
  `chapter_id` INT PRIMARY KEY AUTO_INCREMENT,
  `course_id` INT NOT NULL,
  `parent_id` INT DEFAULT 0 COMMENT '0表示顶级章节',
  `chapter_name` VARCHAR(100) NOT NULL,
  `sort_order` INT NOT NULL DEFAULT 0,
  FOREIGN KEY (`course_id`) REFERENCES `course`(`course_id`)
) COMMENT '课程章节表';

CREATE TABLE `course_progress` (
  `student_id` INT NOT NULL,
  `chapter_id` INT NOT NULL,
  `progress` DECIMAL(5,2) DEFAULT 0.00 COMMENT '0-100%',
  `last_access` DATETIME,
  `completed` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`student_id`, `chapter_id`),
  FOREIGN KEY (`student_id`) REFERENCES `user`(`user_id`),
  FOREIGN KEY (`chapter_id`) REFERENCES `chapter`(`chapter_id`)
) COMMENT '学习进度表';

-- 教学资源模块
CREATE TABLE `resource` (
  `res_id` INT PRIMARY KEY AUTO_INCREMENT,
  `res_name` VARCHAR(200) NOT NULL,
  `res_type` ENUM('ppt', 'pdf', 'video', 'question', 'case') NOT NULL,
  `uploader_id` INT NOT NULL,
  `file_path` VARCHAR(255) NOT NULL,
  `file_size` BIGINT NOT NULL COMMENT '字节',
  `subject` VARCHAR(50) NOT NULL,
  `difficulty` ENUM('easy', 'medium', 'hard'),
  `version` INT DEFAULT 1,
  `prev_version` INT COMMENT '历史版本指针',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`uploader_id`) REFERENCES `user`(`user_id`)
) COMMENT '教学资源表';

CREATE TABLE `resource_sharing` (
  `res_id` INT NOT NULL,
  `course_id` INT NOT NULL,
  `permission` ENUM('view', 'edit') DEFAULT 'view',
  PRIMARY KEY (`res_id`, `course_id`),
  FOREIGN KEY (`res_id`) REFERENCES `resource`(`res_id`),
  FOREIGN KEY (`course_id`) REFERENCES `course`(`course_id`)
) COMMENT '资源共享表';

-- 在线授课模块
CREATE TABLE `live_session` (
  `session_id` INT PRIMARY KEY AUTO_INCREMENT,
  `course_id` INT NOT NULL,
  `start_time` DATETIME NOT NULL,
  `end_time` DATETIME,
  `status` ENUM('scheduled', 'live', 'ended') DEFAULT 'scheduled',
  `recording_path` VARCHAR(255),
  FOREIGN KEY (`course_id`) REFERENCES `course`(`course_id`)
) COMMENT '直播课程表';

CREATE TABLE `interaction` (
  `interaction_id` INT PRIMARY KEY AUTO_INCREMENT,
  `session_id` INT NOT NULL,
  `type` ENUM('quiz', 'poll', 'group') NOT NULL,
  `content` TEXT NOT NULL COMMENT 'JSON格式存储题目/选项',
  `start_time` DATETIME,
  `end_time` DATETIME,
  `result_data` TEXT COMMENT 'JSON格式存储结果',
  FOREIGN KEY (`session_id`) REFERENCES `live_session`(`session_id`)
) COMMENT '课堂互动表';

-- 作业考试模块
CREATE TABLE `assignment` (
  `assign_id` INT PRIMARY KEY AUTO_INCREMENT,
  `course_id` INT NOT NULL,
  `title` VARCHAR(200) NOT NULL,
  `description` TEXT,
  `assign_type` ENUM('homework', 'exam') NOT NULL,
  `start_time` DATETIME,
  `end_time` DATETIME NOT NULL,
  `time_limit` INT COMMENT '考试时长(分钟)',
  `total_score` DECIMAL(5,2) NOT NULL,
  `anti_cheat` TINYINT(1) DEFAULT 0 COMMENT '是否启用防作弊',
  FOREIGN KEY (`course_id`) REFERENCES `course`(`course_id`)
) COMMENT '作业/考试表';

CREATE TABLE `assignment_question` (
  `question_id` INT PRIMARY KEY AUTO_INCREMENT,
  `assign_id` INT NOT NULL,
  `content` TEXT NOT NULL,
  `question_type` ENUM('single', 'multiple', 'fill', 'essay') NOT NULL,
  `correct_answer` TEXT COMMENT '客观题答案',
  `score` DECIMAL(5,2) NOT NULL,
  `sort_order` INT NOT NULL,
  FOREIGN KEY (`assign_id`) REFERENCES `assignment`(`assign_id`)
) COMMENT '题目表';

CREATE TABLE `submission` (
  `submission_id` INT PRIMARY KEY AUTO_INCREMENT,
  `assign_id` INT NOT NULL,
  `student_id` INT NOT NULL,
  `submit_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `auto_score` DECIMAL(5,2) COMMENT '客观题得分',
  `manual_score` DECIMAL(5,2) COMMENT '主观题得分',
  `total_score` DECIMAL(5,2) COMMENT '总分',
  `status` ENUM('submitted', 'graded', 'late') NOT NULL,
  FOREIGN KEY (`assign_id`) REFERENCES `assignment`(`assign_id`),
  FOREIGN KEY (`student_id`) REFERENCES `user`(`user_id`)
) COMMENT '学生提交表';

-- 学情分析模块
CREATE TABLE `learning_analytics` (
  `analytics_id` INT PRIMARY KEY AUTO_INCREMENT,
  `student_id` INT NOT NULL,
  `course_id` INT NOT NULL,
  `login_duration` INT DEFAULT 0 COMMENT '分钟',
  `resource_downloads` INT DEFAULT 0,
  `interaction_rate` DECIMAL(5,2) DEFAULT 0.00 COMMENT '0-100%',
  `assignment_score` DECIMAL(5,2) DEFAULT 0.00,
  `exam_score` DECIMAL(5,2) DEFAULT 0.00,
  `last_update` DATETIME DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY (`student_id`, `course_id`),
  FOREIGN KEY (`student_id`) REFERENCES `user`(`user_id`),
  FOREIGN KEY (`course_id`) REFERENCES `course`(`course_id`)
) COMMENT '学情分析表';

-- 日志审计模块
CREATE TABLE `operation_log` (
  `log_id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `ip_address` VARCHAR(45) NOT NULL,
  `module` VARCHAR(30) NOT NULL,
  `action` VARCHAR(20) NOT NULL,
  `target_id` INT COMMENT '操作目标ID',
  `details` TEXT,
  `operation_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`)
) COMMENT '操作日志表';

CREATE TABLE `system_log` (
  `syslog_id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `log_level` ENUM('INFO', 'WARN', 'ERROR') NOT NULL,
  `component` VARCHAR(50) NOT NULL,
  `message` TEXT NOT NULL,
  `exception` TEXT,
  `log_time` DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT '系统日志表';