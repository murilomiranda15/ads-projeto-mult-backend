package br.com.senaisp.bauru.prjCtrlFinanceiro.services;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.senaisp.bauru.prjCtrlFinanceiro.models.Lancamento;
import br.com.senaisp.bauru.prjCtrlFinanceiro.repositories.LancamentoRepository;

@Service
public class LancamentoService {
    private final LancamentoRepository repository;

    public LancamentoService(LancamentoRepository repository) {
        this.repository = repository;
    }

    public List<Lancamento> listar() {
        return repository.findAll();
    }

    public Lancamento salvar(Lancamento lancamento) {
        return repository.save(lancamento);
    }
    
    public Lancamento buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }
    
    public Lancamento atualizar(Long id, Lancamento lancamento) {
        Lancamento lancamentoBanco = repository.findById(id).orElse(null);
        if (lancamentoBanco == null) {
            return null;
        }
        lancamentoBanco.setDescricao(lancamento.getDescricao());
        lancamentoBanco.setValor(lancamento.getValor());
        lancamentoBanco.setDataLancamento(lancamento.getDataLancamento());
        lancamentoBanco.setObservacao(lancamento.getObservacao());
        lancamentoBanco.setCategoria(lancamento.getCategoria());
        // Se já tiver adicionado o usuário no model, descomente a linha abaixo:
        // lancamentoBanco.setUsuario(lancamento.getUsuario());
        
        return repository.save(lancamentoBanco);
    }

    public boolean excluir(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}