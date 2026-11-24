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

    private Long extrairUsuarioId(Jwt jwt) {
        return Long.valueOf(jwt.getSubject());
    }

    @GetMapping
    public List<LancamentoResponse> listar(@AuthenticationPrincipal Jwt jwt) {
        return service.listar(extrairUsuarioId(jwt));
    }

    @PostMapping
    public ResponseEntity<LancamentoResponse> cadastrar(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody LancamentoRequest dados) {
        LancamentoResponse criado = service.cadastrar(extrairUsuarioId(jwt), dados);
        return ResponseEntity.status(201).body(criado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LancamentoResponse> buscarPorId(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorIdAndUsuarioId(id, extrairUsuarioId(jwt)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LancamentoResponse> atualizar(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id,
            @RequestBody LancamentoRequest dados) {
        return ResponseEntity.ok(service.alterar(extrairUsuarioId(jwt), id, dados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id) {
        service.excluir(extrairUsuarioId(jwt), id);
        return ResponseEntity.noContent().build();
    }
}