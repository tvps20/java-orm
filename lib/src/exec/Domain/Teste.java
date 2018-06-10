package exec.Domain;

import orm.Attributes.ForeingKey;
import orm.Attributes.Required;
import orm.Attributes.Varchar;

public class Teste extends Entity {

    @Varchar
    @Required
    private String name;

    @ForeingKey(table = "Projeto")
    private int projetoId;
}
