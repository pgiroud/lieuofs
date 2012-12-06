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
package org.lieuofs.canton.biz;

import java.util.Date;

import org.lieuofs.canton.ICanton;

public class Canton implements ICanton {

	private Long id;
	private String codeISO2;
	private String nom;
	private Date dernierChangement;
	
	public Canton() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.lieuofs.canton.ICanton#getCodeIso2()
	 */
	@Override
	public String getCodeIso2() {
		return codeISO2;
	}

	/* (non-Javadoc)
	 * @see ch.ge.afc.ofs.OFSChangeable#getDernierChangement()
	 */
	@Override
	public Date getDernierChangement() {
		return new Date(dernierChangement.getTime());
	}

	/* (non-Javadoc)
	 * @see org.lieuofs.ILieuOFS#getNom()
	 */
	@Override
	public String getNom() {
		return nom;
	}

	/* (non-Javadoc)
	 * @see org.lieuofs.ILieuOFS#getNumeroOFS()
	 */
	@Override
	public int getNumeroOFS() {
		return (int) (9000 + id);
	}

	/* (non-Javadoc)
	 * @see org.lieuofs.canton.ICanton#getId()
	 */
	@Override
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
	 * @param codeISO2 the codeISO2 to set
	 */
	public void setCodeISO2(String codeISO2) {
		this.codeISO2 = codeISO2;
	}

	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * @param dernierChangement the dernierChangement to set
	 */
	public void setDernierChangement(Date dernierChangement) {
		this.dernierChangement = dernierChangement;
	}
	
	
}
