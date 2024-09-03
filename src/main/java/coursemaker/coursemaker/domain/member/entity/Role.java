package coursemaker.coursemaker.domain.member.entity;

public enum Role {
    ADMIN("ROLE_ADMIN", "관리자"),// 관리자
    PARTNER("ROLE_PARTNER", "파트너사"),// 파트너사
    BEGINNER_TRAVELER("ROLE_BEGINNER_TRAVELER", "초보 여행가"),// 일반 여행가
    INTERMEDIATE_TRAVELER("ROLE_INTERMEDIATE_TRAVELER", "중급 여행가"),// 중급 여행가
    PRO_TRAVELER("ROLE_PRO_TRAVELER", "프로 여행가");// 프로 여행가

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
                result = ADMIN.roleKor;
                break;
            case "PARTNER":
                result = PARTNER.roleKor;
                break;
            case "BEGINNER_TRAVELER":
                result = BEGINNER_TRAVELER.roleKor;
                break;
            case "INTERMEDIATE_TRAVELER":
                result = INTERMEDIATE_TRAVELER.roleKor;
                break;
            case "PRO_TRAVELER":
                result = PRO_TRAVELER.roleKor;
                break;
            }
        return result;
    }
}
