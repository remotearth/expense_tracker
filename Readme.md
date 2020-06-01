## To send a general notification from pushtry.com, the notification payload structure should be like this.   
  
    {  
      "to": "FIREBASE_TOKEN",  
      //if app is background, this tille and body will be shown as a notification in system tray.  
      //then when user clicks on that notification, app will be opened and user will see the "message" in "data" part in a dialog view
      "notification": {  
        "title": "Hello",  
        "body": "Hello world" // when app is on foreground and this notification payload is received, this "body" message will be shown in a dialog view
      },  
      "data": {  
        "message": "Hello world 2"  
      }  
    }
