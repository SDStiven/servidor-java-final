package com.labanta.servidorlocal.service;

import com.labanta.servidorlocal.dto.LoginRequestDTO;
import com.labanta.servidorlocal.dto.RegistoRequestDTO;
import com.labanta.servidorlocal.exception.UtilizadorExistenteException;
import com.labanta.servidorlocal.model.UtilizadorModel;
import com.labanta.servidorlocal.repository.UtilizadorRepository;
import com.labanta.servidorlocal.security.JwtService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UtilizadorRepository repository;
    private final EmailService emailService;


    public AuthService(UtilizadorRepository utilizadorRepository, EmailService emailService) {
        this.repository = utilizadorRepository;

        this.emailService = emailService;
    }

    public UtilizadorModel registarUtilizador(RegistoRequestDTO dados) {
        UtilizadorModel novoutilizador = new UtilizadorModel();

        if (repository.findByUsername(dados.getUsername()).isPresent()) {
            throw new UtilizadorExistenteException(
                    "Este username já está em uso, por favor escolha outro.");
        }

        novoutilizador.setUsername(dados.getUsername());
        novoutilizador.setEmail(dados.getEmail());
        novoutilizador.setPassword(dados.getPassword());

        emailService.enviarEmailBoasVindas(novoutilizador.getEmail(), novoutilizador.getUsername());

        return repository.save(novoutilizador);
    }

    public UtilizadorModel buscarUtilizadorPorUsername(String username) {

        return repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));
    }

    // public String login(LoginRequestDTO dados) {
    //     UtilizadorModel Utilizador = repository
    //             .findByUsername(dados.getUsername())
    //             .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));

    //     if (!Utilizador.getPassword().equals(dados.getPassword())) {
    //         throw new RuntimeException("Password incorreta");
    //     }
    //     return jwtService.generateToken(Utilizador.getUsername());
    // }

}
