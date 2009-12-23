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
package ch.ge.afc.ofs.district.biz.dao;

import java.util.Date;

import ch.ge.afc.ofs.Mutable;
import ch.ge.afc.ofs.district.TypeDistrict;

/**
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public class PersistDistrict {
	
	private Long id;
	private int numero;
	private String nom;
	private String nomCourt;
	private Long idCanton;
	private TypeDistrict typeEnregistrement;
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
	 * @return the idCanton
	 */
	public Long getIdCanton() {
		return idCanton;
	}
	/**
	 * @param idCanton the idCanton to set
	 */
	public void setIdCanton(Long idCanton) {
		this.idCanton = idCanton;
	}
	/**
	 * @return the typeEnregistrement
	 */
	public TypeDistrict getTypeEnregistrement() {
		return typeEnregistrement;
	}

	
	
	/**
	 * @param typeEnregistrementId the typeEnregistrementId to set
	 */
	public void setTypeEnregistrement(TypeDistrict typeEnregistrement) {
		this.typeEnregistrement = typeEnregistrement;
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
