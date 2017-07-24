# -*- coding: utf-8 -*-
"""
Created on Fri Jul 21 00:48:27 2017

@author: DataScience
"""

# -*- coding: utf-8 -*-
"""
Created on Thu Jul 20 18:17:59 2017

@author: DataScience
"""

#生成上证50支股票，流动股东变化间隔的分布图

import cPickle as cp
import datetime as dt
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

shares = cp.load(open('shares.pkl'))
#res = pd.DataFrame()
res = []

for stock in list(shares.keys()):
    Holder_list = shares[stock]['holder_liqname']
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
   # res = pd.concat([res,res_tmp],axis = 1)
    res= res + list(res_tmp)
#==============================================================================
# res.columns = list(shares.keys())
# res.to_csv('SH50_liq_change.csv')
#==============================================================================
plt.hist(res,bins=40, histtype="stepfilled", color="#CD4F39")
plt.title('SH50_liq',fontsize=30 )
plt.xlabel('Gap',fontsize=20)
plt.ylabel('Num',fontsize=20)
