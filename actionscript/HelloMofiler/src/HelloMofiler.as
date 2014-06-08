package
{
	import com.debokeh.anes.utils.DeviceInfoUtil;
	import com.mofiler.Mofiler;
	
	import flash.display.Sprite;
	import flash.display.StageAlign;
	import flash.display.StageScaleMode;
	import flash.events.MouseEvent;
	import flash.text.TextField;
	
	
	
	[SWF(height="600", width="1024", frameRate="30", backgroundColor="#BBBBBB")]
	public class HelloMofiler extends Sprite
	{
		private var helloButton:CustomSimpleButton;
		private var txtField:TextField;
		
		
		public function HelloMofiler()
		{
			super();
			stage.align = StageAlign.TOP_LEFT;
			stage.scaleMode = StageScaleMode.NO_SCALE;
			
			txtField = new TextField();
			txtField.text = "<info>";
			txtField.width = 400;
			
			addChild(txtField);
			
			/* A button to run native code. */
			helloButton = new CustomSimpleButton();
			helloButton.width = 150;
			helloButton.x = stage.stageWidth / 2 - helloButton.width / 2;
			helloButton.y = stage.stageHeight / 2;
						
			/* Listen for a click on the button. */
			helloButton.addEventListener(MouseEvent.CLICK, onHelloButtonClick);
			helloButton.enabled = true;
			addChild(helloButton);
			stage.nativeWindow.visible = true;
			
		}
		
		private function onHelloButtonClick(event:MouseEvent):void
		{
			var m:Mofiler = new Mofiler();
			m.setAppKey("myAppKey");
			m.setAppName("myAppName");
			m.setAppVersion("1.0");
			m.setURL("mofiler.com:8081");
			m.addIdentity("username", "flash_jhondoe");
			if(DeviceInfoUtil.getPIN()!=null){
				m.addIdentity("pin", DeviceInfoUtil.getPIN());
			}
			if(DeviceInfoUtil.getCurrentDeviceName()!=null){
				m.addIdentity("deviceName", DeviceInfoUtil.getCurrentDeviceName());
			}
			if(DeviceInfoUtil.getCurrentMACAddress()!=null){
				m.addIdentity("macAddress", DeviceInfoUtil.getCurrentMACAddress());
			}
			if(DeviceInfoUtil.getCurrentSSID()!=null){
				m.addIdentity("ssid", DeviceInfoUtil.getCurrentSSID());
			}
			if(DeviceInfoUtil.getIMEI()!=null){
				m.addIdentity("imei", DeviceInfoUtil.getIMEI());
			}
			if(DeviceInfoUtil.getDeviceModelName()!=null){
				m.addIdentity("deviceModelName", DeviceInfoUtil.getDeviceModelName());
			}
			
			txtField.text = "Pin: "+DeviceInfoUtil.getPIN()+"\n";
			txtField.text += "deviceName"+DeviceInfoUtil.getCurrentDeviceName()+"\n";;
			txtField.text += "macAddress: "+DeviceInfoUtil.getCurrentMACAddress()+"\n";;
			txtField.text += "ssid: "+DeviceInfoUtil.getCurrentSSID()+"\n";;
			txtField.text += "imei: "+DeviceInfoUtil.getIMEI()+"\n";;
			txtField.text += "deviceModelName: "+DeviceInfoUtil.getDeviceModelName()+"\n";;
				
				
			m.injectValue("testKey", "testValue", 0);
			
		}
	}
	
}
import flash.display.Shape;
import flash.display.SimpleButton;
class CustomSimpleButton extends SimpleButton {
	private var upColor:uint   = 0xFFCC00;
	private var overColor:uint = 0xCCFF00;
	private var downColor:uint = 0x00CCFF;
	private var size:uint      = 80;
	
	public function CustomSimpleButton() {
		downState      = new ButtonDisplayState(downColor, size);
		overState      = new ButtonDisplayState(overColor, size);
		upState        = new ButtonDisplayState(upColor, size);
		hitTestState   = new ButtonDisplayState(upColor, size * 2);
		hitTestState.x = -(size / 4);
		hitTestState.y = hitTestState.x;
		useHandCursor  = true;
	}
}

class ButtonDisplayState extends Shape {
	private var bgColor:uint;
	private var size:uint;
	
	public function ButtonDisplayState(bgColor:uint, size:uint) {
		this.bgColor = bgColor;
		this.size    = size;
		draw();
	}
	
	private function draw():void {
		graphics.beginFill(bgColor);
		graphics.drawRect(0, 0, size, size);
		graphics.endFill();
	}
}