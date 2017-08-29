# -*- coding: utf-8 -*-

import cPickle as pickle
from pickle_date import toCSV

shares = []
shares.append(pickle.load(open('shares.pkl')))
shares.append(pickle.load(open('sz_shares.pkl')))
shares.append(pickle.load(open('other_shares.pkl')))

print 'load done'

target = u'中国证券金融股份有限公司'

for share in shares:
    res = []
    for key in list(share.keys()):
        for i in range(1087):
            try:
                tmp_h = share[key]['holder_name'][i].decode('gbk')
            except UnicodeError:
                tmp_h = share[key]['holder_name'][i].decode('utf-8')
            try:
                tmp_hl = share[key]['holder_liqname'][i].decode('gbk')
            except UnicodeError:
                tmp_hl = share[key]['holder_liqname'][i].decode('utf-8')
            if target in tmp_h or target in tmp_hl:
                res.append(key)
                break
    print res
    toCSV(res, 'holder_filtration_' + str(len(share)) + '.csv')

                
