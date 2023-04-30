** Push notification to user on a speicific version of the app was introduced in version 49

server key:   AAAA8NMHzts:APA91bFjZ397mj_ElThfxrs59IMEngwEST0jTxJbxf01_iANrCrH79lWlU2cgn3ri20_hsyLo7bYknJ0t7scicWA5618J2JbRecmxaeZCW6CEwe-GDgi-IePcePml1wCJR8h4_Hm5wXx
    

To send notifications, go to https://firebase-notifier.vercel.app/


** To send notifications to a specific version to all user:

1. set topic as:  general_user
2. Don't add title and message
3. Click on ADD DATA
4. set two key-value item with key "message" and "version_code"

    message: There is a new version available! You can download it from google play.
    version_code: 57

5. Click on SEND button.



** To send notifications to all user:

1. set topic as:  general_user
2. Add title and message
3. Add key-value data with key "message"
    message: There is a new version available! You can download it from google play.
4. Click on SEND button.


** To send notifications to a specific version to specific user, use device token instead of "Topic".
