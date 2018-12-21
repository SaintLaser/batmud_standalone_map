
import re

print(bytes("asdfasdf", 'utf-8'))

b = b''
b2 = b'ab'
c = b + b2
print( c )

for c in bytes("abc",'utf-8'):
    print(str(c),'c= ', c == 98, c, str(type(c)), )
    print(str(type(bytes(c))))


a=99
a = bytes([a]) + bytes([a]) + bytes()
print('a is ',a)
print( 'bb', a + a)

print( 'bytesa ', (b'10').decode("utf-8"))
print( 'bytesa ', (b'10')==b'10')


print( '--- ', (b'10\r\n\044aa').decode("utf-8"))

print('ord a= ', ord('a'))

print( "test bytes is number ", (b'1a0').decode("utf-8").isdigit())
print( "nnnn--  ", (b'\33'))

print(ord("<"))

print(  str(3).encode("utf-8"))

print( b'05' in [b"05", b"06", b"11", b"29", b"40", b"41", b"42"])

print('----------------------------------')

# example \r\n\033[1;35mBracelet of Despair\033[0m\r\n
# 32 no aggro
# 31 aggro mob
# 33 Malady blade
# 34 a squat well , A lightweight shovel
# 35 \r\n\033[1;35mBracelet of Despair\033[0m\r\n

pattern_mob_noagg = re.compile(b'\r\n\033\[1;31m(.*)\033\[0m\r\n')
pattern_mob_aggro = re.compile(b'\r\n\033\[1;32m(.*)\033\[0m\r\n')
pattern_weapon =    re.compile(b'\r\n\033\[1;33m(.*)\033\[0m\r\n')
pattern_material =  re.compile(b'\r\n\033\[1;34m(.*)\033\[0m\r\n')
pattern_eq =        re.compile(b'\r\n\033\[1;35m(.*)\033\[0m\r\n')

pattern_fire = re.compile(b'\r\n\033\[1;3([12345]m.*)\033\[0m\r\n')

list =[ b'\r\n\033[1;33mThe Malady blade\033[0m\033[31m <red glow>\033[0m\033[0m\r\n\033[1;33mThe shimmering sword of justice\033[0m\033[31m <red glow>\033[0m\033[0m\r\n', \
    b'\r\n\033[1;35mBracelet of Despair\033[0m\r\n\033[1;35mBracelet of Despair\033[0m\r\n', \
    b'\x1b[0m\r\n\033[1;34ma squat well\033[0m\r\n\033<10spec']

#开始捕捉对象
for ii in list:

    ## 分析当前包含的资料内容
    sth = pattern_fire.findall(ii)
    for w in sth:
        obj_type = ''
        if w[0] == ord('1'): # no aggro mob
            print( 'no aggro=', w[2:].decode('utf-8'))
            obj_type = 'mob_noaggro'
        elif w[0] == ord('2'):
            print( 'aggro =', w[2:].decode('utf-8'))
            obj_type = 'mob_aggro'
        elif w[0] == ord('3'):
            obj_type = 'weapon'
        elif w[0] == ord('4'):
            obj_type = 'material'
        elif w[0] == ord('5'):
            obj_type = 'eq'
        else:
            print( 'i dont recongnize it ')
            obj_type = 'unknown'

        ## analysis object
        obj = w[2:]
        print( obj_type, ':', obj)
        # print( 'sss  ', re.sub(b'a', b'b', b'aaaaa') )
        # print( 'obj  ', type(obj),obj, ii)
        # print( 'replace  ', re.sub(b'The Malady blade\033[0m\033[31m <red glow>\033[0m', b'22', b'The Malady blade\033\[0m\033\[31m <red glow>\033\[0m') )
        # print( 'replace2  ', ii.replace(obj, obj+b'[' + obj_type.encode('utf-8') + b']') )
        ii=ii.replace(obj, obj+b'[' + obj_type.encode('utf-8') + b']')

    print('after ii is: ',ii)

# phone = "2004-959-559 # 这是一个电话号码"
#
# # 删除注释
# num = re.sub(r'#.*$', "", phone)
# print("电话号码 : ", num)
#
# # 移除非数字的内容
# num = re.sub(r'\D', "", phone)
# print("电话号码 : ", num)
#
# print( re.sub(b'aa',b'',b'aabb'))