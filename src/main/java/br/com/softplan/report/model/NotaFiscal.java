package br.com.softplan.report.model;

import java.math.BigDecimal;

public class NotaFiscal {

	private Long numero;
	private BigDecimal valor;

	// Necessário para o Jackson desserializar JSON
	public NotaFiscal() {
	}

	public NotaFiscal(Long numero, BigDecimal valor) {
		this.numero = numero;
		this.valor = valor;
	}

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
}
