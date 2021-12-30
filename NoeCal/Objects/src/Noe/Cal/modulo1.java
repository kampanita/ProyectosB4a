package Noe.Cal;


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

public class modulo1 extends Activity implements B4AActivity{
	public static modulo1 mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "Noe.Cal", "Noe.Cal.modulo1");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (modulo1).");
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
		activityBA = new BA(this, layout, processBA, "Noe.Cal", "Noe.Cal.modulo1");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "Noe.Cal.modulo1", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (modulo1) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (modulo1) Resume **");
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
		return modulo1.class;
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
            BA.LogInfo("** Activity (modulo1) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (modulo1) Pause event (activity is not paused). **");
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
            modulo1 mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (modulo1) Resume **");
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
public static anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spinner1 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _aceptar = null;
public anywheresoftware.b4a.objects.ButtonWrapper _cancelar = null;
public anywheresoftware.b4a.objects.EditTextWrapper _fecha = null;
public anywheresoftware.b4a.objects.ButtonWrapper _fechapicker = null;
public anywheresoftware.b4a.objects.EditTextWrapper _horas = null;
public anywheresoftware.b4a.objects.LabelWrapper _label1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label3 = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel1 = null;
public Noe.Cal.b4xdialog _dialog = null;
public Noe.Cal.b4xdatetemplate _datetemplate = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spinner2 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _incidencia = null;
public anywheresoftware.b4a.objects.ButtonWrapper _borrar = null;
public b4a.example.dateutils _dateutils = null;
public Noe.Cal.main _main = null;
public Noe.Cal.starter _starter = null;
public Noe.Cal.modulo2 _modulo2 = null;
public Noe.Cal.xuiviewsutils _xuiviewsutils = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _aceptar_click() throws Exception{
anywheresoftware.b4a.sql.SQL.ResultSetWrapper _rs = null;
int _respuesta = 0;
String _query = "";
String _tipo = "";
 //BA.debugLineNum = 139;BA.debugLine="Private Sub Aceptar_Click";
 //BA.debugLineNum = 140;BA.debugLine="If Horas.Text<>\"\" And Fecha.Text <> \"\" And Spinne";
if ((mostCurrent._horas.getText()).equals("") == false && (mostCurrent._fecha.getText()).equals("") == false && mostCurrent._spinner1.getSelectedIndex()!=0 && mostCurrent._spinner2.getSelectedIndex()!=0) { 
 //BA.debugLineNum = 141;BA.debugLine="Dim rs As ResultSet";
_rs = new anywheresoftware.b4a.sql.SQL.ResultSetWrapper();
 //BA.debugLineNum = 143;BA.debugLine="Dim respuesta As Int";
_respuesta = 0;
 //BA.debugLineNum = 144;BA.debugLine="respuesta=sql.ExecQuerySingleResult($\"select cou";
_respuesta = (int)(Double.parseDouble(_sql.ExecQuerySingleResult(("select count(*) from dias where dia='")+mostCurrent._fecha.getText()+("'"))));
 //BA.debugLineNum = 146;BA.debugLine="Dim query As String";
_query = "";
 //BA.debugLineNum = 147;BA.debugLine="If respuesta=0 Then";
if (_respuesta==0) { 
 //BA.debugLineNum = 149;BA.debugLine="Dim tipo As String";
_tipo = "";
 //BA.debugLineNum = 150;BA.debugLine="tipo=Spinner1.SelectedItem";
_tipo = mostCurrent._spinner1.getSelectedItem();
 //BA.debugLineNum = 151;BA.debugLine="query=\"insert into dias(dia,horas,tipo,tipo2,in";
_query = "insert into dias(dia,horas,tipo,tipo2,incidencia) values(?,?,?,?,?)";
 //BA.debugLineNum = 152;BA.debugLine="Try";
try { //BA.debugLineNum = 153;BA.debugLine="sql.ExecNonQuery2(query,Array As Object(Fecha.";
_sql.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._fecha.getText()),(Object)(mostCurrent._horas.getText()),(Object)(_tipo),(Object)(mostCurrent._spinner2.getSelectedItem()),(Object)(mostCurrent._incidencia.getText())}));
 } 
       catch (Exception e13) {
			processBA.setLastException(e13); //BA.debugLineNum = 155;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("51572880",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 157;BA.debugLine="rs=sql.ExecQuery(\"commit\")";
_rs = (anywheresoftware.b4a.sql.SQL.ResultSetWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.ResultSetWrapper(), (android.database.Cursor)(_sql.ExecQuery("commit")));
 //BA.debugLineNum = 159;BA.debugLine="rs.Close";
_rs.Close();
 //BA.debugLineNum = 160;BA.debugLine="ToastMessageShow(\"Se ha guardado el parte en la";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Se ha guardado el parte en la BBDD"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 162;BA.debugLine="Fecha.Text=\"\"";
mostCurrent._fecha.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 163;BA.debugLine="Horas.Text=\"\"";
mostCurrent._horas.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 164;BA.debugLine="Incidencia.Text=\"\"";
mostCurrent._incidencia.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 165;BA.debugLine="Spinner2.SelectedIndex=0";
mostCurrent._spinner2.setSelectedIndex((int) (0));
 //BA.debugLineNum = 166;BA.debugLine="Spinner1.SelectedIndex=0";
mostCurrent._spinner1.setSelectedIndex((int) (0));
 }else {
 //BA.debugLineNum = 168;BA.debugLine="Dim tipo As String";
_tipo = "";
 //BA.debugLineNum = 169;BA.debugLine="tipo=Spinner1.SelectedItem";
_tipo = mostCurrent._spinner1.getSelectedItem();
 //BA.debugLineNum = 170;BA.debugLine="query=\"update dias set Horas=?,tipo=?,tipo2=?,i";
_query = "update dias set Horas=?,tipo=?,tipo2=?,incidencia=? where dia='"+mostCurrent._fecha.getText()+"'";
 //BA.debugLineNum = 171;BA.debugLine="Try";
try { //BA.debugLineNum = 172;BA.debugLine="sql.ExecNonQuery2(query,Array As Object(Horas.";
_sql.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(mostCurrent._horas.getText()),(Object)(_tipo),(Object)(mostCurrent._spinner2.getSelectedItem()),(Object)(mostCurrent._incidencia.getText())}));
 } 
       catch (Exception e30) {
			processBA.setLastException(e30); //BA.debugLineNum = 174;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("51572899",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 };
 //BA.debugLineNum = 176;BA.debugLine="rs=sql.ExecQuery(\"commit\")";
_rs = (anywheresoftware.b4a.sql.SQL.ResultSetWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.ResultSetWrapper(), (android.database.Cursor)(_sql.ExecQuery("commit")));
 //BA.debugLineNum = 179;BA.debugLine="rs.Close";
_rs.Close();
 //BA.debugLineNum = 180;BA.debugLine="ToastMessageShow(\"Se ha guardado el parte en la";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Se ha guardado el parte en la BBDD"),anywheresoftware.b4a.keywords.Common.False);
 };
 };
 //BA.debugLineNum = 186;BA.debugLine="End Sub";
return "";
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 35;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 36;BA.debugLine="sql.Initialize(File.DirInternal, \"NoeCal.db\", Fal";
_sql.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"NoeCal.db",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 39;BA.debugLine="Activity.LoadLayout(\"introducir\")";
mostCurrent._activity.LoadLayout("introducir",mostCurrent.activityBA);
 //BA.debugLineNum = 40;BA.debugLine="Spinner1.addall(Array As String(\"\",\"Dia Normal\",\"";
mostCurrent._spinner1.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"","Dia Normal","Dia Festivo Ordinario","Dia Festivo Extraordinario","Noche Normal","Noche Festivo Ordinario","Noche Festivo Extraordinario"}));
 //BA.debugLineNum = 41;BA.debugLine="Spinner2.addall(Array As String(\"\",\"M31\",\"M32\",\"M";
mostCurrent._spinner2.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"","M31","M32","M33","M34","M41","M42","M43","M51","M52","T31","T32","T33","T34","T41","T42","T43","T5","N3","N4","N5","RT"}));
 //BA.debugLineNum = 42;BA.debugLine="Horas.Text=\"\"";
mostCurrent._horas.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 43;BA.debugLine="Incidencia.Text=\"\"";
mostCurrent._incidencia.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 44;BA.debugLine="Fecha.Text=\"\"";
mostCurrent._fecha.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 45;BA.debugLine="Spinner2.SelectedIndex=0";
mostCurrent._spinner2.setSelectedIndex((int) (0));
 //BA.debugLineNum = 46;BA.debugLine="Spinner1.SelectedIndex=0";
mostCurrent._spinner1.setSelectedIndex((int) (0));
 //BA.debugLineNum = 48;BA.debugLine="Dialog.Initialize(Activity)";
mostCurrent._dialog._initialize /*String*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(mostCurrent._activity.getObject())));
 //BA.debugLineNum = 49;BA.debugLine="Dialog.TitleBarColor=xui.Color_Transparent";
mostCurrent._dialog._titlebarcolor /*int*/  = _xui.Color_Transparent;
 //BA.debugLineNum = 50;BA.debugLine="Dialog.BackgroundColor=xui.Color_Gray";
mostCurrent._dialog._backgroundcolor /*int*/  = _xui.Color_Gray;
 //BA.debugLineNum = 52;BA.debugLine="DateTemplate.Initialize";
mostCurrent._datetemplate._initialize /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 53;BA.debugLine="DateTemplate.MinYear = 1973";
mostCurrent._datetemplate._minyear /*int*/  = (int) (1973);
 //BA.debugLineNum = 54;BA.debugLine="DateTemplate.MaxYear = 2100";
mostCurrent._datetemplate._maxyear /*int*/  = (int) (2100);
 //BA.debugLineNum = 56;BA.debugLine="Dialog.Title = \"\"";
mostCurrent._dialog._title /*Object*/  = (Object)("");
 //BA.debugLineNum = 57;BA.debugLine="SetLightTheme";
_setlighttheme();
 //BA.debugLineNum = 62;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 69;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 71;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 65;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 67;BA.debugLine="End Sub";
return "";
}
public static void  _borrar_click() throws Exception{
ResumableSub_Borrar_Click rsub = new ResumableSub_Borrar_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_Borrar_Click extends BA.ResumableSub {
public ResumableSub_Borrar_Click(Noe.Cal.modulo1 parent) {
this.parent = parent;
}
Noe.Cal.modulo1 parent;
Noe.Cal.b4xdialog _d = null;
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
 //BA.debugLineNum = 270;BA.debugLine="If Fecha.Text<>\"\" Then";
if (true) break;

case 1:
//if
this.state = 10;
if ((parent.mostCurrent._fecha.getText()).equals("") == false) { 
this.state = 3;
}else {
this.state = 9;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 271;BA.debugLine="Dim d As B4XDialog";
_d = new Noe.Cal.b4xdialog();
 //BA.debugLineNum = 272;BA.debugLine="d.initialize(Activity)";
_d._initialize /*String*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(parent.mostCurrent._activity.getObject())));
 //BA.debugLineNum = 273;BA.debugLine="d.Title=\"Borrar registro\"";
_d._title /*Object*/  = (Object)("Borrar registro");
 //BA.debugLineNum = 274;BA.debugLine="d.BackgroundColor=Colors.White";
_d._backgroundcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 275;BA.debugLine="d.BodyTextColor=Colors.Blue";
_d._bodytextcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Blue;
 //BA.debugLineNum = 276;BA.debugLine="d.VisibleAnimationDuration=300";
_d._visibleanimationduration /*int*/  = (int) (300);
 //BA.debugLineNum = 277;BA.debugLine="Dim rs As Object";
_rs = new Object();
 //BA.debugLineNum = 278;BA.debugLine="rs=d.Show(\"¿Quieres borrar el registro?\",\"Si\",\"N";
_rs = _d._show /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ ((Object)("¿Quieres borrar el registro?"),(Object)("Si"),(Object)("No"),(Object)(""));
 //BA.debugLineNum = 279;BA.debugLine="Wait For(rs) complete (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 11;
return;
case 11:
//C
this.state = 4;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 280;BA.debugLine="If Result = xui.DialogResponse_Positive Then";
if (true) break;

case 4:
//if
this.state = 7;
if (_result==parent._xui.DialogResponse_Positive) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 281;BA.debugLine="sql.ExecNonQuery(\"delete from Dias where dia='\"";
parent._sql.ExecNonQuery("delete from Dias where dia='"+parent.mostCurrent._fecha.getText()+"'");
 //BA.debugLineNum = 282;BA.debugLine="ToastMessageShow(\"Borrado el parte del dia \"&Fe";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Borrado el parte del dia "+parent.mostCurrent._fecha.getText()),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 283;BA.debugLine="Fecha.Text=\"\"";
parent.mostCurrent._fecha.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 284;BA.debugLine="Spinner1.SelectedIndex=0";
parent.mostCurrent._spinner1.setSelectedIndex((int) (0));
 //BA.debugLineNum = 285;BA.debugLine="Spinner2.SelectedIndex=0";
parent.mostCurrent._spinner2.setSelectedIndex((int) (0));
 //BA.debugLineNum = 286;BA.debugLine="Horas.Text=\"\"";
parent.mostCurrent._horas.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 287;BA.debugLine="Incidencia.Text=\"\"";
parent.mostCurrent._incidencia.setText(BA.ObjectToCharSequence(""));
 if (true) break;

case 7:
//C
this.state = 10;
;
 if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 290;BA.debugLine="ToastMessageShow(\"Nada que borrar\",False )";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Nada que borrar"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 292;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _complete(int _result) throws Exception{
}
public static String  _cancelar_click() throws Exception{
 //BA.debugLineNum = 134;BA.debugLine="Private Sub Cancelar_Click";
 //BA.debugLineNum = 136;BA.debugLine="Activity.finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 137;BA.debugLine="End Sub";
return "";
}
public static String  _datatotext(long _datos) throws Exception{
 //BA.debugLineNum = 127;BA.debugLine="Sub datatotexT(datos As Long) As String";
 //BA.debugLineNum = 128;BA.debugLine="DateTime.DateFormat = \"dd MMMM yyyy\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("dd MMMM yyyy");
 //BA.debugLineNum = 130;BA.debugLine="Return DateTime.Date(datos)";
if (true) return anywheresoftware.b4a.keywords.Common.DateTime.Date(_datos);
 //BA.debugLineNum = 132;BA.debugLine="End Sub";
return "";
}
public static void  _fechapicker_click() throws Exception{
ResumableSub_FechaPicker_Click rsub = new ResumableSub_FechaPicker_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_FechaPicker_Click extends BA.ResumableSub {
public ResumableSub_FechaPicker_Click(Noe.Cal.modulo1 parent) {
this.parent = parent;
}
Noe.Cal.modulo1 parent;
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
 //BA.debugLineNum = 89;BA.debugLine="Wait For (Dialog.ShowTemplate(DateTemplate, \"\", \"";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, parent.mostCurrent._dialog._showtemplate /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ ((Object)(parent.mostCurrent._datetemplate),(Object)(""),(Object)(""),(Object)("Cancelar")));
this.state = 5;
return;
case 5:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 91;BA.debugLine="If Result = xui.DialogResponse_Positive Then";
if (true) break;

case 1:
//if
this.state = 4;
if (_result==parent._xui.DialogResponse_Positive) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 92;BA.debugLine="Fecha.Text=datatotexT(DateTemplate.Date)";
parent.mostCurrent._fecha.setText(BA.ObjectToCharSequence(_datatotext(parent.mostCurrent._datetemplate._getdate /*long*/ ())));
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 95;BA.debugLine="ver_reg(Fecha.Text)";
_ver_reg(parent.mostCurrent._fecha.getText());
 //BA.debugLineNum = 98;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 14;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 17;BA.debugLine="Private Spinner1 As Spinner";
mostCurrent._spinner1 = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Private Aceptar As Button";
mostCurrent._aceptar = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Private Cancelar As Button";
mostCurrent._cancelar = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Private Fecha As EditText";
mostCurrent._fecha = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Private FechaPicker As Button";
mostCurrent._fechapicker = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Private Horas As EditText";
mostCurrent._horas = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Private Label1 As Label";
mostCurrent._label1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Private Label2 As Label";
mostCurrent._label2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Private Label3 As Label";
mostCurrent._label3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private Panel1 As Panel";
mostCurrent._panel1 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private Dialog As B4XDialog";
mostCurrent._dialog = new Noe.Cal.b4xdialog();
 //BA.debugLineNum = 29;BA.debugLine="Dim DateTemplate As B4XDateTemplate";
mostCurrent._datetemplate = new Noe.Cal.b4xdatetemplate();
 //BA.debugLineNum = 30;BA.debugLine="Private Spinner2 As Spinner";
mostCurrent._spinner2 = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Private Incidencia As EditText";
mostCurrent._incidencia = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Private Borrar As Button";
mostCurrent._borrar = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 33;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Dim sql As SQL";
_sql = new anywheresoftware.b4a.sql.SQL();
 //BA.debugLineNum = 10;BA.debugLine="Private xui As XUI";
_xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 12;BA.debugLine="End Sub";
return "";
}
public static String  _setlighttheme() throws Exception{
int _textcolor = 0;
anywheresoftware.b4a.objects.B4XViewWrapper _b = null;
 //BA.debugLineNum = 188;BA.debugLine="Sub SetLightTheme";
 //BA.debugLineNum = 189;BA.debugLine="Dialog.TitleBarColor = xui.Color_Blue";
mostCurrent._dialog._titlebarcolor /*int*/  = _xui.Color_Blue;
 //BA.debugLineNum = 190;BA.debugLine="Dialog.TitleBarHeight = 80dip";
mostCurrent._dialog._titlebarheight /*int*/  = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80));
 //BA.debugLineNum = 191;BA.debugLine="Dim TextColor As Int = 0xFF5B5B5B";
_textcolor = ((int)0xff5b5b5b);
 //BA.debugLineNum = 193;BA.debugLine="Dialog.BackgroundColor = xui.Color_LightGray";
mostCurrent._dialog._backgroundcolor /*int*/  = _xui.Color_LightGray;
 //BA.debugLineNum = 194;BA.debugLine="Dialog.ButtonsColor = xui.Color_White";
mostCurrent._dialog._buttonscolor /*int*/  = _xui.Color_White;
 //BA.debugLineNum = 195;BA.debugLine="Dialog.ButtonsTextColor = Dialog.TitleBarColor";
mostCurrent._dialog._buttonstextcolor /*int*/  = mostCurrent._dialog._titlebarcolor /*int*/ ;
 //BA.debugLineNum = 196;BA.debugLine="Dialog.BorderColor = xui.Color_Transparent";
mostCurrent._dialog._bordercolor /*int*/  = _xui.Color_Transparent;
 //BA.debugLineNum = 197;BA.debugLine="DateTemplate.DaysInWeekColor = xui.Color_Black";
mostCurrent._datetemplate._daysinweekcolor /*int*/  = _xui.Color_Black;
 //BA.debugLineNum = 198;BA.debugLine="DateTemplate.SelectedColor = 0xFF39D7CE";
mostCurrent._datetemplate._selectedcolor /*int*/  = ((int)0xff39d7ce);
 //BA.debugLineNum = 199;BA.debugLine="DateTemplate.HighlightedColor = 0xFF00CEFF";
mostCurrent._datetemplate._highlightedcolor /*int*/  = ((int)0xff00ceff);
 //BA.debugLineNum = 200;BA.debugLine="DateTemplate.DaysInMonthColor = TextColor";
mostCurrent._datetemplate._daysinmonthcolor /*int*/  = _textcolor;
 //BA.debugLineNum = 201;BA.debugLine="DateTemplate.lblMonth.TextColor = TextColor";
mostCurrent._datetemplate._lblmonth /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTextColor(_textcolor);
 //BA.debugLineNum = 202;BA.debugLine="DateTemplate.lblYear.TextColor = TextColor";
mostCurrent._datetemplate._lblyear /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTextColor(_textcolor);
 //BA.debugLineNum = 203;BA.debugLine="DateTemplate.SelectedColor = 0xFFFFA761";
mostCurrent._datetemplate._selectedcolor /*int*/  = ((int)0xffffa761);
 //BA.debugLineNum = 205;BA.debugLine="For Each b As B4XView In Array(DateTemplate.btnMo";
_b = new anywheresoftware.b4a.objects.B4XViewWrapper();
{
final Object[] group15 = new Object[]{(Object)(mostCurrent._datetemplate._btnmonthleft /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .getObject()),(Object)(mostCurrent._datetemplate._btnmonthright /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .getObject()),(Object)(mostCurrent._datetemplate._btnyearleft /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .getObject()),(Object)(mostCurrent._datetemplate._btnyearright /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .getObject())};
final int groupLen15 = group15.length
;int index15 = 0;
;
for (; index15 < groupLen15;index15++){
_b = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(group15[index15]));
 //BA.debugLineNum = 206;BA.debugLine="b.Color = xui.Color_Transparent";
_b.setColor(_xui.Color_Transparent);
 //BA.debugLineNum = 207;BA.debugLine="b.TextColor = TextColor";
_b.setTextColor(_textcolor);
 }
};
 //BA.debugLineNum = 213;BA.debugLine="End Sub";
return "";
}
public static String  _spinner2_itemclick(int _position,Object _value) throws Exception{
String _valor = "";
 //BA.debugLineNum = 218;BA.debugLine="Private Sub Spinner2_ItemClick (Position As Int, V";
 //BA.debugLineNum = 219;BA.debugLine="Dim valor As String";
_valor = "";
 //BA.debugLineNum = 220;BA.debugLine="valor=Value";
_valor = BA.ObjectToString(_value);
 //BA.debugLineNum = 223;BA.debugLine="If valor.startswith(\"M\") Or valor.startswith(\"T\")";
if (_valor.startsWith("M") || _valor.startsWith("T")) { 
 //BA.debugLineNum = 224;BA.debugLine="Horas.Text=\"7.25\"";
mostCurrent._horas.setText(BA.ObjectToCharSequence("7.25"));
 };
 //BA.debugLineNum = 226;BA.debugLine="If valor.startswith(\"N\") Then";
if (_valor.startsWith("N")) { 
 //BA.debugLineNum = 227;BA.debugLine="Horas.Text=\"9.5\"";
mostCurrent._horas.setText(BA.ObjectToCharSequence("9.5"));
 };
 //BA.debugLineNum = 229;BA.debugLine="If valor.StartsWith(\"RT\") Then";
if (_valor.startsWith("RT")) { 
 //BA.debugLineNum = 230;BA.debugLine="Horas.Text=\"2.25\"";
mostCurrent._horas.setText(BA.ObjectToCharSequence("2.25"));
 };
 //BA.debugLineNum = 232;BA.debugLine="End Sub";
return "";
}
public static String  _ver_reg(String _lafecha) throws Exception{
anywheresoftware.b4a.sql.SQL.ResultSetWrapper _rs = null;
int _respuesta = 0;
String _cual = "";
 //BA.debugLineNum = 100;BA.debugLine="Sub ver_reg(laFecha As String)";
 //BA.debugLineNum = 101;BA.debugLine="Dim rs As ResultSet";
_rs = new anywheresoftware.b4a.sql.SQL.ResultSetWrapper();
 //BA.debugLineNum = 102;BA.debugLine="Dim respuesta As Int";
_respuesta = 0;
 //BA.debugLineNum = 103;BA.debugLine="respuesta=sql.ExecQuerySingleResult($\"select coun";
_respuesta = (int)(Double.parseDouble(_sql.ExecQuerySingleResult(("select count(*) from dias where dia='")+_lafecha+("'"))));
 //BA.debugLineNum = 106;BA.debugLine="If respuesta=1 Then";
if (_respuesta==1) { 
 //BA.debugLineNum = 107;BA.debugLine="ToastMessageShow(\"Parte ya existía, recuperamos";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Parte ya existía, recuperamos la información."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 108;BA.debugLine="rs=sql.ExecQuery($\"select Dia,Horas,Tipo,tipo2,i";
_rs = (anywheresoftware.b4a.sql.SQL.ResultSetWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.ResultSetWrapper(), (android.database.Cursor)(_sql.ExecQuery(("select Dia,Horas,Tipo,tipo2,incidencia from dias where Dia='")+mostCurrent._fecha.getText()+("'"))));
 //BA.debugLineNum = 109;BA.debugLine="rs.Position=0";
_rs.setPosition((int) (0));
 //BA.debugLineNum = 111;BA.debugLine="Horas.Text=rs.GetDouble(\"Horas\")";
mostCurrent._horas.setText(BA.ObjectToCharSequence(_rs.GetDouble("Horas")));
 //BA.debugLineNum = 112;BA.debugLine="Dim cual As String";
_cual = "";
 //BA.debugLineNum = 113;BA.debugLine="cual =rs.GetString(\"Tipo\")";
_cual = _rs.GetString("Tipo");
 //BA.debugLineNum = 114;BA.debugLine="Spinner1.SelectedIndex=Spinner1.IndexOf(cual)";
mostCurrent._spinner1.setSelectedIndex(mostCurrent._spinner1.IndexOf(_cual));
 //BA.debugLineNum = 116;BA.debugLine="cual=rs.GetString(\"Tipo2\")";
_cual = _rs.GetString("Tipo2");
 //BA.debugLineNum = 117;BA.debugLine="Spinner2.SelectedIndex=Spinner2.IndexOf(cual)";
mostCurrent._spinner2.setSelectedIndex(mostCurrent._spinner2.IndexOf(_cual));
 //BA.debugLineNum = 119;BA.debugLine="Incidencia.text=rs.getString(\"Incidencia\")";
mostCurrent._incidencia.setText(BA.ObjectToCharSequence(_rs.GetString("Incidencia")));
 }else {
 //BA.debugLineNum = 121;BA.debugLine="Fecha.Text=laFecha";
mostCurrent._fecha.setText(BA.ObjectToCharSequence(_lafecha));
 //BA.debugLineNum = 122;BA.debugLine="Spinner1.SelectedIndex=0";
mostCurrent._spinner1.setSelectedIndex((int) (0));
 //BA.debugLineNum = 123;BA.debugLine="Spinner2.SelectedIndex=0";
mostCurrent._spinner2.setSelectedIndex((int) (0));
 };
 //BA.debugLineNum = 125;BA.debugLine="End Sub";
return "";
}
public static String  _ver_registro(String _lafecha) throws Exception{
anywheresoftware.b4a.sql.SQL.ResultSetWrapper _rs = null;
int _respuesta = 0;
String _cual = "";
 //BA.debugLineNum = 234;BA.debugLine="Sub ver_registro(lafecha As String)";
 //BA.debugLineNum = 236;BA.debugLine="Activity.LoadLayout(\"introducir\")";
mostCurrent._activity.LoadLayout("introducir",mostCurrent.activityBA);
 //BA.debugLineNum = 237;BA.debugLine="Spinner1.addall(Array As String(\"\",\"Dia Normal\",\"";
mostCurrent._spinner1.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"","Dia Normal","Dia Festivo Ordinario","Dia Festivo Extraordinario","Noche Normal","Noche Festivo Ordinario","Noche Festivo Extraordinario"}));
 //BA.debugLineNum = 238;BA.debugLine="Spinner2.addall(Array As String(\"\",\"M31\",\"M32\",\"M";
mostCurrent._spinner2.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"","M31","M32","M33","M34","M41","M42","M43","M51","M52","T31","T32","T33","T34","T41","T42","T43","T5","N3","N4","N5","RT"}));
 //BA.debugLineNum = 240;BA.debugLine="Dim rs As ResultSet";
_rs = new anywheresoftware.b4a.sql.SQL.ResultSetWrapper();
 //BA.debugLineNum = 242;BA.debugLine="Dim respuesta As Int";
_respuesta = 0;
 //BA.debugLineNum = 243;BA.debugLine="respuesta=sql.ExecQuerySingleResult($\"select coun";
_respuesta = (int)(Double.parseDouble(_sql.ExecQuerySingleResult(("select count(*) from dias where dia='")+_lafecha+("'"))));
 //BA.debugLineNum = 246;BA.debugLine="If respuesta=1 Then";
if (_respuesta==1) { 
 //BA.debugLineNum = 248;BA.debugLine="rs=sql.ExecQuery($\"select Dia,Horas,Tipo,tipo2,i";
_rs = (anywheresoftware.b4a.sql.SQL.ResultSetWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.ResultSetWrapper(), (android.database.Cursor)(_sql.ExecQuery(("select Dia,Horas,Tipo,tipo2,incidencia from dias where Dia='")+_lafecha+("'"))));
 //BA.debugLineNum = 249;BA.debugLine="rs.Position=0";
_rs.setPosition((int) (0));
 //BA.debugLineNum = 251;BA.debugLine="Fecha.Text=rs.GetString(\"Dia\")";
mostCurrent._fecha.setText(BA.ObjectToCharSequence(_rs.GetString("Dia")));
 //BA.debugLineNum = 252;BA.debugLine="Horas.Text=rs.GetDouble(\"Horas\")";
mostCurrent._horas.setText(BA.ObjectToCharSequence(_rs.GetDouble("Horas")));
 //BA.debugLineNum = 253;BA.debugLine="Incidencia.Text=rs.GetString(\"Incidencia\")";
mostCurrent._incidencia.setText(BA.ObjectToCharSequence(_rs.GetString("Incidencia")));
 //BA.debugLineNum = 254;BA.debugLine="Dim cual As String";
_cual = "";
 //BA.debugLineNum = 255;BA.debugLine="cual =rs.GetString(\"Tipo\")";
_cual = _rs.GetString("Tipo");
 //BA.debugLineNum = 256;BA.debugLine="Spinner1.SelectedIndex=Spinner1.IndexOf(cual)";
mostCurrent._spinner1.setSelectedIndex(mostCurrent._spinner1.IndexOf(_cual));
 //BA.debugLineNum = 257;BA.debugLine="cual =rs.GetString(\"Tipo2\")";
_cual = _rs.GetString("Tipo2");
 //BA.debugLineNum = 258;BA.debugLine="Spinner2.SelectedIndex=Spinner2.IndexOf(cual)";
mostCurrent._spinner2.setSelectedIndex(mostCurrent._spinner2.IndexOf(_cual));
 }else {
 //BA.debugLineNum = 260;BA.debugLine="Fecha.Text=lafecha";
mostCurrent._fecha.setText(BA.ObjectToCharSequence(_lafecha));
 //BA.debugLineNum = 261;BA.debugLine="Spinner1.SelectedIndex=0";
mostCurrent._spinner1.setSelectedIndex((int) (0));
 //BA.debugLineNum = 262;BA.debugLine="Spinner2.SelectedIndex=0";
mostCurrent._spinner2.setSelectedIndex((int) (0));
 };
 //BA.debugLineNum = 266;BA.debugLine="End Sub";
return "";
}
}
