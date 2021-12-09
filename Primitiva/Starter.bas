B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Service
Version=9.9
@EndOfDesignText@
#Region  Service Attributes 
	#StartAtBoot: False
	#ExcludeFromLibrary: True
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.

End Sub

Sub Service_Create
	'This is the program entry point.
	'This is a good place to load resources that are not specific to a single activity.
	'File.Copy(File.DirAssets, "Primi.db",File.DirInternal,"Primi.db" )
	'File.Delete(File.DirInternal,"primi.db")
	'File.Copy(File.DirAssets, "primi.db",File.DirInternal,"primi.db" )
	If Not (File.Exists(File.DirInternal,"primi.db") )Then
		File.Copy(File.DirAssets, "primi.db",File.DirInternal,"primi.db" )
	End If
	
	Dim locale As JavaObject
	locale.InitializeNewInstance("java.util.Locale", Array("es"))
	Dim jo As JavaObject
	jo.InitializeStatic("java.util.Locale").RunMethod("setDefault", Array(locale))
	
'	ActivateSSL
End Sub

Sub Service_Start (StartingIntent As Intent)
	Service.StopAutomaticForeground 'Starter service can start in the foreground state in some edge cases.
End Sub

Sub Service_TaskRemoved
	'This event will be raised when the user removes the app from the recent apps list.
End Sub

'Return true to allow the OS default exceptions handler to handle the uncaught exception.
Sub Application_Error (Error As Exception, StackTrace As String) As Boolean
	Return True
End Sub

Sub Service_Destroy

End Sub
'Sub ActivateSSL
'
'	Dim javaobjectContext                                                       As JavaObject
'	Dim javaobjectInstance                                                      As JavaObject
'	Dim objectListener                                                          As Object
'	Dim phoneInstance                                                           As Phone
'
'	Try
'		Select Case phoneInstance.SdkVersion
'			Case 16, 17, 18, 19, 20 ' Android 4.1 - 4.4
'			Case Else
'				Return
'		End Select
'		javaobjectInstance.InitializeStatic ("com.google.android.gms.security.ProviderInstaller")
'		javaobjectContext.InitializeContext
'		DisableStrictMode
'		objectListener = javaobjectInstance.CreateEventFromUI ("com.google.android.gms.security.ProviderInstaller.ProviderInstallListener", "objectListener", Null)
'		javaobjectInstance.RunMethod ("installIfNeededAsync", Array (javaobjectContext, objectListener))
'		Wait For objectListener_Event (stringMethodName As String, objectArguments () As Object)
'		' If stringMethodName = "onProviderInstalled" Then [ Provider installed successfully ] Else [ Error installing provider (objectArguments (0)) ]
'	Catch
'		Log("Application_Error")
'	End Try
'
'End Sub
'
'Sub DisableStrictMode
'
'	Dim javaobjectInstance                                                      As JavaObject
'	Dim javaobjectPolicy                                                        As JavaObject
'	Dim javaobjectStrictMode                                                    As JavaObject
'
'	Try
'		javaobjectInstance.InitializeStatic ("android.os.Build.VERSION")
'		If javaobjectInstance.GetField ("SDK_INT") > 9 Then
'			javaobjectPolicy = javaobjectPolicy.InitializeNewInstance ("android.os.StrictMode.ThreadPolicy.Builder", Null)
'			javaobjectPolicy = javaobjectPolicy.RunMethodJO ("permitAll", Null). RunMethodJO ("build", Null)
'			javaobjectStrictMode.InitializeStatic ("android.os.StrictMode"). RunMethod ("setThreadPolicy", Array (javaobjectPolicy))
'		End If
'	Catch
'		Log("Application_Error")
'	End Try
'
'End Sub