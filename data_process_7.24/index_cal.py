# -*- coding: utf-8 -*-
"""
Created on Sun Jul 23 10:32:43 2017

@author: DataScience
"""

#找出 所有股票中股东不含“中国证券金融股份有限公司”的股票名称

import cPickle as cp

#shares = cp.load(open('shares.pkl'))
from t7_23 import sz_shares as shares

print 'load done'

target = u'中国证券金融股份有限公司'

res = []

for key in list(shares.keys()):
    contain_flag = False
    for i in range(1087):
        try:
            tmp_h = shares[key]['holder_name'][i].decode('gbk')
            
        except UnicodeError :
            tmp_h = shares[key]['holder_name'][i].decode('utf-8')
        try:   
            tmp_hl = shares[key]['holder_liqname'][i].decode('gbk')
        except UnicodeError :
            tmp_hl = shares[key]['holder_liqname'][i].decode('utf-8')
        if target in tmp_h or target in tmp_hl:
            contain_flag = True
            break
        
    if contain_flag:
        res.append(key)
        
print res


