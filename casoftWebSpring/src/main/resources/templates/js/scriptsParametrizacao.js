// Limpar formulário
function limparFormulario() {
  document.getElementById('formEmpresa').reset();
  document.getElementById('logoPreview').style.display = 'none';
}

// Enviar formulário
function enviarFormulario(event) {
    event.preventDefault(); // Evita o recarregamento da página

    var fdados = document.getElementById("formEmpresa");

    const cnpjInput = document.getElementById('cnpj');
    const cnpjValue = cnpjInput.value;
    const cnpjFeedback = document.getElementById('cnpjFeedback');

    let isCnpjValid = validarCNPJ(cnpjValue);

    if (!isCnpjValid) {
        cnpjInput.classList.add('is-invalid');
        cnpjFeedback.textContent = 'CNPJ inválido.';
        cnpjInput.focus(); 
        return false; 
    } else {
        cnpjInput.classList.remove('is-invalid');
        cnpjInput.classList.add('is-valid');
        cnpjFeedback.textContent = '';
    }

    const cepValido = validarCampo('cep', 8, 'CEP deve ter 8 dígitos.');
    const telValido = validarCampo('telefone', 10, 'Telefone deve ter 10 ou 11 dígitos.');

    if (!isCnpjValid || !cepValido || !telValido) {
        return false;
    }

    const token = authManager.getToken();

    const URL = "http://localhost:8080/apis/param";
    const metodo = modoCadastro ? "POST" : "PUT";
    let formData = new FormData(fdados);

    fetch(URL, {
        method: metodo,
        headers: {
            "Authorization": "Bearer " + token
        },
        body: formData
    })
    .then((response) => response.json())
    .then((json) => {
        alert(json.mensagem);
        // Opcional: Limpar formulário se o cadastro/alteração for bem-sucedido
        // limparFormulario();
    })
    .catch((error) => {
        alert("Erro ao cadastrar empresa. Por favor, tente novamente. " + error.message);
        console.error("Erro:", error);
    });

    return false; // Retorna false para evitar submissão padrão do formulário
}

// Event listeners para formatação de campos

// Formatação de CNPJ
document.getElementById('cnpj').addEventListener('input', function(e) {
  let value = e.target.value.replace(/\D/g, '');
  if (value.length > 14) value = value.slice(0, 14);
  
  if (value.length > 12) {
    value = value.replace(/^(\d{2})(\d{3})(\d{3})(\d{4})(\d{2}).*/, '$1.$2.$3/$4-$5');
  } else if (value.length > 8) {
    value = value.replace(/^(\d{2})(\d{3})(\d{3})(\d*)/, '$1.$2.$3/$4');
  } else if (value.length > 5) {
    value = value.replace(/^(\d{2})(\d{3})(\d*)/, '$1.$2.$3');
  } else if (value.length > 2) {
    value = value.replace(/^(\d{2})(\d*)/, '$1.$2');
  }
  
  e.target.value = value;
});

// Formatação de telefone
document.getElementById('telefone').addEventListener('input', function(e) {
  let value = e.target.value.replace(/\D/g, '');
  if (value.length > 11) value = value.slice(0, 11);
  
  if (value.length > 10) {
    value = value.replace(/^(\d{2})(\d{5})(\d{4}).*/, '($1) $2-$3');
  } else if (value.length > 6) {
    value = value.replace(/^(\d{2})(\d{4})(\d*)/, '($1) $2-$3');
  } else if (value.length > 2) {
    value = value.replace(/^(\d{2})(\d*)/, '($1) $2');
  }
  
  e.target.value = value;
});

// Formatação de CEP
document.getElementById('cep').addEventListener('input', function(e) {
  let value = e.target.value.replace(/\D/g, '');
  if (value.length > 8) value = value.slice(0, 8);
  
  if (value.length > 5) {
    value = value.replace(/^(\d{5})(\d{3}).*/, '$1-$2');
  }
  
  e.target.value = value;
});

// Busca de CEP
document.getElementById('cep').addEventListener('blur', function(e) {
  const cep = e.target.value.replace(/\D/g, '');
  if (cep.length === 8) {
    fetch(`https://viacep.com.br/ws/${cep}/json/`)
      .then(response => response.json())
      .then(data => {
        if (!data.erro) {
          document.getElementById('logradouro').value = data.logradouro;
          document.getElementById('bairro').value = data.bairro;
          document.getElementById('cidade').value = data.localidade;
          document.getElementById('estado').value = data.uf;
          // Coloca o foco no campo número
          document.getElementById('numero').focus();
        }
      })
      .catch(error => console.error('Erro na busca do CEP:', error));
  }
});



modoCadastro = true;
// Função para verificar se já existe uma empresa cadastrada
function carregarEmpresa() {
    modoCadastro = true;
    const URL = "http://localhost:8080/apis/param/1";
    const token = authManager.getToken();
    fetch(URL, {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token // Adiciona o token no cabeçalho
        },
    })
    .then((response) => {
        if (!response.ok) {
            throw new Error("Erro ao obter a empresa.");
        }
        return response.json();
    })
    .then((data) => {
        if (Object.keys(data).length === 0) {
            console.log("Nenhuma empresa cadastrada.");
            atualizarTexto(false);
            return;
        }
        // Preenche os campos com os dados da empresa
        document.getElementById('nomeEmpresa').value = data.nomeEmpresa;
        document.getElementById('cnpj').value = formatarCNPJ(data.cnpj);
        document.getElementById('logradouro').value = data.logradouro;
        document.getElementById('numero').value = data.numero;
        document.getElementById('bairro').value = data.bairro;
        document.getElementById('cidade').value = data.cidade;
        document.getElementById('estado').value = data.estado;
        document.getElementById('cep').value = formatarCEP(data.cep);
        document.getElementById('telefone').value = formatarTelefone(data.telefone);
        document.getElementById('email').value = data.email;
        document.getElementById('complemento').value = data.complemento;
        atualizarTexto(true);
        modoCadastro = false;
    });
}


// Verificar ao carregar a página
window.onload = carregarEmpresa;

function atualizarTexto(alteracao) {
    const tituloPagina = alteracao ? "Alteração de Empresa" : "Cadastro de Empresa";
    const tituloCard = alteracao ? "Alteração de Empresa" : "Cadastro de Empresa";
    const textoBotao = alteracao ? "Alterar" : "Cadastrar";

    // Mudar o título da página
    document.title = tituloPagina;

    // Mudar o título do formulário
    const titulo = document.querySelector(".card-section h3");
    if (titulo) titulo.textContent = tituloCard;

    // Mudar o texto do botão
    const botao = document.querySelector("button[type='submit']");
    if (botao) botao.textContent = textoBotao;

    // Mudar os links da sidebar
    const sidebarLinks = document.querySelectorAll(".sidebar a");
    sidebarLinks.forEach((link) => {
        if (link.textContent.includes("Cadastro")) {
            link.textContent = link.textContent.replace("Cadastro", "Alteração");
        }
    });
}
function formatarCNPJ(cnpj) {
    return cnpj.replace(/^(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})$/, "$1.$2.$3/$4-$5");
}
function formatarTelefone(telefone) {
  if (telefone.length === 11) {
      return telefone.replace(/^(\d{2})(\d{5})(\d{4})$/, "($1) $2-$3");
  }
  return telefone.replace(/^(\d{2})(\d{4})(\d{4})$/, "($1) $2-$3");
}
function formatarCEP(cep) {
    return cep.replace(/^(\d{5})(\d{3})$/, "$1-$2");
}

function validarCampo(inputId, length, message) {
  const input = document.getElementById(inputId);
  const feedback = document.getElementById(inputId + 'Feedback');
  if(inputId=='telefone'){
    if (input.value.replace(/\D/g, '').length !== length && input.value.replace(/\D/g, '').length !== length+1) {
      input.classList.add('is-invalid');
      feedback.textContent = message;
      return false;
    } else {
      input.classList.remove('is-invalid');
      feedback.textContent = '';
      return true;
    }
  }
  else if (input.value.replace(/\D/g, '').length !== length) {
    input.classList.add('is-invalid');
    feedback.textContent = message;
    return false;
  } else {
    input.classList.remove('is-invalid');
    feedback.textContent = '';
    return true;
  }
}
function validarCNPJ(cnpj) {
    cnpj = cnpj.replace(/[^\d]+/g, '');

    if (cnpj == '') return false;

    if (cnpj.length != 14) return false;

    if (cnpj == "00000000000000" ||
        cnpj == "11111111111111" ||
        cnpj == "22222222222222" ||
        cnpj == "33333333333333" ||
        cnpj == "44444444444444" ||
        cnpj == "55555555555555" ||
        cnpj == "66666666666666" ||
        cnpj == "77777777777777" ||
        cnpj == "88888888888888" ||
        cnpj == "99999999999999")
        return false;

    // Valida DVs
    let tamanho = cnpj.length - 2
    let numeros = cnpj.substring(0, tamanho);
    let digitos = cnpj.substring(tamanho);
    let soma = 0;
    let pos = tamanho - 7;
    for (let i = tamanho; i >= 1; i--) {
        soma += numeros.charAt(tamanho - i) * pos--;
        if (pos < 2)
            pos = 9;
    }
    let resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
    if (resultado != digitos.charAt(0))
        return false;

    tamanho = tamanho + 1;
    numeros = cnpj.substring(0, tamanho);
    soma = 0;
    pos = tamanho - 7;
    for (let i = tamanho; i >= 1; i--) {
        soma += numeros.charAt(tamanho - i) * pos--;
        if (pos < 2)
            pos = 9;
    }
    resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
    if (resultado != digitos.charAt(1))
        return false;

    return true;
}
