package br.com.senaisp.bauru.prjCtrlFinanceiro.dtos;

public record LoginResponse(String token, String tipo, long expiraEmSegundos) {}