function parseNotas(texto) {
  return texto
    .split(/[\n,]+/)
    .map(s => s.trim())
    .filter(Boolean);
}

function setStatus(msg) {
  document.getElementById('status').textContent = msg || '';
}

function setRetorno(msg, type = 'secondary') {
  const el = document.getElementById('retornoGerar');
  el.className = `alert alert-${type} py-2 mb-0`;
  el.textContent = msg;
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

  const cols = Object.keys(lista[0]);
  thead.innerHTML = '<tr>' + cols.map(c => `<th>${c}</th>`).join('') + '</tr>';

  lista.forEach(item => {
    const tr = document.createElement('tr');
    tr.innerHTML = cols.map(c => `<td>${item[c] ?? ''}</td>`).join('');
    tbody.appendChild(tr);
  });

  setStatus('OK.');
}

async function gerarESalvar() {
  const notasTexto = document.getElementById('notas').value;
  const notas = parseNotas(notasTexto);

  //if (notas.length === 0) {
    //setRetorno('Informe ao menos uma nota.', 'warning');
   // return;
 // }

  setStatus('Enviando...');
  const res = await fetch('observacoes/gerar', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ notas })
  });

  const texto = await res.text();
  setRetorno(texto || '(vazio)', res.ok ? 'success' : 'danger');

  if (!res.ok) {
    setStatus('Falhou.');
    return;
  }

  document.getElementById('notas').value = '';
  await carregarLista();
}

document.getElementById('btnGerar').addEventListener('click', () => {
  gerarESalvar().catch(e => { setStatus('Erro'); setRetorno(e.message, 'danger'); });
});

document.getElementById('btnRecarregar').addEventListener('click', () => {
  carregarLista().catch(e => { setStatus('Erro'); setRetorno(e.message, 'danger'); });
});

// primeira carga
carregarLista().catch(e => { setStatus('Erro'); setRetorno(e.message, 'danger'); });
