
// Array para armazenar os itens não conciliados e seus estados temporários
let itensNaoConciliados = [];
// Array para armazenar apenas as conciliações marcadas como pendentes
let conciliacoesPendentesParaSalvar = [];
// Variáveis para guardar o item que está sendo editado no modal de problema
let currentEditingItemId = null;
let currentEditingItemType = null;

// Variáveis para o modal de confirmação genérico
let confirmActionCallback = null; // Função a ser executada se o usuário confirmar
let confirmActionParams = null; // Parâmetros para a função de callback

// Adiciona um listener para carregar os itens de conciliação quando a página estiver pronta
document.addEventListener("DOMContentLoaded", () => {
    carregarItensConciliacao();
});


// Função para exibir mensagens na tela (sucesso/erro)
function showMessage(message, type) {
    const msgDiv = document.getElementById("mensagem");
    msgDiv.textContent = message;
    msgDiv.style.display = 'block';

    // Aplica estilos baseados no tipo de mensagem (sem operador ternário)
    if (type === 'red') {
        msgDiv.style.backgroundColor = '#ffebee'; // Fundo vermelho claro
        msgDiv.style.color = '#d32f2f'; // Texto vermelho escuro
        msgDiv.style.borderColor = '#ef9a9a'; // Borda vermelha
    } else if (type === 'green') {
        msgDiv.style.backgroundColor = '#e8f5e9'; // Fundo verde claro
        msgDiv.color = '#388e3c'; // Texto verde escuro
        msgDiv.style.borderColor = '#a5d6a7'; // Borda verde
    } else {
        // Estilo padrão se nenhum tipo for especificado
        msgDiv.style.backgroundColor = '#f0f0f0';
        msgDiv.color = '#333';
        msgDiv.style.borderColor = '#ccc';
    }

    setTimeout(() => {
        msgDiv.style.display = 'none';
        msgDiv.textContent = ''; // Limpa a mensagem
    }, 5000);
}

// Função para exibir o modal de confirmação genérico
function showConfirmModal(message, callback, params) {
    const modal = document.getElementById("confirmModal");
    const modalMessage = document.getElementById("confirmModalMessage");
    const confirmButton = document.getElementById("confirmModalYesButton");

    modalMessage.textContent = message;
    confirmActionCallback = callback;
    confirmActionParams = params;

    // Remove qualquer listener anterior para evitar múltiplas execuções
    confirmButton.onclick = null;
    confirmButton.addEventListener('click', executeConfirmAction);

    modal.style.display = "flex"; // Usa flex para centralizar
}

// Função para fechar o modal de confirmação genérico
function closeConfirmModal() {
    const modal = document.getElementById("confirmModal");
    modal.style.display = "none";
    confirmActionCallback = null;
    confirmActionParams = null;
}

// Função para executar a ação confirmada do modal
function executeConfirmAction() {
    if (confirmActionCallback) {
        confirmActionCallback.apply(null, confirmActionParams);
    }
    closeConfirmModal();
}

// Função principal para carregar e exibir os itens de conciliação
async function carregarItensConciliacao() {
    try {
        const response = await fetch("http://localhost:8080/apis/conciliacao/nao-conciliados");

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.mensagem || `Erro HTTP! Status: ${response.status}`);
        }

        const data = await response.json(); // Espera um JSON

        // Verifica se a resposta contém uma mensagem de erro ou uma lista vazia
        if (data && data.erro) {
            throw new Error(data.erro);
        }

        if (!data || data.length === 0) {
            itensNaoConciliados = []; // Garante que a lista está vazia
        } else {
            const itensRecebidos = data.ok ? data.ok : data;
            itensNaoConciliados = itensRecebidos.map(item => {
                let status = item.statusConciliacao;
                if (status === null) {
                    status = 'Aguardando';
                }
                return {
                    id: item.id,
                    tipo: item.tipo,
                    valor: item.valor,
                    descricao: item.descricao,
                    statusConciliacao: status
                };
            });
        }

        renderizarTabelaConciliacao(); // Chama a função para renderizar a tabela
    } catch (error) {
        console.error("Erro ao carregar itens para conciliação:", error);
        showMessage("Erro ao carregar itens para conciliação: " + error.message, 'red');
    }
}

// Função para renderizar a tabela de conciliação
function renderizarTabelaConciliacao() {
    const tableBody = document.getElementById("conciliacaoTableBody");
    tableBody.innerHTML = ''; // Limpa a tabela antes de renderizar

    // Obtém o valor do campo de filtro
    const filtroInput = document.getElementById("filtro");
    let filtro = "";
    if (filtroInput) {
        filtro = filtroInput.value.toLowerCase();
    }

    const filteredItems = [];
    if (itensNaoConciliados.length > 0) {
        itensNaoConciliados.forEach(item => {
            const tipoMatches = item.tipo.toLowerCase().includes(filtro);
            const valorMatches = item.valor.toFixed(2).toLowerCase().includes(filtro); // Converte valor para string para comparação
            const descriptionMatches = item.descricao.toLowerCase().includes(filtro);
            const statusMatches = item.statusConciliacao.toLowerCase().includes(filtro);

            if (filtro === "" || tipoMatches || valorMatches || descriptionMatches || statusMatches) {
                filteredItems.push(item);
            }
        });
    }

    if (filteredItems.length === 0) {
        const row = tableBody.insertRow();
        const cell = row.insertCell();
        cell.colSpan = 6; // Cobre todas as colunas
        cell.textContent = "Nenhum item para conciliação encontrado.";
        cell.style.textAlign = 'center'; // Centraliza a mensagem
        return;
    }

    filteredItems.forEach(item => {
        const row = tableBody.insertRow();
        row.dataset.id = item.id;
        row.dataset.type = item.tipo;

        row.insertCell().textContent = item.id;
        row.insertCell().textContent = item.tipo;
        row.insertCell().textContent = `R$ ${item.valor.toFixed(2)}`;
        row.insertCell().textContent = item.descricao;

        const statusCell = row.insertCell();
        const statusSpan = document.createElement('span');
        statusSpan.id = `status-${item.tipo}-${item.id}`;
        statusSpan.textContent = item.statusConciliacao; // Exibe o status atual
        statusCell.appendChild(statusSpan);

        const actionsCell = row.insertCell();
        // Botão CONCILIADO
        const conciliadoButton = document.createElement('button');
        conciliadoButton.textContent = 'Conciliado';
        conciliadoButton.className = 'btn btn-sm btn-success'; // Estilo Bootstrap
        conciliadoButton.onclick = function() {
            showConfirmModal(`Deseja marcar este item (${item.tipo} ID: ${item.id}) como CONCILIADO?`, confirmConciliadoAction, [item.id, item.tipo]);
        };
        actionsCell.appendChild(conciliadoButton);

        // Botão PENDENTE
        const pendingButton = document.createElement('button');
        pendingButton.textContent = 'Pendente';
        pendingButton.className = 'btn btn-sm btn-warning ms-2';
        pendingButton.onclick = function() {
            handleConciliacaoPendente(item.id, item.tipo);
        };
        actionsCell.appendChild(pendingButton);
    });
}

// Ação para confirmar conciliação (chamada pelo modal)
async function confirmConciliadoAction(id, tipo) {
    const itemIndex = itensNaoConciliados.findIndex(item => item.id === id && item.tipo === tipo);
    if (itemIndex === -1) {
        showMessage("Erro: Item não encontrado para conciliação.", 'red');
        return;
    }

    let endpoint = `/apis/conciliacao/receita/${id}/status`;
    if (tipo === 'Despesa') {
        endpoint = `/apis/conciliacao/despesa/${id}/status`;
    }

    try {
        const response = await fetch(`http://localhost:8080${endpoint}`, {
            method: 'PUT'
        });

        const result = await response.json(); // Espera um Map de retorno (com "ok" ou "erro")
        if (response.ok && result.mensagem) {
            itensNaoConciliados.splice(itemIndex, 1); // Remove 1 item a partir do index
            renderizarTabelaConciliacao();
            showMessage(result.mensagem, 'green'); // Exibe a mensagem de sucesso
        } else {
            throw new Error(result.erro || `Erro desconhecido ao atualizar status para CONCILIADO.`);
        }
    } catch (error) {
        console.error("Erro ao atualizar status:", error);
        showMessage("Erro ao atualizar status: " + error.message, 'red');
    }
}

// Função para lidar com a ação de conciliação Pendente (abre o modal de problema)
function handleConciliacaoPendente(id, tipo) {
    currentEditingItemId = id;
    currentEditingItemType = tipo;
    showProblemaModal();
}

// Função para exibir a janela modal de problema
function showProblemaModal() {
    const modal = document.getElementById("problemaModal");
    const dataProblemaInput = document.getElementById("dataProblema");
    // Define a data atual como valor padrão
    dataProblemaInput.value = new Date().toISOString().split('T')[0];
    const descricaoProblemaInput = document.getElementById("descricaoProblema");
    descricaoProblemaInput.value = ''; // Limpa a descrição
    descricaoProblemaInput.style.border = ""; // Limpa borda de erro anterior

    // Remove label de erro anterior se existir
    const oldErrorLabel = descricaoProblemaInput.parentElement.querySelector('.error-label');
    if (oldErrorLabel) {
        oldErrorLabel.remove();
    }

    modal.style.display = "flex"; // Usa flex para centralizar o modal
}

// Função para fechar a janela modal de problema
function closeProblemaModal() {
    const modal = document.getElementById("problemaModal");
    modal.style.display = "none";
    currentEditingItemId = null;
    currentEditingItemType = null;
    // Limpa a borda e a mensagem de erro ao fechar o modal
    const descricaoProblemaInput = document.getElementById("descricaoProblema");
    descricaoProblemaInput.style.border = "";
    const oldErrorLabel = descricaoProblemaInput.parentElement.querySelector('.error-label');
    if (oldErrorLabel) {
        oldErrorLabel.remove();
    }
}

// Função para salvar os detalhes do problema (chamada pelo modal)
async function saveProblema() {
    const dataProblema = document.getElementById("dataProblema").value;
    const descricaoProblemaInput = document.getElementById("descricaoProblema");
    const descricaoProblema = descricaoProblemaInput.value.trim();

    // Limpa estilos de erro anteriores
    descricaoProblemaInput.style.border = "";
    const oldErrorLabel = descricaoProblemaInput.parentElement.querySelector('.error-label');
    if (oldErrorLabel) {
        oldErrorLabel.remove();
    }

    if (descricaoProblema === "") {
        descricaoProblemaInput.style.border = "2px solid red";
        const errorLabel = document.createElement('div');
        errorLabel.className = 'error-label';
        errorLabel.style.color = "red";
        errorLabel.style.fontSize = "12px";
        errorLabel.style.marginTop = "5px";
        errorLabel.innerText = "A descrição do problema não pode estar vazia.";
        descricaoProblemaInput.parentElement.appendChild(errorLabel);
        showMessage("Por favor, preencha a descrição do problema.", 'red');
        return;
    }

    const itemIndex = itensNaoConciliados.findIndex(
        item => item.id === currentEditingItemId && item.tipo === currentEditingItemType
    );

    if (itemIndex !== -1) {
        const item = itensNaoConciliados[itemIndex];

        const conciliacaoProblema = {
            concDtProblema: dataProblema,
            concDescProblema: descricaoProblema,
            concDtSolucao: null,
            concDescSolucao: null,
            concReceitaId: 0, // Default para 0, será ajustado abaixo
            concDespesaId: 0  // Default para 0, será ajustado abaixo
        };

        if (item.tipo === 'Receita') {
            conciliacaoProblema.concReceitaId = item.id;
        } else {
            conciliacaoProblema.concDespesaId = item.id;
        }

        conciliacoesPendentesParaSalvar.push(conciliacaoProblema);

        itensNaoConciliados.splice(itemIndex, 1); // Remove 1 item a partir do index
        renderizarTabelaConciliacao(); // Renderiza a tabela novamente para remover o item

        showMessage(`Problema para ${item.tipo} ID ${item.id} registrado como pendente e removido da lista.`, 'green');
        closeProblemaModal();
    } else {
        showMessage("Erro: Item não encontrado para registrar problema.", 'red');
        closeProblemaModal();
    }
}

// Função para exibir o modal de confirmação para salvar conciliações pendentes
function showConfirmSalvarPendentesModal() {
    if (conciliacoesPendentesParaSalvar.length === 0) {
        showMessage("Nenhuma conciliação pendente para salvar.", 'red');
        return;
    }
    showConfirmModal(`Deseja salvar ${conciliacoesPendentesParaSalvar.length} conciliações pendentes?`, salvarConciliacoesPendentes);
}

// Função para salvar todas as conciliações pendentes (enviar para o backend)
async function salvarConciliacoesPendentes() {
    if (conciliacoesPendentesParaSalvar.length === 0) {
        showMessage("Nenhuma conciliação pendente para salvar.", 'red');
        return;
    }

    try {
        const response = await fetch("http://localhost:8080/apis/conciliacao/problemas", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(conciliacoesPendentesParaSalvar)
        });

        const result = await response.json(); // Espera um Map de retorno (com "ok" ou "erro")
        if (response.ok && result.mensagem) {
            showMessage(result.mensagem, 'green'); // Exibe a mensagem de sucesso
            conciliacoesPendentesParaSalvar = []; // Limpa a lista após salvar com sucesso
        } else {
            throw new Error(result.erro || "Erro desconhecido ao salvar conciliações pendentes.");
        }
    } catch (error) {
        console.error("Erro ao salvar conciliações pendentes:", error);
        showMessage("Erro ao salvar conciliações pendentes: " + error.message, 'red');
    }
}

// Simulação de authManager.logout() para o botão Deslogar
const authManager = {
    logout: function() {
        showMessage('Função Deslogar simulada. Redirecionaria para a página de login.', 'green');
        window.location.href = 'login.html';
    }
};
