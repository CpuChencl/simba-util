from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
import pymysql
import os
import codecs
import natsort

"""
    自动建数据库的工具:
            跟各种.sql文件放在同一目录, 运行该脚本即可, 输入新的数据库名称然后回车即可建库

"""

HOST = '127.0.0.1'
USER = 'root'
PASSWORD = 'ut123456'
PORT = 3306

DEL_TABLE = 'DROP TABLE %s'
DEL_DATABASE = 'DROP DATABASE %s'


def delete_database(name):
    """
    CREATE DATABASE name
    :param name: 数据库的名字
    :return:
    """
    try:
        conn = pymysql.connect(host=HOST, user=USER, passwd=PASSWORD, port=PORT, charset='utf8')
        cur = conn.cursor()
        cur.execute(DEL_DATABASE%(name))
        cur.close()
        conn.commit()
        conn.close()
    except Exception as e:
        print(e)

def create_database(name):
    """
    CREATE DATABASE name
    :param name: 数据库的名字
    :return:
    """
    try:
        conn = pymysql.connect(host=HOST, user=USER, passwd=PASSWORD, port=PORT, charset='utf8')
        cur = conn.cursor()
        cur.execute('CREATE DATABASE ' + name)
        cur.close()
        conn.commit()
        conn.close()
    except Exception as e:
        print(e)


def get_mysql_session(user, password, ip, port, db_name):
    """
    建立连接, 返回数据库连接对象, 用于增删改
    """
    DB_CON_STR = 'mysql+pymysql://%s:%s@%s:%s/%s?charset=utf8mb4' % (user, password, ip, port, db_name)
    print(DB_CON_STR)
    engine = create_engine(DB_CON_STR, echo=False)
    DBSession = sessionmaker(bind=engine)
    return DBSession()


def execute_sqls(mysession, sqls):
    """
    单句sql或者多句sql都行: input: str or list or tuple
    :param mysession:
    :param sqls:
    :return:
    """
    if isinstance(sqls, str):  # 单句sql直接执行
        mysession.execute(sqls)
    elif isinstance(sqls, list):  # 多句sql遍历执行
        for sql in sqls:
            mysession.execute(sql)
    elif isinstance(sqls, tuple):  # 多句sql遍历执行
        for sql in sqls:
            mysession.execute(sql)
    mysession.commit()


def get_suffixfiles_fullpath(suffix):
    """
    获取当前目录下所有.xxx文件的路径 (路径列表自然排序)
    :param suffix: 后缀如".sql" ".java"
    :return: list of str
    """
    sql_files = list(filter(lambda x: x.endswith(suffix), os.listdir(os.getcwd())))
    sqlFilesFullPath = list(map(lambda x: os.getcwd() + '\\' + x, sql_files))
    return natsort.natsorted(sqlFilesFullPath)


def get_file_content(file_path):
    """
    读取文件, 暂时只支持utf8和gbk编码的文件, 自动去除BOM
    :param file_path:
    :return: str
    """
    try:
        with open(file_path, encoding='utf-8') as f1:
            raw = f1.read()
            # 去掉BOM
            bom_head = raw.encode(encoding='utf-8')[:3]
            if bom_head == codecs.BOM_UTF8:
                raw = raw.encode(encoding='utf-8')[3:].decode(encoding='utf-8')
            return raw
    except Exception as e:
        with open(file_path, encoding='GBK') as f2:
            return f2.read()


def auto_create(name):
    print('数据库初始化中...')
    # delete_database(name)     # 删库太危险,暂时屏蔽
    create_database(name)
    mysession = get_mysql_session(USER, PASSWORD, HOST, PORT, name)
    for x in get_suffixfiles_fullpath('.sql'):
        path,filename = os.path.split(x)
        print('执行SQL文件:',filename,"...")
        sql = get_file_content(x)
        execute_sqls(mysession, sql)
    mysession.close()


def run_with_cmd():
    global USER
    global PASSWORD
    global HOST
    global PORT
    database_name = input('数据库名:')
    if database_name != '':
        mysql_ip = input('ip(默认127.0.0.1):')
        mysql_port = input('port(默认3306):')
        username = input('用户(默认root):')
        pwd = input('密码(默认ut123456):')
        if mysql_ip != '':
            HOST = mysql_ip
        if mysql_port != '':
            PORT = mysql_port
        if username != '':
            USER = username
        if pwd != '':
            PASSWORD = pwd
        auto_create(database_name)
        print('**********已完成!*********')
        os.system('pause')


if __name__ == '__main__':
    run_with_cmd()
