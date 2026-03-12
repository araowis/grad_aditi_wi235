// package com.example.spring.university_portal.services;

// import org.springframework.stereotype.Service;

// import com.example.spring.university_portal.dataaccess.LoginDTO;
// import com.example.spring.university_portal.dataaccess.SignUpDTO;
// import com.example.spring.university_portal.helpers.RoleEnum;
// import com.example.spring.university_portal.models.*;
// import com.example.spring.university_portal.repositories.RoleRepository;
// import com.example.spring.university_portal.repositories.UserRepository;

// import java.util.List;
// import java.util.Optional;

// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.crypto.password.PasswordEncoder;

// @Service
// public class AuthenticationService {
//     private final UserRepository userRepository;
//     private final RoleRepository roleRepository;
//     private final PasswordEncoder passwordEncoder;
//     private final AuthenticationManager authenticationManager;

//     public AuthenticationService(UserRepository userRepository, RoleRepository roleRepository,
//             PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
//         this.userRepository = userRepository;
//         this.roleRepository = roleRepository;
//         this.passwordEncoder = passwordEncoder;
//         this.authenticationManager = authenticationManager;
//     }

//     public User signup(SignUpDTO input) throws Exception {
//         Optional<Role> optionalRole = roleRepository.findByRole(RoleEnum.STUDENT);

//         Role role = roleRepository.findByRole(RoleEnum.STUDENT)
//         .orElseThrow(() -> new RuntimeException("Default role not found"));

//         User user = new User(
//             input.getUsername(), 
//             input.getFullName(), 
//             input.getEmail(), 
//             passwordEncoder.encode(input.getPassword()), 
//             optionalRole.get());

//         return userRepository.save(user);
//     }

//     public User authenticate(LoginDTO input) {
//         authenticationManager.authenticate(
//             new UsernamePasswordAuthenticationToken(
//                 input.getUsername(),
//                 input.getPassword()
//             )
//         );
//         return userRepository.findByUsername(input.getUsername()).orElseThrow();
//     }

//     public List<User> allUsers() {
//         return userRepository.findAll();
//     }
    
// }
