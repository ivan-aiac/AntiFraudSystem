package aiac.antifraudsystem.service;

import aiac.antifraudsystem.dto.*;
import aiac.antifraudsystem.enums.AppRole;
import aiac.antifraudsystem.model.AppUser;
import aiac.antifraudsystem.model.AppUserDetails;
import aiac.antifraudsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class LoginService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ResponseEntity<?> addUser(UserDTO userDTO) {
        if (repository.existsByUserDetailsUsernameIgnoreCase(userDTO.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        AppUser user = userDTO.mapToAppUser();
        AppUserDetails userDetails = user.getUserDetails();
        userDetails.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        if (repository.count() == 0) {
            userDetails.setRole(AppRole.ADMINISTRATOR);
            userDetails.setNonLocked(true);
        }
        user = repository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserDTO.of(user));
    }

    public List<UserDTO> findAllUsers() {
        return repository.findAllByOrderById().stream()
                .map(UserDTO::of)
                .toList();
    }

    @Transactional
    public ResponseEntity<?> deleteByUsername(String username) {
        AppUser user = findUserByUsernameOrThrowNotFound(username);
        repository.delete(user);
        return ResponseEntity.ok(new DeleteUserResponse(username, "Deleted successfully!"));
    }

    public ResponseEntity<?> changeUserRole(RoleChangeRequest request) {
        if (request.getRole() == AppRole.ADMINISTRATOR) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        AppUser user = findUserByUsernameOrThrowNotFound(request.getUsername());
        if (user.getUserDetails().getRole() == request.getRole()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        user.getUserDetails().setRole(request.getRole());
        repository.save(user);
        return ResponseEntity.ok(UserDTO.of(user));
    }

    public ResponseEntity<?> changeUserLockedStatus(LockedStatusRequest request) {
        AppUser user = findUserByUsernameOrThrowNotFound(request.getUsername());
        if (user.getUserDetails().getRole() == AppRole.ADMINISTRATOR) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        boolean unlocked = request.getOperation().getValue();
        user.getUserDetails().setNonLocked(unlocked);
        repository.save(user);
        SimpleResponse response = new SimpleResponse("User %s %s!"
                .formatted(user.getUserDetails().getUsername(), unlocked ? "unlocked" : "locked"));
        return ResponseEntity.ok(response);
    }

    private AppUser findUserByUsernameOrThrowNotFound(String username) {
        return repository.findByUserDetailsUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
