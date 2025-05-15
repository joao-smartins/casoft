// auth.js
function checkAuth() {
    const token = localStorage.getItem("authToken");
    if (!token) {
        
        window.location.href = "ControleAcesso.html";
    }
}

// Chama a função automaticamente ao carregar a página
checkAuth();