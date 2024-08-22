package coursemaker.coursemaker.api.busanApi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BusanApiResponse {

    @JsonProperty("response")
    private Response response;

    @Data
    public static class Response {
        @JsonProperty("header")
        private Header header;

        @JsonProperty("body")
        private Body body;
    }

    @Data
    public static class Header {
        @JsonProperty("resultCode")
        private String resultCode;

        @JsonProperty("resultMsg")
        private String resultMsg;
    }

    @Data
    public static class Body {
        @JsonProperty("items")
        private Items items = new Items();

        @JsonProperty("numOfRows")
        private int numOfRows;

        @JsonProperty("pageNo")
        private int pageNo;

        @JsonProperty("totalCount")
        private int totalCount;
    }

    @Data
    public static class Items {
        @JsonProperty("item")
        private List<Item> item = new ArrayList<>();
    }

    @Data
    public static class Item {
        @Schema(description = "갈맷길 구간", example = "1코스 2구간")
        @JsonProperty("gugan_nm")
        private String guganNm;

        @Schema(description = "갈맷길 지점 내용", example = "기장군청-죽성만-대변항-오랑대-해동용궁사-송정해수욕장-문탠로드")
        @JsonProperty("gm_course")
        private String gmCourse;

        @Schema(description = "시작지점 주소", example="부산광역시 기장군 기장읍 기장대로 560")
        @JsonProperty("start_addr")
        private String startAddr;

        @Schema(description = "갈맷길 추가 설명")
        @JsonProperty("gm_text")
        private String gmText;

        @Schema(description = "코스 번호", example = "1코스")
        @JsonProperty("course_nm")
        private String courseNm;

        @Schema(description = "시작지점명", example = "기장군청")
        @JsonProperty("start_pls")
        private String startPls;

        @Schema(description = "중간지점명", example = "해동용궁사")
        @JsonProperty("middle_pls")
        private String middlePls;

        @Schema(description = "대표이미지(원본)", example = "부산광역시 기장군 기장읍 기장해안로 216")
        @JsonProperty("middle_adr")
        private String middleAdr;

        @Schema(description = "종료지점명", example = "문탠로드")
        @JsonProperty("end_pls")
        private String endPls;

        @Schema(description = "종료지점주소", example = "부산광역시 해운대구 중동 산 115-3")
        @JsonProperty("end_addr")
        private String endAddr;

        @Schema(description = "거리", example = "20.1km")
        @JsonProperty("gm_range")
        private String gmRange;

        @Schema(description = "난이도", example = "중")
        @JsonProperty("gm_degree")
        private String gmDegree;

        @Schema(description = "순번", example = "2")
        @JsonProperty("seq")
        private int seq;
    }
}