#Makefile

自動化編譯的演化流程：GNU Make -> Autotools -> CMake

參考：Wen Liao - GNU Make, Autotools, CMake 簡介

## GNU Make
就是一般我們看到的 Makefile 和可以只輸入 `make` 和 `make install` 和 `make clean` 等來達到自動化編譯的程式

```
target: <prerequisites> ...
<TAB>gcc -o hello hello.c
<TAB><Other recipe>
...
```

target 除了是在 Makefile 中作為 block(function-like) 名稱，他也是我們在命令列輸入 make 可以直接執行的名稱，例如 `make clean` 會對應到：
```
clean:
	rm files
```


recipe 其實就是寫 shell script

recipe 的一些寫法：
* echo Test	=> 一般寫 shell script 的方法，即印出 "Test"
* @echo Test	=> @ 開頭的話，這行會執行，但不會顯示在 stdout

* 內建變數
	* @echo $@	=> 取 target 名稱
	* @echo $^	=> 取所有 prerequisite 名稱
	* @echo $<   => 取第一個 prerequisite 名稱
	* @echo $(var) => 取變數 var 的值
	* @echo $?	=> 取得已被更新之 prerequisites 的值

* 簡單的變數設定
	* var = val		=> 單純設定值
	* var := val		=> 不可變，若 var 已被賦值，第二次賦值無效
	* var ?= val		=>	預設，設定如果沒有另外賦值，預設就是什麼
	* var += val		=>	串接

* 條件判斷

```
if()
<TAB>...
else
<TAB>...
endif
```

$(LOGNAME)	  	=> 取得環境變數


* 函數：`$(name paras)`
	* $(warning 訊息)  	=> 印出 `Makefile:(line no.): (message)`
	* $(error 訊息)
	* $(substr from, to, 處理敘述)
	* $(patsubstr pattern, 替換成, 處理敘述)
	* $(shell 命令)


###.PHONY

make 就算沒有 Makefile，也可以執行編譯和輸出，最常見的就是有個 C 檔案 a.c，可以直接用 `make a` 產生 a.out

那如果我們建立的一個檔案名稱是：clean，並且有個 target 是 clean，此時 make 就會以為我們是要編譯產生 clean 這個檔案，為了避免這個錯誤，因此我們可以宣告 clean 這個 target 是與檔案無關的 target name (純命令名稱的概念)

最常見的範例：`.PHONY: clean install`

## Autotools

這個工具的目的是要解決：
* 跨平台：例如 memset 和 bzero、路徑或檔案不同、system call 不同
* 同平台但 library 版本不同或 prototype 不同
* 相依性問題

在 make 前，很多時候需要先 ./configure 後面接一大串設定，然後才 make，這個工具可以幫你自動化產生這些步驟的：

1. configrue.ac 和 Makefile.am 是兩個你主要要寫的檔案
2. 寫完以後呼叫工具產生 configure, Makefile.in 和 config.in
3. 接著你就可以跑 ./configure
4. 接著會產生 Makefile 和 config.h
5. 最後你在用 make 和 make install 來編譯和安裝

(比較好寫的 config 和 makefile? 可以幫你產生 makefile 和 configure?)

autogen.sh 可以幫你呼叫相關工具 (自己寫？)

### 情境資料夾
```
.
|--- autogen.sh
|--- configure.ac
|--- Makefile.am
|--- include
|		|___ liba.h
|		|___ libb.h
|
|--- libs
|		|___ liba.c
|		|___ libb.c
|		|___ Makefile.am
|
|___ src
		|___ test.c
		|___ Makefile.am
```

### configure.ac
```
AC_PREREQ([2.68])	# 版本要求
AC_INIT([Test_Autotools], [0], [test])	# 套件資訊
AM_INIT_AUTOMAKE([foreign -Wall -Werror])	# 給 automake 資訊
AC_CONFIG_HEADERS([config.h])	# 用 ./configure 後要產生的 config 檔案
AC_PROG_RANLIB	# 使用靜態函式庫
AC_CONFIG_FILES([Makefile src/Makefile libs/Makefile])	# Makefile.am 的路徑
AC_PROG_CC	# 搜尋 cc 編譯器
AC_OUTPUT		# 結束 config, 開始產生相關檔案
```

### Makefile.am

configure.ac 可以只有一份，但是 Makefile 必續在不同資料夾中個別撰寫，例如我們在 configure.ac 中設定了 Makefile 的路徑有三個，所以就分別要在 src, libs 和 root 都放一個 Makefile.am

* root

```
SUBDIRS = libs src
```

* libs

```
AM_CFLAGS = -I ../include		# 指定 include 路徑
lib_LIBRARIES = liba.a libb.a	# 產生 liba.a 和 libb.a, *(註1)

liba_a_SOURCES = liba.c liba.h	# 設定 liba.a 編譯時相依檔案
libb_a_SOURCES = libb.c libb.h	# 設定 libb.a 編譯時相依檔案

include_HEADERS = ../include/libs.h ../include/libb.h	# 指定要安裝到 $(prefix)/include 的檔案
```

註1：lib_LIBRARIES 的 lib_ 表示安裝時要放把 liba.a 和 libb.a在 $(prefix)/lib 中，預設 prefix=/usr/local

* src

```
LDADD = ../libs/libs.a ../libs/libb.a	# 指定連結哪些 library
AM_CFLAGS = -I ../include		# 指定 include 路徑
bin_PROGRAMS = test				# 指定安裝到 $(prefix)/bin 的檔案
test_SOURCES = test.c			# 設定 test 編譯時相依檔案 
```

所以看起來，Autotools 就是代替安裝者寫好了預設的 ./configure 的很多後面的設定參數，並且幫開發或發佈者自動產生 Makefile 的工具，最常見 ./configure 的設定參數是 `--prefix=(path)`

接著就可以依照以下流程完成安裝：./configure -> make -> make install 

## CMake

### 目的

將 build 和 source code 分開，並且支援 cache 加快編譯速度

取代 autotools

### 流程

1. 寫 CMakeLists.txt
2. 輸入 cmake，這會產生平台相依的編譯環境檔案，例如 Makefile 等等
3. 輸入 make
4. 輸入 cmake install，就是 make install 的工作


### 情境資料夾
```
.
|--- CMakeLists.txt
|--- include
|		|___ liba.h
|		|___ libb.h
|		|___ CMakeLists.txt
|
|--- libs
|		|___ liba.c
|		|___ libb.c
|		|___ CMakeLists.txt
|
|___ src
		|___ test.c
		|___ CMakeLists.txt
```

### CMakeLists.txt

* root

```
cmake_minimum_requireed(VERSION 2.8)		# 設定 cmake 最低版本需求
project(test_name)		# 設定你 project name

# 設定變數
set(SRC_DIR src)
set(LIB_DIR libs)
set(INC_DIR include)

set(CMAKE_C_FLAGS "-Wall -Werror")	# 設定編譯參數
include_directories(${INC_DIR})		# 指定 include 目錄

# 告訴 cmake 要去下列的目錄編譯
add_subdirectory(${SRC_DIR})
add_subdirectory(${LIB_DIR})
add_subdirectory(${INC_DIR})
```

* libs

```
# 設定變數，指定 library 相依於哪個檔案
set(liba_SRCS liba.c)
set(libb_SRCS libb.c)

# 指定編譯型式為 shared library
add_library(a SHARED ${liba_SRCS})
add_library(b SHARED ${libb_SRCS})

install(TARGETS a b LIBRARY DESTINATION lib)	# 安裝格式：install(TARGETS 函式庫名 LIBRARY DESTINATION 安裝路徑)
```

* src

```
set(test_SRCS test.c)
add_executable(${PROJECT_NAME} ${test_SRCS})	# 產生執行檔
target_link_libraries(${PROJECT_NAME} a b)	# 指定連結的函式庫
install(TARGET ${PROJECT_NAME} DESTINATION bin)	# 安裝格式：install(TARGET 執行檔名 DESTINATION 安裝路徑)
```

* include

```
install(FILES liba.h libb.h DESTINATION include)	# 安裝格式：install(FILES 標頭檔名 DESTINATION 安裝路徑)
```

使用 CMake，你連 configure 都不用寫了！