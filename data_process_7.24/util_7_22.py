# -*- coding: utf-8 -*-
"""
Created on Sat Jul 22 20:24:05 2017

@author: DataScience
"""

# -*- coding: utf-8 -*-
"""
Created on Sat Jul 22 20:21:59 2017

@author: DataScience
"""

#保存生成pkl文件所需的字典和列表，初始化

usual_keys = [0, 'holder_num', 2, 3, 4, 5, 6, 1, 8, 9, 'holder_qavgchange_trans',\
              'holder_qavgpctchange', 'holder_avgpctchange', 'holder_top10pct', 7,\
              'holder_havgpctchange_trans', 'holder_liqname', 'holder_name', \
              'holder_havgchange', 'holder_qavgchange', 'holder_avgnum',\
              'holder_top10quantity', 'holder_top10liqquantity', \
              'holder_havgpctchange', 'holder_avgnum_trans', 'holder_avgpctchange_trans',\
              'holder_controller', 'holder_havgchange_trans', 'holder_avgpct_trans',\
              'time', 'holder_avgpct', 'holder_qavgpctchange_trans']
    

usual_dic ={
                0: 'time' ,
                1:  'holder_top10pct'  ,
                2:  'holder_top10quantity'  ,
                3:   'holder_top10liqquantity'  ,
                4:    'holder_controller',
                5:    'holder_name',
                6:    'holder_liqname' ,
                7:   'holder_num' ,
                8:    'holder_avgnum',
                9:    'holder_avgpct',
                10:   'holder_havgpctchange' ,
                11:   'holder_qavgpctchange' ,
                12:   'holder_havgchange' ,
                13:    'holder_qavgchange',
                14:    'holder_avgpctchange',
                15:    'holder_avgnum_trans',
                16:   'holder_avgpct_trans' ,
                17:   'holder_havgpctchange_trans' ,
                18:   'holder_qavgpctchange_trans' ,
                19:    'holder_havgchange_trans',
                20:     'holder_qavgchange_trans',
                21:    'holder_avgpctchange_trans'
            }



top10_holder_keys = ['holder_pct', 'holder_sharecategory', \
                     'holder_liqsharecategory', 'shareholdernature', 'holder_liqpct', \
                     'holder_liqquantity', 'holder_quantity'] 

holder_dic = {
            0 : 'holder_quantity',
            1 : 'holder_pct',
            2 : 'holder_sharecategory',
            3 : 'holder_liqquantity',
            4 : 'holder_liqpct',
            5 : 'holder_liqsharecategory' ,
            6 : 'shareholdernature'
        }
        
        


#Init
def Init(keys):
    sz_shares = dict.fromkeys(keys,{})
    for key in keys:
        sz_shares[key] = dict.fromkeys(usual_keys, {})
        for m in usual_keys :
            
            if m in range(10):
                sz_shares[key][m] = dict.fromkeys(top10_holder_keys, {})
                for j in top10_holder_keys:
                    sz_shares[key][m][j] = []
                    
            else:
                sz_shares[key][m] = []
    return sz_shares



