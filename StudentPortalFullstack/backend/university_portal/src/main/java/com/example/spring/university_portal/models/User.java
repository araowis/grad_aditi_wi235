// package com.example.spring.university_portal.models;

// import java.util.UUID;

// import jakarta.persistence.CascadeType;
// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;
// import jakarta.persistence.Table;

// @Entity
// @Table(name = "users")
// public class User {
//     @Id
//     @Column(name = "user_id")
//     private Integer user_uuid;

//     @Column(name = "user_name")
//     private String username;

//     private String fullName;

//     private String email;

//     @Column(name = "password_hash")
//     private String password;

//     @ManyToOne(cascade = CascadeType.REMOVE)
//     @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
//     private Role role;

//     public User() {}

//     public User(String username, String fullName, String email, String password, Role role) {
//         this.username = username;
//         this.fullName = fullName;
//         this.email = email;
//         this.password = password;
//         this.role = role;
//     }

//     public String getUsername() {
//         return username;
//     }

//     public void setUsername(String username) {
//         this.username = username;
//     }

//     public String getFullName() {
//         return fullName;
//     }

//     public void setFullName(String fullName) {
//         this.fullName = fullName;
//     }

//     public String getEmail() {
//         return email;
//     }

//     public void setEmail(String email) {
//         this.email = email;
//     }

//     public String getPassword() {
//         return password;
//     }

//     public void setPassword(String password) {
//         this.password = password;
//     }

//     public Role getRole() {
//         return role;
//     }

//     public void setRole(Role role) {
//         this.role = role;
//     }

// }
