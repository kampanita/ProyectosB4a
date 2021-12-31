B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Activity
Version=11.16
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: True
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	Private xui As XUI
	Dim sql As SQL
	
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	Private SpinnerAno As Spinner
	Private SpinnerMEs As Spinner
	Private Totales As CustomListView


	Private ver_dia As Label
	Private ver_horas As Label
	Private ver_tipo As Label
	Private Grid As CustomListView
	Private total_h_mes As Label
	Private ver_tipo2 As Label
	Private ver_incidencia As Label
	Private ver_dia2 As Label
	Private ver_horas2 As Label

End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	'Activity.LoadLayout("Layout1")
	sql.Initialize(File.DirInternal, "NoeCal.db", False)
	verRegistros
End Sub

Sub Activity_Resume
	verRegistros
End Sub

Sub Activity_Pause (UserClosed As Boolean)
	If UserClosed Then
		Activity.LoadLayout("Layout")
	End If
End Sub

Sub verRegistros
	
	Activity.LoadLayout("ver")
	Dim años As List
	Dim meses As List
	años.Initialize
	meses.Initialize

	DateTime.DateFormat="yyyy"
	For a=DateTime.GetYear(DateTime.Now)-5 To DateTime.GetYear(DateTime.Now)+5
		años.Add(a)
	Next
	SpinnerAno.AddAll(años)
	DateTime.DateFormat="MMM"
	meses=DateUtils.GetMonthsNames
	SpinnerMEs.AddAll(meses)
	SpinnerAno.SelectedIndex=5 '<< este año
	SpinnerMEs.SelectedIndex=DateTime.GetMonth(DateTime.Now)-1 '<< este mes
	leer_registros
	
End Sub

Sub leer_registros
	Dim rs As ResultSet
	Dim where As String
	where="%" & SpinnerMEs.SelectedItem & "%" & SpinnerAno.SelectedItem &"%"

	'Lineas de Totales

	rs=sql.ExecQuery2("select Dia,Horas,Tipo,Tipo2,incidencia from dias where dia like ? and tipo2<>'LIB' order by Dia asc",Array As String(where))
	
	Grid.Clear
	Do While rs.NextRow
		Dim h As String
		h=rs.GetDouble("Horas")
		Grid.Add(crea_row(rs.GetString("Dia"),h,rs.GetString("Tipo"),rs.GetString("Tipo2"),rs.GetString("Incidencia")),rs.GetString("Dia"))
	Loop

	'Totales
	Dim Total_dia_normal As Double
	Dim Total_dia_festivo As Double
	Dim Total_dia_festivo_extra As Double
	Dim Total_noche_normal As Double
	Dim Total_noche_festivo As Double
	Dim Total_noche_festivo_extra As Double
	Dim Total_Global As Double
	
	'
	Dim Lista_tipos As List
	Lista_tipos.initialize
	Lista_tipos.addall(Array As String("Dia Normal","Dia Festivo Ordinario","Dia Festivo Extraordinario","Noche Normal","Noche Festivo Ordinario","Noche Festivo Extraordinario"))
		
	Total_dia_normal=sql.ExecQuerySingleResult2("select total(Horas) from dias where dia like ? and tipo=? order by dia asc",Array As String(where,Lista_tipos.get(0)))
	
	Total_dia_festivo=sql.ExecQuerySingleResult2("select ifnull(total(Horas),0) from dias where dia like ? and tipo=? ",Array As String(where,Lista_tipos.get(1)))
	Total_dia_festivo_extra=sql.ExecQuerySingleResult2("select total(Horas) from dias where dia like ? and tipo=? ",Array As String(where,Lista_tipos.get(2)))
	Total_noche_normal=sql.ExecQuerySingleResult2("select total(Horas) from dias where dia like ? and tipo=? ",Array As String(where,Lista_tipos.get(3)))
	Total_noche_festivo=sql.ExecQuerySingleResult2("select total(Horas) from dias where dia like ? and tipo=? ",Array As String(where,Lista_tipos.get(4)))
	Total_noche_festivo_extra = sql.ExecQuerySingleResult2("select total(Horas) from dias where dia like ? and tipo=? ",Array As String(where,Lista_tipos.get(5)))
	Total_Global = sql.ExecQuerySingleResult2("select total(Horas) from dias where dia like	 ? ",Array As String(where))
	
	
	'Lineas
	Totales.Clear
	Totales.Add(crea_row2("Total Horas dia normal", Total_dia_normal),rs)
	Totales.Add(crea_row2("Total Horas dia festivo ordinario", Total_dia_festivo),rs)
	Totales.Add(crea_row2("Total Horas dia festivo extraordinario", Total_dia_festivo_extra),rs)
	Totales.Add(crea_row2("Total Horas noche normales", Total_noche_normal),rs)
	Totales.Add(crea_row2("Total Horas noche festivo ordinario", Total_noche_festivo),rs)
	Totales.Add(crea_row2("Total Horas noche festivo extraordinario", Total_noche_festivo_extra),rs)
	'Grid.Add(crea_row2("TOTAL HORAS MES", Total_Global),rs)
	total_h_mes.Text="TOTAL HORAS MES: " & Total_Global
	
	rs=sql.ExecQuery("select Dia,Horas,Tipo,Tipo2,incidencia from dias order by Dia asc")
	
End Sub

Sub crea_row(text1 As String,text2 As String,text3 As String, text4 As String,text5 As String) As B4XView
	Dim p As B4XView= xui.CreatePanel("")
	p.SetLayoutAnimated(100,0,0,100%x,36dip)
	p.LoadLayout("ver_rows")
	ver_dia.Text = text1
	ver_horas.Text = text2
	ver_tipo.Text=text3
	ver_tipo2.text=text4
	ver_incidencia.Text=text5
	Return p
End Sub

Sub crea_row2(text1 As String,text2 As String) As B4XView
	Dim p As B4XView= xui.CreatePanel("")
	p.SetLayoutAnimated(100,0,0,100%x,30dip)
	p.LoadLayout("ver_rows2")
	ver_dia2.Text = text1
	ver_horas2.Text = text2

	Return p
End Sub

Private Sub SpinnerMEs_ItemClick (Position As Int, Value As Object)
	leer_registros
End Sub

Private Sub SpinnerAno_ItemClick (Position As Int, Value As Object)
	leer_registros
End Sub

Private Sub Grid_ItemClick (Index As Int, Value As Object)
    CallSubDelayed2("modulo1","ver_registro",Value)
End Sub

Private Sub ver_horas_Click
	'modulo1.Fecha.Text=modulo1.D.DataToText(modulo1.D.DateTick)
	'StartActivity("modulo1")
	'CallSub2(modulo1,"ver_reg",ver_dia.text)
	'Activity.LoadLayout("introducir")
End Sub

Private Sub ver_dia_Click
'	StartActivity("modulo1")
'	Activity.LoadLayout("introducir")
'	CallSub2(modulo1,"ver_reg",ver_dia.text)
End Sub

Private Sub Grid_ItemLongClick (Index As Int, Value As Object)
	CallSub2("modulo1","ver_registro",Value)
End Sub




