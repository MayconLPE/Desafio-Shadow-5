package com.shadow.desafio.controllers;

import com.shadow.desafio.dtos.UsuariosDto;
import com.shadow.desafio.entities.Usuarios;
import com.shadow.desafio.exceptions.UsuariosExceptions;
import com.shadow.desafio.service.UsuariosService;
import com.shadow.desafio.util.ValidarCPF;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("usuario")
@CrossOrigin(origins = "*", maxAge = 3600)/* Permitir acesso a qualquer fonte*/
@Data
public class UsuariosController {

    final UsuariosService usuariosService;

    @PostMapping(value = "/salvar")
    public ResponseEntity<Object> salvarUsuarios(@RequestBody Usuarios usuarios) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuariosService.save(usuarios));
    }
    @GetMapping(value = "/listartodos")
    public ResponseEntity<Object> listarTodos(){
        return ResponseEntity.status(HttpStatus.OK).body(usuariosService.findAll());
    }
    @GetMapping(value = "/buscarid") /* Com RequestParam */
    public ResponseEntity<Usuarios> buscarID(@RequestParam(name = "codigoid") UUID codigoID) {
        Usuarios usuarios = usuariosService.findById(codigoID);
        return new ResponseEntity<Usuarios>(usuarios, HttpStatus.OK);
    }
    @GetMapping(value = "/{codigoID}") /* Passando a Variavel */
    public ResponseEntity<Usuarios> buscarID2(@PathVariable UUID codigoID) {
            Usuarios usuarios = usuariosService.findById(codigoID);
            return new ResponseEntity<Usuarios>(usuarios, HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizar(@PathVariable(value = "id") UUID codigoID,
                                                    @RequestBody @Valid UsuariosDto usuariosDto){
        Optional<Usuarios> usuariosOptional = Optional.ofNullable(usuariosService.findById(codigoID));
        if (!usuariosOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }
        var usuarios = new Usuarios();
        BeanUtils.copyProperties(usuariosDto, usuarios); /* conversão de Dto para entity*/
        usuarios.setCodigoID(usuariosOptional.get().getCodigoID()); /* Setando Id para permanecer o mesmo */
        return ResponseEntity.status(HttpStatus.OK).body(usuariosService.save(usuarios));
    }
    @DeleteMapping("/{codigoID}")
    public ResponseEntity<Object> delete2(@PathVariable(value = "codigoID") UUID codigoID){
        Optional<Usuarios> usuariosOptional = Optional.ofNullable(usuariosService.findById(codigoID));
        if (!usuariosOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id não encontrado.");
        }
        usuariosService.delete(usuariosOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Usuário deletado com sucesso.");
    }
}
