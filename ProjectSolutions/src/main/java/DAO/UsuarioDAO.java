/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Model.Filial;
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

    private static Database db = new Database();

    public static boolean salvarUsuario(Usuario u) {
        Connection conn = db.obterConexao();
        try {
            PreparedStatement query = conn.prepareStatement("INSERT INTO"
                    + " TBL_USUARIO VALUES (?, ?, ?, ?, ?);");

            query.setString(1, u.getNome());
            query.setString(2, u.getEmail());
            query.setString(3, u.getSenha());
            query.setInt(4, u.getCodigoFilial());
            query.setInt(5, u.getSetor());

            int rs = query.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean alterarUsuario(Usuario u) {
        Connection conn = db.obterConexao();
        try {
            PreparedStatement query = conn.prepareStatement("UPDATE"
                    + " TBL_USUARIO SET (NOME = ?, EMAIL = ?, SENHA = ?, FILIAL = ?, SETOR = ?);");

            query.setString(1, u.getNome());
            query.setString(2, u.getEmail());
            query.setString(3, u.getSenha());
            query.setInt(4, u.getCodigoFilial());
            query.setInt(5, u.getSetor());

            int rs = query.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean excluirUsuario(int uCodigo) {
        Connection conn = db.obterConexao();
        try {
            PreparedStatement query = conn.prepareStatement("DELETE FROM TBL_USUARIO WHERE ID_USUARIO = ?");

            query.setInt(1, uCodigo);

            ResultSet linhasAfetadas = query.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static ArrayList<Usuario> getUsuarios() {
        ArrayList<Usuario> funcionarios = new ArrayList<>();
        Connection conn = db.obterConexao();
        try {
            PreparedStatement query = conn.prepareStatement("SELECT * FROM TBL_USUARIO "
                    + " INNER JOIN TBL_SETOR ON TBL_USUARIO.FK_SETOR = TBL_SETOR.ID_SETOR;");

            ResultSet rs = query.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    funcionarios.add(new Usuario(
                            rs.getInt(0),
                            rs.getString(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getInt(4),
                            rs.getInt(5)
                    ));

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return funcionarios;
    }

}
