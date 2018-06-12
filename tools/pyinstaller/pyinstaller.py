import  os
file = input("请输入需要打包成exe文件的python文件名")
if __name__ == '__main__':
    from PyInstaller.__main__ import run
    opts=[file+'.py','-F']
    run(opts)
