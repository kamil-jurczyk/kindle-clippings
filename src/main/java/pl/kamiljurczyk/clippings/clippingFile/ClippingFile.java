package pl.kamiljurczyk.clippings.clippingFile;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kamiljurczyk.clippings.clipping.Clipping;

import java.util.ArrayList;
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
    private Long userId;
    private String fileName;

    @OneToMany(
            mappedBy = "clippingFile",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    @JsonManagedReference
    private List<Clipping> clippingList = new ArrayList<>();

    public void addClipping(Clipping clipping) {
        clippingList.add(clipping);
        clipping.setClippingFile(this);
    }

    public void removeClipping(Clipping clipping) {
        clippingList.remove(clipping);
        clipping.setClippingFile(null);
    }
}
