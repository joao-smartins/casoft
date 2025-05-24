// Array para armazenar os itens não conciliados e seus estados temporários
let itensNaoConciliados = [];
// Array para armazenar apenas as conciliações marcadas como pendentes
let conciliacoesPendentesParaSalvar = [];
// Variáveis para guardar o item que está sendo editado no modal
let currentEditingItemId = null;
let currentEditingItemType = null;

// Adiciona um listener para carregar os itens de conciliação quando a página estiver pronta
document.addEventListener("DOMContentLoaded", carregarItensConciliacao);

// Função principal para carregar e exibir os itens de conciliação
async function carregarItensConciliacao() {
    try {
        // Faz a chamada real para o backend para buscar itens não conciliados
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

        // Se a resposta for uma lista vazia ou nula (sem "ok" ou "erro"),
        // ou se o 'ok' estiver vazio, trate como sem itens
        if (!data || data.length === 0 || (data.ok && data.ok.length === 0)) {
            itensNaoConciliados = []; // Garante que a lista está vazia
        } else {
            // Se houver "ok", assume que o array de itens está dentro dele (como no seu TipoDespesasView)
            const itensRecebidos = data.ok ? data.ok : data;
            itensNaoConciliados = itensRecebidos.map(item => ({
                id: item.id,
                tipo: item.tipo,
                valor: item.valor,
                descricao: item.descricao,
                statusConciliacao: item.statusConciliacao || 'AGUARDANDO' // Assume 'AGUARDANDO' se o status for nulo
            }));
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

    if (itensNaoConciliados.length === 0) {
        const row = tableBody.insertRow();
        const cell = row.insertCell();
        cell.colSpan = 6; // Cobre todas as colunas
        cell.textContent = "Nenhum item para conciliação encontrado.";
        cell.style.textAlign = 'center'; // Centraliza a mensagem
        return;
    }

    itensNaoConciliados.forEach(item => {
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
        conciliadoButton.onclick = () => handleConciliacao(item.id, item.tipo, 'CONCILIADO');
        actionsCell.appendChild(conciliadoButton);

        // Botão PENDENTE
        const pendingButton = document.createElement('button');
        pendingButton.textContent = 'Pendente';
        pendingButton.className = 'btn btn-sm btn-warning ms-2'; // Estilo Bootstrap com margem
        pendingButton.onclick = () => handleConciliacao(item.id, item.tipo, 'PENDENTE');
        actionsCell.appendChild(pendingButton);
    });
}

// Função para lidar com a ação de conciliação (CONCILIADO ou PENDENTE)
async function handleConciliacao(id, tipo, status) {
    // Encontra o item na lista local
    const itemIndex = itensNaoConciliados.findIndex(item => item.id === id && item.tipo === tipo);
    if (itemIndex === -1) {
        showMessage("Erro: Item não encontrado na lista para conciliação.", 'red');
        return;
    }

    const item = itensNaoConciliados[itemIndex];

    if (status === 'CONCILIADO') {
        // Confirmação antes de conciliar
        if (!confirm(`Deseja marcar este item (${item.tipo} ID: ${item.id}) como CONCILIADO?`)) {
            return;
        }

        const endpoint = tipo === 'Receita'
            ? `http://localhost:8080/apis/conciliacao/receita/${id}/status`
            : `http://localhost:8080/apis/conciliacao/despesa/${id}/status`;

        try {
            const response = await fetch(endpoint, {
                method: 'PUT'
            });

            const result = await response.json(); // Espera um Map de retorno (com "ok" ou "erro")
            if (response.ok && result.ok) {
                // Remove o item da lista de não conciliados e renderiza a tabela
                itensNaoConciliados.splice(itemIndex, 1); // Remove 1 item a partir do index
                renderizarTabelaConciliacao();
                showMessage(result.ok, 'green'); // Exibe a mensagem de sucesso
            } else {
                throw new Error(result.erro || `Erro desconhecido ao atualizar status para ${status}.`);
            }
        } catch (error) {
            console.error("Erro ao atualizar status:", error);
            showMessage("Erro ao atualizar status: " + error.message, 'red');
        }
    } else if (status === 'PENDENTE') {
        // Armazena os dados do item para uso no modal de problema
        currentEditingItemId = id;
        currentEditingItemType = tipo;
        showProblemaModal();
    }
}

// Função para exibir a janela modal de problema
function showProblemaModal() {
    const modal = document.getElementById("problemaModal");
    const dataProblemaInput = document.getElementById("dataProblema");
    // Define a data atual como valor padrão
    dataProblemaInput.value = new Date().toISOString().split('T')[0];
    document.getElementById("descricaoProblema").value = ''; // Limpa a descrição
    modal.style.display = "flex"; // Usa flex para centralizar o modal
}

// Função para fechar a janela modal de problema
function closeProblemaModal() {
    const modal = document.getElementById("problemaModal");
    modal.style.display = "none";
    currentEditingItemId = null;
    currentEditingItemType = null;
}

// Função para salvar os detalhes do problema (chamada pelo modal)
async function saveProblema() {
    const dataProblema = document.getElementById("dataProblema").value;
    const descricaoProblema = document.getElementById("descricaoProblema").value.trim();

    if (!descricaoProblema) {
        showMessage("A descrição do problema não pode estar vazia.", 'red');
        return;
    }

    const itemIndex = itensNaoConciliados.findIndex(
        item => item.id === currentEditingItemId && item.tipo === currentEditingItemType
    );

    if (itemIndex !== -1) {
        const item = itensNaoConciliados[itemIndex];

        // Cria um objeto Conciliacao conforme o modelo Java para envio ao backend
        const conciliacaoProblema = {
            concDtProblema: dataProblema, // FormatoISO (yyyy-MM-DD)
            concDescProblema: descricaoProblema,
            concDtSolucao: null, // Inicialmente nulo
            concDescSolucao: null, // Inicialmente nulo
            // Define o ID da receita ou despesa, e o outro como 0 (que o backend vai converter para NULL)
            concReceitaId: item.tipo === 'Receita' ? item.id : 0,
            concDespesaId: item.tipo === 'Despesa' ? item.id : 0
        };

        // Adiciona à lista local para envio posterior (será salvo em lote)
        conciliacoesPendentesParaSalvar.push(conciliacaoProblema);

        // Remove o item da lista de não conciliados para que não apareça mais na tabela
        itensNaoConciliados.splice(itemIndex, 1); // Remove 1 item a partir do index
        renderizarTabelaConciliacao(); // Renderiza a tabela novamente para remover o item

        showMessage(`Problema para ${item.tipo} ID ${item.id} registrado como pendente e removido da lista.`, 'green');
        closeProblemaModal();
    } else {
        showMessage("Erro: Item não encontrado para registrar problema.", 'red');
        closeProblemaModal();
    }
}

// Função para exibir mensagens na tela (sucesso/erro)
function showMessage(message, type) {
    const msgDiv = document.getElementById("mensagem");
    msgDiv.textContent = message;
    msgDiv.className = `alert ${type === 'red' ? 'alert-danger' : 'alert-success'}`; // Estilo Bootstrap
    msgDiv.style.display = 'block';
    setTimeout(() => {
        msgDiv.style.display = 'none';
    }, 5000);
}

// Função para salvar todas as conciliações pendentes (enviar para o backend em lote)
async function salvarConciliacoesPendentes() {
    if (conciliacoesPendentesParaSalvar.length === 0) {
        showMessage("Nenhuma conciliação pendente para salvar.", 'red');
        return;
    }

    // Confirmação antes de salvar em lote
    if (!confirm(`Deseja salvar ${conciliacoesPendentesParaSalvar.length} conciliações pendentes?`)) {
        return;
    }

    try {
        const response = await fetch("http://localhost:8080/apis/conciliacao/problemas", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(conciliacoesPendentesParaSalvar)
        });

        const result = await response.json(); // Espera um Map de retorno (com "ok" ou "erro")
        if (response.ok && result.ok) {
            showMessage(result.ok, 'green'); // Exibe a mensagem de sucesso
            conciliacoesPendentesParaSalvar = []; // Limpa a lista após salvar com sucesso
            // Não é necessário recarregar carregarItensConciliacao() aqui, pois os itens já foram removidos da lista
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
        alert('Função Deslogar simulada. Redirecionaria para a página de login.');
        // window.location.href = 'login.html'; // Descomente para redirecionar
    }
};

// Adiciona o link de Conciliação à sidebar (se ainda não estiver lá)
document.addEventListener('DOMContentLoaded', () => {
    const sidebar = document.getElementById('sidebar');
    if (sidebar) {
        // Verifica se o link de conciliação já existe para evitar duplicatas
        if (!sidebar.querySelector('a[href="conciliacao.html"]')) {
            const conciliacaoLink = document.createElement('a');
            conciliacaoLink.href = 'conciliacao.html';
            conciliacaoLink.textContent = 'Conciliação';
            // Encontra o link de Tipos de Despesas para inserir o novo link depois
            const tiposDespesasLink = sidebar.querySelector('a[href="tipoDespesa.html"]');
            if (tiposDespesasLink) {
                tiposDespesasLink.parentNode.insertBefore(conciliacaoLink, tiposDespesasLink.nextSibling);
            } else {
                sidebar.appendChild(conciliacaoLink); // Adiciona no final se não encontrar
            }
        }
    }
});