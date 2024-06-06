package coursemaker.coursemaker.jwt;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class RefreshToken {

    @Id
    private String id;
    private String refreshToken;

//TODO :redis로 구현(고도화)
//    @Indexed
    private String accessToken;
//    @TimeToLive
    private Integer expiration;
    private Boolean isBlackListed;

    public void setBlackList(){
        this.isBlackListed = true;
    }
}
