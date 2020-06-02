## To send a general notification from pushtry.com, the notification payload structure should be like this.   
if app is background, this title and body will be shown as a notification in system tray.  
then when user clicks on that notification, app will be opened and user will see the "message" in "data" part in a dialog view  
when app is on foreground and this notification payload is received, this "body" message will be shown in a dialog view

server key:   AAAA8NMHzts:APA91bFjZ397mj_ElThfxrs59IMEngwEST0jTxJbxf01_iANrCrH79lWlU2cgn3ri20_hsyLo7bYknJ0t7scicWA5618J2JbRecmxaeZCW6CEwe-GDgi-IePcePml1wCJR8h4_Hm5wXx

    {  
      "to": "FIREBASE_TOKEN",
      "notification": {
        "title": "Hello",
        "body": "Hello world"
      },
      "data": {
        "message": "Hello world 2"
      }
    }
