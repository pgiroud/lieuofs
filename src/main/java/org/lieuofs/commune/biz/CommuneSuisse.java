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

import java.util.Date;

import org.lieuofs.Mutable;
import org.lieuofs.canton.ICanton;
import org.lieuofs.commune.ICommuneSuisse;
import org.lieuofs.commune.TypeCommune;
import org.lieuofs.commune.biz.dao.PersistCommune;
import org.lieuofs.district.IDistrict;

public class CommuneSuisse implements ICommuneSuisse {
	private PersistCommune persistant;
	private ICanton canton;
	private IDistrict district;

	
	public CommuneSuisse(PersistCommune persistant) {
		super();
		this.persistant = persistant;
	}
	/**
	 * @return
	 * @see org.lieuofs.commune.biz.dao.PersistCommune#getDernierChangement()
	 */
	@Override
	public Date getDernierChangement() {
		return persistant.getDernierChangement();
	}
	/**
	 * @return
	 * @see org.lieuofs.commune.biz.dao.PersistCommune#getId()
	 */
	@Override
	public Long getId() {
		return persistant.getId();
	}
	/**
	 * @return
	 * @see org.lieuofs.commune.biz.dao.PersistCommune#getInscription()
	 */
	@Override
	public Mutable getInscription() {
		return persistant.getInscription();
	}
	/**
	 * @return
	 * @see org.lieuofs.commune.biz.dao.PersistCommune#getNom()
	 */
	@Override
	public String getNom() {
		return persistant.getNom();
	}
	/**
	 * @return
	 * @see org.lieuofs.commune.biz.dao.PersistCommune#getNomCourt()
	 */
	@Override
	public String getNomCourt() {
		return persistant.getNomCourt();
	}
	/**
	 * @return
	 * @see org.lieuofs.commune.biz.dao.PersistCommune#getNumero()
	 */
	@Override
	public int getNumeroOFS() {
		return persistant.getNumero();
	}
	/**
	 * @return
	 * @see org.lieuofs.commune.biz.dao.PersistCommune#getRadiation()
	 */
	@Override
	public Mutable getRadiation() {
		return persistant.getRadiation();
	}
	/**
	 * @return
	 * @see org.lieuofs.commune.biz.dao.PersistCommune#getTypeEnregistrement()
	 */
	@Override
	public TypeCommune getTypeEnregistrement() {
		return persistant.getTypeEnregistrement();
	}
	/**
	 * @return
	 * @see org.lieuofs.commune.biz.dao.PersistCommune#isProvisoire()
	 */
	@Override
	public boolean isProvisoire() {
		return persistant.isProvisoire();
	}
	/**
	 * @param dernierChangement
	 * @see org.lieuofs.commune.biz.dao.PersistCommune#setDernierChangement(java.util.Date)
	 */
	public void setDernierChangement(Date dernierChangement) {
		persistant.setDernierChangement(dernierChangement);
	}
	/**
	 * @param id
	 * @see org.lieuofs.commune.biz.dao.PersistCommune#setId(java.lang.Long)
	 */
	public void setId(Long id) {
		persistant.setId(id);
	}
	/**
	 * @param inscription
	 * @see org.lieuofs.commune.biz.dao.PersistCommune#setInscription(org.lieuofs.Mutable)
	 */
	public void setInscription(Mutable inscription) {
		persistant.setInscription(inscription);
	}
	/**
	 * @param nom
	 * @see org.lieuofs.commune.biz.dao.PersistCommune#setNom(java.lang.String)
	 */
	public void setNom(String nom) {
		persistant.setNom(nom);
	}
	/**
	 * @param nomCourt
	 * @see org.lieuofs.commune.biz.dao.PersistCommune#setNomCourt(java.lang.String)
	 */
	public void setNomCourt(String nomCourt) {
		persistant.setNomCourt(nomCourt);
	}
	/**
	 * @param numero
	 * @see org.lieuofs.commune.biz.dao.PersistCommune#setNumero(int)
	 */
	public void setNumero(int numero) {
		persistant.setNumero(numero);
	}
	/**
	 * @param provisoire
	 * @see org.lieuofs.commune.biz.dao.PersistCommune#setProvisoire(boolean)
	 */
	public void setProvisoire(boolean provisoire) {
		persistant.setProvisoire(provisoire);
	}
	/**
	 * @param radiation
	 * @see org.lieuofs.commune.biz.dao.PersistCommune#setRadiation(org.lieuofs.Mutable)
	 */
	public void setRadiation(Mutable radiation) {
		persistant.setRadiation(radiation);
	}
	/**
	 * @param typeEnregistrement
	 * @see org.lieuofs.commune.biz.dao.PersistCommune#setTypeEnregistrement(org.lieuofs.commune.TypeCommune)
	 */
	public void setTypeEnregistrement(TypeCommune typeEnregistrement) {
		persistant.setTypeEnregistrement(typeEnregistrement);
	}
	/**
	 * @return the canton
	 */
	@Override
	public ICanton getCanton() {
		return canton;
	}
	/**
	 * @param canton the canton to set
	 */
	public void setCanton(ICanton canton) {
		this.canton = canton;
	}
	/**
	 * @return the district
	 */
	@Override
	public IDistrict getDistrict() {
		return district;
	}
	/**
	 * @param district the district to set
	 */
	public void setDistrict(IDistrict district) {
		this.district = district;
	}

    @Override
    public int hashCode() {
        long value = this.getId();
        return (int)(value ^ (value >>> 32));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CommuneSuisse)) return false;
        return this.getId().equals(((CommuneSuisse) obj).getId());
    }
}
