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
document.getElementById("loginForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const login = document.getElementById("login").value;
  const senha = document.getElementById("senha").value;

  fetch(`http://localhost:8080/autenticar/${login}/${senha}`, {
      method: "GET"
  })
  .then((response) => {
      if (!response.ok) {
          return response.json().then((errorData) => {
              throw new Error(errorData.mensagem); // Lança erro para o .catch
          });
      }
      return response.json(); // Extrai o objeto JSON contendo o token e o nível
  })
  .then((data) => {
      const token = data.token;
      const nivel = data.nivel;

      // Armazenando o token e o nível no localStorage
      localStorage.setItem("authToken", token);
      localStorage.setItem("userNivel", nivel);

      // Redirecionando com base no nível do usuário
      if (nivel === "ADMIN") {
          alert("Login bem-sucedido! Bem-vindo, Administrador!");
          window.location.href = "cadastroEmpresa.html";
      } else {
          alert("Login bem-sucedido! Bem-vindo!");
          window.location.href = "home.html";
      }
  })
  .catch((error) => {
      console.error("Erro durante o login:", error);
      alert("Erro ao tentar fazer login: " + error.message);
  });
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