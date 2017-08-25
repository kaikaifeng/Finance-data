count=0
for file in /home/hadoop/BUAAQuant/Stk_1F/Stk_1F_Format/*.csv
do
	name=$(basename $file .csv)
	mysql -u$1 -p$2 <<EOF
	use data;
	create table if not exists $name(
		datetime datetime not null,
		start float not null,
		max float not null,
		min float not null,
		end float not null,
		amount float not null,
		money float not null	
	);
EOF
	((count=count+1))
	if (($count%1000==0))
	then
		echo "已经完成"$count
	fi
done
