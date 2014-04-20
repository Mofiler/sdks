// Default empty project template
import bb.cascades 1.0

// creates one page with a label
Page {
    id: mypage
    property int mycounter: 0
    Container {
        layout: StackLayout {}
        Label {
            text: qsTr("Hello World")
            textStyle.base: SystemDefaults.TextStyles.BigText
            verticalAlignment: VerticalAlignment.Center
            horizontalAlignment: HorizontalAlignment.Center
        }
        
        Button{
            id: initialmof
            text: "Initialize mofiler"
            onClicked: {
                console.log("doing something");
                mofiler.appKey = "HELLOMOFILERCASCADESKEY";
                //mofiler.setAppName("HelloMofiler");
                mofiler.appName = "HelloMofiler";
                //mofiler.setAppVersion("1.0");
                mofiler.appVersion = "1.0";
                //mofiler.setUrl("http://localhost:3000");
                //mofiler.url = "http://localhost:3000";
                //mofiler.url = "http://192.168.0.26:3000";
                mofiler.url = "http://192.168.1.67:3000";
                mofiler.addIdentity("username", "johndoe");
            }
            
        }

        Button {
            id: undato
            text: "Send data to mofiler"
            onClicked: {
                console.log("doing something");
                var i = mypage.mycounter;
                //for (var i=0; i < 12; i++){
                    mofiler.injectValue("mykey" + i, "myvalue");
                    mypage.mycounter++;
                    text = "Send data to mofiler " + mypage.mycounter;
                //}
            }

        }

    }
}

