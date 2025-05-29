package com.example.biblioteca.controller;

import com.example.biblioteca.model.Libro;
import com.example.biblioteca.repository.LibroRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class LibroController {

    private final LibroRepository repo;

    public LibroController(LibroRepository repo) {
        this.repo = repo;
    }

    // Mostrar lista
    @GetMapping("/")
    public String listarLibros(Model model) {
        model.addAttribute("libros", repo.findAll());
        return "listar";
    }

    // Formulario para nuevo libro
    @GetMapping("/agregar")
    public String mostrarFormulario(Model model) {
        model.addAttribute("libro", new Libro());
        return "agregar";
    }

    // Guardar nuevo libro
    @PostMapping("/guardar")
    public String guardarLibro(@ModelAttribute Libro libro) {
        repo.save(libro);
        return "redirect:/";
    }

    // Formulario para editar
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("libro", repo.findById(id).orElse(null));
        return "editar";
    }

    // Guardar edición
    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute Libro libro) {
        repo.save(libro);
        return "redirect:/";
    }

    // Eliminar
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/listar")
    public String listarLibrosDesdeListar(Model model) {
        model.addAttribute("libros", repo.findAll());
        return "listar";
    }

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout) {
        
        ModelAndView model = new ModelAndView("login");
        
        if (error != null) {
            model.addObject("error", "Credenciales incorrectas.");
        }
        
        if (logout != null) {
            model.addObject("message", "Sesión cerrada correctamente.");
        }
        
        return model;
    }

    

}



