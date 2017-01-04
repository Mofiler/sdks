package
{
	import com.mofiler.anes.Mofiler;

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
//			stage.nativeWindow.visible = true;
			
		}
		
		private function onHelloButtonClick(event:MouseEvent):void
		{
			
			var m:Mofiler = Mofiler.getInstance();
			m.setAppKey("SENSEBYTEMOBILE_TEST_FLASH");
			m.setAppName("App Test Flash");
//			m.setURL("mofiler.com/mock");
			m.setURL("mofiler.com");
			m.setUseVerboseContext(true);
			m.setUseLocation(true);
			
			//SET UNIQUE IDENTIFIERS FOR YOUR USER
			m.addIdentity("username", "flash_jhondoe");
			m.addIdentity("email", "flash@jhondoe.com");

			//INJECT VALUES
			m.injectValue("randomValue", (Math.random()*9999999).toString());
			m.injectValue("testKey0-1", "testValue0");
			m.injectValue("testKey-1", "testValue");
			m.injectValue("testKey-1", "testValue");
			m.injectValue("testKey1-1", "testValue");
			m.injectValue("testKey1-1", "testValue");
			m.injectValue("testKey2-1", "testValue");
			m.injectValue("testKey2-1", "testValue");
			m.injectValue("testKey2-1", "testValue");
			m.injectValue("testKey2-1", "testValue");
			m.injectValue("testKey2-1", "testValue");
			m.injectValue("testKey2-1", "testValue");
			m.injectValue("testKey3-1", "testValue");
			m.injectValue("testKey4-1", "testValue");
			m.injectValue("testKey5-1", "testValue");
			
			m.flushDataToMofiler();
			
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