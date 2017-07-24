# -*- coding: utf-8 -*-
"""
Created on Sun Jul 23 10:15:58 2017

@author: DataScience
"""

# -*- coding: utf-8 -*-
"""
Created on Thu Jul 20 21:08:17 2017

@author: DataScience
"""



import os
from util_7_22 import *
import cPickle as cp

pathDir = os.listdir("F:\\50SH\\300holder")
keys = map(lambda x:x.split('.')[0],pathDir)

count = 0
sz_shares = Init(keys)



for i in pathDir:
    path = "F:\\50SH\\300holder\\"+i 
    tmp_key = i.split('.')[0]
    with open(path) as f:

        for line in f.readlines():
  
            line_spl = line.split('|')

            for j in range (22):
                sz_shares[tmp_key][usual_dic[j]].append(line_spl[j])

            for h in range (10):
                for l in range(7):
                    sz_shares[tmp_key][h][holder_dic[l]].append(line_spl[7*h + l + 22])
                    
    count += 1
    print count
 









   
# 保存
#==============================================================================
# with open("sz_shares.pkl","wb") as f_s:
#     cp.dump(sz_shares,f_s)
#==============================================================================


#==============================================================================
# for k in top10_holder_keys:
#     print k
#     print sz_shares['000001'][0][k][1086].decode('gbk')
#==============================================================================
  
    


        
        
      
#==============================================================================
#         sz_shares[key] = {}
#         sz_shares[key]['holder_avgnum'] = []
#         sz_shares[key]['holder_avgnum_trans'] = []
#         sz_shares[key]['holder_avgpct'] = []
#         sz_shares[key]['holder_avgpct_trans'] = []
#         sz_shares[key]['holder_avgpctchange'] = []
#         sz_shares[key]['holder_avgpctchange_trans'] = []
#         sz_shares[key]['holder_controller'] = []
#         sz_shares[key]['holder_havgchange'] = []
#         sz_shares[key]['holder_havgchange_trans'] = []
#         sz_shares[key]['holder_havgpctchange'] = []
#         sz_shares[key]['holder_havgpctchange_trans'] = []
#         sz_shares[key]['holder_liqname'] = []
#         sz_shares[key]['holder_name'] = []
#         sz_shares[key]['holder_num'] = []
#         sz_shares[key]['holder_qavgchange'] = []
#         sz_shares[key]['holder_qavgchange_trans'] = []
#         sz_shares[key]['holder_qavgpctchange'] = []
#         sz_shares[key]['holder_qavgpctchange_trans'] = []
#         sz_shares[key]['holder_top10liqquantity'] = []
#         sz_shares[key]['holder_top10pct'] = []
#         sz_shares[key]['holder_quantity'] = []
#         sz_shares[key]['holder_time'] = []
#         
#==============================================================================
        
        
        
        