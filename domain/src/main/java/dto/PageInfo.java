package dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PageInfo{

    private int pageNum;
    private int pageSize;
    private long totalCount;
    private int totalPage;
    private int elementSize;

    @Builder
    private PageInfo(int pageNum, int pageSize, long totalCount, int totalPage,int elementSize){
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.totalPage = totalPage;
        this.elementSize = elementSize;
    }

    public static PageInfo of(int pageNum, int pageSize, long totalCount, int totalPage,int elementSize){
        return PageInfo.builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .totalCount(totalCount)
                .totalPage(totalPage)
                .elementSize(elementSize)
                .build();
    }



}
