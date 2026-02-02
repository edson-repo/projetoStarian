function parseNotas(texto) {
  const linhas = texto
    .split(/\n+/)
    .map(s => s.trim())
    .filter(Boolean);

  const notas = [];
  const erros = [];

  linhas.forEach((linha, idx) => {
    // aceita "numero;valor" ou "numero,valor"
    const partes = linha.split(/[;,]/).map(p => p.trim()).filter(Boolean);

    if (partes.length < 2) {
      erros.push(`Linha ${idx + 1}: use o formato numero;valor (ex: 123; 99,90)`);
      return;
    }

    const numero = Number(partes[0].replace(/\D+/g, '')); // só dígitos
    const valorStr = partes[1];

    // aceita "99,90" ou "99.90" e também "1.234,56"
    const valor = Number(valorStr.replace(/\./g, '').replace(',', '.')); // "1.234,56" -> "1234.56"

    if (!Number.isFinite(numero) || numero <= 0) {
      erros.push(`Linha ${idx + 1}: número inválido ("${partes[0]}")`);
      return;
    }

    if (!Number.isFinite(valor) || valor < 0) {
      erros.push(`Linha ${idx + 1}: valor inválido ("${partes[1]}")`);
      return;
    }

    notas.push({ numero, valor });
  });

  return { notas, erros };
}

function setStatus(msg) {
  document.getElementById('status').textContent = msg || '';
}

function setRetorno(msg, type = 'secondary') {
  const el = document.getElementById('retornoGerar');
  el.className = `alert alert-${type} py-2 mb-0`;
  el.textContent = msg;
}

function formatarData(iso) {
  // iso tipo: "2026-02-02T09:54:09.31"
  if (!iso) return '';
  try {
    const d = new Date(iso);
    if (Number.isNaN(d.getTime())) return iso;
    return d.toLocaleString('pt-BR');
  } catch {
    return iso;
  }
}

async function carregarLista() {
  setStatus('Carregando lista...');

  const res = await fetch('observacoes/getAll');
  if (!res.ok) {
    const err = await res.text();
    setStatus('');
    throw new Error('Erro no getAll: ' + err);
  }

  const lista = await res.json();

  document.getElementById('count').textContent = Array.isArray(lista) ? lista.length : 0;

  const thead = document.getElementById('thead');
  const tbody = document.getElementById('tbody');
  thead.innerHTML = '';
  tbody.innerHTML = '';

  if (!lista || lista.length === 0) {
    thead.innerHTML = '<tr><th>Nenhum registro</th></tr>';
    setStatus('Lista vazia.');
    return;
  }

  // Colunas que você quer mostrar (na ordem)
  // ObservacaoResponse atual: id, entrada, saida, dataCriacao
  const cols = ['id', 'entrada', 'saida', 'dataCriacao'];

  thead.innerHTML = '<tr>' + cols.map(c => `<th>${c}</th>`).join('') + '</tr>';

  lista.forEach(item => {
    const tr = document.createElement('tr');

    const id = item.id ?? '';
    const entrada = item.entrada ?? '';
    const saida = item.saida ?? '';
    const dataCriacao = formatarData(item.dataCriacao);

    tr.innerHTML = `
      <td>${id}</td>
      <td>${entrada}</td>
      <td>${saida}</td>
      <td>${dataCriacao}</td>
    `;

    tbody.appendChild(tr);
  });

  setStatus('OK.');
}

async function gerarESalvar() {
  const notasTexto = document.getElementById('notas').value;
  const { notas, erros } = parseNotas(notasTexto);

  if (erros.length > 0) {
    setRetorno(erros.join('\n'), 'warning');
    setStatus('Erro.');
    return;
  }

  if (notas.length === 0) {
    setRetorno('Informe ao menos uma nota no formato numero;valor', 'warning');
    setStatus('');
    return;
  }

  setStatus('Enviando...');

  const res = await fetch('observacoes/gerarNotas', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    // ✅ array direto porque o backend espera List<NotaFiscal>
    body: JSON.stringify(notas)
  });

  const texto = await res.text();
  setRetorno(texto || '(vazio)', res.ok ? 'success' : 'danger');

  if (!res.ok) {
    setStatus('Falhou.');
    return;
  }

  document.getElementById('notas').value = '';

  // ✅ importante: se o getAll falhar, NÃO apaga o retorno do POST
  try {
    await carregarLista();
  } catch (e) {
    console.error(e);
    setStatus('Salvou, mas falhou ao atualizar a lista.');
    // não chama setRetorno aqui!
  }
}

document.getElementById('btnGerar').addEventListener('click', () => {
  gerarESalvar().catch(e => {
    setStatus('Erro');
    setRetorno(e.message, 'danger');
  });
});

// primeira carga
carregarLista().catch(e => {
  setStatus('Erro');
  setRetorno(e.message, 'danger');
});
