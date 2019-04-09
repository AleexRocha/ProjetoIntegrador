package Controller;

import DAO.FilialDAO;
import Model.Filial;
import java.util.ArrayList;

/**
 *
 * @author daniel
 */
public class FilialController {

    /**
     *
     * @param fLogradouro - String
     * @param fNumero - int
     * @param fCep - int
     * @param fBairro - String
     * @param fEstado - String
     * @param fCidade - String
     * @param fTelefone - int
     * @return
     */
    public static boolean salvarFiial(String fLogradouro, int fNumero, int fCep, String fBairro, String fEstado, String fCidade, int fTelefone) {
        Filial f = new Filial(fLogradouro, fNumero, fCep, fBairro, fEstado, fCidade, fTelefone);
        return FilialDAO.salvarFilial(f);
    }

    /**
     * @param fCodigo - int
     * @return
     *
     */
    public static boolean excluirFilial(int fCodigo) {
        return FilialDAO.excluirFilial(fCodigo);
    }

    /**
     *
     * @param fCodigo - int
     * @param fLogradouro - String
     * @param fNumero - int
     * @param fCep - int
     * @param fBairro - String
     * @param fEstado - String
     * @param fCidade - String
     * @param fTelefone - int
     * @return
     */
    public static boolean atualizarFilial(int fCodigo, String fLogradouro, int fNumero, int fCep, String fBairro, String fEstado, String fCidade, int fTelefone) {
        Filial f = new Filial(fLogradouro, fNumero, fCep, fBairro, fEstado, fCidade, fTelefone);
        f.setCodigo(fCodigo);
        return FilialDAO.atualizarFilial(f);
    }

    public static ArrayList<String[]> getFilial() {
        ArrayList<Filial> filiais = FilialDAO.getFiliais();
        ArrayList<String[]> listaFiliais = new ArrayList<>();

        for (int i = 0; i < filiais.size(); i++) {
            listaFiliais.add(
                    new String[]{
                        String.valueOf(filiais.get(i).getCodigo()),
                        filiais.get(i).getLogradouro(),
                        String.valueOf(filiais.get(i).getNumero()),
                        String.valueOf(filiais.get(i).getCep()),
                        filiais.get(i).getBairro(),
                        filiais.get(i).getEstado(),
                        filiais.get(i).getCidade(),
                        String.valueOf(filiais.get(i).getTelefone()),});
        }
        return listaFiliais;
    }
}
