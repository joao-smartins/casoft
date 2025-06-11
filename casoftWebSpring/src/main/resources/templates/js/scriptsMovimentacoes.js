// --- ESCOPO DO SCRIPT ---
let todasAsMovimentacoes = [];


// --- INICIALIZAÇÃO ---
document.addEventListener("DOMContentLoaded", async () => {
    await carregarMovimentacoes();
    await carregarContasParaSelect('conta');
    await carregarUsuarios();
   //await ff(); // Chamada para a função que busca dados por status

    document.getElementById('filtroColuna').addEventListener('change', atualizarInputDeFiltro);
    document.getElementById('btnFiltrar').addEventListener('click', aplicarFiltro);
    document.getElementById('btnLimparFiltro').addEventListener('click', limparFiltro);
});


// --- LÓGICA DE FILTRAGEM (ATUALIZADA) ---

function renderizarTabela(movimentacoesParaRenderizar) {
    const tbody = document.querySelector("#tabelaMovimentacoes tbody");
    tbody.innerHTML = "";

    if (!movimentacoesParaRenderizar || movimentacoesParaRenderizar.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center">Nenhuma movimentação encontrada.</td></tr>';
        return;
    }

    movimentacoesParaRenderizar.forEach(mov => {
        tbody.innerHTML += `
          <tr>
            <td>${mov.id}</td>
            <td>${mov.total !== null ? mov.total.toFixed(2) : ""}</td>
            <td>${mov.data || ""}</td>
            <td>${mov.usuarioId || ""}</td>
            <td>${mov.contaBancariaId || ""}</td>
            <td>
              <button class="btn btn-sm btn-warning" onclick='editarMovimentacao(${JSON.stringify(mov)})'>Editar</button>
              <button class="btn btn-sm btn-danger" onclick='excluirMovimentacao(${mov.id})'>Excluir</button>
            </td>
          </tr>
        `;
    });
}

function aplicarFiltro() {
    const coluna = document.getElementById('filtroColuna').value;
    if (!coluna) {
        alert('Por favor, selecione uma coluna para filtrar.');
        return;
    }

    let movimentacoesFiltradas = todasAsMovimentacoes;

    if (coluna === 'total') {
        const tipoDeFiltro = document.getElementById('filtroTipoTotal').value;
        if (tipoDeFiltro === 'entre') {
            const min = parseFloat(document.getElementById('filtroValorMin').value);
            const max = parseFloat(document.getElementById('filtroValorMax').value);
            if (isNaN(min) || isNaN(max)) {
                alert('Para o filtro "Entre", os valores mínimo e máximo devem ser preenchidos.');
                return;
            }
            movimentacoesFiltradas = todasAsMovimentacoes.filter(mov => mov.total >= min && mov.total <= max);
        } else {
            const valor = parseFloat(document.getElementById('filtroValor').value);
            if (isNaN(valor)) {
                alert('Por favor, insira um valor numérico para o filtro.');
                return;
            }
            switch (tipoDeFiltro) {
                case 'exatamente':
                    movimentacoesFiltradas = todasAsMovimentacoes.filter(mov => mov.total === valor);
                    break;
                case 'acima':
                    movimentacoesFiltradas = todasAsMovimentacoes.filter(mov => mov.total > valor);
                    break;
                case 'abaixo':
                    movimentacoesFiltradas = todasAsMovimentacoes.filter(mov => mov.total < valor);
                    break;
            }
        }
    } else if (coluna === 'data') {
        const tipoDeFiltro = document.getElementById('filtroTipoData').value;
        if (tipoDeFiltro === 'entre') {
            const dataInicio = document.getElementById('filtroValorInicio').value;
            const dataFim = document.getElementById('filtroValorFim').value;
            if (!dataInicio || !dataFim) {
                alert('Para o filtro "Entre", as datas inicial e final devem ser preenchidas.');
                return;
            }
            movimentacoesFiltradas = todasAsMovimentacoes.filter(mov => mov.data && mov.data >= dataInicio && mov.data <= dataFim);
        } else {
            const valor = document.getElementById('filtroValor').value;
            if (!valor) {
                alert('Por favor, insira uma data para o filtro.');
                return;
            }
            switch (tipoDeFiltro) {
                case 'exatamente':
                    movimentacoesFiltradas = todasAsMovimentacoes.filter(mov => mov.data === valor);
                    break;
                case 'apos':
                    movimentacoesFiltradas = todasAsMovimentacoes.filter(mov => mov.data && mov.data > valor);
                    break;
                case 'antes':
                    movimentacoesFiltradas = todasAsMovimentacoes.filter(mov => mov.data && mov.data < valor);
                    break;
            }
        }
    } else if (coluna === 'contaBancariaId') { // LÓGICA ATUALIZADA PARA CHECKBOXES
        const checkboxesMarcados = document.querySelectorAll('.filtro-conta-checkbox:checked');
        
        if (checkboxesMarcados.length === 0) {
            alert('Por favor, selecione ao menos uma conta para filtrar.');
            return;
        }

        const contasSelecionadas = Array.from(checkboxesMarcados).map(cb => cb.value);
        
        movimentacoesFiltradas = todasAsMovimentacoes.filter(mov => 
            contasSelecionadas.includes(String(mov.contaBancariaId))
        );
    } else { // Lógica para outras colunas (ex: Usuário)
        const valorInput = document.getElementById('filtroValor');
        if (!valorInput || !valorInput.value) {
            alert('Por favor, insira um valor para o filtro.');
            return;
        }
        const valor = valorInput.value;
        movimentacoesFiltradas = todasAsMovimentacoes.filter(mov => mov[coluna] !== null && String(mov[coluna]) === valor);
    }

    renderizarTabela(movimentacoesFiltradas);
}

function limparFiltro() {
    document.getElementById('filtroColuna').value = '';
    document.getElementById('filtroValorContainer').innerHTML = '';
    renderizarTabela(todasAsMovimentacoes);
}

// --- FUNÇÕES DE RENDERIZAÇÃO DE INPUTS (ATUALIZADA) ---

function renderizarInputsDeTotal() {
    const container = document.getElementById('totalValorInputsContainer');
    const tipoDeFiltro = document.getElementById('filtroTipoTotal').value;
    container.innerHTML = '';

    if (tipoDeFiltro === 'entre') {
        container.innerHTML = `
            <div class="row">
                <div class="col-sm-6"><label for="filtroValorMin" class="form-label">Mínimo:</label><input type="number" step="0.01" id="filtroValorMin" class="form-control"></div>
                <div class="col-sm-6"><label for="filtroValorMax" class="form-label">Máximo:</label><input type="number" step="0.01" id="filtroValorMax" class="form-control"></div>
            </div>`;
    } else {
        container.innerHTML = `<label for="filtroValor" class="form-label">Valor:</label><input type="number" step="0.01" id="filtroValor" class="form-control">`;
    }
}

function renderizarInputsDeData() {
    const container = document.getElementById('dataValorInputsContainer');
    const tipoDeFiltro = document.getElementById('filtroTipoData').value;
    container.innerHTML = '';

    if (tipoDeFiltro === 'entre') {
        container.innerHTML = `
            <div class="row">
                <div class="col-sm-6"><label for="filtroValorInicio" class="form-label">De:</label><input type="date" id="filtroValorInicio" class="form-control"></div>
                <div class="col-sm-6"><label for="filtroValorFim" class="form-label">Até:</label><input type="date" id="filtroValorFim" class="form-control"></div>
            </div>`;
    } else {
        container.innerHTML = `<label for="filtroValor" class="form-label">Data:</label><input type="date" id="filtroValor" class="form-control">`;
    }
}

async function atualizarInputDeFiltro() {
    const container = document.getElementById('filtroValorContainer');
    const coluna = this.value;
    container.innerHTML = '';

    if (!coluna) return;

    if (coluna === 'total') {
        container.innerHTML = `
            <div class="row">
                <div class="col-sm-5"><label for="filtroTipoTotal" class="form-label">Tipo:</label><select id="filtroTipoTotal" class="form-select"><option value="exatamente">Exatamente</option><option value="acima">Acima de</option><option value="abaixo">Abaixo de</option><option value="entre">Entre</option></select></div>
                <div class="col-sm-7" id="totalValorInputsContainer"></div>
            </div>`;
        document.getElementById('filtroTipoTotal').addEventListener('change', renderizarInputsDeTotal);
        renderizarInputsDeTotal();
    } else if (coluna === 'data') {
        container.innerHTML = `
            <div class="row">
                <div class="col-sm-5"><label for="filtroTipoData" class="form-label">Tipo:</label><select id="filtroTipoData" class="form-select"><option value="exatamente">Exatamente em</option><option value="apos">Após</option><option value="antes">Antes de</option><option value="entre">Entre</option></select></div>
                <div class="col-sm-7" id="dataValorInputsContainer"></div>
            </div>`;
        document.getElementById('filtroTipoData').addEventListener('change', renderizarInputsDeData);
        renderizarInputsDeData();
    } else if (coluna === 'contaBancariaId') { // ALTERADO PARA GERAR DROPDOWN COM CHECKBOXES
        // Estrutura do dropdown do Bootstrap
        container.innerHTML = `
            <label class="form-label">Contas:</label>
            <div class="dropdown">
                <button class="btn btn-outline-secondary dropdown-toggle w-100" type="button" id="dropdownContasFiltro" data-bs-toggle="dropdown" aria-expanded="false">
                    Selecionar Contas...
                </button>
                <div class="dropdown-menu p-2 w-100" aria-labelledby="dropdownContasFiltro" id="menuDropdownContas">
                    <p class="text-center mb-0">Carregando...</p>
                </div>
            </div>
        `;
        // Para o menu não fechar ao clicar no checkbox
        document.getElementById('menuDropdownContas').addEventListener('click', e => e.stopPropagation());
        
        // Popula o dropdown com as contas
        try {
            const response = await fetch('http://localhost:8080/apis/conta');
            const contas = await response.json();
            const menu = document.getElementById('menuDropdownContas');
            menu.innerHTML = ''; // Limpa o 'Carregando...'
            contas.forEach(conta => {
                menu.innerHTML += `
                    <div class="form-check">
                        <input class="form-check-input filtro-conta-checkbox" type="checkbox" value="${conta.id}" id="conta-check-${conta.id}">
                        <label class="form-check-label" for="conta-check-${conta.id}">
                            ${conta.banco} - Ag: ${conta.agencia}
                        </label>
                    </div>
                `;
            });
        } catch(error) {
            console.error("Erro ao carregar contas para o filtro:", error);
            document.getElementById('menuDropdownContas').innerHTML = '<p class="text-center text-danger mb-0">Erro ao carregar.</p>';
        }

    } else { // Para "Usuário" ou outros filtros de texto simples
        const isUsuario = coluna === 'usuarioId';
        let inputHtml = `
            <label for="filtroValor" class="form-label">Valor:</label>
            <${isUsuario ? 'select' : 'input type="text"'} id="filtroValor" class="form-control">
            </${isUsuario ? '</select>' : ''}>`;
        container.innerHTML = inputHtml;
        if(isUsuario) {
            await popularSelectGenerico('filtroValor', 'http://localhost:8080/api/usuarios/todos', 'id', 'nome');
        }
    }
}


// --- LÓGICA DE CARREGAMENTO DE DADOS E FUNÇÕES AUXILIARES ---

async function carregarMovimentacoes() {
    const url = "http://localhost:8080/apis/movimentacao";
    try {
        const response = await fetch(url);
        if (!response.ok) throw new Error(`Erro na requisição: ${response.statusText}`);
        todasAsMovimentacoes = await response.json();
        renderizarTabela(todasAsMovimentacoes);
    } catch (error) {
        console.error("Falha ao carregar movimentações:", error);
        const tbody = document.querySelector("#tabelaMovimentacoes tbody");
        tbody.innerHTML = `<tr><td colspan="6" class="text-center text-danger">Erro ao carregar dados. Verifique a conexão com o servidor.</td></tr>`;
    }
}

async function carregarContasParaSelect(selectId) {
    await popularSelectGenerico(selectId, 'http://localhost:8080/apis/conta', 'id', item => `${item.banco} - Ag: ${item.agencia} - Conta: ${item.numero}`);
    if (selectId === 'conta') {
         document.getElementById(selectId).addEventListener('change', (event) => {
            document.getElementById('contaId').value = event.target.value;
        });
    }
}

async function carregarUsuarios() {
    await popularSelectGenerico('usuario', 'http://localhost:8080/api/usuarios/todos', 'id', item => `${item.nome} - ${item.nivelAcesso}`);
    document.getElementById('usuario').addEventListener('change', (event) => {
        document.getElementById('usuarioId').value = event.target.value;
    });
}

async function popularSelectGenerico(selectId, url, valorKey, textoKey) {
    try {
        const response = await fetch(url);
        if (!response.ok) throw new Error(`A resposta da rede não foi ok para ${url}`);
        
        const dados = await response.json();
        const selectElement = document.getElementById(selectId);
        
        const defaultOption = selectElement.querySelector('option');
        selectElement.innerHTML = '';
        if (defaultOption) {
            selectElement.appendChild(defaultOption);
        } else {
             selectElement.innerHTML = '<option value="">Selecione...</option>';
        }
        
        dados.forEach(item => {
            const option = document.createElement("option");
            option.value = item[valorKey];
            option.textContent = typeof textoKey === 'function' ? textoKey(item) : item[textoKey];
            selectElement.appendChild(option);
        });
    } catch (error) {
        console.error(`Erro ao carregar dados para o select #${selectId}:`, error);
    }
}

// --- Funções do formulário (sem alterações) ---

function editarMovimentacao(mov) {
    document.getElementById("idMovimentacao").value = mov.id;
    document.getElementById("total").value = mov.total || "";
    document.getElementById("data").value = mov.data || "";
    document.getElementById("usuario").value = mov.usuarioId || "";
    document.getElementById("usuarioId").value = mov.usuarioId || "";
    document.getElementById("conta").value = mov.contaBancariaId || "";
    document.getElementById("contaId").value = mov.contaBancariaId || "";
}

async function salvarMovimentacao(event) {
    event.preventDefault();
    const id = document.getElementById("idMovimentacao").value;
    const movimentacao = {
        movbancTotal: parseFloat(document.getElementById("total").value),
        movbancData: document.getElementById("data").value,
        usuarioUserId: document.getElementById("usuarioId").value,
        contabancariaContabId: document.getElementById("contaId").value
    };
    let url = "http://localhost:8080/apis/movimentacao";
    let method = "POST";

    if (id) {
        url += `/update/${id}`;
        method = "PUT";
    }

    const response = await fetch(url, {
        method: method,
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(movimentacao)
    });

    if (response.ok) {
        alert("Operação realizada com sucesso!");
        limparFormulario(); 
        await carregarMovimentacoes();
    } else {
        const data = await response.json();
        alert(data.mensagem || "Erro ao salvar movimentação bancária.");
    }
    return false;
}

async function excluirMovimentacao(id) {
    if (!confirm("Tem certeza que deseja excluir esta movimentação bancária?")) return;
    const response = await fetch(`http://localhost:8080/apis/movimentacao/${id}`, {
        method: "DELETE"
    });
    if (response.ok) {
        alert("Excluído com sucesso!");
        await carregarMovimentacoes();
    } else {
        const data = await response.json();
        alert(data.mensagem || "Erro ao excluir.");
    }
}

function limparFormulario() {
    document.getElementById("formMovimentacao").reset();
    document.getElementById("idMovimentacao").value = "";
    document.getElementById("contaId").value = ""; 
    document.getElementById("usuarioId").value = ""; 
}

// async function ff() {
//     // Define the parameters
//     const statusReceita = 'PAGO';
//     const statusDespesa = 'PENDENTE';

//     // Construct the URL with URLSearchParams to handle special characters safely
//     const url = new URL('http://localhost:8080/apis/saldo-geral/por-status');
//     url.searchParams.append('statusReceita', statusReceita);
//     url.searchParams.append('statusDespesa', statusDespesa);

//     // Perform the fetch request
//     try {
//         const response = await fetch(url);
//         if (!response.ok) {
//             throw new Error(`HTTP error! Status: ${response.status}`);
//         }
//         const data = await response.json();
//         console.log('Success:', data);
//         // You can now use your data, for example:
//         // document.getElementById('saldo-total').textContent = data.saldo; 
//         return data;
//     } catch (error) {
//         console.error('Error fetching data:', error);
//         throw error;
//     }
// }