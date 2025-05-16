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