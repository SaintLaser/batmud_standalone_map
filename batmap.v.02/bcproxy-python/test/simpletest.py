
print(bytes("asdfasdf", 'utf-8'))

b = b''
b2 = b'ab'
c = b + b2
print( c )

for c in bytes("abc",'utf-8'):
    print( str(c),'c= ', c==98,  c,str(type(c)),  )
    print(str(type(bytes(c))))


a=99
a = bytes([a]) + bytes([a]) + bytes()
print('a is ',a)
print( 'bb', a + a)

print( 'bytesa ', (b'10').decode("utf-8"))
print( 'bytesa ', (b'10')==b'10')

print('ord a= ', ord('a'))

print( "test bytes is number ", (b'1a0').decode("utf-8").isdigit())

print(ord("<"))