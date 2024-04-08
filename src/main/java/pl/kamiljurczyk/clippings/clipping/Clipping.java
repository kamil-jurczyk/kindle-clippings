package pl.kamiljurczyk.clippings.clipping;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import pl.kamiljurczyk.clippings.clippingFile.ClippingFile;

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
    private String bookTitle;
    private String author;
    private String bookPage;
    private String bookLocation;
    private String highlight;
    private LocalDateTime bookHighlightDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private ClippingFile clippingFile;
}
