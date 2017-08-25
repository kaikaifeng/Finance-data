count=0
for file in /home/hadoop/BUAAQuant/Stk_1F/Stk_1F_Format/*.csv
do
	echo 19910501|mysqlimport --local --fields-terminated-by=',' --fields-optionally-enclosed-by='"' --lines-terminated-by='\n' --ignore-lines=0 -h localhost -P 3306 -p$1 --replace data $file
	((count=count+1))
	if (($count%1000==0))
	then
		echo "已经完成"$count"条数据导入"
	fi
done


