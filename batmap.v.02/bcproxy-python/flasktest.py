from flask import Flask
from flask_sockets import Sockets
import random
import time
import thread

ws_handlers =[]
app = Flask(__name__)
sockets = Sockets(app)

@sockets.route('/location')
def echo_socket(ws):
    global ws_handlers
    print "connected!"
    ws_handlers = [1]
    ws_handlers[0] = ws
    
    print 'len connected ',len(ws_handlers)
    while not ws.closed:
        message = ws.receive()
        print "receive:" , message

    print 'sockets disconnected!'  
    ws_handlers = []  

def genData():  
	global ws_handlers
	while 1:
		if len(ws_handlers) > 0:
		    a = random.random()
		    print 'gen ', a
		    ws_handlers[0].send( str(a) )
		
		time.sleep(1)

	print 'wait 2s '
	time.sleep(2)

if __name__ == "__main__":
	thread.start_new_thread(genData, ())  
	from gevent import pywsgi
	from geventwebsocket.handler import WebSocketHandler
	server = pywsgi.WSGIServer(('', 10001), app, handler_class=WebSocketHandler)
	server.serve_forever()


