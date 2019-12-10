; dpos 1.1 Installer 
; Written by Dongwun
; Copyright 2017 Dongwun


;Compression options
  CRCCheck on
  SetCompressor /SOLID lzma

  Name "dPOS"

  ;Product information
  VIAddVersionKey ProductName "dPOS"
  VIAddVersionKey CompanyName "(주)동운시스템"
  VIAddVersionKey LegalCopyright "Copyright (c) 2017 Dongwun Systems"
  VIAddVersionKey FileDescription "dPOS Installer"
  VIAddVersionKey FileVersion "1.1"
  VIAddVersionKey ProductVersion "1.1"
  VIAddVersionKey Comments "dongwun.com"
  VIAddVersionKey InternalName "dpos@1.1.0.0.exe"
  VIProductVersion 1.1.0.0

;--------------------------------
;Include Modern UI

  !include "MUI2.nsh"
  !include "ZipDLL.nsh"
  !include "StrFunc.nsh"
  !include "InstallOptions.nsh"
  !include "LogicLib.nsh"
${StrRep}
  Var "JavaHome"
  Var CpuType
  Var dest

;--------------------------------
;General

  ;Name and file

  OutFile dPOS-1.1.exe

  ;Default installation folder
  InstallDir "$PROGRAMFILES\dPOS"

  ReserveFile "${NSISDIR}\Plugins\InstallOptions.dll"
  ReserveFile "jvm.ini"

  ;Request application privileges for Windows Vista and Windows 7
  RequestExecutionLevel admin

  ;Install types
  InstType Normal ; 1
  ;InstType Minimum ; 2
  ;InstType Full ; 3

;--------------------------------
;Interface Settings

  !define MUI_HEADERIMAGE
  !define MUI_HEADERIMAGE_RIGHT
  !define MUI_HEADERIMAGE_BITMAP header.bmp
  !define MUI_WELCOMEFINISHPAGE_BITMAP dposlogo.bmp
  !define MUI_FINISHPAGE_SHOWREADME "$INSTDIR\README.txt"
  !define MUI_FINISHPAGE_RUN $INSTDIR\start.bat
  !define MUI_FINISHPAGE_NOREBOOTSUPPORT

  !define MUI_ABORTWARNING

  !define TEMP1 $R0
  !define TEMP2 $R1

  !define MUI_ICON dpos.ico
  !define MUI_UNICON dpos.ico

  ;Show all languages, despite user's codepage
  !define MUI_LANGDLL_ALLLANGUAGES
  !define MUI_LANGDLL_ALWAYSSHOW

;Language Selection Dialog Settings

;--------------------------------
;Pages

  !insertmacro MUI_PAGE_WELCOME 
  Page custom SetChooseJVM Void "$(TEXT_JVM_PAGETITLE)"
  Page custom VerifyJRE Void ""
  !insertmacro MUI_PAGE_LICENSE $(LicenseRTF)
  !insertmacro MUI_PAGE_DIRECTORY
  !insertmacro MUI_PAGE_INSTFILES
  !insertmacro MUI_PAGE_FINISH

 ;Uninstall Page order
  !insertmacro MUI_UNPAGE_CONFIRM
  !insertmacro MUI_UNPAGE_INSTFILES

 ;License dialog


;This will make your installer start faster.

  !insertmacro MUI_RESERVEFILE_LANGDLL

  ;Install Options pages
  
;Languages
  !insertmacro MUI_LANGUAGE "Korean" ;first language is the default language
  !insertmacro MUI_LANGUAGE "English"
  
  LicenseLangString LicenseRTF ${LANG_KOREAN} "License_ko.rtf"
  LicenseLangString LicenseRTF ${LANG_ENGLISH} "License_en.rtf"
  
  LicenseData $(LicenseRTF)
 
  # KOREAN
  LangString TEXT_JVM_TITLE ${LANG_KOREAN} "자바(Java) 가상 머신"
  LangString TEXT_JVM_SUBTITLE ${LANG_KOREAN} "자바 가상 머신 경로 선택."
  LangString TEXT_JVM_PAGETITLE ${LANG_KOREAN} ": 자바 가상 머신 경로 선택"

  LangString TEXT_PATH_JRE ${LANG_KOREAN} 'J2SE 7.0 이상의 자바 실행 환경(JRE)이 설치된 경로를 선택하세요. \
                                            (C:\Program Files\Java\jre7 처럼 bin/java.exe를 가지고 있는 폴더) \
											64비트 환경이라면 반드시 64비트 JRE를 선택해야 합니다:'
  LangString TEXT_REMOVE ${LANG_KOREAN} "dPOS 디렉토리에 있는 모든 파일을 삭제하겠습니까? (보관해야할 \
											개인 파일이 있다면 No를 클릭하세요)"
  # ENGLISH
  LangString TEXT_JVM_TITLE ${LANG_ENGLISH} "Java Virtual Machine"
  LangString TEXT_JVM_SUBTITLE ${LANG_ENGLISH} "Java Virtual Machine path selection."
  LangString TEXT_JVM_PAGETITLE ${LANG_ENGLISH} ": Java Virtual Machine path selection"

  LangString TEXT_PATH_JRE ${LANG_ENGLISH} 'Please select the path of a J2SE 5.0 or later JRE installed on your system. \
											Note that if you have a 64-bit operating system, you must specify a 64-bit JRE:'
  LangString TEXT_REMOVE ${LANG_ENGLISH} "Remove all files in your dPOS directory ? (If you have anything  \
											you created that you want to keep, click No)"
##################INSTALLER SECTIONS##################


Section "Core" SecPOSCore

  SectionIn 1 RO

  Call checkJvm

  SetOutPath $INSTDIR
  File configure.bat
  File Migrate.bat
  File newtext.txt
  File README.txt
  File README_Locales.txt
  File ResetSeq.bat
  File start.bat
  File dpos_splash_dark.png
  File dpos.ico
  File dpos.jar
  SetOutPath $INSTDIR\lib
  File /r lib\*.*
  SetOutPath $INSTDIR\licensing
  File /r licensing\*.*
  SetOutPath $INSTDIR\locales
  File /r locales\*.*
  SetOutPath $INSTDIR\reports
  File /r reports\*.*
  SetOutPath $INSTDIR
  
  Call findJavaPath
  Pop $2

  IfSilent +2 0
 !insertmacro INSTALLOPTIONS_READ $2 "jvm.ini" "Field 2" "State"

  StrCpy "$JavaHome" $2
  Call findJVMPath
  Pop $2

  DetailPrint "Using Jvm: $2"

SectionEnd


Section "Start Menu Items" SecMenu

  SectionIn 1

  !insertmacro INSTALLOPTIONS_READ $2 "jvm.ini" "Field 2" "State"

  CreateShortCut "$DESKTOP\dPOS.lnk" "$INSTDIR\start.bat" \
				 "" "$INSTDIR\dPOS.ico" 0 SW_SHOWNORMAL

SectionEnd


Section -post
    
  WriteUninstaller "$INSTDIR\Uninstall.exe"

SectionEnd

  LangString DESC_Section3 ${LANG_ENGLISH} "dPOS를 설치합니다."
  LangString DESC_Section6 ${LANG_ENGLISH} "dPOS 시작 메뉴를 생성합니다."
  
  LangString DESC_Section3 ${LANG_KOREAN} "Install the dPOS core."
  LangString DESC_Section6 ${LANG_KOREAN} "Create a Start Menu for dPOS."
  
 !insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
  !insertmacro MUI_DESCRIPTION_TEXT ${SecPOSCore} $(DESC_Section3)
  !insertmacro MUI_DESCRIPTION_TEXT ${SecMenu} $(DESC_Section6)
!insertmacro MUI_FUNCTION_DESCRIPTION_END


;--------------------------------
;Installer Functions

Function .onInit

  ;Reset install dir for 64-bit
  ExpandEnvStrings $0 "%PROGRAMW6432%"
  StrCmp $0 "%PROGRAMW6432%" +2 0
  StrCpy $INSTDIR "$0\dPOS"

  ;Extract Install Options INI Files
  !insertmacro INSTALLOPTIONS_EXTRACT "jvm.ini"

FunctionEnd

Function Void
FunctionEnd

Function SetChooseJVM
  !insertmacro MUI_HEADER_TEXT "$(TEXT_JVM_TITLE)" "$(TEXT_JVM_SUBTITLE)"

  Call findJavaPath
  pop $R0

  ${If} $R0 == ""
  StrCpy $R0 "Please select a valid JRE folder..."
  ${EndIf}

  !insertmacro INSTALLOPTIONS_WRITE "jvm.ini" "Field 2" "State" $R0
  !insertmacro INSTALLOPTIONS_WRITE "jvm.ini" "Field 1" "Text" "$(TEXT_PATH_JRE)"

  !insertmacro INSTALLOPTIONS_DISPLAY "jvm.ini"
FunctionEnd

Function VerifyJRE
	!insertmacro INSTALLOPTIONS_READ $dest "jvm.ini" "Field 2" "State"

	IfFileExists $dest\lib\ext\*.* dirLibExists dirLibNotExists
	dirLibExists:
	Goto okJRE
	dirLibNotExists:
	MessageBox MB_OK "Choose a valid JRE Folder !"
	Call SetChooseJVM

	IfFileExists $dest\bin\*.* dirBinExists dirBinNotExists
	dirBinExists:
	Goto okJRE
	dirBinNotExists:
	MessageBox MB_OK "Choose a valid JRE Folder !"
	Call SetChooseJVM

	okJRE:
FunctionEnd

Function FindCpuType

  ClearErrors
  ; Default CPU is always x86
  StrCpy $1 "x86"
  ExpandEnvStrings $0 "%PROCESSOR_ARCHITEW6432%"
  StrCmp $0 "%PROCESSOR_ARCHITEW6432%" +5 0
  StrCmp $0 "IA64" 0 +3
  StrCpy $1 "i64"
  Goto FoundCpu
  StrCpy $1 "x64"

FoundCpu:
  ; Put the result in the stack
  Push $1

FunctionEnd


; =====================
; FindJavaPath Function
; =====================
;
; Find the JAVA_HOME used on the system, and put the result on the top of the
; stack
; Will return an empty string if the path cannot be determined

Function findJavaPath

  ;ClearErrors

  ;ReadEnvStr $1 JAVA_HOME

  ;IfErrors 0 FoundJDK

  ClearErrors

  ; Use the 64-bit registry on 64-bit machines
  ExpandEnvStrings $0 "%PROGRAMW6432%"
  StrCmp $0 "%PROGRAMW6432%" +2 0
  SetRegView 64

  ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
  StrCpy $2 "1.6"
  ReadRegStr $1 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$2" "JavaHome"
  ReadRegStr $3 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$2" "RuntimeLib"

  ;FoundJDK:

  IfErrors 0 NoErrors
  StrCpy $1 ""

NoErrors:

  ClearErrors

  ; Put the result in the stack
  Push $1

FunctionEnd

; ====================
; FindJVMPath Function
; ====================
;
; Find the full JVM path, and put the result on top of the stack
; Argument: JVM base path (result of findJavaPath)
; Will return an empty string if the path cannot be determined
Function findJVMPath

  ClearErrors

  ;Step one: Is this a JRE path (Program Files\Java\XXX)
  StrCpy $1 "$JavaHome"

  StrCpy $2 "$1\bin\hotspot\jvm.dll"
  IfFileExists "$2" FoundJvmDll
  StrCpy $2 "$1\bin\server\jvm.dll"
  IfFileExists "$2" FoundJvmDll
  StrCpy $2 "$1\bin\client\jvm.dll"
  IfFileExists "$2" FoundJvmDll
  StrCpy $2 "$1\bin\classic\jvm.dll"
  IfFileExists "$2" FoundJvmDll

  ;Step two: Is this a JDK path (Program Files\XXX\jre)
  StrCpy $1 "$JavaHome\jre"

  StrCpy $2 "$1\bin\hotspot\jvm.dll"
  IfFileExists "$2" FoundJvmDll
  StrCpy $2 "$1\bin\server\jvm.dll"
  IfFileExists "$2" FoundJvmDll
  StrCpy $2 "$1\bin\client\jvm.dll"
  IfFileExists "$2" FoundJvmDll
  StrCpy $2 "$1\bin\classic\jvm.dll"
  IfFileExists "$2" FoundJvmDll

  ClearErrors
  ;Step tree: Read defaults from registry

  ReadRegStr $1 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
  ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$1" "RuntimeLib"

  IfErrors 0 FoundJvmDll
  StrCpy $2 ""

  FoundJvmDll:
  ClearErrors

  ; Put the result in the stack
  Push $2

FunctionEnd

; ====================
; CheckJvm Function
; ====================

Function checkJvm

  !insertmacro INSTALLOPTIONS_READ $3 "jvm.ini" "Field 2" "State"
  IfFileExists "$3\bin\java.exe" NoErrors1
  MessageBox MB_OK|MB_ICONSTOP "자바 가상 머신을 찾을 수 없습니다 :$\r$\n$3"
  Quit
NoErrors1:
  StrCpy "$JavaHome" $3
  Call findJVMPath
  Pop $4
  StrCmp $4 "" 0 NoErrors2
  MessageBox MB_OK|MB_ICONSTOP "자바 가상 머신을 찾을 수 없습니다:$\r$\n$3"
  Quit
NoErrors2:

FunctionEnd


Function xmlEscape
  Pop $0
  ${StrRep} $0 $0 "&" "&amp;"
  ${StrRep} $0 $0 "$\"" "&quot;"
  ${StrRep} $0 $0 "<" "&lt;"
  ${StrRep} $0 $0 ">" "&gt;"
  Push $0
FunctionEnd

; Copy specified file contents to $R9
Function copyFile

  ClearErrors
  Pop $0
  FileOpen $1 $0 r

 NoError:

  FileRead $1 $2
  IfErrors EOF 0
  FileWrite $R9 $2

  IfErrors 0 NoError

 EOF:

  FileClose $1
  ClearErrors

FunctionEnd


;--------------------------------
;Uninstaller Section

Section Uninstall

  Delete "$INSTDIR\uninstall.dat"
  Delete "$INSTDIR\uninstall.exe"

  Delete "$INSTDIR\configure.bat"
  Delete "$INSTDIR\Migrate.bat"
  Delete "$INSTDIR\newtext.txt"
  Delete "$INSTDIR\README.txt"
  Delete "$INSTDIR\README_Locales.txt"
  Delete "$INSTDIR\ResetSeq.bat"
  Delete "$INSTDIR\start.bat"
  Delete "$INSTDIR\dpos_splash_dark.png"
  Delete "$INSTDIR\dpos.ico"
  Delete "$INSTDIR\dpos.jar"
  RMDir /r "$INSTDIR\reports"
  RMDir /r "$INSTDIR\locales"
  RMDir /r "$INSTDIR\licensing"
  RMDir /r "$INSTDIR\lib"
  RMDir "$INSTDIR"
  Delete "$DESKTOP\dPOS.lnk"
SectionEnd

;--------------------------------
;Uninstaller Functions

Function un.onInit

  !insertmacro MUI_UNGETLANGUAGE

FunctionEnd