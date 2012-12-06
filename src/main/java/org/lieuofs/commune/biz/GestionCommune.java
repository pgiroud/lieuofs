/**
 * This file is part of LieuOFS.
 *
 * LieuOFS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * LieuOFS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LieuOFS.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lieuofs.commune.biz;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.lieuofs.Mutable;
import org.lieuofs.TypeMutation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.lieuofs.canton.CantonCritere;
import org.lieuofs.canton.ICanton;
import org.lieuofs.canton.biz.IGestionCanton;
import org.lieuofs.commune.CommuneCritere;
import org.lieuofs.commune.ICommuneSuisse;
import org.lieuofs.commune.IMutationCommune;
import org.lieuofs.commune.MutationCommuneCritere;
import org.lieuofs.commune.TypeMutationCommune;
import org.lieuofs.commune.biz.dao.CommuneOFSDao;
import org.lieuofs.commune.biz.dao.PersistCommune;
import org.lieuofs.district.IDistrict;
import org.lieuofs.district.biz.IGestionDistrict;

import static org.lieuofs.TypeMutation.CHGT_NOM_COMM;
import static org.lieuofs.TypeMutation.CHGT_NOM_DIST;
import static org.lieuofs.TypeMutation.MODIF_TERR;
import static org.lieuofs.TypeMutation.RATT;
import static org.lieuofs.TypeMutation.RENUMEROTATION_FORMELLE;
import static org.lieuofs.district.TypeDistrict.DISTRICT;

public class GestionCommune implements IGestionCommune {

	final Logger logger = LoggerFactory.getLogger(GestionCommune.class);
	private CommuneOFSDao dao;
	private IGestionCanton gestionnaireCanton;
	private IGestionDistrict gestionnaireDistrict;
	
	private final Comparator<Date> comparateurDate = new StrictDateComparator();
	
	/**
	 * @param dao the dao to set
	 */
	public void setDao(CommuneOFSDao dao) {
		this.dao = dao;
	}

	/**
	 * @param gestionnaireCanton the gestionnaireCanton to set
	 */
	public void setGestionnaireCanton(IGestionCanton gestionnaireCanton) {
		this.gestionnaireCanton = gestionnaireCanton;
	}

	/**
	 * @param gestionnaireDistrict the gestionnaireDistrict to set
	 */
	public void setGestionnaireDistrict(IGestionDistrict gestionnaireDistrict) {
		this.gestionnaireDistrict = gestionnaireDistrict;
	}

	private ICanton lireCanton(String codeCanton) {
		CantonCritere critere = new CantonCritere();
		critere.setCodeISO2(codeCanton);
		List<ICanton> liste = gestionnaireCanton.rechercher(critere);
		assert 1 == liste.size();
		return liste.get(0);
	}
	
	private IDistrict lireDistrict(Long id) {
		return gestionnaireDistrict.lire(id);
	}
	
	private CommuneSuisse creerCommune(PersistCommune communePersistante) {
		if (null == communePersistante) return null;
		CommuneSuisse commune = new CommuneSuisse(communePersistante);
		commune.setCanton(lireCanton(communePersistante.getCodeCanton()));
		IDistrict district = lireDistrict(communePersistante.getDistrictId());
		if (DISTRICT.equals(district.getTypeEnregistrement())) {
			commune.setDistrict(district);
		}
		return commune;
	}
	
	@Override
	public ICommuneSuisse lire(Long id) {
		if (null == id) throw new IllegalArgumentException("L'identifiant d'historisation ne peut pas être nul !!");
		PersistCommune communePersistante = dao.lire(id);
		return creerCommune(communePersistante);
	}

	@Override
	public List<ICommuneSuisse> rechercher(CommuneCritere critere) {
		if (null == critere) throw new IllegalArgumentException("Le critère de recherche ne peut pas être nul !");
		List<PersistCommune> listes = dao.rechercher(critere);
		List<ICommuneSuisse> listesCommunes = new ArrayList<ICommuneSuisse>();
		for (PersistCommune persistant : listes) listesCommunes.add(creerCommune(persistant));
		return listesCommunes;
	}

	private List<PersistCommune> getCommunesRadiees(List<PersistCommune> communes, int numMutation) {
		List<PersistCommune> communesFiltrees = new ArrayList<PersistCommune>();
		for (PersistCommune commune : communes) {
			Mutable radiation = commune.getRadiation();
			if (null != radiation && numMutation == radiation.getNumero()) communesFiltrees.add(commune);
		}
		return communesFiltrees;
	}
	
	private List<PersistCommune> getCommunesCrees(List<PersistCommune> communes, int numMutation) {
		List<PersistCommune> communesFiltrees = new ArrayList<PersistCommune>();
		for (PersistCommune commune : communes) {
			Mutable inscription = commune.getInscription();
			if (numMutation == inscription.getNumero()) communesFiltrees.add(commune);
		}
		return communesFiltrees;
	}

	private TypeMutation getUniqueType(PersistCommune communeSource, PersistCommune communeCible) {
		TypeMutation typeSource = communeSource.getRadiation().getMode();
		TypeMutation typeCible = communeCible.getInscription().getMode();
		if (typeSource.equals(typeCible)) {
			return typeSource;
		} else throw new IllegalArgumentException("Il n'existe pas un type unique mais 2 types : " + typeSource + ", " + typeCible);
	}
	
	
	protected TypeMutationCommune obtenirTypeMutation(List<PersistCommune> communesSource, List<PersistCommune> communesCible) {
		if (communesSource.size() > 1) {
			// Fusion ou inclusion de commune
			boolean hasModifTerritoire = false;
			for (PersistCommune commune : communesSource) if (MODIF_TERR.equals(commune.getRadiation().getMode())) hasModifTerritoire = true;
			if (hasModifTerritoire) return TypeMutationCommune.INCLUSION;
			else return TypeMutationCommune.FUSION;
		} else {
			if (communesCible.size() > 1) {
				// scission ou exclusion
				boolean hasModifTerritoire = false;
				for (PersistCommune commune : communesCible) if (MODIF_TERR.equals(commune.getInscription().getMode())) hasModifTerritoire = true;
				if (hasModifTerritoire) return TypeMutationCommune.EXCLUSION;
				else return TypeMutationCommune.SCISSION;
			} else {
				PersistCommune communeSource = communesSource.get(0);
				PersistCommune communeCible = communesCible.get(0);
				TypeMutation type = this.getUniqueType(communeSource,communeCible);
				if (MODIF_TERR.equals(type)) return TypeMutationCommune.ECHANGE_TERRITOIRE;
				if (CHGT_NOM_COMM.equals(type)) return TypeMutationCommune.CHANGEMENT_NOM;
				if (RATT.equals(type)) {
					if (communeSource.getCodeCanton().equals(communeCible.getCodeCanton())) {
						return TypeMutationCommune.CHANGEMENT_DISTRICT;
					} else {
						return TypeMutationCommune.CHANGEMENT_CANTON;
					}
				}
				if (RENUMEROTATION_FORMELLE.equals(type)) return TypeMutationCommune.RENUMEROTATION_FORMELLE;
				if (CHGT_NOM_DIST.equals(type)) return TypeMutationCommune.CHANGEMENT_NOM_DISTRICT;
				logger.error("Impossible de définir une mutation pour le type " + type);
				throw new IllegalArgumentException("Impossible de définir une mutation pour le type " + type);
			}
				
		}
	}
		
	protected IMutationCommune creerMutation(int numero, List<PersistCommune> communesSource, List<PersistCommune> communesCible) {
		TypeMutationCommune type = this.obtenirTypeMutation(communesSource, communesCible);
		MutationCommune mutation = new MutationCommune(type);
		mutation.setNumero(numero);
		for (PersistCommune persistant : communesSource) {
			mutation.ajouterCommuneOrigine(this.creerCommune(persistant));
		}
		for (PersistCommune persistant : communesCible) {
			mutation.ajouterCommuneCible(this.creerCommune(persistant));
		}
		mutation.setDateEffet(communesCible.get(0).getInscription().getDate());
		return mutation;
	}
	
	
	@Override
	public IMutationCommune lireMutation(int numero) {
		List<PersistCommune> communes = dao.getMutation(numero);
		List<PersistCommune> communesCrees = this.getCommunesCrees(communes, numero);
		List<PersistCommune> communesRadiees = this.getCommunesRadiees(communes, numero);
		return creerMutation(numero,communesRadiees,communesCrees);
	}

	private boolean isInDate(Date dateDebut, Date dateFin, Date date) {
		if (null == dateDebut && null == dateFin) return true;
		if (null == dateDebut) {
			// la date de fin n'est pas nulle
			// on s'assure que la date est plus petite que la date de fin
			return 0 <= comparateurDate.compare(dateFin, date);
		}
		if (null == dateFin) {
			// La date de début n'est pas nulle
			// On s'assure que la date est plus grande que la date de début
			return 0 <= comparateurDate.compare(date, dateDebut);
		}
		// La date de début et la date de fin ne sont pas nulles
		return 0 <= comparateurDate.compare(dateFin, date) 
				&& 0 <= comparateurDate.compare(date, dateDebut);
	}
	
	private List<Integer> filtrerMutation(PersistCommune commune, Date dateDebut, Date dateFin) {
		List<Integer> liste = new ArrayList<Integer>();
		Mutable mutation = commune.getInscription();
		if (!TypeMutation.PREMIERE_SAISIE.equals(mutation.getMode())) {
			if (isInDate(dateDebut,dateFin,mutation.getDate())) liste.add(mutation.getNumero());
		}
		mutation = commune.getRadiation();
		if (null != mutation) {
			if (isInDate(dateDebut,dateFin,mutation.getDate())) liste.add(mutation.getNumero());
		}
		return liste;
	}
	
	
	/* (non-Javadoc)
	 * @see org.lieuofs.commune.biz.IGestionCommune#rechercherMutation(org.lieuofs.commune.MutationCommuneCritere)
	 */
	@Override
	public List<IMutationCommune> rechercherMutation(
			MutationCommuneCritere critere) {
		if (null == critere) throw new IllegalArgumentException("Le critère de recherche ne peut pas être nul !");
		CommuneCritere critereCommune = new CommuneCritere();
		critereCommune.setCodeCanton(critere.getCodeCanton());
		List<PersistCommune> listes = dao.rechercher(critereCommune);
		Set<Integer> numMutation = new HashSet<Integer>();
		for (PersistCommune commune : listes) {
			for (Integer numero : filtrerMutation(commune,critere.getDateDebut(), critere.getDateFin())) {
				numMutation.add(numero);
			}
		}
		List<IMutationCommune> mutations = new ArrayList<IMutationCommune>();
		for (Integer numero : numMutation) {
			IMutationCommune mutation = this.lireMutation(numero);
			if (!critere.getTypeAExclure().contains(mutation.getType())) {
				mutations.add(mutation);
			}
		}
		Collections.sort(mutations, new Comparator<IMutationCommune>() {

			/* (non-Javadoc)
			 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
			 */
			@Override
			public int compare(IMutationCommune o1, IMutationCommune o2) {
				return comparateurDate.compare(o1.getDateEffet(), o2.getDateEffet());
			}
			
		});
		return mutations;
	}

	   /**
	    * Cette classe de comparateur permet de comparer 2 dates (java.util.Date) en ne considérant
	    * que les parties jour, mois, année pour faire la comparaison.
	    * <br><br>
	    *
	    * Ainsi, les parties heure / minute / seconde de l'une ou l'autre des dates sont ignorées.
	    *
	    *
	    * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
	    */
	   private final static class StrictDateComparator implements Comparator<Date>
	   {
	      /**
	       * Compare 2 dates en ne considérant que les informations jour, mois et année.
	       * <br>
	       *
	       * Les paramètres doivent être des dates (java.util.Date) non nulles.
	       *
	       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	       * @see java.util.Date
	       */
	      public int compare(Date pO1, Date pO2)
	      {
	         Calendar cal = Calendar.getInstance();
	         cal.setTime(pO1);
	         int date1 = cal.get(Calendar.YEAR)*10000 + cal.get(Calendar.MONTH) * 100 + cal.get(Calendar.DATE);
	         cal.setTime(pO2);
	         int date2 = cal.get(Calendar.YEAR)*10000 + cal.get(Calendar.MONTH) * 100 + cal.get(Calendar.DATE);
	         return date1 - date2;
	      }
	   }
	
	
}
