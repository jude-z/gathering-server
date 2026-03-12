package util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import util.page.PageCalculator;
import util.page.PageInfo;
import util.page.PageableInfo;

import static org.assertj.core.api.Assertions.assertThat;

class PageCalculatorTest {

    @Nested
    @DisplayName("toPageableInfo")
    class ToPageableInfo {

        @Test
        @DisplayName("1페이지 요청 시 offset은 0이다")
        void firstPage() {
            PageableInfo result = PageCalculator.toPageableInfo(1, 10);

            assertThat(result.getOffset()).isEqualTo(0);
            assertThat(result.getLimit()).isEqualTo(10);
        }

        @Test
        @DisplayName("2페이지 요청 시 offset은 pageSize와 같다")
        void secondPage() {
            PageableInfo result = PageCalculator.toPageableInfo(2, 10);

            assertThat(result.getOffset()).isEqualTo(10);
            assertThat(result.getLimit()).isEqualTo(10);
        }

        @Test
        @DisplayName("3페이지, pageSize 5 요청 시 offset은 10이다")
        void thirdPageWithSize5() {
            PageableInfo result = PageCalculator.toPageableInfo(3, 5);

            assertThat(result.getOffset()).isEqualTo(10);
            assertThat(result.getLimit()).isEqualTo(5);
        }
    }

    @Nested
    @DisplayName("toDefaultPageableInfo")
    class ToDefaultPageableInfo {

        @Test
        @DisplayName("기본 페이지 정보는 offset 0, limit 1이다")
        void defaultValues() {
            PageableInfo result = PageCalculator.toDefaultPageableInfo();

            assertThat(result.getOffset()).isEqualTo(0);
            assertThat(result.getLimit()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("toPageInfo with raw parameters")
    class ToPageInfoRaw {

        @Test
        @DisplayName("offset 0, limit 10일 때 pageNum은 1이다")
        void firstPageInfo() {
            PageInfo<String> result = PageCalculator.toPageInfo("content", 0, 10, 50L, 10);

            assertThat(result.getContent()).isEqualTo("content");
            assertThat(result.getPageNum()).isEqualTo(1);
            assertThat(result.getPageSize()).isEqualTo(10);
            assertThat(result.getTotalCount()).isEqualTo(50L);
            assertThat(result.getTotalPage()).isEqualTo(5);
            assertThat(result.getElementSize()).isEqualTo(10);
        }

        @Test
        @DisplayName("offset 10, limit 10일 때 pageNum은 2이다")
        void secondPageInfo() {
            PageInfo<String> result = PageCalculator.toPageInfo("data", 10, 10, 25L, 5);

            assertThat(result.getPageNum()).isEqualTo(2);
            assertThat(result.getTotalPage()).isEqualTo(3);
        }

        @Test
        @DisplayName("totalCount가 limit으로 나누어 떨어지지 않으면 totalPage는 올림된다")
        void totalPageCeil() {
            PageInfo<String> result = PageCalculator.toPageInfo("data", 0, 10, 21L, 10);

            assertThat(result.getTotalPage()).isEqualTo(3);
        }

        @Test
        @DisplayName("totalCount가 0이면 totalPage는 0이다")
        void emptyResult() {
            PageInfo<String> result = PageCalculator.toPageInfo("empty", 0, 10, 0L, 0);

            assertThat(result.getPageNum()).isEqualTo(1);
            assertThat(result.getTotalCount()).isEqualTo(0L);
            assertThat(result.getTotalPage()).isEqualTo(0);
            assertThat(result.getElementSize()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("toPageInfo with PageableInfo")
    class ToPageInfoWithPageableInfo {

        @Test
        @DisplayName("PageableInfo를 사용하여 PageInfo를 생성한다")
        void withPageableInfo() {
            PageableInfo pageableInfo = PageableInfo.of(20, 10);

            PageInfo<String> result = PageCalculator.toPageInfo("content", pageableInfo, 100L, 10);

            assertThat(result.getPageNum()).isEqualTo(3);
            assertThat(result.getPageSize()).isEqualTo(10);
            assertThat(result.getTotalCount()).isEqualTo(100L);
            assertThat(result.getTotalPage()).isEqualTo(10);
            assertThat(result.getElementSize()).isEqualTo(10);
        }

        @Test
        @DisplayName("raw 파라미터와 PageableInfo 결과가 동일하다")
        void consistentWithRawParams() {
            PageableInfo pageableInfo = PageableInfo.of(10, 5);

            PageInfo<String> fromPageable = PageCalculator.toPageInfo("data", pageableInfo, 30L, 5);
            PageInfo<String> fromRaw = PageCalculator.toPageInfo("data", 10, 5, 30L, 5);

            assertThat(fromPageable.getPageNum()).isEqualTo(fromRaw.getPageNum());
            assertThat(fromPageable.getPageSize()).isEqualTo(fromRaw.getPageSize());
            assertThat(fromPageable.getTotalCount()).isEqualTo(fromRaw.getTotalCount());
            assertThat(fromPageable.getTotalPage()).isEqualTo(fromRaw.getTotalPage());
        }
    }
}
