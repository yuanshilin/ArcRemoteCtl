###############################################################################
# ArcRemoteCtl
LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_PACKAGE_NAME:= ArcRemoteCtl
LOCAL_MODULE_TAGS := optional
LOCAL_CERTIFICATE := platform
LOCAL_SRC_FILES := $(call all-java-files-under, src)
#LOCAL_PRIVILEGED_MODULE := true
LOCAL_PRIVATE_PLATFORM_APIS := true
LOCAL_STATIC_ANDROID_LIBRARIES := \
	androidx.appcompat_appcompat \
	androidx-constraintlayout_constraintlayout

include $(BUILD_PACKAGE)
