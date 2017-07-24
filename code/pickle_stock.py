import cPickle as pickle
import csv

def construct_name_dic(names):
    returnDic = dict()
    for name in names:
        sp = name.split(';')
        for s in sp:
            if returnDic.has_key(s) == False:
                returnDic[s] = list()
    return returnDic

def toCSV(output, fileName):
    csvFile = open(fileName, 'wb')
    writer = csv.writer(csvFile)
    for enter in output.keys():
        l = [enter]
        l.extend(output[enter])
        writer.writerow(l)
    csvFile.close()

data = pickle.load(open('shares.pkl'))
keys = data.keys()

for stack_num in keys:
    stack = data[stack_num]
    holder_name = stack['holder_name']
    holder_liqname = stack['holder_liqname']
    names = construct_name_dic(holder_name)
    liqnames = construct_name_dic(holder_liqname)
    time = stack['time']
    timeLength = len(time)

    for t in range(0, timeLength):
        hm = holder_name[t]
        hlm = holder_liqname[t]
        sphm = hm.split(';')
        sphlm = hlm.split(';')
        for num in range(0, 10):
            holder = stack[num]
            holder_pct = holder['holder_pct']
            holder_liqpct = holder['holder_liqpct']
            if len(sphm) > num:
                names[sphm[num]].append(holder_pct[t])
            if len(sphlm) > num:
                liqnames[sphlm[num]].append(holder_liqpct[t])
        for enter in names.keys():
            if len(names[enter]) == t:
                names[enter].append(None)
            elif len(names[enter]) < t:
                print 'WRONG!!!'
        for enter in liqnames.keys():
            if len(liqnames[enter]) == t:
                liqnames[enter].append(None)
            elif len(liqnames[enter]) < t:
                print 'WRONG!!!'
    toCSV(names, 'output/' + stack_num + '.csv')
    toCSV(liqnames, 'output/' + stack_num + 'liq.csv')

            
        
        
        
    
