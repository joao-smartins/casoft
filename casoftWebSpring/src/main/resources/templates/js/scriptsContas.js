document.addEventListener("DOMContentLoaded", carregarContas);

async function carregarContas() {
    const response = await fetch("http://localhost:8080/apis/conta");
    const contas = await response.json();
    const tbody = document.querySelector("#tabelaContas tbody");
    tbody.innerHTML = "";

    contas.forEach(conta => {
        tbody.innerHTML += `
      <tr>
        <td>${conta.agencia}</td>
        <td>${conta.numero}</td>
        <td>${conta.banco}</td>
        <td>${conta.telefone}</td>
        <td>${conta.endereco}, ${conta.ende_num}</td>
        <td>${conta.gerente}</td>
        <td>
          <button class="btn btn-sm btn-warning" onclick='editarConta(${JSON.stringify(conta)})'>Editar</button>
          <button class="btn btn-sm btn-danger" onclick='excluirConta(${conta.id})'>Excluir</button>
        </td>
      </tr>
    `;
    });
}

function editarConta(conta) {
    document.getElementById("idConta").value = conta.id;
    document.getElementById("agencia").value = conta.agencia;
    document.getElementById("numero").value = conta.numero;
    document.getElementById("banco").value = conta.banco;
    document.getElementById("telefone").value = conta.telefone;
    document.getElementById("endereco").value = conta.endereco;
    document.getElementById("ende_num").value = conta.ende_num;
    document.getElementById("gerente").value = conta.gerente;
}

async function salvarConta(event) {
    event.preventDefault();

    let hasErrors = false;
    const removeErrorMessage = (campo) => {
        const errorElement = document.getElementById(`${campo}-error`);
        if (errorElement) errorElement.remove();
    };

    const showError = (campo, message) => {
        const input = document.getElementById(campo);
        input.style.border = "2px solid red";
        const errorDiv = document.createElement('div');
        errorDiv.id = `${campo}-error`;
        errorDiv.className = 'error-message';
        errorDiv.style.color = 'red';
        errorDiv.style.fontSize = '12px';
        errorDiv.style.marginTop = '5px';
        errorDiv.textContent = message;
        input.parentNode.appendChild(errorDiv);
        hasErrors = true;
    };

    // Reset borders and remove error messages
    const camposObrigatorios = ["agencia", "numero", "banco", "telefone", "endereco", "ende_num", "gerente"];
    camposObrigatorios.forEach(campo => {
        document.getElementById(campo).style.border = "1px solid #ced4da";
        removeErrorMessage(campo);
    });

    // Validação dos campos obrigatórios
    const camposVazios = camposObrigatorios.filter(campo => !document.getElementById(campo).value.trim());
    camposVazios.forEach(campo => {
        showError(campo, "Este campo é obrigatório");
    });

    // Validação da agência
    const agencia = document.getElementById("agencia").value.toString();
    if (agencia && (agencia.length < 4 || agencia.length > 5)) {
        showError("agencia", "A agência deve ter 4 ou 5 dígitos");
    }

    // Validação do número da conta
    const numeroLimpo = document.getElementById("numero").value.replace(/\D/g, '');
    if (numeroLimpo && numeroLimpo.length !== 8 && numeroLimpo.length !== 10) {
        showError("numero", "O número da conta deve ter 8 ou 10 dígitos");
    }

    // Validação do telefone
    const telefone = document.getElementById("telefone").value.replace(/\D/g, '');
    if (telefone && telefone.length !== 11) {
        showError("telefone", "O telefone deve ter 11 dígitos (incluindo DDD)");
    }

    if (hasErrors) {
        return false;
    }

    const conta = {
        id: document.getElementById("idConta").value,
        agencia: parseInt(document.getElementById("agencia").value),
        numero: document.getElementById("numero").value,
        banco: document.getElementById("banco").value,
        telefone: document.getElementById("telefone").value,
        endereco: document.getElementById("endereco").value,
        ende_num: parseInt(document.getElementById("ende_num").value),
        gerente: document.getElementById("gerente").value
    };

    const metodo = conta.id ? "PUT" : "POST";
    const url = conta.id
        ? `http://localhost:8080/apis/conta?id_conta=${conta.id}`
        : "http://localhost:8080/apis/conta";

    await fetch(url, {
        method: metodo,
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(conta)
    });

    limparFormulario();
    await carregarContas();
}

async function excluirConta(id) {
    if (!confirm("Deseja excluir esta conta bancária?")) return;

    await fetch(`http://localhost:8080/apis/conta/${id}`, {
        method: "DELETE"
    });

    await carregarContas();
}

function limparFormulario() {
    document.getElementById("formConta").reset();
    document.getElementById("idConta").value = "";
}


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

