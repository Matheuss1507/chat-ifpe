package br.com.ifpe.chat.model.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.ifpe.chat.model.entities.Estudante;

public interface EstudanteRepository extends JpaRepository<Estudante, String> {
    Optional<Estudante> findByEmail(String email);
}