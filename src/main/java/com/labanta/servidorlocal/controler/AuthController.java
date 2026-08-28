package com.labanta.servidorlocal.controler;

import com.labanta.servidorlocal.dto.GeoLocationResponseDTO;
import com.labanta.servidorlocal.dto.LoginRequestDTO;
import com.labanta.servidorlocal.dto.RegistoRequestDTO;
import com.labanta.servidorlocal.model.UtilizadorModel;
import com.labanta.servidorlocal.security.JwtService;
import com.labanta.servidorlocal.service.AuthService;
import com.labanta.servidorlocal.service.EmailService;
import com.labanta.servidorlocal.service.GeoServiceService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;
    private final AuthService authService;
    private final GeoServiceService geoServiceService;
    private final EmailService emailService;

    

    public AuthController(JwtService jwtService, AuthService authService,GeoServiceService geoServiceService,EmailService emailService) {
        this.jwtService = jwtService;
        this.authService = authService;
        this.geoServiceService = geoServiceService;
        this.emailService = emailService;
    }

    @Operation(
            summary = "login",
            description = "Rota para fazer login"
    )
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequest) {

        UtilizadorModel utilizador = authService.buscarUtilizadorPorUsername(loginRequest.getUsername());

        if (!utilizador.getPassword().equals(loginRequest.getPassword())) {
            throw new RuntimeException("Credenciais inválidas");
        }

        String token = jwtService.gerarToken(
                utilizador.getUsername());

        return ResponseEntity.ok(token);
    }

    @Operation(
            summary = "registar",
            description = "Rota para registar"
    )
    @PostMapping("/registar")
    public UtilizadorModel registar(@RequestBody RegistoRequestDTO dados) {
        return authService.registarUtilizador(dados);
    }

    @Operation(
            summary = "alerta-login",
            description = "Rota para alerta-login"
    )
    @PostMapping("/alerta-login")
    public ResponseEntity<String> alertaLogin(@RequestParam String email , @RequestParam String ip) {

        GeoLocationResponseDTO geoLocationResponseDTO = geoServiceService.localizarIP(ip);

        emailService.enviarAlertaSeguranca(email,geoLocationResponseDTO.getCity(),geoLocationResponseDTO.getCountry_name());

        return ResponseEntity.ok("Alerta de segurança processado!");
    }
}
