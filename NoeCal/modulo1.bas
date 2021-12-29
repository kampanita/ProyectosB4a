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
	Dim sql As SQL
	Private xui As XUI

End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	Private Spinner1 As Spinner
	Private Aceptar As Button
	Private Cancelar As Button
	Private Fecha As EditText
	Private FechaPicker As Button
	Private Horas As EditText
	Private Label1 As Label
	Private Label2 As Label
	Private Label3 As Label
	Private Dialog As B4XDialog
	Private Panel1 As Panel
	Dim DateTemplate As B4XDateTemplate
	Private Spinner2 As Spinner
	Private Incidencia As EditText
	Private Borrar As Button
End Sub

Sub Activity_Create(FirstTime As Boolean)
	sql.Initialize(File.DirInternal, "NoeCal.db", False)
	'Do not forget to load the layout file created with the visual designer. For example:
	'Activity.LoadLayout("Layout1")
	Activity.LoadLayout("introducir")
	Spinner1.addall(Array As String("","Dia Normal","Dia Festivo Ordinario","Dia Festivo Extraordinario","Noche Normal","Noche Festivo Ordinario","Noche Festivo Extraordinario"))
	Spinner2.addall(Array As String("","M31","M32","M33","M34","M41","M42","M43","M51","M52","T31","T32","T33","T34","T41","T42","T43","T5","N3","N4","N5","RT"))
	Horas.Text=""
	Incidencia.Text=""
	Fecha.Text=""
	Spinner2.SelectedIndex=0
	Spinner1.SelectedIndex=0
	
	Dialog.Initialize(Activity)
	Dialog.TitleBarColor=xui.Color_Transparent
	Dialog.BackgroundColor=xui.Color_Gray
	
	DateTemplate.Initialize
	DateTemplate.MinYear = 2021
	DateTemplate.MaxYear = 2050
	Dialog.Title = "Escoge la fecha"
	SetLightTheme
	
	
	
     
End Sub


Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub


Private Sub FechaPicker_Click
	'Seleccionar la fecha
	'fUNCIONA en el emulador pero no en el telefono.
	'---------------------------------------------------------------------------------------
	'Dim D As DateDialog
	'D.Initialize(Me,"",DateTime.Now)
	'D.Show("Selecciona dia","Si","Cancelar","No")
	'D.Show("","","","")
	'R=D.DoEventWaitResponce
	
	'ToastMessageShow(R&" "&D.DataToText(D.DateTick),True)
	'Fecha.Text=D.DataToText(D.DateTick)
	'---------------------------------------------------------------------------------------
	
	
	Wait For (Dialog.ShowTemplate(DateTemplate, "", "", "Cancel")) Complete (Result As Int)
	
	If Result = xui.DialogResponse_Positive Then		
		Fecha.Text=datatotexT(DateTemplate.Date)
	End If


	Dim rs As ResultSet
	Dim respuesta As Int
	respuesta=sql.ExecQuerySingleResult($"select count(*) from dias where dia='"$&Fecha.Text&$"'"$ )
	'Log(respuesta)
	
	If respuesta=1 Then
		ToastMessageShow("Parte ya existía, recuperamos la información.",False)
		rs=sql.ExecQuery($"select Dia,Horas,Tipo,tipo2,incidencia from dias where Dia='"$&Fecha.Text&$"'"$ )
		rs.Position=0

		Horas.Text=rs.GetDouble("Horas")
		Dim cual As String
		cual =rs.GetString("Tipo")
		Spinner1.SelectedIndex=Spinner1.IndexOf(cual)
		
		cual=rs.GetString("Tipo2")
		Spinner2.SelectedIndex=Spinner2.IndexOf(cual)
		
		Incidencia.text=rs.getString("Incidencia")
	Else
		Horas.Text=""			
		Spinner1.SelectedIndex=0
		Spinner2.SelectedIndex=0
	End If
	
End Sub

Sub datatotexT(datos As Long) As String
	DateTime.DateFormat = "dd MMMM yyyy"
	'Log(DateTime.Date(datos))
	Return DateTime.Date(datos)
	
End Sub

Private Sub Cancelar_Click
	'Activity.LoadLayout("Layout")
	Activity.finish
End Sub

Private Sub Aceptar_Click
	If Horas.Text<>"" And Fecha.Text <> "" And Spinner1.SelectedIndex<>0 And Spinner2.SelectedIndex<>0 Then
		Dim rs As ResultSet
		
		Dim respuesta As Int
		respuesta=sql.ExecQuerySingleResult($"select count(*) from dias where dia='"$&Fecha.Text&$"'"$ )
		
		Dim query As String
		If respuesta=0 Then
			'hacer insert
			Dim tipo As String
			tipo=Spinner1.SelectedItem
			query="insert into dias(dia,horas,tipo,tipo2,incidencia) values(?,?,?,?,?)"
			Try
				sql.ExecNonQuery2(query,Array As Object(Fecha.Text,Horas.Text,tipo,Spinner2.SelectedItem,Incidencia.text))
			Catch
				Log(LastException)
			End Try
			rs=sql.ExecQuery("commit")
		
			rs.Close
			ToastMessageShow("Se ha guardado el parte en la BBDD",True)
			' inicializamos.
			Fecha.Text=""
			Horas.Text=""
			Incidencia.Text=""
			Spinner2.SelectedIndex=0
			Spinner1.SelectedIndex=0
		Else
			Dim tipo As String
			tipo=Spinner1.SelectedItem
			query="update dias set Horas=?,tipo=?,tipo2=?,incidencia=? where dia='"&Fecha.Text&"'"
			Try
				sql.ExecNonQuery2(query,Array As Object(Horas.Text,tipo,Spinner2.SelectedItem,Incidencia.text))
			Catch
				Log(LastException)
			End Try
			rs=sql.ExecQuery("commit")
			'hacer update
		
			rs.Close
			ToastMessageShow("Se ha guardado el parte en la BBDD",False)
		End If
		
		'Activity.Finish
		
	End If
End Sub

Sub SetLightTheme
	Dialog.TitleBarColor = xui.Color_Blue
	Dialog.TitleBarHeight = 80dip
	Dim TextColor As Int = 0xFF5B5B5B
	Dialog.BackgroundColor = xui.Color_White
	Dialog.ButtonsColor = xui.Color_White
	Dialog.ButtonsTextColor = Dialog.TitleBarColor
	Dialog.BorderColor = xui.Color_Transparent
	DateTemplate.DaysInWeekColor = xui.Color_Black
	DateTemplate.SelectedColor = 0xFF39D7CE
	DateTemplate.HighlightedColor = 0xFF00CEFF
	DateTemplate.DaysInMonthColor = TextColor
	DateTemplate.lblMonth.TextColor = TextColor
	DateTemplate.lblYear.TextColor = TextColor
	DateTemplate.SelectedColor = 0xFFFFA761
	
	For Each b As B4XView In Array(DateTemplate.btnMonthLeft, DateTemplate.btnMonthRight, DateTemplate.btnYearLeft, DateTemplate.btnYearRight)
		b.Color = xui.Color_Transparent
		b.TextColor = TextColor
        #if B4i
        Dim no As NativeObject = b
        no.RunMethod("setTitleColor:forState:", Array(no.ColorToUIColor(TextColor), 0))
        #End If
	Next
End Sub

'


Private Sub Spinner2_ItemClick (Position As Int, Value As Object)
	Dim valor As String
	valor=Value
'	Log("valor "&valor)
	
	If valor.startswith("M") Or valor.startswith("T") Then
		Horas.Text="7.25"
	End If
	If valor.startswith("N") Then
		Horas.Text="9.5"
	End If
	If valor.StartsWith("RT") Then
		Horas.Text="2.25"
	End If
End Sub

Sub ver_registro(lafecha As String)
	
	Activity.LoadLayout("introducir")
	Spinner1.addall(Array As String("","Dia Normal","Dia Festivo Ordinario","Dia Festivo Extraordinario","Noche Normal","Noche Festivo Ordinario","Noche Festivo Extraordinario"))
	Spinner2.addall(Array As String("","M31","M32","M33","M34","M41","M42","M43","M51","M52","T31","T32","T33","T34","T41","T42","T43","T5","N3","N4","N5","RT"))
	
	Dim rs As ResultSet
	
	Dim respuesta As Int
	respuesta=sql.ExecQuerySingleResult($"select count(*) from dias where dia='"$&lafecha&$"'"$ )
	'Log(respuesta)
	
	If respuesta=1 Then
		
		rs=sql.ExecQuery($"select Dia,Horas,Tipo,tipo2,incidencia from dias where Dia='"$&lafecha&$"'"$ )
		rs.Position=0
		
		Fecha.Text=rs.GetString("Dia")
		Horas.Text=rs.GetDouble("Horas")
		Incidencia.Text=rs.GetString("Incidencia")
		Dim cual As String
		cual =rs.GetString("Tipo")
		Spinner1.SelectedIndex=Spinner1.IndexOf(cual)
		cual =rs.GetString("Tipo2")
		Spinner2.SelectedIndex=Spinner2.IndexOf(cual)
		
	End If

End Sub

Private Sub Borrar_Click
	
	If Fecha.Text<>"" Then
		Dim d As B4XDialog
		d.initialize(Activity)
		d.Title="Borrar registro"
		d.BackgroundColor=Colors.White
		d.BodyTextColor=Colors.Blue
		d.VisibleAnimationDuration=300
		Dim rs As Object
		rs=d.Show("¿Quieres borrar el registro?","Si","No","")
		Wait For(rs) complete (Result As Int)
		If Result = xui.DialogResponse_Positive Then
			sql.ExecNonQuery("delete from Dias where dia='"&Fecha.text&"'")
			ToastMessageShow("Borrado el parte del dia "&Fecha.Text,True)
			Fecha.Text=""
			Spinner1.SelectedIndex=0
			Spinner2.SelectedIndex=0
			Horas.Text=""
			Incidencia.Text=""
		End If
	Else	
		ToastMessageShow("Nada que borrar",False )
	End If
End Sub