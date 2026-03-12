package util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryUtilTest {

    @ParameterizedTest
    @ValueSource(strings = {"category1", "category10", "category20"})
    @DisplayName("존재하는 카테고리 이름이면 true를 반환한다")
    void existCategory_returnsTrue(String name) {
        assertThat(CategoryUtil.existCategory(name)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"category0", "category21", "invalid", "", "CATEGORY1"})
    @DisplayName("존재하지 않는 카테고리 이름이면 false를 반환한다")
    void existCategory_returnsFalse(String name) {
        assertThat(CategoryUtil.existCategory(name)).isFalse();
    }

    @Test
    @DisplayName("카테고리 이름 배열은 20개이다")
    void namesArraySize() {
        assertThat(CategoryUtil.names).hasSize(20);
    }

    @Test
    @DisplayName("카테고리 리스트는 20개이다")
    void listSize() {
        assertThat(CategoryUtil.list).hasSize(20);
    }
}
