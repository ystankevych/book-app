package mate.academy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.BookDto;
import mate.academy.dto.BookSearchParametersDto;
import mate.academy.dto.CreateBookRequestDto;
import mate.academy.service.BookService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book management", description = "Endpoints for managing books")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/books")
@Validated
public class BookController {
    private final BookService bookService;

    @PostMapping
    @Operation(summary = "Create a new book")
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto book) {
        return bookService.save(book);
    }

    @PostMapping("/{id}")
    @Operation(
            summary = "Update existing book",
            description = "Update existing book by its id and specifying new parameters"
    )
    public BookDto updateBook(@PathVariable @Positive Long id,
                              @RequestBody @Valid CreateBookRequestDto book) {
        return bookService.updateBook(id, book);
    }

    @GetMapping
    @Operation(
            summary = "Get all books",
            description = """
    Getting all books with default pagination (10 books per page) and sorting"""
    )
    public List<BookDto> getAll(@ParameterObject @PageableDefault Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a book by its id")
    public BookDto getBookById(@Parameter(description = "Id of book to be searched")
                               @PathVariable @Positive Long id) {
        return bookService.findById(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book by its id")
    public void deleteById(@PathVariable @Positive Long id) {
        bookService.deleteById(id);
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search books by parameters",
            description = """
                    Search books based on the occurrence of a keyword in the title(case-insensitive)
                    and authors""")
    public List<BookDto> searchBooks(BookSearchParametersDto bookSearchParametersDto,
                                     @ParameterObject @PageableDefault Pageable pageable) {
        return bookService.findByParameters(bookSearchParametersDto, pageable);
    }
}
