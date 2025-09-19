package org.example.xmlapidemo.controller;

import jakarta.validation.Valid;
import org.example.xmlapidemo.dto.ApiResponse;
import org.example.xmlapidemo.dto.UsersResponse;
import org.example.xmlapidemo.entity.User;
import org.example.xmlapidemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * GET - Get all users
     * URL: http://localhost:8080/api/users
     */
    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<UsersResponse> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            UsersResponse response = new UsersResponse(users);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new UsersResponse(List.of()));
        }
    }

    /**
     * GET - Get user by ID
     * URL: http://localhost:8080/api/users/{id}
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            Optional<User> user = userService.getUserById(id);
            if (user.isPresent()) {
                return ResponseEntity.ok(user.get()); // Return User directly
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("User not found with id: " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving user: " + e.getMessage()));
        }
    }

    /**
     * GET - Search users by name
     * URL: http://localhost:8080/api/users/search?name=john
     */
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<UsersResponse> searchUsers(@RequestParam String name) {
        try {
            List<User> users = userService.searchUsersByName(name);
            UsersResponse response = new UsersResponse(users);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new UsersResponse(List.of()));
        }
    }

    /**
     * GET - Get active users only
     * URL: http://localhost:8080/api/users/active
     */
    @GetMapping(value = "/active", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<UsersResponse> getActiveUsers() {
        try {
            List<User> users = userService.getActiveUsers();
            UsersResponse response = new UsersResponse(users);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new UsersResponse(List.of()));
        }
    }

    /**
     * POST - Create new user
     * URL: http://localhost:8080/api/users
     * Content-Type: application/xml
     * FIXED: Return User directly instead of ApiResponse<User>
     */
    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE,
            produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error creating user: " + e.getMessage()));
        }
    }

    /**
     * PUT - Update user completely
     * URL: http://localhost:8080/api/users/{id}
     * Content-Type: application/xml
     * FIXED: Return User directly instead of ApiResponse<User>
     */
    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_XML_VALUE,
            produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                        @Valid @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error updating user: " + e.getMessage()));
        }
    }

    /**
     * PATCH - Partially update user
     * URL: http://localhost:8080/api/users/{id}
     * Content-Type: application/xml
     * FIXED: Return User directly instead of ApiResponse<User>
     */
    @PatchMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_XML_VALUE,
            produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> partialUpdateUser(@PathVariable Long id,
                                               @RequestBody User userDetails) {
        try {
            User updatedUser = userService.partialUpdateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error updating user: " + e.getMessage()));
        }
    }

    /**
     * DELETE - Delete user (with authentication)
     * URL: http://localhost:8080/api/users/{id}
     * Requires: Authorization header with Basic auth (username: admin, password: admin123)
     * FIXED: Create a simple response without generics + Authentication
     */
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> deleteUser(@PathVariable Long id,
                                        @RequestHeader(value = "Authorization", required = false) String authHeader) {

        // Check authentication
        if (!isAuthenticated(authHeader)) {
            ApiResponse response = new ApiResponse();
            response.setStatus("error");
            response.setMessage("Unauthorized. Please provide valid credentials.");
            response.setTimestamp(java.time.LocalDateTime.now().toString());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            userService.deleteUser(id);
            // Create a simple success response without using generic ApiResponse
            ApiResponse response = new ApiResponse();
            response.setStatus("success");
            response.setMessage("User with ID " + id + " has been deleted successfully");
            response.setTimestamp(java.time.LocalDateTime.now().toString());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error deleting user: " + e.getMessage()));
        }
    }

    /**
     * Helper method to validate Basic Authentication
     * Expected credentials: username = "admin", password = "admin123"
     */
    private boolean isAuthenticated(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            return false;
        }

        try {
            // Extract the base64 encoded credentials
            String base64Credentials = authHeader.substring(6); // Remove "Basic "
            byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(decodedBytes, StandardCharsets.UTF_8);

            // Expected format: "username:password"
            String[] parts = credentials.split(":", 2);
            if (parts.length != 2) {
                return false;
            }

            String username = parts[0];
            String password = parts[1];

            // Simple hardcoded authentication (for demo purposes)
            // In production, use proper authentication service/database
            return "admin".equals(username) && "admin123".equals(password);

        } catch (Exception e) {
            return false;
        }
    }
}