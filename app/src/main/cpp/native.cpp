#include <jni.h>
#include "logging.h"
#include "native.h"

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
    LOGD("%s", "JNI_OnLoad");
    BILI_JNI_OnLoad(vm, reserved);
    return JNI_VERSION_1_6;
}
