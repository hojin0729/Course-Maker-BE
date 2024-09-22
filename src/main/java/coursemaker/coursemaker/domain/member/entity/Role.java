package coursemaker.coursemaker.domain.member.entity;

public enum Role {
    ROLE_ADMIN("ROLE_ADMIN", "관리자"),// 관리자
    ROLE_PARTNER("ROLE_PARTNER", "파트너사"),// 파트너사
    ROLE_BEGINNER_TRAVELER("ROLE_BEGINNER_TRAVELER", "초보 여행가"),// 일반 여행가
    ROLE_INTERMEDIATE_TRAVELER("ROLE_INTERMEDIATE_TRAVELER", "중급 여행가"),// 중급 여행가
    ROLE_PRO_TRAVELER("ROLE_PRO_TRAVELER", "프로 여행가");// 프로 여행가

    private String role;
    private String roleKor;

    Role(String role, String roleKor) {
        this.role = role;
        this.roleKor = roleKor;
    }

    public String getRole() {
        return role;
    }

    public static String toKor(String role) {
        String result=null;

        switch (role) {
            case "ADMIN":
            case "ROLE_ADMIN":
                result = ROLE_ADMIN.roleKor;
                break;
            case "PARTNER":
            case "ROLE_PARTNER":
                result = ROLE_PARTNER.roleKor;
                break;
            case "BEGINNER_TRAVELER":
            case "ROLE_BEGINNER_TRAVELER":
                result = ROLE_BEGINNER_TRAVELER.roleKor;
                break;
            case "INTERMEDIATE_TRAVELER":
            case "ROLE_INTERMEDIATE_TRAVELER":
                result = ROLE_INTERMEDIATE_TRAVELER.roleKor;
                break;
            case "PRO_TRAVELER":
            case "ROLE_PRO_TRAVELER":
                result = ROLE_PRO_TRAVELER.roleKor;
                break;
            }
        return result;
    }
}
