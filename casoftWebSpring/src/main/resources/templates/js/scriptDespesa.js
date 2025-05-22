
    // Função para definir a data atual nos campos de data
    document.addEventListener('DOMContentLoaded', function() {
      const hoje = new Date().toISOString().split('T')[0];
      document.getElementById('despesa_dt_lanc').value = hoje;
    });

    // Função para preview do comprovante
    function previewComprovante(input) {
      const file = input.files[0];
      const imgPreview = document.getElementById('comprovantePreview');
      const pdfPreview = document.getElementById('pdfPreview');
      
      // Reset previews
      imgPreview.style.display = 'none';
      pdfPreview.style.display = 'none';
      
      if (file) {
        if (file.type.startsWith('image/')) {
          const reader = new FileReader();
          reader.onload = function(e) {
            imgPreview.src = e.target.result;
            imgPreview.style.display = 'block';
          };
          reader.readAsDataURL(file);
        } else if (file.type === 'application/pdf') {
          pdfPreview.style.display = 'block';
        }
      }
    }

    // Função para limpar o formulário
    function limparFormularioDespesa() {
      document.getElementById('formDespesa').reset();
      document.getElementById('comprovantePreview').style.display = 'none';
      document.getElementById('pdfPreview').style.display = 'none';
      
      // Redefine a data de lançamento para hoje
      const hoje = new Date().toISOString().split('T')[0];
      document.getElementById('despesa_dt_lanc').value = hoje;
    }

    // Função para enviar o formulário
    function enviarFormularioDespesa(event) {
        event.preventDefault(); // Evita o recarregamento da página
        var fdados = document.getElementById("formDespesa");
            
        // Obtendo o token do localStorage
        const token = authManager.getToken();
        const usuarioID = authManager.getId();

        const URL = "http://localhost:8080/apis/despesa";
        let formData = new FormData(fdados);

        const hoje = new Date().toISOString().split('T')[0];
        formData.append("data_lanc", hoje);
        formData.append("usuario_id", usuarioID);
        console.log(usuarioID);
            
        fetch(URL, {
            method: 'POST',
            headers: {
                "Authorization": "Bearer " + token
            },
            body: formData
        })
        .then((response) => response.json())
        .then((json) => {
            alert(json.mensagem);
        })
        .catch((error) => {
            alert("Erro ao cadastrar empresa. Por favor, tente novamente. " + error.message);
            console.error("Erro:", error);
        });
    }

    // Função para carregar categorias do banco de dados
    async function carregarCategorias() {
      try {
        const URL = "http://localhost:8080/apis/tipoDespesas";
        const response = await fetch(URL, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem('token') // Assumindo que o token está no localStorage
          }
        });
        
        if (response.ok) {
          const categorias = await response.json();
          const selectCategoria = document.getElementById('categoriadespesa_catdesp_id');
          
          // Limpa as opções existentes
          selectCategoria.innerHTML = '<option value="">Selecione uma categoria</option>';
          
          // Adiciona as categorias carregadas
          categorias.forEach(categoria => {
            const option = document.createElement('option');
            option.value = categoria.id;
            option.textContent = categoria.nome;
            selectCategoria.appendChild(option);
          });
        } else {
          console.error('Erro ao carregar categorias:', response.statusText);
          document.getElementById('categoriadespesa_catdesp_id').innerHTML = '<option value="">Erro ao carregar categorias</option>';
        }
      } catch (error) {
        console.error('Erro ao carregar categorias:', error);
        document.getElementById('categoriadespesa_catdesp_id').innerHTML = '<option value="">Erro ao carregar categorias</option>';
      }
    }

    // Função para carregar eventos do banco de dados
    async function carregarEventos() {
      try {
        const URL = "http://localhost:8080/apis/eventos";
        const response = await fetch(URL, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem('token') // Assumindo que o token está no localStorage
          }
        });
        
        if (response.ok) {
          const eventos = await response.json();
          const selectEvento = document.getElementById('evento_evento_id');
          
          // Limpa as opções existentes
          selectEvento.innerHTML = '<option value="">Nenhum evento selecionado</option>';
          
          // Adiciona os eventos carregados
          eventos.forEach(evento => {
            const option = document.createElement('option');
            option.value = evento.id;
            option.textContent = evento.nome;
            selectEvento.appendChild(option);
          });
        } else {
          console.error('Erro ao carregar eventos:', response.statusText);
          document.getElementById('evento_evento_id').innerHTML = '<option value="">Erro ao carregar eventos</option>';
        }
      } catch (error) {
        console.error('Erro ao carregar eventos:', error);
        document.getElementById('evento_evento_id').innerHTML = '<option value="">Erro ao carregar eventos</option>';
      }
    }

    // Função para carregar usuários e eventos dinamicamente
    function carregarDadosFormulario() {
      carregarCategorias();
      carregarEventos();
    }

    // Chama a função ao carregar a página
    carregarDadosFormulario();