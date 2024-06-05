package pl.kamiljurczyk.clippings.clippingFile;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.kamiljurczyk.clippings.clipping.Clipping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
@Slf4j
class ClippingExtractData {

    private static final String DATE_FORMAT = "MMMM d, yyyy h:mm:ss a";
    private static final String BOOK_PAGE_OPEN_STRING = "page ";
    private static final String BOOK_PAGE_CLOSE_STRING = " |";
    private static final String BOOK_LOCATION_OPEN_STRING = "Location ";
    private static final String BOOK_LOCATION_CLOSE_STRING = " |";
    private static final String AUTHOR_OPEN_STRING = "(";
    private static final String AUTHOR_CLOSE_STRING = ")";
    private static final String BOOK_TITLE_PATTERN = " \\(.*?\\)";

    List<Clipping> toClippingEntitiesList(List<ArrayList<String>> listOfClippings) {
        return listOfClippings.stream()
                .map(highlightList -> Clipping.builder()
                        .bookTitle(getBookTitle(highlightList))
                        .author(getAuthor(highlightList))
                        .highlight(getHighlight(highlightList))
                        .bookPage(getBookPage(highlightList))
                        .bookLocation(getBookLocation(highlightList))
                        .bookHighlightDate(getBookHighlightDate(highlightList))
                        .build())
                .toList();
    }

    List<ArrayList<String>> extractClippingsFromTxtFile(MultipartFile clipping) {
        if (clipping == null || clipping.isEmpty()) {
            throw new IllegalArgumentException("The file is empty.");
        }

        ArrayList<ArrayList<String>> linesList = new ArrayList<>();

        try (InputStream inputStream = clipping.getInputStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            ArrayList<String> group = new ArrayList<>();
            int linesCount = 0;

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                group.add(line);
                linesCount++;

                if (linesCount % 5 == 0) {
                    linesList.add(group);
                    group = new ArrayList<>();
                }
            }
        } catch (IOException e) {
            log.error(e.toString());
        }

        return linesList;
    }

    String getHighlight(ArrayList<String> highlightList) {
        return highlightList.get(3);
    }

    LocalDateTime getBookHighlightDate(ArrayList<String> highlightList) {
        String[] split = highlightList.get(1).split(", ");

        String date = split[split.length - 2] + ", " + split[split.length - 1];

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT).withLocale(Locale.ENGLISH);
        return LocalDateTime.parse(date, formatter);
    }

    String getBookPage(ArrayList<String> highlightList) {
        return StringUtils.substringBetween(highlightList.get(1), BOOK_PAGE_OPEN_STRING, BOOK_PAGE_CLOSE_STRING);
    }

    String getBookLocation(ArrayList<String> highlightList) {
        return StringUtils.substringBetween(highlightList.get(1), BOOK_LOCATION_OPEN_STRING, BOOK_LOCATION_CLOSE_STRING);
    }

    String getBookTitle(ArrayList<String> highlightList) {
        Pattern pattern = Pattern.compile(BOOK_TITLE_PATTERN);
        Matcher matcher = pattern.matcher(highlightList.getFirst());

        return matcher.replaceAll("");
    }

    String getAuthor(ArrayList<String> highlightList) {
        return StringUtils.substringBetween(highlightList.getFirst(), AUTHOR_OPEN_STRING, AUTHOR_CLOSE_STRING);
    }
}