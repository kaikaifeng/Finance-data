for file in /home/hadoop/BUAAQuant/Stk_1F/Stk_1F_Format/*.csv
do
	name=$(basename $file .csv)
	echo $name
	mysql -uroot -p19910501 <<EOF
	use data;
	select COLUMN_NAME from information_schema.COLUMNS where table_name = '$name' and table_schema = 'data';
EOF
done
