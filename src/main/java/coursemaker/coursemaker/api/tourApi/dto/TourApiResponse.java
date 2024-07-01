package coursemaker.coursemaker.api.tourApi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class TourApiResponse {

    private Response response;

    @Data
    public static class Response {
        private Header header;
        private Body body;
    }

    @Data
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Data
    public static class Body {
        private Items items;
        private int numOfRows;
        private int pageNo;
        private int totalCount;
    }

    @Data
    public static class Items {
        private List<Item> item;
    }

    @Data
    public static class Item {

        @Schema(description = "이름", example = "SEA LIFE 부산아쿠아리움")
        private String title;
        @Schema(description = "콘텐츠 ID", example = "229912")
        private long contentid;
        @Schema(description = "전화번호")
        private String tel;

        @Schema(description = "주소", example = "부산광역시 해운대구 해운대해변로 266")
        private String addr1;
        @Schema(description = "상세주소", example = "(중동)")
        private String addr2;
        //        @Schema(description = "지역코드", example = "6")
//        private int areacode;
        @Schema(description = "sigungucode", example = "16")
        private int sigungucode;

        @Schema(description = "대표이미지(원본)", example = "http://tong.visitkorea.or.kr/cms/resource/09/3020609_image2_1.jpg")
        private String firstimage;
        @Schema(description = "대표이미지(썸네일)", example = "http://tong.visitkorea.or.kr/cms/resource/09/3020609_image3_1.jpg")
        private String firstimage2;

        @Schema(description = "X좌표, Longitude, 경도", example = "129.1610328343")
        private double mapx;
        @Schema(description = "Y좌표, Latitude, 위도", example = "35.1591965750")
        private double mapy;
        @Schema(description = "우편번호", example = "48100")
        private String zipcode;

//        @Schema(description = "교과서속여행지 여부", example = "0")
//        private String booktour;

        @Schema(description = "등록일", example = "20070716090000")
        private String createdtime;
        @Schema(description = "수정일", example = "20240509134635")
        private String modifiedtime;

        @Schema(description = "대분류", example = "A02")
        private String cat1;
        @Schema(description = "중분류", example = "A0202")
        private String cat2;
        @Schema(description = "소분류", example = "A02020600")
        private String cat3;

        @Schema(description = "콘텐츠타입 ID", example = "14")
        private int contenttypeid;

//        @Schema(description = "저작권 유형 Type1:제1유형(출처표시-권장), Type3:제3유형(제1유형 + 변경금지)", example = "Type3")
//        private String cpyrhtDivCd;

//        @Schema(description = "Map Level", example = "6")
//        private int mlevel;
    }

    // 호진님이 작업하셨었던 코드
//    private Response response;
//
//    @Getter
//    @Setter
//    public static class Response {
//        private Header header;
//        private Body body;
//
//        @Getter
//        @Setter
//        public static class Header {
//            private String resultCode;
//            private String resultMsg;
//        }
//
//        @Getter
//        @Setter
//        public static class Body {
//            private Items items;
//
//            @Getter
//            @Setter
//            public static class Items {
//                private List<AreaTour> item;
//            }
//        }
//    }
//
//    @Getter
//    @Setter
//    public static class AreaTour {
//        private String addr1;
//        private String addr2;
//        private int areacode;
//        private boolean booktour;
//        private String cat1;
//        private String cat2;
//        private String cat3;
//        private long contentid;
//        private int contenttypeid;
//        private String createdtime;
//        private String firstimage;
//        private String firstimage2;
//        private String cpyrhtDivCd;
//        private double mapx;
//        private double mapy;
//        private int mlevel;
//        private String modifiedtime;
//        private int sigungucode;
//        private String tel;
//        private String title;
//        private String zipcode;
//    }
}

