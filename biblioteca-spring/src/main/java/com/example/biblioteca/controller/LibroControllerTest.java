package com.example.biblioteca.controller;

import com.example.biblioteca.model.Libro;
import com.example.biblioteca.repository.LibroRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LibroController.class)
public class LibroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibroRepository libroRepository;

    @Test
    @WithMockUser
    public void testListarLibros() throws Exception {
        // Mock de datos
        Libro libro1 = new Libro("El Quijote", "Cervantes", 1605);
        Libro libro2 = new Libro("Cien años de soledad", "García Márquez", 1967);
        libro1.setId(1L);
        libro2.setId(2L);
        
        when(libroRepository.findAll()).thenReturn(Arrays.asList(libro1, libro2));

        // Ejecutar petición y verificar
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("listar"))
                .andExpect(model().attributeExists("libros"));
    }

    @Test
    @WithMockUser
    public void testAgregarLibro() throws Exception {
        mockMvc.perform(get("/agregar"))
                .andExpect(status().isOk())
                .andExpect(view().name("agregar"))
                .andExpect(model().attributeExists("libro"));
    }

    @Test
    @WithMockUser
    public void testGuardarLibro() throws Exception {
        when(libroRepository.save(any(Libro.class))).thenReturn(new Libro("Test", "Test Author", 2000));

        mockMvc.perform(post("/guardar")
                .param("titulo", "Test")
                .param("autor", "Test Author")
                .param("anio", "2000")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @WithMockUser
    public void testEditarLibro() throws Exception {
        Libro libro = new Libro("El Quijote", "Cervantes", 1605);
        libro.setId(1L);
        
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));

        mockMvc.perform(get("/editar/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("editar"))
                .andExpect(model().attributeExists("libro"));
    }
}