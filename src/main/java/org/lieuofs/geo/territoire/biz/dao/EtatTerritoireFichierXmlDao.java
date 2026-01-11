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
package org.lieuofs.geo.territoire.biz.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.lieuofs.geo.territoire.biz.EtatTerritoireCritere;
import org.lieuofs.util.InfosONUetISO3166;
import org.lieuofs.util.dao.FichierOFSTxtDao;

/**
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public class EtatTerritoireFichierXmlDao implements EtatTerritoireDao {

	/**************************************************/
    /****************** Attributs *********************/
    /**************************************************/

	final Logger logger = LoggerFactory.getLogger(FichierOFSTxtDao.class);
	private final String fichierDansClasspathAvecCheminComplet;
	private final String charsetName;
	private DateFormat dateFmt;
	
	private Set<EtatTerritoirePersistant> tous = new HashSet<EtatTerritoirePersistant>(290);
	private Set<EtatTerritoirePersistant> etats = new HashSet<EtatTerritoirePersistant>(290);
	private Map<Integer,EtatTerritoirePersistant> mapParNum = new HashMap<Integer,EtatTerritoirePersistant>();
	
    /**************************************************/
    /**************** Constructeurs *******************/
    /**************************************************/

	public EtatTerritoireFichierXmlDao(String fichierAvecCheminComplet) {
		super();
		this.fichierDansClasspathAvecCheminComplet = fichierAvecCheminComplet;
		charsetName = "UTF-8";
		dateFmt = new SimpleDateFormat("yyyy-MM-dd");
		chargerResource();
	}

	
	/**************************************************/
    /******************* Méthodes *********************/
    /**************************************************/

	private ClassLoader getClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		}
		catch (Throwable ex) {}
		if (cl == null) {
			cl = FichierOFSTxtDao.class.getClassLoader();
			if (cl == null) {
				try {
					cl = ClassLoader.getSystemClassLoader();
				}
				catch (Throwable ex) {
				}
			}
		}
		return cl;
	}

	boolean exist() {
		try {
			ClassLoader cl = getClassLoader();
			InputStream is = (cl != null ? cl.getResourceAsStream(fichierDansClasspathAvecCheminComplet) : ClassLoader.getSystemResourceAsStream(fichierDansClasspathAvecCheminComplet));
			return null != is && new BufferedReader(new InputStreamReader(is,charsetName)).ready();
		} catch (IOException ioe) {
			logger.debug("Pas de lecture possible dans 'classpath:" + fichierDansClasspathAvecCheminComplet + "'",ioe);
		}
		return false;
	}

	private InputStream getInputStream() {
		ClassLoader cl = getClassLoader();
		URL resource = cl.getResource(fichierDansClasspathAvecCheminComplet);
		InputStream stream = (cl != null ? cl.getResourceAsStream(fichierDansClasspathAvecCheminComplet) : ClassLoader.getSystemResourceAsStream(fichierDansClasspathAvecCheminComplet));
		return stream;
	}


	private void chargerResource() {
		try (InputStream flux = getInputStream()) {
			XMLEventReader reader = XMLInputFactory.newInstance().createXMLEventReader(flux, this.charsetName);
			EtatTerritoirePersistant etatTerritoire = null;
			while (reader.hasNext()) {
				XMLEvent event = (XMLEvent) reader.next();
				if (event.isStartElement()) {
					StartElement element = (StartElement) event;
					String nomElem = element.getName().toString();
					if ("country".equals(nomElem)) {
						etatTerritoire = new EtatTerritoirePersistant();
					} else if ("id".equals(nomElem)) {
						Characters characters = (Characters) reader.next();
						etatTerritoire.setNumeroOFS(Integer.parseInt(characters.getData()));
					} else if ("unId".equals(nomElem)) {
						Characters characters = (Characters) reader.next();
						if (null == etatTerritoire.getInfosISO()) etatTerritoire.setInfosISO(new InfosONUetISO3166());
						etatTerritoire.getInfosISO().setCodeNumeriqueONU(Integer.parseInt(characters.getData()));
					} else if ("iso2Id".equals(nomElem)) {
						Characters characters = (Characters) reader.next();
						if (null == etatTerritoire.getInfosISO()) etatTerritoire.setInfosISO(new InfosONUetISO3166());
						etatTerritoire.getInfosISO().setCodeIsoAlpha2(characters.getData());
					} else if ("iso3Id".equals(nomElem)) {
						Characters characters = (Characters) reader.next();
						if (null == etatTerritoire.getInfosISO()) etatTerritoire.setInfosISO(new InfosONUetISO3166());
						etatTerritoire.getInfosISO().setCodeIsoAlpha3(characters.getData());
					} else if ("shortNameDe".equals(nomElem)) {
						Characters characters = (Characters) reader.next();
						etatTerritoire.ajouterFormeCourte("de", characters.getData());
					} else if ("shortNameFr".equals(nomElem)) {
						Characters characters = (Characters) reader.next();
						etatTerritoire.ajouterFormeCourte("fr", characters.getData());
					} else if ("shortNameIt".equals(nomElem)) {
						Characters characters = (Characters) reader.next();
						etatTerritoire.ajouterFormeCourte("it", characters.getData());
					} else if ("shortNameEn".equals(nomElem)) {
						Characters characters = (Characters) reader.next();
						etatTerritoire.ajouterFormeCourte("en", characters.getData());
					} else if ("officialNameDe".equals(nomElem)) {
						Characters characters = (Characters) reader.next();
						etatTerritoire.ajouterDesignationOfficielle("de", characters.getData());
					} else if ("officialNameFr".equals(nomElem)) {
						Characters characters = (Characters) reader.next();
						etatTerritoire.ajouterDesignationOfficielle("fr", characters.getData());
					} else if ("officialNameIt".equals(nomElem)) {
						Characters characters = (Characters) reader.next();
						etatTerritoire.ajouterDesignationOfficielle("it", characters.getData());
					} else if ("continent".equals(nomElem)) {
						Characters characters = (Characters) reader.next();
						etatTerritoire.setNumContinent(Integer.parseInt(characters.getData()));
					} else if ("region".equals(nomElem)) {
						Characters characters = (Characters) reader.next();
						etatTerritoire.setNumRegion(Integer.parseInt(characters.getData()));
					} else if ("state".equals(nomElem)) {
						Characters characters = (Characters) reader.next();
						etatTerritoire.setEtat(Boolean.valueOf(characters.getData()));
					} else if ("areaState".equals(nomElem)) {
						Characters characters = (Characters) reader.next();
						etatTerritoire.setNumEtatRattachement(Integer.parseInt(characters.getData()));
					} else if ("unMember".equals(nomElem)) {
						Characters characters = (Characters) reader.next();
						etatTerritoire.setMembreONU(Boolean.valueOf(characters.getData()));
					} else if ("unEntryDate".equals(nomElem)) {
						Characters characters = (Characters) reader.next();
						etatTerritoire.setDateEntreeONU(dateFmt.parse(characters.getData()));
					} else if ("recognizedCh".equals(nomElem)) {
						Characters characters = (Characters) reader.next();
						etatTerritoire.setReconnuSuisse(Boolean.valueOf(characters.getData()));
					} else if ("recognizedDate".equals(nomElem)) {
						Characters characters = (Characters) reader.next();
						etatTerritoire.setDateReconnaissance(dateFmt.parse(characters.getData()));
					} else if ("remarkDe".equals(nomElem)) {
						Characters characters = (Characters) reader.next();
						etatTerritoire.ajouterRemarque("de", characters.getData());
					} else if ("remarkFr".equals(nomElem)) {
						Characters characters = (Characters) reader.next();
						etatTerritoire.ajouterRemarque("fr", characters.getData());
					} else if ("remarkIt".equals(nomElem)) {
						Characters characters = (Characters) reader.next();
						etatTerritoire.ajouterRemarque("it", characters.getData());
					} else if ("entryValid".equals(nomElem)) {
						Characters characters = (Characters) reader.next();
						etatTerritoire.setValide(Boolean.valueOf(characters.getData()));
					} else if ("dateOfChange".equals(nomElem)) {
						Characters characters = (Characters) reader.next();
						etatTerritoire.setDateDernierChangement(dateFmt.parse(characters.getData()));
					}
				} else if (event.isEndElement()) {
					EndElement element = (EndElement) event;
					if ("country".equals(element.getName().toString())) {
						stockerEtatTerritoire(etatTerritoire);
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void stockerEtatTerritoire(EtatTerritoirePersistant etatTerritoire) {
		mapParNum.put(etatTerritoire.getNumeroOFS(), etatTerritoire);
		tous.add(etatTerritoire);
		if (etatTerritoire.isEtat()) etats.add(etatTerritoire);
	}
	
	
	@Override
	public EtatTerritoirePersistant lire(int numeroOFS) {
		return mapParNum.get(numeroOFS);
	}

	private boolean accept(EtatTerritoirePersistant etatTerr, EtatTerritoireCritere critere) {
		if (null != critere.getNoOFSEtatRattachement()) {
			if (etatTerr.getNumEtatRattachement() != critere.getNoOFSEtatRattachement()) return false;
		}
		if (null != critere.getValide()) {
			if (etatTerr.isValide() != critere.getValide().booleanValue()) return false;
		}
		return true;
	}
	
	
	@Override
	public Set<EtatTerritoirePersistant> rechercher(EtatTerritoireCritere critere) {
		Set<EtatTerritoirePersistant> ensemble = new HashSet<EtatTerritoirePersistant>(tous);
		if (Boolean.TRUE.equals(critere.getEstEtat())) ensemble = etats;
		if (Boolean.FALSE.equals(critere.getEstEtat())) ensemble.removeAll(etats);
		Set<EtatTerritoirePersistant> retour = new HashSet<EtatTerritoirePersistant>();
		for (EtatTerritoirePersistant etatTerr : ensemble) {
			if (accept(etatTerr,critere)) retour.add(etatTerr);
		}
		return retour;
	}

	
}
