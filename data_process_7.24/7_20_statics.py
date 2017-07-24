# -*- coding: utf-8 -*-
"""
Created on Thu Jul 20 18:17:59 2017

@author: DataScience
"""
#生成 股东变化间隔分布图，可根据需要更改输入和关键字段

import cPickle as cp
import datetime as dt
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from t7_23 import sz_shares as shares    #深证
#shares = cp.load(open('shares.pkl'))    #上证
#res = pd.DataFrame()
res = []

sns.set_palette('deep', desat=.6)  
sns.set_context(rc={'figure.figsize': (8, 5) } )  

for stock in list(shares.keys()):
    Holder_list = shares[stock]['holder_liqname']   #可更改holder_liqname为holder_name
    Time_list = shares[stock]['time']

    index = 0
    change_index = []
    
    for i in Holder_list:
        if index == 0:
            index += 1
            continue
        elif (i == Holder_list[index-1]) :
            index += 1
            continue
        else:
            change_index.append(index)
            index += 1
         
    changeT_list = []
    
    index = 0        
    for j in Time_list:
        if index in change_index or index == 0:
            changeT_list.append(dt.datetime.strptime(j,"%Y-%m-%d").date())
            
        index += 1
        
    series = pd.Series(changeT_list)
    dif = series.diff()
    res_tmp = (dif[1:] / np.timedelta64(1, 'D')).astype(int)
    res = res + list(res_tmp)
    
#res.columns = list(shares.keys())
#res.to_csv('SH50_change.csv')
plt.hist(res,bins=40, histtype="stepfilled", color="#CD4F39")
plt.title('SZ300_liq',fontsize=30 )
plt.xlabel('Gap',fontsize=20)
plt.ylabel('Num',fontsize=20)

