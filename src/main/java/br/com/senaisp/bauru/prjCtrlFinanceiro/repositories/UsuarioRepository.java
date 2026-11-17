package br.com.senaisp.bauru.prjCtrlFinanceiro.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.senaisp.bauru.prjCtrlFinanceiro.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {}