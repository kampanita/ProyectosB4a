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
	Dim xui As XUI
	Dim sql As SQL
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	Private Grid2 As CustomListView
	Private SpinnerAno2 As Spinner
	Private SpinnerMEs2 As Spinner
	Private cal_dia1 As Label
	Private cal_dia2 As Label
	Private cal_dia3 As Label
	Private cal_dia4 As Label
	Private cal_dia5 As Label
	Private cal_dia6 As Label
	Private cal_dia7 As Label
	Private cal_tipo1 As Label
	Private cal_tipo2 As Label
	Private cal_tipo3 As Label
	Private cal_tipo4 As Label
	Private cal_tipo5 As Label
	Private cal_tipo6 As Label
	Private cal_tipo7 As Label
	Private Panel6 As Panel
	Private Panel7 As Panel
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	'Activity.LoadLayout("Layout1")
	Activity.LoadLayout("ver2")
End Sub

Sub Activity_Resume
	DateTime.DateFormat="m"
	Log(DateTime.Now)
	Log(DateTime.Date(DateTime.Now))
	Dim mes As String =DateTime.Date(DateTime.Now)
	DateTime.DateFormat="yyyy"
	Dim año As String =DateTime.Date(DateTime.Now)
	VerCalendario(mes, año)
	
End Sub

Sub Activity_Pause (UserClosed As Boolean)
	If UserClosed Then
		Activity.LoadLayout("Layout")
	End If
End Sub

Sub VerCalendario(mes As Int, año As Int)
	Grid2.Clear
	Dim dias_mes As Int
	
	dias_mes=DateUtils.NumberOfDaysInMonth(mes,año)
	Log(dias_mes)
	
	Dim lista_dias As List
	lista_dias.initialize
	For i=1 To 7
		lista_dias.Add(i) 	
	Next
	Grid2.Add(crea_dia(lista_dias),1)
	
	lista_dias.initialize
	For i=8 To 14
		lista_dias.Add(i)
	Next
	Grid2.Add(crea_dia(lista_dias),2)
	
	lista_dias.Initialize
	For i=15 To 21
		lista_dias.Add(i)
	Next
	Grid2.Add(crea_dia(lista_dias),3)

	lista_dias.Initialize	
	For i=22 To 28
			lista_dias.Add(i)
	Next
	Grid2.Add(crea_dia(lista_dias),4)

	If dias_mes >= 29 Then
		For i=29 To dias_mes
			lista_dias.Add(i)
		Next
		Grid2.Add(crea_dia(lista_dias),5)
	End If
	


End Sub

Sub crea_dia(lista As List) As B4XView
	Dim p As B4XView= xui.CreatePanel("")
	p.SetLayoutAnimated(100,0,0,100%x,36dip)
	p.LoadLayout("ver_cal_row")
	Dim where As String
	Dim rs As ResultSet
	Dim lista_tipos As List
	lista_tipos.Initialize
	For Each ddd In lista		
		where = ddd & " " & SpinnerMEs2.SelectedItem&" "&SpinnerAno2.Selecteditem
		rs=sql.ExecQuery2("select tipo2 from Dias where Dia=?",Array As String(where))
		If rs.RowCount>1 Then
			lista_tipos.add(rs.GetString("Tipo"))
		End If
	Next
	cal_dia1.Text=lista.Get(0)
	cal_tipo1.Text=lista_tipos.Get(0)
	Select cal_tipo1.Text.SubString(1)
		Case "M"
			cal_tipo1.Color=xui.Color_Red
		Case "N"
			cal_tipo1.Color=xui.Color_Magenta
		Case "R"
			cal_tipo1.Color=xui.Color_Green			
	End Select
	cal_dia2.Text=lista.Get(1)
	cal_tipo2.Text=lista_tipos.Get(1)
	cal_dia3.Text=lista.Get(2)
	cal_tipo3.Text=lista_tipos.Get(2)
	cal_dia4.Text=lista.Get(3)
	cal_tipo4.Text=lista_tipos.Get(3)
	cal_dia5.Text=lista.Get(4)
	cal_tipo5.Text=lista_tipos.Get(4)
	cal_dia6.Text=lista.Get(5)
	cal_tipo6.Text=lista_tipos.Get(5)
	cal_dia7.Text=lista.Get(6)
	cal_tipo7.Text=lista_tipos.Get(6)
	
	Return p
End Sub

Private Sub SpinnerMEs2_ItemClick (Position As Int, Value As Object)
	'VerCalendario(DateTime.Date())
End Sub

Private Sub SpinnerAno2_ItemClick (Position As Int, Value As Object)
	'VerCalendario(DateTime.Date())
End Sub