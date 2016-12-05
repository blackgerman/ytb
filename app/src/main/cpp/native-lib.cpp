#include <jni.h>
#include <string>

extern "C"
jstring
Java_abiguime_tz_com_tzyoutube_YSplashActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
