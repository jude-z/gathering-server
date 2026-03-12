package util.page;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PageableInfoTest {

    @Test
    @DisplayName("of 메서드로 PageableInfo를 생성한다")
    void createWithOf() {
        PageableInfo pageableInfo = PageableInfo.of(0, 10);

        assertThat(pageableInfo.getOffset()).isEqualTo(0);
        assertThat(pageableInfo.getLimit()).isEqualTo(10);
    }

    @Test
    @DisplayName("offset과 limit 값이 정확히 설정된다")
    void valuesAreCorrect() {
        PageableInfo pageableInfo = PageableInfo.of(20, 5);

        assertThat(pageableInfo.getOffset()).isEqualTo(20);
        assertThat(pageableInfo.getLimit()).isEqualTo(5);
    }
}
