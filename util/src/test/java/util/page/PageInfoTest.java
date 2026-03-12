package util.page;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PageInfoTest {

    @Test
    @DisplayName("of 메서드로 PageInfo를 생성한다")
    void createWithOf() {
        PageInfo<String> pageInfo = PageInfo.of("content", 1, 10, 100L, 10, 10);

        assertThat(pageInfo.getContent()).isEqualTo("content");
        assertThat(pageInfo.getPageNum()).isEqualTo(1);
        assertThat(pageInfo.getPageSize()).isEqualTo(10);
        assertThat(pageInfo.getTotalCount()).isEqualTo(100L);
        assertThat(pageInfo.getTotalPage()).isEqualTo(10);
        assertThat(pageInfo.getElementSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("제네릭 타입으로 List를 사용할 수 있다")
    void genericWithList() {
        List<String> items = List.of("a", "b", "c");

        PageInfo<List<String>> pageInfo = PageInfo.of(items, 1, 10, 3L, 1, 3);

        assertThat(pageInfo.getContent()).hasSize(3);
        assertThat(pageInfo.getContent()).containsExactly("a", "b", "c");
    }
}
