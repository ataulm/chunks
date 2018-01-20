chunks offers **short-lived** to-do lists to gsd.

Add your Fabric API key if you want to send crash info to your Fabric dashboard:

```
# <project_root>/fabric.properties

apiKey=a1b2c2d3e4f5g6h7i8j9
```

Add a signing config properties if you want to make release builds:

```
# <project_root>/signing.properties

storeFile=/path/to/the/keystore
storePassword=password
keyAlias=keyAlias
keyPassword=password
```

# Releasing

A great person has a CI run the following commands before merges to `master`, but a good person will run it before merging back to master and definitely before releasing.
You'll need a device connected (Genymotion will work) with the latest version of Android TalkBack installed. Android TalkBack should have been run at least once to get past the first-run tutorial.


```bash
./gradlew clean build;\
./gradlew mobile:installDebug;\
adb shell pm grant com.ataulm.chunks android.permission.WRITE_SECURE_SETTINGS;\
adb shell am start -a "com.novoda.espresso.DISABLE_TALKBACK";\
./gradlew mobile:cAT;\
adb shell am start -a "com.novoda.espresso.DISABLE_TALKBACK";
```
