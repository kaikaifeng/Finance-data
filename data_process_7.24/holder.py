# -*- coding: utf-8 -*-
"""
Created on Thu Jul 20 15:51:13 2017

@author: DataScience
"""
#test

import cPickle as cp
import datetime as dt
import pandas as pd
import numpy as np
import time as t

shares = cp.load(open('sz_shares.pkl'))
begin = t.clock()

print shares['000001'][0]['holder_sharecategory'][0].decode('gbk')

print t.clock() - begin
#==============================================================================
# index = 0
# change_index = []
# 
# for i in Holder_list:
#     if index == 0:
#         index += 1
#         continue
#     elif (i == Holder_list[index-1]) :
#         for j in range(10):
#             if shares['600000'][j]['']
#         index += 1
#         continue
#     else:
#         change_index.append(index)
#         index += 1
#      
# changeT_list = []
# 
# index = 0        
# for j in Time_list:
#     if index in change_index or index == 0:
#         changeT_list.append(dt.datetime.strptime(j,"%Y-%m-%d").date())
#         
#     index += 1
#     
# series = pd.Series(changeT_list)
# dif = series.diff()
# res = (dif[1:] / np.timedelta64(1, 'D')).astype(int)
# print res
#==============================================================================
