## To send a general notification from pushtry.com, the notification payload structure should be like this.   
if app is background, this title and body will be shown as a notification in system tray.  
then when user clicks on that notification, app will be opened and user will see the "message" in "data" part in a dialog view  
when app is on foreground and this notification payload is received, this "body" message will be shown in a dialog view

** Push notification to user on a speicific version of the app is introduced in version 49

server key:   AAAA8NMHzts:APA91bFjZ397mj_ElThfxrs59IMEngwEST0jTxJbxf01_iANrCrH79lWlU2cgn3ri20_hsyLo7bYknJ0t7scicWA5618J2JbRecmxaeZCW6CEwe-GDgi-IePcePml1wCJR8h4_Hm5wXx
    
    when need to send all users
    
    {  
      "to": "FIREBASE_TOKEN",
      "notification": {
        "title": "Hello",
        "body": "Hello world"
      },
      "data": {
        "message": "Hello world 2",
        "version": "0"
      }
    }
    
or     
        {                   
          "data": {
            "message": "Hi, there is a new version of the app. You can download it from Google play",
            "version": "0"           
          }
        }

all user of a specific vesion:
        {                   
          "data": {
            "message": "Hi, there is a new version of the app. You can download it from Google play",
            "version": "48"           
          }
        }


to single user

    {  
          "to": "token",
          "notification": {
            "title": "New Version!",
            "body": "Hi, there is a new version of the app. You can download it from Google play"
          },
          "data": {
            "message": "Hi, there is a new version of the app. You can download it from Google play",
            "version": "0"
          }
        }
        
Or
        {  
          "to": "token",        
          "data": {
            "message": "Hi, there is a new version of the app. You can download it from Google play",
            "version": "0"           
          }
        }
        
to specific version of the app. 48 is a version code. if set 0
        {  
          "to": "token",        
          "data": {
            "message": "Hi, there is a new version of the app. You can download it from Google play",
            "version": "48"           
          }
        }

        
        
        
    to multiple device id 
    
    {  
     "registration_ids": ["token1","token2"]     
     "data": {
            "message": "Hello world 2",
            "version_code": "0"
          }
        }