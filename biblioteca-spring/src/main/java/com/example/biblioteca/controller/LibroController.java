package com.example.biblioteca.controller;

import com.example.biblioteca.model.Libro;
import com.example.biblioteca.repository.LibroRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class LibroController {

    private static final Logger logger = LoggerFactory.getLogger(LibroController.class);

    @Autowired
    private LibroRepository libroRepository;

    @GetMapping("/")
    public String listar(Model model) {
        List<Libro> libros = libroRepository.findAll();
        model.addAttribute("libros", libros);
        return "listar";
    }

    @GetMapping("/listar")
    public String listarRedirect() {
        return "redirect:/";
    }

    @GetMapping("/agregar")
    public String agregar(Model model) {
        model.addAttribute("libro", new Libro());
        return "agregar";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Libro libro, RedirectAttributes redirectAttributes) {
        try {
            logger.info("Guardando libro: {}", libro);
            libroRepository.save(libro);
            redirectAttributes.addFlashAttribute("mensaje", "Libro guardado correctamente");
            return "redirect:/";
        } catch (Exception e) {
            logger.error("Error al guardar el libro", e);
            redirectAttributes.addFlashAttribute("error", "Error al guardar el libro: " + e.getMessage());
            return "redirect:/agregar";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Optional<Libro> libro = libroRepository.findById(id);
        if (libro.isPresent()) {
            model.addAttribute("libro", libro.get());
            return "editar";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute Libro libro, RedirectAttributes redirectAttributes) {
        try {
            libroRepository.save(libro);
            redirectAttributes.addFlashAttribute("mensaje", "Libro actualizado correctamente");
            return "redirect:/";
        } catch (Exception e) {
            logger.error("Error al actualizar el libro", e);
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el libro: " + e.getMessage());
            return "redirect:/editar/" + libro.getId();
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            libroRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("mensaje", "Libro eliminado correctamente");
        } catch (Exception e) {
            logger.error("Error al eliminar el libro", e);
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el libro");
        }
        return "redirect:/";
    }
}