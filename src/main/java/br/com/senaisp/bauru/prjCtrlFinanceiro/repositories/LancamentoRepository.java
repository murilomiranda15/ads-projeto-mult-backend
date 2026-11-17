package br.com.senaisp.bauru.prjCtrlFinanceiro.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.senaisp.bauru.prjCtrlFinanceiro.models.Lancamento;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
    List<Lancamento> findByUsuarioId(Long usuarioId);
    Optional<Lancamento> findByIdAndUsuarioId(Long id, Long usuarioId);
}