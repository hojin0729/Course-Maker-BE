package coursemaker.coursemaker.domain.member.entity;

public enum Role {
    USER("ROLE_USER"),// 일반 회원
    ADMIN("ROLE_ADMIN"),// 관리자
    PARTNER("ROLE_PARTNER"),// 파트너사
    BEGINNER_TRAVELER("ROLE_BEGINNER_TRAVELER"),// 일반 여행가
    INTERMEDIATE_TRAVELER("ROLE_INTERMEDIATE_TRAVELER"),// 중급 여행가
    PRO_TRAVELER("ROLE_PRO_TRAVELER");// 프로 여행가

    private String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
