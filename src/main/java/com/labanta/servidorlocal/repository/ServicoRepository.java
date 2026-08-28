package com.labanta.servidorlocal.repository;

import com.labanta.servidorlocal.model.ServicoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicoRepository extends JpaRepository<ServicoModel, Long>{
    // Encontra todos os serviços que estão ativos
    List<ServicoModel> findByEstaAtivoTrue();

    // Encontra serviços que custam menos do que um determinado valor
    List<ServicoModel> findByPrecoLessThan(Double valorMaximo);

    List<ServicoModel> findByTituloContainingIgnoreCase(String termo);
    // Só com esta linha, vocês acabaram de herdar métodos gratuitos para:
        // - .findAll() ->
    // Buscar todos (substitui a nossa antiga Lista!)
        // - .save()    -> Guardar novo serviço ou atualizar
        // - .deleteById() -> Apagar serviço pelo ID
        // - .findById()   -> Buscar apenas um serviço específico
}
