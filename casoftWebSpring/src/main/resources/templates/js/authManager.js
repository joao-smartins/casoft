
// Classe para gerenciar autenticação
class AuthManager {
  constructor() {
    this.tokenKey = "authToken";
    this.nivelKey = "userNivel";
    this.apiBaseUrl = "http://localhost:8080";       
  }

  // Salva dados de autenticação
  setAuthData(token, nivel) {
    localStorage.setItem(this.tokenKey, token);
    localStorage.setItem(this.nivelKey, nivel);
  }

  // Obtém o token atual
  getToken() {
    return localStorage.getItem(this.tokenKey);
  }

  // Obtém o nível do usuário
  getNivel() {
    return localStorage.getItem(this.nivelKey);
  }

  // Verifica se o usuário está autenticado
  isAuthenticated() {
    return !!this.getToken();
  }

  // Limpa os dados de autenticação
  logout() {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.nivelKey);
    window.location.href = "ControleAcesso.html";
  }

  // Verifica se o token é válido no backend
  async verificarToken() {
    const token = this.getToken();
    
    if (!token) {
      return false;
    }

    try {
      const response = await fetch(`${this.apiBaseUrl}/verificar-token`, {
        method: "GET",
        headers: {
          "Authorization": `Bearer ${token}`
        }
      });

      if (!response.ok) {
        // Se o token for inválido, faça logout
        this.logout();
        return false;
      }
      
      return true;
    } catch (error) {
      console.error("Erro ao verificar token:", error);
      return false;
    }
  }

  // Fazer login
  async login(login, senha) {
    try {
      const response = await fetch(`${this.apiBaseUrl}/${login}/${senha}`, {
        method: "GET"
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.mensagem);
      }

      const data = await response.json();
      const { token, nivel } = data;
      
      // Salva os dados de autenticação
      this.setAuthData(token, nivel);
      const empresaCadastrada = await this.verificaEmpresaCadastrada();
      // Redireciona com base no nível
      if (nivel === "ADMIN") {
        alert("Login bem-sucedido! Bem-vindo, Administrador!");
        if(empresaCadastrada)
          window.location.href = "home.html";
        else
          window.location.href = "cadastroEmpresa.html";
      } else {
        alert("Login bem-sucedido! Bem-vindo!");
        window.location.href = "home.html";
      }
      
      return true;
    } catch (error) {
      console.error("Erro durante o login:", error);
      alert("Erro ao tentar fazer login: " + error.message);
      return false;
    }
  }
  async verificaEmpresaCadastrada() {
    const URL = "http://localhost:8080/apis/param/1";
    const token = authManager.getToken();
    const response = await fetch(URL, {
      method: "GET",
      headers: {
          "Authorization": "Bearer " + token, // Adiciona o token no cabeçalho
      },
    });
    if (!response.ok) {
        return false;
    }
    return true;
    }
}

// Cria instância global do gerenciador de autenticação
const authManager = new AuthManager();

// Exporta a instância
window.authManager = authManager;
