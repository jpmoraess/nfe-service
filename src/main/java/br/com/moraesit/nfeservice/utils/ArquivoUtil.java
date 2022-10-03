package br.com.moraesit.nfeservice.utils;

import br.com.swconsultoria.nfe.util.ObjetoUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPOutputStream;

public class ArquivoUtil {

    /**
     * Transforma a string do XML em bytes compactado
     * @param xml
     * @return
     * @throws IOException
     */
    public static byte[] compactaXml(String xml) throws IOException {
        if (!ObjetoUtil.verifica(xml).isPresent())
            return null;
        ByteArrayOutputStream obj = new ByteArrayOutputStream();
        try (OutputStream outputStream = new GZIPOutputStream(obj)) {
            outputStream.write(xml.getBytes(StandardCharsets.UTF_8));
        }
        return obj.toByteArray();
    }
}
