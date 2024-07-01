package coursemaker.coursemaker.domain.member.entity;

public enum Role {
    USER("ROLE_USER"),// 일반 회원
    ADMIN("ROLE_ADMIN"),// 관리자
    PARTNER("ROLE_PARTNER"),// 파트너사
    BRONZE_TRAVELER("ROLE_BRONZE_TRAVELER"),// 일반 여행가
    SILVER_TRAVELER("ROLE_SILVER_TRAVELER"),// 실버 여행가
    GOLD_TRAVELER("ROLE_GOLD_TRAVELER");// 골드 여행가

    private String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
