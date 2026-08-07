package br.com.ifpe.chat.controllers;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import br.com.ifpe.chat.dto.LoginDTO;
import br.com.ifpe.chat.model.entities.Estudante;
import br.com.ifpe.chat.model.repositories.EstudanteRepository;
import br.com.ifpe.chat.security.JwtUtils;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private EstudanteRepository estudanteRepo;

    @Autowired
    private PasswordEncoder encoder;
    
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Estudante e) {
        if (e.getMatricula() == null || e.getMatricula().isBlank() ||
            e.getSenha() == null || e.getSenha().isBlank() ||
            e.getEmail() == null || e.getEmail().isBlank()) {
            return ResponseEntity.badRequest().body("Todos os campos são obrigatórios.");
        }
        String ciphedPassword = encoder.encode(e.getSenha());
        e.setSenha(ciphedPassword);
        this.estudanteRepo.save(e);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginDTO login) {
        if (login.getLogin() == null || login.getLogin().isBlank() ||
            login.getPassword() == null || login.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body("E-mail e senha são obrigatórios.");
        }

        Optional<Estudante> eOpt = this.estudanteRepo.findByEmail(login.getLogin());
        if (eOpt.isPresent()) {
            Estudante e = eOpt.get();
            if (this.encoder.matches(login.getPassword(), e.getSenha())) {
            	String token = this.jwtUtils.generateToken(e.getMatricula(), "ESTUDANTE");
                return ResponseEntity.ok(Map.of(
                    "token", token,
                    "matricula", e.getMatricula(),
                    "nome", e.getNome()
                ));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("E-mail ou senha inválidos.");
    }
}