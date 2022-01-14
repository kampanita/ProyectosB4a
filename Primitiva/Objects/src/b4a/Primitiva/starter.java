package b4a.Primitiva;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.objects.ServiceHelper;
import anywheresoftware.b4a.debug.*;

public class starter extends  android.app.Service{
	public static class starter_BR extends android.content.BroadcastReceiver {

		@Override
		public void onReceive(android.content.Context context, android.content.Intent intent) {
            BA.LogInfo("** Receiver (starter) OnReceive **");
			android.content.Intent in = new android.content.Intent(context, starter.class);
			if (intent != null)
				in.putExtra("b4a_internal_intent", intent);
            ServiceHelper.StarterHelper.startServiceFromReceiver (context, in, true, BA.class);
		}

	}
    static starter mostCurrent;
	public static BA processBA;
    private ServiceHelper _service;
    public static Class<?> getObject() {
		return starter.class;
	}
	@Override
	public void onCreate() {
        super.onCreate();
        mostCurrent = this;
        if (processBA == null) {
		    processBA = new BA(this, null, null, "b4a.Primitiva", "b4a.Primitiva.starter");
            if (BA.isShellModeRuntimeCheck(processBA)) {
                processBA.raiseEvent2(null, true, "SHELL", false);
		    }
            try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            processBA.loadHtSubs(this.getClass());
            ServiceHelper.init();
        }
        _service = new ServiceHelper(this);
        processBA.service = this;
        
        if (BA.isShellModeRuntimeCheck(processBA)) {
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.Primitiva.starter", processBA, _service, anywheresoftware.b4a.keywords.Common.Density);
		}
        if (!true && ServiceHelper.StarterHelper.startFromServiceCreate(processBA, false) == false) {
				
		}
		else {
            processBA.setActivityPaused(false);
            BA.LogInfo("*** Service (starter) Create ***");
            processBA.raiseEvent(null, "service_create");
        }
        processBA.runHook("oncreate", this, null);
        if (true) {
			ServiceHelper.StarterHelper.runWaitForLayouts();
		}
    }
		@Override
	public void onStart(android.content.Intent intent, int startId) {
		onStartCommand(intent, 0, 0);
    }
    @Override
    public int onStartCommand(final android.content.Intent intent, int flags, int startId) {
    	if (ServiceHelper.StarterHelper.onStartCommand(processBA, new Runnable() {
            public void run() {
                handleStart(intent);
            }}))
			;
		else {
			ServiceHelper.StarterHelper.addWaitForLayout (new Runnable() {
				public void run() {
                    processBA.setActivityPaused(false);
                    BA.LogInfo("** Service (starter) Create **");
                    processBA.raiseEvent(null, "service_create");
					handleStart(intent);
                    ServiceHelper.StarterHelper.removeWaitForLayout();
				}
			});
		}
        processBA.runHook("onstartcommand", this, new Object[] {intent, flags, startId});
		return android.app.Service.START_NOT_STICKY;
    }
    public void onTaskRemoved(android.content.Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        if (true)
            processBA.raiseEvent(null, "service_taskremoved");
            
    }
    private void handleStart(android.content.Intent intent) {
    	BA.LogInfo("** Service (starter) Start **");
    	java.lang.reflect.Method startEvent = processBA.htSubs.get("service_start");
    	if (startEvent != null) {
    		if (startEvent.getParameterTypes().length > 0) {
    			anywheresoftware.b4a.objects.IntentWrapper iw = ServiceHelper.StarterHelper.handleStartIntent(intent, _service, processBA);
    			processBA.raiseEvent(null, "service_start", iw);
    		}
    		else {
    			processBA.raiseEvent(null, "service_start");
    		}
    	}
    }
	
	@Override
	public void onDestroy() {
        super.onDestroy();
        if (true) {
            BA.LogInfo("** Service (starter) Destroy (ignored)**");
        }
        else {
            BA.LogInfo("** Service (starter) Destroy **");
		    processBA.raiseEvent(null, "service_destroy");
            processBA.service = null;
		    mostCurrent = null;
		    processBA.setActivityPaused(true);
            processBA.runHook("ondestroy", this, null);
        }
	}

@Override
	public android.os.IBinder onBind(android.content.Intent intent) {
		return null;
	}public anywheresoftware.b4a.keywords.Common __c = null;
public b4a.example.dateutils _dateutils = null;
public b4a.Primitiva.main _main = null;
public b4a.Primitiva.httputils2service _httputils2service = null;
public b4a.Primitiva.xuiviewsutils _xuiviewsutils = null;
public static void  _activatessl() throws Exception{
ResumableSub_ActivateSSL rsub = new ResumableSub_ActivateSSL(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_ActivateSSL extends BA.ResumableSub {
public ResumableSub_ActivateSSL(b4a.Primitiva.starter parent) {
this.parent = parent;
}
b4a.Primitiva.starter parent;
anywheresoftware.b4j.object.JavaObject _javaobjectcontext = null;
anywheresoftware.b4j.object.JavaObject _javaobjectinstance = null;
Object _objectlistener = null;
anywheresoftware.b4a.phone.Phone _phoneinstance = null;
String _stringmethodname = "";
Object[] _objectarguments = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
try {

        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 49;BA.debugLine="Dim javaobjectContext";
_javaobjectcontext = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 50;BA.debugLine="Dim javaobjectInstance";
_javaobjectinstance = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 51;BA.debugLine="Dim objectListener";
_objectlistener = new Object();
 //BA.debugLineNum = 52;BA.debugLine="Dim phoneInstance";
_phoneinstance = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 54;BA.debugLine="Try";
if (true) break;

case 1:
//try
this.state = 12;
this.catchState = 11;
this.state = 3;
if (true) break;

case 3:
//C
this.state = 4;
this.catchState = 11;
 //BA.debugLineNum = 55;BA.debugLine="Select Case phoneInstance.SdkVersion";
if (true) break;

case 4:
//select
this.state = 9;
switch (BA.switchObjectToInt(_phoneinstance.getSdkVersion(),(int) (16),(int) (17),(int) (18),(int) (19),(int) (20))) {
case 0: 
case 1: 
case 2: 
case 3: 
case 4: {
this.state = 6;
if (true) break;
}
default: {
this.state = 8;
if (true) break;
}
}
if (true) break;

case 6:
//C
this.state = 9;
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 58;BA.debugLine="Return";
if (true) return ;
 if (true) break;

case 9:
//C
this.state = 12;
;
 //BA.debugLineNum = 60;BA.debugLine="javaobjectInstance.InitializeStatic (\"com.google";
_javaobjectinstance.InitializeStatic("com.google.android.gms.security.ProviderInstaller");
 //BA.debugLineNum = 61;BA.debugLine="javaobjectContext.InitializeContext";
_javaobjectcontext.InitializeContext(processBA);
 //BA.debugLineNum = 62;BA.debugLine="DisableStrictMode";
_disablestrictmode();
 //BA.debugLineNum = 63;BA.debugLine="objectListener = javaobjectInstance.CreateEventF";
_objectlistener = _javaobjectinstance.CreateEventFromUI(processBA,"com.google.android.gms.security.ProviderInstaller.ProviderInstallListener","objectListener",anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 64;BA.debugLine="javaobjectInstance.RunMethod (\"installIfNeededAs";
_javaobjectinstance.RunMethod("installIfNeededAsync",new Object[]{(Object)(_javaobjectcontext.getObject()),_objectlistener});
 //BA.debugLineNum = 65;BA.debugLine="Wait For objectListener_Event (stringMethodName";
anywheresoftware.b4a.keywords.Common.WaitFor("objectlistener_event", processBA, this, null);
this.state = 13;
return;
case 13:
//C
this.state = 12;
_stringmethodname = (String) result[0];
_objectarguments = (Object[]) result[1];
;
 if (true) break;

case 11:
//C
this.state = 12;
this.catchState = 0;
 //BA.debugLineNum = 68;BA.debugLine="Log(\"Application_Error\")";
anywheresoftware.b4a.keywords.Common.LogImpl("62162709","Application_Error",0);
 if (true) break;
if (true) break;

case 12:
//C
this.state = -1;
this.catchState = 0;
;
 //BA.debugLineNum = 71;BA.debugLine="End Sub";
if (true) break;
}} 
       catch (Exception e0) {
			
if (catchState == 0)
    throw e0;
else {
    state = catchState;
processBA.setLastException(e0);}
            }
        }
    }
}
public static void  _objectlistener_event(String _stringmethodname,Object[] _objectarguments) throws Exception{
}
public static boolean  _application_error(anywheresoftware.b4a.objects.B4AException _error,String _stacktrace) throws Exception{
 //BA.debugLineNum = 40;BA.debugLine="Sub Application_Error (Error As Exception, StackTr";
 //BA.debugLineNum = 41;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 42;BA.debugLine="End Sub";
return false;
}
public static String  _disablestrictmode() throws Exception{
anywheresoftware.b4j.object.JavaObject _javaobjectinstance = null;
anywheresoftware.b4j.object.JavaObject _javaobjectpolicy = null;
anywheresoftware.b4j.object.JavaObject _javaobjectstrictmode = null;
 //BA.debugLineNum = 73;BA.debugLine="Sub DisableStrictMode";
 //BA.debugLineNum = 75;BA.debugLine="Dim javaobjectInstance";
_javaobjectinstance = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 76;BA.debugLine="Dim javaobjectPolicy";
_javaobjectpolicy = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 77;BA.debugLine="Dim javaobjectStrictMode";
_javaobjectstrictmode = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 79;BA.debugLine="Try";
try { //BA.debugLineNum = 80;BA.debugLine="javaobjectInstance.InitializeStatic (\"android.os";
_javaobjectinstance.InitializeStatic("android.os.Build.VERSION");
 //BA.debugLineNum = 81;BA.debugLine="If javaobjectInstance.GetField (\"SDK_INT\") > 9 T";
if ((double)(BA.ObjectToNumber(_javaobjectinstance.GetField("SDK_INT")))>9) { 
 //BA.debugLineNum = 82;BA.debugLine="javaobjectPolicy = javaobjectPolicy.InitializeN";
_javaobjectpolicy = _javaobjectpolicy.InitializeNewInstance("android.os.StrictMode.ThreadPolicy.Builder",(Object[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 83;BA.debugLine="javaobjectPolicy = javaobjectPolicy.RunMethodJO";
_javaobjectpolicy = _javaobjectpolicy.RunMethodJO("permitAll",(Object[])(anywheresoftware.b4a.keywords.Common.Null)).RunMethodJO("build",(Object[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 84;BA.debugLine="javaobjectStrictMode.InitializeStatic (\"android";
_javaobjectstrictmode.InitializeStatic("android.os.StrictMode").RunMethod("setThreadPolicy",new Object[]{(Object)(_javaobjectpolicy.getObject())});
 };
 } 
       catch (Exception e12) {
			processBA.setLastException(e12); //BA.debugLineNum = 87;BA.debugLine="Log(\"Application_Error\")";
anywheresoftware.b4a.keywords.Common.LogImpl("62228238","Application_Error",0);
 };
 //BA.debugLineNum = 90;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 7;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 11;BA.debugLine="End Sub";
return "";
}
public static String  _service_create() throws Exception{
anywheresoftware.b4j.object.JavaObject _locale = null;
anywheresoftware.b4j.object.JavaObject _jo = null;
 //BA.debugLineNum = 13;BA.debugLine="Sub Service_Create";
 //BA.debugLineNum = 19;BA.debugLine="If Not (File.Exists(File.DirInternal,\"primi.db\")";
if (anywheresoftware.b4a.keywords.Common.Not(anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"primi.db"))) { 
 //BA.debugLineNum = 20;BA.debugLine="File.Copy(File.DirAssets, \"primi.db\",File.DirInt";
anywheresoftware.b4a.keywords.Common.File.Copy(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"primi.db",anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"primi.db");
 };
 //BA.debugLineNum = 23;BA.debugLine="Dim locale As JavaObject";
_locale = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 24;BA.debugLine="locale.InitializeNewInstance(\"java.util.Locale\",";
_locale.InitializeNewInstance("java.util.Locale",new Object[]{(Object)("es")});
 //BA.debugLineNum = 25;BA.debugLine="Dim jo As JavaObject";
_jo = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 26;BA.debugLine="jo.InitializeStatic(\"java.util.Locale\").RunMethod";
_jo.InitializeStatic("java.util.Locale").RunMethod("setDefault",new Object[]{(Object)(_locale.getObject())});
 //BA.debugLineNum = 28;BA.debugLine="ActivateSSL";
_activatessl();
 //BA.debugLineNum = 29;BA.debugLine="End Sub";
return "";
}
public static String  _service_destroy() throws Exception{
 //BA.debugLineNum = 44;BA.debugLine="Sub Service_Destroy";
 //BA.debugLineNum = 46;BA.debugLine="End Sub";
return "";
}
public static String  _service_start(anywheresoftware.b4a.objects.IntentWrapper _startingintent) throws Exception{
 //BA.debugLineNum = 31;BA.debugLine="Sub Service_Start (StartingIntent As Intent)";
 //BA.debugLineNum = 32;BA.debugLine="Service.StopAutomaticForeground 'Starter service";
mostCurrent._service.StopAutomaticForeground();
 //BA.debugLineNum = 33;BA.debugLine="End Sub";
return "";
}
public static String  _service_taskremoved() throws Exception{
 //BA.debugLineNum = 35;BA.debugLine="Sub Service_TaskRemoved";
 //BA.debugLineNum = 37;BA.debugLine="End Sub";
return "";
}
}
