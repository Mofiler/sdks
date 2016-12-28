package com.mofiler.anes
{
	import flash.events.EventDispatcher;
	import flash.external.ExtensionContext;

	public class Mofiler extends EventDispatcher
	{
		private static const _EXTENSION_ID:String = "com.mofiler.anes.Mofiler";

		static private var instance:Mofiler= null;
		private var context:ExtensionContext = ExtensionContext.createExtensionContext(_EXTENSION_ID, 'Mofiler');
		
		function Mofiler() {
			
		}
		
		static public function getInstance():Mofiler {
			if(instance == null)
				instance = new Mofiler();
			return instance;
		}
		
		
		public function setURL(url:String):void
		{
			try
			{
				context.call('setURL', url);
			}
			catch (error:Error)
			{
				trace(error.message);
			}
		}

		public function setAppKey(appKey:String):void
		{
//			var context:ExtensionContext;
			try
			{
//				context = ExtensionContext.createExtensionContext(_EXTENSION_ID, 'setAppKey');
				context.call('setAppKey', appKey);
			}
			catch (error:Error)
			{
				trace(error.message);
			}
			
//			if (context)
//			{
//				context.dispose();
//				context = null;
//			}
			
		}

		public function setAppName(appName:String):void
		{
//			var context:ExtensionContext;
			try
			{
//				context = ExtensionContext.createExtensionContext(_EXTENSION_ID, 'setAppName');
				context.call('setAppName', appName);
			}
			catch (error:Error)
			{
				trace(error.message);
			}
			
//			if (context)
//			{
//				context.dispose();
//				context = null;
//			}
			
		}

		public function addIdentity(key:String, value:String):void
		{
//			var context:ExtensionContext;
			try
			{
//				context = ExtensionContext.createExtensionContext(_EXTENSION_ID, 'addIdentity');
				context.call('addIdentity', [key, value]);
			}
			catch (error:Error)
			{
				trace(error.message);
			}
			
//			if (context)
//			{
//				context.dispose();
//				context = null;
//			}
			
		}

		public function setUseVerboseContext(state:Boolean):void
		{
//			var context:ExtensionContext;
			try
			{
//				context = ExtensionContext.createExtensionContext(_EXTENSION_ID, 'setUseVerboseContext');
				context.call('setUseVerboseContext', state);
			}
			catch (error:Error)
			{
				trace(error.message);
			}
			
//			if (context)
//			{
//				context.dispose();
//				context = null;
//			}
			
		}
		
		public function setUseLocation(state:Boolean):void
		{
//			var context:ExtensionContext;
			try
			{
//				context = ExtensionContext.createExtensionContext(_EXTENSION_ID, 'setUseLocation');
				context.call('setUseLocation', state);
			}
			catch (error:Error)
			{
				trace(error.message);
			}
			
//			if (context)
//			{
//				context.dispose();
//				context = null;
//			}
			
		}
		
		public function injectValue(key:String, value:String):void
		{
//			var context:ExtensionContext;
			try
			{
//				context = ExtensionContext.createExtensionContext(_EXTENSION_ID, 'injectValue');
				context.call('injectValue', [key, value]);
			}
			catch (error:Error)
			{
				trace(error.message);
			}
			
//			if (context)
//			{
//				context.dispose();
//				context = null;
//			}
			
		}
		
		public function flushDataToMofiler():void
		{
//			var context:ExtensionContext;
			try
			{
//				context = ExtensionContext.createExtensionContext(_EXTENSION_ID, 'flushDataToMofiler');
				context.call('flushDataToMofiler');
			}
			catch (error:Error)
			{
				trace(error.message);
			}
			
//			if (context)
//			{
//				context.dispose();
//				context = null;
//			}
			
		}

	}
}