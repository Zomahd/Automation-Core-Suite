package com.automation.core.suite.utils;

import com.automation.core.suite.configuration.AutomationCoreConfigWrapper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FileUtil {

    public static void main(String[] args) throws IOException {
        //closeProcess();
        copyCustomProfile();
    }

    private static void copyCustomProfile() {

        //Delete current profile
        boolean deleted = false;
        File destination = new File(AutomationCoreConfigWrapper.INSTANCE.get("chrome.profile.destination"));
        try {
            FileUtils.deleteDirectory(destination);
            deleted = true;
        } catch (IOException e) {
            System.err.println("Error deleting current custom profile.");
            e.printStackTrace();
        }

        boolean copied = false;
        if (deleted) {

            //Copy the custom file again
            destination = new File(AutomationCoreConfigWrapper.INSTANCE.get("chrome.profile.destination"));
            destination.mkdirs();

            String source = AutomationCoreConfigWrapper.INSTANCE.get("chrome.profile.location", "");
            try {
                if (!source.isEmpty()) {
                    FileUtils.copyDirectoryToDirectory(new File(source), destination);
                    copied = true;
                } else {
                    System.err.println("Source or Destination are empty values");
                }
            } catch (IOException e) {
                System.err.println("Error copying the folders");
                e.printStackTrace();
            }

        }

        if (copied) {
            System.out.println("Custom Profile copied correctly.");
        }

    }
}
