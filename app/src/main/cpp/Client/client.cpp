#include <jni.h>
#include <android/log.h>
#include <unistd.h>
#include <string>
#include "Icon.h"

#define LOG_TAG "Debug"
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

extern "C"
JNIEXPORT jstring JNICALL
Java_gamingbd_pro_Native_IconNative(JNIEnv *env, jclass clazz) {
    return GetIcon(env);
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_gamingbd_pro_Native_SuperUserPermissionGranted(JNIEnv *env, jclass clazz) {
    if ( system("su") == 0 )
        return true;
    return false;
}

__attribute__((constructor))
int main(){
    LOGD("%s", "Client: I am loaded in the address space");
}