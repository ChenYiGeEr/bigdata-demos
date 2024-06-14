import sys

if (len(sys.argv)) == 1:
	print("Usage: argument's length must > 2")
	exit(1)

# 将参数赋值给code_str
code_str = ' '.join(sys.argv[1:len(sys.argv)])
# print(code_str)

try:
    # 使用 'exec' 模式来编译代码，这样可以对代码进行执行前的检查
    compile(code_str, '<stdin>', 'exec')
except SyntaxError as e:
    print(e)