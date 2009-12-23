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
package ch.ge.afc.ofs.district.biz;

import java.util.Date;

import ch.ge.afc.ofs.Mutable;
import ch.ge.afc.ofs.canton.ICanton;
import ch.ge.afc.ofs.district.IDistrict;
import ch.ge.afc.ofs.district.TypeDistrict;
import ch.ge.afc.ofs.district.biz.dao.PersistDistrict;

public class District implements IDistrict {

	private PersistDistrict districtPersistant;
	ICanton canton;
	
	public District(PersistDistrict persitant) {
		districtPersistant = persitant;
	}
	
	/* (non-Javadoc)
	 * @see ch.ge.afc.ofs.Identifiable#getId()
	 */
	@Override
	public Long getId() {
		return districtPersistant.getId();
	}

	/* (non-Javadoc)
	 * @see ch.ge.afc.ofs.district.IDistrict#getCanton()
	 */
	@Override
	public ICanton getCanton() {
		return canton;
	}

	/* (non-Javadoc)
	 * @see ch.ge.afc.ofs.ILieuOFS#getNumeroOFS()
	 */
	@Override
	public int getNumeroOFS() {
		return districtPersistant.getNumero();
	}

	/* (non-Javadoc)
	 * @see ch.ge.afc.ofs.ILieuOFS#getNom()
	 */
	@Override
	public String getNom() {
		return districtPersistant.getNom();
	}

	/* (non-Javadoc)
	 * @see ch.ge.afc.ofs.district.IDistrict#getNomCourt()
	 */
	@Override
	public String getNomCourt() {
		return districtPersistant.getNomCourt();
	}

	/* (non-Javadoc)
	 * @see ch.ge.afc.ofs.district.IDistrict#getTypeEnregistrement()
	 */
	@Override
	public TypeDistrict getTypeEnregistrement() {
		return districtPersistant.getTypeEnregistrement();
	}

	/* (non-Javadoc)
	 * @see ch.ge.afc.ofs.district.IDistrict#getInscription()
	 */
	@Override
	public Mutable getInscription() {
		return districtPersistant.getInscription();
	}

	/* (non-Javadoc)
	 * @see ch.ge.afc.ofs.district.IDistrict#getRadiation()
	 */
	@Override
	public Mutable getRadiation() {
		return districtPersistant.getRadiation();
	}

	/* (non-Javadoc)
	 * @see ch.ge.afc.ofs.Changeable#getDernierChangement()
	 */
	@Override
	public Date getDernierChangement() {
		return districtPersistant.getDernierChangement();
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		districtPersistant.setId(id);
	}

	/**
	 * @param numero the numero to set
	 */
	public void setNumero(int numero) {
		districtPersistant.setNumero(numero);
	}

	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		districtPersistant.setNom(nom);
	}

	/**
	 * @param nomCourt the nomCourt to set
	 */
	public void setNomCourt(String nomCourt) {
		districtPersistant.setNomCourt(nomCourt);
	}

	/**
	 * @param canton the canton to set
	 */
	public void setCanton(ICanton canton) {
		this.canton = canton;
		districtPersistant.setIdCanton(canton.getId());
	}

	/**
	 * @param typeEnregistrement the typeEnregistrement to set
	 */
	public void setTypeEnregistrement(TypeDistrict typeEnregistrement) {
		districtPersistant.setTypeEnregistrement(typeEnregistrement);
	}

	/**
	 * @param inscription the inscription to set
	 */
	public void setInscription(Mutable inscription) {
		districtPersistant.setInscription(inscription);
	}

	/**
	 * @param radiation the radiation to set
	 */
	public void setRadiation(Mutable radiation) {
		districtPersistant.setRadiation(radiation);
	}

	/**
	 * @param dernierChangement the dernierChangement to set
	 */
	public void setDernierChangement(Date dernierChangement) {
		districtPersistant.setDernierChangement(dernierChangement);
	}

	
}
