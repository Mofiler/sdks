## Installation

ionic cordova plugin add https://github.com/Mofiler/cordova-plugin-mofiler.git

## Usage

```
import { Platform } from 'ionic-angular';
import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';

declare var cordova: any;

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})


export class HomePage {

  public MofilerPlugin;

  constructor(public navCtrl: NavController, platform: Platform) {
		
    platform.ready().then(() => {
      this.MofilerPlugin = cordova.require("cordova-plugin-mofiler.mofiler");

      this.MofilerPlugin.setURL ("mofiler.com/mock"); //http://mofiler.com/mock <— para pruebas
      this.MofilerPlugin.setAppKey ("TEST-KEY");
      this.MofilerPlugin.setAppName ("Test Name");
      this.MofilerPlugin.setUseVerboseContext(true);
      this.MofilerPlugin.setUseLocation(true);
      this.MofilerPlugin.setReadPhoneState(false);

      this.MofilerPlugin.addIdentity("username", "bt");// <— la forma que tengan de identificar al usuario, por ejemplo email, FB o algun id random
    });
  }

  logEvent(event) {
    console.log("flush");

    this.MofilerPlugin.injectValue("valueKey", ""+new Date().getTime());
    this.MofilerPlugin.flushDataToMofiler();
  }

}
```
