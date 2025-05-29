package com.example.biblioteca.repository;

import com.example.biblioteca.model.Libro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class LibroRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LibroRepository libroRepository;

    @Test
    public void testGuardarYRecuperarLibro() {
        // Crear y persistir un libro
        Libro libro = new Libro("El Principito", "Antoine de Saint-Exupéry", 1943);
        entityManager.persist(libro);
        entityManager.flush();

        // Recuperar todos los libros
        List<Libro> libros = libroRepository.findAll();
        
        // Verificar que se guardó correctamente
        assertThat(libros).hasSize(1);
        assertThat(libros.get(0).getTitulo()).isEqualTo("El Principito");
        assertThat(libros.get(0).getAutor()).isEqualTo("Antoine de Saint-Exupéry");
        assertThat(libros.get(0).getAnio()).isEqualTo(1943);
    }

    @Test
    public void testActualizarLibro() {
        // Crear y persistir un libro
        Libro libro = new Libro("Título Original", "Autor Original", 2000);
        entityManager.persist(libro);
        entityManager.flush();
        
        // Modificar el libro
        libro.setTitulo("Título Actualizado");
        libro.setAutor("Autor Actualizado");
        libro.setAnio(2023);
        libroRepository.save(libro);
        
        // Recuperar el libro actualizado
        Libro libroActualizado = libroRepository.findById(libro.getId()).orElse(null);
        
        // Verificar que se actualizó correctamente
        assertThat(libroActualizado).isNotNull();
        assertThat(libroActualizado.getTitulo()).isEqualTo("Título Actualizado");
        assertThat(libroActualizado.getAutor()).isEqualTo("Autor Actualizado");
        assertThat(libroActualizado.getAnio()).isEqualTo(2023);
    }

    @Test
    public void testEliminarLibro() {
        // Crear y persistir un libro
        Libro libro = new Libro("Libro a eliminar", "Autor", 2000);
        entityManager.persist(libro);
        
        // Crear un segundo libro
        Libro libro2 = new Libro("Libro a mantener", "Otro Autor", 2010);
        entityManager.persist(libro2);
        entityManager.flush();
        
        // Eliminar el primer libro
        libroRepository.deleteById(libro.getId());
        
        // Recuperar todos los libros
        List<Libro> libros = libroRepository.findAll();
        
        // Verificar que solo queda un libro
        assertThat(libros).hasSize(1);
        assertThat(libros.get(0).getTitulo()).isEqualTo("Libro a mantener");
    }
}