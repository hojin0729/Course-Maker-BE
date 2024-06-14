package coursemaker.coursemaker.util;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
@Setter
public class CourseMakerPagination<E> {
    @Schema(description = "현재 페이지", example = "1")
    private Integer currentPage;// 현재 페이지
    @Schema(description = "전체 페이지", example = "10")
    private Integer totalPage;// 전체 페이지
    @Schema(description = "페이지당 데이터 갯수", example = "12")
    private Integer pagingSlice;// 페이징 단위
    @Schema(description = "데이터")
    private List<E> contents;// 페이징된 데이터
    @Schema(description = "전체 데이터 갯수", example = "120")
    private Long totalContents;// 전체 데이터

    private CourseMakerPagination(){}

    /*페이지네이션 -> 코스메이커 페이지네이션*/
    public CourseMakerPagination(Pageable pageable, Page<E> page){
        this.currentPage = pageable.getPageNumber() + 1;
        this.totalPage = page.getTotalPages();
        this.pagingSlice = pageable.getPageSize();
        this.contents = page.getContent();
        this.totalContents = page.getTotalElements();
    }

    /*전체 데이터 리스트 -> 코스메이커 페이지네이션
    * @param contentList: db에 있는 전체 리스트*/
    public CourseMakerPagination(Pageable pageable, List<E> contentList){
        /*List -> Page 변환*/
        int start=(int)pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), contentList.size());
        Page<E> page = new PageImpl<>(contentList.subList(start, end), pageable, contentList.size());

        this.currentPage = pageable.getPageNumber() + 1;
        this.totalPage = page.getTotalPages();
        this.pagingSlice = pageable.getPageSize();
        this.contents = page.getContent();
        this.totalContents = page.getTotalElements();
    }

}
