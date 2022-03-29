package com.gflopes.bookstoremanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    @Operation(summary = "Return an example hello world!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success method return")
    })
    @GetMapping
    public String hello() {
        return "Hello BookStore Manager";
    }
}
