package util.page;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ScrollInfoTest {

    @Test
    @DisplayName("of 메서드로 ScrollInfo를 생성한다")
    void createWithOf() {
        ScrollInfo scrollInfo = ScrollInfo.of(1, 10, true);

        assertThat(scrollInfo.getPageNum()).isEqualTo(1);
        assertThat(scrollInfo.getPageSize()).isEqualTo(10);
        assertThat(scrollInfo.isHasNext()).isTrue();
    }

    @Test
    @DisplayName("다음 페이지가 없으면 hasNext는 false이다")
    void noNextPage() {
        ScrollInfo scrollInfo = ScrollInfo.of(5, 10, false);

        assertThat(scrollInfo.isHasNext()).isFalse();
    }
}
