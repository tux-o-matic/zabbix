Service Dependency Check
=====

    chmod +x /srv/zabbix/libexec/dependency
    
    [root@netkiller zabbix_agentd.d]# /srv/zabbix/libexec/dependency -h
    /srv/zabbix/libexec/dependency - Software dependency check
        author neo <netkiller@msn.com>
        -d/--discovery
        -p/--ping 192.168.0.1/www.netkiller.cn
        -c/--check 192.186.0.1 80
        -h/--help
        
   zabbix_get -s 10.24.15.18 -k 'dependency.discovery'
