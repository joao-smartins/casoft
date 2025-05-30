document.addEventListener("DOMContentLoaded", carregarLancamentos);

async function carregarLancamentos() {
    const response = await fetch("http://localhost:8080/apis/lancamento-bancario");
    const lancamentos = await response.json();
    const tbody = document.querySelector("#tabelaLancamentos tbody");
    tbody.innerHTML = "";

    lancamentos.forEach(lanc => {
        tbody.innerHTML += `
          <tr>
            <td>${lanc.id}</td>
            <td>${lanc.dataLancamento || ""}</td>
            <td>${lanc.descricao}</td>
            <td>${lanc.origem || ""}</td>
            <td>${lanc.destino || ""}</td>
            <td>${lanc.contaBancariaId || ""}</td>
            <td>${lanc.receitaId || ""}</td>
            <td>${lanc.despesaId || ""}</td>
            <td>
              <button class="btn btn-sm btn-warning" onclick='editarLancamento(${JSON.stringify(lanc)})'>Editar</button>
              <button class="btn btn-sm btn-danger" onclick='excluirLancamento(${lanc.id})'>Excluir</button>
            </td>
          </tr>
        `;
    });
}

function editarLancamento(lanc) {
    document.getElementById("idLancamento").value = lanc.id;
    document.getElementById("descricao").value = lanc.descricao || "";
    document.getElementById("valor").value = lanc.valor || "";
    document.getElementById("data").value = lanc.dataLancamento || "";
    document.getElementById("origem").value = lanc.origem || "";
    document.getElementById("destino").value = lanc.destino || "";
    document.getElementById("conta").value = lanc.contaBancariaId || "";
    document.getElementById("receita").value = lanc.receitaId || "";
    document.getElementById("despesa").value = lanc.despesaId || "";
}

async function salvarLancamento(event) {
    event.preventDefault();
    const id = document.getElementById("idLancamento").value;
    const lancamento = {
        descricao: document.getElementById("descricao").value,
        valor: parseFloat(document.getElementById("valor").value),
        dataLancamento: document.getElementById("data").value,
        origem: document.getElementById("origem").value,
        destino: document.getElementById("destino").value,
        contaBancariaId: document.getElementById("conta").value,
        receitaId: document.getElementById("receita").value,
        despesaId: document.getElementById("despesa").value
    };
    let url = "http://localhost:8080/apis/lancamento-bancario";
    let method = "POST";

    if (id) {
        url += `/${id}`;
        method = "PUT";
    }

    const response = await fetch(url, {
        method: method,
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(lancamento)
    });

    const data = await response.json();
    if (response.ok) {
        alert(data.mensagem || "Operação realizada com sucesso!");
        document.getElementById("formLancamento").reset();
        carregarLancamentos();
    } else {
        alert(data.mensagem || "Erro ao salvar lançamento.");
    }
}

async function excluirLancamento(id) {
    if (!confirm("Tem certeza que deseja excluir este lançamento?")) return;
    const response = await fetch(`http://localhost:8080/apis/lancamento-bancario/${id}`, {
        method: "DELETE"
    });
    const data = await response.json();
    if (response.ok) {
        alert(data.mensagem || "Excluído com sucesso!");
        carregarLancamentos();
    } else {
        alert(data.mensagem || "Erro ao excluir.");
    }
}