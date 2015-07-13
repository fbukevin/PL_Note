#Package

A.java
```
package Any;
class A{
	main(){
		object B
	}
}
```

B.java
```
package Any;
class B{
	...
}
```

此時編譯：

`$ javac -classpath .:*.jar A.java B.java`

然後執行：

`$ java -classpath .:*.jar A`

會發生：

`錯誤: 找不到或無法載入主要類別 A`

解決方法(似乎)是拿掉 A 和 B 的 package 即可
但(應該)不是理想解法，因為假如我就是要 package?

編譯完以後，要建立一個 Any 資料夾，把 A.class 和 B.class (和其他 \*.jar) 放到 Any
然後再 Any 外面用 `java -classpath .:Any:Any/*.jar A` 來執行

Example
```
javacp .:thesis2:thesis2/phidget21.jar:thesis2/org.eclipse.paho.client.mqttv3-1.0.2.jar thesis2/SpatialExample
```
