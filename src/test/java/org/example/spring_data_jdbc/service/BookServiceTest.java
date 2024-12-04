package org.example.spring_data_jdbc.service;

import org.example.spring_data_jdbc.model.Book;
import org.example.spring_data_jdbc.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book sampleBook;

    @BeforeEach
    void setUp() {
        sampleBook = new Book(1L, "Sample Title", "Sample Author", 2021);
    }

    @Test
    void getAllBooks_ShouldReturnListOfBooks() {
        when(bookRepository.findAll()).thenReturn(Arrays.asList(sampleBook));

        List<Book> books = bookService.getAllBooks();

        assertNotNull(books);
        assertEquals(1, books.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getBookById_WhenBookExists_ShouldReturnBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));

        Optional<Book> book = bookService.getBookById(1L);

        assertTrue(book.isPresent());
        assertEquals(sampleBook, book.get());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void getBookById_WhenBookDoesNotExist_ShouldReturnEmptyOptional() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Book> book = bookService.getBookById(1L);

        assertFalse(book.isPresent());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void addBook_ShouldInvokeSaveMethod() {
        when(bookRepository.save(sampleBook)).thenReturn(1);

        bookService.addBook(sampleBook);

        verify(bookRepository, times(1)).save(sampleBook);
    }

    @Test
    void updateBook_ShouldInvokeUpdateMethod() {
        when(bookRepository.update(sampleBook)).thenReturn(1);

        bookService.updateBook(sampleBook);

        verify(bookRepository, times(1)).update(sampleBook);
    }

    @Test
    void deleteBook_WhenBookExists_ShouldReturnTrue() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        when(bookRepository.deleteById(1L)).thenReturn(1);

        boolean isDeleted = bookService.deleteBook(1L);

        assertTrue(isDeleted);
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteBook_WhenBookDoesNotExist_ShouldReturnFalse() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        boolean isDeleted = bookService.deleteBook(1L);

        assertFalse(isDeleted);
        verify(bookRepository, never()).deleteById(1L);
    }
}
