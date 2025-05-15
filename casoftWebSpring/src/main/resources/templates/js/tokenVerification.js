// tokenVerification.js - Script para verificar token em páginas protegidas

document.addEventListener("DOMContentLoaded", function() {
  // Verifica se o usuário está autenticado ao carregar uma página protegida
  function verificarAutenticacao() {
    const token = authManager.getToken();
    
    // Se não houver token, redireciona para a página de login
    if (!token) {
      window.location.href = "ControleAcesso.html";
      return;
    }
    
    // Verifica se o token ainda é válido
    authManager.verificarToken()
      .then(isValid => {
        if (!isValid) {
          alert("Sua sessão expirou. Por favor, faça login novamente.");
          authManager.logout();
        }
      })
      .catch(error => {
        console.error("Erro ao verificar token:", error);
        alert("Erro ao verificar autenticação. Por favor, faça login novamente.");
        authManager.logout();
      });
  }
  
  // Verifica permissões de acesso à página baseado no nível do usuário
  function verificarPermissoes() {
    const nivel = authManager.getNivel();
    const path = window.location.pathname;
    
    // Páginas administrativas
    const adminPages = [
      "/cadastroEmpresa.html",
      "/gerenciarUsuarios.html",
      "/relatorios.html"
      // Adicione outras páginas administrativas conforme necessário
    ];
    
    // Verifica se é uma página administrativa e o usuário não é admin
    if(nivel==null){
      window.location.href = "ControleAcesso.html";
    }
    else if (adminPages.some(page => path.endsWith(page)) && nivel !== "ADMIN") {
      alert("Você não tem permissão para acessar esta página.",nivel);
      window.location.href = "home.html";
    }
  }
  
  // Executa verificações ao carregar a página
  verificarAutenticacao();
  verificarPermissoes();
  
  // Configura verificação periódica do token (a cada 5 minutos)
  setInterval(() => {
    authManager.verificarToken()
      .then(isValid => {
        if (!isValid) {
          alert("Sua sessão expirou. Por favor, faça login novamente.");
          authManager.logout();
        }
      })
      .catch(error => {
        console.error("Erro na verificação periódica do token:", error);
      });
  }, 20 * 60 * 1000); // 20 minutos
});
