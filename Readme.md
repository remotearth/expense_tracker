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


    {  
          "to": "fKWitFkMRDaS3jhgAXF47D:APA91bEM_DoQPuDhxzLQMCs_KYjm8buMXBA82MKmrxY2T7iL1-rS31mQLkx9kSFF9J-qFcZiEv2BVQWlzyk0mXQ2zn8zDeP69wgEGb2YZWM8qrUdGjjj5DZXO6mPxUdNi1Xp_1MHxQPc",
          "notification": {
            "title": "New Version!",
            "body": "Hi, there is a new version of the app. You can download it from Google play"
          },
          "data": {
            "message": "Hi, there is a new version of the app. You can download it from Google play"
          }
        }