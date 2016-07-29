package studentEnrollment.model;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long>{
	/* A version to fetch List instead of Page to avoid extra count query. */
    List<Student> findAllBy(Pageable pageable);

    public Student findByUserName(String userName);
    
}
