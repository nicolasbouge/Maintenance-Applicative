package fr.unilim.saes5.persistence.glossary;

import fr.unilim.saes5.model.Glossary;

import java.util.List;

public interface GlossaryDao {

    void saveGlossary(Glossary project);

    List<Glossary> getAllGlossary();
}
