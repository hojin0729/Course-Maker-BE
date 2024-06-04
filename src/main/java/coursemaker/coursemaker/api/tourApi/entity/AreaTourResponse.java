package coursemaker.coursemaker.api.tourApi.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AreaTourResponse {
    private Response response;

    @Getter
    @Setter
    public static class Response {
        private Header header;
        private Body body;

        @Getter
        @Setter
        public static class Header {
            private String resultCode;
            private String resultMsg;
        }

        @Getter
        @Setter
        public static class Body {
            private Items items;

            @Getter
            @Setter
            public static class Items {
                private List<AreaTour> item;
            }
        }
    }

    @Getter
    @Setter
    public static class AreaTour {
        private String addr1;
        private String addr2;
        private int areacode;
        private boolean booktour;
        private String cat1;
        private String cat2;
        private String cat3;
        private long contentid;
        private int contenttypeid;
        private String createdtime;
        private String firstimage;
        private String firstimage2;
        private String cpyrhtDivCd;
        private double mapx;
        private double mapy;
        private int mlevel;
        private String modifiedtime;
        private int sigungucode;
        private String tel;
        private String title;
        private String zipcode;
    }
}
