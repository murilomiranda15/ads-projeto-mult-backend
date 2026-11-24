package br.com.senaisp.bauru.prjCtrlFinanceiro.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.senaisp.bauru.prjCtrlFinanceiro.dtos.LancamentoRequest;
import br.com.senaisp.bauru.prjCtrlFinanceiro.dtos.LancamentoResponse;
import br.com.senaisp.bauru.prjCtrlFinanceiro.services.LancamentoService;

@RestController
@RequestMapping("/lancamentos")
@CrossOrigin(origins = "*")
public class LancamentoController {
    private final LancamentoService service;
    public LancamentoController(LancamentoService service) {
        this.service = service;
    }
    @GetMapping
    public List<LancamentoResponse> listar(@AuthenticationPrincipal Jwt jwt) {
        return service.listar(Long.valueOf(jwt.getSubject()));
    }

    /* TODO: Ajustar os itens abaixo:
       substituindo @PathVariable Long usuarioId por @AuthenticationPrincipal Jwt jwt
       usuarioId por Long.valueOf(jwt.getSubject())
       na atividade autonoma
       */
    
    @PostMapping
    public ResponseEntity<LancamentoResponse> cadastrar(
            @PathVariable Long usuarioId, @RequestBody LancamentoRequest dados) {
        return ResponseEntity.status(201).body(service.cadastrar(usuarioId, dados));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<LancamentoResponse> buscarPorIdAndUsuarioId(
            @PathVariable Long usuarioId, @PathVariable Long id) {
        return ResponseEntity.status(200).body(service.buscarPorIdAndUsuarioId(id, usuarioId));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<LancamentoResponse> atualizar(
    @PathVariable Long usuarioId, @PathVariable Long id, @RequestBody LancamentoRequest dados) {
    return ResponseEntity.status(200).body(service.alterar(usuarioId, id, dados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<LancamentoResponse> apagar(
    @PathVariable Long usuarioId, @PathVariable Long id) {
        boolean excluida = service.excluir(usuarioId, id);
        if (!excluida) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }   
}

