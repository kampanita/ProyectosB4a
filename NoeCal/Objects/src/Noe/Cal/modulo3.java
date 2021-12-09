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

public class modulo3 extends Activity implements B4AActivity{
	public static modulo3 mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "Noe.Cal", "Noe.Cal.modulo3");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (modulo3).");
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
		activityBA = new BA(this, layout, processBA, "Noe.Cal", "Noe.Cal.modulo3");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "Noe.Cal.modulo3", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (modulo3) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (modulo3) Resume **");
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
		return modulo3.class;
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
            BA.LogInfo("** Activity (modulo3) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (modulo3) Pause event (activity is not paused). **");
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
            modulo3 mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (modulo3) Resume **");
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
public b4a.example3.customlistview _grid2 = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spinnerano2 = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spinnermes2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _cal_dia1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _cal_dia2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _cal_dia3 = null;
public anywheresoftware.b4a.objects.LabelWrapper _cal_dia4 = null;
public anywheresoftware.b4a.objects.LabelWrapper _cal_dia5 = null;
public anywheresoftware.b4a.objects.LabelWrapper _cal_dia6 = null;
public anywheresoftware.b4a.objects.LabelWrapper _cal_dia7 = null;
public anywheresoftware.b4a.objects.LabelWrapper _cal_tipo1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _cal_tipo2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _cal_tipo3 = null;
public anywheresoftware.b4a.objects.LabelWrapper _cal_tipo4 = null;
public anywheresoftware.b4a.objects.LabelWrapper _cal_tipo5 = null;
public anywheresoftware.b4a.objects.LabelWrapper _cal_tipo6 = null;
public anywheresoftware.b4a.objects.LabelWrapper _cal_tipo7 = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel6 = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel7 = null;
public b4a.example.dateutils _dateutils = null;
public Noe.Cal.main _main = null;
public Noe.Cal.starter _starter = null;
public Noe.Cal.modulo1 _modulo1 = null;
public Noe.Cal.modulo2 _modulo2 = null;
public Noe.Cal.xuiviewsutils _xuiviewsutils = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 37;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 40;BA.debugLine="Activity.LoadLayout(\"ver2\")";
mostCurrent._activity.LoadLayout("ver2",mostCurrent.activityBA);
 //BA.debugLineNum = 41;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 54;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 55;BA.debugLine="If UserClosed Then";
if (_userclosed) { 
 //BA.debugLineNum = 56;BA.debugLine="Activity.LoadLayout(\"Layout\")";
mostCurrent._activity.LoadLayout("Layout",mostCurrent.activityBA);
 };
 //BA.debugLineNum = 58;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
String _mes = "";
String _año = "";
 //BA.debugLineNum = 43;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 44;BA.debugLine="DateTime.DateFormat=\"m\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("m");
 //BA.debugLineNum = 45;BA.debugLine="Log(DateTime.Now)";
anywheresoftware.b4a.keywords.Common.LogImpl("32949122",BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),0);
 //BA.debugLineNum = 46;BA.debugLine="Log(DateTime.Date(DateTime.Now))";
anywheresoftware.b4a.keywords.Common.LogImpl("32949123",anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),0);
 //BA.debugLineNum = 47;BA.debugLine="Dim mes As String =DateTime.Date(DateTime.Now)";
_mes = anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 48;BA.debugLine="DateTime.DateFormat=\"yyyy\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy");
 //BA.debugLineNum = 49;BA.debugLine="Dim año As String =DateTime.Date(DateTime.Now)";
_año = anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 50;BA.debugLine="VerCalendario(mes, año)";
_vercalendario((int)(Double.parseDouble(_mes)),(int)(Double.parseDouble(_año)));
 //BA.debugLineNum = 52;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.B4XViewWrapper  _crea_dia(anywheresoftware.b4a.objects.collections.List _lista) throws Exception{
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;
String _where = "";
anywheresoftware.b4a.sql.SQL.ResultSetWrapper _rs = null;
anywheresoftware.b4a.objects.collections.List _lista_tipos = null;
Object _ddd = null;
 //BA.debugLineNum = 103;BA.debugLine="Sub crea_dia(lista As List) As B4XView";
 //BA.debugLineNum = 104;BA.debugLine="Dim p As B4XView= xui.CreatePanel(\"\")";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = _xui.CreatePanel(processBA,"");
 //BA.debugLineNum = 105;BA.debugLine="p.SetLayoutAnimated(100,0,0,100%x,36dip)";
_p.SetLayoutAnimated((int) (100),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (36)));
 //BA.debugLineNum = 106;BA.debugLine="p.LoadLayout(\"ver_cal_row\")";
_p.LoadLayout("ver_cal_row",mostCurrent.activityBA);
 //BA.debugLineNum = 107;BA.debugLine="Dim where As String";
_where = "";
 //BA.debugLineNum = 108;BA.debugLine="Dim rs As ResultSet";
_rs = new anywheresoftware.b4a.sql.SQL.ResultSetWrapper();
 //BA.debugLineNum = 109;BA.debugLine="Dim lista_tipos As List";
_lista_tipos = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 110;BA.debugLine="lista_tipos.Initialize";
_lista_tipos.Initialize();
 //BA.debugLineNum = 111;BA.debugLine="For Each ddd In lista";
{
final anywheresoftware.b4a.BA.IterableList group8 = _lista;
final int groupLen8 = group8.getSize()
;int index8 = 0;
;
for (; index8 < groupLen8;index8++){
_ddd = group8.Get(index8);
 //BA.debugLineNum = 112;BA.debugLine="where = ddd & \" \" & SpinnerMEs2.SelectedItem&\" \"";
_where = BA.ObjectToString(_ddd)+" "+mostCurrent._spinnermes2.getSelectedItem()+" "+mostCurrent._spinnerano2.getSelectedItem();
 //BA.debugLineNum = 113;BA.debugLine="rs=sql.ExecQuery2(\"select tipo2 from Dias where";
_rs = (anywheresoftware.b4a.sql.SQL.ResultSetWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.ResultSetWrapper(), (android.database.Cursor)(_sql.ExecQuery2("select tipo2 from Dias where Dia=?",new String[]{_where})));
 //BA.debugLineNum = 114;BA.debugLine="If rs.RowCount>1 Then";
if (_rs.getRowCount()>1) { 
 //BA.debugLineNum = 115;BA.debugLine="lista_tipos.add(rs.GetString(\"Tipo\"))";
_lista_tipos.Add((Object)(_rs.GetString("Tipo")));
 };
 }
};
 //BA.debugLineNum = 118;BA.debugLine="cal_dia1.Text=lista.Get(0)";
mostCurrent._cal_dia1.setText(BA.ObjectToCharSequence(_lista.Get((int) (0))));
 //BA.debugLineNum = 119;BA.debugLine="cal_tipo1.Text=lista_tipos.Get(0)";
mostCurrent._cal_tipo1.setText(BA.ObjectToCharSequence(_lista_tipos.Get((int) (0))));
 //BA.debugLineNum = 120;BA.debugLine="Select cal_tipo1.Text.SubString(1)";
switch (BA.switchObjectToInt(mostCurrent._cal_tipo1.getText().substring((int) (1)),"M","N","R")) {
case 0: {
 //BA.debugLineNum = 122;BA.debugLine="cal_tipo1.Color=xui.Color_Red";
mostCurrent._cal_tipo1.setColor(_xui.Color_Red);
 break; }
case 1: {
 //BA.debugLineNum = 124;BA.debugLine="cal_tipo1.Color=xui.Color_Magenta";
mostCurrent._cal_tipo1.setColor(_xui.Color_Magenta);
 break; }
case 2: {
 //BA.debugLineNum = 126;BA.debugLine="cal_tipo1.Color=xui.Color_Green";
mostCurrent._cal_tipo1.setColor(_xui.Color_Green);
 break; }
}
;
 //BA.debugLineNum = 128;BA.debugLine="cal_dia2.Text=lista.Get(1)";
mostCurrent._cal_dia2.setText(BA.ObjectToCharSequence(_lista.Get((int) (1))));
 //BA.debugLineNum = 129;BA.debugLine="cal_tipo2.Text=lista_tipos.Get(1)";
mostCurrent._cal_tipo2.setText(BA.ObjectToCharSequence(_lista_tipos.Get((int) (1))));
 //BA.debugLineNum = 130;BA.debugLine="cal_dia3.Text=lista.Get(2)";
mostCurrent._cal_dia3.setText(BA.ObjectToCharSequence(_lista.Get((int) (2))));
 //BA.debugLineNum = 131;BA.debugLine="cal_tipo3.Text=lista_tipos.Get(2)";
mostCurrent._cal_tipo3.setText(BA.ObjectToCharSequence(_lista_tipos.Get((int) (2))));
 //BA.debugLineNum = 132;BA.debugLine="cal_dia4.Text=lista.Get(3)";
mostCurrent._cal_dia4.setText(BA.ObjectToCharSequence(_lista.Get((int) (3))));
 //BA.debugLineNum = 133;BA.debugLine="cal_tipo4.Text=lista_tipos.Get(3)";
mostCurrent._cal_tipo4.setText(BA.ObjectToCharSequence(_lista_tipos.Get((int) (3))));
 //BA.debugLineNum = 134;BA.debugLine="cal_dia5.Text=lista.Get(4)";
mostCurrent._cal_dia5.setText(BA.ObjectToCharSequence(_lista.Get((int) (4))));
 //BA.debugLineNum = 135;BA.debugLine="cal_tipo5.Text=lista_tipos.Get(4)";
mostCurrent._cal_tipo5.setText(BA.ObjectToCharSequence(_lista_tipos.Get((int) (4))));
 //BA.debugLineNum = 136;BA.debugLine="cal_dia6.Text=lista.Get(5)";
mostCurrent._cal_dia6.setText(BA.ObjectToCharSequence(_lista.Get((int) (5))));
 //BA.debugLineNum = 137;BA.debugLine="cal_tipo6.Text=lista_tipos.Get(5)";
mostCurrent._cal_tipo6.setText(BA.ObjectToCharSequence(_lista_tipos.Get((int) (5))));
 //BA.debugLineNum = 138;BA.debugLine="cal_dia7.Text=lista.Get(6)";
mostCurrent._cal_dia7.setText(BA.ObjectToCharSequence(_lista.Get((int) (6))));
 //BA.debugLineNum = 139;BA.debugLine="cal_tipo7.Text=lista_tipos.Get(6)";
mostCurrent._cal_tipo7.setText(BA.ObjectToCharSequence(_lista_tipos.Get((int) (6))));
 //BA.debugLineNum = 141;BA.debugLine="Return p";
if (true) return _p;
 //BA.debugLineNum = 142;BA.debugLine="End Sub";
return null;
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 13;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 16;BA.debugLine="Private Grid2 As CustomListView";
mostCurrent._grid2 = new b4a.example3.customlistview();
 //BA.debugLineNum = 17;BA.debugLine="Private SpinnerAno2 As Spinner";
mostCurrent._spinnerano2 = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Private SpinnerMEs2 As Spinner";
mostCurrent._spinnermes2 = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Private cal_dia1 As Label";
mostCurrent._cal_dia1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Private cal_dia2 As Label";
mostCurrent._cal_dia2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Private cal_dia3 As Label";
mostCurrent._cal_dia3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Private cal_dia4 As Label";
mostCurrent._cal_dia4 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Private cal_dia5 As Label";
mostCurrent._cal_dia5 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Private cal_dia6 As Label";
mostCurrent._cal_dia6 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Private cal_dia7 As Label";
mostCurrent._cal_dia7 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Private cal_tipo1 As Label";
mostCurrent._cal_tipo1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private cal_tipo2 As Label";
mostCurrent._cal_tipo2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private cal_tipo3 As Label";
mostCurrent._cal_tipo3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Private cal_tipo4 As Label";
mostCurrent._cal_tipo4 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Private cal_tipo5 As Label";
mostCurrent._cal_tipo5 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Private cal_tipo6 As Label";
mostCurrent._cal_tipo6 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Private cal_tipo7 As Label";
mostCurrent._cal_tipo7 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Private Panel6 As Panel";
mostCurrent._panel6 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Private Panel7 As Panel";
mostCurrent._panel7 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 35;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Dim xui As XUI";
_xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 10;BA.debugLine="Dim sql As SQL";
_sql = new anywheresoftware.b4a.sql.SQL();
 //BA.debugLineNum = 11;BA.debugLine="End Sub";
return "";
}
public static String  _spinnerano2_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 148;BA.debugLine="Private Sub SpinnerAno2_ItemClick (Position As Int";
 //BA.debugLineNum = 150;BA.debugLine="End Sub";
return "";
}
public static String  _spinnermes2_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 144;BA.debugLine="Private Sub SpinnerMEs2_ItemClick (Position As Int";
 //BA.debugLineNum = 146;BA.debugLine="End Sub";
return "";
}
public static String  _vercalendario(int _mes,int _año) throws Exception{
int _dias_mes = 0;
anywheresoftware.b4a.objects.collections.List _lista_dias = null;
int _i = 0;
 //BA.debugLineNum = 60;BA.debugLine="Sub VerCalendario(mes As Int, año As Int)";
 //BA.debugLineNum = 61;BA.debugLine="Grid2.Clear";
mostCurrent._grid2._clear();
 //BA.debugLineNum = 62;BA.debugLine="Dim dias_mes As Int";
_dias_mes = 0;
 //BA.debugLineNum = 64;BA.debugLine="dias_mes=DateUtils.NumberOfDaysInMonth(mes,año)";
_dias_mes = mostCurrent._dateutils._numberofdaysinmonth(mostCurrent.activityBA,_mes,_año);
 //BA.debugLineNum = 65;BA.debugLine="Log(dias_mes)";
anywheresoftware.b4a.keywords.Common.LogImpl("33080197",BA.NumberToString(_dias_mes),0);
 //BA.debugLineNum = 67;BA.debugLine="Dim lista_dias As List";
_lista_dias = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 68;BA.debugLine="lista_dias.initialize";
_lista_dias.Initialize();
 //BA.debugLineNum = 69;BA.debugLine="For i=1 To 7";
{
final int step7 = 1;
final int limit7 = (int) (7);
_i = (int) (1) ;
for (;_i <= limit7 ;_i = _i + step7 ) {
 //BA.debugLineNum = 70;BA.debugLine="lista_dias.Add(i)";
_lista_dias.Add((Object)(_i));
 }
};
 //BA.debugLineNum = 72;BA.debugLine="Grid2.Add(crea_dia(lista_dias),1)";
mostCurrent._grid2._add(_crea_dia(_lista_dias),(Object)(1));
 //BA.debugLineNum = 74;BA.debugLine="lista_dias.initialize";
_lista_dias.Initialize();
 //BA.debugLineNum = 75;BA.debugLine="For i=8 To 14";
{
final int step12 = 1;
final int limit12 = (int) (14);
_i = (int) (8) ;
for (;_i <= limit12 ;_i = _i + step12 ) {
 //BA.debugLineNum = 76;BA.debugLine="lista_dias.Add(i)";
_lista_dias.Add((Object)(_i));
 }
};
 //BA.debugLineNum = 78;BA.debugLine="Grid2.Add(crea_dia(lista_dias),2)";
mostCurrent._grid2._add(_crea_dia(_lista_dias),(Object)(2));
 //BA.debugLineNum = 80;BA.debugLine="lista_dias.Initialize";
_lista_dias.Initialize();
 //BA.debugLineNum = 81;BA.debugLine="For i=15 To 21";
{
final int step17 = 1;
final int limit17 = (int) (21);
_i = (int) (15) ;
for (;_i <= limit17 ;_i = _i + step17 ) {
 //BA.debugLineNum = 82;BA.debugLine="lista_dias.Add(i)";
_lista_dias.Add((Object)(_i));
 }
};
 //BA.debugLineNum = 84;BA.debugLine="Grid2.Add(crea_dia(lista_dias),3)";
mostCurrent._grid2._add(_crea_dia(_lista_dias),(Object)(3));
 //BA.debugLineNum = 86;BA.debugLine="lista_dias.Initialize";
_lista_dias.Initialize();
 //BA.debugLineNum = 87;BA.debugLine="For i=22 To 28";
{
final int step22 = 1;
final int limit22 = (int) (28);
_i = (int) (22) ;
for (;_i <= limit22 ;_i = _i + step22 ) {
 //BA.debugLineNum = 88;BA.debugLine="lista_dias.Add(i)";
_lista_dias.Add((Object)(_i));
 }
};
 //BA.debugLineNum = 90;BA.debugLine="Grid2.Add(crea_dia(lista_dias),4)";
mostCurrent._grid2._add(_crea_dia(_lista_dias),(Object)(4));
 //BA.debugLineNum = 92;BA.debugLine="If dias_mes >= 29 Then";
if (_dias_mes>=29) { 
 //BA.debugLineNum = 93;BA.debugLine="For i=29 To dias_mes";
{
final int step27 = 1;
final int limit27 = _dias_mes;
_i = (int) (29) ;
for (;_i <= limit27 ;_i = _i + step27 ) {
 //BA.debugLineNum = 94;BA.debugLine="lista_dias.Add(i)";
_lista_dias.Add((Object)(_i));
 }
};
 //BA.debugLineNum = 96;BA.debugLine="Grid2.Add(crea_dia(lista_dias),5)";
mostCurrent._grid2._add(_crea_dia(_lista_dias),(Object)(5));
 };
 //BA.debugLineNum = 101;BA.debugLine="End Sub";
return "";
}
}
