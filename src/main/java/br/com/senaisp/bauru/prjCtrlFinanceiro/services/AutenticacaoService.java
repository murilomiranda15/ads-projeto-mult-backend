package br.com.senaisp.bauru.prjCtrlFinanceiro.services;


import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import br.com.senaisp.bauru.prjCtrlFinanceiro.dtos.LoginRequest;
import br.com.senaisp.bauru.prjCtrlFinanceiro.dtos.LoginResponse;
import br.com.senaisp.bauru.prjCtrlFinanceiro.models.Usuario;
import br.com.senaisp.bauru.prjCtrlFinanceiro.repositories.UsuarioRepository;

@Service
public class AutenticacaoService {
  private final UsuarioRepository usuarios;
  private final PasswordEncoder encoder;
  private final JwtEncoder jwtEncoder;
  private final String issuer;
  private final long minutos;
 
  public AutenticacaoService(UsuarioRepository usuarios,
      PasswordEncoder encoder, JwtEncoder jwtEncoder,
      @Value("${app.jwt.issuer}") String issuer,
      @Value("${app.jwt.expiration-minutes}") long minutos) {
    this.usuarios = usuarios; this.encoder = encoder;
    this.jwtEncoder = jwtEncoder; this.issuer = issuer; this.minutos = minutos;
  }
 
  public LoginResponse login(LoginRequest req) {
    if (req == null || req.email() == null || req.senha() == null)
      throw new BadCredentialsException("Credenciais inválidas");
    Usuario u = usuarios.findByEmailIgnoreCase(req.email().trim())
      .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));
    if (!Boolean.TRUE.equals(u.getAtivo()) ||
        !encoder.matches(req.senha(), u.getSenhaHash()))
      throw new BadCredentialsException("Credenciais inválidas");
    Instant agora = Instant.now();
     JwtClaimsSet claims = JwtClaimsSet.builder()
       .issuer(issuer).issuedAt(agora)
       .expiresAt(agora.plus(minutos, ChronoUnit.MINUTES))
       .subject(u.getId().toString()).build();
     JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
     String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims))
       .getTokenValue();
     return new LoginResponse(token, "Bearer", minutos * 60);
   }
 }    