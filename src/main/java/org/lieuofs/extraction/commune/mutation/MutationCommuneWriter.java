package org.lieuofs.extraction.commune.mutation;

import org.lieuofs.commune.IMutationCommune;

interface MutationCommuneWriter {
	String ecrireMutation(IMutationCommune mutation);
}
