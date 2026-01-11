package org.lieuofs.extraction.commune;

import org.lieuofs.canton.biz.GestionCanton;
import org.lieuofs.canton.biz.IGestionCanton;
import org.lieuofs.canton.biz.dao.CantonOFSDao;
import org.lieuofs.canton.biz.dao.CantonOFSFichierTxtDao;
import org.lieuofs.commune.biz.GestionCommune;
import org.lieuofs.commune.biz.IGestionCommune;
import org.lieuofs.commune.biz.dao.CommuneOFSDao;
import org.lieuofs.commune.biz.dao.CommuneOFSFichierTxtDao;
import org.lieuofs.district.biz.GestionDistrict;
import org.lieuofs.district.biz.IGestionDistrict;
import org.lieuofs.district.biz.dao.DistrictOFSDao;
import org.lieuofs.district.biz.dao.DistrictOFSFichierTxtDao;

import java.io.IOException;

public class ConstructeurContexteGestionCommune {

    private String cheminFichierOFS(String type) {
        return switch (type) {
            case "canton" -> "org/lieuofs/canton/biz/dao/GDEHist_KT.txt";
            case "district" -> "org/lieuofs/district/biz/dao/GDEHist_BEZ.txt";
            case "commune" -> "org/lieuofs/commune/biz/dao/GDEHist_GDE.txt";
            default -> "";
        };
    }

    private CommuneOFSDao construireDaoCommune() throws IOException {
        return new CommuneOFSFichierTxtDao(cheminFichierOFS("commune"));
    }

    private CantonOFSDao construireDaoCanton() throws IOException {
        return new CantonOFSFichierTxtDao(cheminFichierOFS("canton"));
    }

    private IGestionCanton construireGestionCanton() throws IOException {
        GestionCanton gestionnaire = new GestionCanton();
        gestionnaire.setDao(construireDaoCanton());
        return gestionnaire;
    }

    private DistrictOFSDao construireDaoDistrict() throws IOException {
        return new DistrictOFSFichierTxtDao(cheminFichierOFS("district"));
    }

    private IGestionDistrict construireGestionDistrict(IGestionCanton gestionCanton) throws IOException {
        GestionDistrict gestionnaire = new GestionDistrict();
        gestionnaire.setDao(construireDaoDistrict());
        gestionnaire.setGestionnaireCanton(gestionCanton);
        return gestionnaire;
    }




    public IGestionCommune construireGestionCommune() throws IOException {
        IGestionCanton gestionCanton = construireGestionCanton();
        IGestionDistrict gestionDistrict = construireGestionDistrict(gestionCanton);

        GestionCommune gestionnaire = new GestionCommune();
        gestionnaire.setDao(construireDaoCommune());
        gestionnaire.setGestionnaireCanton(gestionCanton);
        gestionnaire.setGestionnaireDistrict(gestionDistrict);
        return gestionnaire;
    }


}
