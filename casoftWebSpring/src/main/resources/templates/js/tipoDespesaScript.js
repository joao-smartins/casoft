/* tipoDespesa */
function cadastrarTipo() {
  var tipoDespesa = document.getElementById("despesaPost").value;

  if (tipoDespesa.trim() === "") {
    document.getElementById("mensagem").innerText = "Erro: A descrição da despesa não pode estar vazia.";
    return;
  }

  var tipo = {
    nome: tipoDespesa
  };

  fetch("http://localhost:8080/apis/tipoDespesas", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(tipo)
  })
      .then(function(response) {
        if (response.ok) {
          return response.json();
        } else {
          return response.json().then(function(e) {
            throw new Error(e.mensagem);
          });
        }
      })
      .then(function() {
        document.getElementById("despesaPost").value = "";
        document.getElementById("mensagem").innerText = "";
        buscarTodosTipo();
      })
      .catch(function(error) {
        document.getElementById("mensagem").innerText = "Erro: " + error.message;
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
  var nome = document.getElementById("tipoDespesaUpdateNome").value;

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
      .then(function(response) {
        if (response.ok) {
          return response.json();
        }
        return response.json().then(function(error) {
          throw new Error(error.mensagem || "Erro desconhecido");
        });
      })
      .then(function(tipo) {
        alert("Tipo de despesa atualizada com sucesso!");
        document.getElementById("tipoDespesaUpdateID").value = "";
        document.getElementById("tipoDespesaUpdateNome").value = "";
        document.getElementById("formAtualizar").style.display = "none";
        buscarTodosTipo();
      })
      .catch(function(error) {
        alert("Erro ao atualizar tipo de despesa: " + error.message);
      });
}

function prepararEdicao(id, nome) {
  document.getElementById("tipoDespesaUpdateID").value = id;
  document.getElementById("tipoDespesaUpdateNome").value = nome;
  document.getElementById("btnAtualizar").style.display = "inline-block";
}

window.onload = buscarTodosTipo;
