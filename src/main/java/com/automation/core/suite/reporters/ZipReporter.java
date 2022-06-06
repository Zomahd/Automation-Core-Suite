package com.automation.core.suite.reporters;

import com.automation.core.suite.utils.AutomationCoreErrorUtils;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipReporter implements IReporter {

    private final static String ZIP_EXTENSION = ".zip";

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {

        String finalOutputDirectory =
                outputDirectory.substring(outputDirectory.length() - 1).equals(File.separator)
                        ? outputDirectory.substring(0, outputDirectory.length() - 1)
                        : outputDirectory;


        String zipFinalName = finalOutputDirectory + ZIP_EXTENSION;

        zip(finalOutputDirectory, zipFinalName);
    }

    public void zip4j(String outputDirectory, String zipFinalName) {

        try {
            ZipFile zipFile = new ZipFile(zipFinalName);

            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

            zipFile.createZipFile(new File(outputDirectory), parameters);

        } catch (Exception e) {
            AutomationCoreErrorUtils.log("ZipReporter-> generateReport:: Exception: " + outputDirectory, e);
        }
    }

    public void zip(String outputDirectory, String zipFinalName) {

        ZipOutputStream zipOutputStream = null;
        FileOutputStream fileOutputStream = null;

        try {

            fileOutputStream = new FileOutputStream(zipFinalName);
            zipOutputStream = new ZipOutputStream(fileOutputStream);
            File inputFile = new File(outputDirectory);

            if (inputFile.isFile()) {
                zipFile(inputFile, zipOutputStream, "");
            } else if (inputFile.isDirectory()) {
                zipFolder(inputFile, zipOutputStream, "");
            }

            zipOutputStream.close();
            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            AutomationCoreErrorUtils.log("ZipReporter-> generateReport:: FileNotFoundException: " + outputDirectory, e);
        } catch (IOException e) {
            AutomationCoreErrorUtils.log("ZipReporter-> generateReport:: IOException: " + outputDirectory, e);
        }
    }


    protected void zipFolder(File inputFolder, ZipOutputStream zipOutputStream, String parentName) throws IOException {

        String zipEntryName = parentName + inputFolder.getName() + File.separator;
        ZipEntry folderZipEntry = new ZipEntry(zipEntryName);
        zipOutputStream.putNextEntry(folderZipEntry);

        File[] contents = inputFolder.listFiles();

        for (File f : contents) {
            if (f.isFile())
                zipFile(f, zipOutputStream, zipEntryName);
            else if (f.isDirectory())
                zipFolder(f, zipOutputStream, zipEntryName);
        }
        zipOutputStream.closeEntry();
    }

    protected void zipFile(File inputFile, ZipOutputStream zipOutputStream, String parentName) throws IOException {

        ZipEntry zipEntry = new ZipEntry(parentName + inputFile.getName());
        zipOutputStream.putNextEntry(zipEntry);

        FileInputStream fileInputStream = new FileInputStream(inputFile);
        byte[] buf = new byte[1024];
        int bytesRead;

        while ((bytesRead = fileInputStream.read(buf)) > 0) {
            zipOutputStream.write(buf, 0, bytesRead);
        }

        zipOutputStream.closeEntry();
        fileInputStream.close();
    }

}
