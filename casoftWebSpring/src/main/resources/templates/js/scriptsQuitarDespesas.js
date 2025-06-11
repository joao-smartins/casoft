let despesasPendentes = [];
let categoriasMap = {};
let eventosMap = {};

// Inicialização da página
$(document).ready(function() {
    initializePage();
});

function initializePage() {
    // Configurar data padrão
    const hoje = new Date().toISOString().split('T')[0];
    $('#data_pagamento_modal').val(hoje);

    // Event listeners
    setupEventListeners();

    // Carregar dados iniciais
    carregarDados();
}

function setupEventListeners() {
    // Botões principais
    $('#btnAtualizarLista').on('click', carregarDados);
    $('#btnAplicarFiltros').on('click', aplicarFiltros);
    $('#btnLimparFiltros').on('click', limparFiltros);

    // Formulário de quitação
    $('#formQuitarDespesa').on('submit', function(e) {
    e.preventDefault();
    processarQuitacao();
    });

    // Cálculo automático da diferença
    $('#valor_pago_modal').on('input', calcularDiferenca);

    // Modal de quitação
    $('#modalQuitarDespesa').on('hidden.bs.modal', function() {
    limparModalQuitacao();
    });
}

async function carregarDados() {
    try {
        mostrarLoading(true);
        
        // Carregar sequencialmente (um após o outro)
        const despesas = await carregarDespesasPendentes();
        despesasPendentes = despesas;
        
        const categorias = await carregarCategorias();
        const eventos = await carregarEventos();
        
        preencherFiltros(categorias, eventos);
        renderizarDespesas(despesasPendentes);
        atualizarContador();
        
    } catch (error) {
        console.error('Erro ao carregar dados:', error);
        mostrarToast('Erro', 'Erro ao carregar os dados: ' + error.message, 'error');
    } finally {
        mostrarLoading(false);
    }
}

async function carregarDespesasPendentes() {
    try {
        const token = authManager.getToken();
        const response = await fetch('http://localhost:8080/apis/despesa/aguardando', {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token
            }
        });

        if (!response.ok) {
            throw new Error(`Erro HTTP: ${response.status}`);
        }

        const dadosApi = await response.json();
        console.log("DADOS BRUTOS RECEBIDOS DA API:", dadosApi); // Mantenha para debug

        // Mapeamento cuidadoso dos dados da API para o formato do frontend
        const despesasFormatadas = dadosApi.map(item => {
            // --- LÓGICA DE EXTRAÇÃO CORRIGIDA E MAIS SEGURA ---
            const categoriaId = (item.categoria && item.categoria.id) ? item.categoria.id : null;
            const eventoId = (item.evento && item.evento.id) ? item.evento.id : null;

            return {
                id: item.id || 0,
                descricao: item.descricao || 'Sem descrição',
                valor: item.val || 0,
                data_lancamento: item.data_lanc,
                data_vencimento: item.data_venc || new Date().toISOString().split('T')[0],
                status: item.status_conci,
                
                // Guarda os objetos inteiros para exibição
                categoria: item.categoria || { id: null, nome: 'Outros' },
                evento: item.evento || null, 
                
                // Guarda os IDs extraídos corretamente para as requisições
                tipoDespesa_id: categoriaId,
                evento_id: eventoId
            };
        });

        console.log('Despesas formatadas com IDs extraídos:', despesasFormatadas);
        return despesasFormatadas;

    } catch (error) {
        console.error('Erro ao carregar despesas:', error);
        mostrarToast('Erro', 'Não foi possível carregar as despesas pendentes.', 'error');
        return [];
    }
}

async function carregarEventos() {
    try {
    const token = authManager.getToken();
    const response = await fetch('http://localhost:8080/apis/eventos', {
        method: 'GET',
        headers: {
        'Authorization': 'Bearer ' + token
        }
    });

    if (!response.ok) {
        throw new Error(`Erro HTTP: ${response.status}`);
    }

    return await response.json();
    
    } catch (error) {
    console.error('Erro ao carregar eventos:', error);
    return [];
    }
}

async function carregarCategorias() {
    try {
    const token = authManager.getToken();
    const response = await fetch('http://localhost:8080/apis/tipoDespesas', {
        method: 'GET',
        headers: {
        'Authorization': 'Bearer ' + token
        }
    });

    if (!response.ok) {
        throw new Error(`Erro HTTP: ${response.status}`);
    }

    return await response.json();
    
    } catch (error) {
    console.error('Erro ao carregar categorias:', error);
    return []; 
    }
}


function preencherFiltros(categorias, eventos) {
try {
// Verificar se os parâmetros são arrays válidos
if (!Array.isArray(categorias) || !Array.isArray(eventos)) {
    console.error('Dados de filtros inválidos:', {categorias, eventos});
    throw new Error('Dados para filtros devem ser arrays');
}

// Limpar e preencher categorias
const selectCategorias = $('#filtroCategoria');
selectCategorias.find('option:not(:first)').remove();

categorias.forEach(cat => {
    // Verificar se o objeto categoria tem a propriedade 'nome'
    const nomeCategoria = cat?.nome || (typeof cat === 'string' ? cat : 'Categoria inválida');
    selectCategorias.append(`<option value="${nomeCategoria}">${nomeCategoria}</option>`);
    
    // Mapear categoria para uso posterior
    if (typeof cat === 'object') {
    categoriasMap[nomeCategoria] = cat;
    } else {
    categoriasMap[nomeCategoria] = { nome: nomeCategoria };
    }
});

// Limpar e preencher eventos
const selectEventos = $('#filtroEvento');
selectEventos.find('option:not(:first)').remove();

eventos.forEach(evt => {
    // Verificar se o objeto evento tem a propriedade 'nome'
    const nomeEvento = evt?.nome || (typeof evt === 'string' ? evt : 'Evento inválido');
    selectEventos.append(`<option value="${nomeEvento}">${nomeEvento}</option>`);
    
    // Mapear evento para uso posterior
    if (typeof evt === 'object') {
        eventosMap[nomeEvento] = evt;
    } else {
        eventosMap[nomeEvento] = { nome: nomeEvento };
    }
});

console.log('Filtros preenchidos com:', { 
    categorias: categorias.length, 
    eventos: eventos.length 
});

} catch (error) {
console.error('Erro ao preencher filtros:', error);
mostrarToast('Erro', 'Não foi possível carregar os filtros', 'error');
}
}

function renderizarDespesas(despesas) {
    const container = $('#listaDespesas');
    const mensagemVazia = $('#mensagemVazia');
    
    container.empty();

    if (despesas.length === 0) {
    mensagemVazia.show();
    return;
    }

    mensagemVazia.hide();

    despesas.forEach(despesa => {
    const card = criarCardDespesa(despesa);
    container.append(card);
    });
}

function criarCardDespesa(despesa) {
    // Verificação de segurança para o objeto despesa
    if (!despesa || typeof despesa !== 'object') {
        console.error('Dados inválidos da despesa:', despesa);
    return '';
    }

    // Função auxiliar para acessar propriedades de objetos com segurança
    const safeGet = (obj, prop, defaultValue = '') => {
    return obj && obj[prop] !== undefined ? obj[prop] : defaultValue;
    };

    // Extrai os valores com fallbacks seguros
    const descricao = safeGet(despesa, 'descricao', 'Sem descrição');
    const categoria = typeof despesa.categoria === 'object' 
    ? safeGet(despesa.categoria, 'nome', 'Sem categoria') 
    : safeGet(despesa, 'categoria', 'Sem categoria');
    const valor = parseFloat(safeGet(despesa, 'valor', 0)) || 0;
    const evento = typeof despesa.evento === 'object'
    ? safeGet(despesa.evento, 'nome', '')
    : safeGet(despesa, 'evento', '');
    const status = safeGet(despesa, 'status', 'Aguardando');

    // Tratamento seguro para datas
    let dataVenc;
    try {
        dataVenc = new Date(safeGet(despesa, 'data_vencimento', new Date()));
    if (isNaN(dataVenc.getTime())) dataVenc = new Date();
    } catch {
        dataVenc = new Date();
    }

    const hoje = new Date();
    hoje.setHours(0, 0, 0, 0);
    dataVenc.setHours(0, 0, 0, 0);

    const isVencida = dataVenc < hoje;
    const diasAteVencimento = Math.ceil((dataVenc - hoje) / (1000 * 60 * 60 * 24));

    let statusVencimento = '';
    let classVencimento = '';

    if (isVencida) {
        statusVencimento = `Vencida há ${Math.abs(diasAteVencimento)} dia(s)`;
        classVencimento = 'text-danger';
    } else if (diasAteVencimento === 0) {
        statusVencimento = 'Vence hoje';
        classVencimento = 'text-warning';
    } else if (diasAteVencimento <= 7) {
        statusVencimento = `Vence em ${diasAteVencimento} dia(s)`;
        classVencimento = 'text-warning';
    } else {
        statusVencimento = `Vence em ${diasAteVencimento} dias`;
        classVencimento = 'text-muted';
    }

    return `
    <div class="col-md-6 col-lg-4 mb-3">
        <div class="card card-despesa h-100 ${isVencida ? 'border-danger' : ''}">
        <div class="card-body">
            <div class="d-flex justify-content-between align-items-start mb-2">
            <h6 class="card-title mb-0 text-truncate" title="${descricao}">
                ${descricao}
            </h6>
            <span class="badge ${isVencida ? 'bg-danger' : 'bg-warning'} ms-2">
                ${status}
            </span>
            </div>
            
            <div class="mb-2">
            <small class="text-muted">Categoria:</small>
            <div class="fw-bold text-primary">${categoria}</div>
            </div>
            
            <div class="mb-2">
            <small class="text-muted">Valor:</small>
            <div class="fs-5 fw-bold text-success">
                R$ ${valor.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}
            </div>
            </div>
            
            <div class="mb-2">
            <small class="text-muted">Vencimento:</small>
            <div class="${classVencimento} fw-bold">
                ${dataVenc.toLocaleDateString('pt-BR')}
                <br><small>${statusVencimento}</small>
            </div>
            </div>
            
            ${evento ? `
            <div class="mb-3">
                <small class="text-muted">Evento:</small>
                <div class="text-info">${evento}</div>
            </div>
            ` : ''}
            
            <div class="mt-auto">
            <button class="btn btn-quitar btn-success w-100" 
                    onclick="abrirModalQuitacao(${safeGet(despesa, 'id', 0)})">
                <i class="fas fa-money-bill-wave me-2"></i>
                Quitar Despesa
            </button>
            </div>
        </div>
        </div>
    </div>
    `;
}

function abrirModalQuitacao(despesaId) {
    const despesa = despesasPendentes.find(d => d.id === despesaId);
    if (!despesa) return;

    // Extrai o nome da categoria (tratando tanto objeto quanto string)
    const nomeCategoria = typeof despesa.categoria === 'object' 
    ? despesa.categoria.nome 
    : despesa.categoria;

    // Extrai o nome do evento (se aplicável)
    const nomeEvento = typeof despesa.evento === 'object'
    ? despesa.evento.nome
    : despesa.evento;

    // Preencher dados da despesa no modal
    $('#despesa_id_quitar').val(despesa.id);
    $('#modal_descricao').text(despesa.descricao);
    $('#modal_categoria').text(nomeCategoria); 
    $('#modal_valor_original').text(`R$ ${despesa.valor.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}`);
    $('#modal_data_vencimento').text(new Date(despesa.data_vencimento).toLocaleDateString('pt-BR'));

    // Pré-preencher valor pago com o valor original
    $('#valor_pago_modal').val(despesa.valor.toFixed(2));

    // Se houver evento, exiba no modal (opcional)
    if (nomeEvento) {
    $('#modal_evento').text(nomeEvento).parent().show();
    } else {
    $('#modal_evento').parent().hide();
    }

    // Calcular diferença inicial
    calcularDiferenca();

    // Mostrar modal
    const modal = new bootstrap.Modal(document.getElementById('modalQuitarDespesa'));
    modal.show();
}

function calcularDiferenca() {
    const valorOriginal = parseFloat($('#modal_valor_original').text().replace('R$ ', '').replace(/\./g, '').replace(',', '.')) || 0;
    const valorPago = parseFloat($('#valor_pago_modal').val()) || 0;
    
    if (valorPago > 0) {
    const diferenca = valorPago - valorOriginal;
    
    $('#resumo_valor_original').text(`R$ ${valorOriginal.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}`);
    $('#resumo_valor_pago').text(`R$ ${valorPago.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}`);
    
    const diferencaElement = $('#resumo_diferenca');
    if (diferenca > 0) {
        diferencaElement.text(`+ R$ ${diferenca.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}`);
        diferencaElement.removeClass('diferenca-negativa').addClass('diferenca-positiva');
    } else if (diferenca < 0) {
        diferencaElement.text(`- R$ ${Math.abs(diferenca).toLocaleString('pt-BR', { minimumFractionDigits: 2 })}`);
        diferencaElement.removeClass('diferenca-positiva').addClass('diferenca-negativa');
    } else {
        diferencaElement.text('R$ 0,00');
        diferencaElement.removeClass('diferenca-positiva diferenca-negativa');
    }
    
    $('#resumoQuitacao').show();
    } else {
    $('#resumoQuitacao').hide();
    }
}

async function processarQuitacao() {
    try {
        const formData = new FormData(document.getElementById('formQuitarDespesa'));
        const despesaId = parseInt(formData.get('despesa_id_quitar'));
        const valorPago = parseFloat(formData.get('valor_pago_modal'));
        // Pegando os novos campos do formulário
        const dataPagamento = formData.get('data_pagamento_modal') || new Date().toISOString().split('T')[0];
        const observacoes = formData.get('observacoes_modal') || '';

        const despesa = despesasPendentes.find(d => d.id === despesaId);
        if (!despesa) {
            mostrarToast('Erro', 'Despesa não encontrada para processar.', 'error');
            return;
        }

        if (!valorPago || isNaN(valorPago) || valorPago <= 0) {
            mostrarToast('Erro', 'O valor pago é inválido.', 'error');
            return;
        }

        const token = authManager.getToken();
        const headers = { 'Authorization': 'Bearer ' + token };

        // Pagamento parcial
        if (valorPago < despesa.valor) {
            // 1. ATUALIZA A DESPESA ORIGINAL (PUT)
            const paramsPut = new URLSearchParams();
            paramsPut.append('id', despesa.id);
            paramsPut.append('valor', despesa.valor);
            paramsPut.append('data_venc', despesa.data_vencimento);
            paramsPut.append('data_lanc', despesa.data_lancamento);
            paramsPut.append('pagamento', valorPago.toFixed(2));
            paramsPut.append('descricao', despesa.descricao);
            paramsPut.append('status_conci', "Pendente"); 
            paramsPut.append('tipoDespesa_id', despesa.tipoDespesa_id || '0');
            paramsPut.append('usuario_id', authManager.getId());
            paramsPut.append('evento_id', despesa.evento_id || '0');
            // ADICIONANDO NOVOS PARÂMETROS OBRIGATÓRIOS
            paramsPut.append('data_pag', dataPagamento);
            paramsPut.append('obs', observacoes);

            const responsePut = await fetch(`http://localhost:8080/apis/despesa/quitar?${paramsPut.toString()}`, {
                method: 'PUT',
                headers: headers
            });

            if (!responsePut.ok) {
                throw new Error('Falha ao atualizar a despesa original.');
            }

            // 2. CRIA A NOVA DESPESA (POST)
            const diferenca = despesa.valor - valorPago;
            const paramsPost = new URLSearchParams();
            paramsPost.append('descricao', `Restante de: ${despesa.descricao}`);
            paramsPost.append('valor', diferenca.toFixed(2));
            paramsPost.append('data_venc', despesa.data_vencimento);
            paramsPost.append('data_lanc', new Date().toISOString().split('T')[0]);
            paramsPost.append('status_conci', "Aguardando");
            paramsPost.append('tipoDespesa_id', despesa.tipoDespesa_id || '0');
            paramsPost.append('evento_id', despesa.evento_id || '0');
            paramsPost.append('usuario_id', authManager.getId());
            paramsPost.append('pai_id', despesa.id);
            paramsPost.append('pagamento', '0.00');

            const responsePost = await fetch(`http://localhost:8080/apis/despesa/quitar?${paramsPost.toString()}`, {
                method: 'POST',
                headers: headers
            });

            if (!responsePost.ok) {
                throw new Error('Falha ao criar a nova despesa com o valor restante.');
            }
            mostrarToast('Sucesso', 'Pagamento parcial registrado e nova despesa criada!', 'success');
        } else {
            // Pagamento total
            const paramsQuitacao = new URLSearchParams();
            paramsQuitacao.append('id', despesa.id);
            paramsQuitacao.append('valor', despesa.valor);
            paramsQuitacao.append('data_venc', despesa.data_vencimento);
            paramsQuitacao.append('data_lanc', despesa.data_lancamento);
            paramsQuitacao.append('pagamento', valorPago.toFixed(2));
            paramsQuitacao.append('descricao', despesa.descricao);
            paramsQuitacao.append('status_conci', "Pendente");
            paramsQuitacao.append('tipoDespesa_id', despesa.tipoDespesa_id || '0');
            paramsQuitacao.append('usuario_id', authManager.getId());
            paramsQuitacao.append('evento_id', despesa.evento_id || '0');
            // ADICIONANDO NOVOS PARÂMETROS OBRIGATÓRIOS
            paramsQuitacao.append('data_pag', dataPagamento);
            paramsQuitacao.append('obs', observacoes);

            const response = await fetch(`http://localhost:8080/apis/despesa/quitar?${paramsQuitacao.toString()}`, {
                method: 'PUT',
                headers: headers
            });

            if (!response.ok) {
                throw new Error('Erro na quitação.');
            }
            mostrarToast('Sucesso', 'Despesa quitada com sucesso!', 'success');
        }

        await carregarDados();
        const modal = bootstrap.Modal.getInstance(document.getElementById('modalQuitarDespesa'));
        modal.hide();
    } catch (error) {
        console.error('Erro no processo de quitação:', error);
        mostrarToast('Erro', error.message || 'Não foi possível completar a operação.', 'error');
    }
}

function aplicarFiltros() {
    try {
        const categoriaSelecionada = $('#filtroCategoria').val();
        const eventoSelecionado = $('#filtroEvento').val();
        const vencimentoSelecionado = $('#filtroVencimento').val();

        let despesasFiltradas = [...despesasPendentes];

        // Filtrar por categoria
        if (categoriaSelecionada) {
            despesasFiltradas = despesasFiltradas.filter(d => {
                const categoriaDespesa = typeof d.categoria === 'object' ? d.categoria.nome : d.categoria;
                return categoriaDespesa === categoriaSelecionada;
            });
        }

        // Filtrar por evento
        if (eventoSelecionado) {
            despesasFiltradas = despesasFiltradas.filter(d => {
                const eventoDespesa = typeof d.evento === 'object' ? d.evento.nome : d.evento;
                return eventoDespesa === eventoSelecionado;
            });
        }

        // Filtrar por vencimento (versão corrigida)
        if (vencimentoSelecionado) {
            const hoje = new Date();
            hoje.setHours(0, 0, 0, 0); // Normaliza para início do dia
            
            despesasFiltradas = despesasFiltradas.filter(d => {
                if (!d.data_vencimento) return false;
                
                try {
                    const dataVenc = new Date(d.data_vencimento);
                    dataVenc.setHours(0, 0, 0, 0); // Normaliza a data de vencimento
                    
                    const diffTime = dataVenc - hoje;
                    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)); 
                    
                    switch (vencimentoSelecionado) {
                        case 'vencidas':
                            return diffDays < 0; // Dias negativos = vencidas
                        case 'hoje':
                            return diffDays === 0;
                        case 'proximos7dias':
                            return diffDays >= 0 && diffDays <= 7;
                        default:
                            return true;
                    }
                } catch (e) {
                    console.error('Erro ao processar data:', d.data_vencimento, e);
                    return false; // Remove itens com datas inválidas
                }
            });
        }

        renderizarDespesas(despesasFiltradas);
        atualizarContador(despesasFiltradas.length);

    } catch (error) {
        console.error('Erro ao aplicar filtros:', error);
        mostrarToast('Erro', 'Falha ao aplicar filtros', 'error');
        renderizarDespesas(despesasPendentes);
    }
}

function limparFiltros() {
    $('#filtroCategoria').val('');
    $('#filtroEvento').val('');
    $('#filtroVencimento').val('');
    renderizarDespesas(despesasPendentes);
    atualizarContador();
}

function atualizarContador(quantidade = null) {
    const total = quantidade !== null ? quantidade : despesasPendentes.length;

    
    // Alterar cor do badge baseado na quantidade
    const badge = $('#contadorDespesas');
    badge.removeClass('bg-info bg-warning bg-success');
    
    if (total === 0) {
    badge.addClass('bg-success');
    } else if (total <= 5) {
    badge.addClass('bg-info');
    } else {
    badge.addClass('bg-warning');
    }
}

function limparModalQuitacao() {
    $('#formQuitarDespesa')[0].reset();
    $('#resumoQuitacao').hide();
    const hoje = new Date().toISOString().split('T')[0];
    $('#data_pagamento_modal').val(hoje);
}

function mostrarLoading(show) {
    const loadingArea = $('#loadingArea');
    const filtrosArea = $('#filtrosArea');
    const listaDespesas = $('#listaDespesas');
    
    if (show) {
    loadingArea.show();
    filtrosArea.hide();
    listaDespesas.hide();
    $('#mensagemVazia').hide();
    } else {
    loadingArea.hide();
    filtrosArea.show();
    listaDespesas.show();
    }
}

function mostrarToast(titulo, mensagem, tipo = 'info') {
    const toast = $('#toastNotificacao');
    const icon = $('#toastIcon');
    const tituloElement = $('#toastTitulo');
    const mensagemElement = $('#toastMensagem');
    
    // Configurar ícone e cor baseado no tipo
    icon.removeClass('fa-info-circle fa-check-circle fa-exclamation-triangle fa-times-circle text-primary text-success text-warning text-danger');
    
    switch (tipo) {
    case 'success':
        icon.addClass('fa-check-circle text-success');
        break;
    case 'warning':
        icon.addClass('fa-exclamation-triangle text-warning');
        break;
    case 'error':
        icon.addClass('fa-times-circle text-danger');
        break;
    default:
        icon.addClass('fa-info-circle text-primary');
    }
    
    tituloElement.text(titulo);
    mensagemElement.text(mensagem);
    
    const bsToast = new bootstrap.Toast(toast[0]);
    bsToast.show();
}
