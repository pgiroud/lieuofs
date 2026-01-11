package org.lieuofs.extraction.commune.mutation;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Template;
import org.lieuofs.ILieuOFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.lieuofs.commune.ICommuneSuisse;
import org.lieuofs.commune.IMutationCommune;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;


public class MutCommuneFreemarkerRefonteSQLWriter implements
		MutationCommuneWriter {

	final Logger logger = LoggerFactory.getLogger(MutCommuneFreemarkerRefonteSQLWriter.class);

	private final Configuration configFreemarker;

	private final String nomScript;
	private final DateFormat fmtDtd = new SimpleDateFormat("yyyyMMdd");
	private final DateFormat fmtOracle = new SimpleDateFormat("dd.MM.yyyy");


	public MutCommuneFreemarkerRefonteSQLWriter(String nomScript) {
		super();
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_34);
		cfg.setClassForTemplateLoading(MutCommuneFreemarkerRefonteSQLWriter.class,"/freemarker/mutation/");
		cfg.setDefaultEncoding("UTF-8");
		this.configFreemarker = cfg;
		this.nomScript = nomScript;
	}

	private Map<Object, Object> creerFlux() {
		Map<Object, Object> flux = new HashMap<Object, Object>();
		flux.put("nomScript", nomScript);
		return flux;
	}

	private String compose(Template template, Object model)
			throws IOException, TemplateException {
		StringWriter result = new StringWriter(1024);
		template.process(model, result);
		return result.toString();
	}

	private void ecrireEvtModifCommune(StringBuilder builder, Date date, int noOFS, String description) throws IOException, TemplateException {
		Map<Object, Object> flux = creerFlux();
		flux.put("description", description.replaceAll("'", "''"));
		flux.put("date", fmtDtd.format(date));
		flux.put("noofs", String.valueOf(noOFS));
		builder.append(compose(configFreemarker.getTemplate("evtModification.ftl"), flux));
	}

	private void ecrireEvtCreationCommune(StringBuilder builder, Date date) throws IOException, TemplateException {
		Map<Object, Object> flux = creerFlux();
		flux.put("description", "Création");
		flux.put("date", fmtDtd.format(date));
		builder.append(compose(configFreemarker.getTemplate("evtCreation.ftl"), flux));
	}

	private void ecrireDesactivationLieuFiscal(StringBuilder builder, Date date, int noOFS, String remarques) throws IOException, TemplateException {
		Map<Object, Object> flux = creerFlux();
		flux.put("description", remarques.replaceAll("'", "''"));
		flux.put("date", fmtOracle.format(date));
		flux.put("noofs", String.valueOf(noOFS));
		builder.append(compose(configFreemarker.getTemplate("desactivationLieuFiscal.ftl"), flux));
	}

	private void ecrireEvtDesactivation(StringBuilder builder, Date date, int noOFS) throws IOException, TemplateException {
		ecrireEvtModifCommune(builder, date, noOFS, "Désactivation");
	}

	private void desactiverCommune(StringBuilder builder, Date date, ICommuneSuisse commune, IMutationCommune mutation) throws IOException, TemplateException {
		ecrireEvtDesactivation(builder, date, commune.getNumeroOFS());
		ecrireDesactivationLieuFiscal(builder, date, commune.getNumeroOFS(), mutation.getDescription());
	}

	private void ecrireLieuFiscalCree(StringBuilder builder, ILieuOFS lieu, Date dateCreation, String remarques) throws IOException, TemplateException {
		Map<Object, Object> flux = creerFlux();
		flux.put("description", remarques.replaceAll("'", "''"));
		flux.put("date", fmtOracle.format(dateCreation));
		flux.put("noofs", String.valueOf(lieu.getNumeroOFS()));
		flux.put("nom", lieu.getNom().replaceAll("'", "''"));
		builder.append(compose(configFreemarker.getTemplate("creationLieuFiscal.ftl"), flux));
	}

	private void ecrireCommuneCree(StringBuilder builder, ICommuneSuisse commune) throws IOException, TemplateException {
		Map<Object, Object> flux = creerFlux();
		flux.put("codeiso2canton", commune.getCanton().getCodeIso2());
		builder.append(compose(configFreemarker.getTemplate("creationCommune.ftl"), flux));
	}

	private String ecrireFusion(IMutationCommune mutation) throws IOException, TemplateException {
		Calendar cal = Calendar.getInstance();
		StringBuilder builder = new StringBuilder();
		builder.append(getBandeau(mutation.getDescription()));
		// Communes fusionnées
		cal.setTime(mutation.getDateEffet());
		cal.add(Calendar.DATE, -1);
		for (ICommuneSuisse commune : mutation.getCommunesOrigines()) {
			desactiverCommune(builder, cal.getTime(), commune, mutation);
		}
		// Commune née de la fusion
		cal.setTime(mutation.getDateEffet());
		ICommuneSuisse communeCree = mutation.getCommunesCibles().get(0);
		ecrireLieuFiscalCree(builder, communeCree, cal.getTime(), mutation.getDescription());
		ecrireCommuneCree(builder, communeCree);
		ecrireEvtCreationCommune(builder, cal.getTime());
		return builder.toString();
	}

	private void ecrireAbsorption(StringBuilder builder, Date date, int noOFS, List<ICommuneSuisse> communeAbsorbee) throws IOException, TemplateException {
		StringBuilder description = new StringBuilder("Absorption ");
		boolean premier = true;
		for (ICommuneSuisse commune : communeAbsorbee) {
			if (!premier) description.append(", ");
			else {
				premier = false;
			}
			description.append(commune.getNom().replaceAll("'", "''")).append(" (").append(commune.getNumeroOFS()).append(")");
		}
		ecrireEvtModifCommune(builder, date, noOFS, description.toString());
	}

	private String ecrireInclusion(IMutationCommune mutation) throws IOException, TemplateException {
		Calendar cal = Calendar.getInstance();
		StringBuilder builder = new StringBuilder();
		builder.append(getBandeau(mutation.getDescription()));
		ICommuneSuisse communeAbsorbante = mutation.getCommunesCibles().get(0);
		// Communes incluses
		cal.setTime(mutation.getDateEffet());
		cal.add(Calendar.DATE, -1);
		for (ICommuneSuisse commune : mutation.getCommunesOrigines()) {
			// Attention, on est obligé de faire ce test car la notion de commune
			// à l'OFS est une notion avec historique !
			if (commune.getNumeroOFS() != communeAbsorbante.getNumeroOFS()) {
				desactiverCommune(builder, cal.getTime(), commune, mutation);
			}
		}
		// Commune absorbante
		cal.setTime(mutation.getDateEffet());
		ecrireAbsorption(builder, cal.getTime(), communeAbsorbante.getNumeroOFS(), mutation.getCommunesOrigines());
		return builder.toString();
	}

	private String getBandeau(String libelle) {
		StringBuilder builder = new StringBuilder();
		String demarque = "------------------------------------------------------------------------";
		builder.append(demarque).append("\n");
		builder.append("-- ");
		builder.append(libelle);
		builder.append("\n").append(demarque).append("\n\n");
		return builder.toString();
	}


	private String ecrireChgtNom(IMutationCommune mutation) throws IOException, TemplateException {
		Calendar cal = Calendar.getInstance();
		StringBuilder builder = new StringBuilder();
		builder.append(getBandeau(mutation.getDescription()));
		cal.setTime(mutation.getDateEffet());
		ICommuneSuisse commune = mutation.getCommunesCibles().get(0);
		ecrireEvtModifCommune(builder, cal.getTime(), commune.getNumeroOFS(), mutation.getDescription());

		Map<Object, Object> flux = creerFlux();
		flux.put("noofs", String.valueOf(commune.getNumeroOFS()));
		flux.put("nouveaunom", commune.getNom().replaceAll("'", "''"));
		builder.append(compose(configFreemarker.getTemplate("chgtNomLieuFiscal.ftl"), flux));
		return builder.toString();
	}


	@Override
	public String ecrireMutation(IMutationCommune mutation) {
		try {
			return switch (mutation.getType()) {
				case FUSION -> ecrireFusion(mutation);
				case INCLUSION -> ecrireInclusion(mutation);
				case CHANGEMENT_NOM -> ecrireChgtNom(mutation);
				case CHANGEMENT_DISTRICT -> null;
				case ECHANGE_TERRITOIRE -> null;
				default -> null;
			};
		} catch (TemplateException | IOException ex) {
			logger.error("Impossible d’écrire la mutation " + mutation,ex);
			throw new RuntimeException(ex);
		}
    }
}
