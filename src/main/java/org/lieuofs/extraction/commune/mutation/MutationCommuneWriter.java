package org.lieuofs.extraction.commune.mutation;

import freemarker.template.TemplateException;
import org.lieuofs.commune.IMutationCommune;

import java.io.IOException;

interface MutationCommuneWriter {
	String ecrireMutation(IMutationCommune mutation);
}
