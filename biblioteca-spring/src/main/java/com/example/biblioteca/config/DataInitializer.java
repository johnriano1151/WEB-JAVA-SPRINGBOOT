package com.example.biblioteca.config;

import com.example.biblioteca.model.Usuario;
import com.example.biblioteca.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Crear usuario administrador predeterminado si no existe
        if (usuarioRepository.findByUsername("admin").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("12345"));
            admin.setNombre("Administrador");
            admin.setRol("ADMIN");
            usuarioRepository.save(admin);
        }

        // Crear usuario bibliotecario si no existe
        if (usuarioRepository.findByUsername("bibliotecario").isEmpty()) {
            Usuario bibliotecario = new Usuario();
            bibliotecario.setUsername("bibliotecario");
            bibliotecario.setPassword(passwordEncoder.encode("12345"));
            bibliotecario.setNombre("Bibliotecario");
            bibliotecario.setRol("USER");
            usuarioRepository.save(bibliotecario);
        }
    }
}