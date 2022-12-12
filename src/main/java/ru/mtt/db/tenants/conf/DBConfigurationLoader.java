package ru.mtt.db.tenants.conf;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import ru.mtt.loader.ResourceLoader;

import javax.validation.constraints.NotNull;
import java.util.HashMap;

public class DBConfigurationLoader {


    @SneakyThrows
    public static @NotNull HashMap<String, DBConfiguration> load(String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        DBConfiguration[] dbParams = mapper.readValue(ResourceLoader.readFile(fileName), DBConfiguration[].class);

        HashMap<String, DBConfiguration> mappedParams = new HashMap<>();
        for (DBConfiguration cfg : dbParams) {
            mappedParams.put(cfg.getName(), cfg);
        }

        if (mappedParams.get("main") == null) {
            throw new IllegalArgumentException("Please, specify primary database with name 'main'");
        }

        return mappedParams;
    }
}
