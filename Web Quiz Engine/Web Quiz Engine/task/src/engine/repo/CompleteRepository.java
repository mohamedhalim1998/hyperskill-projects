package engine.repo;

import engine.model.Complete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompleteRepository extends JpaRepository<Complete, Integer> {
    Page<Complete> findCompletionsByUserEmail(Pageable page, String userEmail);
}
