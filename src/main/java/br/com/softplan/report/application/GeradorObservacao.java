package br.com.softplan.report.application;

import java.util.Iterator;
import java.util.List;

public class GeradorObservacao { 

	//Textos pré-definidos
	static final String umoNota = "Fatura da nota fiscal de simples remessa: ";
	//Identificador da entidade
	String t;
		
	//Gera observações, com texto pre-definido, incluindo os números, das notas fiscais, recebidos no parâmetro
	public String geraObservacao(List args1)
	{
		t = "";
		if (!args1.isEmpty())
		{
			return retornaCodigos(args1) + ".";
		}		
		return "";		
	}

	//Cria observação
	private String retornaCodigos(List lista2) {
		if (lista2.size() >= 2) {
			t = "Fatura das notas fiscais de simples remessa: ";
		} else {
			t = umoNota;
		}
		
		//Acha separador
		StringBuilder c = new StringBuilder();
		for (Iterator<Integer> iterator=lista2.iterator();iterator.hasNext();) {
			Integer t2 = iterator.next();
			String s = "";
			if( c.toString() == null || c.toString().length() <= 0 )
				s =  "";
				else if( iterator.hasNext() )
					s =  ", ";
				else
					s =  " e ";
			
			c.append(s + t2);
		}
		
		return t + c;
	}
}