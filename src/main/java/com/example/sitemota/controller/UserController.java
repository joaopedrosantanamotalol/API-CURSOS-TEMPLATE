package com.example.sitemota.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.sitemota.dto.UserFilter;
import com.example.sitemota.dto.UserRequest;
import com.example.sitemota.dto.UserResponse;
import com.example.sitemota.service.UserService;



@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(Authentication authentication) {
      UserFilter userF = (UserFilter) authentication.getPrincipal();
    UserResponse user = service.buscarPorId(userF.id()); 
    return ResponseEntity.ok(user);
    }

    // CREATE
    @PostMapping
    public UserResponse criar(@RequestBody UserRequest request){
        return service.criar(request);
    }

    // LIST
    @GetMapping
    public List<UserResponse> listar(){
        return service.listar();
    }

    // FIND BY ID
    @GetMapping("/{id}")
    public UserResponse buscarPorId(@PathVariable Long id){
        return service.buscarPorId(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public UserResponse atualizar(@PathVariable Long id, @RequestBody UserRequest request){
        return service.atualizar(id, request);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id){
        service.deletar(id);
    }
}