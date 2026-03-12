package entity.category;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryTest {

    @Test
    @DisplayName("changeName - 카테고리 이름 변경")
    void changeName() {
        Category category = Category.builder()
                .name("original")
                .build();

        category.changeName("updated");

        assertThat(category.getName()).isEqualTo("updated");
    }
}
