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

import java.util.Date;

import org.lieuofs.Mutable;
import org.lieuofs.commune.TypeCommune;

public class PersistCommune {
	private Long id;
	private Long districtId;
	private int numero;
	private String nom;
	private String nomCourt;
	private String codeCanton;
	private TypeCommune typeEnregistrement;
	boolean provisoire;
	private Mutable inscription;
	private Mutable radiation;
	private Date dernierChangement;
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	
	/**
	 * @return the districtId
	 */
	public Long getDistrictId() {
		return districtId;
	}
	/**
	 * @param districtId the districtId to set
	 */
	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}
	/**
	 * @return the numero
	 */
	public int getNumero() {
		return numero;
	}
	/**
	 * @param numero the numero to set
	 */
	public void setNumero(int numero) {
		this.numero = numero;
	}
	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}
	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}
	/**
	 * @return the nomCourt
	 */
	public String getNomCourt() {
		return nomCourt;
	}
	/**
	 * @param nomCourt the nomCourt to set
	 */
	public void setNomCourt(String nomCourt) {
		this.nomCourt = nomCourt;
	}
	/**
	 * @return the codeCanton
	 */
	public String getCodeCanton() {
		return codeCanton;
	}
	/**
	 * @param codeCanton the codeCanton to set
	 */
	public void setCodeCanton(String codeCanton) {
		this.codeCanton = codeCanton;
	}
	/**
	 * @return the typeEnregistrement
	 */
	public TypeCommune getTypeEnregistrement() {
		return typeEnregistrement;
	}
	/**
	 * @param typeEnregistrement the typeEnregistrement to set
	 */
	public void setTypeEnregistrement(TypeCommune typeEnregistrement) {
		this.typeEnregistrement = typeEnregistrement;
	}
	/**
	 * @return the provisoire
	 */
	public boolean isProvisoire() {
		return provisoire;
	}
	/**
	 * @param provisoire the provisoire to set
	 */
	public void setProvisoire(boolean provisoire) {
		this.provisoire = provisoire;
	}
	/**
	 * @return the inscription
	 */
	public Mutable getInscription() {
		return inscription;
	}
	/**
	 * @param inscription the inscription to set
	 */
	public void setInscription(Mutable inscription) {
		this.inscription = inscription;
	}
	/**
	 * @return the radiation
	 */
	public Mutable getRadiation() {
		return radiation;
	}
	/**
	 * @param radiation the radiation to set
	 */
	public void setRadiation(Mutable radiation) {
		this.radiation = radiation;
	}
	/**
	 * @return the dernierChangement
	 */
	public Date getDernierChangement() {
		return dernierChangement;
	}
	/**
	 * @param dernierChangement the dernierChangement to set
	 */
	public void setDernierChangement(Date dernierChangement) {
		this.dernierChangement = dernierChangement;
	}
	
	

}
