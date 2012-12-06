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
package org.lieuofs.commune.biz.dao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lieuofs.Mutable;
import org.lieuofs.TypeMutation;
import org.lieuofs.commune.CommuneCritere;
import org.lieuofs.commune.TypeCommune;
import org.lieuofs.util.dao.FichierOFSTxtDao;

public class CommuneOFSFichierTxtDao extends FichierOFSTxtDao implements
		CommuneOFSDao {

    /**************************************************/
    /****************** Attributs *********************/
    /**************************************************/

	private Map<Long,PersistCommune> mapParId = new HashMap<Long,PersistCommune>();
	private Map<String,List<PersistCommune>> mapParCanton = new HashMap<String,List<PersistCommune>>();
	private Map<Long,List<PersistCommune>> mapParDistrict = new HashMap<Long,List<PersistCommune>>();
	private Map<Integer,List<PersistCommune>> mapMutation = new HashMap<Integer,List<PersistCommune>>();

    /**************************************************/
    /**************** Constructeurs *******************/
    /**************************************************/

	public CommuneOFSFichierTxtDao() {
		super();
	}
	
	/**************************************************/
    /******************* Méthodes *********************/
    /**************************************************/

	@Override
	protected void traiterLigneFichier(String... tokens) throws ParseException {
		PersistCommune commune = new PersistCommune();
		commune.setId(Long.decode(tokens[0].trim()));
		commune.setDistrictId(Long.decode(tokens[1].trim()));
		commune.setCodeCanton(tokens[2].trim());
		commune.setNumero(Integer.decode(tokens[3].trim()));
		commune.setNom(tokens[4].trim());
		commune.setNomCourt(tokens[5].trim());
		commune.setTypeEnregistrement(TypeCommune.getParId(Long.decode(tokens[6].trim())));
		commune.setProvisoire(0 == Integer.parseInt(tokens[7].trim()));
		commune.setInscription(creerMutation(tokens[8].trim(),tokens[9].trim(),tokens[10].trim(),false));
		commune.setRadiation(creerMutation(tokens[11].trim(),tokens[12].trim(),tokens[13].trim(),true));
		commune.setDernierChangement(getDateFmt().parse(tokens[14].trim()));
		stockerCommune(commune);
	}

	
	private void stockerCommune(PersistCommune commune) {
		mapParId.put(commune.getId(), commune);
		
		String codeCanton = commune.getCodeCanton();
		if (!mapParCanton.containsKey(codeCanton)) mapParCanton.put(codeCanton, new ArrayList<PersistCommune>());
		mapParCanton.get(codeCanton).add(commune);

		Long districtId = commune.getDistrictId();
		if (!mapParDistrict.containsKey(districtId)) mapParDistrict.put(districtId, new ArrayList<PersistCommune>());
		mapParDistrict.get(districtId).add(commune);
		
		stockerMutation(commune);
		
	}
	
	
	private void stockerMutation(PersistCommune commune) {
		Mutable mutation = commune.getInscription();
		int cle = mutation.getNumero();
		if (!TypeMutation.PREMIERE_SAISIE.equals(mutation.getMode())) {
			if (!mapMutation.containsKey(cle)) mapMutation.put(cle, new ArrayList<PersistCommune>());
			mapMutation.get(mutation.getNumero()).add(commune);
		}
		mutation = commune.getRadiation();
		if (null != mutation) {
			cle = mutation.getNumero();
			if (!mapMutation.containsKey(cle)) mapMutation.put(cle, new ArrayList<PersistCommune>());
			mapMutation.get(mutation.getNumero()).add(commune);
		}
	}
	
	@Override
	public PersistCommune lire(Long id) {
		return mapParId.get(id);
	}

	private List<PersistCommune> filtrer(Collection<PersistCommune> listeAFiltrer, FiltreCommune filtre) {
		List<PersistCommune> communeFiltree = new ArrayList<PersistCommune>();
		for (PersistCommune commune : listeAFiltrer) {
			if (filtre.accept(commune)) communeFiltree.add(commune);
		}
		return communeFiltree;
	}
	
	@Override
	public List<PersistCommune> rechercher(CommuneCritere critere) {
		Collection<PersistCommune> communes = null;
		if (null != critere.getDistrict()) {
			communes = mapParDistrict.get(critere.getDistrict().getId());
		} else if (null != critere.getCodeCanton()) {
			communes = mapParCanton.get(critere.getCodeCanton());
		} else {
			communes = mapParId.values();
		}
		FiltreCommuneComposite filtres = new FiltreCommuneComposite();
		if (null != critere.getDateValidite()) {
			filtres.ajouterFiltre(new FiltreCommuneDateValidite(critere.getDateValidite()));
		}
		if (!EnumSet.complementOf(critere.getType()).isEmpty()) {
			filtres.ajouterFiltre(new FiltreTypeCommune(critere.getType()));
		}
		return filtrer(communes,filtres);
	}

	@Override
	public List<PersistCommune> getMutation(int numero) {
		return mapMutation.get(numero);
	}


	private interface FiltreCommune {
		boolean accept(PersistCommune commune);
	}
	
	private static class FiltreCommuneComposite implements FiltreCommune {

		private List<FiltreCommune> filtres = new ArrayList<FiltreCommune>();
		
		public void ajouterFiltre(FiltreCommune filtre) {
			filtres.add(filtre);
		}
		
		@Override
		public boolean accept(PersistCommune commune) {
			for (FiltreCommune filtre : filtres) {
				if (!filtre.accept(commune)) return false;
			}
			return true;
		}
	}
	
	private static class FiltreCommuneDateValidite implements FiltreCommune {

		   /**
		    * Ce comparateur permet de comparer 2 dates (java.util.Date) en ne considérant
		    * que les parties jour, mois, année pour faire la comparaison.
		    * <br><br>
		    *
		    * Ainsi, les parties heure / minute / seconde de l'une ou l'autre des dates sont ignorées.
		    *
		    */
		   public final static Comparator<Date> STRICT_DATE_COMPARATOR = new StrictDateComparator();

		   private final Date date;
		
		public FiltreCommuneDateValidite(Date date) {
			this.date = date;
		}
		
		
		@Override
		public boolean accept(PersistCommune commune) {
			if (0 < STRICT_DATE_COMPARATOR.compare(commune.getInscription().getDate(),date)) return false;
			if (null == commune.getRadiation()) return true;
			Date dateRadiation = commune.getRadiation().getDate();
			if (null != date) {
				if (0 < STRICT_DATE_COMPARATOR.compare(date, dateRadiation)) return false;
			}
			return true;
		}
		
	}

	private static class FiltreTypeCommune implements FiltreCommune {
		
		private final EnumSet<TypeCommune> types;
		
		public FiltreTypeCommune(EnumSet<TypeCommune> types) {
			this.types = types;
		}

		@Override
		public boolean accept(PersistCommune commune) {
			for (TypeCommune type : types) {
				if (type.equals(commune.getTypeEnregistrement())) return true;
			}
			return false;
		}
		
		
		
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
