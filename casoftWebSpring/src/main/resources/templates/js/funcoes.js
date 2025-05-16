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


function cadastrarTipo()
{
  var tipoDespesa = document.getElementById("despesaPost").value;

  var tipo = {
    descricao : tipoDespesa
  };

  fetch("http://localhost:8080/apis/despesa", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(tipo)
  })
      .then(response => {
        if(response.ok)
        {
          return response.json();
        }
        return response.json().then(error => {throw new Error(error.mensagem);});
      })
      .then(data => {
        document.getElementById("mensagem").innerText = data.mensagem || "Despesa cadastrada com sucesso!";
        console.log(data);
      })
      .catch(error => {
        document.getElementById("mensagem").innerText = "Erro: " + error.message;
        console.error(error);
      });
}

function buscarTodosTipo(){
  fetch("http://localhost:8080/apis/despesa")
      .then(response => {
        console.log("Resposta recebida", response);
        if(response.ok)
        {
          return response.json();
        }
        return response.json().then(error => { throw new Error(error.mensagem); });
      })
      .then(data => {
        console.log("Dados recebidos:", data);
        var listTipo = document.getElementById("despesaGet");
        listTipo.innerHTML = '';

        if(data.length === 0)
        {
          listTipo.innerHTML = "<p>Nenhuma despesa encontrado.</p>";
        }
        else
        {
          var ul = document.createElement('ul');
          data.forEach(TipoDespesa => {
            var li = document.createElement('li');
            li.textContent = `ID: ${TipoDespesa.id}, Nome: ${TipoDespesa.descricao}`;
            ul.appendChild(li);
          });
          listTipo.appendChild(ul);
        }
      })
      .catch(error => {
        console.error(error);
        alert("Erro ao carregar tipos!");
      });
}

function buscarTipoId(){
  var id = document.getElementById("despesaGetID").value;

  fetch(`http://localhost:8080/apis/despesa/${id}`)
      .then(response => {
        if(response.ok)
        {
          return response.json();
        }
        return response.json().then(error => { throw new Error(error.mensagem); });
      })
      .then(TipoDespesa => {
        var resul = document.getElementById("resultado");
        resul.innerHTML = `<p>ID: ${TipoDespesa.id}, Nome: ${TipoDespesa.descricao}</p>`;
      })
      .catch(error => {
        console.error("Erro:", error);
        alert("Erro ao buscar tipo: " + error.message);
      })
}

function deletarTipo(){
  var id = document.getElementById("despesaDelete").value;

  fetch(`http://localhost:8080/apis/despesa/${id}`,{
    method: "DELETE"
  })
      .then(response => {
        if (response.ok) {
          alert("Tipo de despesa deletada com sucesso!");
        } else {
          return response.json().then(error => { throw new Error(error.mensagem); });
        }
      })
      .catch(error => {
        console.error("Erro:", error);
        alert("Erro ao deletar tipo de despesa: " + error.message);
      });
}

function alterarTipo(){
  var ident = document.getElementById("tipoDespesaUpdateID").value;
  var nome = document.getElementById("tipoDespesaUpdateNome").value;

  var tipoAtualizado = {
    id : ident,
    descricao: nome
  };

  fetch("http://localhost:8080/apis/despesa", {
    method: "PUT",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(tipoAtualizado)
  })
      .then(response => {
        if (response.ok)
        {
          return response.json();
        } else
        {
          return response.json().then(error => {
            throw new Error(error.mensagem || "Erro desconhecido");
          });
        }
      })
      .then(tipo => {
        alert("Tipo de despesa atualizada com sucesso!");
        console.log(tipo);
      })
      .catch(error => {
        console.error("Erro:", error);
        alert("Erro ao atualizar tipo de despesa: " + error.message);
      })
}