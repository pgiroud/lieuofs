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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.lieuofs.TypeMutation;
import org.lieuofs.commune.ICommuneSuisse;
import org.lieuofs.commune.IMutationCommune;
import org.lieuofs.commune.TypeMutationCommune;

/**
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public class MutationCommune implements IMutationCommune {

	private final TypeMutationCommune typeMutation;
	
	private int numero;
	private List<ICommuneSuisse> communesOrigines = new ArrayList<ICommuneSuisse>();
	private List<ICommuneSuisse> communesCibles = new ArrayList<ICommuneSuisse>();
	private Date dateEffet;
	
	public MutationCommune(TypeMutationCommune type) {
		this.typeMutation = type;
	}
	
	
	@Override
	public List<ICommuneSuisse> getCommunesCibles() {
		return new ArrayList<ICommuneSuisse>(communesCibles);
	}

	@Override
	public List<ICommuneSuisse> getCommunesOrigines() {
		return new ArrayList<ICommuneSuisse>(communesOrigines);
	}

	@Override
	public TypeMutationCommune getType() {
		return typeMutation;
	}

	@Override
	public Date getDateEffet() {
		return dateEffet;
	}


	/**
	 * @param dateEffet the dateEffet to set
	 */
	public void setDateEffet(Date dateEffet) {
		this.dateEffet = dateEffet;
	}


	/**
	 * @param communeOrigine the communesOrigines to set
	 */
	public void ajouterCommuneOrigine(ICommuneSuisse communeOrigine) {
		this.communesOrigines.add(communeOrigine);
	}


	/**
	 * @param communeCible the communesCibles to set
	 */
	public void ajouterCommuneCible(ICommuneSuisse communeCible) {
		this.communesCibles.add(communeCible);
	}


	/* (non-Javadoc)
	 * @see org.lieuofs.commune.IMutationCommune#getNumero()
	 */
	@Override
	public int getNumero() {
		return numero;
	}


	/**
	 * @param numero the numero to set
	 */
	public void setNumero(int numero) {
		this.numero = numero;
	}

	
	private String getDesc(ICommuneSuisse commune) {
		return commune.getNom() + "(" + commune.getNumeroOFS() + ")";
	}
	
	private String getDesc(List<ICommuneSuisse> liste) {
		StringBuilder buffer = new StringBuilder();
		Iterator<ICommuneSuisse> iter = liste.iterator();
		buffer.append(getDesc(iter.next()));
		while (iter.hasNext()) {
			buffer.append(", ");
			buffer.append(getDesc(iter.next()));
		}
		return buffer.toString();
	}
	
	
	private List<ICommuneSuisse> getCommune(List<ICommuneSuisse> liste, TypeMutation type, boolean radiation) {
		List<ICommuneSuisse> listeRetour = new ArrayList<ICommuneSuisse>();
		for (ICommuneSuisse commune : liste) {
			if (radiation) {
				if (type.equals(commune.getRadiation().getMode())) listeRetour.add(commune);
			} else {
				if (type.equals(commune.getInscription().getMode())) listeRetour.add(commune);
			}
		}
		return listeRetour;
	}
	
	public String getDescription() {
		DateFormat dtFormat = new SimpleDateFormat("dd.MM.yyyy");
		switch (typeMutation) {
		case INCLUSION:
			List<ICommuneSuisse> communesIncluses = getCommune(communesOrigines, TypeMutation.RADIATION,true);
			StringBuilder builder = new StringBuilder("La commune ");
			builder.append(getDesc(communesCibles.get(0)));
			builder.append(" a absorbé ");
			if (communesIncluses.size() > 1) {
				builder.append("les communes ");
				builder.append(getDesc(communesIncluses));
			} else {
				builder.append("la commune ");
				builder.append(getDesc(communesIncluses.get(0)));
			}
			builder.append(" le ");
			builder.append(dtFormat.format(this.dateEffet));
			return  builder.toString();
		case FUSION:
			builder = new StringBuilder("La commune ");
			builder.append(getDesc(communesCibles.get(0)));
			builder.append(" est née de la fusion des communes ");
			builder.append(getDesc(communesOrigines));
			builder.append(" le ");
			builder.append(dtFormat.format(this.dateEffet));
			return  builder.toString();
		case CHANGEMENT_NOM:
			builder = new StringBuilder("Changement de nom ");
			builder.append(getDesc(communesOrigines));
			builder.append(" --> ");
			builder.append(getDesc(communesCibles));
			builder.append(" le ");
			builder.append(dtFormat.format(this.dateEffet));
			return  builder.toString();
		case CHANGEMENT_CANTON:
			ICommuneSuisse communeOrigine = communesOrigines.get(0);
			ICommuneSuisse communeCible = communesCibles.get(0);
			builder = new StringBuilder("Changement de canton ");
			builder.append(getDesc(communesOrigines));
			builder.append(" du canton ");
			builder.append(communeOrigine.getCanton().getCodeIso2());
			builder.append(" --> ");
			builder.append(getDesc(communesCibles));
			builder.append(" du canton ");
			builder.append(communeCible.getCanton().getCodeIso2());
			builder.append(" le ");
			builder.append(dtFormat.format(this.dateEffet));
			return  builder.toString();
		case CHANGEMENT_DISTRICT:
			communeOrigine = communesOrigines.get(0);
			communeCible = communesCibles.get(0);
			builder = new StringBuilder("Changement de district ");
			builder.append(getDesc(communesOrigines));
			builder.append(" du district ");
			builder.append(communeOrigine.getDistrict().getNom());
			builder.append(" --> ");
			builder.append(getDesc(communesCibles));
			builder.append(" du district ");
			builder.append(communeCible.getDistrict().getNom());
			builder.append(" le ");
			builder.append(dtFormat.format(this.dateEffet));
			return  builder.toString();
		case RENUMEROTATION_FORMELLE:
			builder = new StringBuilder("Renumérotation formelle ");
			builder.append(getDesc(communesOrigines));
			builder.append(" --> ");
			builder.append(getDesc(communesCibles));
			builder.append(" le ");
			builder.append(dtFormat.format(this.dateEffet));
			return  builder.toString();
		default:
			return "TODO";
		}
	}
	
}
