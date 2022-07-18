package com.gflopes.bookstoremanager.users.controller.docs;

import com.gflopes.bookstoremanager.users.dto.MessageDTO;
import com.gflopes.bookstoremanager.users.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Tag(name = "users", description = "Users Management")
public interface UserControllerDocs {

    @Operation(summary = "Users create operation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success created user"),
            @ApiResponse(responseCode = "400", description = "Missing required field(s) or an error on validation user rules system")
    })
    MessageDTO create(UserDTO userToCreateDTO);

    @Operation(summary = "Users update operation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success updated user"),
            @ApiResponse(responseCode = "400", description = "Missing required field(s) or an error on validation user rules system")
    })
    MessageDTO update(@PathVariable Long id, @RequestBody @Valid UserDTO userToUpdateDTO);

    @Operation(summary = "Delete user by Id operation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success user deleted"),
            @ApiResponse(responseCode = "404", description = "User not found error code")
    })
    void delete(@PathVariable Long id);
}
