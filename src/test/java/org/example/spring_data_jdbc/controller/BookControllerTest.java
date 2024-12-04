package org.example.spring_data_jdbc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.spring_data_jdbc.model.Book;
import org.example.spring_data_jdbc.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private final Book sampleBook = new Book(1L, "Sample Title", "Sample Author", 2021);

    @Test
    void getAllBooks_ShouldReturnListOfBooks() throws Exception {
        when(bookService.getAllBooks()).thenReturn(Arrays.asList(sampleBook));

        mockMvc.perform(get("/api/books")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("Sample Title"));

        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void getBookById_WhenBookExists_ShouldReturnBook() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(Optional.of(sampleBook));

        mockMvc.perform(get("/api/books/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Sample Title"));

        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    void getBookById_WhenBookDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/books/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    void addBook_ShouldReturnSuccessMessage() throws Exception {
        doNothing().when(bookService).addBook(any(Book.class));

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleBook)))
                .andExpect(status().isOk())
                .andExpect(content().string("Book added successfully"));

        verify(bookService, times(1)).addBook(any(Book.class));
    }

    @Test
    void updateBook_ShouldReturnSuccessMessage() throws Exception {
        doNothing().when(bookService).updateBook(any(Book.class));

        mockMvc.perform(put("/api/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleBook)))
                .andExpect(status().isOk())
                .andExpect(content().string("Book updated successfully"));

        verify(bookService, times(1)).updateBook(any(Book.class));
    }

    @Test
    void deleteBook_WhenBookExists_ShouldReturnSuccessMessage() throws Exception {
        when(bookService.deleteBook(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/books/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("Book deleted successfully"));

        verify(bookService, times(1)).deleteBook(1L);
    }

    @Test
    void deleteBook_WhenBookDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(bookService.deleteBook(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/books/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Book not found"));

        verify(bookService, times(1)).deleteBook(1L);
    }
}
