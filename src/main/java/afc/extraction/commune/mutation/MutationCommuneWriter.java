package afc.extraction.commune.mutation;

import ch.ge.afc.ofs.commune.IMutationCommune;

interface MutationCommuneWriter {
	String ecrireMutation(IMutationCommune mutation);
}
