package com.example.sitemota.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.sitemota.dto.LoginRequest;
import com.example.sitemota.dto.LoginResponse;
import com.example.sitemota.dto.UserRequest;
import com.example.sitemota.dto.UserResponse;
import com.example.sitemota.model.User;
import com.example.sitemota.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder config;
    private final JwtService jwtservice;

    public UserService(UserRepository repository, PasswordEncoder config, JwtService jwtservice) {
        this.repository = repository;
        this.config = config;
        this.jwtservice = jwtservice;
    }

    // criar
    public UserResponse criar(UserRequest request){
    User user = toEntity(request);

    User salvo = repository.save(user);
    return toResponse(salvo);
    }

    // Listar
    public List<UserResponse> listar(){
    return repository.findAll()
            .stream()
            .sorted(Comparator.comparing(User::getNome))
            .map(user -> new UserResponse(
                    user.getId(),
                    user.getEmail(),
                    user.getNome(),
                    user.getSexo(),
                    user.getRole()
            ))
            .toList();
}

    // FIND BY ID
    public UserResponse buscarPorId(Long id){
        User user = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Usuario não Encontrado"));
        return toResponse(user);
    }

    public UserResponse findByEmail(String email){
    User user = repository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    return toResponse(user);
}

    // UPDATE
    public UserResponse atualizar(Long id, UserRequest request){

        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        user.setNome(request.nome());
        user.setEmail(request.email());
        user.setSexo(request.sexo());
        user.setSenha(config.encode(request.senha()));

        User atualizado = repository.save(user);

        return toResponse(atualizado);
    }


    // DELETE
    public void deletar(Long id){
        if(!repository.existsById(id)){
            throw new RuntimeException("Usuário não encontrado");
        }
        repository.deleteById(id);
    }


    // LOGIN 
    public LoginResponse login(LoginRequest request){
        User user = repository.findByEmail(request.email())
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    if(!config.matches(request.senha(), user.getSenha())){
        throw new RuntimeException("Senha inválida");
    }

    String token = jwtservice.gerarToken(user.getEmail(), user.getRole());

    return new LoginResponse(token);
    }

    // CONVERSÃO DE ENTIDADE PRA DTO


    private User toEntity(UserRequest request){
        User user = new User();
        user.setNome(request.nome());
        user.setEmail(request.email());
        user.setSexo(request.sexo());
        user.setSenha(config.encode(request.senha()));
        user.setRole(
    request.role() == null ? "USER" : request.role()
);
        return user;
    }
    private UserResponse toResponse(User user){
        return new UserResponse(
            user.getId(),
            user.getEmail(),
            user.getNome(),
            user.getSexo(),
            user.getRole()
        );
    }
    
}
