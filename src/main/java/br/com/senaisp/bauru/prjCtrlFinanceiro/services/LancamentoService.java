package br.com.senaisp.bauru.prjCtrlFinanceiro.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.senaisp.bauru.prjCtrlFinanceiro.dtos.LancamentoRequest;
import br.com.senaisp.bauru.prjCtrlFinanceiro.dtos.LancamentoResponse;
import br.com.senaisp.bauru.prjCtrlFinanceiro.exceptions.RecursoNaoEncontradoException;
import br.com.senaisp.bauru.prjCtrlFinanceiro.models.Categoria;
import br.com.senaisp.bauru.prjCtrlFinanceiro.models.Lancamento;
import br.com.senaisp.bauru.prjCtrlFinanceiro.models.Usuario;
import br.com.senaisp.bauru.prjCtrlFinanceiro.repositories.CategoriaRepository;
import br.com.senaisp.bauru.prjCtrlFinanceiro.repositories.LancamentoRepository;
import br.com.senaisp.bauru.prjCtrlFinanceiro.repositories.UsuarioRepository;

@Service
public class LancamentoService {

    private final LancamentoRepository lancamentos;
    private final UsuarioRepository usuarios;
    private final CategoriaRepository categorias;

    public LancamentoService(LancamentoRepository lancamentos,
            UsuarioRepository usuarios, CategoriaRepository categorias) {
        this.lancamentos = lancamentos;
        this.usuarios = usuarios;
        this.categorias = categorias;
    }

    @Transactional
    public LancamentoResponse cadastrar(Long usuarioId, LancamentoRequest dados) {
        if (dados == null || dados.descricao() == null || dados.descricao().isBlank()
                || dados.valor() == null
                || dados.valor().compareTo(BigDecimal.ZERO) <= 0
                || dados.dataLancamento() == null || dados.categoriaId() == null) {
            throw new IllegalArgumentException("Dados obrigatórios inválidos");
        }
        Usuario usuario = usuarios.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
        Categoria categoria = categorias.findById(dados.categoriaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada"));
        if (!Boolean.TRUE.equals(categoria.getAtiva())) {
            throw new IllegalArgumentException("Categoria inativa!");
        }

        Lancamento l = new Lancamento();
        l.setUsuario(usuario);
        l.setCategoria(categoria);
        l.setDescricao(dados.descricao().trim());
        l.setValor(dados.valor());
        l.setDataLancamento(dados.dataLancamento());
        l.setObservacao(dados.observacao());
        return converter(lancamentos.save(l));
    }

    @Transactional(readOnly = true)
    public List<LancamentoResponse> listar(Long usuarioId) {
        if (!usuarios.existsById(usuarioId)) {
            throw new RecursoNaoEncontradoException("Usuário não encontrado");
        }
        return lancamentos.findByUsuarioId(usuarioId).stream()
                .map(this::converter)
                .toList();
    }

    @Transactional(readOnly = true)
    public LancamentoResponse buscarPorIdAndUsuarioId(Long id, Long usuarioId) {
        return lancamentos.findByIdAndUsuarioId(id, usuarioId)
                .map(this::converter)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Lançamento não encontrado"));
    }

    @Transactional
    public LancamentoResponse alterar(Long usuarioId, Long id, LancamentoRequest dados) {
        if (dados == null) {
            throw new IllegalArgumentException("Dados obrigatórios inválidos");
        }
        Lancamento l = lancamentos.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Lançamento não encontrado"));

        if (dados.categoriaId() != null) {
            Categoria categoria = categorias.findById(dados.categoriaId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada"));
            if (!Boolean.TRUE.equals(categoria.getAtiva())) {
                throw new IllegalArgumentException("Categoria inativa!");
            }
            l.setCategoria(categoria);
        }
        if (dados.descricao() != null) {
            if (dados.descricao().isBlank()) {
                throw new IllegalArgumentException("Descrição inválida");
            }
            l.setDescricao(dados.descricao().trim());
        }
        if (dados.valor() != null) {
            if (dados.valor().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Valor deve ser maior que zero");
            }
            l.setValor(dados.valor());
        }
        if (dados.dataLancamento() != null) {
            l.setDataLancamento(dados.dataLancamento());
        }
        if (dados.observacao() != null) {
            l.setObservacao(dados.observacao());
        }

        return converter(lancamentos.save(l));
    }

    @Transactional
    public void excluir(Long usuarioId, Long id) {
        Lancamento l = lancamentos.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Lançamento não encontrado"));
        lancamentos.delete(l);
    }

    private LancamentoResponse converter(Lancamento l) {
        return new LancamentoResponse(
                l.getId(),
                l.getDescricao(),
                l.getValor(),
                l.getDataLancamento(),
                l.getObservacao(),
                l.getCategoria().getId(),
                l.getCategoria().getDescricao(),
                l.getUsuario().getId());
    }
}