function createAuthenticatedClient() {
  return {
    // Método GET autenticado
    async get(url) {
      if (!await authManager.verificarToken()) {
        throw new Error("Sessão expirada. Por favor, faça login novamente.");
      }
      
      const token = authManager.getToken();
      const response = await fetch(url, {
        method: "GET",
        headers: {
          "Authorization": `Bearer ${token}`
        }
      });
      
      if (response.status === 401 || response.status === 403) {
        authManager.logout();
        throw new Error("Sessão expirada. Por favor, faça login novamente.");
      }
      
      return response;
    },
    
    // Método POST autenticado
    async post(url, data) {
      if (!await authManager.verificarToken()) {
        throw new Error("Sessão expirada. Por favor, faça login novamente.");
      }
      
      const token = authManager.getToken();
      const response = await fetch(url, {
        method: "POST",
        headers: {
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
      });
      
      if (response.status === 401 || response.status === 403) {
        authManager.logout();
        throw new Error("Sessão expirada. Por favor, faça login novamente.");
      }
      
      return response;
    },
    
    // Adicione outros métodos conforme necessário (PUT, DELETE, etc.)
  };
}

// Cliente HTTP autenticado para uso em toda a aplicação
const httpClient = createAuthenticatedClient();

// Exporta para uso global
window.httpClient = httpClient;