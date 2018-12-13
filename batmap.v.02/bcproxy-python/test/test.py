# encoding: UTF-8
import re
import threading
 
#-*- coding: utf-8 -*-
from socket import *
import time
from time import ctime
import _thread

mapperHandler = None

HOST=''
PORT=12345
BUFSIZ=1024
ADDR=(HOST, PORT)
sock=socket(AF_INET, SOCK_STREAM)
sock.bind(ADDR)
## 最多支持的连接数
sock.listen(1)

def listen_mapper():
	global mapperHandler
	while True:
		## 仅支持一个连接！
	    print('waiting for connection')
	    tcpClientSock, addr=sock.accept()
	    print('connect from ', addr)
	    mapperHandler = tcpClientSock

		#maybe need except solving
	    while True:
	            data=tcpClientSock.recv(BUFSIZ)


		# mapperHandler = None
		# tcpClientSock.close()
	sock.close()

# true start
_thread.start_new_thread(listen_mapper, ())

while True:
	global mapperHandler
	if mapperHandler != None :
		print('fire')
		mapperHandler.send(('[%s]' %(ctime())).encode('utf-8'))
	else :
		print('nonono')

	print('waiting')
	time.sleep(4)

# 将正则表达式编译成Pattern对象
pattern = re.compile(r'.*?Loc:.*?\[(\d+,\d+)\] in .*? \b((?:Lucentium|Desolathya|Rothikgen|Laenor|Furnachia)).*?',flags=re.S)
# pattern = re.compile(r'(?:Lucentium|Desolathya|Rothikgen|Laenor|Furnachia)',flags=re.S)
# pattern = re.compile(r'.*\b((?:one|two|hello))\b.*',flags=re.M)
 
# 使用Pattern匹配文本，获得匹配结果，无法匹配时将返回None
a='''
<---------^--------->  Loc:    Broad Trail [402,155] in northern Lucentium a
|                   |  Exits:  north, south, east, west, northeast, northwest, 
|                   |          southeast, southwest
|        hh,        |  
| 
'''
b = "->  Loc:    Broad Trail [402,155] in northern two Lucentium"

match = pattern.match(a)
if match:
    print('fire') #'fire'
    # 使用Match获得分组信息
    print(match.group(1),match.group(2))

print( '>>>>>>>')

