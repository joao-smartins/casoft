document.addEventListener("DOMContentLoaded", carregarMovimentacoes);

async function carregarMovimentacoes() {
    const response = await fetch("http://localhost:8080/apis/movimentacao");
    const movimentacoes = await response.json();
    const tbody = document.querySelector("#tabelaMovimentacoes tbody");
    tbody.innerHTML = "";

    movimentacoes.forEach(mov => {
        tbody.innerHTML += `
          <tr>
            <td>${mov.id}</td>
            <td>${mov.total || ""}</td>
            <td>${mov.data || ""}</td>
            <td>${mov.usuarioId || ""}</td>
            <td>${mov.contaBancariaId || ""}</td>
            <td>
              <button class="btn btn-sm btn-warning" onclick='editarMovimentacao(${JSON.stringify(mov)})'>Editar</button>
              <button class="btn btn-sm btn-danger" onclick='excluirMovimentacao(${mov.id})'>Excluir</button>
            </td>
          </tr>
        `;
    });
}

function editarMovimentacao(mov) {
    document.getElementById("idMovimentacao").value = mov.id;
    document.getElementById("total").value = mov.total || "";
    document.getElementById("data").value = mov.data || "";
    document.getElementById("usuario").value = mov.usuarioId || "";
    document.getElementById("conta").value = mov.contaBancariaId || "";
}

async function salvarMovimentacao(event) {
    event.preventDefault();
    const id = document.getElementById("idMovimentacao").value;
    const movimentacao = {
        total: parseFloat(document.getElementById("total").value),
        data: document.getElementById("data").value,
        usuarioUserId: document.getElementById("usuario").value,
        contabancariaContabId: document.getElementById("conta").value
    };
    let url = "http://localhost:8080/apis/movimentacao";
    let method = "POST";

    if (id) {
        url += `/${id}`;
        method = "PUT";
    }

    const response = await fetch(url, {
        method: method,
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(movimentacao)
    });

    const data = await response.json();
    if (response.ok) {
        alert(data.mensagem || "Operação realizada com sucesso!");
        document.getElementById("formMovimentacao").reset();
        carregarMovimentacoes();
    } else {
        alert(data.mensagem || "Erro ao salvar movimentação bancária.");
    }
    return false;
}

async function excluirMovimentacao(id) {
    if (!confirm("Tem certeza que deseja excluir esta movimentação bancária?")) return;
    const response = await fetch(`http://localhost:8080/apis/movimentacao/${id}`, {
        method: "DELETE"
    });
    const data = await response.json();
    if (response.ok) {
        alert(data.mensagem || "Excluído com sucesso!");
        carregarMovimentacoes();
    } else {
        alert(data.mensagem || "Erro ao excluir.");
    }
}

function limparFormulario() {
    document.getElementById("formMovimentacao").reset();
    document.getElementById("idMovimentacao").value = "";
}