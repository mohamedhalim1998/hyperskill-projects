package platform.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import platform.model.CodeSnippet;
import java.util.List;

@Repository
public interface CodeRepository extends CrudRepository<CodeSnippet, String> {
    @Query("SELECT c FROM codes c WHERE c.hasViewLimit = false AND c.timeLimit  = 0 ORDER BY c.date DESC")
    List<CodeSnippet> getLatestCode(Pageable pageable);


}
