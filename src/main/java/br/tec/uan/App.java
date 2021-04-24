package br.tec.uan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * mvn archetype:generate -DarchetypeArtifactId=maven-archetype-quickstart
 */
public class App
{
    public String getText(File pdfFile) throws FileNotFoundException, IOException {
        PDFTextStripper pdfStripper = null;
        PDDocument pdDoc = null;
        COSDocument cosDoc = null;

        try {
            PDFParser parser = new PDFParser(new RandomAccessFile(pdfFile, "r"));
            parser.parse();
            cosDoc = parser.getDocument();

            pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);

            return pdfStripper.getText(pdDoc);
        }
        finally {
            try {
                if (cosDoc != null) {
                    cosDoc.close();
                }
                if (pdDoc != null) {
                    pdDoc.close();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        if (args.length != 1) {
            System.err.println("Informe o caminho completo para o PDF");
            System.exit(9);
        }

        File pdfFile = new File(args[0]);
        if (!pdfFile.exists()) {
            System.err.println("O PDF informado não existe");
            System.exit(9);
        }

        File outputPdfFile = new File(pdfFile.getParentFile(), pdfFile.getName() + ".txt");
        // File pdfFile = new
        // File("/Users/jesussaad/Downloads/DocumentosAgrupados-2/1f91145f-2840-4345-bff9-51b4ad68203f.pdf");

        System.out.println("Extraindo o texto do PDF...");
        String text = new App().getText(pdfFile);
        Files.write(outputPdfFile.toPath(), text.getBytes());
        System.out.println("Texto extraído para o arquivo " + outputPdfFile.getPath());
    }
}
