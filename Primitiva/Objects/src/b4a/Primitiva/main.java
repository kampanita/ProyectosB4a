package b4a.Primitiva;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;
    public static boolean dontPause;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mostCurrent = this;
		if (processBA == null) {
			processBA = new BA(this.getApplicationContext(), null, null, "b4a.Primitiva", "b4a.Primitiva.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
				p.finish();
			}
		}
        processBA.setActivityPaused(true);
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(this, processBA, wl, false))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "b4a.Primitiva", "b4a.Primitiva.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.Primitiva.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEventFromUI(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return main.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null)
            return;
        if (this != mostCurrent)
			return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        if (!dontPause)
            BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (main) Pause event (activity is not paused). **");
        if (mostCurrent != null)
            processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        if (!dontPause) {
            processBA.setActivityPaused(true);
            mostCurrent = null;
        }

        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
            main mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
            if (mc != mostCurrent)
                return;
		    processBA.raiseEvent(mc._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        for (int i = 0;i < permissions.length;i++) {
            Object[] o = new Object[] {permissions[i], grantResults[i] == 0};
            processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
        }
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.sql.SQL _sql = null;
public static anywheresoftware.b4a.objects.collections.List _nums = null;
public static anywheresoftware.b4a.objects.collections.List _lista = null;
public anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public anywheresoftware.b4a.objects.ButtonWrapper _nueva = null;
public anywheresoftware.b4a.objects.WebViewWrapper _webview1 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _guardar = null;
public anywheresoftware.b4a.objects.ButtonWrapper _verguardados = null;
public b4a.example3.customlistview _customlistview1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _l1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _l2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _l3 = null;
public anywheresoftware.b4a.objects.LabelWrapper _l4 = null;
public anywheresoftware.b4a.objects.LabelWrapper _l5 = null;
public anywheresoftware.b4a.objects.LabelWrapper _l6 = null;
public b4a.example3.customlistview _customlistview2 = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _ganadora = null;
public anywheresoftware.b4a.objects.LabelWrapper _combi = null;
public anywheresoftware.b4a.objects.LabelWrapper _acier = null;
public anywheresoftware.b4a.objects.LabelWrapper _compl = null;
public b4a.example3.customlistview _premio_grid = null;
public anywheresoftware.b4a.objects.ButtonWrapper _manual = null;
public wheelviewnewwrapper.wheelviewnewWrapper _wheelviewnew1 = null;
public wheelviewnewwrapper.wheelviewnewWrapper _wheelviewnew2 = null;
public wheelviewnewwrapper.wheelviewnewWrapper _wheelviewnew3 = null;
public wheelviewnewwrapper.wheelviewnewWrapper _wheelviewnew4 = null;
public wheelviewnewwrapper.wheelviewnewWrapper _wheelviewnew5 = null;
public wheelviewnewwrapper.wheelviewnewWrapper _wheelviewnew6 = null;
public b4a.example.dateutils _dateutils = null;
public b4a.Primitiva.starter _starter = null;
public b4a.Primitiva.httputils2service _httputils2service = null;
public b4a.Primitiva.xuiviewsutils _xuiviewsutils = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 60;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 61;BA.debugLine="Activity.LoadLayout(\"Layout2\")";
mostCurrent._activity.LoadLayout("Layout2",mostCurrent.activityBA);
 //BA.debugLineNum = 62;BA.debugLine="sql.Initialize(File.DirInternal,\"primi.db\",True)";
_sql.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"primi.db",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 63;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 64;BA.debugLine="lista.Initialize";
_lista.Initialize();
 //BA.debugLineNum = 65;BA.debugLine="cargar_guardados";
_cargar_guardados();
 };
 //BA.debugLineNum = 68;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 75;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 77;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 70;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 72;BA.debugLine="cargar_guardados";
_cargar_guardados();
 //BA.debugLineNum = 73;BA.debugLine="End Sub";
return "";
}
public static void  _borra_tmp_click() throws Exception{
ResumableSub_Borra_tmp_Click rsub = new ResumableSub_Borra_tmp_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_Borra_tmp_Click extends BA.ResumableSub {
public ResumableSub_Borra_tmp_Click(b4a.Primitiva.main parent) {
this.parent = parent;
}
b4a.Primitiva.main parent;
int _pk = 0;
b4a.Primitiva.b4xdialog _d = null;
Object _rs = null;
int _result = 0;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 498;BA.debugLine="Dim pk As Int";
_pk = 0;
 //BA.debugLineNum = 499;BA.debugLine="pk=sql.ExecQuerySingleResult(\"select count(*) as";
_pk = (int)(Double.parseDouble(parent._sql.ExecQuerySingleResult("select count(*) as cuantos from apuestas_tmp")));
 //BA.debugLineNum = 500;BA.debugLine="If pk>0 Then";
if (true) break;

case 1:
//if
this.state = 10;
if (_pk>0) { 
this.state = 3;
}else {
this.state = 9;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 501;BA.debugLine="Dim d As B4XDialog";
_d = new b4a.Primitiva.b4xdialog();
 //BA.debugLineNum = 502;BA.debugLine="d.initialize(Activity)";
_d._initialize /*String*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(parent.mostCurrent._activity.getObject())));
 //BA.debugLineNum = 503;BA.debugLine="d.Title=\"¡¡ATENCIÓN!!\"";
_d._title /*Object*/  = (Object)("¡¡ATENCIÓN!!");
 //BA.debugLineNum = 504;BA.debugLine="d.BackgroundColor=0x42F7F7F7";
_d._backgroundcolor /*int*/  = ((int)0x42f7f7f7);
 //BA.debugLineNum = 505;BA.debugLine="d.TitleBarColor=0xFF00891D";
_d._titlebarcolor /*int*/  = ((int)0xff00891d);
 //BA.debugLineNum = 506;BA.debugLine="d.BodyTextColor=Colors.white";
_d._bodytextcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 507;BA.debugLine="d.VisibleAnimationDuration=300";
_d._visibleanimationduration /*int*/  = (int) (300);
 //BA.debugLineNum = 508;BA.debugLine="Dim rs As Object";
_rs = new Object();
 //BA.debugLineNum = 509;BA.debugLine="rs=d.Show(\"Quieres borrar todos las combinacione";
_rs = _d._show /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ ((Object)("Quieres borrar todos las combinaciones temporales ¿?"),(Object)("Si"),(Object)("No"),(Object)(""));
 //BA.debugLineNum = 510;BA.debugLine="Wait For(rs) complete (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 11;
return;
case 11:
//C
this.state = 4;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 511;BA.debugLine="If Result = xui.DialogResponse_Positive Then";
if (true) break;

case 4:
//if
this.state = 7;
if (_result==parent.mostCurrent._xui.DialogResponse_Positive) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 512;BA.debugLine="sql.ExecNonQuery(\"delete from Apuestas_tmp\")";
parent._sql.ExecNonQuery("delete from Apuestas_tmp");
 //BA.debugLineNum = 513;BA.debugLine="ToastMessageShow(\"Borrados todas las combinacio";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Borrados todas las combinaciones de memoria."),anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 7:
//C
this.state = 10;
;
 //BA.debugLineNum = 515;BA.debugLine="cargar_guardados";
_cargar_guardados();
 if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 517;BA.debugLine="ToastMessageShow(\"Nada que borrar.\",True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Nada que borrar."),anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 520;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _complete(int _result) throws Exception{
}
public static void  _borrar_guardados_click() throws Exception{
ResumableSub_borrar_guardados_Click rsub = new ResumableSub_borrar_guardados_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_borrar_guardados_Click extends BA.ResumableSub {
public ResumableSub_borrar_guardados_Click(b4a.Primitiva.main parent) {
this.parent = parent;
}
b4a.Primitiva.main parent;
b4a.Primitiva.b4xdialog _d = null;
Object _rs = null;
int _result = 0;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 403;BA.debugLine="Dim d As B4XDialog";
_d = new b4a.Primitiva.b4xdialog();
 //BA.debugLineNum = 404;BA.debugLine="d.initialize(Activity)";
_d._initialize /*String*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(parent.mostCurrent._activity.getObject())));
 //BA.debugLineNum = 405;BA.debugLine="d.Title=\"¡¡ATENCIÓN!!\"";
_d._title /*Object*/  = (Object)("¡¡ATENCIÓN!!");
 //BA.debugLineNum = 406;BA.debugLine="d.BackgroundColor=0x42F7F7F7";
_d._backgroundcolor /*int*/  = ((int)0x42f7f7f7);
 //BA.debugLineNum = 407;BA.debugLine="d.TitleBarColor=0xFF00891D";
_d._titlebarcolor /*int*/  = ((int)0xff00891d);
 //BA.debugLineNum = 408;BA.debugLine="d.BodyTextColor=Colors.white";
_d._bodytextcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 409;BA.debugLine="d.VisibleAnimationDuration=300";
_d._visibleanimationduration /*int*/  = (int) (300);
 //BA.debugLineNum = 410;BA.debugLine="Dim rs As Object";
_rs = new Object();
 //BA.debugLineNum = 411;BA.debugLine="rs=d.Show(\"Quieres borrar todos las combinaciones";
_rs = _d._show /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ ((Object)("Quieres borrar todos las combinaciones guardadas ¿?"+anywheresoftware.b4a.keywords.Common.CRLF+" Se perderán los datos almacenados."+anywheresoftware.b4a.keywords.Common.CRLF+"Recuerda que puedes borrar una a una manteniendo pulsado sobre ella."),(Object)("Si"),(Object)("No"),(Object)(""));
 //BA.debugLineNum = 412;BA.debugLine="Wait For(rs) complete (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 5;
return;
case 5:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 413;BA.debugLine="If Result = xui.DialogResponse_Positive Then";
if (true) break;

case 1:
//if
this.state = 4;
if (_result==parent.mostCurrent._xui.DialogResponse_Positive) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 414;BA.debugLine="sql.ExecNonQuery(\"delete from Apuestas\")";
parent._sql.ExecNonQuery("delete from Apuestas");
 //BA.debugLineNum = 415;BA.debugLine="ToastMessageShow(\"Borrados todas las combinacion";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Borrados todas las combinaciones de memoria."),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 416;BA.debugLine="cargar_guardados";
_cargar_guardados();
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 418;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _canc_click() throws Exception{
 //BA.debugLineNum = 672;BA.debugLine="Private Sub canc_Click";
 //BA.debugLineNum = 673;BA.debugLine="Activity.LoadLayout(\"Layout2\")";
mostCurrent._activity.LoadLayout("Layout2",mostCurrent.activityBA);
 //BA.debugLineNum = 674;BA.debugLine="cargar_guardados";
_cargar_guardados();
 //BA.debugLineNum = 675;BA.debugLine="End Sub";
return "";
}
public static String  _cargar_guardados() throws Exception{
anywheresoftware.b4a.sql.SQL.ResultSetWrapper _rs = null;
anywheresoftware.b4a.objects.collections.List _items = null;
int _primary_key = 0;
int _i = 0;
 //BA.debugLineNum = 443;BA.debugLine="Sub cargar_guardados";
 //BA.debugLineNum = 445;BA.debugLine="Activity.LoadLayout(\"Layout2\")";
mostCurrent._activity.LoadLayout("Layout2",mostCurrent.activityBA);
 //BA.debugLineNum = 446;BA.debugLine="Dim rs As ResultSet";
_rs = new anywheresoftware.b4a.sql.SQL.ResultSetWrapper();
 //BA.debugLineNum = 447;BA.debugLine="rs=sql.ExecQuery(\"select * from Apuestas_tmp\")";
_rs = (anywheresoftware.b4a.sql.SQL.ResultSetWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.ResultSetWrapper(), (android.database.Cursor)(_sql.ExecQuery("select * from Apuestas_tmp")));
 //BA.debugLineNum = 448;BA.debugLine="Dim items As List";
_items = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 449;BA.debugLine="Dim primary_key As Int";
_primary_key = 0;
 //BA.debugLineNum = 450;BA.debugLine="For i=0 To rs.RowCount-1";
{
final int step6 = 1;
final int limit6 = (int) (_rs.getRowCount()-1);
_i = (int) (0) ;
for (;_i <= limit6 ;_i = _i + step6 ) {
 //BA.debugLineNum = 451;BA.debugLine="rs.Position=i";
_rs.setPosition(_i);
 //BA.debugLineNum = 452;BA.debugLine="items.initialize";
_items.Initialize();
 //BA.debugLineNum = 453;BA.debugLine="primary_key=rs.GetiNT(\"Apuesta\")";
_primary_key = _rs.GetInt("Apuesta");
 //BA.debugLineNum = 454;BA.debugLine="items.Add(rs.Getint(\"n1\"))";
_items.Add((Object)(_rs.GetInt("n1")));
 //BA.debugLineNum = 455;BA.debugLine="items.Add(rs.Getint(\"n2\"))";
_items.Add((Object)(_rs.GetInt("n2")));
 //BA.debugLineNum = 456;BA.debugLine="items.Add(rs.Getint(\"n3\"))";
_items.Add((Object)(_rs.GetInt("n3")));
 //BA.debugLineNum = 457;BA.debugLine="items.Add(rs.Getint(\"n4\"))";
_items.Add((Object)(_rs.GetInt("n4")));
 //BA.debugLineNum = 458;BA.debugLine="items.Add(rs.Getint(\"n5\"))";
_items.Add((Object)(_rs.GetInt("n5")));
 //BA.debugLineNum = 459;BA.debugLine="items.Add(rs.Getint(\"n6\"))";
_items.Add((Object)(_rs.GetInt("n6")));
 //BA.debugLineNum = 460;BA.debugLine="CustomListView1.Add(crea_row(items),primary_key)";
mostCurrent._customlistview1._add(_crea_row(_items),(Object)(_primary_key));
 }
};
 //BA.debugLineNum = 463;BA.debugLine="lista=items";
_lista = _items;
 //BA.debugLineNum = 464;BA.debugLine="If CustomListView1.size>0 Then";
if (mostCurrent._customlistview1._getsize()>0) { 
 //BA.debugLineNum = 465;BA.debugLine="CustomListView1.ScrollToItem(CustomListView1.Siz";
mostCurrent._customlistview1._scrolltoitem((int) (mostCurrent._customlistview1._getsize()-1));
 };
 //BA.debugLineNum = 468;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.keywords.Common.ResumableSubWrapper  _checkproviderinstaller() throws Exception{
ResumableSub_CheckProviderInstaller rsub = new ResumableSub_CheckProviderInstaller(null);
rsub.resume(processBA, null);
return (anywheresoftware.b4a.keywords.Common.ResumableSubWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper(), rsub);
}
public static class ResumableSub_CheckProviderInstaller extends BA.ResumableSub {
public ResumableSub_CheckProviderInstaller(b4a.Primitiva.main parent) {
this.parent = parent;
}
b4a.Primitiva.main parent;
boolean _retval = false;
anywheresoftware.b4j.object.JavaObject _jo = null;
anywheresoftware.b4j.object.JavaObject _context = null;
Object _listener = null;
String _methodname = "";
Object[] _args = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
{
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,null);return;}
case 0:
//C
this.state = 1;
 //BA.debugLineNum = 537;BA.debugLine="Dim retVal As Boolean = False";
_retval = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 538;BA.debugLine="Dim jo As JavaObject";
_jo = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 539;BA.debugLine="jo.InitializeStatic(\"com.google.android.gms.secur";
_jo.InitializeStatic("com.google.android.gms.security.ProviderInstaller");
 //BA.debugLineNum = 540;BA.debugLine="Dim context As JavaObject";
_context = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 541;BA.debugLine="context.InitializeContext";
_context.InitializeContext(processBA);
 //BA.debugLineNum = 542;BA.debugLine="Dim listener As Object = jo.CreateEventFromUI(\"co";
_listener = _jo.CreateEventFromUI(processBA,"com.google.android.gms.security.ProviderInstaller.ProviderInstallListener","listener",anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 544;BA.debugLine="Log(\"Installing security provider if needed...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("51703957","Installing security provider if needed...",0);
 //BA.debugLineNum = 545;BA.debugLine="jo.RunMethod(\"installIfNeededAsync\", Array(contex";
_jo.RunMethod("installIfNeededAsync",new Object[]{(Object)(_context.getObject()),_listener});
 //BA.debugLineNum = 546;BA.debugLine="Wait For listener_Event (MethodName As String, Ar";
anywheresoftware.b4a.keywords.Common.WaitFor("listener_event", processBA, this, null);
this.state = 7;
return;
case 7:
//C
this.state = 1;
_methodname = (String) result[0];
_args = (Object[]) result[1];
;
 //BA.debugLineNum = 547;BA.debugLine="If MethodName = \"onProviderInstalled\" Then";
if (true) break;

case 1:
//if
this.state = 6;
if ((_methodname).equals("onProviderInstalled")) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 548;BA.debugLine="Log(\"Provider installed successfully\")";
anywheresoftware.b4a.keywords.Common.LogImpl("51703961","Provider installed successfully",0);
 //BA.debugLineNum = 549;BA.debugLine="retVal = True";
_retval = anywheresoftware.b4a.keywords.Common.True;
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 551;BA.debugLine="Log(\"Error installing provider: \" & Args(0))";
anywheresoftware.b4a.keywords.Common.LogImpl("51703964","Error installing provider: "+BA.ObjectToString(_args[(int) (0)]),0);
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 553;BA.debugLine="Return retVal";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,(Object)(_retval));return;};
 //BA.debugLineNum = 554;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _listener_event(String _methodname,Object[] _args) throws Exception{
}
public static void  _comprobar_click() throws Exception{
ResumableSub_Comprobar_Click rsub = new ResumableSub_Comprobar_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_Comprobar_Click extends BA.ResumableSub {
public ResumableSub_Comprobar_Click(b4a.Primitiva.main parent) {
this.parent = parent;
}
b4a.Primitiva.main parent;
String _url = "";
String _html = "";
b4a.Primitiva.httpjob _http = null;
String _inicio = "";
String _final = "";
int _index1 = 0;
int _index2 = 0;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 215;BA.debugLine="Activity.LoadLayout(\"WebView\")";
parent.mostCurrent._activity.LoadLayout("WebView",mostCurrent.activityBA);
 //BA.debugLineNum = 216;BA.debugLine="Dim url As String";
_url = "";
 //BA.debugLineNum = 218;BA.debugLine="url=\"https://www.laprimitiva.info/\"";
_url = "https://www.laprimitiva.info/";
 //BA.debugLineNum = 220;BA.debugLine="Dim html As String";
_html = "";
 //BA.debugLineNum = 221;BA.debugLine="Dim http As HttpJob";
_http = new b4a.Primitiva.httpjob();
 //BA.debugLineNum = 222;BA.debugLine="http.Initialize(\"\",Me)";
_http._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 223;BA.debugLine="http.Download(url)";
_http._download /*String*/ (_url);
 //BA.debugLineNum = 224;BA.debugLine="Wait For (http) JobDone(http As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_http));
this.state = 5;
return;
case 5:
//C
this.state = 1;
_http = (b4a.Primitiva.httpjob) result[0];
;
 //BA.debugLineNum = 225;BA.debugLine="If http.Success Then";
if (true) break;

case 1:
//if
this.state = 4;
if (_http._success /*boolean*/ ) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 227;BA.debugLine="html=http.getString";
_html = _http._getstring /*String*/ ();
 //BA.debugLineNum = 229;BA.debugLine="Dim inicio As String =$\"<article class=\"$";
_inicio = ("<article class=");
 //BA.debugLineNum = 230;BA.debugLine="Dim final As String	=$\"</article>\"$";
_final = ("</article>");
 //BA.debugLineNum = 232;BA.debugLine="Dim index1 As Int=html.IndexOf(inicio)";
_index1 = _html.indexOf(_inicio);
 //BA.debugLineNum = 233;BA.debugLine="Dim index2 As Int=html.IndexOf(final)";
_index2 = _html.indexOf(_final);
 //BA.debugLineNum = 235;BA.debugLine="html=html.Substring2(index1,index2)";
_html = _html.substring(_index1,_index2);
 //BA.debugLineNum = 236;BA.debugLine="html=$\"<head><link rel=\"stylesheet\" href=\"https:";
_html = ("<head><link rel=\"stylesheet\" href=\"https://www.laprimitiva.info/css/master.min.css\"></head><body>")+_html+"</body>";
 //BA.debugLineNum = 238;BA.debugLine="html=RegexReplace($\"href=\\\"/loteriaprimitiva/\"$,";
_html = _regexreplace(("href=\\\"/loteriaprimitiva/"),_html,("href=\\\"https://laprimitiva.info/loteriaprimitiva/"));
 //BA.debugLineNum = 239;BA.debugLine="html=RegexReplace($\"href=\"/jugar-loteria-primiti";
_html = _regexreplace(("href=\"/jugar-loteria-primitiva.html\""),_html,("href=\\\"https://laprimitiva.info/jugar-loteria-primitiva.html\""));
 //BA.debugLineNum = 241;BA.debugLine="WebView1.LoadHtml(html)";
parent.mostCurrent._webview1.LoadHtml(_html);
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 243;BA.debugLine="http.Release";
_http._release /*String*/ ();
 //BA.debugLineNum = 245;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _jobdone(b4a.Primitiva.httpjob _http) throws Exception{
}
public static void  _comprobar2_click() throws Exception{
ResumableSub_Comprobar2_Click rsub = new ResumableSub_Comprobar2_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_Comprobar2_Click extends BA.ResumableSub {
public ResumableSub_Comprobar2_Click(b4a.Primitiva.main parent) {
this.parent = parent;
}
b4a.Primitiva.main parent;
String _url = "";
int _x = 0;
int _i = 0;
int _acertado = 0;
int _complementario = 0;
String _combinacion = "";
anywheresoftware.b4a.objects.collections.List _lacombi = null;
anywheresoftware.b4a.objects.collections.List _elacierto = null;
anywheresoftware.b4a.objects.collections.List _elcomplem = null;
String _inicio = "";
String _final = "";
int _index1 = 0;
int _index2 = 0;
anywheresoftware.b4a.keywords.Regex.MatcherWrapper _mat = null;
anywheresoftware.b4a.objects.collections.List _premiados = null;
String _html = "";
b4a.Primitiva.httpjob _http = null;
anywheresoftware.b4a.objects.collections.List _mensaje = null;
anywheresoftware.b4a.sql.SQL.ResultSetWrapper _rs = null;
String _texto = "";
String _patron = "";
int step60;
int limit60;
int step61;
int limit61;
int step67;
int limit67;
int step81;
int limit81;
int step89;
int limit89;

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
 //BA.debugLineNum = 262;BA.debugLine="Activity.LoadLayout(\"premiados\")";
parent.mostCurrent._activity.LoadLayout("premiados",mostCurrent.activityBA);
 //BA.debugLineNum = 263;BA.debugLine="Dim url As String";
_url = "";
 //BA.debugLineNum = 264;BA.debugLine="url=\"https://www.laprimitiva.info/\"";
_url = "https://www.laprimitiva.info/";
 //BA.debugLineNum = 265;BA.debugLine="Dim x As Int";
_x = 0;
 //BA.debugLineNum = 266;BA.debugLine="Dim i As Int";
_i = 0;
 //BA.debugLineNum = 267;BA.debugLine="Dim acertado As Int=0";
_acertado = (int) (0);
 //BA.debugLineNum = 268;BA.debugLine="Dim complementario As Int=0";
_complementario = (int) (0);
 //BA.debugLineNum = 269;BA.debugLine="Dim combinacion As String";
_combinacion = "";
 //BA.debugLineNum = 270;BA.debugLine="Dim lacombi As List";
_lacombi = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 271;BA.debugLine="Dim elacierto As List";
_elacierto = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 272;BA.debugLine="Dim elcomplem As List";
_elcomplem = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 273;BA.debugLine="Dim inicio As String";
_inicio = "";
 //BA.debugLineNum = 274;BA.debugLine="Dim final As String";
_final = "";
 //BA.debugLineNum = 275;BA.debugLine="Dim index1 As Int";
_index1 = 0;
 //BA.debugLineNum = 276;BA.debugLine="Dim index2 As Int";
_index2 = 0;
 //BA.debugLineNum = 277;BA.debugLine="Dim mat As Matcher";
_mat = new anywheresoftware.b4a.keywords.Regex.MatcherWrapper();
 //BA.debugLineNum = 278;BA.debugLine="Dim premiados As List";
_premiados = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 279;BA.debugLine="premiados.Initialize";
_premiados.Initialize();
 //BA.debugLineNum = 280;BA.debugLine="elacierto.Initialize";
_elacierto.Initialize();
 //BA.debugLineNum = 281;BA.debugLine="elcomplem.Initialize";
_elcomplem.Initialize();
 //BA.debugLineNum = 282;BA.debugLine="lacombi.Initialize";
_lacombi.Initialize();
 //BA.debugLineNum = 283;BA.debugLine="Dim html As String";
_html = "";
 //BA.debugLineNum = 284;BA.debugLine="Dim http As HttpJob";
_http = new b4a.Primitiva.httpjob();
 //BA.debugLineNum = 285;BA.debugLine="Dim mensaje As List";
_mensaje = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 286;BA.debugLine="mensaje.Initialize";
_mensaje.Initialize();
 //BA.debugLineNum = 287;BA.debugLine="Dim rs As ResultSet";
_rs = new anywheresoftware.b4a.sql.SQL.ResultSetWrapper();
 //BA.debugLineNum = 288;BA.debugLine="http.Initialize(\"\",Me)";
_http._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 289;BA.debugLine="Dim texto As String";
_texto = "";
 //BA.debugLineNum = 291;BA.debugLine="Try";
if (true) break;

case 1:
//try
this.state = 6;
this.catchState = 5;
this.state = 3;
if (true) break;

case 3:
//C
this.state = 6;
this.catchState = 5;
 //BA.debugLineNum = 292;BA.debugLine="http.Download(url)";
_http._download /*String*/ (_url);
 //BA.debugLineNum = 293;BA.debugLine="Wait For (http) JobDone(http As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_http));
this.state = 59;
return;
case 59:
//C
this.state = 6;
_http = (b4a.Primitiva.httpjob) result[0];
;
 if (true) break;

case 5:
//C
this.state = 6;
this.catchState = 0;
 //BA.debugLineNum = 295;BA.debugLine="Log(\"error de http\")";
anywheresoftware.b4a.keywords.Common.LogImpl("5852002","error de http",0);
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 299;BA.debugLine="If http.Success Then";

case 6:
//if
this.state = 9;
this.catchState = 0;
if (_http._success /*boolean*/ ) { 
this.state = 8;
}if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 300;BA.debugLine="html=http.getString";
_html = _http._getstring /*String*/ ();
 //BA.debugLineNum = 301;BA.debugLine="inicio =$\"<article class=\"$";
_inicio = ("<article class=");
 //BA.debugLineNum = 302;BA.debugLine="final=$\"</article>\"$";
_final = ("</article>");
 //BA.debugLineNum = 303;BA.debugLine="Dim index1 As Int=html.IndexOf(inicio)";
_index1 = _html.indexOf(_inicio);
 //BA.debugLineNum = 304;BA.debugLine="Dim index2 As Int=html.IndexOf(final)";
_index2 = _html.indexOf(_final);
 //BA.debugLineNum = 305;BA.debugLine="html=html.Substring2(index1,index2)";
_html = _html.substring(_index1,_index2);
 if (true) break;

case 9:
//C
this.state = 10;
;
 //BA.debugLineNum = 307;BA.debugLine="http.Release";
_http._release /*String*/ ();
 //BA.debugLineNum = 308;BA.debugLine="inicio=$\"<div class=\"combi\">\"$";
_inicio = ("<div class=\"combi\">");
 //BA.debugLineNum = 309;BA.debugLine="final=$\"<div class=\"sepanum\"></div>\"$";
_final = ("<div class=\"sepanum\"></div>");
 //BA.debugLineNum = 310;BA.debugLine="index1=html.IndexOf(inicio)";
_index1 = _html.indexOf(_inicio);
 //BA.debugLineNum = 311;BA.debugLine="index2=html.IndexOf(final)";
_index2 = _html.indexOf(_final);
 //BA.debugLineNum = 312;BA.debugLine="If index2>1 And index1>1 Then";
if (true) break;

case 10:
//if
this.state = 58;
if (_index2>1 && _index1>1) { 
this.state = 12;
}if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 313;BA.debugLine="html=html.Substring2(index1,index2)";
_html = _html.substring(_index1,_index2);
 //BA.debugLineNum = 315;BA.debugLine="Dim patron As String=$\"<div class=\\\"num\\\">(\\d+)<";
_patron = ("<div class=\\\"num\\\">(\\d+)<\\/div>");
 //BA.debugLineNum = 316;BA.debugLine="mat=Regex.Matcher(patron,html)";
_mat = anywheresoftware.b4a.keywords.Common.Regex.Matcher(_patron,_html);
 //BA.debugLineNum = 318;BA.debugLine="Do While mat.Find = True";
if (true) break;

case 13:
//do while
this.state = 16;
while (_mat.Find()==anywheresoftware.b4a.keywords.Common.True) {
this.state = 15;
if (true) break;
}
if (true) break;

case 15:
//C
this.state = 13;
 //BA.debugLineNum = 319;BA.debugLine="premiados.Add(mat.Group(1))";
_premiados.Add((Object)(_mat.Group((int) (1))));
 if (true) break;

case 16:
//C
this.state = 17;
;
 //BA.debugLineNum = 322;BA.debugLine="rs=sql.ExecQuery(\"select n1,n2,n3,n4,n5,n6 from";
_rs = (anywheresoftware.b4a.sql.SQL.ResultSetWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.ResultSetWrapper(), (android.database.Cursor)(parent._sql.ExecQuery("select n1,n2,n3,n4,n5,n6 from apuestas")));
 //BA.debugLineNum = 324;BA.debugLine="Do While rs.NextRow";
if (true) break;

case 17:
//do while
this.state = 43;
while (_rs.NextRow()) {
this.state = 19;
if (true) break;
}
if (true) break;

case 19:
//C
this.state = 20;
 //BA.debugLineNum = 325;BA.debugLine="acertado=0";
_acertado = (int) (0);
 //BA.debugLineNum = 326;BA.debugLine="complementario=0";
_complementario = (int) (0);
 //BA.debugLineNum = 327;BA.debugLine="combinacion=rellena(rs.GetInt(\"n1\"))&\"-\"&rellen";
_combinacion = _rellena(_rs.GetInt("n1"))+"-"+_rellena(_rs.GetInt("n2"))+"-"+_rellena(_rs.GetInt("n3"))+"-"+_rellena(_rs.GetInt("n4"))+"-"+_rellena(_rs.GetInt("n5"))+"-"+_rellena(_rs.GetInt("n6"));
 //BA.debugLineNum = 329;BA.debugLine="For i = 1 To 6";
if (true) break;

case 20:
//for
this.state = 31;
step60 = 1;
limit60 = (int) (6);
_i = (int) (1) ;
this.state = 60;
if (true) break;

case 60:
//C
this.state = 31;
if ((step60 > 0 && _i <= limit60) || (step60 < 0 && _i >= limit60)) this.state = 22;
if (true) break;

case 61:
//C
this.state = 60;
_i = ((int)(0 + _i + step60)) ;
if (true) break;

case 22:
//C
this.state = 23;
 //BA.debugLineNum = 330;BA.debugLine="For x=0 To 5";
if (true) break;

case 23:
//for
this.state = 30;
step61 = 1;
limit61 = (int) (5);
_x = (int) (0) ;
this.state = 62;
if (true) break;

case 62:
//C
this.state = 30;
if ((step61 > 0 && _x <= limit61) || (step61 < 0 && _x >= limit61)) this.state = 25;
if (true) break;

case 63:
//C
this.state = 62;
_x = ((int)(0 + _x + step61)) ;
if (true) break;

case 25:
//C
this.state = 26;
 //BA.debugLineNum = 331;BA.debugLine="If rs.getInt(\"n\"&i)=premiados.Get(x) Then";
if (true) break;

case 26:
//if
this.state = 29;
if (_rs.GetInt("n"+BA.NumberToString(_i))==(double)(BA.ObjectToNumber(_premiados.Get(_x)))) { 
this.state = 28;
}if (true) break;

case 28:
//C
this.state = 29;
 //BA.debugLineNum = 332;BA.debugLine="acertado=acertado+1";
_acertado = (int) (_acertado+1);
 if (true) break;

case 29:
//C
this.state = 63;
;
 if (true) break;
if (true) break;

case 30:
//C
this.state = 61;
;
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 336;BA.debugLine="For i=1 To 6";

case 31:
//for
this.state = 38;
step67 = 1;
limit67 = (int) (6);
_i = (int) (1) ;
this.state = 64;
if (true) break;

case 64:
//C
this.state = 38;
if ((step67 > 0 && _i <= limit67) || (step67 < 0 && _i >= limit67)) this.state = 33;
if (true) break;

case 65:
//C
this.state = 64;
_i = ((int)(0 + _i + step67)) ;
if (true) break;

case 33:
//C
this.state = 34;
 //BA.debugLineNum = 337;BA.debugLine="If rs.getInt(\"n\"&i)=premiados.Get(6) Then";
if (true) break;

case 34:
//if
this.state = 37;
if (_rs.GetInt("n"+BA.NumberToString(_i))==(double)(BA.ObjectToNumber(_premiados.Get((int) (6))))) { 
this.state = 36;
}if (true) break;

case 36:
//C
this.state = 37;
 //BA.debugLineNum = 338;BA.debugLine="complementario=complementario+1";
_complementario = (int) (_complementario+1);
 if (true) break;

case 37:
//C
this.state = 65;
;
 if (true) break;
if (true) break;

case 38:
//C
this.state = 39;
;
 //BA.debugLineNum = 341;BA.debugLine="mensaje.Add(\"[ \"&combinacion&\" ] : Acertados:\"";
_mensaje.Add((Object)("[ "+_combinacion+" ] : Acertados:"+BA.NumberToString(_acertado)+" Complementario:"+BA.NumberToString(_complementario)));
 //BA.debugLineNum = 342;BA.debugLine="lacombi.add(combinacion)";
_lacombi.Add((Object)(_combinacion));
 //BA.debugLineNum = 343;BA.debugLine="elacierto.add(acertado)";
_elacierto.Add((Object)(_acertado));
 //BA.debugLineNum = 344;BA.debugLine="elcomplem.add(complementario)";
_elcomplem.Add((Object)(_complementario));
 //BA.debugLineNum = 345;BA.debugLine="If acertado>=3 Then";
if (true) break;

case 39:
//if
this.state = 42;
if (_acertado>=3) { 
this.state = 41;
}if (true) break;

case 41:
//C
this.state = 42;
 //BA.debugLineNum = 346;BA.debugLine="MsgboxAsync(\"Tienes un premio de \"&acertado&\"";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Tienes un premio de "+BA.NumberToString(_acertado)+" Aciertos y "+BA.NumberToString(_complementario)+" Complementario con la combinacion : ["+_combinacion+"]"),BA.ObjectToCharSequence("PREMIO!!"),processBA);
 if (true) break;

case 42:
//C
this.state = 17;
;
 if (true) break;

case 43:
//C
this.state = 44;
;
 //BA.debugLineNum = 349;BA.debugLine="texto=\"\"";
_texto = "";
 //BA.debugLineNum = 350;BA.debugLine="For i=0 To 5";
if (true) break;

case 44:
//for
this.state = 53;
step81 = 1;
limit81 = (int) (5);
_i = (int) (0) ;
this.state = 66;
if (true) break;

case 66:
//C
this.state = 53;
if ((step81 > 0 && _i <= limit81) || (step81 < 0 && _i >= limit81)) this.state = 46;
if (true) break;

case 67:
//C
this.state = 66;
_i = ((int)(0 + _i + step81)) ;
if (true) break;

case 46:
//C
this.state = 47;
 //BA.debugLineNum = 351;BA.debugLine="If i=0 Then";
if (true) break;

case 47:
//if
this.state = 52;
if (_i==0) { 
this.state = 49;
}else {
this.state = 51;
}if (true) break;

case 49:
//C
this.state = 52;
 //BA.debugLineNum = 352;BA.debugLine="texto=rellena(premiados.Get(i))";
_texto = _rellena((int)(BA.ObjectToNumber(_premiados.Get(_i))));
 if (true) break;

case 51:
//C
this.state = 52;
 //BA.debugLineNum = 354;BA.debugLine="texto=texto&\"-\"&rellena(premiados.Get(i))";
_texto = _texto+"-"+_rellena((int)(BA.ObjectToNumber(_premiados.Get(_i))));
 if (true) break;

case 52:
//C
this.state = 67;
;
 if (true) break;
if (true) break;

case 53:
//C
this.state = 54;
;
 //BA.debugLineNum = 357;BA.debugLine="texto=\"[ \"&texto& \" ]   COMP:\" & rellena(premiad";
_texto = "[ "+_texto+" ]   COMP:"+_rellena((int)(BA.ObjectToNumber(_premiados.Get((int) (6)))))+" REINT:"+_rellena((int)(BA.ObjectToNumber(_premiados.Get((int) (7)))));
 //BA.debugLineNum = 358;BA.debugLine="For i = 0 To mensaje.Size-1";
if (true) break;

case 54:
//for
this.state = 57;
step89 = 1;
limit89 = (int) (_mensaje.getSize()-1);
_i = (int) (0) ;
this.state = 68;
if (true) break;

case 68:
//C
this.state = 57;
if ((step89 > 0 && _i <= limit89) || (step89 < 0 && _i >= limit89)) this.state = 56;
if (true) break;

case 69:
//C
this.state = 68;
_i = ((int)(0 + _i + step89)) ;
if (true) break;

case 56:
//C
this.state = 69;
 //BA.debugLineNum = 359;BA.debugLine="premio_grid.Add(crea_row_premiados(lacombi.Get";
parent.mostCurrent._premio_grid._add(_crea_row_premiados(BA.ObjectToString(_lacombi.Get(_i)),BA.ObjectToString(_elacierto.Get(_i)),BA.ObjectToString(_elcomplem.Get(_i))),(Object)(_i));
 if (true) break;
if (true) break;

case 57:
//C
this.state = 58;
;
 //BA.debugLineNum = 361;BA.debugLine="Ganadora.Text=texto";
parent.mostCurrent._ganadora.setText(BA.ObjectToCharSequence(_texto));
 if (true) break;

case 58:
//C
this.state = -1;
;
 //BA.debugLineNum = 364;BA.debugLine="End Sub";
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
public static anywheresoftware.b4a.objects.B4XViewWrapper  _crea_row(anywheresoftware.b4a.objects.collections.List _listac) throws Exception{
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;
anywheresoftware.b4a.objects.AnimationWrapper _a = null;
 //BA.debugLineNum = 119;BA.debugLine="Sub crea_row(listac As List) As B4XView";
 //BA.debugLineNum = 120;BA.debugLine="Dim p As B4XView= xui.CreatePanel(\"\")";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = mostCurrent._xui.CreatePanel(processBA,"");
 //BA.debugLineNum = 121;BA.debugLine="p.LoadLayout(\"ver_rows\")";
_p.LoadLayout("ver_rows",mostCurrent.activityBA);
 //BA.debugLineNum = 122;BA.debugLine="L1.text=listac.Get(0)";
mostCurrent._l1.setText(BA.ObjectToCharSequence(_listac.Get((int) (0))));
 //BA.debugLineNum = 123;BA.debugLine="L2.text=listac.Get(1)";
mostCurrent._l2.setText(BA.ObjectToCharSequence(_listac.Get((int) (1))));
 //BA.debugLineNum = 124;BA.debugLine="L3.text=listac.Get(2)";
mostCurrent._l3.setText(BA.ObjectToCharSequence(_listac.Get((int) (2))));
 //BA.debugLineNum = 125;BA.debugLine="L4.text=listac.Get(3)";
mostCurrent._l4.setText(BA.ObjectToCharSequence(_listac.Get((int) (3))));
 //BA.debugLineNum = 126;BA.debugLine="L5.text=listac.Get(4)";
mostCurrent._l5.setText(BA.ObjectToCharSequence(_listac.Get((int) (4))));
 //BA.debugLineNum = 127;BA.debugLine="L6.text=listac.Get(5)";
mostCurrent._l6.setText(BA.ObjectToCharSequence(_listac.Get((int) (5))));
 //BA.debugLineNum = 129;BA.debugLine="p.SetLayoutAnimated(100,0,0,100%x,60dip)";
_p.SetLayoutAnimated((int) (100),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 //BA.debugLineNum = 131;BA.debugLine="Dim a As Animation";
_a = new anywheresoftware.b4a.objects.AnimationWrapper();
 //BA.debugLineNum = 132;BA.debugLine="a.InitializeRotateCenter(\"\", 0, 360, L1)";
_a.InitializeRotateCenter(mostCurrent.activityBA,"",(float) (0),(float) (360),(android.view.View)(mostCurrent._l1.getObject()));
 //BA.debugLineNum = 133;BA.debugLine="a.Duration=400";
_a.setDuration((long) (400));
 //BA.debugLineNum = 134;BA.debugLine="a.RepeatCount=0";
_a.setRepeatCount((int) (0));
 //BA.debugLineNum = 136;BA.debugLine="a.Start(L1)";
_a.Start((android.view.View)(mostCurrent._l1.getObject()));
 //BA.debugLineNum = 137;BA.debugLine="a.Start(L2)";
_a.Start((android.view.View)(mostCurrent._l2.getObject()));
 //BA.debugLineNum = 138;BA.debugLine="a.Start(L3)";
_a.Start((android.view.View)(mostCurrent._l3.getObject()));
 //BA.debugLineNum = 139;BA.debugLine="a.Start(L4)";
_a.Start((android.view.View)(mostCurrent._l4.getObject()));
 //BA.debugLineNum = 140;BA.debugLine="a.Start(L5)";
_a.Start((android.view.View)(mostCurrent._l5.getObject()));
 //BA.debugLineNum = 141;BA.debugLine="a.Start(L6)";
_a.Start((android.view.View)(mostCurrent._l6.getObject()));
 //BA.debugLineNum = 160;BA.debugLine="Return p";
if (true) return _p;
 //BA.debugLineNum = 161;BA.debugLine="End Sub";
return null;
}
public static anywheresoftware.b4a.objects.B4XViewWrapper  _crea_row_premiados(String _c,String _a,String _cmp) throws Exception{
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;
 //BA.debugLineNum = 163;BA.debugLine="Sub crea_row_premiados(c As String,a As String,cmp";
 //BA.debugLineNum = 164;BA.debugLine="Dim p As B4XView= xui.CreatePanel(\"\")";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = mostCurrent._xui.CreatePanel(processBA,"");
 //BA.debugLineNum = 165;BA.debugLine="p.LoadLayout(\"row_premiados\")";
_p.LoadLayout("row_premiados",mostCurrent.activityBA);
 //BA.debugLineNum = 166;BA.debugLine="combi.Text=c";
mostCurrent._combi.setText(BA.ObjectToCharSequence(_c));
 //BA.debugLineNum = 167;BA.debugLine="acier.Text=a";
mostCurrent._acier.setText(BA.ObjectToCharSequence(_a));
 //BA.debugLineNum = 168;BA.debugLine="compl.Text=cmp";
mostCurrent._compl.setText(BA.ObjectToCharSequence(_cmp));
 //BA.debugLineNum = 170;BA.debugLine="p.SetLayoutAnimated(100,0,0,100%x,30dip)";
_p.SetLayoutAnimated((int) (100),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30)));
 //BA.debugLineNum = 172;BA.debugLine="Return p";
if (true) return _p;
 //BA.debugLineNum = 173;BA.debugLine="End Sub";
return null;
}
public static anywheresoftware.b4a.objects.collections.List  _createnumberslist(int _numberstocreate,int _minnumber,int _maxnumber,boolean _ascend) throws Exception{
anywheresoftware.b4a.objects.collections.List _results = null;
anywheresoftware.b4a.objects.collections.Map _nummap = null;
int _i = 0;
Object _o = null;
 //BA.debugLineNum = 95;BA.debugLine="Sub CreateNumbersList(NumbersToCreate As Int,MinNu";
 //BA.debugLineNum = 96;BA.debugLine="Dim Results As List";
_results = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 97;BA.debugLine="Results.Initialize";
_results.Initialize();
 //BA.debugLineNum = 99;BA.debugLine="If MaxNumber - MinNumber < NumbersToCreate Then R";
if (_maxnumber-_minnumber<_numberstocreate) { 
if (true) return _results;};
 //BA.debugLineNum = 101;BA.debugLine="Dim NumMap As Map";
_nummap = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 103;BA.debugLine="NumMap.Initialize";
_nummap.Initialize();
 //BA.debugLineNum = 104;BA.debugLine="Dim i As Int";
_i = 0;
 //BA.debugLineNum = 105;BA.debugLine="For i = MinNumber To MaxNumber";
{
final int step7 = 1;
final int limit7 = _maxnumber;
_i = _minnumber ;
for (;_i <= limit7 ;_i = _i + step7 ) {
 //BA.debugLineNum = 106;BA.debugLine="NumMap.Put(i,i)";
_nummap.Put((Object)(_i),(Object)(_i));
 }
};
 //BA.debugLineNum = 109;BA.debugLine="Do While Results.Size < NumbersToCreate";
while (_results.getSize()<_numberstocreate) {
 //BA.debugLineNum = 110;BA.debugLine="Dim O As Object = NumMap.Remove(Rnd(MinNumber, M";
_o = _nummap.Remove((Object)(anywheresoftware.b4a.keywords.Common.Rnd(_minnumber,(int) (_maxnumber+1))));
 //BA.debugLineNum = 111;BA.debugLine="If O <> Null Then Results.Add(O)";
if (_o!= null) { 
_results.Add(_o);};
 }
;
 //BA.debugLineNum = 114;BA.debugLine="Results.Sort(Ascend)";
_results.Sort(_ascend);
 //BA.debugLineNum = 116;BA.debugLine="Return Results";
if (true) return _results;
 //BA.debugLineNum = 117;BA.debugLine="End Sub";
return null;
}
public static void  _customlistview1_itemlongclick(int _index,Object _value) throws Exception{
ResumableSub_CustomListView1_ItemLongClick rsub = new ResumableSub_CustomListView1_ItemLongClick(null,_index,_value);
rsub.resume(processBA, null);
}
public static class ResumableSub_CustomListView1_ItemLongClick extends BA.ResumableSub {
public ResumableSub_CustomListView1_ItemLongClick(b4a.Primitiva.main parent,int _index,Object _value) {
this.parent = parent;
this._index = _index;
this._value = _value;
}
b4a.Primitiva.main parent;
int _index;
Object _value;
b4a.Primitiva.b4xdialog _d = null;
Object _rs = null;
int _result = 0;
int _primary_key = 0;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 699;BA.debugLine="If lista.size>0 Then";
if (true) break;

case 1:
//if
this.state = 8;
if (parent._lista.getSize()>0) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 700;BA.debugLine="Dim d As B4XDialog";
_d = new b4a.Primitiva.b4xdialog();
 //BA.debugLineNum = 701;BA.debugLine="d.initialize(Activity)";
_d._initialize /*String*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(parent.mostCurrent._activity.getObject())));
 //BA.debugLineNum = 702;BA.debugLine="d.Title=\"Borrando combinación\"";
_d._title /*Object*/  = (Object)("Borrando combinación");
 //BA.debugLineNum = 703;BA.debugLine="d.BackgroundColor=0x42F7F7F7";
_d._backgroundcolor /*int*/  = ((int)0x42f7f7f7);
 //BA.debugLineNum = 704;BA.debugLine="d.TitleBarColor=0xFF00891D";
_d._titlebarcolor /*int*/  = ((int)0xff00891d);
 //BA.debugLineNum = 705;BA.debugLine="d.BodyTextColor=Colors.white";
_d._bodytextcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 706;BA.debugLine="d.VisibleAnimationDuration=300";
_d._visibleanimationduration /*int*/  = (int) (300);
 //BA.debugLineNum = 707;BA.debugLine="Dim rs As Object";
_rs = new Object();
 //BA.debugLineNum = 708;BA.debugLine="rs=d.Show(\"Quieres borrar la combinacion ¿?\"&CRL";
_rs = _d._show /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ ((Object)("Quieres borrar la combinacion ¿?"+anywheresoftware.b4a.keywords.Common.CRLF+" Solo se eliminará esta combinación."),(Object)("Si"),(Object)("No"),(Object)(""));
 //BA.debugLineNum = 709;BA.debugLine="Wait For(rs) complete (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 9;
return;
case 9:
//C
this.state = 4;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 710;BA.debugLine="If Result = xui.DialogResponse_Positive Then";
if (true) break;

case 4:
//if
this.state = 7;
if (_result==parent.mostCurrent._xui.DialogResponse_Positive) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 711;BA.debugLine="Dim primary_key As Int";
_primary_key = 0;
 //BA.debugLineNum = 712;BA.debugLine="primary_key=Value";
_primary_key = (int)(BA.ObjectToNumber(_value));
 //BA.debugLineNum = 713;BA.debugLine="CustomListView1.RemoveAt(Index)";
parent.mostCurrent._customlistview1._removeat(_index);
 //BA.debugLineNum = 714;BA.debugLine="sql.ExecNonQuery(\"delete from Apuestas_tmp wher";
parent._sql.ExecNonQuery("delete from Apuestas_tmp where Apuesta='"+BA.NumberToString(_primary_key)+"'");
 //BA.debugLineNum = 715;BA.debugLine="ToastMessageShow(\"Borrada la combinación\",True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Borrada la combinación"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 716;BA.debugLine="cargar_guardados";
_cargar_guardados();
 if (true) break;

case 7:
//C
this.state = 8;
;
 if (true) break;

case 8:
//C
this.state = -1;
;
 //BA.debugLineNum = 720;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _customlistview2_itemlongclick(int _index,Object _value) throws Exception{
ResumableSub_CustomListView2_ItemLongClick rsub = new ResumableSub_CustomListView2_ItemLongClick(null,_index,_value);
rsub.resume(processBA, null);
}
public static class ResumableSub_CustomListView2_ItemLongClick extends BA.ResumableSub {
public ResumableSub_CustomListView2_ItemLongClick(b4a.Primitiva.main parent,int _index,Object _value) {
this.parent = parent;
this._index = _index;
this._value = _value;
}
b4a.Primitiva.main parent;
int _index;
Object _value;
b4a.Primitiva.b4xdialog _d = null;
Object _rs = null;
int _result = 0;
int _primary_key = 0;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 367;BA.debugLine="Dim d As B4XDialog";
_d = new b4a.Primitiva.b4xdialog();
 //BA.debugLineNum = 368;BA.debugLine="d.initialize(Activity)";
_d._initialize /*String*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(parent.mostCurrent._activity.getObject())));
 //BA.debugLineNum = 369;BA.debugLine="d.Title=\"Borrando combinación\"";
_d._title /*Object*/  = (Object)("Borrando combinación");
 //BA.debugLineNum = 370;BA.debugLine="d.BackgroundColor=0x42F7F7F7";
_d._backgroundcolor /*int*/  = ((int)0x42f7f7f7);
 //BA.debugLineNum = 371;BA.debugLine="d.TitleBarColor=0xFF00891D";
_d._titlebarcolor /*int*/  = ((int)0xff00891d);
 //BA.debugLineNum = 372;BA.debugLine="d.BodyTextColor=Colors.white";
_d._bodytextcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 373;BA.debugLine="d.VisibleAnimationDuration=300";
_d._visibleanimationduration /*int*/  = (int) (300);
 //BA.debugLineNum = 374;BA.debugLine="Dim rs As Object";
_rs = new Object();
 //BA.debugLineNum = 375;BA.debugLine="rs=d.Show(\"Quieres borrar la combinacion ¿?\"&CRLF";
_rs = _d._show /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ ((Object)("Quieres borrar la combinacion ¿?"+anywheresoftware.b4a.keywords.Common.CRLF+" Solo se eliminará esta combinación."),(Object)("Si"),(Object)("No"),(Object)(""));
 //BA.debugLineNum = 376;BA.debugLine="Wait For(rs) complete (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 5;
return;
case 5:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 377;BA.debugLine="If Result = xui.DialogResponse_Positive Then";
if (true) break;

case 1:
//if
this.state = 4;
if (_result==parent.mostCurrent._xui.DialogResponse_Positive) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 378;BA.debugLine="Dim primary_key As Int";
_primary_key = 0;
 //BA.debugLineNum = 379;BA.debugLine="primary_key=Value";
_primary_key = (int)(BA.ObjectToNumber(_value));
 //BA.debugLineNum = 380;BA.debugLine="CustomListView2.RemoveAt(Index)";
parent.mostCurrent._customlistview2._removeat(_index);
 //BA.debugLineNum = 381;BA.debugLine="sql.ExecNonQuery(\"delete from Apuestas where Apu";
parent._sql.ExecNonQuery("delete from Apuestas where Apuesta='"+BA.NumberToString(_primary_key)+"'");
 //BA.debugLineNum = 382;BA.debugLine="ToastMessageShow(\"Borrada la combinación\",True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Borrada la combinación"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 383;BA.debugLine="VerGuardados_Click";
_verguardados_click();
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 385;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 25;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 27;BA.debugLine="Private xui As XUI";
mostCurrent._xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 28;BA.debugLine="Private Nueva As Button";
mostCurrent._nueva = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Private WebView1 As WebView";
mostCurrent._webview1 = new anywheresoftware.b4a.objects.WebViewWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Private Guardar As Button";
mostCurrent._guardar = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Private VerGuardados As Button";
mostCurrent._verguardados = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Private CustomListView1 As CustomListView";
mostCurrent._customlistview1 = new b4a.example3.customlistview();
 //BA.debugLineNum = 34;BA.debugLine="Private L1 As Label";
mostCurrent._l1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 35;BA.debugLine="Private L2 As Label";
mostCurrent._l2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 36;BA.debugLine="Private L3 As Label";
mostCurrent._l3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 37;BA.debugLine="Private L4 As Label";
mostCurrent._l4 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Private L5 As Label";
mostCurrent._l5 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Private L6 As Label";
mostCurrent._l6 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 40;BA.debugLine="Dim lista As List";
_lista = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 42;BA.debugLine="Private CustomListView2 As CustomListView";
mostCurrent._customlistview2 = new b4a.example3.customlistview();
 //BA.debugLineNum = 43;BA.debugLine="Private Panel1 As Panel";
mostCurrent._panel1 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 45;BA.debugLine="Private Ganadora As Label";
mostCurrent._ganadora = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 46;BA.debugLine="Private combi As Label";
mostCurrent._combi = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 47;BA.debugLine="Private acier As Label";
mostCurrent._acier = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 48;BA.debugLine="Private compl As Label";
mostCurrent._compl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 49;BA.debugLine="Private premio_grid As CustomListView";
mostCurrent._premio_grid = new b4a.example3.customlistview();
 //BA.debugLineNum = 50;BA.debugLine="Private manual As Button";
mostCurrent._manual = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 51;BA.debugLine="Private WheelViewNew1 As WheelViewNew";
mostCurrent._wheelviewnew1 = new wheelviewnewwrapper.wheelviewnewWrapper();
 //BA.debugLineNum = 52;BA.debugLine="Private WheelViewNew2 As WheelViewNew";
mostCurrent._wheelviewnew2 = new wheelviewnewwrapper.wheelviewnewWrapper();
 //BA.debugLineNum = 53;BA.debugLine="Private WheelViewNew3 As WheelViewNew";
mostCurrent._wheelviewnew3 = new wheelviewnewwrapper.wheelviewnewWrapper();
 //BA.debugLineNum = 54;BA.debugLine="Private WheelViewNew4 As WheelViewNew";
mostCurrent._wheelviewnew4 = new wheelviewnewwrapper.wheelviewnewWrapper();
 //BA.debugLineNum = 55;BA.debugLine="Private WheelViewNew5 As WheelViewNew";
mostCurrent._wheelviewnew5 = new wheelviewnewwrapper.wheelviewnewWrapper();
 //BA.debugLineNum = 56;BA.debugLine="Private WheelViewNew6 As WheelViewNew";
mostCurrent._wheelviewnew6 = new wheelviewnewwrapper.wheelviewnewWrapper();
 //BA.debugLineNum = 58;BA.debugLine="End Sub";
return "";
}
public static String  _guardar_click() throws Exception{
anywheresoftware.b4a.sql.SQL.ResultSetWrapper _rs = null;
 //BA.debugLineNum = 175;BA.debugLine="Private Sub Guardar_Click";
 //BA.debugLineNum = 176;BA.debugLine="Dim rs As ResultSet";
_rs = new anywheresoftware.b4a.sql.SQL.ResultSetWrapper();
 //BA.debugLineNum = 177;BA.debugLine="rs=sql.ExecQuery(\"select count(*) from Apuestas_";
_rs = (anywheresoftware.b4a.sql.SQL.ResultSetWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.ResultSetWrapper(), (android.database.Cursor)(_sql.ExecQuery("select count(*) from Apuestas_tmp")));
 //BA.debugLineNum = 178;BA.debugLine="If rs.RowCount>0 Then";
if (_rs.getRowCount()>0) { 
 //BA.debugLineNum = 179;BA.debugLine="rs=sql.ExecQuery(\"select fecha,n1,n2,n3,n4,n5,n6";
_rs = (anywheresoftware.b4a.sql.SQL.ResultSetWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.ResultSetWrapper(), (android.database.Cursor)(_sql.ExecQuery("select fecha,n1,n2,n3,n4,n5,n6 from Apuestas_tmp")));
 //BA.debugLineNum = 180;BA.debugLine="Do While rs.NextRow";
while (_rs.NextRow()) {
 //BA.debugLineNum = 181;BA.debugLine="sql.ExecNonQuery(\"insert into Apuestas (fecha,n";
_sql.ExecNonQuery("insert into Apuestas (fecha,n1,n2,n3,n4,n5,n6) values ('"+_rs.GetString("Fecha")+"','"+BA.NumberToString(_rs.GetInt("n1"))+"','"+BA.NumberToString(_rs.GetInt("n2"))+"','"+BA.NumberToString(_rs.GetInt("n3"))+"','"+BA.NumberToString(_rs.GetInt("n4"))+"','"+BA.NumberToString(_rs.GetInt("n5"))+"','"+BA.NumberToString(_rs.GetInt("n6"))+"')");
 }
;
 //BA.debugLineNum = 183;BA.debugLine="sql.ExecNonQuery(\"delete from Apuestas_tmp\")";
_sql.ExecNonQuery("delete from Apuestas_tmp");
 //BA.debugLineNum = 184;BA.debugLine="CustomListView1.Clear";
mostCurrent._customlistview1._clear();
 //BA.debugLineNum = 185;BA.debugLine="ToastMessageShow(\"Guardada la combinacion en la";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Guardada la combinacion en la BBDD"),anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 187;BA.debugLine="ToastMessageShow(\"Nada que guardar\",True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Nada que guardar"),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 189;BA.debugLine="End Sub";
return "";
}
public static String  _guardar_solo_1_click() throws Exception{
anywheresoftware.b4a.sql.SQL.ResultSetWrapper _rs = null;
 //BA.debugLineNum = 420;BA.debugLine="Private Sub guardar_solo_1_Click";
 //BA.debugLineNum = 421;BA.debugLine="Dim rs As ResultSet";
_rs = new anywheresoftware.b4a.sql.SQL.ResultSetWrapper();
 //BA.debugLineNum = 422;BA.debugLine="rs=sql.ExecQuery(\"select count(*) num from apuest";
_rs = (anywheresoftware.b4a.sql.SQL.ResultSetWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.ResultSetWrapper(), (android.database.Cursor)(_sql.ExecQuery("select count(*) num from apuestas where n1='8' and n2='22' and n3='26' and n4='31' and n5='46' and n6='49'")));
 //BA.debugLineNum = 423;BA.debugLine="If rs.RowCount>0 Then";
if (_rs.getRowCount()>0) { 
 //BA.debugLineNum = 424;BA.debugLine="rs.Position=0";
_rs.setPosition((int) (0));
 //BA.debugLineNum = 425;BA.debugLine="If rs.getint(\"num\")=0 Then";
if (_rs.GetInt("num")==0) { 
 //BA.debugLineNum = 426;BA.debugLine="sql.ExecNonQuery2(\"insert into Apuestas (fecha,";
_sql.ExecNonQuery2("insert into Apuestas (fecha,n1,n2,n3,n4,n5,n6) values (?,'8','22','26','31','46','49')",anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())}));
 //BA.debugLineNum = 427;BA.debugLine="sql.ExecNonQuery2(\"insert into Apuestas (fecha,";
_sql.ExecNonQuery2("insert into Apuestas (fecha,n1,n2,n3,n4,n5,n6) values (?,'9','25','35','38','40','44')",anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())}));
 //BA.debugLineNum = 428;BA.debugLine="sql.ExecNonQuery2(\"insert into Apuestas (fecha,";
_sql.ExecNonQuery2("insert into Apuestas (fecha,n1,n2,n3,n4,n5,n6) values (?,'7','12','13','18','30','33')",anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())}));
 //BA.debugLineNum = 429;BA.debugLine="sql.ExecNonQuery2(\"insert into Apuestas (fecha,";
_sql.ExecNonQuery2("insert into Apuestas (fecha,n1,n2,n3,n4,n5,n6) values (?,'1','9','12','14','44','48')",anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())}));
 //BA.debugLineNum = 430;BA.debugLine="sql.ExecNonQuery2(\"insert into Apuestas (fecha,";
_sql.ExecNonQuery2("insert into Apuestas (fecha,n1,n2,n3,n4,n5,n6) values (?,'1','19','23','30','39','43')",anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())}));
 //BA.debugLineNum = 431;BA.debugLine="sql.ExecNonQuery2(\"insert into Apuestas (fecha,";
_sql.ExecNonQuery2("insert into Apuestas (fecha,n1,n2,n3,n4,n5,n6) values (?,'20','22','23','31','36','40')",anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())}));
 //BA.debugLineNum = 433;BA.debugLine="ToastMessageShow(\"Guardada la combinacion ESPEC";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Guardada la combinacion ESPECIAL NOE en la BBDD"),anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 436;BA.debugLine="ToastMessageShow(\"Combinacion ESPECIAL NOE ya e";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Combinacion ESPECIAL NOE ya está guardada"),anywheresoftware.b4a.keywords.Common.True);
 };
 };
 //BA.debugLineNum = 439;BA.debugLine="CustomListView1.Clear";
mostCurrent._customlistview1._clear();
 //BA.debugLineNum = 440;BA.debugLine="cargar_guardados";
_cargar_guardados();
 //BA.debugLineNum = 441;BA.debugLine="End Sub";
return "";
}
public static String  _manual_click() throws Exception{
int _a = 0;
 //BA.debugLineNum = 558;BA.debugLine="Private Sub manual_Click";
 //BA.debugLineNum = 559;BA.debugLine="Activity.LoadLayout(\"manual\")";
mostCurrent._activity.LoadLayout("manual",mostCurrent.activityBA);
 //BA.debugLineNum = 560;BA.debugLine="Dim a As Int";
_a = 0;
 //BA.debugLineNum = 561;BA.debugLine="nums.initialize";
_nums.Initialize();
 //BA.debugLineNum = 562;BA.debugLine="For a = 1 To 49";
{
final int step4 = 1;
final int limit4 = (int) (49);
_a = (int) (1) ;
for (;_a <= limit4 ;_a = _a + step4 ) {
 //BA.debugLineNum = 563;BA.debugLine="nums.Add(a)";
_nums.Add((Object)(_a));
 }
};
 //BA.debugLineNum = 565;BA.debugLine="WheelViewNew1.addData(nums)";
mostCurrent._wheelviewnew1.addData((java.util.List)(_nums.getObject()));
 //BA.debugLineNum = 566;BA.debugLine="WheelViewNew1.Skin = WheelViewNew1.SKIN_TYPE_HOLO";
mostCurrent._wheelviewnew1.setSkin(mostCurrent._wheelviewnew1.SKIN_TYPE_HOLO);
 //BA.debugLineNum = 567;BA.debugLine="WheelViewNew1.WheelClickable = True";
mostCurrent._wheelviewnew1.setWheelClickable(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 568;BA.debugLine="WheelViewNew1.WheelLoop = True";
mostCurrent._wheelviewnew1.setWheelLoop(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 569;BA.debugLine="WheelViewNew2.addData(nums)";
mostCurrent._wheelviewnew2.addData((java.util.List)(_nums.getObject()));
 //BA.debugLineNum = 570;BA.debugLine="WheelViewNew2.Skin = WheelViewNew1.SKIN_TYPE_HOLO";
mostCurrent._wheelviewnew2.setSkin(mostCurrent._wheelviewnew1.SKIN_TYPE_HOLO);
 //BA.debugLineNum = 571;BA.debugLine="WheelViewNew2.WheelClickable = True";
mostCurrent._wheelviewnew2.setWheelClickable(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 572;BA.debugLine="WheelViewNew2.WheelLoop = True";
mostCurrent._wheelviewnew2.setWheelLoop(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 573;BA.debugLine="WheelViewNew3.addData(nums)";
mostCurrent._wheelviewnew3.addData((java.util.List)(_nums.getObject()));
 //BA.debugLineNum = 574;BA.debugLine="WheelViewNew3.Skin = WheelViewNew1.SKIN_TYPE_HOLO";
mostCurrent._wheelviewnew3.setSkin(mostCurrent._wheelviewnew1.SKIN_TYPE_HOLO);
 //BA.debugLineNum = 575;BA.debugLine="WheelViewNew3.WheelClickable = True";
mostCurrent._wheelviewnew3.setWheelClickable(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 576;BA.debugLine="WheelViewNew3.WheelLoop = True";
mostCurrent._wheelviewnew3.setWheelLoop(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 577;BA.debugLine="WheelViewNew4.addData(nums)";
mostCurrent._wheelviewnew4.addData((java.util.List)(_nums.getObject()));
 //BA.debugLineNum = 578;BA.debugLine="WheelViewNew4.Skin = WheelViewNew1.SKIN_TYPE_HOLO";
mostCurrent._wheelviewnew4.setSkin(mostCurrent._wheelviewnew1.SKIN_TYPE_HOLO);
 //BA.debugLineNum = 579;BA.debugLine="WheelViewNew4.WheelClickable = True";
mostCurrent._wheelviewnew4.setWheelClickable(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 580;BA.debugLine="WheelViewNew4.WheelLoop = True";
mostCurrent._wheelviewnew4.setWheelLoop(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 581;BA.debugLine="WheelViewNew5.addData(nums)";
mostCurrent._wheelviewnew5.addData((java.util.List)(_nums.getObject()));
 //BA.debugLineNum = 582;BA.debugLine="WheelViewNew5.Skin = WheelViewNew1.SKIN_TYPE_HOLO";
mostCurrent._wheelviewnew5.setSkin(mostCurrent._wheelviewnew1.SKIN_TYPE_HOLO);
 //BA.debugLineNum = 583;BA.debugLine="WheelViewNew5.WheelClickable = True";
mostCurrent._wheelviewnew5.setWheelClickable(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 584;BA.debugLine="WheelViewNew5.WheelLoop = True";
mostCurrent._wheelviewnew5.setWheelLoop(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 585;BA.debugLine="WheelViewNew6.addData(nums)";
mostCurrent._wheelviewnew6.addData((java.util.List)(_nums.getObject()));
 //BA.debugLineNum = 586;BA.debugLine="WheelViewNew6.Skin = WheelViewNew1.SKIN_TYPE_HOLO";
mostCurrent._wheelviewnew6.setSkin(mostCurrent._wheelviewnew1.SKIN_TYPE_HOLO);
 //BA.debugLineNum = 587;BA.debugLine="WheelViewNew6.WheelClickable = True";
mostCurrent._wheelviewnew6.setWheelClickable(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 588;BA.debugLine="WheelViewNew6.WheelLoop = True";
mostCurrent._wheelviewnew6.setWheelLoop(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 589;BA.debugLine="WheelViewNew1.SelectedTextColor=0xFF196000";
mostCurrent._wheelviewnew1.setSelectedTextColor(((int)0xff196000));
 //BA.debugLineNum = 590;BA.debugLine="WheelViewNew1.NONSelectedTextColor=Colors.Black";
mostCurrent._wheelviewnew1.setNonSelectedTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 591;BA.debugLine="WheelViewNew2.SelectedTextColor=0xFF196000";
mostCurrent._wheelviewnew2.setSelectedTextColor(((int)0xff196000));
 //BA.debugLineNum = 592;BA.debugLine="WheelViewNew2.NONSelectedTextColor=Colors.Black";
mostCurrent._wheelviewnew2.setNonSelectedTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 593;BA.debugLine="WheelViewNew3.SelectedTextColor=0xFF196000";
mostCurrent._wheelviewnew3.setSelectedTextColor(((int)0xff196000));
 //BA.debugLineNum = 594;BA.debugLine="WheelViewNew3.NONSelectedTextColor=Colors.Black";
mostCurrent._wheelviewnew3.setNonSelectedTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 595;BA.debugLine="WheelViewNew4.SelectedTextColor=0xFF196000";
mostCurrent._wheelviewnew4.setSelectedTextColor(((int)0xff196000));
 //BA.debugLineNum = 596;BA.debugLine="WheelViewNew4.NONSelectedTextColor=Colors.Black";
mostCurrent._wheelviewnew4.setNonSelectedTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 597;BA.debugLine="WheelViewNew5.SelectedTextColor=0xFF196000";
mostCurrent._wheelviewnew5.setSelectedTextColor(((int)0xff196000));
 //BA.debugLineNum = 598;BA.debugLine="WheelViewNew5.NONSelectedTextColor=Colors.Black";
mostCurrent._wheelviewnew5.setNonSelectedTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 599;BA.debugLine="WheelViewNew6.SelectedTextColor=0xFF196000";
mostCurrent._wheelviewnew6.setSelectedTextColor(((int)0xff196000));
 //BA.debugLineNum = 600;BA.debugLine="WheelViewNew6.NONSelectedTextColor=Colors.Black";
mostCurrent._wheelviewnew6.setNonSelectedTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 601;BA.debugLine="WheelViewNew1.WheelBackgroundColor=0x7FDBE7D4";
mostCurrent._wheelviewnew1.setWheelBackgroundColor(((int)0x7fdbe7d4));
 //BA.debugLineNum = 602;BA.debugLine="WheelViewNew2.WheelBackgroundColor=0x7FDBE7D4";
mostCurrent._wheelviewnew2.setWheelBackgroundColor(((int)0x7fdbe7d4));
 //BA.debugLineNum = 603;BA.debugLine="WheelViewNew3.WheelBackgroundColor=0x7FDBE7D4";
mostCurrent._wheelviewnew3.setWheelBackgroundColor(((int)0x7fdbe7d4));
 //BA.debugLineNum = 604;BA.debugLine="WheelViewNew4.WheelBackgroundColor=0x7FDBE7D4";
mostCurrent._wheelviewnew4.setWheelBackgroundColor(((int)0x7fdbe7d4));
 //BA.debugLineNum = 605;BA.debugLine="WheelViewNew5.WheelBackgroundColor=0x7FDBE7D4";
mostCurrent._wheelviewnew5.setWheelBackgroundColor(((int)0x7fdbe7d4));
 //BA.debugLineNum = 606;BA.debugLine="WheelViewNew6.WheelBackgroundColor=0x7FDBE7D4";
mostCurrent._wheelviewnew6.setWheelBackgroundColor(((int)0x7fdbe7d4));
 //BA.debugLineNum = 609;BA.debugLine="WheelViewNew2.Color=Colors.Magenta";
mostCurrent._wheelviewnew2.setColor(anywheresoftware.b4a.keywords.Common.Colors.Magenta);
 //BA.debugLineNum = 611;BA.debugLine="WheelViewNew1.Tag=-1";
mostCurrent._wheelviewnew1.setTag((Object)(-1));
 //BA.debugLineNum = 612;BA.debugLine="WheelViewNew2.Tag=-1";
mostCurrent._wheelviewnew2.setTag((Object)(-1));
 //BA.debugLineNum = 613;BA.debugLine="WheelViewNew3.Tag=-1";
mostCurrent._wheelviewnew3.setTag((Object)(-1));
 //BA.debugLineNum = 614;BA.debugLine="WheelViewNew4.Tag=-1";
mostCurrent._wheelviewnew4.setTag((Object)(-1));
 //BA.debugLineNum = 615;BA.debugLine="WheelViewNew5.Tag=-1";
mostCurrent._wheelviewnew5.setTag((Object)(-1));
 //BA.debugLineNum = 616;BA.debugLine="WheelViewNew6.Tag=-1";
mostCurrent._wheelviewnew6.setTag((Object)(-1));
 //BA.debugLineNum = 617;BA.debugLine="WheelViewNew1.applyStyle";
mostCurrent._wheelviewnew1.applyStyle();
 //BA.debugLineNum = 618;BA.debugLine="WheelViewNew2.applyStyle";
mostCurrent._wheelviewnew2.applyStyle();
 //BA.debugLineNum = 619;BA.debugLine="WheelViewNew3.applyStyle";
mostCurrent._wheelviewnew3.applyStyle();
 //BA.debugLineNum = 620;BA.debugLine="WheelViewNew4.applyStyle";
mostCurrent._wheelviewnew4.applyStyle();
 //BA.debugLineNum = 621;BA.debugLine="WheelViewNew5.applyStyle";
mostCurrent._wheelviewnew5.applyStyle();
 //BA.debugLineNum = 622;BA.debugLine="WheelViewNew6.applyStyle";
mostCurrent._wheelviewnew6.applyStyle();
 //BA.debugLineNum = 627;BA.debugLine="End Sub";
return "";
}
public static String  _nueva_click() throws Exception{
int _pk = 0;
 //BA.debugLineNum = 79;BA.debugLine="Private Sub Nueva_Click";
 //BA.debugLineNum = 81;BA.debugLine="lista=CreateNumbersList(6,1,49,True)";
_lista = _createnumberslist((int) (6),(int) (1),(int) (49),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 83;BA.debugLine="Dim pk As Int";
_pk = 0;
 //BA.debugLineNum = 84;BA.debugLine="pk=sql.ExecQuerySingleResult(\"select count(*) as";
_pk = (int)(Double.parseDouble(_sql.ExecQuerySingleResult("select count(*) as cuantos from apuestas_tmp")));
 //BA.debugLineNum = 85;BA.debugLine="pk=pk+1";
_pk = (int) (_pk+1);
 //BA.debugLineNum = 86;BA.debugLine="CustomListView1.Add(crea_row(lista),pk)";
mostCurrent._customlistview1._add(_crea_row(_lista),(Object)(_pk));
 //BA.debugLineNum = 87;BA.debugLine="CustomListView1.ScrollToItem(CustomListView1.Size";
mostCurrent._customlistview1._scrolltoitem((int) (mostCurrent._customlistview1._getsize()-1));
 //BA.debugLineNum = 89;BA.debugLine="If lista.Size>0 Then";
if (_lista.getSize()>0) { 
 //BA.debugLineNum = 90;BA.debugLine="sql.ExecNonQuery(\"insert into Apuestas_tmp (apue";
_sql.ExecNonQuery("insert into Apuestas_tmp (apuesta,fecha,n1,n2,n3,n4,n5,n6) values ('"+BA.NumberToString(_pk)+"','"+anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+"','"+BA.ObjectToString(_lista.Get((int) (0)))+"','"+BA.ObjectToString(_lista.Get((int) (1)))+"','"+BA.ObjectToString(_lista.Get((int) (2)))+"','"+BA.ObjectToString(_lista.Get((int) (3)))+"','"+BA.ObjectToString(_lista.Get((int) (4)))+"','"+BA.ObjectToString(_lista.Get((int) (5)))+"')");
 };
 //BA.debugLineNum = 93;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        b4a.example.dateutils._process_globals();
main._process_globals();
starter._process_globals();
httputils2service._process_globals();
xuiviewsutils._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 17;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 20;BA.debugLine="Dim sql As SQL";
_sql = new anywheresoftware.b4a.sql.SQL();
 //BA.debugLineNum = 21;BA.debugLine="Dim nums As List";
_nums = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 22;BA.debugLine="Dim lista As List";
_lista = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 23;BA.debugLine="End Sub";
return "";
}
public static String  _regexreplace(String _pattern,String _text,String _replacement) throws Exception{
anywheresoftware.b4a.keywords.Regex.MatcherWrapper _m = null;
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
 //BA.debugLineNum = 470;BA.debugLine="Sub RegexReplace(Pattern As String, Text As String";
 //BA.debugLineNum = 471;BA.debugLine="Dim m As Matcher";
_m = new anywheresoftware.b4a.keywords.Regex.MatcherWrapper();
 //BA.debugLineNum = 472;BA.debugLine="m = Regex.Matcher(Pattern, Text)";
_m = anywheresoftware.b4a.keywords.Common.Regex.Matcher(_pattern,_text);
 //BA.debugLineNum = 473;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 474;BA.debugLine="r.Target = m";
_r.Target = (Object)(_m.getObject());
 //BA.debugLineNum = 475;BA.debugLine="Return r.RunMethod2(\"replaceAll\", Replacement, \"j";
if (true) return BA.ObjectToString(_r.RunMethod2("replaceAll",_replacement,"java.lang.String"));
 //BA.debugLineNum = 476;BA.debugLine="End Sub";
return "";
}
public static String  _rellena(int _dat) throws Exception{
 //BA.debugLineNum = 478;BA.debugLine="Sub rellena(dat As Int) As String";
 //BA.debugLineNum = 479;BA.debugLine="If dat<9 Then";
if (_dat<9) { 
 //BA.debugLineNum = 480;BA.debugLine="Return \"0\"&dat";
if (true) return "0"+BA.NumberToString(_dat);
 }else {
 //BA.debugLineNum = 482;BA.debugLine="Return dat";
if (true) return BA.NumberToString(_dat);
 };
 //BA.debugLineNum = 484;BA.debugLine="End Sub";
return "";
}
public static String  _resultados_click() throws Exception{
 //BA.debugLineNum = 387;BA.debugLine="Private Sub Resultados_Click";
 //BA.debugLineNum = 388;BA.debugLine="Activity.LoadLayout(\"WebView\")";
mostCurrent._activity.LoadLayout("WebView",mostCurrent.activityBA);
 //BA.debugLineNum = 389;BA.debugLine="End Sub";
return "";
}
public static String  _trytofix() throws Exception{
anywheresoftware.b4j.object.JavaObject _jo = null;
anywheresoftware.b4j.object.JavaObject _context = null;
Object _listener = null;
 //BA.debugLineNum = 247;BA.debugLine="Public Sub TryToFix";
 //BA.debugLineNum = 248;BA.debugLine="Dim jo As JavaObject";
_jo = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 249;BA.debugLine="jo.InitializeStatic(\"com.google.android.gms.secur";
_jo.InitializeStatic("com.google.android.gms.security.ProviderInstaller");
 //BA.debugLineNum = 250;BA.debugLine="Dim context As JavaObject";
_context = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 251;BA.debugLine="context.InitializeContext";
_context.InitializeContext(processBA);
 //BA.debugLineNum = 253;BA.debugLine="Starter.DisableStrictMode";
mostCurrent._starter._disablestrictmode /*String*/ ();
 //BA.debugLineNum = 255;BA.debugLine="Dim listener As Object = jo.CreateEventFromUI(\"co";
_listener = _jo.CreateEventFromUI(processBA,"com.google.android.gms.security.ProviderInstaller.ProviderInstallListener","ProviderInstall",anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 257;BA.debugLine="Log(\"Installing security provider if needed...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("5786442","Installing security provider if needed...",0);
 //BA.debugLineNum = 258;BA.debugLine="jo.RunMethod(\"installIfNeededAsync\", Array(contex";
_jo.RunMethod("installIfNeededAsync",new Object[]{(Object)(_context.getObject()),_listener});
 //BA.debugLineNum = 259;BA.debugLine="End Sub";
return "";
}
public static String  _validar_click() throws Exception{
anywheresoftware.b4a.objects.collections.List _lista2 = null;
anywheresoftware.b4a.objects.collections.List _lista_final = null;
int _a = 0;
anywheresoftware.b4a.objects.AnimationWrapper _b = null;
int _pk = 0;
 //BA.debugLineNum = 630;BA.debugLine="Private Sub Validar_Click";
 //BA.debugLineNum = 632;BA.debugLine="Dim lista2 As List";
_lista2 = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 633;BA.debugLine="Dim lista_final As List";
_lista_final = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 634;BA.debugLine="lista_final.Initialize";
_lista_final.Initialize();
 //BA.debugLineNum = 635;BA.debugLine="lista2.Initialize2(Array As Int(WheelViewNew1.Tag";
_lista2.Initialize2(anywheresoftware.b4a.keywords.Common.ArrayToList(new int[]{(int)(BA.ObjectToNumber(mostCurrent._wheelviewnew1.getTag())),(int)(BA.ObjectToNumber(mostCurrent._wheelviewnew2.getTag())),(int)(BA.ObjectToNumber(mostCurrent._wheelviewnew3.getTag())),(int)(BA.ObjectToNumber(mostCurrent._wheelviewnew4.getTag())),(int)(BA.ObjectToNumber(mostCurrent._wheelviewnew5.getTag())),(int)(BA.ObjectToNumber(mostCurrent._wheelviewnew6.getTag()))}));
 //BA.debugLineNum = 636;BA.debugLine="lista2.Sort(True)";
_lista2.Sort(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 637;BA.debugLine="Dim a As Int";
_a = 0;
 //BA.debugLineNum = 638;BA.debugLine="For a=0 To 5";
{
final int step7 = 1;
final int limit7 = (int) (5);
_a = (int) (0) ;
for (;_a <= limit7 ;_a = _a + step7 ) {
 //BA.debugLineNum = 639;BA.debugLine="If 0>lista2.Get(a) Then";
if (0>(double)(BA.ObjectToNumber(_lista2.Get(_a)))) { 
 //BA.debugLineNum = 640;BA.debugLine="ToastMessageShow(\"Combinación inválida\",True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Combinación inválida"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 641;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 643;BA.debugLine="If lista_final.IndexOf(lista2.Get(a))=-1 Then";
if (_lista_final.IndexOf(_lista2.Get(_a))==-1) { 
 //BA.debugLineNum = 644;BA.debugLine="lista_final.Add(lista2.Get(a))";
_lista_final.Add(_lista2.Get(_a));
 };
 }
};
 //BA.debugLineNum = 647;BA.debugLine="If lista_final.Size<6 Then";
if (_lista_final.getSize()<6) { 
 //BA.debugLineNum = 648;BA.debugLine="ToastMessageShow(\"Combinación inválida\",True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Combinación inválida"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 649;BA.debugLine="Dim b As Animation";
_b = new anywheresoftware.b4a.objects.AnimationWrapper();
 //BA.debugLineNum = 650;BA.debugLine="b.InitializeRotateCenter(\"\", 0, 360, WheelViewNe";
_b.InitializeRotateCenter(mostCurrent.activityBA,"",(float) (0),(float) (360),(android.view.View)(mostCurrent._wheelviewnew1.getObject()));
 //BA.debugLineNum = 651;BA.debugLine="b.Start(WheelViewNew1)";
_b.Start((android.view.View)(mostCurrent._wheelviewnew1.getObject()));
 //BA.debugLineNum = 652;BA.debugLine="b.Start(WheelViewNew2)";
_b.Start((android.view.View)(mostCurrent._wheelviewnew2.getObject()));
 //BA.debugLineNum = 653;BA.debugLine="b.Start(WheelViewNew3)";
_b.Start((android.view.View)(mostCurrent._wheelviewnew3.getObject()));
 //BA.debugLineNum = 654;BA.debugLine="b.Start(WheelViewNew4)";
_b.Start((android.view.View)(mostCurrent._wheelviewnew4.getObject()));
 //BA.debugLineNum = 655;BA.debugLine="b.Start(WheelViewNew5)";
_b.Start((android.view.View)(mostCurrent._wheelviewnew5.getObject()));
 //BA.debugLineNum = 656;BA.debugLine="b.Start(WheelViewNew6)";
_b.Start((android.view.View)(mostCurrent._wheelviewnew6.getObject()));
 //BA.debugLineNum = 657;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 659;BA.debugLine="Dim pk As Int";
_pk = 0;
 //BA.debugLineNum = 660;BA.debugLine="pk=sql.ExecQuerySingleResult(\"select count(*) as";
_pk = (int)(Double.parseDouble(_sql.ExecQuerySingleResult("select count(*) as cuantos from apuestas_tmp")));
 //BA.debugLineNum = 661;BA.debugLine="pk=pk+1";
_pk = (int) (_pk+1);
 //BA.debugLineNum = 662;BA.debugLine="CustomListView1.Add(crea_row(lista2),pk)";
mostCurrent._customlistview1._add(_crea_row(_lista2),(Object)(_pk));
 //BA.debugLineNum = 663;BA.debugLine="CustomListView1.ScrollToItem(CustomListView1.Size";
mostCurrent._customlistview1._scrolltoitem((int) (mostCurrent._customlistview1._getsize()-1));
 //BA.debugLineNum = 665;BA.debugLine="If lista2.Size>0 Then";
if (_lista2.getSize()>0) { 
 //BA.debugLineNum = 666;BA.debugLine="sql.ExecNonQuery(\"insert into Apuestas_tmp (apue";
_sql.ExecNonQuery("insert into Apuestas_tmp (apuesta,fecha,n1,n2,n3,n4,n5,n6) values ('"+BA.NumberToString(_pk)+"','"+anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+"','"+BA.ObjectToString(_lista2.Get((int) (0)))+"','"+BA.ObjectToString(_lista2.Get((int) (1)))+"','"+BA.ObjectToString(_lista2.Get((int) (2)))+"','"+BA.ObjectToString(_lista2.Get((int) (3)))+"','"+BA.ObjectToString(_lista2.Get((int) (4)))+"','"+BA.ObjectToString(_lista2.Get((int) (5)))+"')");
 };
 //BA.debugLineNum = 668;BA.debugLine="Activity.LoadLayout(\"Layout2\")";
mostCurrent._activity.LoadLayout("Layout2",mostCurrent.activityBA);
 //BA.debugLineNum = 669;BA.debugLine="cargar_guardados";
_cargar_guardados();
 //BA.debugLineNum = 670;BA.debugLine="End Sub";
return "";
}
public static String  _verguardados_click() throws Exception{
anywheresoftware.b4a.sql.SQL.ResultSetWrapper _rs = null;
anywheresoftware.b4a.objects.collections.List _items = null;
int _primary_key = 0;
int _i = 0;
 //BA.debugLineNum = 191;BA.debugLine="Private Sub VerGuardados_Click";
 //BA.debugLineNum = 192;BA.debugLine="Activity.LoadLayout(\"Layout\")";
mostCurrent._activity.LoadLayout("Layout",mostCurrent.activityBA);
 //BA.debugLineNum = 193;BA.debugLine="Dim rs As ResultSet";
_rs = new anywheresoftware.b4a.sql.SQL.ResultSetWrapper();
 //BA.debugLineNum = 194;BA.debugLine="rs=sql.ExecQuery(\"select * from Apuestas\")";
_rs = (anywheresoftware.b4a.sql.SQL.ResultSetWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.ResultSetWrapper(), (android.database.Cursor)(_sql.ExecQuery("select * from Apuestas")));
 //BA.debugLineNum = 195;BA.debugLine="Dim items As List";
_items = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 196;BA.debugLine="Dim primary_key As Int";
_primary_key = 0;
 //BA.debugLineNum = 197;BA.debugLine="For i=0 To rs.RowCount-1";
{
final int step6 = 1;
final int limit6 = (int) (_rs.getRowCount()-1);
_i = (int) (0) ;
for (;_i <= limit6 ;_i = _i + step6 ) {
 //BA.debugLineNum = 198;BA.debugLine="rs.Position=i";
_rs.setPosition(_i);
 //BA.debugLineNum = 199;BA.debugLine="items.initialize";
_items.Initialize();
 //BA.debugLineNum = 200;BA.debugLine="primary_key=rs.GetiNT(\"Apuesta\")";
_primary_key = _rs.GetInt("Apuesta");
 //BA.debugLineNum = 201;BA.debugLine="items.Add(rs.Getint(\"n1\"))";
_items.Add((Object)(_rs.GetInt("n1")));
 //BA.debugLineNum = 202;BA.debugLine="items.Add(rs.Getint(\"n2\"))";
_items.Add((Object)(_rs.GetInt("n2")));
 //BA.debugLineNum = 203;BA.debugLine="items.Add(rs.Getint(\"n3\"))";
_items.Add((Object)(_rs.GetInt("n3")));
 //BA.debugLineNum = 204;BA.debugLine="items.Add(rs.Getint(\"n4\"))";
_items.Add((Object)(_rs.GetInt("n4")));
 //BA.debugLineNum = 205;BA.debugLine="items.Add(rs.Getint(\"n5\"))";
_items.Add((Object)(_rs.GetInt("n5")));
 //BA.debugLineNum = 206;BA.debugLine="items.Add(rs.Getint(\"n6\"))";
_items.Add((Object)(_rs.GetInt("n6")));
 //BA.debugLineNum = 207;BA.debugLine="CustomListView2.Add(crea_row(items),primary_key)";
mostCurrent._customlistview2._add(_crea_row(_items),(Object)(_primary_key));
 }
};
 //BA.debugLineNum = 209;BA.debugLine="If CustomListView2.size>0 Then";
if (mostCurrent._customlistview2._getsize()>0) { 
 //BA.debugLineNum = 210;BA.debugLine="CustomListView2.ScrollToItem(CustomListView2.Siz";
mostCurrent._customlistview2._scrolltoitem((int) (mostCurrent._customlistview2._getsize()-1));
 };
 //BA.debugLineNum = 212;BA.debugLine="End Sub";
return "";
}
public static String  _volver_click() throws Exception{
 //BA.debugLineNum = 391;BA.debugLine="Private Sub volver_Click";
 //BA.debugLineNum = 392;BA.debugLine="Activity.LoadLayout(\"Layout2\")";
mostCurrent._activity.LoadLayout("Layout2",mostCurrent.activityBA);
 //BA.debugLineNum = 393;BA.debugLine="cargar_guardados";
_cargar_guardados();
 //BA.debugLineNum = 395;BA.debugLine="End Sub";
return "";
}
public static String  _volver2_click() throws Exception{
 //BA.debugLineNum = 397;BA.debugLine="Private Sub Volver2_Click";
 //BA.debugLineNum = 398;BA.debugLine="Activity.LoadLayout(\"layout\")";
mostCurrent._activity.LoadLayout("layout",mostCurrent.activityBA);
 //BA.debugLineNum = 399;BA.debugLine="VerGuardados_Click";
_verguardados_click();
 //BA.debugLineNum = 400;BA.debugLine="End Sub";
return "";
}
public static String  _volver3_click() throws Exception{
 //BA.debugLineNum = 486;BA.debugLine="Private Sub Volver3_Click";
 //BA.debugLineNum = 487;BA.debugLine="Activity.LoadLayout(\"layout\")";
mostCurrent._activity.LoadLayout("layout",mostCurrent.activityBA);
 //BA.debugLineNum = 488;BA.debugLine="VerGuardados_Click";
_verguardados_click();
 //BA.debugLineNum = 489;BA.debugLine="End Sub";
return "";
}
public static String  _volver4_click() throws Exception{
 //BA.debugLineNum = 491;BA.debugLine="Private Sub volver4_Click";
 //BA.debugLineNum = 492;BA.debugLine="Activity.LoadLayout(\"layout\")";
mostCurrent._activity.LoadLayout("layout",mostCurrent.activityBA);
 //BA.debugLineNum = 493;BA.debugLine="VerGuardados_Click";
_verguardados_click();
 //BA.debugLineNum = 494;BA.debugLine="End Sub";
return "";
}
public static String  _wheelviewnew1_item_selected(int _position) throws Exception{
 //BA.debugLineNum = 677;BA.debugLine="Private Sub WheelViewNew1_item_selected(position A";
 //BA.debugLineNum = 678;BA.debugLine="WheelViewNew1.Tag=position+1";
mostCurrent._wheelviewnew1.setTag((Object)(_position+1));
 //BA.debugLineNum = 679;BA.debugLine="End Sub";
return "";
}
public static String  _wheelviewnew2_item_selected(int _position) throws Exception{
 //BA.debugLineNum = 680;BA.debugLine="Private Sub WheelViewNew2_item_selected(position A";
 //BA.debugLineNum = 681;BA.debugLine="WheelViewNew2.Tag=position+1";
mostCurrent._wheelviewnew2.setTag((Object)(_position+1));
 //BA.debugLineNum = 682;BA.debugLine="End Sub";
return "";
}
public static String  _wheelviewnew3_item_selected(int _position) throws Exception{
 //BA.debugLineNum = 683;BA.debugLine="Private Sub WheelViewNew3_item_selected(position A";
 //BA.debugLineNum = 684;BA.debugLine="WheelViewNew3.Tag=position+1";
mostCurrent._wheelviewnew3.setTag((Object)(_position+1));
 //BA.debugLineNum = 685;BA.debugLine="End Sub";
return "";
}
public static String  _wheelviewnew4_item_selected(int _position) throws Exception{
 //BA.debugLineNum = 686;BA.debugLine="Private Sub WheelViewNew4_item_selected(position A";
 //BA.debugLineNum = 687;BA.debugLine="WheelViewNew4.Tag=position+1";
mostCurrent._wheelviewnew4.setTag((Object)(_position+1));
 //BA.debugLineNum = 688;BA.debugLine="End Sub";
return "";
}
public static String  _wheelviewnew5_item_selected(int _position) throws Exception{
 //BA.debugLineNum = 689;BA.debugLine="Private Sub WheelViewNew5_item_selected(position A";
 //BA.debugLineNum = 690;BA.debugLine="WheelViewNew5.Tag=position+1";
mostCurrent._wheelviewnew5.setTag((Object)(_position+1));
 //BA.debugLineNum = 691;BA.debugLine="End Sub";
return "";
}
public static String  _wheelviewnew6_item_selected(int _position) throws Exception{
 //BA.debugLineNum = 692;BA.debugLine="Private Sub WheelViewNew6_item_selected(position A";
 //BA.debugLineNum = 693;BA.debugLine="WheelViewNew6.Tag=position+1";
mostCurrent._wheelviewnew6.setTag((Object)(_position+1));
 //BA.debugLineNum = 695;BA.debugLine="End Sub";
return "";
}
}
