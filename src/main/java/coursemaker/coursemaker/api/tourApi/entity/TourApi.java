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

    @Column(name = "title")
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

    // 필드 추가: contentTypeId = 12 (관광지)
    @Column(name = "accomcount")
    private String accomcount;

    @Column(name = "chkbabycarriage", length = 50)
    private String chkbabycarriage;

    @Column(name = "chkcreditcard", length = 50)
    private String chkcreditcard;

    @Column(name = "chkpet", length = 50)
    private String chkpet;

    @Column(name = "expagerange", columnDefinition = "TEXT")
    private String expagerange;

    @Column(name = "expguide", columnDefinition = "TEXT")
    private String expguide;

    @Column(name = "heritage1")
    private Integer heritage1;

    @Column(name = "heritage2")
    private Integer heritage2;

    @Column(name = "heritage3")
    private Integer heritage3;

    @Column(name = "infocenter", columnDefinition = "TEXT")
    private String infocenter;

    @Column(name = "opendate", columnDefinition = "TEXT")
    private String opendate;

    @Column(name = "parking", columnDefinition = "TEXT")
    private String parking;

    @Column(name = "restdate", columnDefinition = "TEXT")
    private String restdate;

    @Column(name = "useseason", columnDefinition = "TEXT")
    private String useseason;

    @Column(name = "usetime", columnDefinition = "TEXT")
    private String usetime;

    // 필드 추가: contentTypeId = 14 (문화시설)
    @Column(name = "accomcountculture", columnDefinition = "TEXT")
    private String accomcountculture;

    @Column(name = "chkbabycarriageculture", length = 50)
    private String chkbabycarriageculture;

    @Column(name = "chkcreditcardculture", length = 50)
    private String chkcreditcardculture;

    @Column(name = "chkpetculture", length = 50)
    private String chkpetculture;

    @Column(name = "discountinfo", columnDefinition = "TEXT")
    private String discountinfo;

    @Column(name = "infocenterculture", columnDefinition = "TEXT")
    private String infocenterculture;

    @Column(name = "parkingculture", columnDefinition = "TEXT")
    private String parkingculture;

    @Column(name = "parkingfee", columnDefinition = "TEXT")
    private String parkingfee;

    @Column(name = "restdateculture", columnDefinition = "TEXT")
    private String restdateculture;

    @Column(name = "usefee", columnDefinition = "TEXT")
    private String usefee;

    @Column(name = "usetimeculture", columnDefinition = "TEXT")
    private String usetimeculture;

    @Column(name = "scale", columnDefinition = "TEXT")
    private String scale;

    @Column(name = "spendtime", columnDefinition = "TEXT")
    private String spendtime;

    // 필드 추가: contentTypeId = 15 (행사/공연/축제)
    @Column(name = "agelimit", columnDefinition = "TEXT")
    private String agelimit;

    @Column(name = "bookingplace", columnDefinition = "TEXT")
    private String bookingplace;

    @Column(name = "discountinfofestival", columnDefinition = "TEXT")
    private String discountinfofestival;

    @Column(name = "eventenddate", length = 50)
    private String eventenddate;

    @Column(name = "eventhomepage", columnDefinition = "TEXT")
    private String eventhomepage;

    @Column(name = "eventplace", columnDefinition = "TEXT")
    private String eventplace;

    @Column(name = "eventstartdate", length = 50)
    private String eventstartdate;

    @Column(name = "festivalgrade", length = 50)
    private String festivalgrade;

    @Column(name = "placeinfo", columnDefinition = "TEXT")
    private String placeinfo;

    @Column(name = "playtime", columnDefinition = "TEXT")
    private String playtime;

    @Column(name = "program", columnDefinition = "TEXT")
    private String program;

    @Column(name = "spendtimefestival", columnDefinition = "TEXT")
    private String spendtimefestival;

    @Column(name = "sponsor1", columnDefinition = "TEXT")
    private String sponsor1;

    @Column(name = "sponsor1tel", columnDefinition = "TEXT")
    private String sponsor1tel;

    @Column(name = "sponsor2", columnDefinition = "TEXT")
    private String sponsor2;

    @Column(name = "sponsor2tel", columnDefinition = "TEXT")
    private String sponsor2tel;

    @Column(name = "subevent", columnDefinition = "TEXT")
    private String subevent;

    @Column(name = "usetimefestival", columnDefinition = "TEXT")
    private String usetimefestival;

    // 필드 추가: contentTypeId = 25 (여행코스)
    @Column(name = "distance", columnDefinition = "TEXT")
    private String distance;

    @Column(name = "infocentertourcourse", columnDefinition = "TEXT")
    private String infocentertourcourse;

    @Column(name = "schedule", columnDefinition = "TEXT")
    private String schedule;

    @Column(name = "taketime", columnDefinition = "TEXT")
    private String taketime;

    @Column(name = "theme", columnDefinition = "TEXT")
    private String theme;

    // 필드 추가: contentTypeId = 28 (레포츠)
    @Column(name = "accomcountleports", columnDefinition = "TEXT")
    private String accomcountleports;

    @Column(name = "chkbabycarriageleports", length = 50)
    private String chkbabycarriageleports;

    @Column(name = "chkcreditcardleports", length = 50)
    private String chkcreditcardleports;

    @Column(name = "chkpetleports", length = 50)
    private String chkpetleports;

    @Column(name = "expagerangeleports", columnDefinition = "TEXT")
    private String expagerangeleports;

    @Column(name = "infocenterleports", columnDefinition = "TEXT")
    private String infocenterleports;

    @Column(name = "openperiod", columnDefinition = "TEXT")
    private String openperiod;

    @Column(name = "parkingfeeleports", columnDefinition = "TEXT")
    private String parkingfeeleports;

    @Column(name = "parkingleports", columnDefinition = "TEXT")
    private String parkingleports;

    @Column(name = "reservation", columnDefinition = "TEXT")
    private String reservation;

    @Column(name = "restdateleports", columnDefinition = "TEXT")
    private String restdateleports;

    @Column(name = "scaleleports", columnDefinition = "TEXT")
    private String scaleleports;

    @Column(name = "usefeeleports", columnDefinition = "TEXT")
    private String usefeeleports;

    @Column(name = "usetimeleports", columnDefinition = "TEXT")
    private String usetimeleports;

    // 필드 추가: contentTypeId = 32 (숙박)
    @Column(name = "accomcountlodging", columnDefinition = "TEXT")
    private String accomcountlodging;

    @Column(name = "benikia", length = 50)
    private String benikia;

    @Column(name = "checkintime", length = 50)
    private String checkintime;

    @Column(name = "checkouttime", length = 50)
    private String checkouttime;

    @Column(name = "chkcooking", length = 50)
    private String chkcooking;

    @Column(name = "foodplace", columnDefinition = "TEXT")
    private String foodplace;

    @Column(name = "goodstay", length = 50)
    private String goodstay;

    @Column(name = "hanok", length = 50)
    private String hanok;

    @Column(name = "infocenterlodging", columnDefinition = "TEXT")
    private String infocenterlodging;

    @Column(name = "parkinglodging", columnDefinition = "TEXT")
    private String parkinglodging;

    @Column(name = "pickup", length = 50)
    private String pickup;

    @Column(name = "roomcount", columnDefinition = "TEXT")
    private String roomcount;

    @Column(name = "reservationlodging", columnDefinition = "TEXT")
    private String reservationlodging;

    @Column(name = "reservationurl", columnDefinition = "TEXT")
    private String reservationurl;

    @Column(name = "roomtype", columnDefinition = "TEXT")
    private String roomtype;

    @Column(name = "scalelodging", columnDefinition = "TEXT")
    private String scalelodging;

    @Column(name = "subfacility", columnDefinition = "TEXT")
    private String subfacility;

    @Column(name = "barbecue", columnDefinition = "TEXT")
    private String barbecue;

    @Column(name = "beauty", columnDefinition = "TEXT")
    private String beauty;

    @Column(name = "beverage", columnDefinition = "TEXT")
    private String beverage;

    @Column(name = "bicycle", columnDefinition = "TEXT")
    private String bicycle;

    @Column(name = "campfire", length = 50)
    private String campfire;

    @Column(name = "fitness", length = 50)
    private String fitness;

    @Column(name = "karaoke", length = 50)
    private String karaoke;

    @Column(name = "publicbath", length = 50)
    private String publicbath;

    @Column(name = "publicpc", length = 50)
    private String publicpc;

    @Column(name = "sauna", length = 50)
    private String sauna;

    @Column(name = "seminar", length = 50)
    private String seminar;

    @Column(name = "sports", length = 50)
    private String sports;

    @Column(name = "refundregulation", columnDefinition = "TEXT")
    private String refundregulation;

    // 필드 추가: contentTypeId = 38 (쇼핑)
    @Column(name = "chkbabycarriageshopping", length = 50)
    private String chkbabycarriageshopping;

    @Column(name = "chkcreditcardshopping", length = 50)
    private String chkcreditcardshopping;

    @Column(name = "chkpetshopping", length = 50)
    private String chkpetshopping;

    @Column(name = "culturecenter", columnDefinition = "TEXT")
    private String culturecenter;

    @Column(name = "fairday", length = 50)
    private String fairday;

    @Column(name = "infocentershopping", columnDefinition = "TEXT")
    private String infocentershopping;

    @Column(name = "opendateshopping", length = 50)
    private String opendateshopping;

    @Column(name = "opentime", columnDefinition = "TEXT")
    private String opentime;

    @Column(name = "parkingshopping", columnDefinition = "TEXT")
    private String parkingshopping;

    @Column(name = "restdateshopping", length = 50)
    private String restdateshopping;

    @Column(name = "restroom", columnDefinition = "TEXT")
    private String restroom;

    @Column(name = "saleitem", columnDefinition = "TEXT")
    private String saleitem;

    @Column(name = "saleitemcost", columnDefinition = "TEXT")
    private String saleitemcost;

    @Column(name = "scaleshopping", columnDefinition = "TEXT")
    private String scaleshopping;

    @Column(name = "shopguide", columnDefinition = "TEXT")
    private String shopguide;

    // 필드 추가: contentTypeId = 39 (음식점)
    @Column(name = "chkcreditcardfood", length = 50)
    private String chkcreditcardfood;

    @Column(name = "discountinfofood", columnDefinition = "TEXT")
    private String discountinfofood;

    @Column(name = "firstmenu", columnDefinition = "TEXT")
    private String firstmenu;

    @Column(name = "infocenterfood", columnDefinition = "TEXT")
    private String infocenterfood;

    @Column(name = "kidsfacility", columnDefinition = "TEXT")
    private String kidsfacility;

    @Column(name = "opendatefood", length = 50)
    private String opendatefood;

    @Column(name = "opentimefood", columnDefinition = "TEXT")
    private String opentimefood;

    @Column(name = "packing", columnDefinition = "TEXT")
    private String packing;

    @Column(name = "parkingfood", columnDefinition = "TEXT")
    private String parkingfood;

    @Column(name = "reservationfood", columnDefinition = "TEXT")
    private String reservationfood;

    @Column(name = "restdatefood", length = 50)
    private String restdatefood;

    @Column(name = "scalefood", columnDefinition = "TEXT")
    private String scalefood;

    @Column(name = "seat", columnDefinition = "TEXT")
    private String seat;

    @Column(name = "smoking", length = 50)
    private String smoking;

    @Column(name = "treatmenu", columnDefinition = "TEXT")
    private String treatmenu;

    @Column(name = "lcnsno", columnDefinition = "TEXT")
    private String lcnsno;

//    @OneToOne
//    private Tag tag;

    @Builder
    public TourApi(String title, long contentid, String tel, String addr1, String addr2, int sigungucode,
                   String firstimage, String firstimage2, BigDecimal mapx, BigDecimal mapy, String zipcode,
                   String createdtime, String modifiedtime, String cat1, String cat2, String cat3,
                   int contenttypeid, Long disabled, String homepage, String overview,
                   String accomcount, String chkbabycarriage, String chkcreditcard, String chkpet,
                   String expagerange, String expguide, Integer heritage1, Integer heritage2,
                   Integer heritage3, String infocenter, String opendate, String parking, String restdate,
                   String useseason, String usetime, String accomcountculture, String chkbabycarriageculture,
                   String chkcreditcardculture, String chkpetculture, String discountinfo, String infocenterculture,
                   String parkingculture, String parkingfee, String restdateculture, String usefee,
                   String usetimeculture, String scale, String spendtime, String agelimit, String bookingplace,
                   String discountinfofestival, String eventenddate, String eventhomepage, String eventplace,
                   String eventstartdate, String festivalgrade, String placeinfo, String playtime, String program,
                   String spendtimefestival, String sponsor1, String sponsor1tel, String sponsor2, String sponsor2tel,
                   String subevent, String usetimefestival, String distance, String infocentertourcourse, String schedule,
                   String taketime, String theme, String accomcountleports, String chkbabycarriageleports,
                   String chkcreditcardleports, String chkpetleports, String expagerangeleports, String infocenterleports,
                   String openperiod, String parkingfeeleports, String parkingleports, String reservation,
                   String restdateleports, String scaleleports, String usefeeleports, String usetimeleports,
                   String accomcountlodging, String benikia, String checkintime, String checkouttime, String chkcooking,
                   String foodplace, String goodstay, String hanok, String infocenterlodging, String parkinglodging,
                   String pickup, String roomcount, String reservationlodging, String reservationurl, String roomtype,
                   String scalelodging, String subfacility, String barbecue, String beauty, String beverage,
                   String bicycle, String campfire, String fitness, String karaoke, String publicbath, String publicpc,
                   String sauna, String seminar, String sports, String refundregulation, String chkbabycarriageshopping,
                   String chkcreditcardshopping, String chkpetshopping, String culturecenter, String fairday,
                   String infocentershopping, String opendateshopping, String opentime, String parkingshopping,
                   String restdateshopping, String restroom, String saleitem, String saleitemcost, String scaleshopping,
                   String shopguide, String chkcreditcardfood, String discountinfofood, String firstmenu, String infocenterfood,
                   String kidsfacility, String opendatefood, String opentimefood, String packing, String parkingfood,
                   String reservationfood, String restdatefood, String scalefood, String seat, String smoking,
                   String treatmenu, String lcnsno) {
        this.title = title;
        this.contentid = contentid;
        this.tel = tel;
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.sigungucode = sigungucode;
        this.firstimage = firstimage;
        this.firstimage2 = firstimage2;
        this.mapx = mapx;
        this.mapy = mapy;
        this.zipcode = zipcode;
        this.createdtime = createdtime;
        this.modifiedtime = modifiedtime;
        this.cat1 = cat1;
        this.cat2 = cat2;
        this.cat3 = cat3;
        this.contenttypeid = contenttypeid;
        this.disabled = disabled;
        this.homepage = homepage;
        this.overview = overview;
        this.accomcount = accomcount;
        this.chkbabycarriage = chkbabycarriage;
        this.chkcreditcard = chkcreditcard;
        this.chkpet = chkpet;
        this.expagerange = expagerange;
        this.expguide = expguide;
        this.heritage1 = heritage1;
        this.heritage2 = heritage2;
        this.heritage3 = heritage3;
        this.infocenter = infocenter;
        this.opendate = opendate;
        this.parking = parking;
        this.restdate = restdate;
        this.useseason = useseason;
        this.usetime = usetime;
        this.accomcountculture = accomcountculture;
        this.chkbabycarriageculture = chkbabycarriageculture;
        this.chkcreditcardculture = chkcreditcardculture;
        this.chkpetculture = chkpetculture;
        this.discountinfo = discountinfo;
        this.infocenterculture = infocenterculture;
        this.parkingculture = parkingculture;
        this.parkingfee = parkingfee;
        this.restdateculture = restdateculture;
        this.usefee = usefee;
        this.usetimeculture = usetimeculture;
        this.scale = scale;
        this.spendtime = spendtime;
        this.agelimit = agelimit;
        this.bookingplace = bookingplace;
        this.discountinfofestival = discountinfofestival;
        this.eventenddate = eventenddate;
        this.eventhomepage = eventhomepage;
        this.eventplace = eventplace;
        this.eventstartdate = eventstartdate;
        this.festivalgrade = festivalgrade;
        this.placeinfo = placeinfo;
        this.playtime = playtime;
        this.program = program;
        this.spendtimefestival = spendtimefestival;
        this.sponsor1 = sponsor1;
        this.sponsor1tel = sponsor1tel;
        this.sponsor2 = sponsor2;
        this.sponsor2tel = sponsor2tel;
        this.subevent = subevent;
        this.usetimefestival = usetimefestival;
        this.distance = distance;
        this.infocentertourcourse = infocentertourcourse;
        this.schedule = schedule;
        this.taketime = taketime;
        this.theme = theme;
        this.accomcountleports = accomcountleports;
        this.chkbabycarriageleports = chkbabycarriageleports;
        this.chkcreditcardleports = chkcreditcardleports;
        this.chkpetleports = chkpetleports;
        this.expagerangeleports = expagerangeleports;
        this.infocenterleports = infocenterleports;
        this.openperiod = openperiod;
        this.parkingfeeleports = parkingfeeleports;
        this.parkingleports = parkingleports;
        this.reservation = reservation;
        this.restdateleports = restdateleports;
        this.scaleleports = scaleleports;
        this.usefeeleports = usefeeleports;
        this.usetimeleports = usetimeleports;
        this.accomcountlodging = accomcountlodging;
        this.benikia = benikia;
        this.checkintime = checkintime;
        this.checkouttime = checkouttime;
        this.chkcooking = chkcooking;
        this.foodplace = foodplace;
        this.goodstay = goodstay;
        this.hanok = hanok;
        this.infocenterlodging = infocenterlodging;
        this.parkinglodging = parkinglodging;
        this.pickup = pickup;
        this.roomcount = roomcount;
        this.reservationlodging = reservationlodging;
        this.reservationurl = reservationurl;
        this.roomtype = roomtype;
        this.scalelodging = scalelodging;
        this.subfacility = subfacility;
        this.barbecue = barbecue;
        this.beauty = beauty;
        this.beverage = beverage;
        this.bicycle = bicycle;
        this.campfire = campfire;
        this.fitness = fitness;
        this.karaoke = karaoke;
        this.publicbath = publicbath;
        this.publicpc = publicpc;
        this.sauna = sauna;
        this.seminar = seminar;
        this.sports = sports;
        this.refundregulation = refundregulation;
        this.chkbabycarriageshopping = chkbabycarriageshopping;
        this.chkcreditcardshopping = chkcreditcardshopping;
        this.chkpetshopping = chkpetshopping;
        this.culturecenter = culturecenter;
        this.fairday = fairday;
        this.infocentershopping = infocentershopping;
        this.opendateshopping = opendateshopping;
        this.opentime = opentime;
        this.parkingshopping = parkingshopping;
        this.restdateshopping = restdateshopping;
        this.restroom = restroom;
        this.saleitem = saleitem;
        this.saleitemcost = saleitemcost;
        this.scaleshopping = scaleshopping;
        this.shopguide = shopguide;
        this.chkcreditcardfood = chkcreditcardfood;
        this.discountinfofood = discountinfofood;
        this.firstmenu = firstmenu;
        this.infocenterfood = infocenterfood;
        this.kidsfacility = kidsfacility;
        this.opendatefood = opendatefood;
        this.opentimefood = opentimefood;
        this.packing = packing;
        this.parkingfood = parkingfood;
        this.reservationfood = reservationfood;
        this.restdatefood = restdatefood;
        this.scalefood = scalefood;
        this.seat = seat;
        this.smoking = smoking;
        this.treatmenu = treatmenu;
        this.lcnsno = lcnsno;
    }
}