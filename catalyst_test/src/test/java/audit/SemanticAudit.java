package audit;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;

public class SemanticAudit {

    public static void verifyCoverage(Set<String> usedKeys) {
        try (InputStream is = SemanticAudit.class.getClassLoader().getResourceAsStream("Index.json")) {
            if (is == null) {
                throw new RuntimeException("Index.json not found in classpath");
            }

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Map<String, String>> indexMap = mapper.readValue(is, new TypeReference<>() {});

            for (String key : indexMap.keySet()) {
                if (!usedKeys.contains(key)) {
                    throw new RuntimeException("Semantic key not exercised in feature file: " + key);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to audit semantic coverage", e);
        }
    }
}