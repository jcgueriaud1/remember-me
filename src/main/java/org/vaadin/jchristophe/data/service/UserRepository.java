package org.vaadin.jchristophe.data.service;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.jchristophe.data.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {

    User findByUsername(String username);
}