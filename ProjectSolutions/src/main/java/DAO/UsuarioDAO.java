package DAO;

import Model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author alexsander.mrocha
 */
public class UsuarioDAO {

    private static final Database db = new Database();

    public static boolean salvarUsuario(Usuario u) {
        Connection conn = db.obterConexao();
        try {
            PreparedStatement query = conn.prepareStatement("INSERT INTO "
                    + " tbl_usuario(nome, email, senha, status, fk_filial, fk_setor) "
                    + "VALUES (?, ?, ?, ?, ?, ?);");

            query.setString(1, u.getNome());
            query.setString(2, u.getEmail());
            query.setString(3, u.getSenha());
            query.setInt(4, 0);
            query.setInt(5, u.getCodigoFilial());
            query.setInt(6, u.getSetor());

            int rs = query.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }

        return true;
    }

    public static boolean alterarUsuario(Usuario u) {
        Connection conn = db.obterConexao();
        try {
            PreparedStatement query = conn.prepareStatement("UPDATE"
                    + " tbl_usuario SET nome = ?, email = ?, senha = ?, fk_filial = ?, fk_setor = ? where id_usuario = ?;");

            query.setString(1, u.getNome());
            query.setString(2, u.getEmail());
            query.setString(3, u.getSenha());
            query.setInt(4, u.getCodigoFilial());
            query.setInt(5, u.getSetor());
            query.setInt(6, u.getCodigo());

            int rs = query.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }

        return true;
    }

    public static boolean excluirUsuario(int uCodigo) {
        Connection conn = db.obterConexao();
        try {
            PreparedStatement query = conn.prepareStatement("UPDATE tbl_usuario SET status = 1 WHERE id_usuario = ?");

            query.setInt(1, uCodigo);

            query.execute();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }

        return true;
    }

    public static ArrayList<Usuario> getUsuarios() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        Connection conn = db.obterConexao();
        try {
            PreparedStatement query = conn.prepareStatement("SELECT u.id_usuario, u.nome, u.email, u.senha,"
                    + "    u.fk_filial, u.fk_setor, s.nome_setor, CONCAT(cidade, \" - \", estado)\n"
                    + "    FROM tbl_usuario AS u INNER JOIN tbl_setor AS s ON \n"
                    + "    u.fk_setor = s.id_setor \n"
                    + "    INNER JOIN tbl_filial AS f ON u.fk_filial = f.id_filial\n"
                    + "    WHERE u.status = 0;");

            ResultSet rs = query.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    Usuario user = new Usuario(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getInt(5),
                            rs.getInt(6)
                    );
                    user.setNomeSetor(rs.getString(7));
                    user.setNomeFilial(rs.getString(8));
                    usuarios.add(user);

                }
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return usuarios;
    }

    public static Usuario getUsuario(int codigoUsuario) {
        Usuario usuarios = null;
        Connection conn = db.obterConexao();
        try {
            PreparedStatement query = conn.prepareStatement("SELECT u.id_usuario, u.nome, u.email, u.senha,"
                    + " u.fk_filial, u.fk_setor, s.nome_setor, CONCAT(cidade, \" - \", estado)\n"
                    + " FROM tbl_usuario AS u INNER JOIN tbl_setor AS s ON "
                    + " u.fk_setor = s.id_setor "
                    + " INNER JOIN tbl_filial AS f ON u.fk_filial = f.id_filial "
                    + " where u.id_usuario = ? AND u.status = 0;");

            query.setInt(1, codigoUsuario);
            ResultSet rs = query.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    Usuario user = new Usuario(rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getInt(5),
                            rs.getInt(6));
                    user.setNomeSetor(rs.getString(7));
                    user.setNomeFilial(rs.getString(8));
                    usuarios = user;
                }
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return usuarios;
    }

    public static ArrayList<Usuario> getSetoresCadastro() {
        ArrayList<Usuario> setores = new ArrayList<>();
        Connection conn = db.obterConexao();
        try {
            PreparedStatement query = conn.prepareStatement("SELECT id_setor, nome_setor FROM tbl_setor;");

            ResultSet rs = query.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    Usuario user = new Usuario();
                    user.setSetor(rs.getInt(1));
                    user.setNomeSetor(rs.getString(2));
                    setores.add(user);
                }
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return setores;
    }

    public static ArrayList<Usuario> getFiliaisCadastro() {
        ArrayList<Usuario> filiais = new ArrayList<>();
        Connection conn = db.obterConexao();
        try {
            PreparedStatement query = conn.prepareStatement("SELECT id_filial, CONCAT(cidade, \" - \", estado) FROM tbl_filial WHERE status = 0;");

            ResultSet rs = query.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    Usuario user = new Usuario();
                    user.setCodigoFilial(rs.getInt(1));
                    user.setNomeFilial(rs.getString(2));
                    filiais.add(user);
                }
            }

            conn.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return filiais;
    }

    public static boolean getLogin(String email, String senha) {

        Connection conn = db.obterConexao();
        try {
            PreparedStatement query = conn.prepareStatement("SELECT * FROM tbl_usuario WHERE email= ? AND senha = ?;");
            query.setString(1, email);
            query.setString(2, senha);
            ResultSet rs = query.executeQuery();
            if (rs.next()) {
                return true;
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return false;
    }
}
