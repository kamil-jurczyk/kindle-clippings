package pl.kamiljurczyk.clippings.clippingFile;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.kamiljurczyk.clippings.clippingFile.ClippingExtractData.*;

class ClippingExtractDataTest {

    private final ArrayList<String> highlightList = new ArrayList<>(List.of(
            "The Pragmatic Programmer: your journey to mastery, 20th Anniversary Edition, 2nd Edition (Andrew Hunt;David Thomas)",
            "- Your Highlight on page 41 | Location 617-617 | Added on Monday, February 20, 2023 10:09:20 AM",
            "",
            "contagious",
            "=========="
    ));

    @Test
    void shouldExtractAuthorFromList() {
        // given
        // when
        String author = getAuthor(highlightList);
        // then
        assertThat(author).isEqualTo("Andrew Hunt;David Thomas");
    }

    @Test
    void shouldExtractBookTitleFromList() {
        // given
        // when
        String bookTitle = getBookTitle(highlightList);
        // then
        assertThat(bookTitle).isEqualTo("The Pragmatic Programmer: your journey to mastery, 20th Anniversary Edition, 2nd Edition");
    }

    @Test
    void shouldExtractBookPageFromList() {
        // given
        // when
        String bookPage = getBookPage(highlightList);
        // then
        assertThat(bookPage).isEqualTo("41");
    }

    @Test
    void shouldExtractBookHighlightDateFromList() {
        // given
        // when
        LocalDateTime highlightDate = getBookHighlightDate(highlightList);
        // then
        assertThat(highlightDate).isEqualTo(LocalDateTime.of(2023, Month.FEBRUARY, 20, 10, 9, 20));
    }

    @Test
    void shouldExtractHighlightFromList() {
        // given
        // when
        String highlight = getHighlight(highlightList);
        // then
        assertThat(highlight).isEqualTo("contagious");
    }

    @Test
    void shouldExtractBookLocationFromList() {
        // given
        // when
        String bookLocation = getBookLocation(highlightList);
        // then
        assertThat(bookLocation).isEqualTo("617-617");
    }
}