#!/usr/bin/python
# -*- coding: UTF-8 –*- 
# This is a simple BatMUD protocol parser, written using only the default python
# library. If you want to make a suggestion or fix something you can contact-me
# at ilorn.mc_at_gmail.com
# Distributed over IDC(I Don't Care) license
import sys
import colortrans
from socket import *
from flask import Flask
from flask_sockets import Sockets
import random
import time
import thread
import re

S_TEXT = 0
S_ESC = 1
S_TAG_OPEN = 2
S_OPEN_CODE = 3
S_TAG_CLOSE = 4
S_CLOSE_CODE = 5
S_AFTER_TEN = 6
S_IAC = 7

############################ start mapper socket
mapperHandler = None

MAPPER_ADDRESS="localhost"
MAPPER_PORT=10000

HOST=''
BUFSIZ=1024
ADDR=(HOST, MAPPER_PORT)
sock=socket(AF_INET, SOCK_STREAM)
sock.bind(ADDR)
## 最多支持的连接数
sock.listen(1)

def listen_mapper():
    global mapperHandler
    while True:
        ## 仅支持一个连接！
        print('[mapper] waiting ...')
        tcpClientSock, addr=sock.accept()
        print('[mapper] connect from ', addr)
        mapperHandler = tcpClientSock

        while True:
            try:
                data=tcpClientSock.recv(BUFSIZ)
            except:
                tcpClientSock.close()
                break

        mapperHandler = None
        tcpClientSock.close()
    sock.close()

# true start
thread.start_new_thread(listen_mapper, ()) 

print "mapper listen at {}".format( MAPPER_PORT)
############################ end of mapper

############################ start realm map websocket listen
ws_handlers =[]
app = Flask(__name__)
sockets = Sockets(app)
websocket_port=10001

# 地址解析
pattern=re.compile(r'.*?Loc:.*?\[(\d+,\d+)\] in .*? \b((?:Lucentium|Desolathya|Rothikgen|Laenor|Furnachia)).*?',flags=re.S)

## 监听的websocket地址
@sockets.route('/location')
def echo_socket(ws):
    global ws_handlers
    print "connected!"
    ws_handlers = [1]
    ws_handlers[0] = ws
    
    while not ws.closed:
        message = ws.receive()
        print "receive:" , message

    print 'sockets disconnected!'  
    ws_handlers = []  

## start websocket
def startWebsocket():
    from gevent import pywsgi
    from geventwebsocket.handler import WebSocketHandler
    server = pywsgi.WSGIServer(('', websocket_port), app, handler_class=WebSocketHandler)
    server.serve_forever()

## action to start ws
thread.start_new_thread(startWebsocket, ())  
print "[realm map] listen at {}".format(websocket_port)
############################### end of websocket


# mysocket=socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# mysocket.connect((MAPPER_ADDRESS, MAPPER_PORT))

class Options:
    def __init__(self, codes, enable_color, enable_combat_plugin):
        self.codes = codes
        self.enable_color = enable_color
        self.enable_combat_plugin = enable_combat_plugin

class Expression:
    def __init__(self):
        self.code = ""
        self.argu = ""
        self.content = ""

class Parser:
    def __init__(self, options):
        self.options = options
        self.stats = S_TEXT
        self.output = ""
        self.stack = []
        # 当收到的数据不完整时，code 很可能被截断，导致前一位数字丢失
        # 因此添加了 tmp_code 变量临时存储前一位，保证 code 始终为两位
        self.tmp_code = ""
        self.expression = None

    def is_valid_code(self, char):
        return char.isdigit() 

    def parse(self, data):
        self.reset()
        return self.process(data)

    def reset(self):
        self.output = ""

    def do_with_text(self, chars):
        if self.expression:
            self.expression.content += chars
        else:
            self.output += chars

    def process(self, data):
        for char in data:
            if self.stats == S_TEXT:
                if char == "\033":
                    self.stats = S_ESC
                else:
                    self.do_with_text(char)
                continue

            if self.stats == S_ESC:
                if char == "<":
                    self.stats = S_TAG_OPEN
                elif char == ">":
                    self.stats = S_TAG_CLOSE
                elif char == "|" and self.expression:
                    self.expression.argu = self.expression.content
                    self.expression.content = ""
                    self.stats = S_TEXT
                else:
                    self.do_with_text("\033" + char)
                    self.stats = S_TEXT
                continue

            if self.stats == S_TAG_OPEN:
                if self.is_valid_code(char):
                    self.tmp_code += char
                    self.stats = S_OPEN_CODE
                else:
                    self.do_with_text("\033" + "<" + char)
                    self.stats = S_TEXT
                continue

            if self.stats == S_OPEN_CODE:
                if self.is_valid_code(char):
                    if self.expression:
                        self.stack.append(self.expression)
                    self.expression = Expression()
                    self.expression.code += self.tmp_code + char
                    self.stats = S_TEXT
                else:
                    self.do_with_text("\033" + "<" + self.tmp_code + char)
                    self.stats = S_TEXT
                self.tmp_code = ""
                continue

            if self.stats == S_TAG_CLOSE:
                if self.is_valid_code(char):
                    self.tmp_code += char
                    self.stats = S_CLOSE_CODE
                else:
                    self.do_with_text("\033" + ">" + char)
                    self.stats = S_TEXT
                continue
            
            if self.stats == S_CLOSE_CODE:
                if self.is_valid_code(char):
                    code = self.tmp_code + char
                    if not self.expression or self.expression.code != code:
                        self.stats = S_TEXT
                    elif self.expression.code == "10" and self.expression.argu == "spec_prompt":
                        self.stats = S_AFTER_TEN
                    else:
                        tmp_content = self.parse_exp(self.expression)
                        self.expression = self.stack.pop() if self.stack else None
                        self.do_with_text(tmp_content)
                        self.stats = S_TEXT
                else:
                    self.do_with_text("\033" + ">" + self.tmp_code + char)
                    self.stats = S_TEXT
                self.tmp_code = ""
                continue

            if self.stats == S_AFTER_TEN:
                if char == "\377":
                    self.stats = S_IAC
                elif char == "\033":
                    self.expression = None
                    self.stats = S_ESC
                else:
                    self.expression = None
                    self.do_with_text(char)
                    self.stats = S_TEXT
                continue

            if self.stats == S_IAC:
                if char == "\371":
                    self.output += self.parse_exp(self.expression)
                    self.expression = None
                    self.stats = S_TEXT
                else:
                    self.expression = self.stack.pop() if self.stack else None
                    self.do_with_text("\377" + char)
                    self.stats = S_TEXT
                continue

        return self.output

    def parse_exp(self, exp):

        global mapperHandler
        if exp.code in self.options.codes or exp.code in ["05", "06", "11", "29", "40", "41", "42"]:
            return ""

        if exp.code in ["22", "23", "24", "25", "31"]:
            return "[-{}-]{}\r\n".format(exp.code, exp.content)
            #return exp.content

        if exp.code == "10":

            ## 查看是否能拿到realm map坐标
            match = pattern.match(exp.content)
            if match and (len(ws_handlers) > 0):
                # 向页面发送坐标
                print '[realm map]send location ', match.group(2)+ ","+match.group(1)
                ws_handlers[0].send(match.group(2)+ ","+match.group(1))

            if exp.argu == "spec_battle" and self.options.enable_combat_plugin:
                return "[10]" + exp.content.replace("\n", " ").strip() + "\r\n"
            elif exp.argu == "spec_prompt":
                return exp.content.strip() +"\r\n"
            elif exp.content == "NoMapSupport":
                return ""
            else:
                return exp.content.strip() +"\r\n"
               
        if exp.code == "20" or exp.code == "21":
            if not self.options.enable_color:
                return exp.content
            elif exp.argu:
                rgb = exp.argu.zfill(6) if len(exp.argu) < 6 else exp.argu
                # fix some invalid RGB color from server
                if len(rgb) > 6:
                    rgb = ''.join(c for c in rgb if c.isdigit())                
                short, _ = colortrans.rgb2short(rgb)
                return "\033[{}8;5;{}m{}\033[0m".format(3 if exp.code == "20" else 4, short, exp.content)

        if exp.code == "99":
            if exp.content.startswith("BAT_MAPPER;;REALM_MAP"):
                return "Exited to realm map.\r\n"
            elif exp.content.startswith("BAT_MAPPER;;"):
                room = exp.content.split(";;")

                ### wind 201801, add code to send socket
                if mapperHandler != None :
                    print "[mapper] send: " + ";;".join(room[1:]) + "@@\n"
                    mapperHandler.send(";;".join(room[1:]) + "@@\n")

                return ""
                ## no need to return
                #return "[-{}-]{}\r\n".format(exp.code, ";;".join(room[1:5] + [room[7]]))

        return "[-{}-]{}\r\n".format(exp.code, exp.content)


if __name__ == '__main__':
    options = Options([], True, True)
    parser = Parser(options)

    with open("logs_debug.txt") as logs:
        content = logs.read()
    #content = """<10spec_map| yest [1m<31north|north>31>10"""
    #content = "<10spec_map|<31north|north>31>10"
    #content = "<10spec_map|123>10"
    #content = """<10spec_map|<200|[32mT>20>10"""
    #content = "<540 0 0>54<10spec_prompt|Hp:301/301 Sp:948/948 Ep:245/245 Exp:147871 >>10<52Zjmee 0 duck 65 1 147871>52<50301 301 948 948 245 245>50"
    #content = """<10spec_map| <200000FF|>20  Massive bronze gate (closed) leads north.>10"""
    #content = """123>20<10spec_map| <200000FF|>20  Massive bronze gate (closed) leads north.>10"""
    #content = """<10spec_map|<200000FF|| >20>20 DATA>20<770000AA|>77  GOOD>10"""
    #content = "<10spec_prompt|Hp:301/301 Sp:963/963 Ep:241/241 Exp:1741 >>10BAAAA"

    data = parser.parse(content)
    print data
