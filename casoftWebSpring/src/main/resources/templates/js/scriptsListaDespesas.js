// Variáveis globais
let despesaSelecionadaId = null;
let tabelaDespesas = null;
const modalConfirmacao = new bootstrap.Modal(document.getElementById('confirmModal'));
const API_BASE_URL = "http://localhost:8080/apis";
let despesas = [];

document.addEventListener('DOMContentLoaded', function() {
    inicializarPagina();
});

async function inicializarPagina() {
    try {
        await carregarEventos();
        await carregarCategorias();
        await carregarEExibirDespesas();
        
        configurarEventListeners();
        configurarValidacoesFormularios();
    } catch (error) {
        console.error('Erro na inicialização:', error);
        criarToast('Erro ao carregar dados iniciais', 'error');
    }
}

function configurarEventListeners() {
    document.getElementById('btnLimparFiltros').addEventListener('click', limparFiltros);
    document.getElementById('btnMostrarFormulario').addEventListener('click', toggleFormularioCadastro);
    document.getElementById('btnConfirmAction').addEventListener('click', confirmarAcao);
    document.getElementById('formDespesa').addEventListener('submit', handleSubmitFormCadastro);
    document.getElementById('formEditarDespesa').addEventListener('submit', handleSubmitFormEdicao);
    document.getElementById('formEditarDespesa').querySelector('.btn-outline-secondary').addEventListener('click', limparFormularioEdicao);

    ['filtroStatus', 'filtroDataInicio', 'filtroDataFim'].forEach(id => {
        document.getElementById(id).addEventListener('change', aplicarFiltros);
    });

    // Event listeners for data validation (already in place from previous version)
    const dataVencCadastro = document.getElementById('data_venc');
    const hoje = new Date().toISOString().split('T')[0];
    dataVencCadastro.setAttribute('min', hoje);

    dataVencCadastro.addEventListener('input', () => {
        const erroDataObrigatoria = document.getElementById('erroDataObrigatoria');
        const erroDataInvalida = document.getElementById('erroDataInvalida');
        erroDataObrigatoria.style.display = 'none';
        erroDataInvalida.style.display = 'none';
        dataVencCadastro.classList.remove('is-invalid');
        dataVencCadastro.classList.remove('is-valid');
        if (!dataVencCadastro.value) {
            erroDataObrigatoria.style.display = 'block';
            dataVencCadastro.classList.add('is-invalid');
        } else if (dataVencCadastro.value < hoje) {
            erroDataInvalida.style.display = 'block';
            dataVencCadastro.classList.add('is-invalid');
        } else {
            dataVencCadastro.classList.add('is-valid');
        }
    });
}

function configurarValidacoesFormularios() {
    // Impede datas menores que hoje para o formulário de cadastro
    const dataVencCadastro = document.getElementById('data_venc');
    const hoje = new Date().toISOString().split('T')[0];
    dataVencCadastro.setAttribute('min', hoje);

    // Adiciona listener para a data de vencimento do formulário de cadastro
    dataVencCadastro.addEventListener('input', () => {
        const erroDataObrigatoria = document.getElementById('erroDataObrigatoria');
        const erroDataInvalida = document.getElementById('erroDataInvalida');
        
        erroDataObrigatoria.style.display = 'none';
        erroDataInvalida.style.display = 'none';
        dataVencCadastro.classList.remove('is-invalid');
        dataVencCadastro.classList.remove('is-valid'); // Limpa o valid para revalidar

        if (!dataVencCadastro.value) {
            erroDataObrigatoria.style.display = 'block';
            dataVencCadastro.classList.add('is-invalid');
        } else if (dataVencCadastro.value < hoje) {
            erroDataInvalida.style.display = 'block';
            dataVencCadastro.classList.add('is-invalid');
        } else {
            dataVencCadastro.classList.add('is-valid');
        }
    });

    // Impede datas menores que hoje para o formulário de edição
    const dataVencEdicao = document.getElementById('data_venc_editar');
    console.log("Elemento dataVencEdicao:", dataVencEdicao)
    dataVencEdicao.setAttribute('min', hoje);

    // Adiciona listener para a data de vencimento do formulário de edição
    dataVencEdicao.addEventListener('input', () => {
        const erroDataObrigatoria = document.getElementById('erroDataVencEditarObrigatoria');
        const erroDataInvalida = document.getElementById('erroDataVencEditarInvalida');
        
        erroDataObrigatoria.style.display = 'none';
        erroDataInvalida.style.display = 'none';
        dataVencEdicao.classList.remove('is-invalid');
        dataVencEdicao.classList.remove('is-valid'); // Limpa o valid para revalidar

        if (!dataVencEdicao.value) {
            erroDataObrigatoria.style.display = 'block';
            dataVencEdicao.classList.add('is-invalid');
        } else if (dataVencEdicao.value < hoje) {
            erroDataInvalida.style.display = 'block';
            dataVencEdicao.classList.add('is-invalid');
        } else {
            dataVencEdicao.classList.add('is-valid');
        }
    });

    // Adiciona validação genérica de Bootstrap para ambos os formulários
    document.querySelectorAll('.needs-validation').forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);

        // Limpa validação ao digitar para todos os campos do formulário
        form.querySelectorAll('input, select, textarea').forEach(campo => {
            campo.addEventListener('input', () => {
                if (campo.checkValidity()) {
                    campo.classList.remove('is-invalid');
                    campo.classList.add('is-valid');
                    if (campo.nextElementSibling && campo.nextElementSibling.classList.contains('invalid-feedback')) {
                        campo.nextElementSibling.style.display = 'none';
                    }
                } else {
                    campo.classList.remove('is-valid');
                }
            });
        });
    });
}


async function carregarDespesas() {
    try {
        mostrarLoading(true);
        const token = authManager.getToken();
        const response = await fetch(`${API_BASE_URL}/despesa`, {
            headers: {
                'Authorization': 'Bearer ' + token
            }
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            const errorMessage = errorData.message || `Erro HTTP ${response.status}`;
            throw new Error(errorMessage);
        }

        const dadosDespesas = await response.json();
        return dadosDespesas;

    } catch (error) {
        console.error('Erro ao carregar despesas:', error);
        criarToast(`Não foi possível carregar as despesas: ${error.message}.`, 'error');
        return [];
    } finally {
        mostrarLoading(false);
    }
}

async function carregarEExibirDespesas() {
    try {
        despesas = await carregarDespesas();
        
        if (tabelaDespesas) {
            tabelaDespesas.destroy();
        }
        
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
                render: function(data, type, row) {
                    if (type === 'display') { // Aplica a mudança apenas para exibição
                        let displayText = data || 'Indefinido';
                        if (displayText === 'Pendente') { // Se o valor for 'Pendente'
                            displayText = 'Pago'; // Mude para 'Pago' para exibição
                        }
                        return `<span class="${getStatusClass(data)}">${displayText}</span>`;
                    }
                    return data || 'Indefinido'; // Para outros tipos (ordenar, filtrar), mantenha o valor original
                },
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
                    <button class="btn btn-sm btn-warning" onclick="mostrarFormularioEdicao(${row.id})" title="Editar">
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
            order: [[3, 'asc']],
            responsive: true,
            scrollX: true,
            autoWidth: false,
            pageLength: 5,
            lengthMenu: [[5,10, 25, 50, 100, -1], [5,10, 25, 50, 100, "Todos"]]
            
        });
        $(window).on('resize', function() {
            if (tabelaDespesas) {
                tabelaDespesas.columns.adjust().responsive.recalc();
            }
        });
    } catch (error) {
        console.error('Erro ao exibir despesas:', error);
        criarToast('Erro ao exibir despesas na tabela.', 'error');
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
        const selectCategoriaEditar = document.getElementById('tipoDespesa_id_editar');
        
        if (response.ok) {
            const categorias = await response.json();
            const defaultOption = '<option value="">Selecione uma categoria</option>';
            selectCategoria.innerHTML = defaultOption;
            selectCategoriaEditar.innerHTML = defaultOption;
            
            categorias.forEach(categoria => {
                const option = document.createElement('option');
                option.value = categoria.id;
                option.textContent = categoria.nome;
                selectCategoria.appendChild(option);
                selectCategoriaEditar.appendChild(option.cloneNode(true));
            });
        } else {
            throw new Error('Erro ao carregar categorias');
        }
    } catch (error) {
        console.error('Erro ao carregar categorias:', error);
        document.getElementById('tipoDespesa_id').innerHTML = '<option value="">Erro ao carregar categorias</option>';
        document.getElementById('tipoDespesa_id_editar').innerHTML = '<option value="">Erro ao carregar categorias</option>';
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
        const selectEventoEditar = document.getElementById('evento_id_editar');
        
        if (response.ok) {
            const eventos = await response.json();
            const defaultOption = '<option value="">Nenhum evento selecionado</option>';
            selectEvento.innerHTML = defaultOption;
            selectEventoEditar.innerHTML = defaultOption;
            
            eventos.forEach(evento => {
                const option = document.createElement('option');
                option.value = evento.id;
                option.textContent = evento.nome;
                selectEvento.appendChild(option);
                selectEventoEditar.appendChild(option.cloneNode(true));
            });
        } else {
            throw new Error('Erro ao carregar eventos');
        }
    } catch (error) {
        console.error('Erro ao carregar eventos:', error);
        document.getElementById('evento_id').innerHTML = '<option value="">Erro ao carregar eventos</option>';
        document.getElementById('evento_id_editar').innerHTML = '<option value="">Erro ao carregar eventos</option>';
    }
}


function toggleFormularioCadastro() {
    const formulario = document.getElementById('formularioDespesa');
    const btn = document.getElementById('btnMostrarFormulario');
    const formularioEdicao = document.getElementById('formularioEdicaoDespesa');

    if (formularioEdicao.style.display === 'block') {
        toggleFormularioEdicao();
    }
    
    if (formulario.style.display === 'none' || formulario.style.display === '') {
        formulario.style.display = 'block';
        btn.textContent = 'Cancelar';
        btn.className = 'btn btn-secondary';
        limparFormularioCadastro();
        formulario.scrollIntoView({ behavior: 'smooth' });
    } else {
        formulario.style.display = 'none';
        btn.textContent = 'Nova Despesa';
        btn.className = 'btn btn-primary';
        limparFormularioCadastro();
    }
}

function limparFormularioCadastro() {
    document.getElementById('formDespesa').reset();
    limparErrosValidacao('formDespesa');
}

async function handleSubmitFormCadastro(event) {
    event.preventDefault();

    if (!validarFormulario('formDespesa')) {
        criarToast('Por favor, corrija os erros no formulário de cadastro.', 'error');
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

        // Adiciona todos os campos do formulário aos dados como RequestParams
        for (const [key, value] of formData.entries()) {
             // Exclua campos que não são RequestParams ou que são tratados de forma diferente
             if (key !== 'id_editar') { // 'id_editar' é do form de edição, não cadastro
                dados.append(key, value);
            }
        }
        dados.append('pagamento', 0); // Sempre 0 no cadastro inicial
        dados.append('data_lanc', dataLanc);
        dados.append('usuario_id', authManager.getId());
        dados.append('status_conci', "Aguardando");

        const token = authManager.getToken();
        // Construa a URL com a query string
        const urlComParams = `${API_BASE_URL}/despesa?${dados.toString()}`;

        const response = await fetch(urlComParams, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                // NÃO envie 'Content-Type': 'application/x-www-form-urlencoded'
                // quando os parâmetros estão na URL. Pode causar problemas no backend.
            },
            // Não envie 'body' pois os dados já estão na URL
        });

        if (response.ok) {
            criarToast('Despesa cadastrada com sucesso!', 'success');
            toggleFormularioCadastro();
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


function toggleFormularioEdicao() {
    const formularioEdicao = document.getElementById('formularioEdicaoDespesa');
    
    if (formularioEdicao.style.display === 'none' || formularioEdicao.style.display === '') {
        formularioEdicao.style.display = 'block';
        formularioEdicao.scrollIntoView({ behavior: 'smooth' });
    } else {
        formularioEdicao.style.display = 'none';
        limparFormularioEdicao();
    }
}

function limparFormularioEdicao() {
    document.getElementById('formEditarDespesa').reset();
    limparErrosValidacao('formEditarDespesa');
}

function mostrarFormularioEdicao(id) {
    const formularioCadastro = document.getElementById('formularioDespesa');
    if (formularioCadastro.style.display === 'block') {
        toggleFormularioCadastro();
    }

    const despesa = despesas.find(d => d.id === id);
    if (!despesa) {
        criarToast('Despesa não encontrada para edição.', 'error');
        return;
    }

    $('#id_editar').val(despesa.id);
    $('#descricao_editar').val(despesa.descricao);
    $('#valor_editar').val(despesa.val);
    $('#pagamento_editar').val(despesa.pagamento ?? '');
    $('#data_venc_editar').val(despesa.data_venc);
    $('#status_conci_editar').val(despesa.status_conci);
    $('#tipoDespesa_id_editar').val(despesa.categoria?.id || '');
    $('#evento_id_editar').val(despesa.evento?.id || '');

    limparErrosValidacao('formEditarDespesa');

    toggleFormularioEdicao();
}

async function handleSubmitFormEdicao(event) {
    event.preventDefault();

    console.log("1. handleSubmitFormEdicao chamado."); // Log 1

    if (!validarFormulario('formEditarDespesa')) {
        console.log("2. Validação do formulário de edição falhou."); // Log 2
        criarToast('Por favor, corrija os erros no formulário de edição.', 'error');
        return;
    }

    console.log("3. Validação do formulário de edição bem-sucedida. Preparando requisição."); // Log 3

    const btnSalvar = document.getElementById('btnSalvarEdicao');
    const originalText = btnSalvar.innerHTML;

    try {
        btnSalvar.disabled = true;
        btnSalvar.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Salvando...';

        const id = parseInt($('#id_editar').val());
        if (isNaN(id)) {
            throw new Error("ID da despesa inválido para edição (NaN).");
        }
        console.log("4. ID da despesa:", id);

        const dados = new URLSearchParams();
        
        // Sempre inclua o ID no RequestParam, além do PathVariable se o backend espera
        dados.append('id', id); 
        dados.append('descricao', $('#descricao_editar').val());
        dados.append('valor', $('#valor_editar').val());
        
        // Removidos os campos 'pagamento_editar' e 'status_conci_editar' do HTML.
        // Envie valores fixos para o backend se eles são obrigatórios.
        dados.append('pagamento', 0); // Valor fixo para pagamento
        dados.append('status_conci', 'Aguardando'); // Valor fixo para status

        dados.append('data_venc', $('#data_venc_editar').val());
        dados.append('tipoDespesa_id', $('#tipoDespesa_id_editar').val());
        
        let eventoId = $('#evento_id_editar').val();
        if (eventoId === null || eventoId === '') { // Verifica se não é null nem string vazia
            eventoId = '0'
        }
        dados.append('evento_id', eventoId);
        const despesaOriginal = despesas.find(d => d.id === id);
        if (despesaOriginal) {
            dados.append('data_lanc', despesaOriginal.data_lanc);
            dados.append('usuario_id', authManager.getId());
        } else {
            console.warn("Despesa original não encontrada para copiar data_lanc/usuario_id. Isso pode causar erro no backend se esses campos forem obrigatórios.");
            // Você pode precisar adicionar uma lógica para obter esses valores de alguma forma
            // se eles são obrigatórios no PUT e a despesa original não for encontrada.
        }
        
        console.log("5. Parâmetros a serem enviados:", dados.toString());

        const token = authManager.getToken();
        if (!token) {
            throw new Error("Token de autenticação não encontrado.");
        }
        console.log("6. Token encontrado. Iniciando fetch PUT.");

        const urlComParams = `${API_BASE_URL}/despesa/${id}?${dados.toString()}`;

        const response = await fetch(urlComParams, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        });

        console.log("7. Resposta do fetch recebida. Status:", response.status);

        if (response.ok) {
            criarToast('Despesa atualizada com sucesso!', 'success');
            toggleFormularioEdicao();
            await carregarEExibirDespesas();
            console.log("8. Despesa atualizada e tabela recarregada.");
        } else {
            const errorData = await response.json().catch(() => ({ message: 'Erro desconhecido da API.' }));
            throw new Error(errorData.message || `Erro HTTP ${response.status}`);
        }

    } catch (error) {
        console.error('handleSubmitFormEdicao: ERRO CAPTURADO:', error);
        criarToast(`Erro ao atualizar despesa: ${error.message}`, 'error');
    } finally {
        btnSalvar.disabled = false;
        btnSalvar.innerHTML = originalText;
        console.log("10. Finalizado handleSubmitFormEdicao.");
    }
}


function validarFormulario(formId) {
    const form = document.getElementById(formId);
    let isValid = true;

    form.classList.add('was-validated');
    form.querySelectorAll('.invalid-feedback').forEach(el => el.style.display = 'none');

    // ... (restante da função validarFormulario permanece a mesma)
    // Apenas certificando que o comportamento dos feedbacks de erro esteja ok.

    if (formId === 'formDespesa') {
        const categoria = document.getElementById('tipoDespesa_id');
        if (!categoria.value) {
            mostrarErroValidacao('tipoDespesa_id', 'Por favor, selecione uma categoria.');
            isValid = false;
        }

        const valor = document.getElementById('valor');
        if (!valor.value || parseFloat(valor.value) <= 0) {
            mostrarErroValidacao('valor', 'Por favor, informe um valor válido maior que zero.');
            isValid = false;
        }

        const dataVenc = document.getElementById('data_venc');
        const hoje = new Date().toISOString().split('T')[0];
        if (!dataVenc.value) {
            document.getElementById('erroDataObrigatoria').style.display = 'block';
            dataVenc.classList.add('is-invalid');
            isValid = false;
        } else if (dataVenc.value < hoje) {
            document.getElementById('erroDataInvalida').style.display = 'block';
            dataVenc.classList.add('is-invalid');
            isValid = false;
        }
    } 
    else if (formId === 'formEditarDespesa') {
        const descricaoEditar = document.getElementById('descricao_editar');
        if (!descricaoEditar.value.trim()) {
            mostrarErroValidacao('descricao_editar', 'A descrição é obrigatória.');
            isValid = false;
        }

        const valorEditar = document.getElementById('valor_editar');
        if (!valorEditar.value || parseFloat(valorEditar.value) <= 0) {
            mostrarErroValidacao('valor_editar', 'Informe um valor válido maior que zero.');
            isValid = false;
        }

        const dataVencEditar = document.getElementById('data_venc_editar');
        const hoje = new Date().toISOString().split('T')[0];
        if (!dataVencEditar.value) {
            document.getElementById('erroDataVencEditarObrigatoria').style.display = 'block';
            dataVencEditar.classList.add('is-invalid');
            isValid = false;
        } else if (dataVencEditar.value < hoje) {
            document.getElementById('erroDataVencEditarInvalida').style.display = 'block';
            dataVencEditar.classList.add('is-invalid');
            isValid = false;
        }


        const tipoDespesaEditar = document.getElementById('tipoDespesa_id_editar');
        if (!tipoDespesaEditar.value) {
            mostrarErroValidacao('tipoDespesa_id_editar', 'Selecione uma categoria.');
            isValid = false;
        }
    }

    if (!form.checkValidity()) {
        isValid = false;
    }

    return isValid;
}


function limparErrosValidacao(formId) {
    const form = document.getElementById(formId);
    form.classList.remove('was-validated');
    form.querySelectorAll('.is-invalid').forEach(el => el.classList.remove('is-invalid'));
    form.querySelectorAll('.is-valid').forEach(el => el.classList.remove('is-valid'));
    form.querySelectorAll('.invalid-feedback').forEach(el => el.style.display = 'none');
}

function mostrarErroValidacao(fieldId, message) {
    const field = document.getElementById(fieldId);
    let errorElement;
    
    if (fieldId === 'data_venc' || fieldId === 'data_venc_editar') {
        const idObrigatoria = fieldId === 'data_venc' ? 'erroDataObrigatoria' : 'erroDataVencEditarObrigatoria';
        const idInvalida = fieldId === 'data_venc' ? 'erroDataInvalida' : 'erroDataVencEditarInvalida';

        document.getElementById(idObrigatoria).style.display = 'none';
        document.getElementById(idInvalida).style.display = 'none';

        if (message.includes('obrigatória') || message.includes('informe a data')) {
            errorElement = document.getElementById(idObrigatoria);
        } else if (message.includes('anterior a hoje') || message.includes('inválida')) {
            errorElement = document.getElementById(idInvalida);
        }
    } else {
        errorElement = field.nextElementSibling;
        while (errorElement && !errorElement.classList.contains('invalid-feedback')) {
            errorElement = errorElement.nextElementSibling;
        }
    }

    field.classList.add('is-invalid');
    if (errorElement) {
        errorElement.textContent = message;
        errorElement.style.display = 'block';
    }
}

// ... (restante das funções de exclusão, filtros e utilitárias permanecem as mesmas)

function confirmarExclusao(id) {
    // 1. Encontrar a despesa na lista global 'despesas'
    const despesaParaExcluir = despesas.find(d => d.id === id);

    if (!despesaParaExcluir) {
        criarToast('Despesa não encontrada para exclusão.', 'error');
        return;
    }

    // 2. Verificar o status da despesa
    if (despesaParaExcluir.status_conci === 'Pendente') {
        criarToast('Não é possível excluir despesas PAGAS.', 'warning');
        return; // Impede a continuação da exclusão
    }

    // Se o status não for "Pendente", procede com a exclusão
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
            criarToast('Despesa excluída com sucesso!', 'success');
            await carregarEExibirDespesas();
        } else {
            const errorData = await response.json().catch(() => ({}));
            throw new Error(errorData.message || `Erro HTTP ${response.status}`);
        }
    } catch (error) {
        console.error('Erro ao excluir despesa:', error);
        criarToast(`Erro ao excluir despesa: ${error.message}`, 'error');
    } finally {
        modalConfirmacao.hide();
        despesaSelecionadaId = null;
    }
}

function aplicarFiltros() {
    if (!tabelaDespesas) return;

    const filtroStatus = document.getElementById('filtroStatus').value;
    const filtroDataInicio = document.getElementById('filtroDataInicio').value;
    const filtroDataFim = document.getElementById('filtroDataFim').value;

    $.fn.dataTable.ext.search = $.fn.dataTable.ext.search.filter(fn => fn.name !== 'filtroPorData');

    tabelaDespesas.column(4).search(filtroStatus);

    if (filtroDataInicio || filtroDataFim) {
        const dataInicio = filtroDataInicio ? new Date(filtroDataInicio + 'T00:00:00') : null;
        const dataFim = filtroDataFim ? new Date(filtroDataFim + 'T23:59:59') : null;

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

        const filtroPorData = function(settings, data, dataIndex) {
            const dataDespesaStr = data[3];
            if (!dataDespesaStr) return true;

            const partes = dataDespesaStr.split('/');
            if (partes.length !== 3) return false;
            const dataDespesa = new Date(`${partes[2]}-${partes[1]}-${partes[0]}T00:00:00`); 

            if (isNaN(dataDespesa.getTime())) return false;

            const depoisDeInicio = !dataInicio || dataDespesa >= dataInicio;
            const antesDeFim = !dataFim || dataDespesa <= dataFim;

            return depoisDeInicio && antesDeFim;
        };
        Object.defineProperty(filtroPorData, 'name', { value: 'filtroPorData' });

        $.fn.dataTable.ext.search.push(filtroPorData);
    }

    tabelaDespesas.draw();
}

function limparFiltros() {
    console.log("Função limparFiltros chamada.");
    document.getElementById('filtroStatus').value = '';
    document.getElementById('filtroDataInicio').value = '';
    document.getElementById('filtroDataFim').value = '';

    $.fn.dataTable.ext.search = $.fn.dataTable.ext.search.filter(fn => fn.name !== 'filtroPorData');

    if (tabelaDespesas) {
        tabelaDespesas.search('').columns().search('').draw();
    }
}

function formatarMoeda(valor) {
    return new Intl.NumberFormat('pt-BR', {
        style: 'currency',
        currency: 'BRL'
    }).format(valor || 0);
}

function formatarData(dataString) {
    if (!dataString) return '';
    try {
        const date = new Date(dataString + 'T00:00:00'); 
        return date.toLocaleDateString('pt-BR');
    } catch {
        return dataString;
    }
}

function getStatusClass(status) {
    const statusLower = (status || '').toLowerCase();
    if (statusLower === 'aguardando') {
        return 'status-aguardando';
    } else if (statusLower === 'conciliado') {
        return 'status-conciliado';
    } else if (statusLower === 'pendente') {
        return 'status-pendente';
    }
    return '';
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

function criarToast(mensagem, tipo = 'info') {
    let toastContainer = document.getElementById('toast-container');
    if (!toastContainer) {
        toastContainer = document.createElement('div');
        toastContainer.id = 'toast-container';
        toastContainer.className = 'toast-container position-fixed top-0 end-0 p-3';
        toastContainer.style.zIndex = '9999';
        document.body.appendChild(toastContainer);
    }

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
    
    const toastElement = document.getElementById(toastId);
    const bsToast = new bootstrap.Toast(toastElement, {
        delay: tipo === 'error' ? 8000 : 4000
    });
    bsToast.show();
    
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