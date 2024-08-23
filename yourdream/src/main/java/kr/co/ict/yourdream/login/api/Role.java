package kr.co.ict.yourdream.login.api;

public enum Role {
    ADMIN("A"),
    CONSULTANT("C");

    private final String code;

    Role(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Role fromCode(String code) {
        for (Role role : Role.values()) {
            if (role.code.equals(code)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role code: " + code);
    }
}
