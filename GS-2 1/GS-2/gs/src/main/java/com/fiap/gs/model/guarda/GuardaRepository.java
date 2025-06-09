package com.fiap.gs.model.guarda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface GuardaRepository extends JpaRepository<GuardaCivil, Long> {

}
