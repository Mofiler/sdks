// Default empty project template
import bb.cascades 1.0

// creates one page with a label
Page {
    id: mypage
    property int mycounter: 0
    property variant myvar: mofiler.value
    onMyvarChanged: {
        console.log("Response gotten from Mofiler server: ");
        var varcont = JSON.stringify(myvar); //note that checking either "myvar" or "mofiler.value" would be the same
        console.log(varcont);
        if (mofiler.value != undefined){
        	console.log("value gotten for requested key: " + JSON.stringify(mofiler.value));    
        }
    }
    
    
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
                mofiler.url = "http://192.168.0.26:3000";
                //mofiler.url = "http://192.168.1.67:3000";
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

        Button {
            id: undatoexpire
            text: "Send data to mofiler - with expiredate"
            onClicked: {
                console.log("doing something");
                var i = mypage.mycounter;
                //for (var i=0; i < 12; i++){
                mofiler.injectValue("mykey" + i, "myvalue", 1398033731463);
                mypage.mycounter ++;
                text = "Send data to mofiler - with expiredate " + mypage.mycounter;
                //}
            }

        }
        
        Button {
            id: getdatabtn
            text: "Receive Data from Mofiler!"
            onClicked: {
                console.log("fetching data...");
                mofiler.getValue("mykey0", "username", "johndoe");
            }

        }

        Button {
            id: flushbtn
            text: "Flush unsent data to Mofiler!"
            onClicked: {
                console.log("flushing...");
                mofiler.flushData();
            }

        }

    }
}

