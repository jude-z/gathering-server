package util;

import dto.PageInfo;

public class PageCalculator {
    public static PageInfo of(int offset, int limit, long totalCount, int elementSize){
        int pageNum = (offset / limit) + 1;
        int totalPage = (int) Math.ceil((double) totalCount / limit);
        return PageInfo.of(pageNum, limit, totalCount, totalPage, elementSize);
    }
}
