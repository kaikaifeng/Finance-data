#-*- coding:utf-8 -*-
import MySQLdb
from datetime import datetime as dt
from pandas import Series
from pandas import DataFrame as df

class Query(object):
	def __init__(self, host='localhost', port=3306, user='root', passwd='', db=''):
		self.conn = MySQLdb.connect(host=host, port=port, user=user, passwd=passwd, db=db)
		self.cur = self.conn.cursor()
	
	def execute(self, sql):
		self.cur.execute(sql)

	def fetchall(self):
		return self.cur.fetchall()



#根据起止日期查询股票数据，分钟级，从每天9.31到11.30，下午1点到3点
#这里把datetime作为行索引，返回一个pandas，列名为[ start , max , min , end , amount , money]
	def Query(self,beginDatetime,endDatetime,stock):
		sql = "Select * From %s where datetime between '%s' and '%s';" %(stock, str(beginDatetime), str(endDatetime))
		self.cur.execute(sql);
		tmp = self.cur.fetchall()
		res = df([i[1:7] for i in tmp],index = [j[0] for j in tmp])
		res.columns = [ 'start' , 'max ', 'min' , 'end ', 'amount' ,' money']
		return res

	def commit(self):
		self.conn.commit()
		
	def closeCursor(self):
		self.cur.close()

	def closeConnect(self):
		self.conn.close()

		
#根据起止日期查询股票数据，天级，返回一个pandas，index和column如Query
	def QueryDaily(self,beginDatetime,endDatetime,stock):
		sql = "Select * From %s where datetime between '%s' and '%s';" %(stock, str(beginDatetime), str(endDatetime))
		self.cur.execute(sql);
		tmp = self.cur.fetchall()
		tmp = filter(lambda x : (x[0].hour == 9 and x[0].minute == 31) , tmp)
		res = df([i[1:7] for i in tmp],index = [j[0].date() for j in tmp])
		res.columns = [ 'start' , 'max ', 'min' , 'end ', 'amount' ,' money']
		return res
		
		
#根据起止日期查询股票每天的收盘价，返回结果为pandas的DtaFrame,行索引为datetime,列名为股票名
	def QueryDaily_end(self,beginDate,endDate,stock):
		sql = "Select * From %s where datetime between '%s' and '%s';" %(stock, str(beginDate.date())+" 00:00:00", str(endDate.date())+" 23:59:59")
		self.cur.execute(sql);
		tmp = self.cur.fetchall()
		res = {}		

		filtered = filter(lambda x : (x[0].hour == 15 and x[0].minute == 0) , tmp)
		for ele in filtered:
			res[ele[0].date()] = ele[4]
		result = df({stock:Series(res)})

		return result

if __name__ == '__main__':
	query = Query(user='root', passwd="BUAAQuant", db='data')	#在本地运行的话可以加上 host = '219.224.169.45'，建议还是在服务器上跑
	#几个例子，跑一下看下每个查询函数的输出结果
	print query.Query(dt(2014,1,10,9,30),dt(2014,1,10,15,0),'SH600000')
	print query.QueryDaily(dt(2014,1,1),dt(2014,1,10),'SH600000')
	print query.QueryDaily_end(dt(2014,1,1),dt(2014,1,10),'SH600000')
	query.closeCursor()
	query.closeConnect()


