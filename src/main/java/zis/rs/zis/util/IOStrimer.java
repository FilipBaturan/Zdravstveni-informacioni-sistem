package zis.rs.zis.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class IOStrimer {


    /**
     * @param putanja do fajla za citanje
     * @return sadrzaj fajla kao string
     * @throws IOException fajl ne postoji na zadatoj putanji ili nema pravo pristupa
     */
    protected String ucitajSadrzajFajla(String putanja) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(putanja));
        return new String(encoded, StandardCharsets.UTF_8);
    }
}
