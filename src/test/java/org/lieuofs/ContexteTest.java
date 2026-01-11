package org.lieuofs;

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
import org.lieuofs.etat.biz.GestionEtat;
import org.lieuofs.etat.biz.IGestionEtat;
import org.lieuofs.geo.territoire.biz.GestionTerritoire;
import org.lieuofs.geo.territoire.biz.IGestionTerritoire;
import org.lieuofs.geo.territoire.biz.dao.EtatTerritoireDao;
import org.lieuofs.geo.territoire.biz.dao.EtatTerritoireFichierXmlDao;

public enum ContexteTest {

    INSTANCE;

    private String cheminFichierOFS(String type) {
        return switch (type) {
            case "canton" -> "org/lieuofs/canton/biz/dao/GDEHist_KT.txt";
            case "district" -> "org/lieuofs/district/biz/dao/GDEHist_BEZ.txt";
            case "commune" -> "org/lieuofs/commune/biz/dao/GDEHist_GDE.txt";
            case "territoire" -> "org/lieuofs/geo/territoire/biz/dao/eCH0072.xml";
            default -> "";
        };
    }

    public CommuneOFSDao construireDaoCommune() {
        return new CommuneOFSFichierTxtDao(cheminFichierOFS("commune"));
    }

    public CantonOFSDao construireDaoCanton() {
        return new CantonOFSFichierTxtDao(cheminFichierOFS("canton"));
    }

    private IGestionCanton construireGestionCanton() {
        GestionCanton gestionnaire = new GestionCanton();
        gestionnaire.setDao(construireDaoCanton());
        return gestionnaire;
    }

    public DistrictOFSDao construireDaoDistrict() {
        return new DistrictOFSFichierTxtDao(cheminFichierOFS("district"));
    }

    public IGestionDistrict construireGestionDistrict(IGestionCanton gestionCanton) {
        GestionDistrict gestionnaire = new GestionDistrict();
        gestionnaire.setDao(construireDaoDistrict());
        gestionnaire.setGestionnaireCanton(gestionCanton);
        return gestionnaire;
    }

    public IGestionDistrict construireGestionDistrict() {
        GestionDistrict gestionnaire = new GestionDistrict();
        gestionnaire.setDao(construireDaoDistrict());
        gestionnaire.setGestionnaireCanton(construireGestionCanton());
        return gestionnaire;
    }


    public IGestionCommune construireGestionCommune() {
        IGestionCanton gestionCanton = construireGestionCanton();
        IGestionDistrict gestionDistrict = construireGestionDistrict(gestionCanton);

        GestionCommune gestionnaire = new GestionCommune();
        gestionnaire.setDao(construireDaoCommune());
        gestionnaire.setGestionnaireCanton(gestionCanton);
        gestionnaire.setGestionnaireDistrict(gestionDistrict);
        return gestionnaire;
    }

    public EtatTerritoireDao construireDaoEtatTerritoire() {
        return new EtatTerritoireFichierXmlDao(cheminFichierOFS("territoire"));
    }

    public IGestionEtat construireGestionEtat() {
        return new GestionEtat(construireDaoEtatTerritoire());
    }

    public IGestionTerritoire constuireGestionTerritoire() {
        EtatTerritoireDao dao = construireDaoEtatTerritoire();
        return new GestionTerritoire(dao,new GestionEtat(dao));
    }
}
