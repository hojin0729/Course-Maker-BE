package coursemaker.coursemaker.api.tourApi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tourApi")
public class TourApi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", columnDefinition = "TEXT")
    private String title;

    @Column(name = "contentid")
    private long contentid;

    @Column(name = "tel", columnDefinition = "TEXT")
    private String tel;

    @Column(name = "addr1", columnDefinition = "TEXT")
    private String addr1;

    @Column(name = "addr2", columnDefinition = "TEXT")
    private String addr2;

    @Column(name = "sigungucode")
    private int sigungucode;

    @Column(name = "firstimage", columnDefinition = "TEXT")
    private String firstimage;

    @Column(name = "firstimage2", columnDefinition = "TEXT")
    private String firstimage2;

    @Column(name = "mapx", precision = 15, scale = 12)
    private BigDecimal mapx;

    @Column(name = "mapy", precision = 15, scale = 12)
    private BigDecimal mapy;

    @Column(name = "zipcode", length = 20)
    private String zipcode;

    @Column(name = "createdtime", length = 50)
    private String createdtime;

    @Column(name = "modifiedtime", length = 50)
    private String modifiedtime;

    @Column(name = "cat1", length = 10)
    private String cat1;

    @Column(name = "cat2", length = 10)
    private String cat2;

    @Column(name = "cat3", length = 10)
    private String cat3;

    @Column(name = "contenttypeid")
    private int contenttypeid;

    @Column(name = "disabled")
    private Long disabled;

    @Column(name = "homepage", columnDefinition = "TEXT")
    private String homepage;

    @Column(name = "overview", columnDefinition = "TEXT")
    private String overview;

//    // 필드 추가: contentTypeId = 12 (관광지)
//    @Column(name = "accomcount")
//    private String accomcount;
//
//    @Column(name = "chkbabycarriage", length = 50)
//    private String chkbabycarriage;
//
//    @Column(name = "chkcreditcard", length = 50)
//    private String chkcreditcard;
//
//    @Column(name = "chkpet", length = 50)
//    private String chkpet;
//
//    @Column(name = "expagerange", columnDefinition = "TEXT")
//    private String expagerange;
//
//    @Column(name = "expguide", columnDefinition = "TEXT")
//    private String expguide;
//
//    @Column(name = "heritage1")
//    private Integer heritage1;
//
//    @Column(name = "heritage2")
//    private Integer heritage2;
//
//    @Column(name = "heritage3")
//    private Integer heritage3;
//
//    @Column(name = "infocenter", columnDefinition = "TEXT")
//    private String infocenter;
//
//    @Column(name = "opendate", columnDefinition = "TEXT")
//    private String opendate;
//
//    @Column(name = "parking", columnDefinition = "TEXT")
//    private String parking;
//
//    @Column(name = "restdate", columnDefinition = "TEXT")
//    private String restdate;
//
//    @Column(name = "useseason", columnDefinition = "TEXT")
//    private String useseason;
//
//    @Column(name = "usetime", columnDefinition = "TEXT")
//    private String usetime;
//
//    // 필드 추가: contentTypeId = 14 (문화시설)
//    @Column(name = "accomcountculture", columnDefinition = "TEXT")
//    private String accomcountculture;
//
//    @Column(name = "chkbabycarriageculture", length = 50)
//    private String chkbabycarriageculture;
//
//    @Column(name = "chkcreditcardculture", length = 50)
//    private String chkcreditcardculture;
//
//    @Column(name = "chkpetculture", length = 50)
//    private String chkpetculture;
//
//    @Column(name = "discountinfo", columnDefinition = "TEXT")
//    private String discountinfo;
//
//    @Column(name = "infocenterculture", columnDefinition = "TEXT")
//    private String infocenterculture;
//
//    @Column(name = "parkingculture", columnDefinition = "TEXT")
//    private String parkingculture;
//
//    @Column(name = "parkingfee", columnDefinition = "TEXT")
//    private String parkingfee;
//
//    @Column(name = "restdateculture", columnDefinition = "TEXT")
//    private String restdateculture;
//
//    @Column(name = "usefee", columnDefinition = "TEXT")
//    private String usefee;
//
//    @Column(name = "usetimeculture", columnDefinition = "TEXT")
//    private String usetimeculture;
//
//    @Column(name = "scale", columnDefinition = "TEXT")
//    private String scale;
//
//    @Column(name = "spendtime", columnDefinition = "TEXT")
//    private String spendtime;
//
//    // 필드 추가: contentTypeId = 15 (행사/공연/축제)
//    @Column(name = "agelimit", columnDefinition = "TEXT")
//    private String agelimit;
//
//    @Column(name = "bookingplace", columnDefinition = "TEXT")
//    private String bookingplace;
//
//    @Column(name = "discountinfofestival", columnDefinition = "TEXT")
//    private String discountinfofestival;
//
//    @Column(name = "eventenddate", length = 50)
//    private String eventenddate;
//
//    @Column(name = "eventhomepage", columnDefinition = "TEXT")
//    private String eventhomepage;
//
//    @Column(name = "eventplace", columnDefinition = "TEXT")
//    private String eventplace;
//
//    @Column(name = "eventstartdate", length = 50)
//    private String eventstartdate;
//
//    @Column(name = "festivalgrade", length = 50)
//    private String festivalgrade;
//
//    @Column(name = "placeinfo", columnDefinition = "TEXT")
//    private String placeinfo;
//
//    @Column(name = "playtime", columnDefinition = "TEXT")
//    private String playtime;
//
//    @Column(name = "program", columnDefinition = "TEXT")
//    private String program;
//
//    @Column(name = "spendtimefestival", columnDefinition = "TEXT")
//    private String spendtimefestival;
//
//    @Column(name = "sponsor1", columnDefinition = "TEXT")
//    private String sponsor1;
//
//    @Column(name = "sponsor1tel", columnDefinition = "TEXT")
//    private String sponsor1tel;
//
//    @Column(name = "sponsor2", columnDefinition = "TEXT")
//    private String sponsor2;
//
//    @Column(name = "sponsor2tel", columnDefinition = "TEXT")
//    private String sponsor2tel;
//
//    @Column(name = "subevent", columnDefinition = "TEXT")
//    private String subevent;
//
//    @Column(name = "usetimefestival", columnDefinition = "TEXT")
//    private String usetimefestival;
//
//    // 필드 추가: contentTypeId = 25 (여행코스)
//    @Column(name = "distance", columnDefinition = "TEXT")
//    private String distance;
//
//    @Column(name = "infocentertourcourse", columnDefinition = "TEXT")
//    private String infocentertourcourse;
//
//    @Column(name = "schedule", columnDefinition = "TEXT")
//    private String schedule;
//
//    @Column(name = "taketime", columnDefinition = "TEXT")
//    private String taketime;
//
//    @Column(name = "theme", columnDefinition = "TEXT")
//    private String theme;
//
//    // 필드 추가: contentTypeId = 28 (레포츠)
//    @Column(name = "accomcountleports", columnDefinition = "TEXT")
//    private String accomcountleports;
//
//    @Column(name = "chkbabycarriageleports", length = 50)
//    private String chkbabycarriageleports;
//
//    @Column(name = "chkcreditcardleports", length = 50)
//    private String chkcreditcardleports;
//
//    @Column(name = "chkpetleports", length = 50)
//    private String chkpetleports;
//
//    @Column(name = "expagerangeleports", columnDefinition = "TEXT")
//    private String expagerangeleports;
//
//    @Column(name = "infocenterleports", columnDefinition = "TEXT")
//    private String infocenterleports;
//
//    @Column(name = "openperiod", columnDefinition = "TEXT")
//    private String openperiod;
//
//    @Column(name = "parkingfeeleports", columnDefinition = "TEXT")
//    private String parkingfeeleports;
//
//    @Column(name = "parkingleports", columnDefinition = "TEXT")
//    private String parkingleports;
//
//    @Column(name = "reservation", columnDefinition = "TEXT")
//    private String reservation;
//
//    @Column(name = "restdateleports", columnDefinition = "TEXT")
//    private String restdateleports;
//
//    @Column(name = "scaleleports", columnDefinition = "TEXT")
//    private String scaleleports;
//
//    @Column(name = "usefeeleports", columnDefinition = "TEXT")
//    private String usefeeleports;
//
//    @Column(name = "usetimeleports", columnDefinition = "TEXT")
//    private String usetimeleports;
//
//    // 필드 추가: contentTypeId = 32 (숙박)
//    @Column(name = "accomcountlodging", columnDefinition = "TEXT")
//    private String accomcountlodging;
//
//    @Column(name = "benikia", length = 50)
//    private String benikia;
//
//    @Column(name = "checkintime", length = 50)
//    private String checkintime;
//
//    @Column(name = "checkouttime", length = 50)
//    private String checkouttime;
//
//    @Column(name = "chkcooking", length = 50)
//    private String chkcooking;
//
//    @Column(name = "foodplace", columnDefinition = "TEXT")
//    private String foodplace;
//
//    @Column(name = "goodstay", length = 50)
//    private String goodstay;
//
//    @Column(name = "hanok", length = 50)
//    private String hanok;
//
//    @Column(name = "infocenterlodging", columnDefinition = "TEXT")
//    private String infocenterlodging;
//
//    @Column(name = "parkinglodging", columnDefinition = "TEXT")
//    private String parkinglodging;
//
//    @Column(name = "pickup", length = 50)
//    private String pickup;
//
//    @Column(name = "roomcount", columnDefinition = "TEXT")
//    private String roomcount;
//
//    @Column(name = "reservationlodging", columnDefinition = "TEXT")
//    private String reservationlodging;
//
//    @Column(name = "reservationurl", columnDefinition = "TEXT")
//    private String reservationurl;
//
//    @Column(name = "roomtype", columnDefinition = "TEXT")
//    private String roomtype;
//
//    @Column(name = "scalelodging", columnDefinition = "TEXT")
//    private String scalelodging;
//
//    @Column(name = "subfacility", columnDefinition = "TEXT")
//    private String subfacility;
//
//    @Column(name = "barbecue", columnDefinition = "TEXT")
//    private String barbecue;
//
//    @Column(name = "beauty", columnDefinition = "TEXT")
//    private String beauty;
//
//    @Column(name = "beverage", columnDefinition = "TEXT")
//    private String beverage;
//
//    @Column(name = "bicycle", columnDefinition = "TEXT")
//    private String bicycle;
//
//    @Column(name = "campfire", length = 50)
//    private String campfire;
//
//    @Column(name = "fitness", length = 50)
//    private String fitness;
//
//    @Column(name = "karaoke", length = 50)
//    private String karaoke;
//
//    @Column(name = "publicbath", length = 50)
//    private String publicbath;
//
//    @Column(name = "publicpc", length = 50)
//    private String publicpc;
//
//    @Column(name = "sauna", length = 50)
//    private String sauna;
//
//    @Column(name = "seminar", length = 50)
//    private String seminar;
//
//    @Column(name = "sports", length = 50)
//    private String sports;
//
//    @Column(name = "refundregulation", columnDefinition = "TEXT")
//    private String refundregulation;
//
//    // 필드 추가: contentTypeId = 38 (쇼핑)
//    @Column(name = "chkbabycarriageshopping", length = 50)
//    private String chkbabycarriageshopping;
//
//    @Column(name = "chkcreditcardshopping", length = 50)
//    private String chkcreditcardshopping;
//
//    @Column(name = "chkpetshopping", length = 50)
//    private String chkpetshopping;
//
//    @Column(name = "culturecenter", columnDefinition = "TEXT")
//    private String culturecenter;
//
//    @Column(name = "fairday", length = 50)
//    private String fairday;
//
//    @Column(name = "infocentershopping", columnDefinition = "TEXT")
//    private String infocentershopping;
//
//    @Column(name = "opendateshopping", length = 50)
//    private String opendateshopping;
//
//    @Column(name = "opentime", columnDefinition = "TEXT")
//    private String opentime;
//
//    @Column(name = "parkingshopping", columnDefinition = "TEXT")
//    private String parkingshopping;
//
//    @Column(name = "restdateshopping", length = 50)
//    private String restdateshopping;
//
//    @Column(name = "restroom", columnDefinition = "TEXT")
//    private String restroom;
//
//    @Column(name = "saleitem", columnDefinition = "TEXT")
//    private String saleitem;
//
//    @Column(name = "saleitemcost", columnDefinition = "TEXT")
//    private String saleitemcost;
//
//    @Column(name = "scaleshopping", columnDefinition = "TEXT")
//    private String scaleshopping;
//
//    @Column(name = "shopguide", columnDefinition = "TEXT")
//    private String shopguide;
//
//    // 필드 추가: contentTypeId = 39 (음식점)
//    @Column(name = "chkcreditcardfood", length = 50)
//    private String chkcreditcardfood;
//
//    @Column(name = "discountinfofood", columnDefinition = "TEXT")
//    private String discountinfofood;
//
//    @Column(name = "firstmenu", columnDefinition = "TEXT")
//    private String firstmenu;
//
//    @Column(name = "infocenterfood", columnDefinition = "TEXT")
//    private String infocenterfood;
//
//    @Column(name = "kidsfacility", columnDefinition = "TEXT")
//    private String kidsfacility;
//
//    @Column(name = "opendatefood", length = 50)
//    private String opendatefood;
//
//    @Column(name = "opentimefood", columnDefinition = "TEXT")
//    private String opentimefood;
//
//    @Column(name = "packing", columnDefinition = "TEXT")
//    private String packing;
//
//    @Column(name = "parkingfood", columnDefinition = "TEXT")
//    private String parkingfood;
//
//    @Column(name = "reservationfood", columnDefinition = "TEXT")
//    private String reservationfood;
//
//    @Column(name = "restdatefood", length = 50)
//    private String restdatefood;
//
//    @Column(name = "scalefood", columnDefinition = "TEXT")
//    private String scalefood;
//
//    @Column(name = "seat", columnDefinition = "TEXT")
//    private String seat;
//
//    @Column(name = "smoking", length = 50)
//    private String smoking;
//
//    @Column(name = "treatmenu", columnDefinition = "TEXT")
//    private String treatmenu;
//
//    @Column(name = "lcnsno", columnDefinition = "TEXT")
//    private String lcnsno;

//    @OneToOne
//    private Tag tag;
}