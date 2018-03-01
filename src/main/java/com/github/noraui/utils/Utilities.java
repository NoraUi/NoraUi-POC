/**
 * NoraUi is licensed under the license GNU AFFERO GENERAL PUBLIC LICENSE
 * 
 * @author Nicolas HALLOUIN
 * @author Stéphane GRILLON
 */
package com.github.noraui.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utilities {

    /**
     * Specific logger
     */
    private static final Logger logger = LoggerFactory.getLogger(Utilities.class);

    public enum OperatingSystem {

        WINDOWS("windows", "windows", ".exe"), LINUX("linux", "linux", ""), MAC("mac", "mac", "");

        private final String operatingSystemName;
        private final String operatingSystemDir;
        private final String suffixBinary;

        OperatingSystem(String operatingSystemName, String operatingSystemDir, String suffixBinary) {
            this.operatingSystemName = operatingSystemName;
            this.operatingSystemDir = operatingSystemDir;
            this.suffixBinary = suffixBinary;
        }

        public static OperatingSystem getOperatingSystem(String osName) {
            for (final OperatingSystem operatingSystemName : values()) {
                if (osName.toLowerCase().contains(operatingSystemName.getOperatingSystemName())) {
                    return operatingSystemName;
                }
            }
            throw new IllegalArgumentException("Unrecognised operating system name '" + osName + "'");
        }

        public static Set<OperatingSystem> getCurrentOperatingSystemAsAHashSet() {
            final String currentOperatingSystemName = System.getProperties().getProperty("os.name");

            final Set<OperatingSystem> listOfOperatingSystems = new HashSet<>();
            listOfOperatingSystems.add(getOperatingSystem(currentOperatingSystemName));

            return listOfOperatingSystems;
        }

        public static OperatingSystem getCurrentOperatingSystem() {
            final String currentOperatingSystemName = System.getProperties().getProperty("os.name");
            return getOperatingSystem(currentOperatingSystemName);
        }

        public String getOperatingSystemName() {
            return operatingSystemName;
        }

        public String getOperatingSystemDir() {
            return operatingSystemDir;
        }

        public String getSuffixBinary() {
            return suffixBinary;
        }

    }

    public enum SystemArchitecture {

        ARCHITECTURE_64_BIT("64bit"), ARCHITECTURE_32_BIT("32bit");

        private final String systemArchitectureName;
        private static final SystemArchitecture defaultSystemArchitecture = ARCHITECTURE_32_BIT;
        private static List<String> architecture64bitNames = Arrays.asList("amd64", "x86_64");

        SystemArchitecture(String systemArchitectureName) {
            this.systemArchitectureName = systemArchitectureName;
        }

        public String getSystemArchitectureName() {
            return systemArchitectureName;
        }

        public static SystemArchitecture getSystemArchitecture(String currentArchitecture) {
            SystemArchitecture result = defaultSystemArchitecture;
            if (architecture64bitNames.contains(currentArchitecture)) {
                result = ARCHITECTURE_64_BIT;
            }
            return result;
        }

        public static SystemArchitecture getCurrentSystemArchitecture() {
            final String currentArchitecture = System.getProperties().getProperty("os.arch");
            logger.info("os.arch: {}", currentArchitecture);
            return getSystemArchitecture(currentArchitecture);
        }

    }

}
