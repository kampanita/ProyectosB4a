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

public class modulo2 extends Activity implements B4AActivity{
	public static modulo2 mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "Noe.Cal", "Noe.Cal.modulo2");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (modulo2).");
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
		activityBA = new BA(this, layout, processBA, "Noe.Cal", "Noe.Cal.modulo2");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "Noe.Cal.modulo2", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (modulo2) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (modulo2) Resume **");
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
		return modulo2.class;
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
            BA.LogInfo("** Activity (modulo2) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (modulo2) Pause event (activity is not paused). **");
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
            modulo2 mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (modulo2) Resume **");
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
public static anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public static anywheresoftware.b4a.sql.SQL _sql = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spinnerano = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spinnermes = null;
public b4a.example3.customlistview _totales = null;
public anywheresoftware.b4a.objects.LabelWrapper _ver_dia = null;
public anywheresoftware.b4a.objects.LabelWrapper _ver_horas = null;
public anywheresoftware.b4a.objects.LabelWrapper _ver_tipo = null;
public b4a.example3.customlistview _grid = null;
public anywheresoftware.b4a.objects.LabelWrapper _total_h_mes = null;
public anywheresoftware.b4a.objects.LabelWrapper _ver_tipo2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _ver_incidencia = null;
public anywheresoftware.b4a.objects.LabelWrapper _ver_dia2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _ver_horas2 = null;
public b4a.example.dateutils _dateutils = null;
public Noe.Cal.main _main = null;
public Noe.Cal.starter _starter = null;
public Noe.Cal.modulo1 _modulo1 = null;
public Noe.Cal.xuiviewsutils _xuiviewsutils = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 34;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 37;BA.debugLine="sql.Initialize(File.DirInternal, \"NoeCal.db\", Fal";
_sql.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"NoeCal.db",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 38;BA.debugLine="verRegistros";
_verregistros();
 //BA.debugLineNum = 39;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 45;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 46;BA.debugLine="If UserClosed Then";
if (_userclosed) { 
 //BA.debugLineNum = 47;BA.debugLine="Activity.LoadLayout(\"Layout\")";
mostCurrent._activity.LoadLayout("Layout",mostCurrent.activityBA);
 };
 //BA.debugLineNum = 49;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 41;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 42;BA.debugLine="verRegistros";
_verregistros();
 //BA.debugLineNum = 43;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.B4XViewWrapper  _crea_row(String _text1,String _text2,String _text3,String _text4,String _text5) throws Exception{
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;
 //BA.debugLineNum = 128;BA.debugLine="Sub crea_row(text1 As String,text2 As String,text3";
 //BA.debugLineNum = 129;BA.debugLine="Dim p As B4XView= xui.CreatePanel(\"\")";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = _xui.CreatePanel(processBA,"");
 //BA.debugLineNum = 130;BA.debugLine="p.SetLayoutAnimated(100,0,0,100%x,36dip)";
_p.SetLayoutAnimated((int) (100),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (36)));
 //BA.debugLineNum = 131;BA.debugLine="p.LoadLayout(\"ver_rows\")";
_p.LoadLayout("ver_rows",mostCurrent.activityBA);
 //BA.debugLineNum = 132;BA.debugLine="ver_dia.Text = text1";
mostCurrent._ver_dia.setText(BA.ObjectToCharSequence(_text1));
 //BA.debugLineNum = 133;BA.debugLine="ver_horas.Text = text2";
mostCurrent._ver_horas.setText(BA.ObjectToCharSequence(_text2));
 //BA.debugLineNum = 134;BA.debugLine="ver_tipo.Text=text3";
mostCurrent._ver_tipo.setText(BA.ObjectToCharSequence(_text3));
 //BA.debugLineNum = 135;BA.debugLine="ver_tipo2.text=text4";
mostCurrent._ver_tipo2.setText(BA.ObjectToCharSequence(_text4));
 //BA.debugLineNum = 136;BA.debugLine="ver_incidencia.Text=text5";
mostCurrent._ver_incidencia.setText(BA.ObjectToCharSequence(_text5));
 //BA.debugLineNum = 137;BA.debugLine="Return p";
if (true) return _p;
 //BA.debugLineNum = 138;BA.debugLine="End Sub";
return null;
}
public static anywheresoftware.b4a.objects.B4XViewWrapper  _crea_row2(String _text1,String _text2) throws Exception{
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;
 //BA.debugLineNum = 140;BA.debugLine="Sub crea_row2(text1 As String,text2 As String) As";
 //BA.debugLineNum = 141;BA.debugLine="Dim p As B4XView= xui.CreatePanel(\"\")";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = _xui.CreatePanel(processBA,"");
 //BA.debugLineNum = 142;BA.debugLine="p.SetLayoutAnimated(100,0,0,100%x,30dip)";
_p.SetLayoutAnimated((int) (100),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30)));
 //BA.debugLineNum = 143;BA.debugLine="p.LoadLayout(\"ver_rows2\")";
_p.LoadLayout("ver_rows2",mostCurrent.activityBA);
 //BA.debugLineNum = 144;BA.debugLine="ver_dia2.Text = text1";
mostCurrent._ver_dia2.setText(BA.ObjectToCharSequence(_text1));
 //BA.debugLineNum = 145;BA.debugLine="ver_horas2.Text = text2";
mostCurrent._ver_horas2.setText(BA.ObjectToCharSequence(_text2));
 //BA.debugLineNum = 147;BA.debugLine="Return p";
if (true) return _p;
 //BA.debugLineNum = 148;BA.debugLine="End Sub";
return null;
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 14;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 17;BA.debugLine="Private SpinnerAno As Spinner";
mostCurrent._spinnerano = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Private SpinnerMEs As Spinner";
mostCurrent._spinnermes = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Private Totales As CustomListView";
mostCurrent._totales = new b4a.example3.customlistview();
 //BA.debugLineNum = 22;BA.debugLine="Private ver_dia As Label";
mostCurrent._ver_dia = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Private ver_horas As Label";
mostCurrent._ver_horas = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Private ver_tipo As Label";
mostCurrent._ver_tipo = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Private Grid As CustomListView";
mostCurrent._grid = new b4a.example3.customlistview();
 //BA.debugLineNum = 26;BA.debugLine="Private total_h_mes As Label";
mostCurrent._total_h_mes = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private ver_tipo2 As Label";
mostCurrent._ver_tipo2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private ver_incidencia As Label";
mostCurrent._ver_incidencia = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Private ver_dia2 As Label";
mostCurrent._ver_dia2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Private ver_horas2 As Label";
mostCurrent._ver_horas2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 32;BA.debugLine="End Sub";
return "";
}
public static String  _grid_itemclick(int _index,Object _value) throws Exception{
 //BA.debugLineNum = 158;BA.debugLine="Private Sub Grid_ItemClick (Index As Int, Value As";
 //BA.debugLineNum = 159;BA.debugLine="CallSubDelayed2(\"modulo1\",\"ver_registro\",Value";
anywheresoftware.b4a.keywords.Common.CallSubDelayed2(processBA,(Object)("modulo1"),"ver_registro",_value);
 //BA.debugLineNum = 160;BA.debugLine="End Sub";
return "";
}
public static String  _grid_itemlongclick(int _index,Object _value) throws Exception{
 //BA.debugLineNum = 175;BA.debugLine="Private Sub Grid_ItemLongClick (Index As Int, Valu";
 //BA.debugLineNum = 176;BA.debugLine="CallSub2(\"modulo1\",\"ver_registro\",Value)";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)("modulo1"),"ver_registro",_value);
 //BA.debugLineNum = 177;BA.debugLine="End Sub";
return "";
}
public static String  _leer_registros() throws Exception{
anywheresoftware.b4a.sql.SQL.ResultSetWrapper _rs = null;
String _where = "";
String _h = "";
double _total_dia_normal = 0;
double _total_dia_festivo = 0;
double _total_dia_festivo_extra = 0;
double _total_noche_normal = 0;
double _total_noche_festivo = 0;
double _total_noche_festivo_extra = 0;
double _total_global = 0;
anywheresoftware.b4a.objects.collections.List _lista_tipos = null;
 //BA.debugLineNum = 73;BA.debugLine="Sub leer_registros";
 //BA.debugLineNum = 74;BA.debugLine="Dim rs As ResultSet";
_rs = new anywheresoftware.b4a.sql.SQL.ResultSetWrapper();
 //BA.debugLineNum = 75;BA.debugLine="Dim where As String";
_where = "";
 //BA.debugLineNum = 76;BA.debugLine="where=\"%\" & SpinnerMEs.SelectedItem & \"%\" & Spinn";
_where = "%"+mostCurrent._spinnermes.getSelectedItem()+"%"+mostCurrent._spinnerano.getSelectedItem()+"%";
 //BA.debugLineNum = 80;BA.debugLine="rs=sql.ExecQuery2(\"select Dia,Horas,Tipo,Tipo2,in";
_rs = (anywheresoftware.b4a.sql.SQL.ResultSetWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.ResultSetWrapper(), (android.database.Cursor)(_sql.ExecQuery2("select Dia,Horas,Tipo,Tipo2,incidencia from dias where dia like ? order by Dia asc",new String[]{_where})));
 //BA.debugLineNum = 82;BA.debugLine="Grid.Clear";
mostCurrent._grid._clear();
 //BA.debugLineNum = 83;BA.debugLine="Do While rs.NextRow";
while (_rs.NextRow()) {
 //BA.debugLineNum = 84;BA.debugLine="Dim h As String";
_h = "";
 //BA.debugLineNum = 85;BA.debugLine="h=rs.GetDouble(\"Horas\")";
_h = BA.NumberToString(_rs.GetDouble("Horas"));
 //BA.debugLineNum = 86;BA.debugLine="Grid.Add(crea_row(rs.GetString(\"Dia\"),h,rs.GetSt";
mostCurrent._grid._add(_crea_row(_rs.GetString("Dia"),_h,_rs.GetString("Tipo"),_rs.GetString("Tipo2"),_rs.GetString("Incidencia")),(Object)(_rs.GetString("Dia")));
 }
;
 //BA.debugLineNum = 90;BA.debugLine="Dim Total_dia_normal As Double";
_total_dia_normal = 0;
 //BA.debugLineNum = 91;BA.debugLine="Dim Total_dia_festivo As Double";
_total_dia_festivo = 0;
 //BA.debugLineNum = 92;BA.debugLine="Dim Total_dia_festivo_extra As Double";
_total_dia_festivo_extra = 0;
 //BA.debugLineNum = 93;BA.debugLine="Dim Total_noche_normal As Double";
_total_noche_normal = 0;
 //BA.debugLineNum = 94;BA.debugLine="Dim Total_noche_festivo As Double";
_total_noche_festivo = 0;
 //BA.debugLineNum = 95;BA.debugLine="Dim Total_noche_festivo_extra As Double";
_total_noche_festivo_extra = 0;
 //BA.debugLineNum = 96;BA.debugLine="Dim Total_Global As Double";
_total_global = 0;
 //BA.debugLineNum = 99;BA.debugLine="Dim Lista_tipos As List";
_lista_tipos = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 100;BA.debugLine="Lista_tipos.initialize";
_lista_tipos.Initialize();
 //BA.debugLineNum = 101;BA.debugLine="Lista_tipos.addall(Array As String(\"Dia Normal\",\"";
_lista_tipos.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"Dia Normal","Dia Festivo Ordinario","Dia Festivo Extraordinario","Noche Normal","Noche Festivo Ordinario","Noche Festivo Extraordinario"}));
 //BA.debugLineNum = 103;BA.debugLine="Total_dia_normal=sql.ExecQuerySingleResult2(\"sele";
_total_dia_normal = (double)(Double.parseDouble(_sql.ExecQuerySingleResult2("select total(Horas) from dias where dia like ? and tipo=? order by dia asc",new String[]{_where,BA.ObjectToString(_lista_tipos.Get((int) (0)))})));
 //BA.debugLineNum = 105;BA.debugLine="Total_dia_festivo=sql.ExecQuerySingleResult2(\"sel";
_total_dia_festivo = (double)(Double.parseDouble(_sql.ExecQuerySingleResult2("select ifnull(total(Horas),0) from dias where dia like ? and tipo=? ",new String[]{_where,BA.ObjectToString(_lista_tipos.Get((int) (1)))})));
 //BA.debugLineNum = 106;BA.debugLine="Total_dia_festivo_extra=sql.ExecQuerySingleResult";
_total_dia_festivo_extra = (double)(Double.parseDouble(_sql.ExecQuerySingleResult2("select total(Horas) from dias where dia like ? and tipo=? ",new String[]{_where,BA.ObjectToString(_lista_tipos.Get((int) (2)))})));
 //BA.debugLineNum = 107;BA.debugLine="Total_noche_normal=sql.ExecQuerySingleResult2(\"se";
_total_noche_normal = (double)(Double.parseDouble(_sql.ExecQuerySingleResult2("select total(Horas) from dias where dia like ? and tipo=? ",new String[]{_where,BA.ObjectToString(_lista_tipos.Get((int) (3)))})));
 //BA.debugLineNum = 108;BA.debugLine="Total_noche_festivo=sql.ExecQuerySingleResult2(\"s";
_total_noche_festivo = (double)(Double.parseDouble(_sql.ExecQuerySingleResult2("select total(Horas) from dias where dia like ? and tipo=? ",new String[]{_where,BA.ObjectToString(_lista_tipos.Get((int) (4)))})));
 //BA.debugLineNum = 109;BA.debugLine="Total_noche_festivo_extra = sql.ExecQuerySingleRe";
_total_noche_festivo_extra = (double)(Double.parseDouble(_sql.ExecQuerySingleResult2("select total(Horas) from dias where dia like ? and tipo=? ",new String[]{_where,BA.ObjectToString(_lista_tipos.Get((int) (5)))})));
 //BA.debugLineNum = 110;BA.debugLine="Total_Global = sql.ExecQuerySingleResult2(\"select";
_total_global = (double)(Double.parseDouble(_sql.ExecQuerySingleResult2("select total(Horas) from dias where dia like	 ? ",new String[]{_where})));
 //BA.debugLineNum = 114;BA.debugLine="Totales.Clear";
mostCurrent._totales._clear();
 //BA.debugLineNum = 115;BA.debugLine="Totales.Add(crea_row2(\"Total Horas dia normal\", T";
mostCurrent._totales._add(_crea_row2("Total Horas dia normal",BA.NumberToString(_total_dia_normal)),(Object)(_rs.getObject()));
 //BA.debugLineNum = 116;BA.debugLine="Totales.Add(crea_row2(\"Total Horas dia festivo or";
mostCurrent._totales._add(_crea_row2("Total Horas dia festivo ordinario",BA.NumberToString(_total_dia_festivo)),(Object)(_rs.getObject()));
 //BA.debugLineNum = 117;BA.debugLine="Totales.Add(crea_row2(\"Total Horas dia festivo ex";
mostCurrent._totales._add(_crea_row2("Total Horas dia festivo extraordinario",BA.NumberToString(_total_dia_festivo_extra)),(Object)(_rs.getObject()));
 //BA.debugLineNum = 118;BA.debugLine="Totales.Add(crea_row2(\"Total Horas noche normales";
mostCurrent._totales._add(_crea_row2("Total Horas noche normales",BA.NumberToString(_total_noche_normal)),(Object)(_rs.getObject()));
 //BA.debugLineNum = 119;BA.debugLine="Totales.Add(crea_row2(\"Total Horas noche festivo";
mostCurrent._totales._add(_crea_row2("Total Horas noche festivo ordinario",BA.NumberToString(_total_noche_festivo)),(Object)(_rs.getObject()));
 //BA.debugLineNum = 120;BA.debugLine="Totales.Add(crea_row2(\"Total Horas noche festivo";
mostCurrent._totales._add(_crea_row2("Total Horas noche festivo extraordinario",BA.NumberToString(_total_noche_festivo_extra)),(Object)(_rs.getObject()));
 //BA.debugLineNum = 122;BA.debugLine="total_h_mes.Text=\"TOTAL HORAS MES: \" & Total_Glob";
mostCurrent._total_h_mes.setText(BA.ObjectToCharSequence("TOTAL HORAS MES: "+BA.NumberToString(_total_global)));
 //BA.debugLineNum = 124;BA.debugLine="rs=sql.ExecQuery(\"select Dia,Horas,Tipo,Tipo2,inc";
_rs = (anywheresoftware.b4a.sql.SQL.ResultSetWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.ResultSetWrapper(), (android.database.Cursor)(_sql.ExecQuery("select Dia,Horas,Tipo,Tipo2,incidencia from dias order by Dia asc")));
 //BA.debugLineNum = 126;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Private xui As XUI";
_xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 10;BA.debugLine="Dim sql As SQL";
_sql = new anywheresoftware.b4a.sql.SQL();
 //BA.debugLineNum = 12;BA.debugLine="End Sub";
return "";
}
public static String  _spinnerano_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 154;BA.debugLine="Private Sub SpinnerAno_ItemClick (Position As Int,";
 //BA.debugLineNum = 155;BA.debugLine="leer_registros";
_leer_registros();
 //BA.debugLineNum = 156;BA.debugLine="End Sub";
return "";
}
public static String  _spinnermes_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 150;BA.debugLine="Private Sub SpinnerMEs_ItemClick (Position As Int,";
 //BA.debugLineNum = 151;BA.debugLine="leer_registros";
_leer_registros();
 //BA.debugLineNum = 152;BA.debugLine="End Sub";
return "";
}
public static String  _ver_dia_click() throws Exception{
 //BA.debugLineNum = 169;BA.debugLine="Private Sub ver_dia_Click";
 //BA.debugLineNum = 173;BA.debugLine="End Sub";
return "";
}
public static String  _ver_horas_click() throws Exception{
 //BA.debugLineNum = 162;BA.debugLine="Private Sub ver_horas_Click";
 //BA.debugLineNum = 167;BA.debugLine="End Sub";
return "";
}
public static String  _verregistros() throws Exception{
anywheresoftware.b4a.objects.collections.List _años = null;
anywheresoftware.b4a.objects.collections.List _meses = null;
int _a = 0;
 //BA.debugLineNum = 51;BA.debugLine="Sub verRegistros";
 //BA.debugLineNum = 53;BA.debugLine="Activity.LoadLayout(\"ver\")";
mostCurrent._activity.LoadLayout("ver",mostCurrent.activityBA);
 //BA.debugLineNum = 54;BA.debugLine="Dim años As List";
_años = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 55;BA.debugLine="Dim meses As List";
_meses = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 56;BA.debugLine="años.Initialize";
_años.Initialize();
 //BA.debugLineNum = 57;BA.debugLine="meses.Initialize";
_meses.Initialize();
 //BA.debugLineNum = 59;BA.debugLine="DateTime.DateFormat=\"yyyy\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy");
 //BA.debugLineNum = 60;BA.debugLine="For a=DateTime.GetYear(DateTime.Now)-5 To DateTim";
{
final int step7 = 1;
final int limit7 = (int) (anywheresoftware.b4a.keywords.Common.DateTime.GetYear(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+5);
_a = (int) (anywheresoftware.b4a.keywords.Common.DateTime.GetYear(anywheresoftware.b4a.keywords.Common.DateTime.getNow())-5) ;
for (;_a <= limit7 ;_a = _a + step7 ) {
 //BA.debugLineNum = 61;BA.debugLine="años.Add(a)";
_años.Add((Object)(_a));
 }
};
 //BA.debugLineNum = 63;BA.debugLine="SpinnerAno.AddAll(años)";
mostCurrent._spinnerano.AddAll(_años);
 //BA.debugLineNum = 64;BA.debugLine="DateTime.DateFormat=\"MMM\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("MMM");
 //BA.debugLineNum = 65;BA.debugLine="meses=DateUtils.GetMonthsNames";
_meses = mostCurrent._dateutils._getmonthsnames(mostCurrent.activityBA);
 //BA.debugLineNum = 66;BA.debugLine="SpinnerMEs.AddAll(meses)";
mostCurrent._spinnermes.AddAll(_meses);
 //BA.debugLineNum = 67;BA.debugLine="SpinnerAno.SelectedIndex=5 '<< este año";
mostCurrent._spinnerano.setSelectedIndex((int) (5));
 //BA.debugLineNum = 68;BA.debugLine="SpinnerMEs.SelectedIndex=DateTime.GetMonth(DateTi";
mostCurrent._spinnermes.setSelectedIndex((int) (anywheresoftware.b4a.keywords.Common.DateTime.GetMonth(anywheresoftware.b4a.keywords.Common.DateTime.getNow())-1));
 //BA.debugLineNum = 69;BA.debugLine="leer_registros";
_leer_registros();
 //BA.debugLineNum = 71;BA.debugLine="End Sub";
return "";
}
}
