package pl.kamiljurczyk.clippings.clippingFile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

interface ClippingFileRepository extends JpaRepository<ClippingFile, Long> {

    @Query("select c from ClippingFile c " +
            "left join fetch c.clippingList " +
            "where c.id = :id")
    Optional<ClippingFile> getClippingFileById(@Param("id") Long id);
}
