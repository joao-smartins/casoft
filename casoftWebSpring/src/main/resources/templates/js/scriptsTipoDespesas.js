const apiBase = "http://localhost:8080/api/usuarios";
let usuarioLogado = null;

// Elementos da interface
const loginScreen = document.getElementById("loginScreen");
const cadastroScreen = document.getElementById("cadastroScreen");
const adminScreen = document.getElementById("adminScreen");
const btnCadastro = document.getElementById("btnCadastro");
const btnVoltarLogin = document.getElementById("btnVoltarLogin");
const btnLogout = document.getElementById("btnLogout");
const mensagemDiv = document.getElementById("mensagem");

// Navegação entre telas
btnCadastro.addEventListener("click", () => {
    loginScreen.style.display = "none";
    cadastroScreen.style.display = "block";
});

btnVoltarLogin.addEventListener("click", () => {
    cadastroScreen.style.display = "none";
    loginScreen.style.display = "block";
});

btnLogout.addEventListener("click", () => {
    usuarioLogado = null;
    adminScreen.style.display = "none";
    loginScreen.style.display = "block";
    mostrarMensagem("Você saiu do sistema", false);
});

// Função para mostrar mensagens
function mostrarMensagem(texto, erro = false) {
    mensagemDiv.textContent = texto;
    mensagemDiv.style.color = erro ? "white" : "white";
    mensagemDiv.style.backgroundColor = erro ? "#f72585" : "#4361ee";
    mensagemDiv.style.display = "block";

    setTimeout(() => {
        mensagemDiv.style.display = "none";
    }, 5000);
}

// Função genérica para requisições
async function fazerRequisicao(url, metodo, dados) {
    const config = {
        method: metodo,
        headers: {
            "Content-Type": "application/json"
        },
        credentials: 'include'
    };

    if (dados) {
        config.body = JSON.stringify(dados);
    }

    const resposta = await fetch(url, config);

    if (!resposta.ok) {
        const erro = await resposta.json();
        throw new Error(erro.mensagem || erro.erro || `Erro ${resposta.status}`);
    }

    return resposta.json();
}

// Login
document.addEventListener("DOMContentLoaded", function() {
    // Verificando se já existe um token salvo
    const token = localStorage.getItem("authToken");
    if (token) {
        // Se o token existir, verifica se ainda é válido
        authManager.verificarToken()
            .then(isValid => {
                if (isValid) {
                    // Se for válido, redireciona para a página apropriada
                    const nivel = authManager.getNivel();
                    if (nivel === "ADMIN") {

                        window.location.href = "cadastroEmpresa.html";
                    } else {
                        window.location.href = "home.html";
                    }
                }
            })
            .catch(error => {
                console.error("Erro ao verificar token:", error);
                // Em caso de erro, limpa o token e mantém na página de login
                authManager.logout();
            });
    }

    // Configurando o listener do formulário de login
    const loginForm = document.getElementById("loginForm");
    if (loginForm) {
        loginForm.addEventListener("submit", async (e) => {
            e.preventDefault();
            const login = document.getElementById("login").value;
            const senha = document.getElementById("senha").value;

            // Utiliza o gerenciador de autenticação para fazer login
            authManager.login(login, senha);
        });
    }
});



// Cadastro
document.getElementById("cadastroForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const usuario = {
        nome: document.getElementById("nome").value,
        login: document.getElementById("novoLogin").value,
        senha: document.getElementById("novaSenha").value,
        cpf: document.getElementById("cpf").value.replace(/\D/g, ""),
        telefone: document.getElementById("telefone").value.replace(/\D/g, ""),
        ativo: true,
        nivelAcesso: "PADRAO"
    };

    try {
        const data = await fazerRequisicao(`${apiBase}/cadastro`, "POST", usuario);

        mostrarMensagem("Usuário cadastrado com sucesso! Faça login para continuar.");
        cadastroScreen.style.display = "none";
        loginScreen.style.display = "block";
        document.getElementById("cadastroForm").reset();

    } catch (err) {
        mostrarMensagem(err.message || "Erro ao cadastrar usuário", true);
    }
});

// Desativar Usuário
document.getElementById("desativarForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const id = document.getElementById("idUsuario").value;

    try {
        const data = await fazerRequisicao(`${apiBase}/${id}/desativar`, "PUT");

        mostrarMensagem("Usuário desativado com sucesso!");
        document.getElementById("desativarForm").reset();
    } catch (err) {
        mostrarMensagem(err.message || "Erro ao desativar usuário", true);
    }
});



/* tipoDespesa */
function cadastrarTipo() {
    var tipoDespesa = document.getElementById("despesaPost");
    var mensagemElement = document.getElementById("mensagem");

    // Remove mensagem e estilo de erro anteriores
    mensagemElement.innerText = "";
    tipoDespesa.style.border = "";

    // Remove label de erro anterior se existir
    const oldErrorLabel = tipoDespesa.parentElement.querySelector('.error-label');
    if (oldErrorLabel) {
        oldErrorLabel.remove();
    }

    if (tipoDespesa.value.trim() === "") {
        tipoDespesa.style.border = "2px solid red";
        const errorLabel = document.createElement('div');
        errorLabel.className = 'error-label';
        errorLabel.style.color = "red";
        errorLabel.style.fontSize = "12px";
        errorLabel.style.marginTop = "5px";
        errorLabel.innerText = "A descrição da despesa não pode estar vazia.";
        tipoDespesa.parentElement.appendChild(errorLabel);
        return;
    }

    // Validação para não permitir apenas números
    if (/^\d+$/.test(tipoDespesa.value)) {
        tipoDespesa.style.border = "2px solid red";
        const errorLabel = document.createElement('div');
        errorLabel.className = 'error-label';
        errorLabel.style.color = "red";
        errorLabel.style.fontSize = "12px";
        errorLabel.style.marginTop = "5px";
        errorLabel.innerText = "A descrição não pode conter apenas números.";
        tipoDespesa.parentElement.appendChild(errorLabel);
        return;
    }

    var tipo = {
        nome: tipoDespesa.value
    };

    fetch("http://localhost:8080/apis/tipoDespesas", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(tipo)
    })
        .then(function (response) {
            if (response.ok) {
                return response.json();
            } else {
                return response.json().then(function (e) {
                    throw new Error(e.mensagem);
                });
            }
        })
        .then(function () {
            tipoDespesa.value = "";
            mensagemElement.innerText = "";
            buscarTodosTipo();
        })
        .catch(function (error) {
            const errorLabel = document.createElement('div');
            errorLabel.className = 'error-label';
            errorLabel.style.color = "red";
            errorLabel.style.fontSize = "12px";
            errorLabel.style.marginTop = "5px";
            errorLabel.innerText = "Erro: " + error.message;
            tipoDespesa.parentElement.appendChild(errorLabel);
        });
}

function buscarTodosTipo() {
    fetch("http://localhost:8080/apis/tipoDespesas")
        .then(function(response) {
            if (response.ok) {
                return response.json();
            } else {
                return response.json().then(function(error) {
                    throw new Error(error.mensagem || "Erro na resposta da API");
                });
            }
        })
        .then(function(data) {
            var tabela = document.getElementById("tabelaTipos");
            tabela.innerHTML = "";

            if (data.length === 0) {
                var linha = document.createElement("tr");
                var celula = document.createElement("td");
                celula.colSpan = 4;
                celula.textContent = "Nenhum tipo de despesa encontrado.";
                linha.appendChild(celula);
                tabela.appendChild(linha);
            } else {
                const filtroInput = document.getElementById("filtro");
                const filtro = filtroInput ? filtroInput.value.toLowerCase() : ""; // Verifica se o elemento existe
                data.forEach(function(tipo) {
                    if (filtro === "" || tipo.nome.toLowerCase().includes(filtro) || tipo.id.toString().includes(filtro)) { // Filtra por nome ou ID
                        var linha = document.createElement("tr");

                        var tdId = document.createElement("td");
                        tdId.textContent = tipo.id;
                        linha.appendChild(tdId);

                        var tdNome = document.createElement("td");
                        tdNome.textContent = tipo.nome;
                        linha.appendChild(tdNome);

                        var tdEditar = document.createElement("td");
                        var botaoEditar = document.createElement("button");
                        botaoEditar.textContent = "Editar";
                        botaoEditar.onclick = function() {
                            preencherCamposAtualizacao(tipo.id, tipo.nome);
                            document.getElementById("formAtualizar").style.display = "block";
                        };
                        tdEditar.appendChild(botaoEditar);
                        linha.appendChild(tdEditar);

                        var tdExcluir = document.createElement("td");
                        var botaoExcluir = document.createElement("button");
                        botaoExcluir.textContent = "Excluir";
                        botaoExcluir.onclick = function() {
                            confirmarExclusao(tipo.id); // Chama a função de confirmação
                        };
                        tdExcluir.appendChild(botaoExcluir);
                        linha.appendChild(tdExcluir);

                        tabela.appendChild(linha);
                    }
                });
            }
        })
        .catch(function(error) {
            console.error("Erro ao carregar tipos:", error);
            alert("Erro ao carregar tipos: " + error.message);
        });
}

function confirmarExclusao(id) {
    if (confirm("Tem certeza que deseja excluir este tipo de despesa?")) {
        deletarTipo(id);
    }
}

function preencherCamposAtualizacao(id, nome) {
    document.getElementById("tipoDespesaUpdateID").value = id;
    document.getElementById("tipoDespesaUpdateNome").value = nome;
    document.getElementById("btnAtualizar").style.display = "inline-block";
}

function buscarTipoId(){
    var id = document.getElementById("despesaGetID").value;

    fetch(`http://localhost:8080/apis/tipoDespesas/${id}`)
        .then(response => {
            if(response.ok)
            {
                return response.json();
            }
            return response.json().then(error => { throw new Error(error.mensagem); });
        })
        .then(TipoDespesa => {
            var resul = document.getElementById("resultado");
            resul.innerHTML = `<p>ID: ${TipoDespesa.id}, Nome: ${TipoDespesa.nome}</p>`;
        })
        .catch(function(error) {
            console.error("Erro:", error);
            alert("Erro ao buscar tipo: " + error.message);
        });
}

function deletarTipo(id) {
    fetch("http://localhost:8080/apis/tipoDespesas/" + id, {
        method: "DELETE"
    })
        .then(function(response) {
            if (response.ok) {
                alert("Tipo de despesa deletado com sucesso!");
                buscarTodosTipo();
            } else {
                return response.json().then(function(error) {
                    throw new Error(error.mensagem);
                });
            }
        })
        .catch(function(error) {
            console.error("Erro ao deletar:", error);
            alert("Erro ao deletar tipo: " + error.message);
        });
}

function alterarTipo() {
    var ident = document.getElementById("tipoDespesaUpdateID").value;
    var nomeInput = document.getElementById("tipoDespesaUpdateNome");
    var nome = nomeInput.value.trim();

    // Remove error styling and label if they exist
    nomeInput.style.border = "";
    const oldErrorLabel = nomeInput.parentElement.querySelector('.error-label');
    if (oldErrorLabel) {
        oldErrorLabel.remove();
    }

    if (nome === "") {
        nomeInput.style.border = "2px solid red";
        const errorLabel = document.createElement('div');
        errorLabel.className = 'error-label';
        errorLabel.style.color = "red";
        errorLabel.style.fontSize = "12px";
        errorLabel.style.marginTop = "5px";
        errorLabel.innerText = "O nome do tipo de despesa não pode estar vazio.";
        nomeInput.parentElement.appendChild(errorLabel);
        return;
    }

    var tipoAtualizado = {
        id: ident,
        nome: nome
    };

    fetch("http://localhost:8080/apis/tipoDespesas", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(tipoAtualizado)
    })
        .then(function (response) {
            if (response.ok) {
                return response.json();
            }
            return response.json().then(function (error) {
                throw new Error(error.mensagem || "Erro desconhecido");
            });
        })
        .then(function (tipo) {
            alert("Tipo de despesa atualizada com sucesso!");
            document.getElementById("tipoDespesaUpdateID").value = "";
            nomeInput.value = "";
            document.getElementById("formAtualizar").style.display = "none";
            buscarTodosTipo();
        })
        .catch(function (error) {
            alert("Erro ao atualizar tipo de despesa: " + error.message);
        });
}

function prepararEdicao(id, nome) {
    document.getElementById("tipoDespesaUpdateID").value = id;
    document.getElementById("tipoDespesaUpdateNome").value = nome;
    document.getElementById("btnAtualizar").style.display = "inline-block";
}

window.onload = buscarTodosTipo;
