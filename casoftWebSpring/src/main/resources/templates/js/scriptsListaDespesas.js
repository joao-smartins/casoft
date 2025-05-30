
// Variáveis globais
let despesaSelecionadaId = null;
let tabelaDespesas = null;
const modalConfirmacao = new bootstrap.Modal(document.getElementById('confirmModal'));
const API_BASE_URL = "http://localhost:8080/apis";

// Inicialização quando o DOM estiver carregado
document.addEventListener('DOMContentLoaded', function() {
    inicializarPagina();
});

async function inicializarPagina() {
    try {
        await carregarEventos();
        await carregarCategorias();
        await carregarEExibirDespesas();
        
        configurarEventListeners();
    } catch (error) {
    console.error('Erro na inicialização:', error);
    mostrarMensagem('Erro ao carregar dados iniciais', 'error');
    }
}

function configurarEventListeners() {
    // Botão de nova despesa
    document.getElementById('btnMostrarFormulario').addEventListener('click', toggleFormulario);
    
    // Botão de confirmação do modal
    document.getElementById('btnConfirmAction').addEventListener('click', confirmarAcao);
    
    // Formulário de cadastro
    document.getElementById('formDespesa').addEventListener('submit', handleSubmitForm);
    
    // Filtros
    document.getElementById('btnAplicarFiltros').addEventListener('click', aplicarFiltros);
    document.getElementById('btnLimparFiltros').addEventListener('click', limparFiltros);
    
    // Auto-aplicar filtros quando mudarem
    ['filtroStatus', 'filtroDataInicio', 'filtroDataFim'].forEach(id => {
    document.getElementById(id).addEventListener('change', aplicarFiltros);
    });
}
// === FUNÇÕES DE CARREGAMENTO DE DADOS ===
async function carregarDespesas() {
    try {
        const token = authManager.getToken();
        const response = await fetch("http://localhost:8080/apis/despesa", {
            headers: {
                'Authorization': 'Bearer ' + token
            }
        });

        if (!response.ok) throw new Error('Erro ao carregar despesas');

        const despesas = await response.json();
        return despesas;

    } catch (error) {
        console.error('Erro:', error);
        alert('Não foi possível carregar as despesas');
        return [];
    }
}

async function carregarEExibirDespesas() {
    try {
    const despesas = await carregarDespesas();
    
    // Destruir tabela existente se houver
    if (tabelaDespesas) {
        tabelaDespesas.destroy();
    }
    
    // Inicializar nova tabela
    tabelaDespesas = $('#tabela-despesas').DataTable({
        data: despesas,
        columns: [
        { 
            data: 'descricao', 
            title: 'Descrição',
            render: function(data, type, row) {
            if (type === 'display' && data && data.length > 50) {
                return `<span title="${data}" class="truncated-desc">${data.substring(0, 30)}...</span>`;
            }
            return data || 'Sem descrição';
            }
        },
        {
            data: 'categoria',
            title: 'Categoria',
            render: data => data?.nome || 'Sem categoria'
        },
        {
            data: 'val',
            title: 'Valor (R$)',
            render: data => formatarMoeda(data),
            className: 'text-end'
        },
        {
            data: 'data_venc',
            title: 'Vencimento',
            render: data => formatarData(data),
            className: 'text-center'
        },
        {
            data: 'status_conci',
            title: 'Status',
            render: data => `<span class="${getStatusClass(data)}">${data || 'Indefinido'}</span>`,
            className: 'text-center'
        },
        {
            data: 'evento',
            title: 'Evento',
            render: data => data?.nome || '<em class="text-muted">Nenhum</em>'
        },
        {
            data: null,
            title: 'Ações',
            render: (data, type, row) => `
            <div class="action-buttons">
                <button class="btn btn-sm btn-warning" onclick="editarDespesa(${row.id})" title="Editar">
                	<i class="bi bi-pencil-square"></i>
                </button>
                <button class="btn btn-sm btn-danger" onclick="confirmarExclusao(${row.id})" title="Excluir">
                	<i class="bi bi-trash"></i>
                </button>
            </div>
            `,
            orderable: false,
            className: 'text-center'
        }
        ],
        language: {
        url: 'https://cdn.datatables.net/plug-ins/1.11.5/i18n/pt-BR.json'
        },
        order: [[3, 'asc']], // Ordenar por vencimento
        responsive: true,
        scrollX: true,
        autoWidth: false,
        pageLength: 5,
        lengthMenu: [[5,10, 25, 50, 100, -1], [5,10, 25, 50, 100, "Todos"]]
        
    });
    // Aguarda 100ms antes de ajustar as colunas
    $(window).on('resize', function() {
        if (tabelaDespesas) {
            tabelaDespesas.columns.adjust().responsive.recalc();
        }
    });
    } catch (error) {
    console.error('Erro ao exibir despesas:', error);
    }
}

async function carregarCategorias() {
    try {
    const token = authManager.getToken();
    const response = await fetch(`${API_BASE_URL}/tipoDespesas`, {
        method: 'GET',
        headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
        }
    });
    
    const selectCategoria = document.getElementById('tipoDespesa_id');
    
    if (response.ok) {
        const categorias = await response.json();
        selectCategoria.innerHTML = '<option value="">Selecione uma categoria</option>';
        
        categorias.forEach(categoria => {
        const option = document.createElement('option');
        option.value = categoria.id;
        option.textContent = categoria.nome;
        selectCategoria.appendChild(option);
        });
    } else {
        throw new Error('Erro ao carregar categorias');
    }
    } catch (error) {
    console.error('Erro ao carregar categorias:', error);
    document.getElementById('tipoDespesa_id').innerHTML = '<option value="">Erro ao carregar categorias</option>';
    }
}

async function carregarEventos() {
    try {
    const token = authManager.getToken();
    const response = await fetch(`${API_BASE_URL}/eventos`, {
        method: 'GET',
        headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
        }
    });
    
    const selectEvento = document.getElementById('evento_id');
    
    if (response.ok) {
        const eventos = await response.json();
        selectEvento.innerHTML = '<option value="">Nenhum evento selecionado</option>';
        
        eventos.forEach(evento => {
        const option = document.createElement('option');
        option.value = evento.id;
        option.textContent = evento.nome;
        selectEvento.appendChild(option);
        });
    } else {
        throw new Error('Erro ao carregar eventos');
    }
    } catch (error) {
    console.error('Erro ao carregar eventos:', error);
    document.getElementById('evento_id').innerHTML = '<option value="">Erro ao carregar eventos</option>';
    }
}

// === FUNÇÕES DE MANIPULAÇÃO DO FORMULÁRIO ===

function toggleFormulario() {
    const formulario = document.getElementById('formularioDespesa');
    const btn = document.getElementById('btnMostrarFormulario');
    
    if (formulario.style.display === 'none' || formulario.style.display === '') {
    formulario.style.display = 'block';
    btn.textContent = 'Cancelar';
    btn.className = 'btn btn-secondary';
    formulario.scrollIntoView({ behavior: 'smooth' });
    } else {
    formulario.style.display = 'none';
    btn.textContent = 'Nova Despesa';
    btn.className = 'btn btn-primary';
    limparFormulario();
    }
}

function limparFormulario() {
    document.getElementById('formDespesa').reset();
    limparErrosValidacao();
}

function limparErrosValidacao() {
    document.querySelectorAll('.error-message').forEach(el => el.textContent = '');
    document.querySelectorAll('.is-invalid').forEach(el => el.classList.remove('is-invalid'));
}

async function handleSubmitForm(event) {
    event.preventDefault();

    if (!validarFormulario()) {
        return;
    }

    const btnSubmit = document.getElementById('btnSubmit');
    const originalText = btnSubmit.innerHTML;

    try {
        btnSubmit.disabled = true;
        btnSubmit.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Cadastrando...';

        const formData = new FormData(event.target);
        const dados = new URLSearchParams();

        const dataLanc = new Date().toISOString().split('T')[0];
        const usuarioId = localStorage.getItem('usuario_id');

        for (const [key, value] of formData.entries()) {
            if (key === 'evento_id') {
                dados.append('evento_id', value ? value : '');
            } else {
                dados.append(key, value);
            }
        }
        dados.append('pagamento', 0);
        dados.append('data_lanc', dataLanc);
        dados.append('usuario_id', authManager.getId());
        dados.append('status_conci', "Aguardando");

        const token = authManager.getToken();
        const response = await fetch(`${API_BASE_URL}/despesa`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: dados.toString()
        });

        if (response.ok) {
            criarToast('Despesa cadastrada com sucesso!', 'success');
            toggleFormulario();
            await carregarEExibirDespesas();
        } else {
            const errorData = await response.json().catch(() => ({}));
            throw new Error(errorData.message || `Erro HTTP ${response.status}`);
        }

    } catch (error) {
        console.error('Erro ao cadastrar despesa:', error);
        criarToast(`Erro ao cadastrar despesa: ${error.message}`, 'error');
    } finally {
        btnSubmit.disabled = false;
        btnSubmit.innerHTML = originalText;
    }
}

function validarFormulario() {
    limparErrosValidacao();
    let isValid = true;

    // Validar categoria
    const categoria = document.getElementById('tipoDespesa_id');
    if (!categoria.value) {
    mostrarErroValidacao('tipoDespesa_id', 'Selecione uma categoria');
    isValid = false;
    }

    // Validar valor
    const valor = document.getElementById('valor');
    if (!valor.value || parseFloat(valor.value) <= 0) {
    mostrarErroValidacao('valor', 'Informe um valor válido maior que zero');
    isValid = false;
    }

    // Validar data de vencimento
    const dataVenc = document.getElementById('data_venc');
    if (!dataVenc.value) {
    mostrarErroValidacao('data_venc', 'Informe a data de vencimento');
    isValid = false;
    }


    return isValid;
}

function mostrarErroValidacao(fieldId, message) {
    const field = document.getElementById(fieldId);
    const errorElement = document.getElementById(`error-${fieldId}`);
    
    field.classList.add('is-invalid');
    if (errorElement) {
    errorElement.textContent = message;
    }
}

// === FUNÇÕES DE AÇÕES (EDITAR/EXCLUIR) ===

function editarDespesa(id) {
    window.location.href = `AlterarDespesa.html?id=${id}`;
}

function confirmarExclusao(id) {
    despesaSelecionadaId = id;
    document.getElementById('modalMessage').textContent = 'Deseja realmente excluir esta despesa? Esta ação não pode ser desfeita.';
    document.getElementById('btnConfirmAction').className = 'btn btn-danger';
    document.getElementById('btnConfirmAction').textContent = 'Excluir';
    modalConfirmacao.show();
}

async function confirmarAcao() {
    if (despesaSelecionadaId) {
    await excluirDespesa();
    }
}

async function excluirDespesa() {
    try {
    const token = authManager.getToken();
    const response = await fetch(`${API_BASE_URL}/despesa/${despesaSelecionadaId}`, {
        method: 'DELETE',
        headers: {
        'Authorization': `Bearer ${token}`
        }
    });

    if (response.ok) {
        mostrarMensagem('Despesa excluída com sucesso!', 'success');
        await carregarEExibirDespesas();
    } else {
        throw new Error(`Erro HTTP ${response.status}`);
    }
    } catch (error) {
    console.error('Erro ao excluir despesa:', error);
    mostrarMensagem('Erro ao excluir despesa', 'error');
    } finally {
    modalConfirmacao.hide();
    despesaSelecionadaId = null;
    }
}

// === FUNÇÕES DE FILTROS ===

function aplicarFiltros() {
    if (!tabelaDespesas) return;

    const filtroStatus = document.getElementById('filtroStatus').value;
    const filtroDataInicio = document.getElementById('filtroDataInicio').value;
    const filtroDataFim = document.getElementById('filtroDataFim').value;

    // Limpa filtros anteriores de data para não acumular
    $.fn.dataTable.ext.search = $.fn.dataTable.ext.search.filter(fn => fn.name !== 'filtroPorData');

    // Filtro de status (coluna 4)
    tabelaDespesas.column(4).search(filtroStatus);

    if (filtroDataInicio || filtroDataFim) {
        const dataInicio = filtroDataInicio ? new Date(filtroDataInicio) : null;
        const dataFim = filtroDataFim ? new Date(filtroDataFim) : null;

        if (dataInicio && isNaN(dataInicio.getTime())) {
            criarToast('Data inicial inválida', 'error');
            return;
        }

        if (dataFim && isNaN(dataFim.getTime())) {
            criarToast('Data final inválida', 'error');
            return;
        }

        if (dataInicio && dataFim && dataInicio > dataFim) {
            criarToast('A data inicial não pode ser maior que a data final', 'error');
            return;
        }

        // Adiciona o filtro personalizado
        const filtroPorData = function(settings, data, dataIndex) {
            const dataDespesaStr = data[3]; // Ajuste conforme a coluna da sua data
            if (!dataDespesaStr) return true;

            // Converter data no formato dd/mm/yyyy
            const partes = dataDespesaStr.split('/');
            if (partes.length !== 3) return false;
            const dataDespesa = new Date(`${partes[2]}-${partes[1]}-${partes[0]}`); // yyyy-mm-dd

            if (isNaN(dataDespesa.getTime())) return false;

            const depoisDeInicio = !dataInicio || dataDespesa >= dataInicio;
            const antesDeFim = !dataFim || dataDespesa <= dataFim;

            return depoisDeInicio && antesDeFim;
        };
        // Nomear função para poder removê-la depois
        Object.defineProperty(filtroPorData, 'name', { value: 'filtroPorData' });

        $.fn.dataTable.ext.search.push(filtroPorData);
    }

    tabelaDespesas.draw();
}


// Adicionar event listeners para os filtros
document.getElementById('filtroDataInicio').addEventListener('change', aplicarFiltros);
document.getElementById('filtroDataFim').addEventListener('change', aplicarFiltros);
document.getElementById('filtroStatus').addEventListener('change', aplicarFiltros);

function limparFiltros() {
    document.getElementById('filtroStatus').value = '';
    document.getElementById('filtroDataInicio').value = '';
    document.getElementById('filtroDataFim').value = '';

    // Remove filtro de datas
    $.fn.dataTable.ext.search = $.fn.dataTable.ext.search.filter(fn => fn.name !== 'filtroPorData');

    if (tabelaDespesas) {
        tabelaDespesas.search('').columns().search('').draw();
    }
}

// === FUNÇÕES UTILITÁRIAS ===

function formatarMoeda(valor) {
    return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL'
    }).format(valor || 0);
}

function formatarData(dataString) {
    if (!dataString) return '';
    try {
    return new Date(dataString + 'T00:00:00').toLocaleDateString('pt-BR');
    } catch {
    return dataString;
    }
}

function getStatusClass(status) {
    const statusLower = (status || '').toLowerCase();
    return `status-${statusLower}`;
}

function mostrarLoading(show) {
    const loadingArea = document.getElementById('loadingArea');
    const tabelaArea = document.querySelector('.table-responsive');
    const filtrosArea = document.getElementById('filtrosArea');
    
    if (show) {
    loadingArea.style.display = 'block';
    tabelaArea.style.display = 'none';
    filtrosArea.style.display = 'none';
    } else {
    loadingArea.style.display = 'none';
    tabelaArea.style.display = 'block';
    filtrosArea.style.display = 'flex';
    }
}

function mostrarMensagem(mensagem, tipo = 'info') {
    // Implementação simples com alert - pode ser substituída por toast/notification
    if (tipo === 'success') {
    alert('✅ ' + mensagem);
    } else if (tipo === 'error') {
    alert('❌ ' + mensagem);
    } else {
    alert('ℹ️ ' + mensagem);
    }
}

// === FUNÇÕES DE NOTIFICAÇÃO AVANÇADA (OPCIONAL) ===

function criarToast(mensagem, tipo = 'info') {
    // Criar container de toasts se não existir
    let toastContainer = document.getElementById('toast-container');
    if (!toastContainer) {
    toastContainer = document.createElement('div');
    toastContainer.id = 'toast-container';
    toastContainer.className = 'toast-container position-fixed top-0 end-0 p-3';
    toastContainer.style.zIndex = '9999';
    document.body.appendChild(toastContainer);
    }

    // Criar o toast
    const toastId = 'toast-' + Date.now();
    const toastHtml = `
    <div id="${toastId}" class="toast align-items-center text-white bg-${getTipoBootstrap(tipo)} border-0" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="d-flex">
        <div class="toast-body">
            ${getIconeTipo(tipo)} ${mensagem}
        </div>
        <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
    </div>
    `;
    
    toastContainer.insertAdjacentHTML('beforeend', toastHtml);
    
    // Mostrar o toast
    const toastElement = document.getElementById(toastId);
    const bsToast = new bootstrap.Toast(toastElement, {
    delay: tipo === 'error' ? 8000 : 4000
    });
    bsToast.show();
    
    // Remover o toast após ser fechado
    toastElement.addEventListener('hidden.bs.toast', () => {
    toastElement.remove();
    });
}

function getTipoBootstrap(tipo) {
    switch(tipo) {
    case 'success': return 'success';
    case 'error': return 'danger';
    case 'warning': return 'warning';
    default: return 'primary';
    }
}

function getIconeTipo(tipo) {
    switch(tipo) {
    case 'success': return '✅';
    case 'error': return '❌';
    case 'warning': return '⚠️';
    default: return 'ℹ️';
    }
}
function editarDespesa(id) {
  const despesa = despesas.find(d => d.id === id);
  if (!despesa) {
    alert('Despesa não encontrada!');
    return;
  }

  // Preencher os campos do formulário de edição
  $('#id_editar').val(despesa.id);
  $('#descricao_editar').val(despesa.descricao);
  $('#valor_editar').val(despesa.val);
  $('#pagamento_editar').val(despesa.valor_pago ?? '');
  $('#data_venc_editar').val(despesa.data_venc);
  $('#status_conci_editar').val(despesa.status_conci);

  // Abrir o modal
  const modal = new bootstrap.Modal(document.getElementById('modalEditarDespesa'));
  modal.show();
}
async function atualizarDespesa(despesa) {
    try {
        const token = authManager.getToken();

        const response = await fetch(`http://localhost:8080/apis/despesa/${despesa.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            },
            body: JSON.stringify(despesa)
        });

        if (!response.ok) {
            throw new Error('Erro ao atualizar a despesa');
        }

        const despesaAtualizada = await response.json();
        return despesaAtualizada;

    } catch (error) {
        console.error('Erro:', error);
        alert('Não foi possível atualizar a despesa');
        throw error;
    }
}
$('#formEditarDespesa').on('submit', async function(e) {
    e.preventDefault();

    const id = parseInt($('#id_editar').val());

    const despesaAtualizada = {
        id: id,
        descricao: $('#descricao_editar').val(),
        val: parseFloat($('#valor_editar').val()),
        valor_pago: $('#pagamento_editar').val() ? parseFloat($('#pagamento_editar').val()) : null,
        data_venc: $('#data_venc_editar').val(),
        status_conci: $('#status_conci_editar').val()
    };

    try {
        const resposta = await atualizarDespesa(despesaAtualizada);

        // Atualiza localmente na lista de despesas, se necessário
        const index = despesas.findIndex(d => d.id === id);
        if (index !== -1) {
            despesas[index] = resposta;
        }

        // Atualiza a tabela
        tabelaDespesas.clear().rows.add(despesas).draw();

        // Fecha o modal
        bootstrap.Modal.getInstance(document.getElementById('modalEditarDespesa')).hide();

        alert('Despesa atualizada com sucesso!');
    } catch (error) {
        console.error('Falha ao atualizar despesa:', error);
    }
});
(() => {
  'use strict'

  // Pega todos os formulários que possuem a classe 'needs-validation'
  const forms = document.querySelectorAll('.needs-validation')

  Array.from(forms).forEach(form => {
    form.addEventListener('submit', event => {
      if (!form.checkValidity()) {
        event.preventDefault()
        event.stopPropagation()
      }

      form.classList.add('was-validated')
    }, false)
  })
})()

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('formDespesa');
    const dataVenc = document.getElementById('data_venc');
    const erroDataInvalida = document.getElementById('erroDataInvalida');
    const erroDataObrigatoria = document.getElementById('erroDataObrigatoria');

    // Impede datas menores que hoje
    const hoje = new Date().toISOString().split('T')[0];
    dataVenc.setAttribute('min', hoje);

    form.addEventListener('submit', (event) => {
        let formValido = true;

        // Resetar mensagens de erro da data
        erroDataInvalida.style.display = 'none';
        erroDataObrigatoria.style.display = 'none';
        dataVenc.classList.remove('is-invalid');

        // Validação da data
        if (!dataVenc.value) {
            erroDataObrigatoria.style.display = 'block';
            dataVenc.classList.add('is-invalid');
            formValido = false;
        } else if (dataVenc.value < hoje) {
            erroDataInvalida.style.display = 'block';
            dataVenc.classList.add('is-invalid');
            formValido = false;
        }

        // Validação padrão Bootstrap
        if (!form.checkValidity()) {
            form.classList.add('was-validated');
            formValido = false;
        }

        if (!formValido) {
            event.preventDefault();
            event.stopPropagation();
        }
    });

    // Remove erros quando o usuário corrige
    document.querySelectorAll('input, select, textarea').forEach((campo) => {
        campo.addEventListener('input', () => {
            if (campo.checkValidity()) {
                campo.classList.remove('is-invalid');
                campo.classList.add('is-valid');
                if (campo.id === 'data_venc') {
                    erroDataObrigatoria.style.display = 'none';
                    erroDataInvalida.style.display = 'none';
                }
            } else {
                campo.classList.remove('is-valid');
            }
        });
    });
});