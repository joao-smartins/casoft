//package casoft.mvc.view;
//
//import casoft.mvc.controller.UsuarioController;
//import casoft.mvc.model.Usuario;
//
//import java.util.List;
//import java.util.Scanner;
//
//public class UsuarioView {
//    private UsuarioController controller = new UsuarioController();
//    private Scanner scanner = new Scanner(System.in);
//
//    public void mostrarTelaLogin() {
//        System.out.println("=== LOGIN ===");
//        System.out.print("Login: ");
//        String login = scanner.nextLine();
//        System.out.print("Senha: ");
//        String senha = scanner.nextLine();
//
//        Usuario usuario = controller.fazerLogin(login, senha);
//        if (usuario != null) {
//            System.out.println("Bem-vindo, " + usuario.getNome() + "!");
//            // Menu principal pd ser chamado aq
//        } else {
//            System.out.println("Login ou senha incorretos!");
//        }
//    }
//
//
//    public void mostrarTelaCadastro() {
//        System.out.println("=== CADASTRO DE USUÁRIO ===");
//
//        System.out.print("Nome: ");
//        String nome = scanner.nextLine();
//
//        System.out.print("Login: ");
//        String login = scanner.nextLine();
//
//        System.out.print("Senha: ");
//        String senha = scanner.nextLine();
//
//        System.out.print("Nível de Acesso (ADMIN/USUARIO): ");
//        String nivelAcesso = scanner.nextLine().toUpperCase();
//
//        System.out.print("CPF (apenas números): ");
//        String cpf = formatarCPF(scanner.nextLine());
//
//        System.out.print("Telefone (apenas números): ");
//        String telefone = formatarTelefone(scanner.nextLine());
//
//        boolean sucesso = controller.cadastrarUsuario(nome, login, senha, nivelAcesso, cpf, telefone);
//        if (sucesso) {
//            System.out.println("Usuário cadastrado com sucesso!");
//        } else {
//            System.out.println("Erro ao cadastrar usuário. Verifique os dados.");
//        }
//    }
//
//
//    public void mostrarTelaConsulta() {
//        System.out.println("=== CONSULTA DE USUÁRIOS ===");
//        System.out.println("Filtros disponíveis:");
//        System.out.println("1 - Por nome");
//        System.out.println("2 - Por nível de acesso");
//        System.out.println("3 - Por status (ativo/inativo)");
//        System.out.print("Escolha uma opção: ");
//
//        int opcao = Integer.parseInt(scanner.nextLine());
//        String filtro = "";
//
//        switch (opcao) {
//            case 1:
//                System.out.print("Digite o nome: ");
//                filtro = "nome LIKE '%" + scanner.nextLine() + "%'";
//                break;
//            case 2:
//                System.out.print("Digite o nível (ADMIN/USUARIO): ");
//                filtro = "nivel_acesso = '" + scanner.nextLine().toUpperCase() + "'";
//                break;
//            case 3:
//                System.out.print("Ativo? (true/false): ");
//                filtro = "ativo = " + scanner.nextLine();
//                break;
//            default:
//                filtro = "";
//        }
//
//        List<Usuario> usuarios = controller.listarUsuarios(filtro);
//        System.out.println("\n=== RESULTADOS ===");
//        for (Usuario usuario : usuarios) {
//            System.out.printf(
//                    "ID: %d | Nome: %s | Login: %s | Nível: %s | Ativo: %s | CPF: %s | Telefone: %s\n",
//                    usuario.getId(),
//                    usuario.getNome(),
//                    usuario.getLogin(),
//                    usuario.getNivelAcesso(),
//                    usuario.isAtivo() ? "Sim" : "Não",
//                    usuario.getCpf(),
//                    usuario.getTelefone()
//            );
//        }
//    }
//
//    private String formatarCPF(String cpf) {
//        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
//    }
//
//    private String formatarTelefone(String telefone) {
//        return telefone.replaceAll("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
//    }
//}