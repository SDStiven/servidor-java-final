package com.labanta.servidorlocal.repository;

import com.labanta.servidorlocal.model.UtilizadorModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilizadorRepository  extends JpaRepository <UtilizadorModel, Long> {
    Optional<UtilizadorModel> findByUsername(String username);
}
