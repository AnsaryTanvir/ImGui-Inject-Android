package gamingbd.pro;

import java.io.File;
import java.util.List;
import android.util.Log;
import java.util.Iterator;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.IOException;
import android.widget.Toast;
import java.io.OutputStream;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import android.content.Context;
import java.io.FileOutputStream;

import eu.chainfire.libsuperuser.Shell;

public class Utils {

    public static final String targetPackage = "com.imangi.templerun" ;   // Target package name
    public static final String injector      = "AndKittyInjector"     ;   // Name of the injector executable, defined in CMakeLists.txt
    public static final String payload       = "libServer.so"         ;   // Name of the payload, defined in CMakeLists.txt


    /**
     * This method is used to temporarily disable SeLinux globally.
     *
     * SeLinux or Security-Enhanced Linux is a Linux Security Module aimed to provide
     * Mandatory Access Control (MAC).
     * With SELinux enabled, root access can't do everything anymore because it imposes some
     * restriction such as restricting calls to ptrace (a system call).
     *
     * @param context This is the context.
     * @exception IOException
     * @exception eu.chainfire.libsuperuser.Shell.ShellDiedException
     **/
    public static void disableSeLinux(Context context){
        try {
            Runtime.getRuntime().exec("su");
            Shell.Pool.SU.run("setenforce 0");
        } catch (IOException | Shell.ShellDiedException e) {
            e.printStackTrace();
        }
    }


    /**
     * This method is used to get the process id of a running application.
     *
     * @param pkg The package name of target application whose process id is to get.
     * @return int process id or -1 if process doesn't exists
     * @exception eu.chainfire.libsuperuser.Shell.ShellDiedException
     */
    public static int GetProcessID(String pkg) {

        try {

            ArrayList arrayList             = new ArrayList();
            Shell.PoolWrapper poolWrapper   = Shell.Pool.SU;
            poolWrapper.run("(toolbox ps; toolbox ps -A; toybox ps; toybox ps -A) | grep \" " + pkg + "$\"", arrayList, null, false);
            Iterator iterator               = arrayList.iterator();

            while (iterator.hasNext()) {

                String Trim = ((String) iterator.next()).trim();
                while (Trim.contains("  "))
                    Trim = Trim.replace("  ", " ");

                String[] Split = Trim.split(" ");
                if (Split.length >= 2)
                    return Integer.parseInt(Split[1]);
            }
            return -1;

        } catch (Shell.ShellDiedException e) {
            e.printStackTrace();
            return -1;
        }
    }


    /**
     * Extracts a file from the APK (Android application package) and saves it to the app's cache directory.
     *
     * This method extracts a specified file from the APK and saves it to the app's cache directory for further use.
     * The file is extracted by its absolute path within the APK, and it is saved with a new name in the cache directory.
     *
     * @param context The context of the Android application.
     * @param fileAbsolutePathInApk The absolute path of the file within the APK to be extracted.
     * @param outputFileName The name of the file to be saved in the app's cache directory.
     *
     * @throws IOException If an I/O error occurs during file extraction or saving.
     */
    public static void extractFileFromApkAndSaveToCache(Context context, String fileAbsolutePathInApk, String outputFileName) {
        try {

            String      apkPath     = context.getPackageCodePath();
            ZipFile     zipFile     = new ZipFile(apkPath);
            ZipEntry    zipEntry    = zipFile.getEntry(fileAbsolutePathInApk);
            InputStream inputStream = zipFile.getInputStream(zipEntry);

            // Get the cache directory dynamically
            File cacheDir   = context.getCacheDir();
            // Create a File object for the output file in the cache directory
            File outputFile = new File(cacheDir, outputFileName);

            // Create the output stream
            OutputStream outputStream = new FileOutputStream(outputFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            // Close streams
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Injects a payload into a target process using the provided injector.
     *
     * This method attempts to inject a payload into a specified target process using an injector.
     * It performs the injection by copying the injector and payload from the app's APK into a whitelisted
     * partition with appropriate permissions and then executing the injector with the payload.
     *
     * @param context The context of the Android application.
     * @return True if the injection is successful, false otherwise.
     */
    public static boolean inject(Context context){

        /* Get the process id (PID) of target */
        int pid = Utils.GetProcessID(targetPackage);

        /* If the process doesn't have a PID i.e (PID < 0) that means it's not running */
        if ( pid < 0 ){
            Toast.makeText(context,targetPackage+" is not running", Toast.LENGTH_SHORT).show();
            return false;
        }

        /* Copy injector & payload into /data/data/package-name/cache */
         extractFileFromApkAndSaveToCache(context, "assets/armeabi-v7a/"  + injector  , injector );
         extractFileFromApkAndSaveToCache(context, "lib/armeabi-v7a/"     + payload   , payload  );

        /* Due to Android Linker Namespace Restriction, we need to execute injector & link payload
           from a whitelisted partition such as /data with appropriate permission */
        try {

            Shell.Pool.SU.run("cp "+    "/data/data/"+context.getPackageName()+"/cache/"+injector+  " /data/local" );
            Shell.Pool.SU.run("rm "+    "/data/data/"+context.getPackageName()+"/cache/"+injector  );
            Shell.Pool.SU.run("chmod "+  "777 "+  "/data/local/"+injector );

            Shell.Pool.SU.run("cp "+   "/data/data/"+context.getPackageName()+"/cache/"+payload+  " /data/local");
            Shell.Pool.SU.run("rm "+   "/data/data/"+context.getPackageName()+"/cache/"+payload );
            Shell.Pool.SU.run("chmod "+  "777 "+  "/data/local/"+payload );

        } catch (Shell.ShellDiedException e) {
            e.printStackTrace();
            return false;
        }


        try {

            List<String> STDOUT = new ArrayList<>();
            String injectCommand = String.format("./data/local/%s %s /data/local/%s", injector, targetPackage, payload );
            Shell.Pool.SU.run( injectCommand, STDOUT, null, false);

            /* Log the output of the performed command i.e injection */
            for ( String line: STDOUT) Log.d("Debug Injector", ""+ line);

            /* Reading the last line of STDOUT */
            if ( STDOUT.get( STDOUT.size() - 1 ).equals("I: Success.") ){
                return true;
            }

            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
