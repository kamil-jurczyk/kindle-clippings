package pl.kamiljurczyk.clippings.clippingFile;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kamiljurczyk.clippings.clipping.Clipping;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClippingFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "clippingFileId")
    private List<Clipping> clippingList;
}
