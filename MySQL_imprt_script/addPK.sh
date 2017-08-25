while read line
do
	mysql -uroot -p$1 << EOF
	use data;
	alter table $line add primary key(datetime);
EOF
done < 'stocks.txt'
