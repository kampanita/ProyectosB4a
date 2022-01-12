package iNKuBo.Compras;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class map2xml extends B4AClass.ImplB4AClass implements BA.SubDelegator{
    private static java.util.HashMap<String, java.lang.reflect.Method> htSubs;
    private void innerInitialize(BA _ba) throws Exception {
        if (ba == null) {
            ba = new BA(_ba, this, htSubs, "iNKuBo.Compras.map2xml");
            if (htSubs == null) {
                ba.loadHtSubs(this.getClass());
                htSubs = ba.htSubs;
            }
            
        }
        if (BA.isShellModeRuntimeCheck(ba)) 
			   this.getClass().getMethod("_class_globals", iNKuBo.Compras.map2xml.class).invoke(this, new Object[] {null});
        else
            ba.raiseEvent2(null, true, "class_globals", false);
    }

 public anywheresoftware.b4a.keywords.Common __c = null;
public com.jamesmurty.utils.XMLBuilder _builder = null;
public iNKuBo.Compras.main _main = null;
public iNKuBo.Compras.starter _starter = null;
public iNKuBo.Compras.httputils2service _httputils2service = null;
public String  _class_globals() throws Exception{
 //BA.debugLineNum = 2;BA.debugLine="Sub Class_Globals";
 //BA.debugLineNum = 3;BA.debugLine="Private builder As XMLBuilder";
_builder = new com.jamesmurty.utils.XMLBuilder();
 //BA.debugLineNum = 4;BA.debugLine="End Sub";
return "";
}
public String  _handleelement(String _key,Object _value) throws Exception{
 //BA.debugLineNum = 44;BA.debugLine="Private Sub HandleElement (key As String, value As";
 //BA.debugLineNum = 45;BA.debugLine="If value Is Map Then";
if (_value instanceof anywheresoftware.b4a.objects.collections.Map.MyMap) { 
 //BA.debugLineNum = 46;BA.debugLine="If key <> \"\" Then builder = builder.element(key)";
if ((_key).equals("") == false) { 
_builder = _builder.element(_key);};
 //BA.debugLineNum = 47;BA.debugLine="HandleMapElement(value)";
_handlemapelement((anywheresoftware.b4a.objects.collections.Map) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.Map(), (anywheresoftware.b4a.objects.collections.Map.MyMap)(_value)));
 //BA.debugLineNum = 48;BA.debugLine="If key <> \"\" Then builder = builder.up";
if ((_key).equals("") == false) { 
_builder = _builder.up();};
 }else if(_value instanceof java.util.List) { 
 //BA.debugLineNum = 50;BA.debugLine="HandleListElement (key, value)";
_handlelistelement(_key,(anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(_value)));
 }else {
 //BA.debugLineNum = 52;BA.debugLine="builder = builder.element(key)";
_builder = _builder.element(_key);
 //BA.debugLineNum = 53;BA.debugLine="builder = builder.text(value)";
_builder = _builder.text(BA.ObjectToString(_value));
 //BA.debugLineNum = 54;BA.debugLine="builder = builder.up";
_builder = _builder.up();
 };
 //BA.debugLineNum = 56;BA.debugLine="End Sub";
return "";
}
public String  _handlelistelement(String _key,anywheresoftware.b4a.objects.collections.List _lst) throws Exception{
Object _value = null;
 //BA.debugLineNum = 58;BA.debugLine="Private Sub HandleListElement (key As String, lst";
 //BA.debugLineNum = 59;BA.debugLine="For Each value As Object In lst";
{
final anywheresoftware.b4a.BA.IterableList group1 = _lst;
final int groupLen1 = group1.getSize()
;int index1 = 0;
;
for (; index1 < groupLen1;index1++){
_value = group1.Get(index1);
 //BA.debugLineNum = 60;BA.debugLine="HandleElement(key, value)";
_handleelement(_key,_value);
 }
};
 //BA.debugLineNum = 62;BA.debugLine="End Sub";
return "";
}
public String  _handlemapelement(anywheresoftware.b4a.objects.collections.Map _m) throws Exception{
anywheresoftware.b4a.objects.collections.Map _attributes = null;
String _attr = "";
String _k = "";
Object _value = null;
 //BA.debugLineNum = 28;BA.debugLine="Private Sub HandleMapElement (m As Map)";
 //BA.debugLineNum = 29;BA.debugLine="Dim attributes As Map = m.Get(\"Attributes\")";
_attributes = new anywheresoftware.b4a.objects.collections.Map();
_attributes = (anywheresoftware.b4a.objects.collections.Map) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.Map(), (anywheresoftware.b4a.objects.collections.Map.MyMap)(_m.Get((Object)("Attributes"))));
 //BA.debugLineNum = 30;BA.debugLine="If attributes.IsInitialized Then";
if (_attributes.IsInitialized()) { 
 //BA.debugLineNum = 31;BA.debugLine="For Each attr As String In attributes.Keys";
{
final anywheresoftware.b4a.BA.IterableList group3 = _attributes.Keys();
final int groupLen3 = group3.getSize()
;int index3 = 0;
;
for (; index3 < groupLen3;index3++){
_attr = BA.ObjectToString(group3.Get(index3));
 //BA.debugLineNum = 32;BA.debugLine="builder.attribute(attr, attributes.Get(attr))";
_builder.attribute(_attr,BA.ObjectToString(_attributes.Get((Object)(_attr))));
 }
};
 //BA.debugLineNum = 34;BA.debugLine="If m.ContainsKey(\"Text\") Then builder.text(m.Get";
if (_m.ContainsKey((Object)("Text"))) { 
_builder.text(BA.ObjectToString(_m.Get((Object)("Text"))));};
 //BA.debugLineNum = 35;BA.debugLine="m.Remove(\"Attributes\")";
_m.Remove((Object)("Attributes"));
 //BA.debugLineNum = 36;BA.debugLine="m.Remove(\"Text\")";
_m.Remove((Object)("Text"));
 };
 //BA.debugLineNum = 38;BA.debugLine="For Each k As String In m.Keys";
{
final anywheresoftware.b4a.BA.IterableList group10 = _m.Keys();
final int groupLen10 = group10.getSize()
;int index10 = 0;
;
for (; index10 < groupLen10;index10++){
_k = BA.ObjectToString(group10.Get(index10));
 //BA.debugLineNum = 39;BA.debugLine="Dim value As Object = m.Get(k)";
_value = _m.Get((Object)(_k));
 //BA.debugLineNum = 40;BA.debugLine="HandleElement(k, value)";
_handleelement(_k,_value);
 }
};
 //BA.debugLineNum = 42;BA.debugLine="End Sub";
return "";
}
public String  _initialize(anywheresoftware.b4a.BA _ba) throws Exception{
innerInitialize(_ba);
 //BA.debugLineNum = 6;BA.debugLine="Public Sub Initialize";
 //BA.debugLineNum = 8;BA.debugLine="End Sub";
return "";
}
public String  _maptoxml(anywheresoftware.b4a.objects.collections.Map _m) throws Exception{
String _k = "";
anywheresoftware.b4a.objects.collections.Map _props = null;
 //BA.debugLineNum = 10;BA.debugLine="Public Sub MapToXml (m As Map) As String";
 //BA.debugLineNum = 11;BA.debugLine="For Each k As String In m.Keys";
{
final anywheresoftware.b4a.BA.IterableList group1 = _m.Keys();
final int groupLen1 = group1.getSize()
;int index1 = 0;
;
for (; index1 < groupLen1;index1++){
_k = BA.ObjectToString(group1.Get(index1));
 //BA.debugLineNum = 12;BA.debugLine="builder = builder.create(k)";
_builder = _builder.create(_k);
 //BA.debugLineNum = 13;BA.debugLine="HandleElement(\"\", m.Get(k))";
_handleelement("",_m.Get((Object)(_k)));
 //BA.debugLineNum = 14;BA.debugLine="Exit";
if (true) break;
 }
};
 //BA.debugLineNum = 16;BA.debugLine="builder = builder.up";
_builder = _builder.up();
 //BA.debugLineNum = 18;BA.debugLine="Dim props As Map";
_props = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 19;BA.debugLine="props.Initialize";
_props.Initialize();
 //BA.debugLineNum = 20;BA.debugLine="props.Put(\"{http://xml.apache.org/xslt}indent-amo";
_props.Put((Object)("{http://xml.apache.org/xslt}indent-amount"),(Object)("4"));
 //BA.debugLineNum = 21;BA.debugLine="props.Put(\"indent\", \"yes\")";
_props.Put((Object)("indent"),(Object)("yes"));
 //BA.debugLineNum = 22;BA.debugLine="Return builder.asString2(props)";
if (true) return _builder.asString2((java.util.Map)(_props.getObject()));
 //BA.debugLineNum = 26;BA.debugLine="End Sub";
return "";
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
BA.senderHolder.set(sender);
return BA.SubDelegator.SubNotFound;
}
}
