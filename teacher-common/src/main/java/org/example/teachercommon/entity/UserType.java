package org.example.teachercommon.entity;

public enum UserType {
    TEACHER("teacher"),
    TA("ta"),
    DEPARTMENT_ADMIN("department_admin");

    private final String value;

    UserType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    // 可选：从数据库值转换到枚举
    public static UserType fromValue(String value) {
        for (UserType type : UserType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的用户类型: " + value);
    }
}