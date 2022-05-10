package com.gflopes.bookstoremanager.author.controller.docs;

import com.gflopes.bookstoremanager.author.dto.AuthorDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "author", description = "Author Management")
public interface AuthorControllerDocs {

    @Operation(summary = "Authors create operation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success created author"),
            @ApiResponse(responseCode = "400", description = "Missing required field(s), wrong field(s) range value or author already registered on system")
    })
    AuthorDTO create(AuthorDTO authorDTO);

    @Operation(summary = "Find author by Id operation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success author found"),
            @ApiResponse(responseCode = "404", description = "Author not found error code")
    })
    AuthorDTO findById(@PathVariable Long id);

    @Operation(summary = "List all registered authors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return all registered authors"),
            @ApiResponse(responseCode = "404", description = "Author not found error code")
    })
    List<AuthorDTO> findAll();

    @Operation(summary = "Delete author by Id operation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success author deleted"),
            @ApiResponse(responseCode = "404", description = "Author not found error code")
    })
    void delete(@PathVariable Long id);
}
