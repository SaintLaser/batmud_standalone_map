# encoding: UTF-8
import re
 
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
    print 'fire'
    # 使用Match获得分组信息
    print match.group(1),match.group(2)

print '>>>>>>>'