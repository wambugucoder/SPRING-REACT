@echo off

set /P inputPlainText=Input the text you want encrypted:

if %inputPlainText% =="" goto failed

echo Encrypting Your Password
cd  C:\Users\Jos Wambugu\Downloads\Programs
call  java -cp jasypt-1.9.3.jar org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI input=%inputPlainText% password=Big_Black_Mamba algorithm=PBEWITHHMACSHA512ANDAES_256 ivGeneratorClassName=org.jasypt.iv.RandomIvGenerator
timeout /t 30

:failed
echo Line is Empty
pause
