// package com.example.spring.university_portal.bootstrap;

// import java.util.Arrays;
// import java.util.Map;
// import java.util.Optional;

// import org.springframework.context.ApplicationListener;
// import org.springframework.context.event.ContextRefreshedEvent;
// import org.springframework.stereotype.Component;

// import com.example.spring.university_portal.helpers.RoleEnum;
// import com.example.spring.university_portal.models.Role;
// import com.example.spring.university_portal.repositories.RoleRepository;

// @Component
// public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent> {
//     private final RoleRepository roleRepository;

//     public RoleSeeder(RoleRepository roleRepository) {
//         this.roleRepository = roleRepository;
//     }

//     @Override
//     public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
//         this.loadRoles();
//     }

//     private void loadRoles() {
//         RoleEnum[] roleNames = new RoleEnum[] { RoleEnum.STUDENT, RoleEnum.ADMIN, RoleEnum.STAFF };

//         Map<RoleEnum, String> roleDescriptionMap = Map.of(
//                 RoleEnum.STUDENT, "a student can only view his own details",
//                 RoleEnum.ADMIN, "can view/edit/update all details",
//                 RoleEnum.STAFF, "can view all details");

//         Arrays.stream(roleNames).forEach((roleName) -> {
//             Optional<Role> optionalRole = roleRepository.findByRole(roleName);

//             optionalRole.ifPresentOrElse(
//                     System.out::println,
//                     () -> System.out.println(roleName + " role not found"));
//         });
//     }
// }
