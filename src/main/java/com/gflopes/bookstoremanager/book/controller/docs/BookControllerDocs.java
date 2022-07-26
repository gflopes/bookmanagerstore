package com.gflopes.bookstoremanager.book.controller.docs;

import com.gflopes.bookstoremanager.book.dto.BookRequestDTO;
import com.gflopes.bookstoremanager.book.dto.BookResponseDTO;
import com.gflopes.bookstoremanager.user.dto.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "books", description = "Books Management")
public interface BookControllerDocs {

    @Operation(summary = "Books create operation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success created book"),
            @ApiResponse(responseCode = "400", description = "Missing required field(s) or an error on validation book rules system")
    })
    BookResponseDTO create(AuthenticatedUser authenticatedUser, BookRequestDTO bookRequestDTO);

    @Operation(summary = "Find book by Id and User operation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success book found"),
            @ApiResponse(responseCode = "404", description = "Book not found error code")
    })
    BookResponseDTO findByIdAndUser(AuthenticatedUser authenticatedUser, Long bookId);

    @Operation(summary = "List all books by a specify authenticated user operation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book list found authenticated user informed")
    })
    List<BookResponseDTO> findAllByUser(AuthenticatedUser authenticatedUser);

    @Operation(summary = "Delete book by Id and User operation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success book deleted"),
            @ApiResponse(responseCode = "404", description = "Book not found error code")
    })
    void deleteByIdAndUser(AuthenticatedUser authenticatedUser, Long bookId);

    @Operation(summary = "Books update operation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success updated book"),
            @ApiResponse(responseCode = "404", description = "Book not found error code"),
            @ApiResponse(responseCode = "400", description = "Missing required field(s) or an error on validation book rules system")
    })
    BookResponseDTO updateByIdAndUser(AuthenticatedUser authenticatedUser,
                                      Long bookId,
                                      BookRequestDTO bookRequestDTO);
}
