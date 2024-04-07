package pl.kamiljurczyk.clippings.clipping;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Clipping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long clippingFileId;
    private String bookTitle;
    private String author;
    private String bookPage;
    private String bookLocation;
    @Column(length = 1000)
    private String highlight;
    private LocalDateTime bookHighlightDate;
}
