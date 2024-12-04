package org.example.spring_data_jdbc.repository;

import org.example.spring_data_jdbc.model.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookRepository {

    private final JdbcTemplate jdbcTemplate;

    public BookRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Book> bookRowMapper = (rs, rowNum) -> new Book(
            rs.getLong("id"),
            rs.getString("title"),
            rs.getString("author"),
            rs.getInt("published_year")
    );

    public List<Book> findAll() {
        return jdbcTemplate.query("SELECT * FROM books", bookRowMapper);
    }

    public Optional<Book> findById(Long id) {
        return jdbcTemplate.query("SELECT * FROM books WHERE id = ?", bookRowMapper, id)
                .stream().findFirst();
    }

    public int save(Book book) {
        return jdbcTemplate.update("INSERT INTO books (title, author, published_year) VALUES (?, ?, ?)",
                book.getTitle(), book.getAuthor(), book.getPublishedYear());
    }

    public int update(Book book) {
        return jdbcTemplate.update("UPDATE books SET title = ?, author = ?, published_year = ? WHERE id = ?",
                book.getTitle(), book.getAuthor(), book.getPublishedYear(), book.getId());
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM books WHERE id = ?", id);
    }
}
