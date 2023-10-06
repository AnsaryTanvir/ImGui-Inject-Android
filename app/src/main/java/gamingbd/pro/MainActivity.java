package gamingbd.pro;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.content.Intent;
import android.content.Context;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;

import static gamingbd.pro.Native.*;
import eu.chainfire.libsuperuser.Shell;

public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("Client");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Get Super User Permission
         */
        GetSuperUserPermission(this);

        /**
         * Disable SeLinux to use ptrace system call/
         */
        Utils.disableSeLinux(getApplicationContext());

        /**
         * Start Floating Service & Kill Current Activity/
         */
        StartFloating(this);
        finish();
    }

    /**
     * Starts the FloatingViewService if the device supports overlay permissions.
     *
     * This method checks if the device supports overlay permissions, and if so, it starts the
     * FloatingViewService, which is responsible for displaying a floating view on the screen.
     * If the device does not support overlay permissions, it prompts the user to grant them.
     *
     * @param context The context of the Android application.
     */
    public static void StartFloating(Context context) {
        if ( Build.VERSION.SDK_INT <  23 || Settings.canDrawOverlays(context)) {
            context.startService(new Intent(context, FloatingViewService.class));
        } else {
            context.startActivity(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + context.getPackageName())));
            Process.killProcess(Process.myPid());
        }
    }

    /**
     * Checks if superuser (root) permission is granted.
     *
     * This method attempts to gain superuser (root) permission and checks if it is granted.
     *
     * @param context The context of the Android application.
     * @return True if superuser permission is granted, false otherwise.
     */
    public static boolean GetSuperUserPermission(Context context){
        try {
            Shell.Pool.SU.run("su");
            if ( SuperUserPermissionGranted() )
                return true;
        } catch (Shell.ShellDiedException e) {
            e.printStackTrace();
        }
        return false;
    }

}