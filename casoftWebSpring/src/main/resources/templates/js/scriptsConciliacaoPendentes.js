// src/main/resources/static/js/scriptsConciliacaoPendentes.js

const API_BASE_URL = 'http://localhost:8080/apis/conciliacao';
let conciliacoesComProblema = []; // Array para armazenar as conciliações com problema
let confirmActionCallback = null; // Função a ser executada se o usuário confirmar
let confirmActionParams = null; // Parâmetros para a função de callback

// Variáveis para o novo modal de solução (para manter o contexto do problema atual)
let currentConcId = null;
let currentItemId = null; // ID da Receita ou Despesa
let currentItemTipo = null; // 'Receita' ou 'Despesa'

// --- Funções Auxiliares Comuns ---

document.addEventListener("DOMContentLoaded", () => {
    // Carrega os dados iniciais
    carregarConciliacoesComProblema();

    // Listener para o botão de confirmar solução dentro do NOVO modal
    document.getElementById("btnConfirmarSolucao").addEventListener("click", () => {
        const dataSolucao = document.getElementById("dataSolucao").value;
        const descricaoSolucao = document.getElementById("descricaoSolucao").value;

        if (!dataSolucao || dataSolucao.trim() === '') {
            showMessage("Por favor, preencha a data da solução.", 'red');
            return;
        }
        if (!descricaoSolucao || descricaoSolucao.trim() === '') {
            showMessage("Por favor, preencha a descrição da solução.", 'red');
            return;
        }

        resolverProblemaFinal(currentConcId, currentItemId, currentItemTipo, dataSolucao, descricaoSolucao);
        closeSolucaoProblemaModal(); // Fecha o modal após iniciar a ação
    });

    // Listener para o filtro de pesquisa
    const filtroInput = document.getElementById("filtro");
    if (filtroInput) {
        // Usa 'input' para filtrar em tempo real conforme o usuário digita
        filtroInput.addEventListener('input', () => {
            // No caso de Conciliações Pendentes, a lista já é carregada uma vez.
            // O filtro será aplicado localmente.
            renderizarTabelaProblemas();
        });
    }

    // Listener para o botão de fechar do modal de confirmação (se existir)
    const closeConfirmModalBtn = document.getElementById("closeConfirmModal");
    if (closeConfirmModalBtn) {
        closeConfirmModalBtn.addEventListener('click', closeConfirmModal);
    }

    // Listener para o botão de cancelar/fechar do modal de solução
    const closeSolucaoModalBtn = document.getElementById("closeSolucaoProblemaModal");
    if (closeSolucaoModalBtn) {
        closeSolucaoModalBtn.addEventListener('click', closeSolucaoProblemaModal);
    }
});

function showMessage(message, type) {
    const msgDiv = document.getElementById("mensagem");
    if (!msgDiv) {
        console.error("Elemento 'mensagem' não encontrado.");
        return;
    }
    msgDiv.textContent = message;
    msgDiv.style.display = 'block';

    // Reset styles first
    msgDiv.style.backgroundColor = '';
    msgDiv.style.color = '';
    msgDiv.style.borderColor = '';

    if (type === 'red') {
        msgDiv.style.backgroundColor = '#ffebee';
        msgDiv.style.color = '#d32f2f';
        msgDiv.style.borderColor = '#ef9a9a';
    } else if (type === 'green') {
        msgDiv.style.backgroundColor = '#e8f5e9';
        msgDiv.style.color = '#388e3c';
        msgDiv.style.borderColor = '#a5d6a7';
    } else { // Default or info (azul claro)
        msgDiv.style.backgroundColor = '#e3f2fd';
        msgDiv.style.color = '#1976d2';
        msgDiv.style.borderColor = '#90caf9';
    }

    setTimeout(() => {
        msgDiv.style.display = 'none';
        msgDiv.textContent = '';
    }, 5000);
}

// Funções para o MODAL DE CONFIRMAÇÃO GENÉRICO
function showConfirmModal(message, callback, params) {
    const modal = document.getElementById("confirmModal");
    const modalMessage = document.getElementById("confirmModalMessage");
    const confirmButton = document.getElementById("confirmModalYesButton");

    if (!modal || !modalMessage || !confirmButton) {
        console.error("Elementos do modal de confirmação não encontrados.");
        return;
    }

    modalMessage.textContent = message;
    confirmActionCallback = callback;
    confirmActionParams = params;

    confirmButton.onclick = null; // Limpa listener anterior para evitar duplicidade
    confirmButton.addEventListener('click', executeConfirmAction);

    modal.style.display = "flex";
}

function closeConfirmModal() {
    const modal = document.getElementById("confirmModal");
    if (modal) {
        modal.style.display = "none";
    }
    confirmActionCallback = null;
    confirmActionParams = null;
}

function executeConfirmAction() {
    if (confirmActionCallback) {
        confirmActionCallback.apply(null, confirmActionParams);
    }
    closeConfirmModal();
}

// Funções para o NOVO MODAL DE SOLUÇÃO DO PROBLEMA
function showSolucaoProblemaModal(concId, itemId, itemTipo) {
    currentConcId = concId;
    currentItemId = itemId;
    currentItemTipo = itemTipo;

    document.getElementById("dataSolucao").valueAsDate = new Date(); // Preenche com a data atual
    document.getElementById("descricaoSolucao").value = ""; // Limpa a descrição

    const modal = document.getElementById("solucaoProblemaModal");
    if (modal) {
        modal.style.display = "flex";
    } else {
        console.error("Modal de solução do problema não encontrado.");
    }
}

function closeSolucaoProblemaModal() {
    const modal = document.getElementById("solucaoProblemaModal");
    if (modal) {
        modal.style.display = "none";
    }
}


// Função para carregar as conciliações que foram registradas como problemas
async function carregarConciliacoesComProblema() {
    try {
        // Remove o filtro da requisição, pois a filtragem será feita no front-end
        const response = await fetch(`${API_BASE_URL}/problemas`);

        if (!response.ok) {
            const errorBody = await response.json();
            throw new Error(errorBody.message || `Erro HTTP! Status: ${response.status}`);
        }

        const data = await response.json();

        if (Array.isArray(data)) {
            conciliacoesComProblema = data;
        } else {
            console.warn("A resposta da API /problemas não é uma array:", data);
            conciliacoesComProblema = [];
            showMessage("Formato de dados inesperado da API. Tente novamente.", 'red');
        }

        renderizarTabelaProblemas();
    } catch (error) {
        console.error("Erro ao carregar conciliações com problema:", error);
        showMessage("Erro ao carregar conciliações com problema: " + error.message, 'red');
        conciliacoesComProblema = [];
        renderizarTabelaProblemas();
    }
}

// Função para renderizar a tabela de problemas de conciliação
function renderizarTabelaProblemas() {
    const tableBody = document.getElementById("problemasTableBody");
    if (!tableBody) {
        console.error("Elemento 'problemasTableBody' não encontrado. Verifique o HTML.");
        return;
    }
    tableBody.innerHTML = ''; // Limpa a tabela

    const filtroInput = document.getElementById("filtro");
    const filtroTexto = filtroInput ? filtroInput.value.toLowerCase().trim() : ""; // Trim para remover espaços

    const filteredProblemas = conciliacoesComProblema.filter(conc => {
        // Converte todos os campos para string e minúsculas para comparação
        const concIdStr = String(conc.concId || '').toLowerCase();
        const itemTipoStr = (conc.itemTipo || '').toLowerCase();
        const itemId = (conc.concReceitaId || conc.concDespesaId || 'N/A').toString().toLowerCase();
        const itemDescricaoStr = (conc.itemDescricao || '').toLowerCase();
        const concDescProblemaStr = (conc.concDescProblema || '').toLowerCase();

        // --- Tratamento aprimorado para Valor Item ---
        // Remove "R$", espaços e substitui vírgula por ponto para padronizar
        const rawItemValor = typeof conc.itemValor === 'number' ? conc.itemValor : parseFloat(String(conc.itemValor || '0').replace(',', '.'));
        const itemValorFormatted = rawItemValor.toFixed(2); // Formata para duas casas decimais
        const itemValorSearchable = itemValorFormatted.toLowerCase().replace('r$', '').replace(/\s/g, ''); // Remove "R$" e espaços

        // --- Tratamento aprimorado para Data Problema ---
        const concDtProblemaFormatted = conc.concDtProblema ? new Date(conc.concDtProblema).toLocaleDateString('pt-BR') : '';
        const concDtProblemaSearchable = concDtProblemaFormatted.toLowerCase(); // Ex: "29/05/2025"

        // Lógica de filtragem
        return (
            filtroTexto === "" || // Se o filtro estiver vazio, mostra tudo
            concIdStr.includes(filtroTexto) ||
            itemTipoStr.includes(filtroTexto) ||
            itemId.includes(filtroTexto) ||
            itemDescricaoStr.includes(filtroTexto) ||
            concDescProblemaStr.includes(filtroTexto) ||
            // Compara o valor formatado e limpo
            itemValorSearchable.includes(filtroTexto.replace('r$', '').replace(/\s/g, '')) ||
            // Compara a data formatada
            concDtProblemaSearchable.includes(filtroTexto)
        );
    });

    if (filteredProblemas.length === 0) {
        const row = tableBody.insertRow();
        const cell = row.insertCell();
        cell.colSpan = 8; // Cobre todas as colunas
        cell.textContent = "Nenhuma conciliação pendente encontrada.";
        cell.style.textAlign = 'center';
        return;
    }

    filteredProblemas.forEach(conc => {
        const row = tableBody.insertRow();

        // Tratamento de valores nulos para exibição
        const itemId = conc.concReceitaId !== null && conc.concReceitaId !== 0 ? conc.concReceitaId : (conc.concDespesaId !== null && conc.concDespesaId !== 0 ? conc.concDespesaId : 'N/A');
        const itemTipo = conc.itemTipo || 'N/A';
        const itemValor = typeof conc.itemValor === 'number' ? `R$ ${conc.itemValor.toFixed(2)}` : 'R$ 0.00';
        const itemDescricao = conc.itemDescricao || 'N/A';
        const concDtProblemaFormatted = conc.concDtProblema ? new Date(conc.concDtProblema).toLocaleDateString('pt-BR') : 'N/A';
        const concDescProblema = conc.concDescProblema || 'N/A';

        row.insertCell().textContent = conc.concId || 'N/A';
        row.insertCell().textContent = itemTipo;
        row.insertCell().textContent = itemId;
        row.insertCell().textContent = itemValor;
        row.insertCell().textContent = itemDescricao;
        row.insertCell().textContent = concDtProblemaFormatted;
        row.insertCell().textContent = concDescProblema;

        const actionsCell = row.insertCell();

        // Botão "Resolver e Conciliar" -> AGORA ABRE O NOVO MODAL DE SOLUÇÃO
        const resolverButton = document.createElement('button');
        resolverButton.textContent = 'Resolver e Conciliar';
        resolverButton.className = 'btn btn-sm btn-info'; // Estilo Bootstrap
        resolverButton.onclick = function() {
            showSolucaoProblemaModal(conc.concId, itemId, itemTipo);
        };
        actionsCell.appendChild(resolverButton);
    });
}

async function resolverProblemaFinal(concId, itemId, itemTipo, dataSolucao, descricaoSolucao) {
    let endpointMarcarConciliado = '';
    if (itemTipo === 'Receita') {
        endpointMarcarConciliado = `/apis/conciliacao/receita/${itemId}/status`;
    } else if (itemTipo === 'Despesa') {
        endpointMarcarConciliado = `/apis/conciliacao/despesa/${itemId}/status`;
    } else {
        showMessage("Erro: Tipo de item desconhecido para conciliação.", 'red');
        return;
    }

    try {
        const responseMarcar = await fetch(`http://localhost:8080${endpointMarcarConciliado}`, {
            method: 'PUT'
        });
        const resultMarcar = await responseMarcar.json();

        if (!responseMarcar.ok) {
            throw new Error(resultMarcar.message || `Falha ao marcar ${itemTipo} ID ${itemId} como conciliado. Status: ${responseMarcar.status}`);
        }

        const responseExcluirConc = await fetch(`${API_BASE_URL}/solucao/${concId}`, { // Agora chama o DELETE /solucao/{id}
            method: 'DELETE', // MUDANÇA PARA DELETE
            headers: { 'Content-Type': 'application/json' }
        });
        const resultExcluirConc = await responseExcluirConc.json();

        if (!responseExcluirConc.ok) {
            throw new Error(resultExcluirConc.message || `Falha ao excluir o registro de conciliação ${concId}. Status: ${responseExcluirConc.status}`);
        }

        // Se ambos os passos forem bem-sucedidos
        showMessage(`Problema ID ${concId} resolvido, ${itemTipo} ID ${itemId} conciliado, e registro de conciliação excluído!`, 'green');
        carregarConciliacoesComProblema();

    } catch (error) {
        console.error("Erro ao resolver problema de conciliação:", error);
        showMessage("Erro ao resolver problema: " + error.message, 'red');
    }
}
