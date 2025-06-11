// --- VARIÁVEIS GLOBAIS ---
// Para a lógica do formulário
let movimentacoesMap = new Map();
// Para a lógica de filtro da tabela
let todosOsLancamentos = [];

// --- INICIALIZAÇÃO ---
document.addEventListener("DOMContentLoaded", async () => {
    // Funções para o formulário
    await carregarContas();
    await carregarMovimentacoesParaSelect();
    
    // Função para a tabela e filtros
    await carregarLancamentos();

    // Eventos para os controles de filtro da tabela
    document.getElementById('filtroColuna').addEventListener('change', atualizarInputDeFiltro);
    document.getElementById('btnFiltrar').addEventListener('click', aplicarFiltro);
    document.getElementById('btnLimparFiltro').addEventListener('click', limparFiltro);

    // Alternância dos campos Receita/Despesa
    document.getElementById('radioReceita').addEventListener('change', function() {
        if (this.checked) {
            document.getElementById('divReceita').style.display = '';
            document.getElementById('divDespesa').style.display = 'none';
            document.getElementById('despesa').value = '';
        }
    });
    document.getElementById('radioDespesa').addEventListener('change', function() {
        if (this.checked) {
            document.getElementById('divReceita').style.display = 'none';
            document.getElementById('divDespesa').style.display = '';
            document.getElementById('receita').value = '';
        }
    });
});


// --- FUNÇÕES DE LÓGICA PARA O FORMULÁRIO (MANTIDAS) ---

async function carregarMovimentacoesParaSelect() {
    const selectMovimentacao = document.getElementById("movimentacaoBancaria");
    const hiddenMovIdInput = document.getElementById("movimentacaoBancariaIdHidden");
    const selectConta = document.getElementById("conta");
    const hiddenContaIdInput = document.getElementById("contaId");

    try {
        const [movResponse, contaResponse, usuarioResponse] = await Promise.all([
            await fetch("http://localhost:8080/apis/movimentacao"),
            await fetch("http://localhost:8080/apis/conta"),
            await fetch("http://localhost:8080/api/usuarios/todos")
        ]);

        if (!movResponse.ok || !contaResponse.ok || !usuarioResponse.ok) {
            throw new Error('Falha ao buscar um ou mais recursos.');
        }

        const movimentacoes = await movResponse.json();
        const contas = await contaResponse.json();
        const usuarios = await usuarioResponse.json();

        
        const contasMap = new Map(contas.map(c => [c.id, c]));
        const usuariosMap = new Map(usuarios.map(u => [u.id, u]));

        selectMovimentacao.innerHTML = '<option value="">Selecione uma movimentação</option>';

        movimentacoes.sort((a, b) => {
            const contaA = contasMap.get(a.contaBancariaId);
            const contaB = contasMap.get(b.contaBancariaId);
            const usuarioA = usuariosMap.get(a.usuarioId);
            const usuarioB = usuariosMap.get(b.usuarioId);

            const nomeAgenciaA = contaA ? `${contaA.banco} Ag: ${contaA.agencia}` : '';
            const nomeAgenciaB = contaB ? `${contaB.banco} Ag: ${contaB.agencia}` : '';
            const nomeUsuarioA = usuarioA ? usuarioA.nome : '';
            const nomeUsuarioB = usuarioB ? usuarioB.nome : '';

            // Ordena por nome da agência, depois por nome do usuário
            const agenciaCompare = nomeAgenciaA.localeCompare(nomeAgenciaB, 'pt-BR');
            if (agenciaCompare !== 0) return agenciaCompare;
            return nomeUsuarioA.localeCompare(nomeUsuarioB, 'pt-BR');
        });

        movimentacoesMap = new Map(movimentacoes.map(m => [m.id, m]));

        movimentacoesMap.forEach(mov => {
            const conta = contasMap.get(mov.contaBancariaId);
            const usuario = usuariosMap.get(mov.usuarioId);
            const nomeAgencia = conta ? `${conta.banco} Ag: ${conta.agencia}` : 'N/A';
            const nomeUsuario = usuario ? usuario.nome : 'N/A';
            const dataMov = mov.data || 'N/A';

            const option = document.createElement('option');
            option.value = mov.id;
            option.textContent = `${nomeAgencia} - ${dataMov} - ${nomeUsuario}`;
            selectMovimentacao.appendChild(option);
        });

        selectMovimentacao.addEventListener('change', (event) => {
            const movimentacaoId = event.target.value;
            hiddenMovIdInput.value = movimentacaoId;
            if (movimentacaoId) {
                const movimentacaoSelecionada = movimentacoesMap.get(parseInt(movimentacaoId));
                if (movimentacaoSelecionada) {
                    const contaId = movimentacaoSelecionada.contaBancariaId;
                    selectConta.value = contaId;
                    hiddenContaIdInput.value = contaId;
                }
            } else {
                selectConta.value = "";
                hiddenContaIdInput.value = "";
            }
        });
    } catch (error) {
        console.error("Erro ao carregar movimentações para o select:", error);
    }
}

async function carregarContas() {
    try {
        const response = await fetch("http://localhost:8080/apis/conta");
        const contas = await response.json();
        const selectConta = document.getElementById("conta");
        selectConta.innerHTML = '<option value="">Conta</option>';
        contas.forEach(conta => {
            const option = document.createElement("option");
            option.value = conta.id;
            option.textContent = `${conta.banco} - Ag: ${conta.agencia} / Conta: ${conta.numero}`;
            selectConta.appendChild(option);
        });
    } catch (error) {
        console.error('Erro ao carregar contas:', error);
    }
}


// --- FUNÇÕES DE LÓGICA PARA A TABELA E FILTROS (ADICIONADAS) ---

function renderizarTabelaLancamentos(lancamentosParaRenderizar) {
    const tbody = document.querySelector("#tabelaLancamentos tbody");
    tbody.innerHTML = "";

    if (!lancamentosParaRenderizar || lancamentosParaRenderizar.length === 0) {
        tbody.innerHTML = '<tr><td colspan="10" class="text-center">Nenhum lançamento encontrado.</td></tr>';
        return;
    }

    lancamentosParaRenderizar.forEach(lanc => {
        tbody.innerHTML += `<tr><td>${lanc.id}</td><td>${lanc.dataLancamento || ""}</td><td>${lanc.descricao}</td><td>${lanc.origem || ""}</td><td>${lanc.destino || ""}</td><td>${lanc.contaBancariaId || ""}</td><td>${lanc.receitaId || ""}</td><td>${lanc.despesaId || ""}</td><td>${lanc.movimentacaoBancariaId || ""}</td><td><button class="btn btn-sm btn-warning" onclick='editarLancamento(${JSON.stringify(lanc)})'>Editar</button><button class="btn btn-sm btn-danger" onclick='excluirLancamento(${lanc.id})'>Excluir</button></td></tr>`;
    });
}

function aplicarFiltro() {
    const coluna = document.getElementById('filtroColuna').value;
    if (!coluna) return;

    let lancamentosFiltrados = todosOsLancamentos;

    if (coluna === 'dataLancamento') {
        const tipo = document.getElementById('filtroTipoData').value;
        if (tipo === 'entre') {
            const inicio = document.getElementById('filtroValorInicio').value;
            const fim = document.getElementById('filtroValorFim').value;
            if (inicio && fim) lancamentosFiltrados = todosOsLancamentos.filter(l => l.dataLancamento && l.dataLancamento >= inicio && l.dataLancamento <= fim);
        } else {
            const valor = document.getElementById('filtroValor').value;
            if (valor) {
                switch (tipo) {
                    case 'exatamente': lancamentosFiltrados = todosOsLancamentos.filter(l => l.dataLancamento === valor); break;
                    case 'apos': lancamentosFiltrados = todosOsLancamentos.filter(l => l.dataLancamento && l.dataLancamento > valor); break;
                    case 'antes': lancamentosFiltrados = todosOsLancamentos.filter(l => l.dataLancamento && l.dataLancamento < valor); break;
                }
            }
        }
    } else if (coluna === 'contaBancariaId') {
        const checks = document.querySelectorAll('.filtro-conta-checkbox:checked');
        if (checks.length > 0) {
            const ids = Array.from(checks).map(cb => cb.value);
            lancamentosFiltrados = todosOsLancamentos.filter(l => ids.includes(String(l.contaBancariaId)));
        }
    } else { // Filtro por palavra-chave
        const keyword = document.getElementById('filtroValor').value.toLowerCase();
        if (keyword) {
            lancamentosFiltrados = todosOsLancamentos.filter(l => l[coluna] && l[coluna].toLowerCase().includes(keyword));
        }
    }
    renderizarTabelaLancamentos(lancamentosFiltrados);
}

function limparFiltro() {
    document.getElementById('filtroColuna').value = '';
    document.getElementById('filtroValorContainer').innerHTML = '';
    renderizarTabelaLancamentos(todosOsLancamentos);
}

function renderizarInputsDeData() {
    const container = document.getElementById('dataValorInputsContainer');
    const tipo = document.getElementById('filtroTipoData').value;
    if (tipo === 'entre') {
        container.innerHTML = `<div class="row"><div class="col-sm-6"><label class="form-label">De:</label><input type="date" id="filtroValorInicio" class="form-control"></div><div class="col-sm-6"><label class="form-label">Até:</label><input type="date" id="filtroValorFim" class="form-control"></div></div>`;
    } else {
        container.innerHTML = `<label class="form-label">Data:</label><input type="date" id="filtroValor" class="form-control">`;
    }
}

async function atualizarInputDeFiltro() {
    const container = document.getElementById('filtroValorContainer');
    const coluna = this.value;
    container.innerHTML = '';
    if (!coluna) return;

    if (coluna === 'dataLancamento') {
        container.innerHTML = `<div class="row"><div class="col-sm-5"><label class="form-label">Tipo:</label><select id="filtroTipoData" class="form-select"><option value="exatamente">Exatamente em</option><option value="apos">Após</option><option value="antes">Antes de</option><option value="entre">Entre</option></select></div><div class="col-sm-7" id="dataValorInputsContainer"></div></div>`;
        document.getElementById('filtroTipoData').addEventListener('change', renderizarInputsDeData);
        renderizarInputsDeData();
    } else if (coluna === 'contaBancariaId') {
        container.innerHTML = `<label class="form-label">Contas:</label><div class="dropdown"><button class="btn btn-outline-secondary dropdown-toggle w-100" type="button" data-bs-toggle="dropdown" aria-expanded="false">Selecionar Contas...</button><div class="dropdown-menu p-2 w-100" id="menuDropdownContas" onclick="event.stopPropagation()"><p class="text-center mb-0">Carregando...</p></div></div>`;
        try {
            const response = await fetch('http://localhost:8080/apis/conta');
            const contas = await response.json();
            const menu = document.getElementById('menuDropdownContas');
            menu.innerHTML = '';
            contas.forEach(c => {
                menu.innerHTML += `<div class="form-check"><input class="form-check-input filtro-conta-checkbox" type="checkbox" value="${c.id}" id="c-${c.id}"><label class="form-check-label" for="c-${c.id}">${c.banco} - Ag: ${c.agencia}</label></div>`;
            });
        } catch(e) { document.getElementById('menuDropdownContas').innerHTML = '<p class="text-danger">Erro.</p>'; }
    } else {
        container.innerHTML = `<label class="form-label">Palavra-chave:</label><input type="text" id="filtroValor" class="form-control">`;
    }
}


// --- FUNÇÕES DE AÇÃO PRINCIPAIS (COM LÓGICA MANTIDA E ADAPTADA) ---

async function carregarLancamentos() {
    try {
        const response = await fetch("http://localhost:8080/apis/lancamento-bancario");
        todosOsLancamentos = await response.json();
        renderizarTabelaLancamentos(todosOsLancamentos);
    } catch(error) {
        console.error("Erro ao carregar lançamentos:", error);
    }
}

function editarLancamento(lanc) {
    document.getElementById("idLancamento").value = lanc.id;
    document.getElementById("descricao").value = lanc.descricao || "";
    document.getElementById("data").value = lanc.dataLancamento || "";
    document.getElementById("origem").value = lanc.origem || "";
    document.getElementById("destino").value = lanc.destino || "";
    document.getElementById("receita").value = lanc.receitaId || "";
    document.getElementById("despesa").value = lanc.despesaId || "";
    document.getElementById("movimentacaoBancaria").value = lanc.movimentacaoBancariaId || "";
    document.getElementById("movimentacaoBancariaIdHidden").value = lanc.movimentacaoBancariaId || "";
    document.getElementById("conta").value = lanc.contaBancariaId || "";
    document.getElementById("contaId").value = lanc.contaBancariaId || "";

    // Alterna o radio e os campos conforme o tipo do lançamento
    if (lanc.receitaId && !lanc.despesaId) {
        document.getElementById('radioReceita').checked = true;
        document.getElementById('divReceita').style.display = '';
        document.getElementById('divDespesa').style.display = 'none';
        document.getElementById('despesa').value = '';
    } else if (lanc.despesaId && !lanc.receitaId) {
        document.getElementById('radioDespesa').checked = true;
        document.getElementById('divReceita').style.display = 'none';
        document.getElementById('divDespesa').style.display = '';
        document.getElementById('receita').value = '';
    } else {
        // Caso ambos estejam vazios, padrão para Receita
        document.getElementById('radioReceita').checked = true;
        document.getElementById('divReceita').style.display = '';
        document.getElementById('divDespesa').style.display = 'none';
        document.getElementById('despesa').value = '';
    }
}

async function salvarLancamento(event) {
    event.preventDefault();
    const id = document.getElementById("idLancamento").value;
    const lancamento = {
        descricao: document.getElementById("descricao").value,
        dataLancamento: document.getElementById("data").value,
        origem: document.getElementById("origem").value,
        destino: document.getElementById("destino").value,
        contaBancariaId: document.getElementById("contaId").value,
        movimentacaoBancariaId: document.getElementById("movimentacaoBancariaIdHidden").value,
        receitaId: document.getElementById("receita").value,
        despesaId: document.getElementById("despesa").value
    };
    if (!lancamento.movimentacaoBancariaId) {
        alert("Por favor, selecione uma movimentação bancária.");
        return;
    }
    const url = id ? `http://localhost:8080/apis/lancamento-bancario/${id}` : "http://localhost:8080/apis/lancamento-bancario";
    const method = id ? "PUT" : "POST";
    const response = await fetch(url, {
        method: method,
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(lancamento)
    });
    const data = await response.json();
    alert(data.mensagem || "Ocorreu um erro.");
    if (response.ok) {
        limparFormulario();
        await carregarLancamentos();
    }
}

async function excluirLancamento(id) {
    if (!confirm("Tem certeza?")) return;
    const response = await fetch(`http://localhost:8080/apis/lancamento-bancario/${id}`, { method: "DELETE" });
    const data = await response.json();
    alert(data.mensagem || "Ocorreu um erro.");
    if (response.ok) await carregarLancamentos();
}

function limparFormulario() {
    document.getElementById("formLancamento").reset();
    document.getElementById("idLancamento").value = "";
    document.getElementById("contaId").value = "";
    document.getElementById("movimentacaoBancariaIdHidden").value = "";
    document.getElementById("conta").value = "";
}