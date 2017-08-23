# -*- coding: utf-8 -*-

import QueryData_MySQL as qd
from datetime import datetime as dt
import pandas as pd

def getStockList(file):
	stocks = [line.replace("\n", "") for line in open(file)]
	
	return stocks

if __name__ == "__main__":
	stocks = getStockList("CompleteLists.txt")
	query = qd.Query(user = 'root', passwd = "BUAAQuant", db = 'data')
	for stock in stocks:
		data = query.QueryDaily_end(dt(2014, 1, 1), dt(2016, 1, 1), stock)
		data.to_csv("output/" + stock + ".csv", index = False, sep = ',')
