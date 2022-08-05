package uk.co.jpmorgan.contactmanager.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import uk.co.jpmorgan.contactmanager.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User getById(Long id);
    List<User> findByIdIn(List<Long> userIds);
}
