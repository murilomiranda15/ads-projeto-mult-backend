package br.com.senaisp.bauru.prjCtrlFinanceiro.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.senaisp.bauru.prjCtrlFinanceiro.dtos.LoginRequest;
import br.com.senaisp.bauru.prjCtrlFinanceiro.dtos.LoginResponse;
import br.com.senaisp.bauru.prjCtrlFinanceiro.services.AutenticacaoService;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private final AutenticacaoService service;
  public AuthController(AutenticacaoService service) { this.service = service; }
  @PostMapping("/login")
  public LoginResponse login(@RequestBody LoginRequest req) {
    return service.login(req);
  }
}
