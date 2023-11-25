package aiac.antifraudsystem.controller;

import aiac.antifraudsystem.dto.LockedStatusRequest;
import aiac.antifraudsystem.dto.RoleChangeRequest;
import aiac.antifraudsystem.dto.UserDTO;
import aiac.antifraudsystem.service.LoginService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final LoginService service;

    @Autowired
    public AuthController(LoginService service) {
        this.service = service;
    }

    @PostMapping("/user")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO user) {
        return service.addUser(user);
    }

    @GetMapping("/list")
    public @ResponseBody List<UserDTO> findAllUsers() {
        return service.findAllUsers();
    }

    @DeleteMapping("/user/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable @Valid @NotBlank String username) {
        return service.deleteByUsername(username);
    }

    @PutMapping("/role")
    public ResponseEntity<?> changeUserRole(@RequestBody @Valid RoleChangeRequest roleChangeRequest) {
        return service.changeUserRole(roleChangeRequest);
    }

    @PutMapping("/access")
    public ResponseEntity<?> changeUserLockedStatus(@RequestBody @Valid LockedStatusRequest lockedStatusRequest) {
        return service.changeUserLockedStatus(lockedStatusRequest);
    }

}
