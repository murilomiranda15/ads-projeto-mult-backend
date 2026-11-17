package br.com.senaisp.bauru.prjCtrlFinanceiro.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LancamentoRequest(
	    String descricao,
	    BigDecimal valor,
	    LocalDate dataLancamento,
	    String observacao,
	    Long categoriaId
) {}