def call(Map parameters = [:], body) {
	def disk = null
	try {
		def result = bat(script: '''
@echo off
setlocal EnableDelayedExpansion
set exitcode=0
for %%i in (Z,Y,X,W,V,U,T,S,R,Q,P,O,N,M,L,K,J,I,H,G,F,E,D,C,B,A) do (
	set "drive=%%i:"
	subst !drive! . > nul
	if !errorlevel! == 0 (
		echo !drive!
		goto :exit
	)
)
echo ERROR: No free drive letter found.
set exitcode=1
set drive=
:exit
endlocal & exit /b %exitcode%
		''', returnStdout: true).trim()
		disk = result.split('\n').last()
		dir("$disk\\") {
			body()
		}
	}
	finally {
		if (disk != null)
		{
			bat "subst $disk /d"
		}
	}
}