package iNKuBo.Compras;


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
			processBA = new BA(this.getApplicationContext(), null, null, "iNKuBo.Compras", "iNKuBo.Compras.main");
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
		activityBA = new BA(this, layout, processBA, "iNKuBo.Compras", "iNKuBo.Compras.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "iNKuBo.Compras.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
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
public static String _key = "";
public static String _access_token = "";
public static de.donmanfred.dbxv2.DropboxWrapper _dropbox = null;
public static de.donmanfred.dbxv2.DbxClientV2Wrapper _client = null;
public static de.donmanfred.dbxv2.DbxRequestConfigWrapper _config = null;
public static de.donmanfred.dbxv2.files.DbxUserFilesRequestsWrapper _dbxfiles = null;
public static anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public static String _api_dev_key = "";
public static String _api_paste_code = "";
public static String _api_user_key = "";
public static String _url = "";
public static String _data = "";
public static anywheresoftware.b4a.objects.collections.Map _parseddata = null;
public static String _user = "";
public static String _pass = "";
public static String _fichero = "";
public static anywheresoftware.b4a.objects.collections.List _lisss = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button1 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext1 = null;
public anywheresoftware.b4a.objects.ListViewWrapper _listview1 = null;
public static String _fichero_datos = "";
public static boolean _borrado = false;
public iNKuBo.Compras.starter _starter = null;
public iNKuBo.Compras.httputils2service _httputils2service = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 73;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 79;BA.debugLine="api_dev_key=\"vgweVFVugGl14JEiE3_rGy8KjWaz5UwQ\"";
_api_dev_key = "vgweVFVugGl14JEiE3_rGy8KjWaz5UwQ";
 //BA.debugLineNum = 80;BA.debugLine="user =\"ionkepa\"";
_user = "ionkepa";
 //BA.debugLineNum = 81;BA.debugLine="pass=\"230173230173\"";
_pass = "230173230173";
 //BA.debugLineNum = 84;BA.debugLine="Activity.LoadLayout(\"Compras\")";
mostCurrent._activity.LoadLayout("Compras",mostCurrent.activityBA);
 //BA.debugLineNum = 85;BA.debugLine="lisss.Initialize";
_lisss.Initialize();
 //BA.debugLineNum = 87;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 88;BA.debugLine="Leer_de_Dropbox";
_leer_de_dropbox();
 }else {
 //BA.debugLineNum = 91;BA.debugLine="Leer_de_disco";
_leer_de_disco();
 };
 //BA.debugLineNum = 94;BA.debugLine="End Sub";
return "";
}
public static void  _activity_pause(boolean _userclosed) throws Exception{
ResumableSub_Activity_Pause rsub = new ResumableSub_Activity_Pause(null,_userclosed);
rsub.resume(processBA, null);
}
public static class ResumableSub_Activity_Pause extends BA.ResumableSub {
public ResumableSub_Activity_Pause(iNKuBo.Compras.main parent,boolean _userclosed) {
this.parent = parent;
this._userclosed = _userclosed;
}
iNKuBo.Compras.main parent;
boolean _userclosed;
boolean _result = false;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 104;BA.debugLine="Log(\"en pausa\")";
anywheresoftware.b4a.keywords.Common.LogImpl("8262145","en pausa",0);
 //BA.debugLineNum = 105;BA.debugLine="If UserClosed Then";
if (true) break;

case 1:
//if
this.state = 6;
if (_userclosed) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 106;BA.debugLine="Wait for(Guardar_en_internet_Dropbox) Complete (";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _guardar_en_internet_dropbox());
this.state = 7;
return;
case 7:
//C
this.state = 6;
_result = (Boolean) result[0];
;
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 110;BA.debugLine="Guardar_en_disco";
_guardar_en_disco();
 //BA.debugLineNum = 111;BA.debugLine="Wait for(Guardar_en_internet_Dropbox) Complete (";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _guardar_en_internet_dropbox());
this.state = 8;
return;
case 8:
//C
this.state = 6;
_result = (Boolean) result[0];
;
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 114;BA.debugLine="Log(\"Sigo en pausa\")";
anywheresoftware.b4a.keywords.Common.LogImpl("8262155","Sigo en pausa",0);
 //BA.debugLineNum = 115;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _complete(boolean _result) throws Exception{
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 99;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 101;BA.debugLine="End Sub";
return "";
}
public static String  _button1_click() throws Exception{
String _trimstring = "";
String _a = "";
 //BA.debugLineNum = 117;BA.debugLine="Sub Button1_Click";
 //BA.debugLineNum = 118;BA.debugLine="If EditText1.Text<>\"\" Then";
if ((mostCurrent._edittext1.getText()).equals("") == false) { 
 //BA.debugLineNum = 119;BA.debugLine="Dim trimstring As String";
_trimstring = "";
 //BA.debugLineNum = 120;BA.debugLine="Dim a As String";
_a = "";
 //BA.debugLineNum = 121;BA.debugLine="a = EditText1.Text";
_a = mostCurrent._edittext1.getText();
 //BA.debugLineNum = 122;BA.debugLine="trimstring = a.SubString2(0,1).ToUpperCase & a.S";
_trimstring = _a.substring((int) (0),(int) (1)).toUpperCase()+_a.substring((int) (1)).toLowerCase();
 //BA.debugLineNum = 123;BA.debugLine="ListView1.AddSingleLine (trimstring)";
mostCurrent._listview1.AddSingleLine(BA.ObjectToCharSequence(_trimstring));
 //BA.debugLineNum = 124;BA.debugLine="ListView1.SingleLineLayout.Label.Color=Colors.D";
mostCurrent._listview1.getSingleLineLayout().Label.setColor(anywheresoftware.b4a.keywords.Common.Colors.DarkGray);
 //BA.debugLineNum = 125;BA.debugLine="EditText1.Text=\"\"";
mostCurrent._edittext1.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 128;BA.debugLine="guardar_en_memoria";
_guardar_en_memoria();
 };
 //BA.debugLineNum = 130;BA.debugLine="End Sub";
return "";
}
public static String  _button2_click() throws Exception{
 //BA.debugLineNum = 591;BA.debugLine="Private Sub Button2_Click";
 //BA.debugLineNum = 592;BA.debugLine="Leer_de_Dropbox";
_leer_de_dropbox();
 //BA.debugLineNum = 593;BA.debugLine="End Sub";
return "";
}
public static String  _button3_click() throws Exception{
 //BA.debugLineNum = 587;BA.debugLine="Private Sub Button3_Click";
 //BA.debugLineNum = 588;BA.debugLine="Guardar_en_internet_Dropbox";
_guardar_en_internet_dropbox();
 //BA.debugLineNum = 589;BA.debugLine="End Sub";
return "";
}
public static String  _cargar_lista(anywheresoftware.b4a.objects.collections.List _lista) throws Exception{
int _i = 0;
 //BA.debugLineNum = 451;BA.debugLine="Sub cargar_lista(lista As List)";
 //BA.debugLineNum = 452;BA.debugLine="Log(\"Cargo la lista\" & lista)";
anywheresoftware.b4a.keywords.Common.LogImpl("8851969","Cargo la lista"+BA.ObjectToString(_lista),0);
 //BA.debugLineNum = 453;BA.debugLine="ListView1.Clear";
mostCurrent._listview1.Clear();
 //BA.debugLineNum = 454;BA.debugLine="Dim i As Int";
_i = 0;
 //BA.debugLineNum = 455;BA.debugLine="For i=0 To lista.Size-1";
{
final int step4 = 1;
final int limit4 = (int) (_lista.getSize()-1);
_i = (int) (0) ;
for (;_i <= limit4 ;_i = _i + step4 ) {
 //BA.debugLineNum = 456;BA.debugLine="ListView1.AddSingleLine(lista.Get(i))";
mostCurrent._listview1.AddSingleLine(BA.ObjectToCharSequence(_lista.Get(_i)));
 }
};
 //BA.debugLineNum = 458;BA.debugLine="ListView1.SingleLineLayout.Label.Color=Colors.Gra";
mostCurrent._listview1.getSingleLineLayout().Label.setColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 //BA.debugLineNum = 459;BA.debugLine="End Sub";
return "";
}
public static String  _dbxfiles_copybatch(boolean _success,anywheresoftware.b4a.objects.collections.Map _meta,String _error) throws Exception{
 //BA.debugLineNum = 551;BA.debugLine="Sub dbxFiles_CopyBatch(success As Boolean, meta As";
 //BA.debugLineNum = 553;BA.debugLine="End Sub";
return "";
}
public static String  _dbxfiles_copybatchcheck(boolean _success,anywheresoftware.b4a.objects.collections.Map _meta,String _error) throws Exception{
 //BA.debugLineNum = 554;BA.debugLine="Sub dbxFiles_CopyBatchCheck(success As Boolean, me";
 //BA.debugLineNum = 556;BA.debugLine="End Sub";
return "";
}
public static String  _dbxfiles_copyreference(anywheresoftware.b4a.objects.collections.Map _metainfo) throws Exception{
 //BA.debugLineNum = 557;BA.debugLine="Sub dbxFiles_CopyReference(metainfo As Map)";
 //BA.debugLineNum = 559;BA.debugLine="End Sub";
return "";
}
public static String  _dbxfiles_createfolder(boolean _success,de.donmanfred.dbxv2.files.FolderMetadataWrapper _meta,String _error) throws Exception{
 //BA.debugLineNum = 560;BA.debugLine="Sub dbxFiles_CreateFolder(success As Boolean, meta";
 //BA.debugLineNum = 562;BA.debugLine="End Sub";
return "";
}
public static String  _dbxfiles_delete(boolean _success,Object _metaobj,String _sessionid) throws Exception{
 //BA.debugLineNum = 496;BA.debugLine="Sub dbxFiles_Delete(success As Boolean, metaObj As";
 //BA.debugLineNum = 497;BA.debugLine="Log($\"dbxFiles_UploadFinished(${success}, ${metaO";
anywheresoftware.b4a.keywords.Common.LogImpl("81179649",("dbxFiles_UploadFinished("+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_success))+", "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",_metaobj)+", "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_sessionid))+")"),0);
 //BA.debugLineNum = 499;BA.debugLine="End Sub";
return "";
}
public static String  _dbxfiles_downloadfinished(boolean _success,de.donmanfred.dbxv2.files.FileMetadataWrapper _meta,String _sessionid) throws Exception{
 //BA.debugLineNum = 486;BA.debugLine="Sub dbxFiles_DownloadFinished(success As Boolean,";
 //BA.debugLineNum = 487;BA.debugLine="Log($\"dbxFiles_DownloadFinished(${success}, ${met";
anywheresoftware.b4a.keywords.Common.LogImpl("81048577",("dbxFiles_DownloadFinished("+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_success))+", "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_meta.getObject()))+", "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_sessionid))+")"),0);
 //BA.debugLineNum = 489;BA.debugLine="End Sub";
return "";
}
public static String  _dbxfiles_listfolder(boolean _success,anywheresoftware.b4a.objects.collections.List _content,String _error) throws Exception{
 //BA.debugLineNum = 501;BA.debugLine="Sub dbxFiles_listFolder(success As Boolean, conten";
 //BA.debugLineNum = 502;BA.debugLine="Log($\"dbxFiles_listFolders(${success}, ${content.";
anywheresoftware.b4a.keywords.Common.LogImpl("81245185",("dbxFiles_listFolders("+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_success))+", "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_content.getSize()))+", "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_error))+")"),0);
 //BA.debugLineNum = 505;BA.debugLine="End Sub";
return "";
}
public static String  _dbxfiles_listrevisions(boolean _success,anywheresoftware.b4a.objects.collections.Map _meta,String _error) throws Exception{
 //BA.debugLineNum = 563;BA.debugLine="Sub dbxFiles_ListRevisions(success As Boolean, met";
 //BA.debugLineNum = 565;BA.debugLine="End Sub";
return "";
}
public static String  _dbxfiles_movebatch(boolean _success,anywheresoftware.b4a.objects.collections.Map _meta,String _error) throws Exception{
 //BA.debugLineNum = 566;BA.debugLine="Sub dbxFiles_MoveBatch(success As Boolean, meta As";
 //BA.debugLineNum = 568;BA.debugLine="End Sub";
return "";
}
public static String  _dbxfiles_movebatchcheck(boolean _success,anywheresoftware.b4a.objects.collections.Map _meta,String _error) throws Exception{
 //BA.debugLineNum = 569;BA.debugLine="Sub dbxFiles_MoveBatchCheck(success As Boolean, me";
 //BA.debugLineNum = 571;BA.debugLine="End Sub";
return "";
}
public static String  _dbxfiles_restore(boolean _success,de.donmanfred.dbxv2.files.FileMetadataWrapper _meta,String _error) throws Exception{
 //BA.debugLineNum = 572;BA.debugLine="Sub dbxFiles_Restore(success As Boolean, meta As F";
 //BA.debugLineNum = 574;BA.debugLine="End Sub";
return "";
}
public static String  _dbxfiles_uploadfinished(boolean _success,Object _metaobj,String _sessionid) throws Exception{
 //BA.debugLineNum = 491;BA.debugLine="Sub dbxFiles_UploadFinished(success As Boolean, me";
 //BA.debugLineNum = 492;BA.debugLine="Log($\"dbxFiles_UploadFinished(${success}, ${metaO";
anywheresoftware.b4a.keywords.Common.LogImpl("81114113",("dbxFiles_UploadFinished("+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_success))+", "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",_metaobj)+", "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_sessionid))+")"),0);
 //BA.debugLineNum = 494;BA.debugLine="End Sub";
return "";
}
public static String  _dbxfiles_uploadprogress(String _sessionid,String _path,String _filename,long _uploaded,long _size) throws Exception{
 //BA.debugLineNum = 509;BA.debugLine="Sub dbxFiles_UploadProgress(sessionId As String, p";
 //BA.debugLineNum = 510;BA.debugLine="Log($\"${filename} -> ${NumberFormat(uploaded / 10";
anywheresoftware.b4a.keywords.Common.LogImpl("81310721",(""+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_filename))+" -> "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(anywheresoftware.b4a.keywords.Common.NumberFormat(_uploaded/(double)1024,(int) (0),(int) (0))))+" KB /"+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(anywheresoftware.b4a.keywords.Common.NumberFormat(_size/(double)1024,(int) (0),(int) (0))))+" kb -> "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(anywheresoftware.b4a.keywords.Common.NumberFormat(100*(_uploaded/(double)_size),(int) (0),(int) (2))))+" - Session "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_sessionid))+""),0);
 //BA.debugLineNum = 511;BA.debugLine="ToastMessageShow($\"${filename} -> ${NumberFormat(";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence((""+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_filename))+" -> "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(anywheresoftware.b4a.keywords.Common.NumberFormat(_uploaded/(double)1024,(int) (0),(int) (0))))+" KB /"+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(anywheresoftware.b4a.keywords.Common.NumberFormat(_size/(double)1024,(int) (0),(int) (0))))+" kb -> "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(anywheresoftware.b4a.keywords.Common.NumberFormat(100*(_uploaded/(double)_size),(int) (0),(int) (2))))+" - Session "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_sessionid))+"")),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 512;BA.debugLine="End Sub";
return "";
}
public static String  _dbxsharing_addfilemember(boolean _success,anywheresoftware.b4a.objects.collections.List _members,String _error) throws Exception{
 //BA.debugLineNum = 528;BA.debugLine="Sub dbxSharing_AddFileMember(success As Boolean, m";
 //BA.debugLineNum = 529;BA.debugLine="Log($\"dbxSharing_listFolders(${success}, ${member";
anywheresoftware.b4a.keywords.Common.LogImpl("81441793",("dbxSharing_listFolders("+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_success))+", "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_members.getSize()))+", "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_error))+")"),0);
 //BA.debugLineNum = 530;BA.debugLine="End Sub";
return "";
}
public static String  _dbxsharing_listfolders(boolean _success,anywheresoftware.b4a.objects.collections.List _folders,String _error) throws Exception{
int _i = 0;
de.donmanfred.dbxv2.sharing.SharedFolderMetadataWrapper _meta = null;
 //BA.debugLineNum = 515;BA.debugLine="Sub dbxSharing_listFolders(success As Boolean, fol";
 //BA.debugLineNum = 516;BA.debugLine="Log($\"dbxSharing_listFolders(${success}, ${folder";
anywheresoftware.b4a.keywords.Common.LogImpl("81376257",("dbxSharing_listFolders("+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_success))+", "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_folders.getSize()))+", "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_error))+")"),0);
 //BA.debugLineNum = 517;BA.debugLine="Borrado=False";
_borrado = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 518;BA.debugLine="If folders.Size > 0 Then";
if (_folders.getSize()>0) { 
 //BA.debugLineNum = 519;BA.debugLine="For i = 0 To folders.Size-1";
{
final int step4 = 1;
final int limit4 = (int) (_folders.getSize()-1);
_i = (int) (0) ;
for (;_i <= limit4 ;_i = _i + step4 ) {
 //BA.debugLineNum = 520;BA.debugLine="Dim meta As SharedFolderMetadata = folders.Get(";
_meta = new de.donmanfred.dbxv2.sharing.SharedFolderMetadataWrapper();
_meta = (de.donmanfred.dbxv2.sharing.SharedFolderMetadataWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new de.donmanfred.dbxv2.sharing.SharedFolderMetadataWrapper(), (com.dropbox.core.v2.sharing.SharedFolderMetadata)(_folders.Get(_i)));
 //BA.debugLineNum = 521;BA.debugLine="Log(meta.PathLower&\" -> \"& meta.Name)";
anywheresoftware.b4a.keywords.Common.LogImpl("81376262",_meta.getPathLower()+" -> "+_meta.getName(),0);
 }
};
 };
 //BA.debugLineNum = 526;BA.debugLine="End Sub";
return "";
}
public static String  _dbxsharing_listmountablefolders(boolean _success,anywheresoftware.b4a.objects.collections.List _folders,String _error) throws Exception{
 //BA.debugLineNum = 532;BA.debugLine="Sub dbxSharing_listMountableFolders(success As Boo";
 //BA.debugLineNum = 533;BA.debugLine="Log($\"dbxSharing_listFolders(${success}, ${folder";
anywheresoftware.b4a.keywords.Common.LogImpl("81507329",("dbxSharing_listFolders("+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_success))+", "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_folders.getSize()))+", "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_error))+")"),0);
 //BA.debugLineNum = 534;BA.debugLine="End Sub";
return "";
}
public static String  _dbxsharing_listreceivedfiles(boolean _success,anywheresoftware.b4a.objects.collections.List _receivedfiles,String _error) throws Exception{
 //BA.debugLineNum = 536;BA.debugLine="Sub dbxSharing_listReceivedFiles(success As Boolea";
 //BA.debugLineNum = 537;BA.debugLine="Log($\"dbxSharing_listFolders(${success}, ${receiv";
anywheresoftware.b4a.keywords.Common.LogImpl("81572865",("dbxSharing_listFolders("+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_success))+", "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_receivedfiles.getSize()))+", "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_error))+")"),0);
 //BA.debugLineNum = 539;BA.debugLine="End Sub";
return "";
}
public static String  _dbxsharing_listsharedlinks(boolean _success,anywheresoftware.b4a.objects.collections.List _sharedlinks,String _error) throws Exception{
 //BA.debugLineNum = 541;BA.debugLine="Sub dbxSharing_listSharedLinks(success As Boolean,";
 //BA.debugLineNum = 542;BA.debugLine="Log($\"dbxSharing_listFolders(${success}, ${shared";
anywheresoftware.b4a.keywords.Common.LogImpl("81638401",("dbxSharing_listFolders("+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_success))+", "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_sharedlinks.getSize()))+", "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_error))+")"),0);
 //BA.debugLineNum = 543;BA.debugLine="End Sub";
return "";
}
public static String  _dbxsharing_sharedfoldermetadata(boolean _success,de.donmanfred.dbxv2.sharing.SharedFolderMetadataWrapper _meta,String _error) throws Exception{
 //BA.debugLineNum = 545;BA.debugLine="Sub dbxSharing_SharedFolderMetadata(success As Boo";
 //BA.debugLineNum = 546;BA.debugLine="Log($\"dbxSharing_listFolders(${success}, ${meta.t";
anywheresoftware.b4a.keywords.Common.LogImpl("81703937",("dbxSharing_listFolders("+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_success))+", "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_meta.toString()))+", "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_error))+")"),0);
 //BA.debugLineNum = 548;BA.debugLine="End Sub";
return "";
}
public static String  _dbxusers_getaccount(de.donmanfred.dbxv2.users.BasicAccountWrapper _account) throws Exception{
 //BA.debugLineNum = 576;BA.debugLine="Sub dbxUsers_getAccount(account As BasicAccount)";
 //BA.debugLineNum = 577;BA.debugLine="Log($\"dbxUsers_getAccount(${account})\"$)";
anywheresoftware.b4a.keywords.Common.LogImpl("82293761",("dbxUsers_getAccount("+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_account.getObject()))+")"),0);
 //BA.debugLineNum = 578;BA.debugLine="End Sub";
return "";
}
public static String  _dbxusers_getaccountbatch(anywheresoftware.b4a.objects.collections.List _batch) throws Exception{
 //BA.debugLineNum = 579;BA.debugLine="Sub dbxUsers_getAccountBatch(batch As List)";
 //BA.debugLineNum = 581;BA.debugLine="End Sub";
return "";
}
public static String  _dbxusers_getcurrentaccount(de.donmanfred.dbxv2.users.FullAccountWrapper _account) throws Exception{
 //BA.debugLineNum = 582;BA.debugLine="Sub dbxUsers_getCurrentAccount(account As FullAcco";
 //BA.debugLineNum = 583;BA.debugLine="Log($\"dbxUsers_getCurrentAccount(${account})\"$)";
anywheresoftware.b4a.keywords.Common.LogImpl("82424833",("dbxUsers_getCurrentAccount("+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_account.getObject()))+")"),0);
 //BA.debugLineNum = 585;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.collections.List  _getelements(anywheresoftware.b4a.objects.collections.Map _m,String _keyl) throws Exception{
anywheresoftware.b4a.objects.collections.List _res = null;
Object _value = null;
 //BA.debugLineNum = 469;BA.debugLine="Sub GetElements (m As Map, keyL As String) As List";
 //BA.debugLineNum = 470;BA.debugLine="Dim res As List";
_res = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 471;BA.debugLine="If m.ContainsKey(keyL) = False Then";
if (_m.ContainsKey((Object)(_keyl))==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 472;BA.debugLine="res.Initialize";
_res.Initialize();
 //BA.debugLineNum = 473;BA.debugLine="Return res";
if (true) return _res;
 }else {
 //BA.debugLineNum = 475;BA.debugLine="Dim value As Object = m.Get(keyL)";
_value = _m.Get((Object)(_keyl));
 //BA.debugLineNum = 476;BA.debugLine="If value Is List Then Return value";
if (_value instanceof java.util.List) { 
if (true) return (anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(_value));};
 //BA.debugLineNum = 477;BA.debugLine="res.Initialize";
_res.Initialize();
 //BA.debugLineNum = 478;BA.debugLine="res.Add(value)";
_res.Add(_value);
 //BA.debugLineNum = 479;BA.debugLine="Return res";
if (true) return _res;
 };
 //BA.debugLineNum = 481;BA.debugLine="End Sub";
return null;
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 59;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 61;BA.debugLine="Private Button1 As Button";
mostCurrent._button1 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 62;BA.debugLine="Private EditText1 As EditText";
mostCurrent._edittext1 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 63;BA.debugLine="Private ListView1 As ListView";
mostCurrent._listview1 = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 66;BA.debugLine="Private fichero_datos As String=\"test.txt\"";
mostCurrent._fichero_datos = "test.txt";
 //BA.debugLineNum = 68;BA.debugLine="Dim Borrado As Boolean";
_borrado = false;
 //BA.debugLineNum = 69;BA.debugLine="End Sub";
return "";
}
public static String  _guardar_en_disco() throws Exception{
int _i = 0;
anywheresoftware.b4a.objects.collections.List _lista = null;
 //BA.debugLineNum = 338;BA.debugLine="Sub Guardar_en_disco";
 //BA.debugLineNum = 339;BA.debugLine="Log(\"Guardar en disco\" )";
anywheresoftware.b4a.keywords.Common.LogImpl("8720897","Guardar en disco",0);
 //BA.debugLineNum = 340;BA.debugLine="Dim i As Int";
_i = 0;
 //BA.debugLineNum = 341;BA.debugLine="Dim lista As List";
_lista = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 342;BA.debugLine="lista.Initialize";
_lista.Initialize();
 //BA.debugLineNum = 343;BA.debugLine="For i=0 To ListView1.Size-1";
{
final int step5 = 1;
final int limit5 = (int) (mostCurrent._listview1.getSize()-1);
_i = (int) (0) ;
for (;_i <= limit5 ;_i = _i + step5 ) {
 //BA.debugLineNum = 344;BA.debugLine="lista.Add(ListView1.GetItem(i))";
_lista.Add(mostCurrent._listview1.GetItem(_i));
 }
};
 //BA.debugLineNum = 346;BA.debugLine="File.WriteList(File.DirInternal, fichero_datos,";
anywheresoftware.b4a.keywords.Common.File.WriteList(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),mostCurrent._fichero_datos,_lista);
 //BA.debugLineNum = 347;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.keywords.Common.ResumableSubWrapper  _guardar_en_internet_dropbox() throws Exception{
ResumableSub_Guardar_en_internet_Dropbox rsub = new ResumableSub_Guardar_en_internet_Dropbox(null);
rsub.resume(processBA, null);
return (anywheresoftware.b4a.keywords.Common.ResumableSubWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper(), rsub);
}
public static class ResumableSub_Guardar_en_internet_Dropbox extends BA.ResumableSub {
public ResumableSub_Guardar_en_internet_Dropbox(iNKuBo.Compras.main parent) {
this.parent = parent;
}
iNKuBo.Compras.main parent;
boolean _encontrado = false;
boolean _success = false;
anywheresoftware.b4a.objects.collections.List _content = null;
String _error = "";
int _i = 0;
de.donmanfred.dbxv2.files.MetadataWrapper _meta = null;
Object _metaobj = null;
String _sessionid = "";
int step8;
int limit8;

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
 //BA.debugLineNum = 180;BA.debugLine="Log (\"Guardar en iternet Dropbox\")";
anywheresoftware.b4a.keywords.Common.LogImpl("8524289","Guardar en iternet Dropbox",0);
 //BA.debugLineNum = 182;BA.debugLine="Guardar_en_disco";
_guardar_en_disco();
 //BA.debugLineNum = 183;BA.debugLine="dbxFiles.listFolder(\"\",False,True,False,False,Tru";
parent._dbxfiles.listFolder(processBA,"",anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 184;BA.debugLine="Dim encontrado As Boolean";
_encontrado = false;
 //BA.debugLineNum = 185;BA.debugLine="encontrado=False";
_encontrado = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 186;BA.debugLine="wait for dbxFiles_listFolder(success As Boolean,";
anywheresoftware.b4a.keywords.Common.WaitFor("dbxfiles_listfolder", processBA, this, null);
this.state = 42;
return;
case 42:
//C
this.state = 1;
_success = (Boolean) result[0];
_content = (anywheresoftware.b4a.objects.collections.List) result[1];
_error = (String) result[2];
;
 //BA.debugLineNum = 187;BA.debugLine="If content.Size > 0 Then";
if (true) break;

case 1:
//if
this.state = 41;
if (_content.getSize()>0) { 
this.state = 3;
}else {
this.state = 34;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 188;BA.debugLine="For i = 0 To content.Size-1";
if (true) break;

case 4:
//for
this.state = 23;
step8 = 1;
limit8 = (int) (_content.getSize()-1);
_i = (int) (0) ;
this.state = 43;
if (true) break;

case 43:
//C
this.state = 23;
if ((step8 > 0 && _i <= limit8) || (step8 < 0 && _i >= limit8)) this.state = 6;
if (true) break;

case 44:
//C
this.state = 43;
_i = ((int)(0 + _i + step8)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 189;BA.debugLine="Dim meta As Metadata = content.Get(i)";
_meta = new de.donmanfred.dbxv2.files.MetadataWrapper();
_meta = (de.donmanfred.dbxv2.files.MetadataWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new de.donmanfred.dbxv2.files.MetadataWrapper(), (com.dropbox.core.v2.files.Metadata)(_content.Get(_i)));
 //BA.debugLineNum = 190;BA.debugLine="Log(meta.toString)";
anywheresoftware.b4a.keywords.Common.LogImpl("8524299",_meta.toString(),0);
 //BA.debugLineNum = 191;BA.debugLine="If meta.Name=fichero_datos Then";
if (true) break;

case 7:
//if
this.state = 22;
if ((_meta.getName()).equals(parent.mostCurrent._fichero_datos)) { 
this.state = 9;
}if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 192;BA.debugLine="encontrado = True";
_encontrado = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 193;BA.debugLine="dbxFiles.delete(\"/\"&fichero_datos)";
parent._dbxfiles.delete("/"+parent.mostCurrent._fichero_datos);
 //BA.debugLineNum = 195;BA.debugLine="wait for dbxFiles_Delete(success As Boolean, m";
anywheresoftware.b4a.keywords.Common.WaitFor("dbxfiles_delete", processBA, this, null);
this.state = 45;
return;
case 45:
//C
this.state = 10;
_success = (Boolean) result[0];
_metaobj = (Object) result[1];
_sessionid = (String) result[2];
;
 //BA.debugLineNum = 196;BA.debugLine="Log($\"dbxFiles_UploadFinished(${success}, ${me";
anywheresoftware.b4a.keywords.Common.LogImpl("8524305",("dbxFiles_UploadFinished("+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_success))+", "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",_metaobj)+", "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_sessionid))+")"),0);
 //BA.debugLineNum = 197;BA.debugLine="If success Then";
if (true) break;

case 10:
//if
this.state = 15;
if (_success) { 
this.state = 12;
}else {
this.state = 14;
}if (true) break;

case 12:
//C
this.state = 15;
 //BA.debugLineNum = 199;BA.debugLine="Borrado=True";
parent._borrado = anywheresoftware.b4a.keywords.Common.True;
 if (true) break;

case 14:
//C
this.state = 15;
 if (true) break;

case 15:
//C
this.state = 16;
;
 //BA.debugLineNum = 204;BA.debugLine="dbxFiles.upload(File.DirInternal,fichero_datos";
parent._dbxfiles.upload(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),parent.mostCurrent._fichero_datos,"/"+parent.mostCurrent._fichero_datos,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 205;BA.debugLine="ToastMessageShow(\"Subiendo Lista a Internet\",F";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Subiendo Lista a Internet"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 206;BA.debugLine="wait for dbxFiles_UploadFinished(success As Bo";
anywheresoftware.b4a.keywords.Common.WaitFor("dbxfiles_uploadfinished", processBA, this, null);
this.state = 46;
return;
case 46:
//C
this.state = 16;
_success = (Boolean) result[0];
_metaobj = (Object) result[1];
_sessionid = (String) result[2];
;
 //BA.debugLineNum = 207;BA.debugLine="Log($\"dbxFiles_UploadFinished(${success}, ${me";
anywheresoftware.b4a.keywords.Common.LogImpl("8524316",("dbxFiles_UploadFinished("+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_success))+", "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",_metaobj)+", "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_sessionid))+")"),0);
 //BA.debugLineNum = 208;BA.debugLine="If success Then";
if (true) break;

case 16:
//if
this.state = 21;
if (_success) { 
this.state = 18;
}else {
this.state = 20;
}if (true) break;

case 18:
//C
this.state = 21;
 //BA.debugLineNum = 209;BA.debugLine="ToastMessageShow(\"Telefono -> Internet [Lista";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Telefono -> Internet [Lista:OK]"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 20:
//C
this.state = 21;
 //BA.debugLineNum = 212;BA.debugLine="ToastMessageShow(\"Telefono -> Internet [Lista";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Telefono -> Internet [Lista:KO]"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 213;BA.debugLine="ListView1.SingleLineLayout.Label.Color=Colors";
parent.mostCurrent._listview1.getSingleLineLayout().Label.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 if (true) break;

case 21:
//C
this.state = 22;
;
 if (true) break;

case 22:
//C
this.state = 44;
;
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 218;BA.debugLine="If encontrado=False Then";

case 23:
//if
this.state = 32;
if (_encontrado==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 25;
}if (true) break;

case 25:
//C
this.state = 26;
 //BA.debugLineNum = 219;BA.debugLine="dbxFiles.upload(File.DirInternal,fichero_datos,";
parent._dbxfiles.upload(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),parent.mostCurrent._fichero_datos,"/"+parent.mostCurrent._fichero_datos,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 220;BA.debugLine="ToastMessageShow(\"Subiendo Lista a Internet\",Fa";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Subiendo Lista a Internet"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 221;BA.debugLine="wait for dbxFiles_UploadFinished(success As Boo";
anywheresoftware.b4a.keywords.Common.WaitFor("dbxfiles_uploadfinished", processBA, this, null);
this.state = 47;
return;
case 47:
//C
this.state = 26;
_success = (Boolean) result[0];
_metaobj = (Object) result[1];
_sessionid = (String) result[2];
;
 //BA.debugLineNum = 222;BA.debugLine="Log($\"dbxFiles_UploadFinished(${success}, ${met";
anywheresoftware.b4a.keywords.Common.LogImpl("8524331",("dbxFiles_UploadFinished("+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_success))+", "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",_metaobj)+", "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_sessionid))+")"),0);
 //BA.debugLineNum = 223;BA.debugLine="If success Then";
if (true) break;

case 26:
//if
this.state = 31;
if (_success) { 
this.state = 28;
}else {
this.state = 30;
}if (true) break;

case 28:
//C
this.state = 31;
 //BA.debugLineNum = 224;BA.debugLine="ToastMessageShow(\"Telefono -> Internet [Lista:";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Telefono -> Internet [Lista:OK]"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 225;BA.debugLine="ListView1.SingleLineLayout.Label.Color=Colors.";
parent.mostCurrent._listview1.getSingleLineLayout().Label.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 if (true) break;

case 30:
//C
this.state = 31;
 //BA.debugLineNum = 227;BA.debugLine="ToastMessageShow(\"Telefono -> Internet [Lista:";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Telefono -> Internet [Lista:KO]"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 228;BA.debugLine="ListView1.SingleLineLayout.Label.Color=Colors.";
parent.mostCurrent._listview1.getSingleLineLayout().Label.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 if (true) break;

case 31:
//C
this.state = 32;
;
 if (true) break;

case 32:
//C
this.state = 41;
;
 if (true) break;

case 34:
//C
this.state = 35;
 //BA.debugLineNum = 232;BA.debugLine="Borrado=True";
parent._borrado = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 233;BA.debugLine="dbxFiles.upload(File.DirInternal,fichero_datos,\"";
parent._dbxfiles.upload(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),parent.mostCurrent._fichero_datos,"/"+parent.mostCurrent._fichero_datos,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 235;BA.debugLine="wait for dbxFiles_UploadFinished(success As Bool";
anywheresoftware.b4a.keywords.Common.WaitFor("dbxfiles_uploadfinished", processBA, this, null);
this.state = 48;
return;
case 48:
//C
this.state = 35;
_success = (Boolean) result[0];
_metaobj = (Object) result[1];
_sessionid = (String) result[2];
;
 //BA.debugLineNum = 236;BA.debugLine="Log($\"dbxFiles_UploadFinished(${success}, ${meta";
anywheresoftware.b4a.keywords.Common.LogImpl("8524345",("dbxFiles_UploadFinished("+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_success))+", "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",_metaobj)+", "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_sessionid))+")"),0);
 //BA.debugLineNum = 237;BA.debugLine="If success Then";
if (true) break;

case 35:
//if
this.state = 40;
if (_success) { 
this.state = 37;
}else {
this.state = 39;
}if (true) break;

case 37:
//C
this.state = 40;
 //BA.debugLineNum = 238;BA.debugLine="ToastMessageShow(\"Telefono -> Internet [Lista:O";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Telefono -> Internet [Lista:OK]"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 39:
//C
this.state = 40;
 //BA.debugLineNum = 241;BA.debugLine="ToastMessageShow(\"Telefono -> Internet [Lista:K";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Telefono -> Internet [Lista:KO]"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 242;BA.debugLine="ListView1.SingleLineLayout.Label.Color=Colors.R";
parent.mostCurrent._listview1.getSingleLineLayout().Label.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 if (true) break;

case 40:
//C
this.state = 41;
;
 if (true) break;

case 41:
//C
this.state = -1;
;
 //BA.debugLineNum = 247;BA.debugLine="Return success";
if (true) {
anywheresoftware.b4a.keywords.Common.ReturnFromResumableSub(this,(Object)(_success));return;};
 //BA.debugLineNum = 248;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _guardar_en_internet_paste() throws Exception{
ResumableSub_Guardar_en_internet_paste rsub = new ResumableSub_Guardar_en_internet_paste(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_Guardar_en_internet_paste extends BA.ResumableSub {
public ResumableSub_Guardar_en_internet_paste(iNKuBo.Compras.main parent) {
this.parent = parent;
}
iNKuBo.Compras.main parent;
iNKuBo.Compras.httpjob _j = null;
int _i = 0;
anywheresoftware.b4a.objects.collections.List _lista = null;
anywheresoftware.b4a.objects.collections.List _nada = null;
int step33;
int limit33;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 261;BA.debugLine="Log(\"Login para guardar\")";
anywheresoftware.b4a.keywords.Common.LogImpl("8655362","Login para guardar",0);
 //BA.debugLineNum = 262;BA.debugLine="Dim j As HttpJob";
_j = new iNKuBo.Compras.httpjob();
 //BA.debugLineNum = 263;BA.debugLine="j.Initialize(\"\", Me)";
_j._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 265;BA.debugLine="url=\"https://pastebin.com/api/api_login.php\"";
parent._url = "https://pastebin.com/api/api_login.php";
 //BA.debugLineNum = 268;BA.debugLine="data=\"api_dev_key=\"&api_dev_key&\"&api_user_name=\"";
parent._data = "api_dev_key="+parent._api_dev_key+"&api_user_name="+parent._user+"&api_user_password="+parent._pass;
 //BA.debugLineNum = 269;BA.debugLine="j.PostString(url, data)";
_j._poststring /*String*/ (parent._url,parent._data);
 //BA.debugLineNum = 271;BA.debugLine="Wait For (j) JobDone(j As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_j));
this.state = 35;
return;
case 35:
//C
this.state = 1;
_j = (iNKuBo.Compras.httpjob) result[0];
;
 //BA.debugLineNum = 272;BA.debugLine="If j.Success Then";
if (true) break;

case 1:
//if
this.state = 6;
if (_j._success /*boolean*/ ) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 273;BA.debugLine="Log(\"User_KEY:\" & j.GetString)";
anywheresoftware.b4a.keywords.Common.LogImpl("8655374","User_KEY:"+_j._getstring /*String*/ (),0);
 //BA.debugLineNum = 274;BA.debugLine="api_user_key=j.GetString";
parent._api_user_key = _j._getstring /*String*/ ();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 276;BA.debugLine="api_user_key=\"\"";
parent._api_user_key = "";
 //BA.debugLineNum = 277;BA.debugLine="Log(\"No conectado a internet\")";
anywheresoftware.b4a.keywords.Common.LogImpl("8655378","No conectado a internet",0);
 if (true) break;

case 6:
//C
this.state = 7;
;
 //BA.debugLineNum = 279;BA.debugLine="j.Release";
_j._release /*String*/ ();
 //BA.debugLineNum = 281;BA.debugLine="If api_user_key<>\"\" Then";
if (true) break;

case 7:
//if
this.state = 34;
if ((parent._api_user_key).equals("") == false) { 
this.state = 9;
}else {
this.state = 33;
}if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 283;BA.debugLine="url=\"https://pastebin.com/api/api_post.php\"";
parent._url = "https://pastebin.com/api/api_post.php";
 //BA.debugLineNum = 284;BA.debugLine="data=\"api_dev_key=\"&api_dev_key &\"&api_user_key=";
parent._data = "api_dev_key="+parent._api_dev_key+"&api_user_key="+parent._api_user_key+"&api_paste_key="+parent._fichero+"&api_option=delete";
 //BA.debugLineNum = 285;BA.debugLine="Log(\"data \"&data)";
anywheresoftware.b4a.keywords.Common.LogImpl("8655386","data "+parent._data,0);
 //BA.debugLineNum = 286;BA.debugLine="j.PostString(url, data)";
_j._poststring /*String*/ (parent._url,parent._data);
 //BA.debugLineNum = 288;BA.debugLine="Wait For (j) JobDone(j As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_j));
this.state = 36;
return;
case 36:
//C
this.state = 10;
_j = (iNKuBo.Compras.httpjob) result[0];
;
 //BA.debugLineNum = 289;BA.debugLine="If j.Success Then";
if (true) break;

case 10:
//if
this.state = 15;
if (_j._success /*boolean*/ ) { 
this.state = 12;
}else {
this.state = 14;
}if (true) break;

case 12:
//C
this.state = 15;
 //BA.debugLineNum = 290;BA.debugLine="Log(\"Borrado anterior fichero\")";
anywheresoftware.b4a.keywords.Common.LogImpl("8655391","Borrado anterior fichero",0);
 if (true) break;

case 14:
//C
this.state = 15;
 //BA.debugLineNum = 292;BA.debugLine="Log(\"Error borrando anterior fichero en interne";
anywheresoftware.b4a.keywords.Common.LogImpl("8655393","Error borrando anterior fichero en internet "+parent._fichero,0);
 if (true) break;

case 15:
//C
this.state = 16;
;
 //BA.debugLineNum = 294;BA.debugLine="j.Release";
_j._release /*String*/ ();
 //BA.debugLineNum = 297;BA.debugLine="Log(\"Guardar en internet \" & api_user_key)";
anywheresoftware.b4a.keywords.Common.LogImpl("8655398","Guardar en internet "+parent._api_user_key,0);
 //BA.debugLineNum = 298;BA.debugLine="Dim i As Int";
_i = 0;
 //BA.debugLineNum = 299;BA.debugLine="Dim lista As List";
_lista = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 300;BA.debugLine="lista.Initialize";
_lista.Initialize();
 //BA.debugLineNum = 301;BA.debugLine="api_paste_code=\"\"";
parent._api_paste_code = "";
 //BA.debugLineNum = 302;BA.debugLine="For i=0 To ListView1.Size-1";
if (true) break;

case 16:
//for
this.state = 25;
step33 = 1;
limit33 = (int) (parent.mostCurrent._listview1.getSize()-1);
_i = (int) (0) ;
this.state = 37;
if (true) break;

case 37:
//C
this.state = 25;
if ((step33 > 0 && _i <= limit33) || (step33 < 0 && _i >= limit33)) this.state = 18;
if (true) break;

case 38:
//C
this.state = 37;
_i = ((int)(0 + _i + step33)) ;
if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 303;BA.debugLine="If i=0 Then";
if (true) break;

case 19:
//if
this.state = 24;
if (_i==0) { 
this.state = 21;
}else {
this.state = 23;
}if (true) break;

case 21:
//C
this.state = 24;
 //BA.debugLineNum = 304;BA.debugLine="api_paste_code=ListView1.getItem(i)";
parent._api_paste_code = BA.ObjectToString(parent.mostCurrent._listview1.GetItem(_i));
 if (true) break;

case 23:
//C
this.state = 24;
 //BA.debugLineNum = 306;BA.debugLine="api_paste_code=api_paste_code & \",\" & ListView";
parent._api_paste_code = parent._api_paste_code+","+BA.ObjectToString(parent.mostCurrent._listview1.GetItem(_i));
 if (true) break;

case 24:
//C
this.state = 38;
;
 if (true) break;
if (true) break;

case 25:
//C
this.state = 26;
;
 //BA.debugLineNum = 310;BA.debugLine="Dim j As HttpJob";
_j = new iNKuBo.Compras.httpjob();
 //BA.debugLineNum = 311;BA.debugLine="j.Initialize(\"\", Me)";
_j._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 313;BA.debugLine="url=\"https://pastebin.com/api/api_post.php\"";
parent._url = "https://pastebin.com/api/api_post.php";
 //BA.debugLineNum = 314;BA.debugLine="data=\"api_user_key=\"&api_user_key &\"&api_dev_key";
parent._data = "api_user_key="+parent._api_user_key+"&api_dev_key="+parent._api_dev_key+"&api_option=paste&api_paste_code="+parent._api_paste_code+"&api_paste_name=lista.txt";
 //BA.debugLineNum = 316;BA.debugLine="j.PostString(url, data)";
_j._poststring /*String*/ (parent._url,parent._data);
 //BA.debugLineNum = 318;BA.debugLine="Wait For (j) JobDone(j As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_j));
this.state = 39;
return;
case 39:
//C
this.state = 26;
_j = (iNKuBo.Compras.httpjob) result[0];
;
 //BA.debugLineNum = 319;BA.debugLine="If j.Success Then";
if (true) break;

case 26:
//if
this.state = 31;
if (_j._success /*boolean*/ ) { 
this.state = 28;
}else {
this.state = 30;
}if (true) break;

case 28:
//C
this.state = 31;
 //BA.debugLineNum = 320;BA.debugLine="Log(\"Subido:\" & j.GetString)";
anywheresoftware.b4a.keywords.Common.LogImpl("8655421","Subido:"+_j._getstring /*String*/ (),0);
 //BA.debugLineNum = 321;BA.debugLine="fichero=Regex.Split(\"/\",j.GetString)(3)";
parent._fichero = anywheresoftware.b4a.keywords.Common.Regex.Split("/",_j._getstring /*String*/ ())[(int) (3)];
 //BA.debugLineNum = 322;BA.debugLine="Log(\"Guardado en internet \" & api_user_key & \"n";
anywheresoftware.b4a.keywords.Common.LogImpl("8655423","Guardado en internet "+parent._api_user_key+"nuevo fichero: "+parent._fichero,0);
 //BA.debugLineNum = 325;BA.debugLine="Dim nada As List";
_nada = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 326;BA.debugLine="nada.Initialize";
_nada.Initialize();
 //BA.debugLineNum = 327;BA.debugLine="File.WriteList(File.DirInternal, fichero_datos,";
anywheresoftware.b4a.keywords.Common.File.WriteList(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),parent.mostCurrent._fichero_datos,_nada);
 if (true) break;

case 30:
//C
this.state = 31;
 //BA.debugLineNum = 329;BA.debugLine="Log(\"Error escribiendo\")";
anywheresoftware.b4a.keywords.Common.LogImpl("8655430","Error escribiendo",0);
 if (true) break;

case 31:
//C
this.state = 34;
;
 //BA.debugLineNum = 331;BA.debugLine="j.Release";
_j._release /*String*/ ();
 if (true) break;

case 33:
//C
this.state = 34;
 //BA.debugLineNum = 333;BA.debugLine="Log(\"Como no puede guardar en internet, guardo e";
anywheresoftware.b4a.keywords.Common.LogImpl("8655434","Como no puede guardar en internet, guardo en disco ",0);
 //BA.debugLineNum = 334;BA.debugLine="Guardar_en_disco";
_guardar_en_disco();
 if (true) break;

case 34:
//C
this.state = -1;
;
 //BA.debugLineNum = 336;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _jobdone(iNKuBo.Compras.httpjob _j) throws Exception{
}
public static String  _guardar_en_memoria() throws Exception{
int _i = 0;
 //BA.debugLineNum = 250;BA.debugLine="Sub guardar_en_memoria";
 //BA.debugLineNum = 251;BA.debugLine="Log(\"Guardar en memoria\" )";
anywheresoftware.b4a.keywords.Common.LogImpl("8589825","Guardar en memoria",0);
 //BA.debugLineNum = 252;BA.debugLine="lisss.Initialize";
_lisss.Initialize();
 //BA.debugLineNum = 253;BA.debugLine="Dim i As Int";
_i = 0;
 //BA.debugLineNum = 254;BA.debugLine="For i=0 To ListView1.Size-1";
{
final int step4 = 1;
final int limit4 = (int) (mostCurrent._listview1.getSize()-1);
_i = (int) (0) ;
for (;_i <= limit4 ;_i = _i + step4 ) {
 //BA.debugLineNum = 255;BA.debugLine="lisss.Add(ListView1.GetItem(i))";
_lisss.Add(mostCurrent._listview1.GetItem(_i));
 }
};
 //BA.debugLineNum = 257;BA.debugLine="End Sub";
return "";
}
public static String  _leer_de_disco() throws Exception{
 //BA.debugLineNum = 461;BA.debugLine="Sub Leer_de_disco";
 //BA.debugLineNum = 462;BA.debugLine="Log(\"Leer de disco\")";
anywheresoftware.b4a.keywords.Common.LogImpl("8917505","Leer de disco",0);
 //BA.debugLineNum = 463;BA.debugLine="ListView1.SingleLineLayout.Label.Color=Colors.Red";
mostCurrent._listview1.getSingleLineLayout().Label.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 464;BA.debugLine="cargar_lista(File.ReadList(File.DirInternal, fich";
_cargar_lista(anywheresoftware.b4a.keywords.Common.File.ReadList(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),mostCurrent._fichero_datos));
 //BA.debugLineNum = 465;BA.debugLine="Log(\"Leido de disco\")";
anywheresoftware.b4a.keywords.Common.LogImpl("8917508","Leido de disco",0);
 //BA.debugLineNum = 467;BA.debugLine="End Sub";
return "";
}
public static void  _leer_de_dropbox() throws Exception{
ResumableSub_Leer_de_Dropbox rsub = new ResumableSub_Leer_de_Dropbox(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_Leer_de_Dropbox extends BA.ResumableSub {
public ResumableSub_Leer_de_Dropbox(iNKuBo.Compras.main parent) {
this.parent = parent;
}
iNKuBo.Compras.main parent;
de.donmanfred.dbxv2.DbxHostWrapper _dbxhost = null;
boolean _success = false;
de.donmanfred.dbxv2.files.FileMetadataWrapper _meta = null;
String _sessionid = "";

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 141;BA.debugLine="Log (\"leer de iternet Dropbox\")";
anywheresoftware.b4a.keywords.Common.LogImpl("8458754","leer de iternet Dropbox",0);
 //BA.debugLineNum = 149;BA.debugLine="config.Initialize(\"\",access_token,key,\"de-de\",5)";
parent._config.Initialize(processBA,"",parent._access_token,parent._key,"de-de",(int) (5));
 //BA.debugLineNum = 150;BA.debugLine="Dim dbxhost As DbxHost";
_dbxhost = new de.donmanfred.dbxv2.DbxHostWrapper();
 //BA.debugLineNum = 151;BA.debugLine="dbxhost.Initialize";
_dbxhost.Initialize(processBA);
 //BA.debugLineNum = 152;BA.debugLine="client.Initialize(\"Dropbox\",config,access_token,d";
parent._client.Initialize(processBA,"Dropbox",(com.dropbox.core.DbxRequestConfig)(parent._config.getObject()),parent._access_token,(com.dropbox.core.DbxHost)(_dbxhost.getObject()));
 //BA.debugLineNum = 153;BA.debugLine="dbxFiles = client.files";
parent._dbxfiles = parent._client.files();
 //BA.debugLineNum = 154;BA.debugLine="dbxFiles.setEventname(\"dbxFiles\")";
parent._dbxfiles.setEventname(processBA,"dbxFiles");
 //BA.debugLineNum = 160;BA.debugLine="Dropbox.Initialize(\"\")";
parent._dropbox.Initialize(processBA,"");
 //BA.debugLineNum = 162;BA.debugLine="ToastMessageShow(\"internet >>> lista ...espera...";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("internet >>> lista ...espera..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 163;BA.debugLine="dbxFiles.download(\"/\"&fichero_datos,File.DirInter";
parent._dbxfiles.download("/"+parent.mostCurrent._fichero_datos,anywheresoftware.b4a.keywords.Common.File.getDirInternal(),parent.mostCurrent._fichero_datos);
 //BA.debugLineNum = 164;BA.debugLine="wait for dbxFiles_DownloadFinished(success As Boo";
anywheresoftware.b4a.keywords.Common.WaitFor("dbxfiles_downloadfinished", processBA, this, null);
this.state = 7;
return;
case 7:
//C
this.state = 1;
_success = (Boolean) result[0];
_meta = (de.donmanfred.dbxv2.files.FileMetadataWrapper) result[1];
_sessionid = (String) result[2];
;
 //BA.debugLineNum = 165;BA.debugLine="If success Then";
if (true) break;

case 1:
//if
this.state = 6;
if (_success) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 166;BA.debugLine="ToastMessageShow(\"Internet -> Telefono [Lista:OK";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Internet -> Telefono [Lista:OK]"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 167;BA.debugLine="ListView1.SingleLineLayout.Label.Color=Colors.Gr";
parent.mostCurrent._listview1.getSingleLineLayout().Label.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 169;BA.debugLine="ToastMessageShow(\"Internet -> Telefono [Lista:KO";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Internet -> Telefono [Lista:KO]"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 170;BA.debugLine="ListView1.SingleLineLayout.Label.Color=Colors.Re";
parent.mostCurrent._listview1.getSingleLineLayout().Label.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 172;BA.debugLine="Leer_de_disco";
_leer_de_disco();
 //BA.debugLineNum = 177;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _leer_de_pastebin() throws Exception{
ResumableSub_Leer_de_pastebin rsub = new ResumableSub_Leer_de_pastebin(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_Leer_de_pastebin extends BA.ResumableSub {
public ResumableSub_Leer_de_pastebin(iNKuBo.Compras.main parent) {
this.parent = parent;
}
iNKuBo.Compras.main parent;
iNKuBo.Compras.httpjob _j = null;
iNKuBo.Compras.xml2map _xm = null;
String _xml = "";
anywheresoftware.b4a.objects.collections.Map _root = null;
int _a = 0;
anywheresoftware.b4a.objects.collections.Map _book = null;
String _title = "";
anywheresoftware.b4a.BA.IterableList group40;
int index40;
int groupLen40;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 350;BA.debugLine="ToastMessageShow(\"internet =======> lista\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("internet =======> lista"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 351;BA.debugLine="Log(\"Leer de internet\")";
anywheresoftware.b4a.keywords.Common.LogImpl("8786434","Leer de internet",0);
 //BA.debugLineNum = 353;BA.debugLine="Log(\"Login\")";
anywheresoftware.b4a.keywords.Common.LogImpl("8786436","Login",0);
 //BA.debugLineNum = 354;BA.debugLine="Dim j As HttpJob";
_j = new iNKuBo.Compras.httpjob();
 //BA.debugLineNum = 355;BA.debugLine="j.Initialize(\"\", Me)";
_j._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 357;BA.debugLine="url=\"https://pastebin.com/api/api_login.php\"";
parent._url = "https://pastebin.com/api/api_login.php";
 //BA.debugLineNum = 358;BA.debugLine="data=\"api_dev_key=\"&api_dev_key&\"&api_user_name=\"";
parent._data = "api_dev_key="+parent._api_dev_key+"&api_user_name="+parent._user+"&api_user_password="+parent._pass;
 //BA.debugLineNum = 360;BA.debugLine="j.PostString(url, data)";
_j._poststring /*String*/ (parent._url,parent._data);
 //BA.debugLineNum = 362;BA.debugLine="Wait For (j) JobDone(j As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_j));
this.state = 41;
return;
case 41:
//C
this.state = 1;
_j = (iNKuBo.Compras.httpjob) result[0];
;
 //BA.debugLineNum = 363;BA.debugLine="If j.Success Then";
if (true) break;

case 1:
//if
this.state = 6;
if (_j._success /*boolean*/ ) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 365;BA.debugLine="Log(\"User_KEY:\" & j.GetString)";
anywheresoftware.b4a.keywords.Common.LogImpl("8786448","User_KEY:"+_j._getstring /*String*/ (),0);
 //BA.debugLineNum = 366;BA.debugLine="api_user_key=j.GetString";
parent._api_user_key = _j._getstring /*String*/ ();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 369;BA.debugLine="api_user_key=\"\"";
parent._api_user_key = "";
 //BA.debugLineNum = 370;BA.debugLine="Log(\"No Loggeado\")";
anywheresoftware.b4a.keywords.Common.LogImpl("8786453","No Loggeado",0);
 if (true) break;

case 6:
//C
this.state = 7;
;
 //BA.debugLineNum = 373;BA.debugLine="j.Release";
_j._release /*String*/ ();
 //BA.debugLineNum = 374;BA.debugLine="If api_user_key<>\"\" Then";
if (true) break;

case 7:
//if
this.state = 37;
if ((parent._api_user_key).equals("") == false) { 
this.state = 9;
}if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 375;BA.debugLine="Log(\"Leida \" & api_user_key)";
anywheresoftware.b4a.keywords.Common.LogImpl("8786458","Leida "+parent._api_user_key,0);
 //BA.debugLineNum = 376;BA.debugLine="data=\"\"";
parent._data = "";
 //BA.debugLineNum = 377;BA.debugLine="url=\"https://pastebin.com/api/api_post.php\"";
parent._url = "https://pastebin.com/api/api_post.php";
 //BA.debugLineNum = 379;BA.debugLine="data=\"api_dev_key=\"&api_dev_key&\"&api_option=lis";
parent._data = "api_dev_key="+parent._api_dev_key+"&api_option=list&api_user_key="+parent._api_user_key+"&api_results_limit=120";
 //BA.debugLineNum = 381;BA.debugLine="Dim j As HttpJob";
_j = new iNKuBo.Compras.httpjob();
 //BA.debugLineNum = 382;BA.debugLine="j.Initialize(\"\", Me)";
_j._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 383;BA.debugLine="j.PostString(url, data)";
_j._poststring /*String*/ (parent._url,parent._data);
 //BA.debugLineNum = 385;BA.debugLine="Wait For (j) JobDone(j As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_j));
this.state = 42;
return;
case 42:
//C
this.state = 10;
_j = (iNKuBo.Compras.httpjob) result[0];
;
 //BA.debugLineNum = 386;BA.debugLine="If j.Success And j.GetString <>\"No pastes found.";
if (true) break;

case 10:
//if
this.state = 36;
if (_j._success /*boolean*/  && (_j._getstring /*String*/ ()).equals("No pastes found.") == false) { 
this.state = 12;
}else {
this.state = 35;
}if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 388;BA.debugLine="data=j.GetString";
parent._data = _j._getstring /*String*/ ();
 //BA.debugLineNum = 389;BA.debugLine="j.Release";
_j._release /*String*/ ();
 //BA.debugLineNum = 391;BA.debugLine="Dim xm As Xml2Map";
_xm = new iNKuBo.Compras.xml2map();
 //BA.debugLineNum = 392;BA.debugLine="xm.Initialize";
_xm._initialize /*String*/ (processBA);
 //BA.debugLineNum = 394;BA.debugLine="Dim xml As String";
_xml = "";
 //BA.debugLineNum = 395;BA.debugLine="xml=\"<root>\"&data&\"</root>\"";
_xml = "<root>"+parent._data+"</root>";
 //BA.debugLineNum = 397;BA.debugLine="Log(xml)";
anywheresoftware.b4a.keywords.Common.LogImpl("8786480",_xml,0);
 //BA.debugLineNum = 398;BA.debugLine="ParsedData = xm.Parse(xml)";
parent._parseddata = _xm._parse /*anywheresoftware.b4a.objects.collections.Map*/ (_xml);
 //BA.debugLineNum = 399;BA.debugLine="url=\"\"";
parent._url = "";
 //BA.debugLineNum = 400;BA.debugLine="Dim root As Map = ParsedData.Get(\"root\")";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = (anywheresoftware.b4a.objects.collections.Map) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.Map(), (anywheresoftware.b4a.objects.collections.Map.MyMap)(parent._parseddata.Get((Object)("root"))));
 //BA.debugLineNum = 401;BA.debugLine="Dim a As Int";
_a = 0;
 //BA.debugLineNum = 402;BA.debugLine="a=0";
_a = (int) (0);
 //BA.debugLineNum = 403;BA.debugLine="For Each book As Map In GetElements(root, \"past";
if (true) break;

case 13:
//for
this.state = 20;
_book = new anywheresoftware.b4a.objects.collections.Map();
group40 = _getelements(_root,"paste");
index40 = 0;
groupLen40 = group40.getSize();
this.state = 43;
if (true) break;

case 43:
//C
this.state = 20;
if (index40 < groupLen40) {
this.state = 15;
_book = (anywheresoftware.b4a.objects.collections.Map) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.Map(), (anywheresoftware.b4a.objects.collections.Map.MyMap)(group40.Get(index40)));}
if (true) break;

case 44:
//C
this.state = 43;
index40++;
if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 404;BA.debugLine="Dim title As String = book.Get(\"paste_title\")";
_title = BA.ObjectToString(_book.Get((Object)("paste_title")));
 //BA.debugLineNum = 405;BA.debugLine="Log(\"Titulo:\" & title)";
anywheresoftware.b4a.keywords.Common.LogImpl("8786488","Titulo:"+_title,0);
 //BA.debugLineNum = 406;BA.debugLine="If title=\"lista.txt\" Then";
if (true) break;

case 16:
//if
this.state = 19;
if ((_title).equals("lista.txt")) { 
this.state = 18;
}if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 407;BA.debugLine="url=book.Get(\"paste_url\")";
parent._url = BA.ObjectToString(_book.Get((Object)("paste_url")));
 //BA.debugLineNum = 408;BA.debugLine="Log(\"Encontrada url \"&url)";
anywheresoftware.b4a.keywords.Common.LogImpl("8786491","Encontrada url "+parent._url,0);
 //BA.debugLineNum = 409;BA.debugLine="Exit";
this.state = 20;
if (true) break;
 if (true) break;

case 19:
//C
this.state = 44;
;
 //BA.debugLineNum = 411;BA.debugLine="a=a+1";
_a = (int) (_a+1);
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 416;BA.debugLine="If url<>\"\" Then";

case 20:
//if
this.state = 33;
if ((parent._url).equals("") == false) { 
this.state = 22;
}if (true) break;

case 22:
//C
this.state = 23;
 //BA.debugLineNum = 417;BA.debugLine="fichero=Regex.Split(\"/\",url)(3)";
parent._fichero = anywheresoftware.b4a.keywords.Common.Regex.Split("/",parent._url)[(int) (3)];
 //BA.debugLineNum = 418;BA.debugLine="Log(\"fichero \"&fichero)";
anywheresoftware.b4a.keywords.Common.LogImpl("8786501","fichero "+parent._fichero,0);
 //BA.debugLineNum = 419;BA.debugLine="url=\"https://pastebin.com/raw/\"&fichero";
parent._url = "https://pastebin.com/raw/"+parent._fichero;
 //BA.debugLineNum = 420;BA.debugLine="If url<>\"\" Then";
if (true) break;

case 23:
//if
this.state = 32;
if ((parent._url).equals("") == false) { 
this.state = 25;
}if (true) break;

case 25:
//C
this.state = 26;
 //BA.debugLineNum = 421;BA.debugLine="j.Initialize(\"\", Me)";
_j._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 422;BA.debugLine="j.Download(url)";
_j._download /*String*/ (parent._url);
 //BA.debugLineNum = 423;BA.debugLine="Wait For (j) JobDone(j As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_j));
this.state = 45;
return;
case 45:
//C
this.state = 26;
_j = (iNKuBo.Compras.httpjob) result[0];
;
 //BA.debugLineNum = 424;BA.debugLine="If j.Success Then";
if (true) break;

case 26:
//if
this.state = 31;
if (_j._success /*boolean*/ ) { 
this.state = 28;
}else {
this.state = 30;
}if (true) break;

case 28:
//C
this.state = 31;
 //BA.debugLineNum = 425;BA.debugLine="Log(j.GetString)";
anywheresoftware.b4a.keywords.Common.LogImpl("8786508",_j._getstring /*String*/ (),0);
 //BA.debugLineNum = 427;BA.debugLine="lisss=Regex.Split(\",\",j.GetString)";
parent._lisss = anywheresoftware.b4a.keywords.Common.ArrayToList(anywheresoftware.b4a.keywords.Common.Regex.Split(",",_j._getstring /*String*/ ()));
 //BA.debugLineNum = 428;BA.debugLine="ToastMessageShow(\"Lista de internet leida\",F";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Lista de internet leida"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 30:
//C
this.state = 31;
 //BA.debugLineNum = 430;BA.debugLine="Log(\"Error leyendo el fichero \"&url)";
anywheresoftware.b4a.keywords.Common.LogImpl("8786513","Error leyendo el fichero "+parent._url,0);
 if (true) break;

case 31:
//C
this.state = 32;
;
 //BA.debugLineNum = 432;BA.debugLine="j.Release";
_j._release /*String*/ ();
 if (true) break;

case 32:
//C
this.state = 33;
;
 if (true) break;

case 33:
//C
this.state = 36;
;
 if (true) break;

case 35:
//C
this.state = 36;
 //BA.debugLineNum = 436;BA.debugLine="Log(\"No habia ficheros de listas en pastebin\")";
anywheresoftware.b4a.keywords.Common.LogImpl("8786519","No habia ficheros de listas en pastebin",0);
 if (true) break;

case 36:
//C
this.state = 37;
;
 if (true) break;
;
 //BA.debugLineNum = 440;BA.debugLine="If lisss.Size=0 Then";

case 37:
//if
this.state = 40;
if (parent._lisss.getSize()==0) { 
this.state = 39;
}if (true) break;

case 39:
//C
this.state = 40;
 //BA.debugLineNum = 441;BA.debugLine="Log(\"No conectado a internet, leo de disco\")";
anywheresoftware.b4a.keywords.Common.LogImpl("8786524","No conectado a internet, leo de disco",0);
 //BA.debugLineNum = 442;BA.debugLine="lisss.AddAll(File.ReadList(File.DirInternal, fic";
parent._lisss.AddAll(anywheresoftware.b4a.keywords.Common.File.ReadList(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),parent.mostCurrent._fichero_datos));
 if (true) break;

case 40:
//C
this.state = -1;
;
 //BA.debugLineNum = 445;BA.debugLine="cargar_lista(lisss)";
_cargar_lista(parent._lisss);
 //BA.debugLineNum = 446;BA.debugLine="ToastMessageShow(\"Lista cargada\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Lista cargada"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 447;BA.debugLine="Log(\"Leído todo\" )";
anywheresoftware.b4a.keywords.Common.LogImpl("8786530","Leído todo",0);
 //BA.debugLineNum = 449;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _listview1_itemlongclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 132;BA.debugLine="Private Sub ListView1_ItemLongClick (Position As I";
 //BA.debugLineNum = 133;BA.debugLine="ListView1.removeAt(Position)";
mostCurrent._listview1.RemoveAt(_position);
 //BA.debugLineNum = 135;BA.debugLine="guardar_en_memoria";
_guardar_en_memoria();
 //BA.debugLineNum = 137;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
starter._process_globals();
httputils2service._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 22;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 28;BA.debugLine="Private key As String = \"lj61utogy2w47zb\"";
_key = "lj61utogy2w47zb";
 //BA.debugLineNum = 30;BA.debugLine="Private access_token=\"dOCrtawc6EQAAAAAAAAAAWMAgy4";
_access_token = "dOCrtawc6EQAAAAAAAAAAWMAgy428MUF7MMlTUUJN-wvwT8M5DtsrR3i-Enqr8Il";
 //BA.debugLineNum = 35;BA.debugLine="Dim Dropbox As DropboxV2";
_dropbox = new de.donmanfred.dbxv2.DropboxWrapper();
 //BA.debugLineNum = 37;BA.debugLine="Dim client As DbxClientV2";
_client = new de.donmanfred.dbxv2.DbxClientV2Wrapper();
 //BA.debugLineNum = 38;BA.debugLine="Dim config As DbxRequestConfig";
_config = new de.donmanfred.dbxv2.DbxRequestConfigWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Dim dbxFiles As DbxUserFilesRequests";
_dbxfiles = new de.donmanfred.dbxv2.files.DbxUserFilesRequestsWrapper();
 //BA.debugLineNum = 45;BA.debugLine="Private xui As XUI";
_xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 46;BA.debugLine="Dim api_dev_key As String";
_api_dev_key = "";
 //BA.debugLineNum = 47;BA.debugLine="Dim api_paste_code As String";
_api_paste_code = "";
 //BA.debugLineNum = 48;BA.debugLine="Dim api_user_key As String";
_api_user_key = "";
 //BA.debugLineNum = 49;BA.debugLine="Dim url As String";
_url = "";
 //BA.debugLineNum = 50;BA.debugLine="Dim data As String";
_data = "";
 //BA.debugLineNum = 51;BA.debugLine="Dim ParsedData As Map";
_parseddata = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 52;BA.debugLine="Dim user As String";
_user = "";
 //BA.debugLineNum = 53;BA.debugLine="Dim pass As String";
_pass = "";
 //BA.debugLineNum = 54;BA.debugLine="Dim fichero As String";
_fichero = "";
 //BA.debugLineNum = 55;BA.debugLine="Dim lisss As List";
_lisss = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 57;BA.debugLine="End Sub";
return "";
}
}
