#!/bin/bash
##################################################
# AUTHOR: Neo <netkiller@msn.com>
# WEBSITE: http://www.netkiller.cn
# Description：zabbix 通过 status 模块监控 php-fpm
# Note：Zabbix 3.2
# DateTime: 2016-11-22
##################################################

HOST="localhost"
PORT="80"
status="status"

function query() {
	curl -s http://${HOST}:${PORT}/${status} | grep "$1" | cut -d : -f 2 | awk '{print $1}'
}

if [ $# == 0 ]; then
		echo $"Usage $0 {check|active|accepts|handled|requests|reading|writing|waiting}"
		exit	
else		
	query "$1"
fi