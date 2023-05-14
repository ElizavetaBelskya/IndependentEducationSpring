package ru.kpfu.itis.belskaya.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.belskaya.models.*;
import ru.kpfu.itis.belskaya.models.dto.OrderDto;

import java.util.List;

@Tags(value = {
        @Tag(name = "Orders")
})
@SecurityRequirement(name = "Authentication")
@RequestMapping("/api/orders")
public interface OrderApi {

    @GetMapping("/{id}")
    @Operation(summary = "Get an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDto.class))
            }),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    ResponseEntity<OrderDto> getOrder(@PathVariable("id") Long id);

    @PostMapping()
    @Operation(summary = "Add an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Order added"),
            @ApiResponse(responseCode = "403", description = "You have no rights to add order")
    })
    ResponseEntity addOrder(@AuthenticationPrincipal Account account, @RequestBody OrderDto orderDto);

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Operation(summary = "Delete an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order deleted"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode =  "403", description = "You have no rights to add order")
    })
    ResponseEntity<String> deleteOrder(
            @AuthenticationPrincipal Account account,
            @PathVariable("id") Long id
    );

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Operation(summary = "Update an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order updated"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode =  "403", description = "You have no rights to add order"),
            @ApiResponse(responseCode = "405", description = "Validation error"),
    })
    ResponseEntity<String> updateOrder(@AuthenticationPrincipal Account account,
            @PathVariable("id") Long id,
            @RequestBody OrderDto orderDto
    );

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('TUTOR')")
    @Operation(summary = "Add a tutor to an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tutor added"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode =  "403", description = "You have no rights to add tutor to order")
    })
    ResponseEntity<String> addTutorToOrder(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal Account account
    );

    @Operation(summary = "Get all orders of a student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of orders",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDto.class))
                    }),
            @ApiResponse(responseCode =  "403", description = "You have no rights to get orders")
    })
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('STUDENT')")
    ResponseEntity<List<OrderDto>> getAllOrders(
            @AuthenticationPrincipal Account account
    );

    @Operation(summary = "Get all subjects")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of subjects",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Subject.class))
                    })
    })
    @GetMapping("/subjects")
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<List<Subject>> getAllSubjects();


}
