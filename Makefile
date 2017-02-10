clean-unity:
	git checkout app/src/main/res/
	git checkout app/src/main/AndroidManifest.xml
	rm app/build.gradle.NEW
	rm app/src/main/java/es/udc/apm/museos/UnityPlayerNativeActivity.java
	rm app/src/main/java/es/udc/apm/museos/UnityPlayerProxyActivity.java
	rm app/src/main/java/es/udc/apm/museos/UnityPlayerActivity.java
