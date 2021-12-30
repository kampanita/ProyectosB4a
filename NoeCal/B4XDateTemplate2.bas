B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Class
Version=7.3
@EndOfDesignText@
Sub Class_Globals
	Private xui As XUI
	Private month, year As Int
	Private boxW, boxH As Float
	Private vCorrection As Float
	Private tempSelectedDay As Int
	Private dayOfWeekOffset As Int
	Private daysInMonth As Int
	Private DaysPaneBg As B4XView
	Private DaysPaneFg As B4XView
	Private cvs As B4XCanvas
	Private cvsBackground As B4XCanvas
	Private selectedDate As Long
	Private PreviousSelectedDate As Long
	Private selectedYear, selectedMonth, selectedDay As Int
	Public HighlightedColor As Int = 0xFF001BBD
	Public SelectedColor As Int = 0xFF0BA29B
	Public DaysInMonthColor As Int = xui.Color_White
	Public DaysInWeekColor As Int = xui.Color_Gray
	Private cvsDays As B4XCanvas
	Private DaysTitlesPane As B4XView
	Public FirstDay As Int = 0
	Public MinYear = 1970, MaxYear = 2030 As Int
	Public btnMonthLeft As B4XView
	Public btnMonthRight As B4XView
	Public btnYearLeft As B4XView
	Public btnYearRight As B4XView
	Public lblMonth As B4XView
	Public lblYear As B4XView
	Private pnlDialog As B4XView
	Private months As List
	Private mDialog As B4XDialog
	Public CloseOnSelection As Boolean = True
	Public DaysOfWeekNames As List
	Dim sql As SQL
End Sub

Public Sub Initialize
	sql.Initialize(File.DirInternal, "NoeCal.db", False)
	pnlDialog = xui.CreatePanel("")
	pnlDialog.SetLayoutAnimated(0, 0, 0, 320dip,400dip)
	pnlDialog.LoadLayout("DateTemplate2")
	pnlDialog.Tag = Me
	month = DateTime.GetMonth(DateTime.Now)
	year = DateTime.GetYear(DateTime.Now)
	months = DateUtils.GetMonthsNames
	selectedDate = DateTime.Now
	setDate(selectedDate)
	cvs.Initialize(DaysPaneFg)
	cvsBackground.Initialize(DaysPaneBg)
	boxW = cvs.TargetRect.Width / 7
	boxH = cvs.TargetRect.Height / 6
	vCorrection = 7dip
	cvsDays.Initialize(DaysTitlesPane)
	#if B4J
	Dim p As Pane = DaysPaneFg
	Private fx As JFX
	p.MouseCursor = fx.Cursors.HAND
	#End If
	DaysOfWeekNames.Initialize
	DaysOfWeekNames.AddAll(DateUtils.GetDaysNames)
End Sub

Private Sub DrawDays
	
	Dim where As String
	Dim tipo2 As String
	Dim rs As ResultSet
	Dim x As Int
	Dim y As Int
	Dim y2 As Int
	Dim color As Int
	
	lblMonth.Text = months.Get(month - 1)
	lblYear.Text = year
	SetYearsButtonState
	cvs.ClearRect(cvs.TargetRect)
	cvsBackground.ClearRect(cvsBackground.TargetRect)
	Dim firstDayOfMonth As Long = DateUtils.setDate(year, month, 1) - 1
	dayOfWeekOffset = (7 + DateTime.GetDayOfWeek(firstDayOfMonth) - FirstDay) Mod 7
	daysInMonth = DateUtils.NumberOfDaysInMonth(month, year)
	
'	Comento porque no necesito ningún día seleccionado.
'	If year = selectedYear And month = selectedMonth Then
'		'draw the selected box
'		DrawBox(cvs, SelectedColor, (selectedDay - 1 + dayOfWeekOffset) Mod 7, _
'			(selectedDay - 1 + dayOfWeekOffset) / 7)
'	End If

	Dim daysFont As B4XFont = xui.CreateDefaultBoldFont(14)
	Dim daysFont2 As B4XFont = xui.CreateDefaultBoldFont(10)

	For day = 1 To daysInMonth
		Dim row As Int = (day - 1 + dayOfWeekOffset) / 7
		where =datatotexT(day)& " " & lblMonth.text&" "&lblYear.text
		
		rs=sql.ExecQuery2("select * from Dias where Dia=?",Array As String(where))
		
		If rs.RowCount>=1 Then
			rs.Position=0
			tipo2=rs.GetString("Tipo2")
			color=xui.Color_Yellow
			
			Select tipo2.SubString2(0,1)
				Case "M"
					color=xui.Color_Red
				Case "T"
					color=xui.Color_Magenta
				Case "N"
					color=xui.Color_Green
				Case "R"
					color=xui.Color_cyan
			End Select
			
			x=(((dayOfWeekOffset + day - 1) Mod 7) + 0.5) * boxW
			y=(row + 0.3)* boxH + vCorrection
			y2 = (row + 0.7)* boxH + vCorrection
			
			'Pinto el dia que tie
			cvs.DrawText(day, x, y, daysFont, xui.color_white , "CENTER")
			
			'Pinto el tipo
			cvs.DrawText(tipo2, x,	y2, daysFont2, xui.color_white , "CENTER")
			
			'Pinto el fondo
			x=(day - 1 + dayOfWeekOffset) Mod 7
			y2=( day - 1 + dayOfWeekOffset) / 7
			
			DrawBox(cvsBackground, color, x, y2)
		Else
			'Pinto un dia "normal"
			cvs.DrawText(day, (((dayOfWeekOffset + day - 1) Mod 7) + 0.5) * boxW, _
				(row + 0.5)* boxH + vCorrection, daysFont, xui.color_white , "CENTER")
		End If
		
	Next
	cvsBackground.Invalidate
	cvs.Invalidate
End Sub

Private Sub SetYearsButtonState
	btnYearLeft.Enabled = year > MinYear
	btnYearRight.Enabled = year < MaxYear
End Sub

Sub datatotexT(datos As Int) As String
	Dim data As String
	If datos<9 Then
		data="0"&datos
	Else 
		data =datos
	End If
	'Log(DateTime.Date(datos))
	Return data 
End Sub
Private Sub DrawBox(c As B4XCanvas, clr As Int, x As Int, y As Int)
	Dim r As B4XRect
	r.Initialize(x * boxW, y * boxH, x * boxW + boxW,  y * boxH + boxH)
	c.DrawRect(r, clr, True, 1dip)
End Sub

'Gets or sets the selected date
Public Sub getDate As Long
	Return selectedDate
End Sub

Public Sub setDate(date As Long)
	'The layout is not loaded immediately so we need to make sure that the layout was loaded.
	If lblYear.IsInitialized = False Then
		selectedDate = date
		Return 'the date will be set after the layout is loaded
	End If
	year = DateTime.GetYear(date)
	month = DateTime.GetMonth(date)
	SelectDay(DateTime.GetDayOfMonth(date))
	lblYear.Text = year
	lblMonth.Text = months.Get(month - 1)
End Sub

Private Sub SelectDay(day As Int)
	selectedDate = DateUtils.setDate(year, month, day)
	selectedDay = day
	selectedMonth = month
	selectedYear = year
End Sub


Private Sub HandleMouse(x As Double, y As Double, move As Boolean)
	Dim boxX = x / boxW, boxY =  y / boxH As Int
	Dim newSelectedDay As Int = boxY * 7 + boxX + 1 - dayOfWeekOffset
	Dim validDay As Boolean = newSelectedDay > 0 And newSelectedDay <= daysInMonth
	If move Then
		If newSelectedDay = tempSelectedDay Then Return
		cvsBackground.ClearRect(cvsBackground.TargetRect)
		tempSelectedDay = newSelectedDay
		If validDay Then
			DrawBox(cvsBackground, HighlightedColor, boxX, boxY)
		End If
	Else
		cvsBackground.ClearRect(cvsBackground.TargetRect)
		If validDay Then
			SelectDay(newSelectedDay)
			If CloseOnSelection Then
				Hide
			'Else
			'	DrawDays
			End If
		End If
		DrawDays
	End If
	
	cvsBackground.Invalidate
End Sub

Private Sub Hide
	mDialog.Close(xui.DialogResponse_Positive)
End Sub


Private Sub btnYear_Click
	Dim btn As B4XView = Sender
	year = year + btn.Tag
	DrawDays
End Sub

Private Sub btnMonth_Click
	Dim btn As B4XView = Sender
	Dim m As Int = 12 + month - 1 + btn.Tag
	month = (m Mod 12) + 1
	DrawDays
End Sub

Private Sub DaysPaneFg_Touch (Action As Int, X As Float, Y As Float)
	Dim p As B4XView = DaysPaneFg
	HandleMouse(X, Y, Action <> p.TOUCH_ACTION_UP)
End Sub

Public Sub GetPanel (Dialog As B4XDialog) As B4XView
	Return pnlDialog
End Sub

Private Sub Show (Dialog As B4XDialog)
	Dim days As List = DaysOfWeekNames
	Dim daysFont As B4XFont = xui.CreateDefaultBoldFont(14)
	cvsDays.ClearRect(cvsDays.TargetRect)
	For i = FirstDay To FirstDay + 7 - 1
		Dim d As String = days.Get(i Mod 7)
		If d.Length > 2 Then d = d.SubString2(0, 2)
		cvsDays.DrawText(d, (i - FirstDay + 0.5) * boxW, 20dip, daysFont, DaysInWeekColor, "CENTER")
	Next
	cvsDays.Invalidate
	mDialog = Dialog
	DrawDays
	PreviousSelectedDate = selectedDate
	Sleep(0)
	SetYearsButtonState
End Sub

Private Sub DialogClosed(Result As Int) 'ignore
	If Result <> xui.DialogResponse_Positive Then
		setDate(PreviousSelectedDate)
	End If
End Sub



