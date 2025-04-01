package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.*;

public class JOOQCodeGenerator {
    public static void main(String[] args) throws Exception {
        Dotenv dotenv = Dotenv.configure().load();

        Database database = new Database()
                .withName("org.jooq.meta.postgres.PostgresDatabase")
                .withIncludes(".*")
                .withExcludes("")
                .withInputSchema("public")
                .withOutputSchemaToDefault(true);

        Target target = new Target()
                .withPackageName("org.example.generated.jooq")
                .withDirectory("src/main/java");

        Configuration configuration = new Configuration()
                .withJdbc(new Jdbc()
                        .withDriver("org.postgresql.Driver")
                        .withUrl(dotenv.get("DATABASE_URL"))
                        .withUser(dotenv.get("DATABASE_USERNAME"))
                        .withPassword(dotenv.get("DATABASE_PASSWORD")))
                .withGenerator(new Generator()
                        .withName("org.jooq.codegen.JavaGenerator")
                        .withDatabase(database)
                        .withTarget(target));

        GenerationTool.generate(configuration);
    }
}
