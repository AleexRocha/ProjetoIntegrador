package ServletUsuario;

import DAO.UsuarioDAO;
import Model.Produto;
import Model.Usuario;
import Utils.CpfValidator;
import static Utils.Criptografia.criptografar;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author guilherme.pereira
 */
@WebServlet(name = "UsuarioEditarServlet", urlPatterns = {"/ti/editar_usuario"})
public class UsuarioEditarServlet extends HttpServlet {

    private void processaRequisicao(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String cCodigo = request.getParameter("codigoUsuario");
        String cNome = request.getParameter("nome");
        String cSenha = request.getParameter("senha");
        String cConfirmacaoSenha = request.getParameter("confirmarSenha");
        String cSetor = request.getParameter("codigoSetor");

        boolean error = false;
        if (cNome.length() == 0) {
            error = true;
            request.setAttribute("nomeErro", "Nome não informado");
        }
        if (cSenha.length() == 0) {
            error = true;
            request.setAttribute("senhaErro", "Senha não informada");
        }
        if (cConfirmacaoSenha.length() == 0) {
            error = true;
            request.setAttribute("cSenhaError", "Por Favor, Confirme a Senha digitada acima");
        }
        if (cSetor == null) {
            error = true;
            request.setAttribute("setorErro", "Setor não informado");
        }
        if (!error) {
            if (cSenha.length() != 0) {
                if (cConfirmacaoSenha.length() == 0) {
                    error = true;
                    request.setAttribute("cSenhaError", "Por Favor, Confirme a Senha digitada acima");
                }
                if (!cConfirmacaoSenha.equals(cSenha)) {
                    error = true;
                    request.setAttribute("varMsg", true);
                    request.setAttribute("cSenhaError", "Senhas não Coincidem");
                    request.setAttribute("msg", "Senha e Confirmação de Senha são diferentes");
                }
            }
        }

        if (error) {
            Usuario usuario = UsuarioDAO.getUsuario(Integer.parseInt(cCodigo));

            ArrayList<Usuario> setores = UsuarioDAO.getSetoresCadastro();

            request.setAttribute("acao", "editar");
            request.setAttribute("codigo", usuario.getCodigo());
            request.setAttribute("nome", usuario.getNome());
            request.setAttribute("email", usuario.getEmail());
            request.setAttribute("senha", usuario.getSenha());
            request.setAttribute("setor", usuario.getSetor());
            request.setAttribute("cpf", usuario.getCpf());
            request.setAttribute("nomeSetor", usuario.getNomeSetor());
            request.setAttribute("listaSetores", setores);

            if (!cSetor.equalsIgnoreCase("cliente")) {
                request.setAttribute("cliente", false);
            }

            request.setAttribute("varMsg", true);
            request.setAttribute("msg", "Erro ao editar o usuário, verifique os campos e tente novamente.");

            RequestDispatcher dispatcher = request.getRequestDispatcher("/ti/cadastro_usuarios.jsp");
            dispatcher.forward(request, response);
        } else {
            HttpSession userLogado = request.getSession();
            Usuario usuario = new Usuario();

            usuario.setCodigo(Integer.parseInt(cCodigo));
            usuario.setNome(cNome);
            usuario.setEmail(String.valueOf(userLogado.getAttribute("emailUsuario")));

            if (cSenha.length() != 0) {
                String senhaCriptografada = criptografar(cSenha);
                usuario.setSenha(senhaCriptografada);
            }

            usuario.setCpf(String.valueOf(userLogado.getAttribute("cpfUser")));
            usuario.setSetor(Integer.parseInt(cSetor));

            boolean httpOK = UsuarioDAO.alterarUsuario(usuario);

            if (httpOK) {
                ArrayList<Usuario> usuarios = UsuarioDAO.getUsuarios();
                request.setAttribute("listaUsuarios", usuarios);

                request.setAttribute("varMsg", true);
                request.setAttribute("msg", "Usuario editado com sucesso.");

                if (userLogado.getAttribute("nomeSetor").equals("Cliente")) {
                    ArrayList<Produto> produtos = DAO.ProdutoDAO.getProdutos();
                    request.setAttribute("listaProdutos", produtos);

                    RequestDispatcher dispatcher = request.getRequestDispatcher("../produtos/cliente_listagem_produtos.jsp");
                    dispatcher.forward(request, response);
                } else {
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/ti/listagem_usuarios.jsp");
                    dispatcher.forward(request, response);
                }

            } else {
                request.setAttribute("varMsgError", true);
                request.setAttribute("msg", "Erro ao salvar edição no banco de dados, verifique os campos e tente novamente.");

                RequestDispatcher dispatcher = request.getRequestDispatcher("/ti/cadastro_usuarios.jsp");
                dispatcher.forward(request, response);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processaRequisicao(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processaRequisicao(req, resp);
    }

}
