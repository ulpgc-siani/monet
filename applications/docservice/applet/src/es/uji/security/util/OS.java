package es.uji.security.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

public class OS {
    public OS() {
    }

    public static byte[] getBytesFromFile(File file) throws IOException {
        FileInputStream is = new FileInputStream(file);
        long length = file.length();
        if(length > 2147483647L) {
            ;
        }

        byte[] bytes = new byte[(int)length];
        int offset = 0;

        int numRead1;
        for(boolean numRead = false; offset < bytes.length && (numRead1 = is.read(bytes, offset, bytes.length - offset)) >= 0; offset += numRead1) {
            ;
        }

        if(offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        } else {
            is.close();
            return bytes;
        }
    }

    public static String getSystemTmpDir() {
        return System.getProperty("java.io.tmpdir");
    }

    public static String stackTraceToString(Exception exc) {
        byte[] b;
        try {
            PipedInputStream e = new PipedInputStream();
            PipedOutputStream pOutput = new PipedOutputStream(e);
            PrintStream pw = new PrintStream(pOutput);
            exc.printStackTrace(pw);
            b = new byte[e.available()];
            e.read(b, 0, e.available());
        } catch (Exception var5) {
            return "Cannot get StackTrace - no info";
        }

        return new String(b);
    }

    public static byte[] inputStreamToByteArray(InputStream in) throws IOException {
        byte[] buffer = new byte[2048];
        boolean length = false;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int length1;
        while((length1 = in.read(buffer)) >= 0) {
            baos.write(buffer, 0, length1);
        }

        return baos.toByteArray();
    }

    public static void dumpToFile(String fileName, byte[] data) throws IOException {
        if(fileName != null && fileName.length() > 0) {
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.write(data);
            fos.flush();
            fos.close();
        }

    }

    public static void dumpToFile(File file, InputStream in) throws IOException {
        System.out.println("Dumping to file available: " + in.available());
        if(file != null) {
            byte[] buffer = new byte[2048];
            boolean length = false;
            FileOutputStream fos = new FileOutputStream(file);

            int length1;
            while((length1 = in.read(buffer)) >= 0) {
                fos.write(buffer, 0, length1);
            }

            fos.close();
        }

    }

    public static void copyfile(String srFile, String dtFile) throws FileNotFoundException, IOException {
        File f1 = new File(srFile);
        File f2 = new File(dtFile);
        if(!f2.exists()) {
            FileInputStream in = new FileInputStream(f1);
            FileOutputStream out = new FileOutputStream(f2);
            byte[] buf = new byte[1024];

            int len;
            while((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            in.close();
            out.close();
        }

    }

    public static String getOS() {
        return System.getProperty("os.name").toLowerCase();
    }

    public static boolean isWindowsXP() {
        return getOS().indexOf("windows xp") > -1;
    }

    public static boolean isWindows2000() {
        return getOS().indexOf("windows 2000") > -1;
    }

    public static boolean isWindows2003() {
        return getOS().indexOf("windows 2003") > -1;
    }

    public static boolean isWindowsVista() {
        return getOS().indexOf("vista") > -1;
    }

    public static boolean isWindows7() {
        return getOS().indexOf("windows 7") > -1;
    }

    public static boolean isWindows8() {
        return getOS().indexOf("windows 8") > -1;
    }

    public static boolean isWindows10() {
        return getOS().indexOf("windows 10") > -1;
    }

    public static boolean isWindowsNT() {
        return getOS().indexOf("nt") > -1;
    }

    public static boolean isMac() {
        return getOS().indexOf("mac") > -1;
    }

    public static boolean isLinux() {
        return getOS().indexOf("linux") > -1;
    }

    public static boolean isWindowsUpperEqualToNT() {
        return isWindowsNT() || isWindows2000() || isWindowsXP() || isWindows2003() || isWindowsVista() || isWindows7() || isWindows8() || isWindows10();
    }

    public static boolean isJavaUpperEqualTo6() {
        String version = System.getProperty("java.version");
        return version.indexOf("1.6") > -1 || version.indexOf("1.7") > -1 || version.indexOf("1.8") > -1;
    }
}
