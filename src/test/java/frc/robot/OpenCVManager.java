package frc.robot;

import java.io.File;

/**
 * Created by Kylec on 5/14/2017.
 */
public class OpenCVManager {
    private String tPath;
    private final static String LIB_NAME_LINUX = "libopencv_java345.so";
    private final static String LIB_NAME_WIN_64 = "opencv_java420.dll"; // TODO: Update windows libs
    private final static String LIB_NAME_WIN_32 = "opencv_java420.dll";

    private boolean isLoaded = false;

    public class OpenCVLoadException extends RuntimeException {
        /**
         *
         */
        private static final long serialVersionUID = -4652152272412108957L;

        public OpenCVLoadException() {
            super("Could not locate OpenCV libraries. " + tPath);
        }
    }

    public class UnsupportedOperatingSystemException extends RuntimeException {
        /**
         *
         */
        private static final long serialVersionUID = -3269353744296468344L;

        public UnsupportedOperatingSystemException() {
            super("Your operating system is not supported.");
        }
    }

    public class UnsupportedArchitectureException extends RuntimeException {
        /**
         *
         */
        private static final long serialVersionUID = -2209742362185232282L;

        public UnsupportedArchitectureException() {
            super("Your hardware architecture is not supported.");
        }
    }

    private OpenCVManager() {
    }

    private static class OpenCVManagerHolder {
        public static final OpenCVManager instance = new OpenCVManager();
    }

    public static OpenCVManager getInstance() {
        return OpenCVManagerHolder.instance;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void load(ISystemProperties systemProperties) {
        OSInfo osInfo = new OSInfo(systemProperties);
        OSInfo.OS os = osInfo.getOperatingSystem();
        OSInfo.Architecture arch = osInfo.getArchitecture();
        String jarLocation = new File(OpenCVManager.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();
        String libName;
        if (os == OSInfo.OS.LINUX) {
            libName = LIB_NAME_LINUX;
        } else if (os == OSInfo.OS.WINDOWS) {
            if (arch == OSInfo.Architecture.x64) {
                libName = LIB_NAME_WIN_64;
            } else if (arch == OSInfo.Architecture.x86) {
                libName = LIB_NAME_WIN_32;
            } else {
                throw new UnsupportedArchitectureException();
            }
        } else {
            throw new UnsupportedOperatingSystemException();
        }
        boolean loaded = tryLoadingLibraries("../lib/" + libName, "libs/" + libName, jarLocation + "/" + libName);
        isLoaded = loaded;
        if (!loaded) {
            throw new OpenCVLoadException();
        }
    }

    private boolean tryLoadingLibraries(String... libraryPaths) {
        for (String path : libraryPaths) {
            if (tryLoadingLibrary(path)) {
                return true;
            }
        }
        return false;
    }

    private boolean tryLoadingLibrary(String libraryPath) {
        try {
            tPath = new File(libraryPath).getAbsolutePath();
            System.err.print(tPath+"\n");
            System.load(tPath);
            return true;
        } catch (UnsatisfiedLinkError e) {
            System.err.print(e.getMessage()+"\n");
            return false;
        }
    }

}
