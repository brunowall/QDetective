package br.ufc.quixada.qdetective.dao;

import br.ufc.quixada.qdetective.models.Denuncia;

/**
 * Created by darkbyte on 23/11/17.
 */

public interface DenunciaDao {
    public void addDenuncia(Denuncia denuncia);
    public void removeDenuncia(Integer id);
    public Denuncia getDenunciabyID(Integer id);
}
