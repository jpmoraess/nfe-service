package br.com.moraesit.nfeservice.utils;

import br.com.swconsultoria.nfe.util.ObjetoUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ArquivoUtil {

    /**
     * Transforma a string do XML em bytes compactado
     *
     * @param xml
     * @return
     * @throws IOException
     */
    public static byte[] compactaXml(String xml) throws IOException {
        if (ObjetoUtil.verifica(xml).isEmpty())
            return null;
        ByteArrayOutputStream obj = new ByteArrayOutputStream();
        try (OutputStream outputStream = new GZIPOutputStream(obj)) {
            outputStream.write(xml.getBytes(StandardCharsets.UTF_8));
        }
        return obj.toByteArray();
    }

    /**
     * Transforma os bytes do XML em String
     *
     * @param bytes
     * @return
     * @throws IOException
     */
    public static String descompactaXml(byte[] bytes) throws IOException {
        if (bytes == null || bytes.length == 0)
            return "";
        GZIPInputStream gzipInputStream;
        gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(bytes));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gzipInputStream, StandardCharsets.UTF_8));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }
}
