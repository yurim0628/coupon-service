package org.example.user.service.port;

import org.example.user.domain.User;

public interface UserRepository {

    User save(User user);

    boolean existsByEmail(String email);
}
