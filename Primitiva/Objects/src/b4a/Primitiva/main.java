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
public anywheresoftware.b4a.objects.collections.List _lista = null;
public b4a.example3.customlistview _customlistview2 = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _ganadora = null;
public anywheresoftware.b4a.objects.LabelWrapper _combi = null;
public anywheresoftware.b4a.objects.LabelWrapper _acier = null;
public anywheresoftware.b4a.objects.LabelWrapper _compl = null;
public b4a.example3.customlistview _premio_grid = null;
public b4a.example.dateutils _dateutils = null;
public b4a.Primitiva.starter _starter = null;
public b4a.Primitiva.httputils2service _httputils2service = null;
public b4a.Primitiva.xuiviewsutils _xuiviewsutils = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 52;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 53;BA.debugLine="Activity.LoadLayout(\"Layout2\")";
mostCurrent._activity.LoadLayout("Layout2",mostCurrent.activityBA);
 //BA.debugLineNum = 54;BA.debugLine="sql.Initialize(File.DirInternal,\"primi.db\",True)";
_sql.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"primi.db",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 55;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 56;BA.debugLine="lista.Initialize";
mostCurrent._lista.Initialize();
 //BA.debugLineNum = 57;BA.debugLine="cargar_guardados";
_cargar_guardados();
 };
 //BA.debugLineNum = 60;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 69;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 71;BA.debugLine="End Sub";
return "";
}
public static void  _activity_resume() throws Exception{
ResumableSub_Activity_Resume rsub = new ResumableSub_Activity_Resume(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_Activity_Resume extends BA.ResumableSub {
public ResumableSub_Activity_Resume(b4a.Primitiva.main parent) {
this.parent = parent;
}
b4a.Primitiva.main parent;
boolean _success = false;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 64;BA.debugLine="Wait For (CheckProviderInstaller) complete (succe";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _checkproviderinstaller());
this.state = 1;
return;
case 1:
//C
this.state = -1;
_success = (Boolean) result[0];
;
 //BA.debugLineNum = 65;BA.debugLine="cargar_guardados";
_cargar_guardados();
 //BA.debugLineNum = 67;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _complete(boolean _success) throws Exception{
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
 //BA.debugLineNum = 509;BA.debugLine="If lista.Size>0 Then";
if (true) break;

case 1:
//if
this.state = 8;
if (parent.mostCurrent._lista.getSize()>0) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 510;BA.debugLine="Dim d As B4XDialog";
_d = new b4a.Primitiva.b4xdialog();
 //BA.debugLineNum = 511;BA.debugLine="d.initialize(Activity)";
_d._initialize /*String*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(parent.mostCurrent._activity.getObject())));
 //BA.debugLineNum = 512;BA.debugLine="d.Title=\"¡¡ATENCIÓN!!\"";
_d._title /*Object*/  = (Object)("¡¡ATENCIÓN!!");
 //BA.debugLineNum = 513;BA.debugLine="d.BackgroundColor=Colors.White";
_d._backgroundcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 514;BA.debugLine="d.BodyTextColor=Colors.Blue";
_d._bodytextcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Blue;
 //BA.debugLineNum = 515;BA.debugLine="d.VisibleAnimationDuration=300";
_d._visibleanimationduration /*int*/  = (int) (300);
 //BA.debugLineNum = 516;BA.debugLine="Dim rs As Object";
_rs = new Object();
 //BA.debugLineNum = 517;BA.debugLine="rs=d.Show(\"Quieres borrar todos las combinaciones";
_rs = _d._show /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ ((Object)("Quieres borrar todos las combinaciones temporales ¿?"),(Object)("Si"),(Object)("No"),(Object)(""));
 //BA.debugLineNum = 518;BA.debugLine="Wait For(rs) complete (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 9;
return;
case 9:
//C
this.state = 4;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 519;BA.debugLine="If Result = xui.DialogResponse_Positive Then";
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
 //BA.debugLineNum = 520;BA.debugLine="sql.ExecNonQuery(\"delete from Apuestas_tmp\")";
parent._sql.ExecNonQuery("delete from Apuestas_tmp");
 //BA.debugLineNum = 521;BA.debugLine="ToastMessageShow(\"Borrados todas las combinacion";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Borrados todas las combinaciones de memoria."),anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 7:
//C
this.state = 8;
;
 //BA.debugLineNum = 523;BA.debugLine="cargar_guardados";
_cargar_guardados();
 if (true) break;

case 8:
//C
this.state = -1;
;
 //BA.debugLineNum = 525;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
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
 //BA.debugLineNum = 406;BA.debugLine="Dim d As B4XDialog";
_d = new b4a.Primitiva.b4xdialog();
 //BA.debugLineNum = 407;BA.debugLine="d.initialize(Activity)";
_d._initialize /*String*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(parent.mostCurrent._activity.getObject())));
 //BA.debugLineNum = 408;BA.debugLine="d.Title=\"¡¡ATENCIÓN!!\"";
_d._title /*Object*/  = (Object)("¡¡ATENCIÓN!!");
 //BA.debugLineNum = 409;BA.debugLine="d.BackgroundColor=Colors.White";
_d._backgroundcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 410;BA.debugLine="d.BodyTextColor=Colors.Blue";
_d._bodytextcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Blue;
 //BA.debugLineNum = 411;BA.debugLine="d.VisibleAnimationDuration=300";
_d._visibleanimationduration /*int*/  = (int) (300);
 //BA.debugLineNum = 412;BA.debugLine="Dim rs As Object";
_rs = new Object();
 //BA.debugLineNum = 413;BA.debugLine="rs=d.Show(\"Quieres borrar todos las combinaciones";
_rs = _d._show /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ ((Object)("Quieres borrar todos las combinaciones guardadas ¿?"+anywheresoftware.b4a.keywords.Common.CRLF+" Se perderán los datos almacenados."+anywheresoftware.b4a.keywords.Common.CRLF+"Recuerda que puedes borrar una a una manteniendo pulsado sobre ella."),(Object)("Si"),(Object)("No"),(Object)(""));
 //BA.debugLineNum = 414;BA.debugLine="Wait For(rs) complete (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 5;
return;
case 5:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 415;BA.debugLine="If Result = xui.DialogResponse_Positive Then";
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
 //BA.debugLineNum = 416;BA.debugLine="sql.ExecNonQuery(\"delete from Apuestas\")";
parent._sql.ExecNonQuery("delete from Apuestas");
 //BA.debugLineNum = 417;BA.debugLine="ToastMessageShow(\"Borrados todas las combinacion";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Borrados todas las combinaciones de memoria."),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 418;BA.debugLine="cargar_guardados";
_cargar_guardados();
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 420;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _cargar_guardados() throws Exception{
anywheresoftware.b4a.sql.SQL.ResultSetWrapper _rs = null;
anywheresoftware.b4a.objects.collections.List _items = null;
int _primary_key = 0;
int _i = 0;
 //BA.debugLineNum = 452;BA.debugLine="Sub cargar_guardados";
 //BA.debugLineNum = 454;BA.debugLine="Activity.LoadLayout(\"Layout2\")";
mostCurrent._activity.LoadLayout("Layout2",mostCurrent.activityBA);
 //BA.debugLineNum = 455;BA.debugLine="Dim rs As ResultSet";
_rs = new anywheresoftware.b4a.sql.SQL.ResultSetWrapper();
 //BA.debugLineNum = 456;BA.debugLine="rs=sql.ExecQuery(\"select * from Apuestas_tmp\")";
_rs = (anywheresoftware.b4a.sql.SQL.ResultSetWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.ResultSetWrapper(), (android.database.Cursor)(_sql.ExecQuery("select * from Apuestas_tmp")));
 //BA.debugLineNum = 457;BA.debugLine="Dim items As List";
_items = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 458;BA.debugLine="Dim primary_key As Int";
_primary_key = 0;
 //BA.debugLineNum = 459;BA.debugLine="For i=0 To rs.RowCount-1";
{
final int step6 = 1;
final int limit6 = (int) (_rs.getRowCount()-1);
_i = (int) (0) ;
for (;_i <= limit6 ;_i = _i + step6 ) {
 //BA.debugLineNum = 460;BA.debugLine="rs.Position=i";
_rs.setPosition(_i);
 //BA.debugLineNum = 461;BA.debugLine="items.initialize";
_items.Initialize();
 //BA.debugLineNum = 462;BA.debugLine="primary_key=rs.GetiNT(\"Apuesta\")";
_primary_key = _rs.GetInt("Apuesta");
 //BA.debugLineNum = 464;BA.debugLine="items.Add(rs.Getint(\"n1\"))";
_items.Add((Object)(_rs.GetInt("n1")));
 //BA.debugLineNum = 465;BA.debugLine="items.Add(rs.Getint(\"n2\"))";
_items.Add((Object)(_rs.GetInt("n2")));
 //BA.debugLineNum = 466;BA.debugLine="items.Add(rs.Getint(\"n3\"))";
_items.Add((Object)(_rs.GetInt("n3")));
 //BA.debugLineNum = 467;BA.debugLine="items.Add(rs.Getint(\"n4\"))";
_items.Add((Object)(_rs.GetInt("n4")));
 //BA.debugLineNum = 468;BA.debugLine="items.Add(rs.Getint(\"n5\"))";
_items.Add((Object)(_rs.GetInt("n5")));
 //BA.debugLineNum = 469;BA.debugLine="items.Add(rs.Getint(\"n6\"))";
_items.Add((Object)(_rs.GetInt("n6")));
 //BA.debugLineNum = 470;BA.debugLine="CustomListView1.Add(crea_row(items),primary_key)";
mostCurrent._customlistview1._add(_crea_row(_items),(Object)(_primary_key));
 }
};
 //BA.debugLineNum = 473;BA.debugLine="lista=items";
mostCurrent._lista = _items;
 //BA.debugLineNum = 474;BA.debugLine="If CustomListView1.size>0 Then";
if (mostCurrent._customlistview1._getsize()>0) { 
 //BA.debugLineNum = 475;BA.debugLine="CustomListView1.ScrollToItem(CustomListView1.Siz";
mostCurrent._customlistview1._scrolltoitem((int) (mostCurrent._customlistview1._getsize()-1));
 };
 //BA.debugLineNum = 478;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 542;BA.debugLine="Dim retVal As Boolean = False";
_retval = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 543;BA.debugLine="Dim jo As JavaObject";
_jo = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 544;BA.debugLine="jo.InitializeStatic(\"com.google.android.gms.secur";
_jo.InitializeStatic("com.google.android.gms.security.ProviderInstaller");
 //BA.debugLineNum = 545;BA.debugLine="Dim context As JavaObject";
_context = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 546;BA.debugLine="context.InitializeContext";
_context.InitializeContext(processBA);
 //BA.debugLineNum = 547;BA.debugLine="Dim listener As Object = jo.CreateEventFromUI(\"co";
_listener = _jo.CreateEventFromUI(processBA,"com.google.android.gms.security.ProviderInstaller.ProviderInstallListener","listener",anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 549;BA.debugLine="Log(\"Installing security provider if needed...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("61703957","Installing security provider if needed...",0);
 //BA.debugLineNum = 550;BA.debugLine="jo.RunMethod(\"installIfNeededAsync\", Array(contex";
_jo.RunMethod("installIfNeededAsync",new Object[]{(Object)(_context.getObject()),_listener});
 //BA.debugLineNum = 551;BA.debugLine="Wait For listener_Event (MethodName As String, Ar";
anywheresoftware.b4a.keywords.Common.WaitFor("listener_event", processBA, this, null);
this.state = 7;
return;
case 7:
//C
this.state = 1;
_methodname = (String) result[0];
_args = (Object[]) result[1];
;
 //BA.debugLineNum = 552;BA.debugLine="If MethodName = \"onProviderInstalled\" Then";
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
 //BA.debugLineNum = 553;BA.debugLine="Log(\"Provider installed successfully\")";
anywheresoftware.b4a.keywords.Common.LogImpl("61703961","Provider installed successfully",0);
 //BA.debugLineNum = 554;BA.debugLine="retVal = True";
_retval = anywheresoftware.b4a.keywords.Common.True;
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 556;BA.debugLine="Log(\"Error installing provider: \" & Args(0))";
anywheresoftware.b4a.keywords.Common.LogImpl("61703964","Error installing provider: "+BA.ObjectToString(_args[(int) (0)]),0);
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 558;BA.debugLine="Return retVal";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,(Object)(_retval));return;};
 //BA.debugLineNum = 559;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 214;BA.debugLine="Activity.LoadLayout(\"WebView\")";
parent.mostCurrent._activity.LoadLayout("WebView",mostCurrent.activityBA);
 //BA.debugLineNum = 215;BA.debugLine="Dim url As String";
_url = "";
 //BA.debugLineNum = 216;BA.debugLine="TryToFix";
_trytofix();
 //BA.debugLineNum = 217;BA.debugLine="url=\"https://www.laprimitiva.info/\"";
_url = "https://www.laprimitiva.info/";
 //BA.debugLineNum = 219;BA.debugLine="Dim html As String";
_html = "";
 //BA.debugLineNum = 220;BA.debugLine="Dim http As HttpJob";
_http = new b4a.Primitiva.httpjob();
 //BA.debugLineNum = 221;BA.debugLine="http.Initialize(\"\",Me)";
_http._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 222;BA.debugLine="http.Download(url)";
_http._download /*String*/ (_url);
 //BA.debugLineNum = 223;BA.debugLine="Wait For (http) JobDone(http As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_http));
this.state = 5;
return;
case 5:
//C
this.state = 1;
_http = (b4a.Primitiva.httpjob) result[0];
;
 //BA.debugLineNum = 224;BA.debugLine="If http.Success Then";
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
 //BA.debugLineNum = 226;BA.debugLine="html=http.getString";
_html = _http._getstring /*String*/ ();
 //BA.debugLineNum = 228;BA.debugLine="Dim inicio As String =$\"<article class=\"$";
_inicio = ("<article class=");
 //BA.debugLineNum = 229;BA.debugLine="Dim final As String	=$\"</article>\"$";
_final = ("</article>");
 //BA.debugLineNum = 231;BA.debugLine="Dim index1 As Int=html.IndexOf(inicio)";
_index1 = _html.indexOf(_inicio);
 //BA.debugLineNum = 232;BA.debugLine="Dim index2 As Int=html.IndexOf(final)";
_index2 = _html.indexOf(_final);
 //BA.debugLineNum = 234;BA.debugLine="html=html.Substring2(index1,index2)";
_html = _html.substring(_index1,_index2);
 //BA.debugLineNum = 235;BA.debugLine="html=$\"<head><link rel=\"stylesheet\" href=\"https:";
_html = ("<head><link rel=\"stylesheet\" href=\"https://www.laprimitiva.info/css/master.min.css\"></head><body>")+_html+"</body>";
 //BA.debugLineNum = 237;BA.debugLine="html=RegexReplace($\"href=\\\"/loteriaprimitiva/\"$,";
_html = _regexreplace(("href=\\\"/loteriaprimitiva/"),_html,("href=\\\"https://laprimitiva.info/loteriaprimitiva/"));
 //BA.debugLineNum = 238;BA.debugLine="html=RegexReplace($\"href=\"/jugar-loteria-primiti";
_html = _regexreplace(("href=\"/jugar-loteria-primitiva.html\""),_html,("href=\\\"https://laprimitiva.info/jugar-loteria-primitiva.html\""));
 //BA.debugLineNum = 240;BA.debugLine="WebView1.LoadHtml(html)";
parent.mostCurrent._webview1.LoadHtml(_html);
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 242;BA.debugLine="http.Release";
_http._release /*String*/ ();
 //BA.debugLineNum = 244;BA.debugLine="End Sub";
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
int step55;
int limit55;
int step56;
int limit56;
int step62;
int limit62;
int step76;
int limit76;
int step84;
int limit84;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 259;BA.debugLine="Activity.LoadLayout(\"premiados\")";
parent.mostCurrent._activity.LoadLayout("premiados",mostCurrent.activityBA);
 //BA.debugLineNum = 260;BA.debugLine="Dim url As String";
_url = "";
 //BA.debugLineNum = 261;BA.debugLine="url=\"https://www.laprimitiva.info/\"";
_url = "https://www.laprimitiva.info/";
 //BA.debugLineNum = 262;BA.debugLine="Dim x As Int";
_x = 0;
 //BA.debugLineNum = 263;BA.debugLine="Dim i As Int";
_i = 0;
 //BA.debugLineNum = 264;BA.debugLine="Dim acertado As Int=0";
_acertado = (int) (0);
 //BA.debugLineNum = 265;BA.debugLine="Dim complementario As Int=0";
_complementario = (int) (0);
 //BA.debugLineNum = 266;BA.debugLine="Dim combinacion As String";
_combinacion = "";
 //BA.debugLineNum = 267;BA.debugLine="Dim lacombi As List";
_lacombi = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 268;BA.debugLine="Dim elacierto As List";
_elacierto = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 269;BA.debugLine="Dim elcomplem As List";
_elcomplem = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 270;BA.debugLine="Dim inicio As String";
_inicio = "";
 //BA.debugLineNum = 271;BA.debugLine="Dim final As String";
_final = "";
 //BA.debugLineNum = 272;BA.debugLine="Dim index1 As Int";
_index1 = 0;
 //BA.debugLineNum = 273;BA.debugLine="Dim index2 As Int";
_index2 = 0;
 //BA.debugLineNum = 274;BA.debugLine="Dim mat As Matcher";
_mat = new anywheresoftware.b4a.keywords.Regex.MatcherWrapper();
 //BA.debugLineNum = 275;BA.debugLine="Dim premiados As List";
_premiados = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 276;BA.debugLine="premiados.Initialize";
_premiados.Initialize();
 //BA.debugLineNum = 277;BA.debugLine="elacierto.Initialize";
_elacierto.Initialize();
 //BA.debugLineNum = 278;BA.debugLine="elcomplem.Initialize";
_elcomplem.Initialize();
 //BA.debugLineNum = 279;BA.debugLine="lacombi.Initialize";
_lacombi.Initialize();
 //BA.debugLineNum = 280;BA.debugLine="Dim html As String";
_html = "";
 //BA.debugLineNum = 281;BA.debugLine="Dim http As HttpJob";
_http = new b4a.Primitiva.httpjob();
 //BA.debugLineNum = 282;BA.debugLine="Dim mensaje As List";
_mensaje = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 283;BA.debugLine="mensaje.Initialize";
_mensaje.Initialize();
 //BA.debugLineNum = 284;BA.debugLine="Dim rs As ResultSet";
_rs = new anywheresoftware.b4a.sql.SQL.ResultSetWrapper();
 //BA.debugLineNum = 285;BA.debugLine="http.Initialize(\"\",Me)";
_http._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 286;BA.debugLine="Dim texto As String";
_texto = "";
 //BA.debugLineNum = 291;BA.debugLine="http.Download(url)";
_http._download /*String*/ (_url);
 //BA.debugLineNum = 292;BA.debugLine="Wait For (http) JobDone(http As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_http));
this.state = 50;
return;
case 50:
//C
this.state = 1;
_http = (b4a.Primitiva.httpjob) result[0];
;
 //BA.debugLineNum = 293;BA.debugLine="If http.Success Then";
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
 //BA.debugLineNum = 294;BA.debugLine="html=http.getString";
_html = _http._getstring /*String*/ ();
 //BA.debugLineNum = 295;BA.debugLine="inicio =$\"<article class=\"$";
_inicio = ("<article class=");
 //BA.debugLineNum = 296;BA.debugLine="final=$\"</article>\"$";
_final = ("</article>");
 //BA.debugLineNum = 297;BA.debugLine="Dim index1 As Int=html.IndexOf(inicio)";
_index1 = _html.indexOf(_inicio);
 //BA.debugLineNum = 298;BA.debugLine="Dim index2 As Int=html.IndexOf(final)";
_index2 = _html.indexOf(_final);
 //BA.debugLineNum = 299;BA.debugLine="html=html.Substring2(index1,index2)";
_html = _html.substring(_index1,_index2);
 if (true) break;

case 4:
//C
this.state = 5;
;
 //BA.debugLineNum = 301;BA.debugLine="http.Release";
_http._release /*String*/ ();
 //BA.debugLineNum = 302;BA.debugLine="inicio=$\"<div class=\"combi\">\"$";
_inicio = ("<div class=\"combi\">");
 //BA.debugLineNum = 303;BA.debugLine="final=$\"<div class=\"sepanum\"></div>\"$";
_final = ("<div class=\"sepanum\"></div>");
 //BA.debugLineNum = 304;BA.debugLine="index1=html.IndexOf(inicio)";
_index1 = _html.indexOf(_inicio);
 //BA.debugLineNum = 305;BA.debugLine="index2=html.IndexOf(final)";
_index2 = _html.indexOf(_final);
 //BA.debugLineNum = 306;BA.debugLine="html=html.Substring2(index1,index2)";
_html = _html.substring(_index1,_index2);
 //BA.debugLineNum = 308;BA.debugLine="Dim patron As String=$\"<div class=\\\"num\\\">(\\d+)<\\";
_patron = ("<div class=\\\"num\\\">(\\d+)<\\/div>");
 //BA.debugLineNum = 309;BA.debugLine="mat=Regex.Matcher(patron,html)";
_mat = anywheresoftware.b4a.keywords.Common.Regex.Matcher(_patron,_html);
 //BA.debugLineNum = 311;BA.debugLine="Do While mat.Find = True";
if (true) break;

case 5:
//do while
this.state = 8;
while (_mat.Find()==anywheresoftware.b4a.keywords.Common.True) {
this.state = 7;
if (true) break;
}
if (true) break;

case 7:
//C
this.state = 5;
 //BA.debugLineNum = 312;BA.debugLine="premiados.Add(mat.Group(1))";
_premiados.Add((Object)(_mat.Group((int) (1))));
 if (true) break;

case 8:
//C
this.state = 9;
;
 //BA.debugLineNum = 315;BA.debugLine="rs=sql.ExecQuery(\"select n1,n2,n3,n4,n5,n6 from a";
_rs = (anywheresoftware.b4a.sql.SQL.ResultSetWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.ResultSetWrapper(), (android.database.Cursor)(parent._sql.ExecQuery("select n1,n2,n3,n4,n5,n6 from apuestas")));
 //BA.debugLineNum = 317;BA.debugLine="Do While rs.NextRow";
if (true) break;

case 9:
//do while
this.state = 35;
while (_rs.NextRow()) {
this.state = 11;
if (true) break;
}
if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 318;BA.debugLine="acertado=0";
_acertado = (int) (0);
 //BA.debugLineNum = 319;BA.debugLine="complementario=0";
_complementario = (int) (0);
 //BA.debugLineNum = 320;BA.debugLine="combinacion=rellena(rs.GetInt(\"n1\"))&\"-\"&rellena";
_combinacion = _rellena(_rs.GetInt("n1"))+"-"+_rellena(_rs.GetInt("n2"))+"-"+_rellena(_rs.GetInt("n3"))+"-"+_rellena(_rs.GetInt("n4"))+"-"+_rellena(_rs.GetInt("n5"))+"-"+_rellena(_rs.GetInt("n6"));
 //BA.debugLineNum = 322;BA.debugLine="For i = 1 To 6";
if (true) break;

case 12:
//for
this.state = 23;
step55 = 1;
limit55 = (int) (6);
_i = (int) (1) ;
this.state = 51;
if (true) break;

case 51:
//C
this.state = 23;
if ((step55 > 0 && _i <= limit55) || (step55 < 0 && _i >= limit55)) this.state = 14;
if (true) break;

case 52:
//C
this.state = 51;
_i = ((int)(0 + _i + step55)) ;
if (true) break;

case 14:
//C
this.state = 15;
 //BA.debugLineNum = 323;BA.debugLine="For x=0 To 5";
if (true) break;

case 15:
//for
this.state = 22;
step56 = 1;
limit56 = (int) (5);
_x = (int) (0) ;
this.state = 53;
if (true) break;

case 53:
//C
this.state = 22;
if ((step56 > 0 && _x <= limit56) || (step56 < 0 && _x >= limit56)) this.state = 17;
if (true) break;

case 54:
//C
this.state = 53;
_x = ((int)(0 + _x + step56)) ;
if (true) break;

case 17:
//C
this.state = 18;
 //BA.debugLineNum = 324;BA.debugLine="If rs.getInt(\"n\"&i)=premiados.Get(x) Then";
if (true) break;

case 18:
//if
this.state = 21;
if (_rs.GetInt("n"+BA.NumberToString(_i))==(double)(BA.ObjectToNumber(_premiados.Get(_x)))) { 
this.state = 20;
}if (true) break;

case 20:
//C
this.state = 21;
 //BA.debugLineNum = 325;BA.debugLine="acertado=acertado+1";
_acertado = (int) (_acertado+1);
 if (true) break;

case 21:
//C
this.state = 54;
;
 if (true) break;
if (true) break;

case 22:
//C
this.state = 52;
;
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 329;BA.debugLine="For i=1 To 6";

case 23:
//for
this.state = 30;
step62 = 1;
limit62 = (int) (6);
_i = (int) (1) ;
this.state = 55;
if (true) break;

case 55:
//C
this.state = 30;
if ((step62 > 0 && _i <= limit62) || (step62 < 0 && _i >= limit62)) this.state = 25;
if (true) break;

case 56:
//C
this.state = 55;
_i = ((int)(0 + _i + step62)) ;
if (true) break;

case 25:
//C
this.state = 26;
 //BA.debugLineNum = 330;BA.debugLine="If rs.getInt(\"n\"&i)=premiados.Get(6) Then";
if (true) break;

case 26:
//if
this.state = 29;
if (_rs.GetInt("n"+BA.NumberToString(_i))==(double)(BA.ObjectToNumber(_premiados.Get((int) (6))))) { 
this.state = 28;
}if (true) break;

case 28:
//C
this.state = 29;
 //BA.debugLineNum = 331;BA.debugLine="complementario=complementario+1";
_complementario = (int) (_complementario+1);
 if (true) break;

case 29:
//C
this.state = 56;
;
 if (true) break;
if (true) break;

case 30:
//C
this.state = 31;
;
 //BA.debugLineNum = 334;BA.debugLine="mensaje.Add(\"[ \"&combinacion&\" ] : Acertados:\" &";
_mensaje.Add((Object)("[ "+_combinacion+" ] : Acertados:"+BA.NumberToString(_acertado)+" Complementario:"+BA.NumberToString(_complementario)));
 //BA.debugLineNum = 335;BA.debugLine="lacombi.add(combinacion)";
_lacombi.Add((Object)(_combinacion));
 //BA.debugLineNum = 336;BA.debugLine="elacierto.add(acertado)";
_elacierto.Add((Object)(_acertado));
 //BA.debugLineNum = 337;BA.debugLine="elcomplem.add(complementario)";
_elcomplem.Add((Object)(_complementario));
 //BA.debugLineNum = 338;BA.debugLine="If acertado>=3 Then";
if (true) break;

case 31:
//if
this.state = 34;
if (_acertado>=3) { 
this.state = 33;
}if (true) break;

case 33:
//C
this.state = 34;
 //BA.debugLineNum = 339;BA.debugLine="MsgboxAsync(\"Tienes un premio de \"&acertado&\" A";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Tienes un premio de "+BA.NumberToString(_acertado)+" Aciertos y "+BA.NumberToString(_complementario)+" Complementario con la combinacion : ["+_combinacion+"]"),BA.ObjectToCharSequence("PREMIO!!"),processBA);
 if (true) break;

case 34:
//C
this.state = 9;
;
 if (true) break;

case 35:
//C
this.state = 36;
;
 //BA.debugLineNum = 342;BA.debugLine="texto=\"\"";
_texto = "";
 //BA.debugLineNum = 343;BA.debugLine="For i=0 To 5";
if (true) break;

case 36:
//for
this.state = 45;
step76 = 1;
limit76 = (int) (5);
_i = (int) (0) ;
this.state = 57;
if (true) break;

case 57:
//C
this.state = 45;
if ((step76 > 0 && _i <= limit76) || (step76 < 0 && _i >= limit76)) this.state = 38;
if (true) break;

case 58:
//C
this.state = 57;
_i = ((int)(0 + _i + step76)) ;
if (true) break;

case 38:
//C
this.state = 39;
 //BA.debugLineNum = 344;BA.debugLine="If i=0 Then";
if (true) break;

case 39:
//if
this.state = 44;
if (_i==0) { 
this.state = 41;
}else {
this.state = 43;
}if (true) break;

case 41:
//C
this.state = 44;
 //BA.debugLineNum = 345;BA.debugLine="texto=rellena(premiados.Get(i))";
_texto = _rellena((int)(BA.ObjectToNumber(_premiados.Get(_i))));
 if (true) break;

case 43:
//C
this.state = 44;
 //BA.debugLineNum = 347;BA.debugLine="texto=texto&\"-\"&rellena(premiados.Get(i))";
_texto = _texto+"-"+_rellena((int)(BA.ObjectToNumber(_premiados.Get(_i))));
 if (true) break;

case 44:
//C
this.state = 58;
;
 if (true) break;
if (true) break;

case 45:
//C
this.state = 46;
;
 //BA.debugLineNum = 350;BA.debugLine="texto=\"[ \"&texto& \" ]   COMP:\" & rellena(premiado";
_texto = "[ "+_texto+" ]   COMP:"+_rellena((int)(BA.ObjectToNumber(_premiados.Get((int) (6)))))+" REINT:"+_rellena((int)(BA.ObjectToNumber(_premiados.Get((int) (7)))));
 //BA.debugLineNum = 351;BA.debugLine="For i = 0 To mensaje.Size-1";
if (true) break;

case 46:
//for
this.state = 49;
step84 = 1;
limit84 = (int) (_mensaje.getSize()-1);
_i = (int) (0) ;
this.state = 59;
if (true) break;

case 59:
//C
this.state = 49;
if ((step84 > 0 && _i <= limit84) || (step84 < 0 && _i >= limit84)) this.state = 48;
if (true) break;

case 60:
//C
this.state = 59;
_i = ((int)(0 + _i + step84)) ;
if (true) break;

case 48:
//C
this.state = 60;
 //BA.debugLineNum = 352;BA.debugLine="premio_grid.Add(crea_row_premiados(lacombi.Get(";
parent.mostCurrent._premio_grid._add(_crea_row_premiados(BA.ObjectToString(_lacombi.Get(_i)),BA.ObjectToString(_elacierto.Get(_i)),BA.ObjectToString(_elcomplem.Get(_i))),(Object)(_i));
 if (true) break;
if (true) break;

case 49:
//C
this.state = -1;
;
 //BA.debugLineNum = 355;BA.debugLine="Ganadora.Text=texto";
parent.mostCurrent._ganadora.setText(BA.ObjectToCharSequence(_texto));
 //BA.debugLineNum = 363;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static anywheresoftware.b4a.objects.B4XViewWrapper  _crea_row(anywheresoftware.b4a.objects.collections.List _listac) throws Exception{
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;
anywheresoftware.b4a.objects.AnimationWrapper _a = null;
 //BA.debugLineNum = 109;BA.debugLine="Sub crea_row(listac As List) As B4XView";
 //BA.debugLineNum = 110;BA.debugLine="Dim p As B4XView= xui.CreatePanel(\"\")";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = mostCurrent._xui.CreatePanel(processBA,"");
 //BA.debugLineNum = 111;BA.debugLine="p.LoadLayout(\"ver_rows\")";
_p.LoadLayout("ver_rows",mostCurrent.activityBA);
 //BA.debugLineNum = 112;BA.debugLine="L1.text=listac.Get(0)";
mostCurrent._l1.setText(BA.ObjectToCharSequence(_listac.Get((int) (0))));
 //BA.debugLineNum = 113;BA.debugLine="L2.text=listac.Get(1)";
mostCurrent._l2.setText(BA.ObjectToCharSequence(_listac.Get((int) (1))));
 //BA.debugLineNum = 114;BA.debugLine="L3.text=listac.Get(2)";
mostCurrent._l3.setText(BA.ObjectToCharSequence(_listac.Get((int) (2))));
 //BA.debugLineNum = 115;BA.debugLine="L4.text=listac.Get(3)";
mostCurrent._l4.setText(BA.ObjectToCharSequence(_listac.Get((int) (3))));
 //BA.debugLineNum = 116;BA.debugLine="L5.text=listac.Get(4)";
mostCurrent._l5.setText(BA.ObjectToCharSequence(_listac.Get((int) (4))));
 //BA.debugLineNum = 117;BA.debugLine="L6.text=listac.Get(5)";
mostCurrent._l6.setText(BA.ObjectToCharSequence(_listac.Get((int) (5))));
 //BA.debugLineNum = 120;BA.debugLine="p.SetLayoutAnimated(100,0,0,100%x,60dip)";
_p.SetLayoutAnimated((int) (100),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 //BA.debugLineNum = 122;BA.debugLine="Dim a As Animation";
_a = new anywheresoftware.b4a.objects.AnimationWrapper();
 //BA.debugLineNum = 123;BA.debugLine="a.InitializeRotateCenter(\"\", 0, 360, L1)";
_a.InitializeRotateCenter(mostCurrent.activityBA,"",(float) (0),(float) (360),(android.view.View)(mostCurrent._l1.getObject()));
 //BA.debugLineNum = 124;BA.debugLine="a.Duration=500";
_a.setDuration((long) (500));
 //BA.debugLineNum = 125;BA.debugLine="a.RepeatCount=1";
_a.setRepeatCount((int) (1));
 //BA.debugLineNum = 127;BA.debugLine="a.Start(L1)";
_a.Start((android.view.View)(mostCurrent._l1.getObject()));
 //BA.debugLineNum = 128;BA.debugLine="a.Start(L2)";
_a.Start((android.view.View)(mostCurrent._l2.getObject()));
 //BA.debugLineNum = 129;BA.debugLine="a.Start(L3)";
_a.Start((android.view.View)(mostCurrent._l3.getObject()));
 //BA.debugLineNum = 130;BA.debugLine="a.Start(L4)";
_a.Start((android.view.View)(mostCurrent._l4.getObject()));
 //BA.debugLineNum = 131;BA.debugLine="a.Start(L5)";
_a.Start((android.view.View)(mostCurrent._l5.getObject()));
 //BA.debugLineNum = 132;BA.debugLine="a.Start(L6)";
_a.Start((android.view.View)(mostCurrent._l6.getObject()));
 //BA.debugLineNum = 149;BA.debugLine="Return p";
if (true) return _p;
 //BA.debugLineNum = 150;BA.debugLine="End Sub";
return null;
}
public static anywheresoftware.b4a.objects.B4XViewWrapper  _crea_row_premiados(String _c,String _a,String _cmp) throws Exception{
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;
 //BA.debugLineNum = 152;BA.debugLine="Sub crea_row_premiados(c As String,a As String,cmp";
 //BA.debugLineNum = 153;BA.debugLine="Dim p As B4XView= xui.CreatePanel(\"\")";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = mostCurrent._xui.CreatePanel(processBA,"");
 //BA.debugLineNum = 154;BA.debugLine="p.LoadLayout(\"row_premiados\")";
_p.LoadLayout("row_premiados",mostCurrent.activityBA);
 //BA.debugLineNum = 155;BA.debugLine="combi.Text=c";
mostCurrent._combi.setText(BA.ObjectToCharSequence(_c));
 //BA.debugLineNum = 156;BA.debugLine="acier.Text=a";
mostCurrent._acier.setText(BA.ObjectToCharSequence(_a));
 //BA.debugLineNum = 157;BA.debugLine="compl.Text=cmp";
mostCurrent._compl.setText(BA.ObjectToCharSequence(_cmp));
 //BA.debugLineNum = 159;BA.debugLine="p.SetLayoutAnimated(100,0,0,100%x,30dip)";
_p.SetLayoutAnimated((int) (100),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30)));
 //BA.debugLineNum = 169;BA.debugLine="Return p";
if (true) return _p;
 //BA.debugLineNum = 170;BA.debugLine="End Sub";
return null;
}
public static anywheresoftware.b4a.objects.collections.List  _createnumberslist(int _numberstocreate,int _minnumber,int _maxnumber,boolean _ascend) throws Exception{
anywheresoftware.b4a.objects.collections.List _results = null;
anywheresoftware.b4a.objects.collections.Map _nummap = null;
int _i = 0;
Object _o = null;
 //BA.debugLineNum = 85;BA.debugLine="Sub CreateNumbersList(NumbersToCreate As Int,MinNu";
 //BA.debugLineNum = 86;BA.debugLine="Dim Results As List";
_results = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 87;BA.debugLine="Results.Initialize";
_results.Initialize();
 //BA.debugLineNum = 89;BA.debugLine="If MaxNumber - MinNumber < NumbersToCreate Then R";
if (_maxnumber-_minnumber<_numberstocreate) { 
if (true) return _results;};
 //BA.debugLineNum = 91;BA.debugLine="Dim NumMap As Map";
_nummap = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 93;BA.debugLine="NumMap.Initialize";
_nummap.Initialize();
 //BA.debugLineNum = 94;BA.debugLine="Dim i As Int";
_i = 0;
 //BA.debugLineNum = 95;BA.debugLine="For i = MinNumber To MaxNumber";
{
final int step7 = 1;
final int limit7 = _maxnumber;
_i = _minnumber ;
for (;_i <= limit7 ;_i = _i + step7 ) {
 //BA.debugLineNum = 96;BA.debugLine="NumMap.Put(i,i)";
_nummap.Put((Object)(_i),(Object)(_i));
 }
};
 //BA.debugLineNum = 99;BA.debugLine="Do While Results.Size < NumbersToCreate";
while (_results.getSize()<_numberstocreate) {
 //BA.debugLineNum = 100;BA.debugLine="Dim O As Object = NumMap.Remove(Rnd(MinNumber, M";
_o = _nummap.Remove((Object)(anywheresoftware.b4a.keywords.Common.Rnd(_minnumber,(int) (_maxnumber+1))));
 //BA.debugLineNum = 101;BA.debugLine="If O <> Null Then Results.Add(O)";
if (_o!= null) { 
_results.Add(_o);};
 }
;
 //BA.debugLineNum = 104;BA.debugLine="Results.Sort(Ascend)";
_results.Sort(_ascend);
 //BA.debugLineNum = 106;BA.debugLine="Return Results";
if (true) return _results;
 //BA.debugLineNum = 107;BA.debugLine="End Sub";
return null;
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
 //BA.debugLineNum = 370;BA.debugLine="d.BackgroundColor=Colors.White";
_d._backgroundcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 371;BA.debugLine="d.BodyTextColor=Colors.Blue";
_d._bodytextcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Blue;
 //BA.debugLineNum = 372;BA.debugLine="d.VisibleAnimationDuration=300";
_d._visibleanimationduration /*int*/  = (int) (300);
 //BA.debugLineNum = 373;BA.debugLine="Dim rs As Object";
_rs = new Object();
 //BA.debugLineNum = 374;BA.debugLine="rs=d.Show(\"Quieres borrar la combinacion ¿?\"&CRLF";
_rs = _d._show /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ ((Object)("Quieres borrar la combinacion ¿?"+anywheresoftware.b4a.keywords.Common.CRLF+" Solo se eliminará esta combinación."),(Object)("Si"),(Object)("No"),(Object)(""));
 //BA.debugLineNum = 375;BA.debugLine="Wait For(rs) complete (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 5;
return;
case 5:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 376;BA.debugLine="If Result = xui.DialogResponse_Positive Then";
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
 //BA.debugLineNum = 377;BA.debugLine="Dim primary_key As Int";
_primary_key = 0;
 //BA.debugLineNum = 378;BA.debugLine="primary_key=Value";
_primary_key = (int)(BA.ObjectToNumber(_value));
 //BA.debugLineNum = 379;BA.debugLine="CustomListView1.RemoveAt(Index)";
parent.mostCurrent._customlistview1._removeat(_index);
 //BA.debugLineNum = 380;BA.debugLine="sql.ExecNonQuery(\"delete from Apuestas where Apu";
parent._sql.ExecNonQuery("delete from Apuestas where Apuesta='"+BA.NumberToString(_primary_key)+"'");
 //BA.debugLineNum = 381;BA.debugLine="ToastMessageShow(\"Borrada la combinación\",True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Borrada la combinación"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 382;BA.debugLine="VerGuardados_Click";
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
 //BA.debugLineNum = 23;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 25;BA.debugLine="Private xui As XUI";
mostCurrent._xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 26;BA.debugLine="Private Nueva As Button";
mostCurrent._nueva = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private WebView1 As WebView";
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
mostCurrent._lista = new anywheresoftware.b4a.objects.collections.List();
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
 //BA.debugLineNum = 50;BA.debugLine="End Sub";
return "";
}
public static String  _guardar_click() throws Exception{
anywheresoftware.b4a.sql.SQL.ResultSetWrapper _rs = null;
 //BA.debugLineNum = 172;BA.debugLine="Private Sub Guardar_Click";
 //BA.debugLineNum = 173;BA.debugLine="Dim rs As ResultSet";
_rs = new anywheresoftware.b4a.sql.SQL.ResultSetWrapper();
 //BA.debugLineNum = 174;BA.debugLine="rs=sql.ExecQuery(\"select count(*) from Apuestas_";
_rs = (anywheresoftware.b4a.sql.SQL.ResultSetWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.ResultSetWrapper(), (android.database.Cursor)(_sql.ExecQuery("select count(*) from Apuestas_tmp")));
 //BA.debugLineNum = 175;BA.debugLine="If rs.RowCount>0 Then";
if (_rs.getRowCount()>0) { 
 //BA.debugLineNum = 176;BA.debugLine="rs=sql.ExecQuery(\"select fecha,n1,n2,n3,n4,n5,n6";
_rs = (anywheresoftware.b4a.sql.SQL.ResultSetWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.ResultSetWrapper(), (android.database.Cursor)(_sql.ExecQuery("select fecha,n1,n2,n3,n4,n5,n6 from Apuestas_tmp")));
 //BA.debugLineNum = 177;BA.debugLine="Do While rs.NextRow";
while (_rs.NextRow()) {
 //BA.debugLineNum = 178;BA.debugLine="sql.ExecNonQuery(\"insert into Apuestas (fecha,n";
_sql.ExecNonQuery("insert into Apuestas (fecha,n1,n2,n3,n4,n5,n6) values ('"+_rs.GetString("Fecha")+"','"+BA.NumberToString(_rs.GetInt("n1"))+"','"+BA.NumberToString(_rs.GetInt("n2"))+"','"+BA.NumberToString(_rs.GetInt("n3"))+"','"+BA.NumberToString(_rs.GetInt("n4"))+"','"+BA.NumberToString(_rs.GetInt("n5"))+"','"+BA.NumberToString(_rs.GetInt("n6"))+"')");
 }
;
 //BA.debugLineNum = 180;BA.debugLine="sql.ExecNonQuery(\"delete from Apuestas_tmp\")";
_sql.ExecNonQuery("delete from Apuestas_tmp");
 //BA.debugLineNum = 181;BA.debugLine="CustomListView1.Clear";
mostCurrent._customlistview1._clear();
 //BA.debugLineNum = 182;BA.debugLine="ToastMessageShow(\"Guardada la combinacion en la";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Guardada la combinacion en la BBDD"),anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 184;BA.debugLine="ToastMessageShow(\"Nada que guardar\",True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Nada que guardar"),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 186;BA.debugLine="End Sub";
return "";
}
public static String  _guardar_solo_1_click() throws Exception{
anywheresoftware.b4a.sql.SQL.ResultSetWrapper _rs = null;
 //BA.debugLineNum = 422;BA.debugLine="Private Sub guardar_solo_1_Click";
 //BA.debugLineNum = 423;BA.debugLine="Dim rs As ResultSet";
_rs = new anywheresoftware.b4a.sql.SQL.ResultSetWrapper();
 //BA.debugLineNum = 424;BA.debugLine="rs=sql.ExecQuery(\"select count(*) num from apuest";
_rs = (anywheresoftware.b4a.sql.SQL.ResultSetWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.ResultSetWrapper(), (android.database.Cursor)(_sql.ExecQuery("select count(*) num from apuestas where n1='8' and n2='22' and n3='26' and n4='31' and n5='46' and n6='49'")));
 //BA.debugLineNum = 425;BA.debugLine="If rs.RowCount>0 Then";
if (_rs.getRowCount()>0) { 
 //BA.debugLineNum = 426;BA.debugLine="rs.Position=0";
_rs.setPosition((int) (0));
 //BA.debugLineNum = 427;BA.debugLine="If rs.getint(\"num\")=0 Then";
if (_rs.GetInt("num")==0) { 
 //BA.debugLineNum = 428;BA.debugLine="sql.ExecNonQuery2(\"insert into Apuestas (fecha,";
_sql.ExecNonQuery2("insert into Apuestas (fecha,n1,n2,n3,n4,n5,n6) values (?,'8','22','26','31','46','49')",anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())}));
 //BA.debugLineNum = 429;BA.debugLine="sql.ExecNonQuery2(\"insert into Apuestas (fecha,";
_sql.ExecNonQuery2("insert into Apuestas (fecha,n1,n2,n3,n4,n5,n6) values (?,'9','25','35','38','40','44')",anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())}));
 //BA.debugLineNum = 430;BA.debugLine="sql.ExecNonQuery2(\"insert into Apuestas (fecha,";
_sql.ExecNonQuery2("insert into Apuestas (fecha,n1,n2,n3,n4,n5,n6) values (?,'7','12','13','18','30','33')",anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())}));
 //BA.debugLineNum = 431;BA.debugLine="sql.ExecNonQuery2(\"insert into Apuestas (fecha,";
_sql.ExecNonQuery2("insert into Apuestas (fecha,n1,n2,n3,n4,n5,n6) values (?,'1','9','12','14','44','48')",anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())}));
 //BA.debugLineNum = 432;BA.debugLine="ToastMessageShow(\"Guardada la combinacion ESPEC";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Guardada la combinacion ESPECIAL NOE en la BBDD"),anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 435;BA.debugLine="ToastMessageShow(\"ombinacion ESPECIAL NOE ya es";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("ombinacion ESPECIAL NOE ya está guardada"),anywheresoftware.b4a.keywords.Common.True);
 };
 };
 //BA.debugLineNum = 438;BA.debugLine="CustomListView1.Clear";
mostCurrent._customlistview1._clear();
 //BA.debugLineNum = 439;BA.debugLine="cargar_guardados";
_cargar_guardados();
 //BA.debugLineNum = 450;BA.debugLine="End Sub";
return "";
}
public static String  _nueva_click() throws Exception{
 //BA.debugLineNum = 73;BA.debugLine="Private Sub Nueva_Click";
 //BA.debugLineNum = 75;BA.debugLine="lista=CreateNumbersList(6,1,49,True)";
mostCurrent._lista = _createnumberslist((int) (6),(int) (1),(int) (49),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 76;BA.debugLine="CustomListView1.Add(crea_row(lista),\"\")";
mostCurrent._customlistview1._add(_crea_row(mostCurrent._lista),(Object)(""));
 //BA.debugLineNum = 77;BA.debugLine="CustomListView1.ScrollToItem(CustomListView1.Size";
mostCurrent._customlistview1._scrolltoitem((int) (mostCurrent._customlistview1._getsize()-1));
 //BA.debugLineNum = 79;BA.debugLine="If lista.Size>0 Then";
if (mostCurrent._lista.getSize()>0) { 
 //BA.debugLineNum = 80;BA.debugLine="sql.ExecNonQuery(\"insert into Apuestas_tmp (fech";
_sql.ExecNonQuery("insert into Apuestas_tmp (fecha,n1,n2,n3,n4,n5,n6) values ('"+anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+"','"+BA.ObjectToString(mostCurrent._lista.Get((int) (0)))+"','"+BA.ObjectToString(mostCurrent._lista.Get((int) (1)))+"','"+BA.ObjectToString(mostCurrent._lista.Get((int) (2)))+"','"+BA.ObjectToString(mostCurrent._lista.Get((int) (3)))+"','"+BA.ObjectToString(mostCurrent._lista.Get((int) (4)))+"','"+BA.ObjectToString(mostCurrent._lista.Get((int) (5)))+"')");
 };
 //BA.debugLineNum = 83;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 21;BA.debugLine="End Sub";
return "";
}
public static String  _regexreplace(String _pattern,String _text,String _replacement) throws Exception{
anywheresoftware.b4a.keywords.Regex.MatcherWrapper _m = null;
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
 //BA.debugLineNum = 480;BA.debugLine="Sub RegexReplace(Pattern As String, Text As String";
 //BA.debugLineNum = 481;BA.debugLine="Dim m As Matcher";
_m = new anywheresoftware.b4a.keywords.Regex.MatcherWrapper();
 //BA.debugLineNum = 482;BA.debugLine="m = Regex.Matcher(Pattern, Text)";
_m = anywheresoftware.b4a.keywords.Common.Regex.Matcher(_pattern,_text);
 //BA.debugLineNum = 483;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 484;BA.debugLine="r.Target = m";
_r.Target = (Object)(_m.getObject());
 //BA.debugLineNum = 485;BA.debugLine="Return r.RunMethod2(\"replaceAll\", Replacement, \"j";
if (true) return BA.ObjectToString(_r.RunMethod2("replaceAll",_replacement,"java.lang.String"));
 //BA.debugLineNum = 486;BA.debugLine="End Sub";
return "";
}
public static String  _rellena(int _dat) throws Exception{
 //BA.debugLineNum = 488;BA.debugLine="Sub rellena(dat As Int) As String";
 //BA.debugLineNum = 489;BA.debugLine="If dat<9 Then";
if (_dat<9) { 
 //BA.debugLineNum = 490;BA.debugLine="Return \"0\"&dat";
if (true) return "0"+BA.NumberToString(_dat);
 }else {
 //BA.debugLineNum = 492;BA.debugLine="Return dat";
if (true) return BA.NumberToString(_dat);
 };
 //BA.debugLineNum = 494;BA.debugLine="End Sub";
return "";
}
public static String  _resultados_click() throws Exception{
 //BA.debugLineNum = 387;BA.debugLine="Private Sub Resultados_Click";
 //BA.debugLineNum = 388;BA.debugLine="Activity.LoadLayout(\"WebView\")";
mostCurrent._activity.LoadLayout("WebView",mostCurrent.activityBA);
 //BA.debugLineNum = 390;BA.debugLine="End Sub";
return "";
}
public static String  _trytofix() throws Exception{
anywheresoftware.b4j.object.JavaObject _jo = null;
anywheresoftware.b4j.object.JavaObject _context = null;
Object _listener = null;
 //BA.debugLineNum = 245;BA.debugLine="Public Sub TryToFix";
 //BA.debugLineNum = 246;BA.debugLine="Dim jo As JavaObject";
_jo = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 247;BA.debugLine="jo.InitializeStatic(\"com.google.android.gms.secur";
_jo.InitializeStatic("com.google.android.gms.security.ProviderInstaller");
 //BA.debugLineNum = 248;BA.debugLine="Dim context As JavaObject";
_context = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 249;BA.debugLine="context.InitializeContext";
_context.InitializeContext(processBA);
 //BA.debugLineNum = 251;BA.debugLine="Starter.DisableStrictMode";
mostCurrent._starter._disablestrictmode /*String*/ ();
 //BA.debugLineNum = 253;BA.debugLine="Dim listener As Object = jo.CreateEventFromUI(\"co";
_listener = _jo.CreateEventFromUI(processBA,"com.google.android.gms.security.ProviderInstaller.ProviderInstallListener","ProviderInstall",anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 255;BA.debugLine="Log(\"Installing security provider if needed...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("6786442","Installing security provider if needed...",0);
 //BA.debugLineNum = 256;BA.debugLine="jo.RunMethod(\"installIfNeededAsync\", Array(contex";
_jo.RunMethod("installIfNeededAsync",new Object[]{(Object)(_context.getObject()),_listener});
 //BA.debugLineNum = 257;BA.debugLine="End Sub";
return "";
}
public static String  _verguardados_click() throws Exception{
anywheresoftware.b4a.sql.SQL.ResultSetWrapper _rs = null;
anywheresoftware.b4a.objects.collections.List _items = null;
int _primary_key = 0;
int _i = 0;
 //BA.debugLineNum = 188;BA.debugLine="Private Sub VerGuardados_Click";
 //BA.debugLineNum = 189;BA.debugLine="Activity.LoadLayout(\"Layout\")";
mostCurrent._activity.LoadLayout("Layout",mostCurrent.activityBA);
 //BA.debugLineNum = 190;BA.debugLine="Dim rs As ResultSet";
_rs = new anywheresoftware.b4a.sql.SQL.ResultSetWrapper();
 //BA.debugLineNum = 191;BA.debugLine="rs=sql.ExecQuery(\"select * from Apuestas\")";
_rs = (anywheresoftware.b4a.sql.SQL.ResultSetWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.ResultSetWrapper(), (android.database.Cursor)(_sql.ExecQuery("select * from Apuestas")));
 //BA.debugLineNum = 192;BA.debugLine="Dim items As List";
_items = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 193;BA.debugLine="Dim primary_key As Int";
_primary_key = 0;
 //BA.debugLineNum = 194;BA.debugLine="For i=0 To rs.RowCount-1";
{
final int step6 = 1;
final int limit6 = (int) (_rs.getRowCount()-1);
_i = (int) (0) ;
for (;_i <= limit6 ;_i = _i + step6 ) {
 //BA.debugLineNum = 195;BA.debugLine="rs.Position=i";
_rs.setPosition(_i);
 //BA.debugLineNum = 196;BA.debugLine="items.initialize";
_items.Initialize();
 //BA.debugLineNum = 197;BA.debugLine="primary_key=rs.GetiNT(\"Apuesta\")";
_primary_key = _rs.GetInt("Apuesta");
 //BA.debugLineNum = 199;BA.debugLine="items.Add(rs.Getint(\"n1\"))";
_items.Add((Object)(_rs.GetInt("n1")));
 //BA.debugLineNum = 200;BA.debugLine="items.Add(rs.Getint(\"n2\"))";
_items.Add((Object)(_rs.GetInt("n2")));
 //BA.debugLineNum = 201;BA.debugLine="items.Add(rs.Getint(\"n3\"))";
_items.Add((Object)(_rs.GetInt("n3")));
 //BA.debugLineNum = 202;BA.debugLine="items.Add(rs.Getint(\"n4\"))";
_items.Add((Object)(_rs.GetInt("n4")));
 //BA.debugLineNum = 203;BA.debugLine="items.Add(rs.Getint(\"n5\"))";
_items.Add((Object)(_rs.GetInt("n5")));
 //BA.debugLineNum = 204;BA.debugLine="items.Add(rs.Getint(\"n6\"))";
_items.Add((Object)(_rs.GetInt("n6")));
 //BA.debugLineNum = 205;BA.debugLine="CustomListView2.Add(crea_row(items),primary_key)";
mostCurrent._customlistview2._add(_crea_row(_items),(Object)(_primary_key));
 }
};
 //BA.debugLineNum = 208;BA.debugLine="If CustomListView2.size>0 Then";
if (mostCurrent._customlistview2._getsize()>0) { 
 //BA.debugLineNum = 209;BA.debugLine="CustomListView2.ScrollToItem(CustomListView2.Siz";
mostCurrent._customlistview2._scrolltoitem((int) (mostCurrent._customlistview2._getsize()-1));
 };
 //BA.debugLineNum = 211;BA.debugLine="End Sub";
return "";
}
public static String  _volver_click() throws Exception{
 //BA.debugLineNum = 392;BA.debugLine="Private Sub volver_Click";
 //BA.debugLineNum = 394;BA.debugLine="Activity.LoadLayout(\"Layout2\")";
mostCurrent._activity.LoadLayout("Layout2",mostCurrent.activityBA);
 //BA.debugLineNum = 395;BA.debugLine="cargar_guardados";
_cargar_guardados();
 //BA.debugLineNum = 397;BA.debugLine="End Sub";
return "";
}
public static String  _volver2_click() throws Exception{
 //BA.debugLineNum = 399;BA.debugLine="Private Sub Volver2_Click";
 //BA.debugLineNum = 401;BA.debugLine="Activity.LoadLayout(\"layout\")";
mostCurrent._activity.LoadLayout("layout",mostCurrent.activityBA);
 //BA.debugLineNum = 402;BA.debugLine="VerGuardados_Click";
_verguardados_click();
 //BA.debugLineNum = 403;BA.debugLine="End Sub";
return "";
}
public static String  _volver3_click() throws Exception{
 //BA.debugLineNum = 496;BA.debugLine="Private Sub Volver3_Click";
 //BA.debugLineNum = 498;BA.debugLine="Activity.LoadLayout(\"layout\")";
mostCurrent._activity.LoadLayout("layout",mostCurrent.activityBA);
 //BA.debugLineNum = 499;BA.debugLine="VerGuardados_Click";
_verguardados_click();
 //BA.debugLineNum = 500;BA.debugLine="End Sub";
return "";
}
public static String  _volver4_click() throws Exception{
 //BA.debugLineNum = 502;BA.debugLine="Private Sub volver4_Click";
 //BA.debugLineNum = 504;BA.debugLine="Activity.LoadLayout(\"layout2\")";
mostCurrent._activity.LoadLayout("layout2",mostCurrent.activityBA);
 //BA.debugLineNum = 505;BA.debugLine="cargar_guardados";
_cargar_guardados();
 //BA.debugLineNum = 506;BA.debugLine="End Sub";
return "";
}
}
