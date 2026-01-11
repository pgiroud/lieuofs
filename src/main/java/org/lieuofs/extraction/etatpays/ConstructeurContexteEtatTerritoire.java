package org.lieuofs.extraction.etatpays;

import org.lieuofs.canton.biz.dao.CantonOFSDao;
import org.lieuofs.canton.biz.dao.CantonOFSFichierTxtDao;
import org.lieuofs.geo.territoire.biz.dao.EtatTerritoireDao;
import org.lieuofs.geo.territoire.biz.dao.EtatTerritoireFichierXmlDao;

import java.io.IOException;

public class ConstructeurContexteEtatTerritoire {

    private String cheminFichierOFS() {
        return "org/lieuofs/geo/territoire/biz/dao/eCH0072_140101.xml";

    }

    private EtatTerritoireDao construireDaoEtatTerritoire() throws IOException {
        return new EtatTerritoireFichierXmlDao(cheminFichierOFS());
    }
}
